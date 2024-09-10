package com.ismael.movies.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class FFmpegHLS {

    private static final Logger logger = LoggerFactory.getLogger(FFmpegHLS.class);
    private final Path baseLocation = Paths.get("videos/hls"); // Caminho relativo

    // Pool de threads para processar os comandos em paralelo
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public Future<Integer> executeFFmpegCommand(String inputFilePath, UUID ridFilme) {
        return executorService.submit(() -> {
            Path absoluteBaseLocation = Paths.get(System.getProperty("user.dir")).resolve(baseLocation);

            Path segmentFilename = absoluteBaseLocation.resolve(ridFilme + "_%03d.ts");
            Path m3u8Filename = absoluteBaseLocation.resolve(ridFilme + ".m3u8");

            // Comando FFmpeg para gerar HLS
            String[] command = {
                    "ffmpeg",
                    "-i", inputFilePath,
                    "-c:v", "copy",
                    "-c:a", "copy",
                    "-start_number", "0",
                    "-hls_time", "10",
                    "-hls_list_size", "0",
                    "-hls_segment_filename", segmentFilename.toString(),
                    m3u8Filename.toString()
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
                return exitCode; // Retorna o código de saída

            } catch (IOException | InterruptedException e) {
                logger.error("Erro ao executar o comando FFmpeg: {}", e.getMessage());
                return -1; // Retorna um código de erro em caso de exceção
            }
        });
    }

    // Método para encerrar o pool de threads quando não for mais necessário
    public void shutdown() {
        executorService.shutdown();
    }
}
