package com.ssafy.groute.controller;

import com.ssafy.groute.dto.UserDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/a")
public class MainController {

    @ApiOperation(value="로그인", notes="로그인")
    @GetMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody UserDTO dto) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();

        return null;
    }
}
