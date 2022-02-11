package com.ssafy.groute.controller;

import com.ssafy.groute.dto.Notification;
import com.ssafy.groute.dto.Place;
import com.ssafy.groute.service.NotificationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
public class NotificationContoller {
    @Autowired
    NotificationService notificationService;

    @ApiOperation(value = "notificion 추가",notes = "notificion 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertNotification(@RequestBody Notification req){

        try {
            notificationService.insertNotification(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "notification 검색",notes = "id로 notification 하나 검색")
    @GetMapping(value = "/detail")
    public ResponseEntity<?> detailNotification(@RequestParam("id") int id) throws Exception{

        Notification res = notificationService.selectNotification(id);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Notification>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "유저아이디로 검색",notes = "유저아이디로 notification 리스트 검색")
    @GetMapping(value = "/detailList")
    public ResponseEntity<?> detailListNotification(@RequestParam("userId") String userId) throws Exception{

        List<Notification> res = notificationService.selectNotificationByUserId(userId);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Notification>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete notification",notes = "notification 삭제")
    @DeleteMapping(value = "/del")
    public ResponseEntity<?> deleteNotification(@RequestParam("id") int id) throws Exception{

        try {
            notificationService.deleteNotification(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "updateTheme",notes = "theme 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateNotification(@RequestBody Notification notification) throws Exception{

        try {
            notificationService.updateNotification(notification);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }
}
