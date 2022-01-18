package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    int registerUser(UserDTO user);

    UserDTO findById(String userId);
}
