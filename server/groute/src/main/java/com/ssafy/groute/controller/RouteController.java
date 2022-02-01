package com.ssafy.groute.controller;

import com.ssafy.groute.dto.Route;
import com.ssafy.groute.service.RouteService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
@RequestMapping("/route")
public class RouteController {

    @Autowired
    RouteService routeService;

    @ApiOperation(value = "route 추가",notes = "route 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertRoute(@RequestBody Route req){

        try {
            routeService.insertRoute(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "route 검색",notes = "이름으로 route 하나 검색")
    @GetMapping(value = "/detail")
    public ResponseEntity<?> detailRoute(@RequestParam("id") int id) throws Exception{

        Route res = routeService.selectRoute(id);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Route>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list route",notes = "모든 route 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listRoute() throws Exception{

        List<Route> res = routeService.selectAllRoute();
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Route>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete route",notes = "route 삭제")
    @DeleteMapping(value = "/del")
    public ResponseEntity<?> deleteRoute(@RequestParam("id") int id) throws Exception{

        try {
            routeService.deleteRoute(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "updateRoute",notes = "route 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateRoute(@RequestBody Route route) throws Exception{

        try {
            routeService.updateRoute(route);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

//    @ApiOperation(value = "bestRoute",notes = "인기경로 베스트5")
//    @GetMapping(value = "/best")
//    public ResponseEntity<?> listBestRoute() throws Exception{
//
//        List<Route> res = routeService.selectBestRoute();
//        if(res==null){
//            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
//        }
//
//        return new ResponseEntity<List<Route>>(res,HttpStatus.OK);
//    }
}
