package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.User;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface UserMapper {

    int registerUser(User user);

    User findById(String userId);
    User findByUidType(String id,String type);

    int deleteUser(String userId);

    int updateUser(User user);
}
