package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.User;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface UserMapper {
    User findById(String userId);
}
