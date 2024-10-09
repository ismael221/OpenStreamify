package com.ismael.movies.exception;

import com.ismael.movies.model.Exceptions.BadRequestException;
import com.ismael.movies.model.Exceptions.ProblemDetails;
import com.ismael.movies.model.Exceptions.ResourceNotFoundException;
import com.ismael.movies.services.HlsService;
import com.ismael.movies.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ProblemDetails problemDetails = new ProblemDetails(
                URI.create("https://example.com/resource-not-found").toString(),
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
                URI.create("https://example.com/internal-server-error").toString(),
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
                URI.create("https://example.com/bad-request-error").toString(),
                "Bad Request Error",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                    request.getDescription(false)
        );
        return new ResponseEntity<>(problemDetails, HttpStatus.BAD_REQUEST);
    }

}
