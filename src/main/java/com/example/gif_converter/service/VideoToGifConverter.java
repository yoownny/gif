package com.example.gif_converter.service;

import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class VideoToGifConverter {
    private static final String FFMPEG_PATH = "ffmpeg";

    public void convertVideoToGif(String inputVideo, String outputGif) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                FFMPEG_PATH,
                "-i", inputVideo,
                "-vf", "fps=15,scale=480:-1:flags=lanczos,split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse",
                "-loop", "0",
                outputGif
        );

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