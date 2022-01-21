package com.ssafy.groute.controller;

import com.ssafy.groute.config.security.JwtTokenProvider;
import com.ssafy.groute.dto.User;
import com.ssafy.groute.mapper.UserMapper;
import com.ssafy.groute.service.StorageService;
import com.ssafy.groute.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private final StorageService storageService;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

//    @ApiOperation(value = "프로필사진", notes = "프로필사진")
//    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
//    public ResponseEntity<byte[]> userImage(@PathVariable("imageName") String imagename) throws IOException {
//        InputStream imageStream = new FileInputStream(uploadPath + "/user/" + imagename);
//        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
//        imageStream.close();
//        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
//    }

//사진 안바꾸고 완료 눌렀을 때 에러나는 부분 처리하기
    @ApiOperation(value = "회원가입", notes = "회원가입")
    @PostMapping(value = "/signup")
    public Boolean registerUser(@RequestBody User user) throws Exception{
        if (userService.findById(user.getId()) != null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getPhone() != null && user.getPhone().equals("")) {
            user.setPhone(null);
        }
        if(user.getBirth() != null && user.getBirth().equals("")) {
            user.setBirth(null);
        }

        userService.registerUser(user);
        return true;
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

//        userService.saveUser(selected);

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
    public Boolean deleteUser(@PathVariable String userId) throws Exception{
        // jwt token 인증 필요

        if (userService.findById(userId) == null) {
            return false;
        }

        userService.deleteUser(userId);
        return true;
    }

    @ApiOperation(value = "회원수정", notes = "회원수정")
    @PutMapping(value = "/update")
    public Boolean updateUser(User user, MultipartFile file) throws Exception{
        if (userService.findById(user.getId()) == null) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (file != null) {
            String fileName = storageService.store(file, uploadPath + "/user");
            user.setImg(fileName);
        } else {
            user.setImg(null);
        }
        if(user.getPhone() != null && user.getPhone().equals("")) {
            user.setPhone(null);
        }
        if(user.getBirth() != null && user.getBirth().equals("")) {
            user.setBirth(null);
        }

        userService.updateUser(user);
        return true;
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
