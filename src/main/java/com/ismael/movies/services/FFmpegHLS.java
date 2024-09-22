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
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class FFmpegHLS {

    @Autowired
    MinioClient minioClient;
    @Autowired
    MinioConfig minioConfig;

    private static final Logger logger = LoggerFactory.getLogger(FFmpegHLS.class);
    private final Path baseLocation = Paths.get("videos/hls"); // Caminho relativo

    // Pool de threads para processar os comandos em paralelo
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public FFmpegHLS(){
    }


    public Future<Integer> executeFFmpegCommand(String inputFilePath, UUID ridFilme) {
        return executorService.submit(() -> {
            Path absoluteBaseLocation = Paths.get(System.getProperty("user.dir")).resolve(baseLocation);

            Path segmentFilename = absoluteBaseLocation.resolve(ridFilme + "_%03d.ts");
            Path m3u8Filename = absoluteBaseLocation.resolve(ridFilme + ".m3u8");

            // Comando FFmpeg para gerar HLS
            String[] command = {
                    "ffmpeg",
                    "-i", inputFilePath,            // Caminho do arquivo de entrada
                    "-c:v", "copy",                 // Copia o vídeo sem alterações
                    "-c:a", "libmp3lame",             // Converte o áudio para MP3 usando libmp3lame
                    "-b:a", "320k",                       // Define a taxa de bits do áudio (128 kbps)
                    "-start_number", "0",           // Inicia a numeração dos segmentos em 0
                    "-hls_time", "10",              // Duração dos segmentos em segundos
                    "-hls_list_size", "0",          // Mantém todos os segmentos na lista
                    "-hls_segment_filename", segmentFilename.toString(), // Nome dos segmentos
                    m3u8Filename.toString()         // Nome d
            };

            // Criando o ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // Redirecionando o fluxo de erro para o fluxo de saída padrão
            processBuilder.redirectErrorStream(true);

            try {
                // Iniciando o processo
                Process process = processBuilder.start();

                // Lendo a saída do processo
                InputStream inputStream = process.getInputStream();
                int c;
                while ((c = inputStream.read()) != -1) {
                    System.out.print((char) c);
                }

                // Esperando o processo terminar
                int exitCode = process.waitFor();
                logger.info("Processo FFmpeg finalizado com código: {}", exitCode);
                if (exitCode == 0) {
                    // Arquivos gerados com sucesso, agora faça upload para o MinIO
                    uploadToMinIO(ridFilme, absoluteBaseLocation);
                }

                return exitCode; // Retorna o código de saída

            } catch (IOException | InterruptedException e) {
                logger.error("Erro ao executar o comando FFmpeg: {}", e.getMessage());
                return -1; // Retorna um código de erro em caso de exceção
            }
        });
    }
    private void uploadToMinIO(UUID ridFilme, Path baseLocation) throws IOException {
        //String bucketName = "openstreamify";  // Nome do bucket no MinIO
        Path m3u8File = baseLocation.resolve(ridFilme + ".m3u8");
        Path tsFilePattern = baseLocation.resolve(ridFilme + "_%03d.ts");

        try {

            // Continue com o upload dos arquivos
            // Fazer upload do arquivo .m3u8
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getStreamBucket())
                            //.object("hls/" + ridFilme + "/" + ridFilme + ".m3u8")  // Caminho no MinIO
                            .object("hls/"+ ridFilme + ".m3u8")
                            .stream(Files.newInputStream(m3u8File), Files.size(m3u8File), -1)
                            .contentType("application/x-mpegURL")
                            .build()
            );

            // Fazer upload de todos os arquivos .ts gerados (usando padrão %03d)
            int i = 0;
            while (true) {
                Path tsFile = baseLocation.resolve(String.format(ridFilme + "_%03d.ts", i));
                if (!Files.exists(tsFile)) {
                    break;  // Sai do loop se não houver mais arquivos
                }

                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(minioConfig.getStreamBucket())
                               // .object("hls/" + ridFilme + "/" + tsFile.getFileName())
                                .object("hls/" + tsFile.getFileName())  // Caminho no MinIO
                                .stream(Files.newInputStream(tsFile), Files.size(tsFile), -1)
                                .contentType("video/MP2T")
                                .build()
                );
                i++;
            }

            logger.info("Arquivos HLS carregados com sucesso no MinIO!");

        } catch (Exception e) {
            logger.error("Erro ao fazer upload para o MinIO: {}", e.getMessage());
            throw new IOException("Falha ao carregar arquivos para o MinIO", e);
        }
    }

    // Método para encerrar o pool de threads quando não for mais necessário
    public void shutdown() {
        executorService.shutdown();
    }
}
