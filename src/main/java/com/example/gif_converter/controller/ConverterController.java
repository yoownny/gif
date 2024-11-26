package com.example.gif_converter.controller;

import com.example.gif_converter.service.VideoService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

// API 엔드포인트를 제공하는 컨트롤러
@RestController
@RequestMapping("/api/converter")
public class ConverterController {
    private final VideoService videoService;

    public ConverterController(VideoService videoService) {
        this.videoService = videoService;
    }

    // 비디오 파일 업로드 및 GIF 변환 API
    @PostMapping(value = "/convert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> convertVideo(@RequestParam("file") MultipartFile file) {
        try {
            String outputFileName = videoService.convertVideo(file);
            return ResponseEntity.ok(outputFileName);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("변환 실패: " + e.getMessage());
        }
    }

    // 변환된 GIF 파일 다운로드 API
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        // 파일 경로 생성
        Path filePath = Paths.get(System.getProperty("java.io.tmpdir"), fileName);
        Resource resource = new UrlResource(filePath.toUri());

        // 파일 다운로드 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_GIF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + "\"")
                .body(resource);
    }
}