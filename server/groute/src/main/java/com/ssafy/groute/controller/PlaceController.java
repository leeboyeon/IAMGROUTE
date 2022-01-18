package com.ssafy.groute.controller;

import com.ssafy.groute.dto.Place;
import com.ssafy.groute.service.PlaceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/place")
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
public class PlaceController {
    @Autowired
    PlaceService placeService;

    @ApiOperation(value = "place 추가",notes = "place 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertPlace(@RequestBody Place req){

        try {
            placeService.insertPlace(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "place 검색",notes = "이름으로 place 하나 검색")
    @GetMapping(value = "/detail")
    public ResponseEntity<?> detailPlace(@RequestParam("id") int id) throws Exception{

        Place res = placeService.selectPlace(id);
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Place>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list place",notes = "모든 place 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listPlace() throws Exception{

        List<Place> res = placeService.selectAllPlace();
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Place>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete place",notes = "place 삭제")
    @DeleteMapping(value = "/del")
    public ResponseEntity<?> deletePlace(@RequestParam("id") int id) throws Exception{

        try {
            placeService.deletePlace(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "updatePlace",notes = "place 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updatePlace(@RequestBody Place place) throws Exception{

        try {
            placeService.updatePlace(place);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }
}
