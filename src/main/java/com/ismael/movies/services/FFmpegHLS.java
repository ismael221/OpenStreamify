package com.ismael.movies.services;

import com.ismael.movies.config.MinioConfig;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class FFmpegHLS {

    @Autowired
    MinioClient minioClient;
    @Autowired
    MinioConfig minioConfig;

    private static final Logger logger = LoggerFactory.getLogger(FFmpegHLS.class);
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public FFmpegHLS() {
    }

    public Future<Integer> executeFFmpegCommand(String inputFilePath, UUID ridFilme) {
        return executorService.submit(() -> {

            // Definir o diretório temporário local para salvar os segmentos HLS
            String tempDir = System.getProperty("java.io.tmpdir") + "/hls/" + ridFilme + "/";
            Files.createDirectories(Paths.get(tempDir)); // Certifique-se de que o diretório existe

            // Caminhos de arquivos locais
            String m3u8FilePath = tempDir + ridFilme + ".m3u8";
            String tsFilePattern = tempDir + ridFilme + "_%03d.ts";

            // Comando FFmpeg para gerar HLS localmente
            String[] command = {
                    "ffmpeg",
                    "-i", inputFilePath,            // Caminho do arquivo de entrada
                    "-c:v", "copy",                 // Copia o vídeo sem alterações
                    "-c:a", "libmp3lame",           // Converte o áudio para MP3 usando libmp3lame
                    "-b:a", "320k",                 // Define a taxa de bits do áudio (320 kbps)
                    "-start_number", "0",           // Inicia a numeração dos segmentos em 0
                    "-hls_time", "10",              // Duração dos segmentos em segundos
                    "-hls_list_size", "0",          // Mantém todos os segmentos na lista
                    "-hls_segment_filename", tsFilePattern,  // Nome dos segmentos localmente
                    m3u8FilePath                   // Nome do arquivo de índice m3u8 localmente
            };

            // Criando o ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            try {
                // Iniciar o processo FFmpeg
                Process process = processBuilder.start();

                // Lendo a saída do processo
                InputStream inputStream = process.getInputStream();
                int c;
                while ((c = inputStream.read()) != -1) {
                    System.out.print((char) c);
                }

                // Esperar o processo finalizar
                int exitCode = process.waitFor();
                logger.info("Processo FFmpeg finalizado com código: {}", exitCode);

                if (exitCode == 0) {
                    // Fazer upload dos arquivos gerados para o MinIO
                    uploadFilesToMinIO(tempDir, ridFilme);

                    // Remover os arquivos locais após o upload
                    cleanUpLocalFiles(tempDir);
                }

                return exitCode;

            } catch (IOException | InterruptedException e) {
                logger.error("Erro ao executar o comando FFmpeg: {}", e.getMessage());
                return -1;
            }
        });
    }

    // Método para fazer o upload dos arquivos gerados para o MinIO
    private void uploadFilesToMinIO(String tempDir, UUID ridFilme) throws Exception {
        File dir = new File(tempDir);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".ts") || name.endsWith(".m3u8"));

        if (files != null) {
            for (File file : files) {
                try (InputStream stream = new FileInputStream(file)) {
                   // String objectName = "hls/" + ridFilme + "/" + file.getName();
                    String objectName = "hls/" + file.getName();
                    minioClient.putObject(
                            PutObjectArgs.builder()
                                    .bucket(minioConfig.getStreamBucket())
                                    .object(objectName)
                                    .stream(stream, file.length(), -1)
                                    .contentType("application/vnd.apple.mpegurl")
                                    .build());
                    logger.info("Arquivo {} enviado para o MinIO.", file.getName());
                }
            }
        }
    }

    // Método para limpar os arquivos locais após o upload
    private void cleanUpLocalFiles(String tempDir) throws IOException {
        File dir = new File(tempDir);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    Files.deleteIfExists(file.toPath());
                }
            }
        }
        Files.deleteIfExists(Paths.get(tempDir));  // Remove o diretório temporário
    }

    // Método para encerrar o pool de threads
    public void shutdown() {
        executorService.shutdown();
    }
}
