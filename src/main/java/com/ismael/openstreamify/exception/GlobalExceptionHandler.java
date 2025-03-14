package com.ismael.openstreamify.exception;

import com.ismael.openstreamify.model.Exceptions.BadRequestException;
import com.ismael.openstreamify.model.Exceptions.MediaNotProcessedException;
import com.ismael.openstreamify.model.Exceptions.ProblemDetails;
import com.ismael.openstreamify.model.Exceptions.ResourceNotFoundException;
import com.ismael.openstreamify.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    final
    NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });
        problemDetail.setProperty("fieldErrors", fieldErrors);
        return problemDetail;
    }

    @ExceptionHandler(MediaNotProcessedException.class)
    public ResponseEntity<ProblemDetails> handleMediaNotProcessedException(MediaNotProcessedException ex,WebRequest request){
        ProblemDetails problemDetails = new ProblemDetails(
                Instant.now(),
                "Not possible to process media",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(problemDetails, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ProblemDetails problemDetails = new ProblemDetails(
                Instant.now(),
                "Resource Not Found",
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(problemDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGlobalException(Exception ex, WebRequest request) {
        ProblemDetails problemDetails = new ProblemDetails(
                Instant.now(),
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getDescription(false)
        );
        String mensagemPersonalizada = "🚨 *Erro não tratado* 🚨\n\n" +
                "*Título*: " + "Verifique os logs" + "\n" +
                "*Status*: " + HttpStatus.INTERNAL_SERVER_ERROR + "\n" +
                "*Mensagem*: " + ex.getMessage() + "\n";

        notificationService.enviarMensagemTelegram(mensagemPersonalizada);

        return new ResponseEntity<>(problemDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ProblemDetails> handleBadRequestException(Exception ex, WebRequest request) {
        ProblemDetails problemDetails = new ProblemDetails(
                Instant.now(),
                "Bad Request Error",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                    request.getDescription(false)
        );
        return new ResponseEntity<>(problemDetails, HttpStatus.BAD_REQUEST);
    }

}
