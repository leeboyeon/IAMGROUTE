package com.ssafy.groute.controller;

import com.ssafy.groute.dto.User;
import com.ssafy.groute.mapper.UserMapper;
import com.ssafy.groute.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserMapper userMapper;

    @ApiOperation(value = "회원가입", notes = "회원가입")
    @PostMapping(value = "/signup")
    public ResponseEntity<?> registerUser(User dto) throws Exception{
//        UserDTO userChk = userService.findById(dto.getId());
        if (userService.findById(dto.getId()) != null) {
            return ResponseEntity.badRequest().body("아이디가 이미 존재합니다.");
        }

        User user = new User();

        user.setId(dto.getId());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setBirth(dto.getBirth());
        user.setType(dto.getType());
        user.setNickname(dto.getNickname());

//        userMapper.registerUser(user);
        userService.registerUser(user);
        return ResponseEntity.ok("회원가입이 완료 되었습니다.");

    }

}
