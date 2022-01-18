package com.ssafy.groute.controller;

import com.ssafy.groute.dto.Area;
import com.ssafy.groute.service.AreaService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
@RequestMapping("/area")
public class AreaController {


    @Autowired
    AreaService areaService;

    @ApiOperation(value = "area 추가",notes = "area 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertArea(@RequestBody Area req){

        try {
            areaService.insertArea(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }
}
