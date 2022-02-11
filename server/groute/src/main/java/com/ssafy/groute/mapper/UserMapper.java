package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    int registerUser(User user);

    User findById(String userId);

    User findByUidType(String id,String type);

    int deleteUser(String userId);

    int updateUser(User user);

    List<User> selectByPlanId(int planId) throws Exception;

    void updateTokenByUserId(User user) throws Exception;

    User selectUserByToken(String token) throws Exception;
}
