package com.ssafy.groute.controller;

import com.ssafy.groute.dto.Theme;
import com.ssafy.groute.service.ThemeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
@RequestMapping("/theme")
public class ThemeController {

    @Autowired
    ThemeService themeService;

    @ApiOperation(value = "theme 추가",notes = "theme 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertTheme(@RequestBody Theme req){

        try {
            themeService.insertTheme(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "theme 검색",notes = "이름으로 theme 하나 검색")
    @GetMapping(value = "/detail")
    public ResponseEntity<?> detailTheme(@RequestParam("id") int id) throws Exception{

        Theme res = themeService.selectTheme(id);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Theme>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list theme",notes = "모든 theme 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listTheme() throws Exception{

        List<Theme> res = themeService.selectAllTheme();
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Theme>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete theme",notes = "theme 삭제")
    @DeleteMapping(value = "/del")
    public ResponseEntity<?> deleteTheme(@RequestParam("id") int id) throws Exception{

        try {
            themeService.deleteTheme(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "updateTheme",notes = "theme 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateTheme(@RequestBody Theme theme) throws Exception{

        try {
            themeService.updateTheme(theme);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }
}
