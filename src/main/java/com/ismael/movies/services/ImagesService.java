package com.ismael.movies.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImagesService {

    private  static  final Logger logger = LoggerFactory.getLogger(ImagesService.class);

    // Configuração do diretório de upload
    private final Path uploadDir = Paths.get("uploads");

    // URL base para acessar os arquivos
    @Value("${server.url}")
    private String serverUrl;

    public ImagesService() throws IOException {
        // Cria o diretório de upload se não existir
        Files.createDirectories(uploadDir);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Gera um nome de arquivo único
        String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Path filePath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Retorna a URL da imagem  return serverUrl + "/api/v1/media/img/" + filename;
        return serverUrl + "/api/v1/media/img/" + filename;
    }

    public Resource getFile(String filename) throws MalformedURLException, IOException {
        Path filePath = uploadDir.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            logger.error("Arquivo não encontrado "+filename);
            throw new RuntimeException("Arquivo não encontrado: " + filename);
        }
    }
}
