package com.ssafy.groute.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebController {

    @ApiOperation(value = "파일 다운로드",notes = "파일 다운로드")
    @GetMapping(value = "/download")
    public void downloadFile(HttpServletResponse response) throws Exception{

        // 프로젝트 폴더의 temp.jpg 파일 로드
        String fileName = "app-universal-release.apk";
        File file = new File("./" + fileName);

        // 클라이언트에서 아래의 이름으로 파일이 받아진다.
        String newFileName = "app-universal-release.apk";

        try (
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                OutputStream out = response.getOutputStream()
        ){
            // 응답이 파일 타입이라는 것을 명시
            response.addHeader("Content-Disposition", "attachment;filename=\""+newFileName+"\"");
            // 응답 크기 명시
            response.setContentLength((int)file.length());

            int read = 0;

            // 실제 데이터 전송
            // OutputStream 의 Deafult 버퍼 사이즈는 8192 Byte
            // 이 루프를 8000 번 정도 돌때마다 약 8KB 정도의 데이터가 전송
            while((read = bis.read()) != -1) {
                out.write(read);
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
