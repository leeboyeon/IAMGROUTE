package com.ssafy.groute.controller;

import com.ssafy.groute.dto.RouteDetail;
import com.ssafy.groute.service.RouteDetailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
@RequestMapping("/routeDetail")
public class RouteDetailController {

    @Autowired
    RouteDetailService routeDetailService;

    @ApiOperation(value = "routeDetail 추가",notes = "routeDetail 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertRouteDetail(@RequestBody RouteDetail req){

        try {
            routeDetailService.insertRouteDetail(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "routeDetail 검색",notes = "이름으로 routeDetail 하나 검색")
    @GetMapping(value = "/detail")
    public ResponseEntity<?> detailRouteDetail(@RequestParam("id") int id) throws Exception{

        RouteDetail res = routeDetailService.selectRouteDetail(id);
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<RouteDetail>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list routeDetail",notes = "모든 routeDetail 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listRouteDetail() throws Exception{

        List<RouteDetail> res = routeDetailService.selectAllRouteDetail();
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<RouteDetail>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete routeDetail",notes = "routeDetail 삭제")
    @DeleteMapping(value = "/del")
    public ResponseEntity<?> deleteRouteDetail(@RequestParam("id") int id) throws Exception{

        try {
            routeDetailService.deleteRouteDetail(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "updateRouteDetail",notes = "routeDetail 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateRouteDetail(@RequestBody RouteDetail routeDetail) throws Exception{

        try {
            routeDetailService.updateRouteDetail(routeDetail);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }
}
