package com.ssafy.groute.controller;


import com.ssafy.groute.dto.Routes;
import com.ssafy.groute.service.RoutesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
public class RoutesController {
    @Autowired
    RoutesService routesService;

    @ApiOperation(value = "routes 추가",notes = "routes 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertRoutes(@RequestBody Routes req){

        try {
            routesService.insertRoutes(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "routes 검색",notes = "이름으로 routes 하나 검색")
    @GetMapping(value = "/detail")
    public ResponseEntity<?> detailRoutes(@RequestParam("id") int id) throws Exception{

        Routes res = routesService.selectRoutes(id);
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Routes>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list routes",notes = "모든 routes 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listRoutes() throws Exception{

        List<Routes> res = routesService.selectAllRoutes();
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Routes>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete routes",notes = "routes 삭제")
    @DeleteMapping(value = "/del")
    public ResponseEntity<?> deleteRoutes(@RequestParam("id") int id) throws Exception{

        try {
            routesService.deleteRoutes(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "updateRoutes",notes = "routes 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateRoutes(@RequestBody Routes routes) throws Exception{

        try {
            routesService.updateRoutes(routes);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }
}
