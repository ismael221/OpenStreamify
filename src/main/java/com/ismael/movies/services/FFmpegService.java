package com.ismael.movies.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    final
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

            // Set local temp directory to save HLS segments
            String tempDir = System.getProperty("java.io.tmpdir") + "/hls/" + fileName + "/";
            Files.createDirectories(Paths.get(tempDir)); //Make sure the directory exists

            // Local file paths
            String m3u8FilePath = tempDir + fileName + ".m3u8";
            String tsFilePattern = tempDir + fileName + "_%03d.ts";

            // FFmpeg command to generate HLS locally
            String[] command = {
                    "ffmpeg",
                    "-i", destFile.getAbsolutePath(),            // Input file path
                    "-c:v", "copy",                 // Copy the video without changes
                    "-c:a", "libmp3lame",           // Convert audio to MP3 using libmp3lame
                    "-b:a", "320k",                 // Sets the audio bitrate (320 kbps)
                    "-start_number", "0",           // Start segment numbering at 0
                    "-hls_time", "10",              // Duration of segments in seconds
                    "-hls_list_size", "0",          // Keeps all segments in the list
                    "-hls_segment_filename", tsFilePattern,  //Name of segments locally
                    m3u8FilePath                   // m3u8 index file name locally
            };

            // Creating the ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            try {
                // Start the FFmpeg process
                Process process = processBuilder.start();

                // Reading process output
                InputStream inputStream = process.getInputStream();
                int c;
                while ((c = inputStream.read()) != -1) {
                    System.out.print((char) c);
                }

                // Wait for the process to finish
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

    public static String removeExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        // If it finds a point, returns the substring before it; otherwise, returns the original name
        return (lastDotIndex == -1) ? fileName : fileName.substring(0, lastDotIndex);
    }
}
