package com.ismael.movies.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FFmpegHLS {

    private  static  final Logger logger = LoggerFactory.getLogger(FFmpegHLS.class);

    private final Path baseLocation = Paths.get("videos/hls"); // Caminho relativo

    public void executeFFmpegCommand(String inputFilePath, UUID ridFilme) {

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
            System.out.println("Processo finalizado com código: " + exitCode);
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
