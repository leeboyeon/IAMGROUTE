package com.ssafy.groute.service;

import com.ssafy.groute.dto.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User findByUidType(String id, String type);
}
