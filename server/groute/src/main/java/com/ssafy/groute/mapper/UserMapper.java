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
    User selectUserIdByEmail(String email) throws Exception;
    User selectUserByIdEmail(String id, String email) throws Exception;
    void updatePassword(User user) throws Exception;
    List<User> selectAllUserList() throws Exception;
    List<User> selectEndedPlanYesterday() throws Exception;
    List<User> selectOneDayBeforeTravel() throws Exception;
    List<User> selectThreeDayBeforeTravel() throws Exception;
    List<User> selectWeekBeforeTravel() throws Exception;

}
