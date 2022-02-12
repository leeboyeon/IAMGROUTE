package com.ssafy.groute.controller;

import com.ssafy.groute.service.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
@CrossOrigin("*")
@Slf4j
public class FcmController {

    @Autowired
    FirebaseCloudMessageService fcmService;

    @PostMapping("/token")
    public ResponseEntity<?> registToken(String token, String userId) {
        log.info("registToken : token:{} {}", token, userId);
        try {
            fcmService.updateToken(token, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true, HttpStatus.OK);

    }

//    @PostMapping("/broadcast")
//    public ResponseEntity<?> broadCast(String title, String body, String path) throws IOException {
//        log.info("broadCast : title:{}, body:{}", title, body);
//        return fcmService.broadCastMessage(title, body, path);
//    }

    @PostMapping("/sendMessageTo")
    public ResponseEntity<?> sendMessageTo(String token, String title, String body, String path) throws IOException {
        log.info("sendMessageTo : token:{}, title:{}, body:{}, path:{}", token, title, body, path);
        try{
            fcmService.sendMessageTo(token, title, body, path);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }
}
