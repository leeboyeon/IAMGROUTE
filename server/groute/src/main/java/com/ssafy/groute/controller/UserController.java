package com.ssafy.groute.controller;

import com.ssafy.groute.config.security.JwtTokenProvider;
import com.ssafy.groute.dto.User;
import com.ssafy.groute.mapper.UserMapper;
import com.ssafy.groute.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "회원가입", notes = "회원가입")
    @PostMapping(value = "/signup")
    public ResponseEntity<?> registerUser(User user) throws Exception{
        if (userService.findById(user.getId()) != null) {
            return ResponseEntity.badRequest().body("아이디가 이미 존재합니다.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.registerUser(user);
        return ResponseEntity.ok("회원가입이 완료 되었습니다.");

    }

    @ApiOperation(value="로그인", notes="로그인")
    @GetMapping("/login")
    public ResponseEntity<?> login(User req) throws Exception{

        User selected = userService.findByUidType(req.getId(), req.getType());

        if(selected == null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }else if(!passwordEncoder.matches(req.getPassword(), selected.getPassword())){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("id",selected.getId());
        resultMap.put("token",jwtTokenProvider.createToken(String.valueOf(selected.getId()), Collections.singletonList("ROLE_USER")));

        selected.setToken(req.getToken());

        userService.updateUser(selected);

        return new ResponseEntity<Map<String, Object>>(resultMap,HttpStatus.OK);
    }

    @ApiOperation(value="Id 중복 체크", notes="Id 중복 체크")
    @GetMapping("/isUsedId")
    public @ResponseBody
    Boolean isUsedId(String id) throws Exception {

        if (userService.findById(id) != null) {
            return true;    // 조회되는 User가 있으면 Id 중복
        } else {
            return false;
        }
    }

    @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴")
    @DeleteMapping(value = "{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) throws Exception{
        // jwt token 인증 필요



        if (userService.findById(userId) == null) {
            return ResponseEntity.badRequest().body("존재하지 않는 아이디입니다.");
        }

        userService.deleteUser(userId);
        return ResponseEntity.ok("회원탈퇴가 완료 되었습니다.");
    }

    @ApiOperation(value = "회원수정", notes = "회원수정")
    @PutMapping(value = "{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, User user) throws Exception{
        if (userService.findById(userId) == null) {
            return ResponseEntity.badRequest().body("존재하지 않는 아이디입니다.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.updateUser(user);

        return ResponseEntity.ok("정보 수정이 완료 되었습니다.");
    }

    @ApiOperation(value = "유저정보", notes = "유저정보")
    @GetMapping(value = "{userId}")
    public ResponseEntity<?> detailUser(@PathVariable String userId) throws Exception{
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("존재하지 않는 아이디입니다.");
        }

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
}
