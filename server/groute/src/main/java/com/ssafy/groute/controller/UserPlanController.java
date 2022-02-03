package com.ssafy.groute.controller;

import com.ssafy.groute.dto.*;
import com.ssafy.groute.service.PlanReviewService;
import com.ssafy.groute.service.RouteDetailService;
import com.ssafy.groute.service.UserPlanService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plan")
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
public class UserPlanController {
    @Autowired
    UserPlanService userPlanService;
    @Autowired
    RouteDetailService routeDetailService;
    @Autowired
    PlanReviewService planReviewService;

    @ApiOperation(value = "userPlan 추가",notes = "planId가 0이면 빈 일정 생성 0이 아니면 해당 일정 복사해서 생성")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertUserPlan(@RequestBody UserPlan req,@RequestParam("userIds") List<String> userIds,@RequestParam("planId") int planId){

        try {
            userPlanService.insertUserPlan(req, userIds, planId);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "userPlan 반환",notes = "해당 place 포함한 경로 찾기")
    @GetMapping(value = "/filter")
    public ResponseEntity<?> filterUserPlan(@RequestParam("placeIds") List<Integer> placeIds,int day) throws Exception{
        List<UserPlan> userPlans = new ArrayList<>();
        try {
            userPlans = userPlanService.selectAllByPlaceId(placeIds,day);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<List<UserPlan>>(userPlans,HttpStatus.OK);
    }

    @ApiOperation(value = "userPlan 카피",notes = "이미 만들어진 일정에서 추천루트 선택했을때 전체 일정 변경이면 day 0, 특정 날짜 변경이면 day에 몇번째 날인지")
    @PostMapping(value = "/copy")
    public ResponseEntity<?> copyUserPlan(@RequestBody UserPlan req,@RequestParam("planId") int planId,@RequestParam("day")int day){

        try {
            userPlanService.copyPlan(req, planId, day);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }


    @ApiOperation(value = "userPlan 검색",notes = "id로 userPlan 상세 정보 반환")
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> detailUserPlan(@PathVariable("id") int id) throws Exception{

        Map<String,Object> res = userPlanService.selectUserPlan(id);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Map<String,Object>>(res,HttpStatus.OK);
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
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "place 삭제",notes = "해당 일정에 place 삭제")
    @DeleteMapping(value = "/place/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable("id") int id){

        try {
            routeDetailService.deleteRouteDetail(id);
        }catch (Exception e){
            e.printStackTrace();
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

    @ApiOperation(value = "isLike",notes = "해당 plan 좋아요 했는지")
    @PostMapping(value ="/isLike")
    public ResponseEntity<?> isLike(@RequestBody PlanLike planLike){
        try {
            PlanLike p = userPlanService.isLike(planLike);
            if(p == null){
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "planLike",notes = "해당 plan 좋아요 또는 좋아요취소")
    @PostMapping(value = "/like")
    public ResponseEntity<?> planLike(@RequestBody PlanLike planLike){
        try {
            userPlanService.likePlan(planLike);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @ApiOperation(value = "bestPlanList",notes = "좋아요가 많은 순 5개 plan리스트 출력")
    @GetMapping(value="/best")
    public ResponseEntity<?> bestPlanList() throws Exception{
        List<UserPlan> res = userPlanService.bestPlanList();
        if(res == null){
            return new ResponseEntity<Boolean>(false,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<UserPlan>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "planReview 추가",notes = "planReview 추가")
    @PostMapping(value = "/review")
    public ResponseEntity<?> insertPlanReview(@RequestBody PlanReview req){

        try {
            planReviewService.insertPlanReview(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "planReview 검색",notes = "이름으로 planReview 하나 검색")
    @GetMapping(value = "/review/detail")
    public ResponseEntity<?> detailPlanReview(@RequestParam("id") int id) throws Exception{

        PlanReview res = planReviewService.selectPlanReview(id);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<PlanReview>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list planReview",notes = "모든 planReview 반환")
    @GetMapping(value = "/review/list")
    public ResponseEntity<?> listPlanReview() throws Exception{

        List<PlanReview> res = planReviewService.selectAllPlanReview();
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<PlanReview>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete planReview",notes = "planReview 삭제")
    @DeleteMapping(value = "/review/del")
    public ResponseEntity<?> deletePlanReview(@RequestParam("id") int id) throws Exception{

        try {
            planReviewService.deletePlanReview(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "updatePlanReview",notes = "planReview 수정")
    @PutMapping(value = "/review/update")
    public ResponseEntity<?> updatePlanReview(@RequestBody PlanReview planReview) throws Exception{

        try {
            planReviewService.updatePlanReview(planReview);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "planReview by planId",notes = "해당 plan의 리뷰 반환")
    @GetMapping(value = "/review/list/{planId}")
    public ResponseEntity<?> listPlanReviewByPlanId(@PathVariable("planId") int planId) throws Exception{

        List<PlanReview> res = planReviewService.selectByPlanId(planId);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<PlanReview>>(res,HttpStatus.OK);
    }
}