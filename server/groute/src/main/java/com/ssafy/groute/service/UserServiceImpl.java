package com.ssafy.groute.service;

import com.ssafy.groute.dto.UserDTO;
import com.ssafy.groute.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public int registerUser(UserDTO user){
        return userMapper.registerUser(user);
    }

    @Override
    public UserDTO findById(String userId){
        return userMapper.findById(userId);
    }
}
