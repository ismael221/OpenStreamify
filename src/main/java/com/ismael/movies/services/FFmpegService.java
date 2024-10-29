package com.ismael.movies.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class FFmpegService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(FFmpegService.class);
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public FFmpegService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    public Future<Integer> executeFFmpegCommand(String file) {
        return executorService.submit(() -> {

            String fileName = removeExtension(file);

            String uploadDir = System.getProperty("java.io.tmpdir")  + File.separator + "raw";

            File directory = new File(uploadDir);

            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    throw new IOException("Failed to create the upload directory.");
                }
            }

            File destFile = new File(uploadDir + File.separator + file);

            // Definir o diretório temporário local para salvar os segmentos HLS
            String tempDir = System.getProperty("java.io.tmpdir") + "/hls/" + fileName + "/";
            Files.createDirectories(Paths.get(tempDir)); // Certifique-se de que o diretório existe

            // Caminhos de arquivos locais
            String m3u8FilePath = tempDir + fileName + ".m3u8";
            String tsFilePattern = tempDir + fileName + "_%03d.ts";

            // Comando FFmpeg para gerar HLS localmente
            String[] command = {
                    "ffmpeg",
                    "-i", destFile.getAbsolutePath(),            // Caminho do arquivo de entrada
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

                return exitCode;

            } catch (IOException | InterruptedException e) {
                logger.error("Erro ao executar o comando FFmpeg: {}", e.getMessage());
                return -1;
            }
        });
    }
    //TODO add some monitoring on minio using prometheus in order to notify a rabbitMq queue, when the bucket is offline/online, allowing asincrous uploads


    // Método para encerrar o pool de threads
    public void shutdown() {
        executorService.shutdown();
    }

    public static String removeExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        // Se encontrar um ponto, retorna a substring antes dele; caso contrário, retorna o nome original
        return (lastDotIndex == -1) ? fileName : fileName.substring(0, lastDotIndex);
    }
}
