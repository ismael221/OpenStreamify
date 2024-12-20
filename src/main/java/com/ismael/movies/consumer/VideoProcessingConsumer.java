package com.ismael.movies.consumer;

import com.ismael.movies.config.RabbitMQConfig;
import com.ismael.movies.services.FFmpegService;
import com.ismael.movies.services.MinioQueueService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class VideoProcessingConsumer {

    @Autowired
    FFmpegService fFmpegService;

    @Autowired
    MinioQueueService minioQueueService;

    @RabbitListener(queues = RabbitMQConfig.ALERT_QUEUE)
    public void processVideo(String videoPath) throws ExecutionException, InterruptedException {
        Path path = Paths.get(videoPath);
        String fileName = path.getFileName().toString();
        Future<Integer> result =  fFmpegService.executeFFmpegCommand(fileName);
        if (result.get() != -1){
            String nameWithoutExtension = removeExtension(fileName);
            minioQueueService.sendToMinioUploadindQueue(nameWithoutExtension);
        }
    }

    public static String removeExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex == -1) ? fileName : fileName.substring(0, lastDotIndex);
    }
}
