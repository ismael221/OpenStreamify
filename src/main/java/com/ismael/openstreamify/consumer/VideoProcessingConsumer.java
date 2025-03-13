package com.ismael.openstreamify.consumer;

import com.ismael.openstreamify.config.RabbitMQConfig;
import com.ismael.openstreamify.services.FFmpegService;
import com.ismael.openstreamify.services.MinioQueueService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class VideoProcessingConsumer {

    private static final Logger logger = LoggerFactory.getLogger(VideoProcessingConsumer.class);

    private final FFmpegService fFmpegService;
    private final MinioQueueService minioQueueService;

    @RabbitListener(queues = RabbitMQConfig.VIDEO_PROCESSING_QUEUE)
    public void processVideo(String videoPath) throws ExecutionException, InterruptedException {
        Path path = Paths.get(videoPath);
        String fileName = path.getFileName().toString();
        Future<Integer> result =  fFmpegService.executeFFmpegCommand(fileName);
        if (result.get() != -1){
            String nameWithoutExtension = removeExtension(fileName);
            minioQueueService.sendToMinioUploadingQueue(nameWithoutExtension);
        }
    }

    public static String removeExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex == -1) ? fileName : fileName.substring(0, lastDotIndex);
    }
}
