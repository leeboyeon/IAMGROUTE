package com.ssafy.groute.controller;

import com.ssafy.groute.dto.PlanShareUser;
import com.ssafy.groute.dto.User;
import com.ssafy.groute.service.FirebaseCloudMessageService;
import com.ssafy.groute.service.PlanShareUserService;
import com.ssafy.groute.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planShareUser")
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
public class PlanShareUserController {

    @Autowired
    PlanShareUserService planShareUserService;

    @Autowired
    UserService userService;

    @Autowired
    FirebaseCloudMessageService fcmService;

    @ApiOperation(value = "planShareUser 추가",notes = "planShareUser 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertPlanShareUser(@RequestBody PlanShareUser req){
        User userInfo = userService.findById(req.getUserId());
        try {
            planShareUserService.insertPlanShareUser(req);
            fcmService.sendMessageTo(userInfo.getToken(), "User", userInfo.getNickname() + " 님, 공유된 여행 일정이 있습니다.\n아이엠그루트에서 확인해볼까요? (☞ﾟヮﾟ)☞", "");
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "planShareUser 검색",notes = "이름으로 planShareUser 하나 검색")
    @GetMapping(value = "/detail")
    public ResponseEntity<?> detailPlanShareUser(@RequestParam("id") int id) throws Exception{

        PlanShareUser res = planShareUserService.selectPlanShareUser(id);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<PlanShareUser>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list planShareUser",notes = "모든 planShareUser 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listPlanShareUser() throws Exception{

        List<PlanShareUser> res = planShareUserService.selectAllPlanShareUser();
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<PlanShareUser>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list shared user",notes = "해당 일정 공유하는 user list 반환")
    @GetMapping(value = "/list/{planId}")
    public ResponseEntity<?> listUserByPlanId(@PathVariable("planId") int planId) throws Exception{

        List<User> res = userService.selectByPlanId(planId);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<User>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete planShareUser",notes = "planShareUser 삭제")
    @PostMapping(value = "/del")
    public ResponseEntity<?> deletePlanShareUser(@RequestBody PlanShareUser planShareUser) throws Exception{

        try {
            planShareUserService.deletePlanShareUser(planShareUser);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "updatePlanShareUser",notes = "planShareUser 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updatePlanShareUser(@RequestBody PlanShareUser planShareUser) throws Exception{

        try {
            planShareUserService.updatePlanShareUser(planShareUser);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }
}
