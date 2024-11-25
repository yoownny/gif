# :bulb:Video to GIF Converter
Spring Boot와 FFmpeg를 활용한 비디오-GIF 변환 웹 애플리케이션

## 주요 기능
- 비디오 파일을 GIF로 변환
- 원본 파일명 유지
- 최적화된 GIF 품질 (15fps, 480p)

## 사용 기술
- Spring Boot 3.4.0
- Java 21
- FFmpeg: 비디오 변환 엔진
- Springdoc OpenAPI: API 문서화

## API
- POST `/api/converter/convert`: 비디오 업로드 및 변환
- GET `/api/converter/download/{fileName}`: 변환된 GIF 다운로드

## 실행 요구사항
- FFmpeg 설치 필수
- Java 21 이상
