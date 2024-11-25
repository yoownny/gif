package com.example.gif_converter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class VideoService {
    private final VideoToGifConverter converter;
    private final Path tempDir;

    public VideoService(VideoToGifConverter converter) {
        this.converter = converter;
        this.tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
    }

    public String convertVideo(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
        String outputName = baseName + ".gif";

        Path inputPath = tempDir.resolve(originalName);
        Path outputPath = tempDir.resolve(outputName);

        Files.copy(file.getInputStream(), inputPath);

        try {
            converter.convertVideoToGif(inputPath.toString(), outputPath.toString());
            return outputName;
        } finally {
            Files.deleteIfExists(inputPath);
        }
    }
}