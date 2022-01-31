package com.ssafy.groute.config.security;

import com.ssafy.groute.dto.User;
import com.ssafy.groute.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userPk){
        User user = null;
        try {
            user =userService.findById(userPk);
        } catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }
}
