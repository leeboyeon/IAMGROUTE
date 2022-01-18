package com.ssafy.groute.service;

import com.ssafy.groute.dto.User;
import com.ssafy.groute.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    @Override
    public int registerUser(User user){
        return userMapper.registerUser(user);
    }

    @Override
    public User findById(String userId){
        return userMapper.findById(userId);
    }

    @Override
    public User findByUidType(String userId, String type){
        return userMapper.findById(userId);
    }

}
