package com.ssafy.groute.service;

import com.ssafy.groute.dto.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public int registerUser(User user);

    public User findById(String userId);
}
