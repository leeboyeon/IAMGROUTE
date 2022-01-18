package com.ssafy.groute.service;

import com.ssafy.groute.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public int registerUser(UserDTO user);

    public UserDTO findById(String userId);
}
