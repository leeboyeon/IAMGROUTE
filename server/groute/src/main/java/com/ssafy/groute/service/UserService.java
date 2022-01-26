package com.ssafy.groute.service;

import com.ssafy.groute.dto.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void registerUser(User user);
    User findById(String userId);
    User findByUidType(String id, String type);
    void deleteUser(String userId) throws Exception;
    void updateUser(User user);
}
