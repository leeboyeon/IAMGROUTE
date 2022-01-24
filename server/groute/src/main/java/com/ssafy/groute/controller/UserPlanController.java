package com.ssafy.groute.controller;

import com.ssafy.groute.dto.Place;
import com.ssafy.groute.dto.RouteDetail;
import com.ssafy.groute.dto.User;
import com.ssafy.groute.dto.UserPlan;
import com.ssafy.groute.service.RouteDetailService;
import com.ssafy.groute.service.UserPlanService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plan")
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
public class UserPlanController {
    @Autowired
    UserPlanService userPlanService;
    @Autowired
    RouteDetailService routeDetailService;

    @ApiOperation(value = "userPlan 추가",notes = "userPlan 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertUserPlan(@RequestBody UserPlan req,@RequestParam("userIds") List<String> userIds){

        try {
            userPlanService.insertUserPlan(req, userIds);
        }catch (Exception e){
//            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "userPlan 검색",notes = "id로 userPlan 하나 검색")
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> detailUserPlan(@PathVariable("id") int id) throws Exception{

        UserPlan res = userPlanService.selectUserPlan(id);
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<UserPlan>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list userPlan",notes = "모든 userPlan 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listUserPlan() throws Exception{

        List<UserPlan> res = userPlanService.selectAllUserPlan();
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<UserPlan>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete userPlan",notes = "userPlan 삭제")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUserPlan(@PathVariable("id") int id) throws Exception{

        try {
            userPlanService.deleteUserPlan(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "updateUserPlan",notes = "userPlan 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateUserPlan(@RequestBody UserPlan userPlan) throws Exception{

        try {
            userPlanService.updateUserPlan(userPlan);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "place 추가",notes = "해당 일정에 place 추가")
    @PostMapping(value = "/place")
    public ResponseEntity<?> addPlace(@RequestBody RouteDetail routeDetail){

        try {
            routeDetailService.insertRouteDetail(routeDetail);
        }catch (Exception e){
//            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "list my userPlan",notes = "userId로 내 여행일정 검색")
    @GetMapping(value = "/list/{userId}")
    public ResponseEntity<?> listMyUserPlan(@PathVariable String userId) throws Exception{

        List<UserPlan> res = userPlanService.selectAllUserPlanById(userId);
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<UserPlan>>(res,HttpStatus.OK);
    }
}