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

import java.util.List;

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

    @ApiOperation(value = "area 검색",notes = "이름으로 area 하나 검색")
    @GetMapping(value = "/detail")
    public ResponseEntity<?> detailArea(@RequestParam("name") String name) throws Exception{

        Area res = areaService.selectArea(name);
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Area>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list area",notes = "모든 area 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listArea() throws Exception{

        List<Area> res = areaService.selectAllArea();
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Area>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete area",notes = "area 삭제")
    @DeleteMapping(value = "/del")
    public ResponseEntity<?> deleteArea(@RequestParam("id") int id) throws Exception{

        try {
            areaService.deleteArea(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "updateArea",notes = "area 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateArea(@RequestBody Area area) throws Exception{

        try {
            areaService.updateArea(area);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }
}
