package com.ssafy.groute.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.groute.dto.*;
import com.ssafy.groute.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    UserService userService;

    @Autowired
    FirebaseCloudMessageService fcmService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    StorageService storageService;

    @Value("${spring.http.multipart.location}")
    private String uploadPath;

    @ApiOperation(value = "userPlan 추가",notes = "planId가 0이면 빈 일정 생성 0이 아니면 해당 일정 복사해서 생성")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertUserPlan(@RequestBody UserPlan req,@RequestParam("userIds") List<String> userIds,@RequestParam("planId") int planId){

        try {
            userPlanService.insertUserPlan(req, userIds, planId);
            for (int i = 0; i < userIds.size(); i++) {
                User userInfo = userService.findById(userIds.get(i));
                fcmService.sendMessageTo(userInfo.getToken(), "User", userInfo.getNickname() + " 님, 공유된 여행 일정이 있습니다.\n아이엠그루트에서 확인해볼까요? (☞ﾟヮﾟ)☞", "");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "userPlan 반환",notes = "해당 place 포함한(flag=1) or 포함하지 않는(flag=2) 경로 찾기")
    @GetMapping(value = "/filter")
    public ResponseEntity<?> filterUserPlan(@RequestParam(required = false) List<Integer> placeIds, @RequestParam("flag")int flag) throws Exception{
        List<UserPlan> userPlans = new ArrayList<>();
        try {
            userPlans = userPlanService.selectAllByPlaceId(placeIds,flag);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<List<UserPlan>>(userPlans,HttpStatus.OK);
    }

    @ApiOperation(value = "userPlan 카피",notes = "이미 만들어진 일정에서 추천루트 선택했을때 day에 일정을 넣을 날짜의 시작날짜 입력" +
            "ex)3박4일 일정에서 1박 2일짜리 루트로 2,3 번째 날의 일정을 변경하고 싶으면 day에 2 입력")
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

    @ApiOperation(value = "list userPlan",notes = "공개된 모든 userPlan 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listUserPlan() throws Exception{

        List<UserPlan> res = userPlanService.selectTUserPlan();
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
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
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "updateUserPlan",notes = "userPlan 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateUserPlan(@RequestBody UserPlan userPlan) throws Exception{

        try {
            userPlanService.updateUserPlan(userPlan);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "place 추가",notes = "해당 일정에 place 추가")
    @PostMapping(value = "/place")
    public ResponseEntity<?> addPlace(@RequestBody RouteDetail routeDetail){

        try {
            routeDetailService.insertRouteDetail(routeDetail);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "place 삭제",notes = "해당 일정에 place 삭제")
    @DeleteMapping(value = "/place/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable("id") int id){

        try {
            userPlanService.deleteRouteDetail(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "update priority",notes = "priority 변경")
    @PutMapping(value = "/place/priority")
    public ResponseEntity<?> updatePriority(@RequestBody List<RouteDetail> routeDetailList) throws Exception{

        try {
            routeDetailService.updatePriority(routeDetailList);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "list my userPlan",notes = "userId로 내 전체 여행일정 검색")
    @GetMapping(value = "/list/{userId}")
    public ResponseEntity<?> listMyUserPlan(@PathVariable String userId) throws Exception{

        List<UserPlan> res = userPlanService.selectAllUserPlanById(userId);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<UserPlan>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list my end userPlan",notes = "userId로 내 끝난 여행일정 검색")
    @GetMapping(value = "/list/end/{userId}")
    public ResponseEntity<?> listMyEndUserPlan(@PathVariable String userId) throws Exception{

        List<UserPlan> res = userPlanService.findEndPlanById(userId);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<UserPlan>>(res,HttpStatus.OK);
    }
    @ApiOperation(value = "list my end userPlan",notes = "userId로 내 안끝난 여행일정 검색")
    @GetMapping(value = "/list/notend/{userId}")
    public ResponseEntity<?> listMyNotEndUserPlan(@PathVariable String userId) throws Exception{

        List<UserPlan> res = userPlanService.findNotEndPlanById(userId);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
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
//    public ResponseEntity<?> insertPlanReview(@RequestBody PlanReview req){
    public ResponseEntity<?> insertPlanReview(@RequestPart(value = "review") String req, @RequestPart(value = "img", required = false) MultipartFile img) throws Exception {

        try {
            PlanReview planReview = mapper.readValue(req, PlanReview.class);
            if (img != null) {
                String fileName = storageService.store(img, uploadPath + "/review/plan");
                planReview.setImg("review/plan/" + fileName);
            } else {
                planReview.setImg(null);
            }
            planReviewService.insertPlanReview(planReview);
        } catch (Exception e){
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
//    public ResponseEntity<?> updatePlanReview(@RequestBody PlanReview planReview) throws Exception{
    public ResponseEntity<?> updatePlanReview(@RequestPart(value = "review") String req, @RequestPart(value = "img", required = false) MultipartFile img) throws Exception {

        try {
            PlanReview planReview = mapper.readValue(req, PlanReview.class);
            String beforeImg = planReview.getImg();

            if (img != null) {
                String fileName = storageService.store(img, uploadPath + "/review/plan");
                planReview.setImg("review/plan/" + fileName);
            } else {
                if(beforeImg.isEmpty() || beforeImg.equals("null")) {
                    planReview.setImg(null);
                } else {
                    planReview.setImg(beforeImg);
                }
            }
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

    @ApiOperation(value = "return like plan list",notes = "내가 좋아요한 plan 리스트 반환")
    @GetMapping(value = "/like/{userId}")
    public ResponseEntity<?> myPlan(@PathVariable("userId") String userId) throws Exception{
        List<UserPlan> res = userPlanService.selectAllPlanByUserId(userId);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<UserPlan>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "최적경로",notes = "최적경로 반환")
    @GetMapping(value = "/path")
    public ResponseEntity<?> shortestPath(@RequestParam int start, @RequestParam int end, @RequestParam int routeId) throws Exception{
        routeDetailService.updatePriority(userPlanService.shortestPath(start, end, routeId));
        List<RouteDetail> res = routeDetailService.selectByRouteId(routeId);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<RouteDetail>>(res,HttpStatus.OK);
    }
}