package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    int registerUser(User user);

    User findById(String userId);
}
