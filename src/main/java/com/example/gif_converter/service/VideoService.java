package com.example.gif_converter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// 비디오 변환 관련 비즈니스 로직을 처리하는 서비스
@Service
public class VideoService {
    private final VideoToGifConverter converter;
    // 임시 파일 저장 디렉토리
    private final Path tempDir;

    public VideoService(VideoToGifConverter converter) {
        this.converter = converter;
        this.tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
    }

    public String convertVideo(MultipartFile file) throws IOException {
        // 원본 파일명에서 확장자를 제외한 이름 추출
        String originalName = file.getOriginalFilename();
        String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
        String outputName = baseName + ".gif";

        // 임시 파일 경로 설정
        Path inputPath = tempDir.resolve(originalName);
        Path outputPath = tempDir.resolve(outputName);

        // 업로드된 파일을 임시 디렉토리에 복사
        Files.copy(file.getInputStream(), inputPath);

        try {
            // GIF 변환 실행
            converter.convertVideoToGif(inputPath.toString(), outputPath.toString());
            return outputName;
        } finally {
            Files.deleteIfExists(inputPath);
        }
    }
}