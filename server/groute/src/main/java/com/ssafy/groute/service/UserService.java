package com.ssafy.groute.service;

import com.ssafy.groute.dto.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void registerUser(User user);
    User findById(String userId);
    User findByUidType(String id, String type);
    void deleteUser(String userId) throws Exception;
    void updateUser(User user);
    List<User> selectByPlanId(int planId) throws Exception;
    void updateTokenByUserId(User user) throws Exception;
    User selectUserByToken(String token) throws Exception;
    User selectUserIdByEmail(String email) throws Exception;
    User selectUserByIdEmail(String id, String email) throws Exception;
    void updatePassword(User user) throws Exception;
    List<User> selectAllUser() throws Exception;
    List<User> selectEndedPlanYesterday() throws Exception;
    List<User> selectOneDayBeforeTravel() throws Exception;
    List<User> selectThreeDayBeforeTravel() throws Exception;
    List<User> selectWeekBeforeTravel() throws Exception;
}
