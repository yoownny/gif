package com.example.gif_converter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class VideoService {
    private final VideoToGifConverter converter;
    private final Path tempDir;

    public VideoService(VideoToGifConverter converter) {
        this.converter = converter;
        this.tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
    }

    public String convertVideo(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;

        Path inputPath = tempDir.resolve(uniqueFilename);
        Path outputPath = tempDir.resolve(uniqueFilename.replaceFirst("[.][^.]+$", "") + ".gif");

        Files.copy(file.getInputStream(), inputPath);

        try {
            converter.convertVideoToGif(inputPath.toString(), outputPath.toString());
            return outputPath.getFileName().toString();
        } finally {
            Files.deleteIfExists(inputPath);
        }
    }
}