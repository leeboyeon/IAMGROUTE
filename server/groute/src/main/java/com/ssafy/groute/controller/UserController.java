package com.ssafy.groute.controller;

import com.ssafy.groute.config.security.JwtTokenProvider;
import com.ssafy.groute.dto.User;
import com.ssafy.groute.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value="로그인", notes="로그인")
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody User req) throws Exception{

        User selected = userService.findByUidType(req.getId(), req.getType());

        if(selected == null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }else if(!passwordEncoder.matches(req.getPassword(), selected.getPassword())){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("id",selected.getId());
        resultMap.put("token",jwtTokenProvider.createToken(String.valueOf(selected.getId()), Collections.singletonList("ROLE_USER")));

        selected.setUpdateDate(req.getToken());

//        userService.saveUser(selected);

        return new ResponseEntity<Map<String, Object>>(resultMap,HttpStatus.OK);
    }
}
