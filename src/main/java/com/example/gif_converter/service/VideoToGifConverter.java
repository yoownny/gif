package com.example.gif_converter.service;

import org.springframework.stereotype.Component;
import java.io.IOException;

// FFmpeg를 사용하여 비디오를 GIF로 변환하는 컴포넌트
@Component
public class VideoToGifConverter {
    // FFmpeg 실행 파일 경로 (환경 변수에 등록된 경우 'ffmpeg'만 사용)
    private static final String FFMPEG_PATH = "ffmpeg";

    public void convertVideoToGif(String inputVideo, String outputGif) throws IOException {
        // FFmpeg 명령어 구성
        ProcessBuilder builder = new ProcessBuilder(
                FFMPEG_PATH,
                "-i", inputVideo,
                "-vf", "fps=15,scale=480:-1:flags=lanczos,split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse",
                // fps=15: 초당 15프레임f
                // scale=480:-1: 너비 480px, 높이는 비율 유지
                // flags=lanczos: 고품질 스케일링
                // palettegen/paletteuse: 최적의 색상 팔레트 생성 및 적용
                "-loop", "0",
                outputGif
        );

        // FFmpeg 프로세스 실행
        Process process = builder.start();
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("FFmpeg process exited with code " + exitCode);
            }
        } catch (InterruptedException e) {
            throw new IOException("변환 중 오류 발생", e);
        }
    }
}