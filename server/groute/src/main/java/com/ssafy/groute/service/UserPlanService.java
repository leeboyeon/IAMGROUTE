package com.ssafy.groute.service;

import com.ssafy.groute.dto.User;
import com.ssafy.groute.dto.UserPlan;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserPlanService {
    void insertUserPlan(UserPlan userPlan, List<String> userIds) throws Exception;
    UserPlan selectUserPlan(int id) throws Exception;
    List<UserPlan> selectAllUserPlan() throws Exception;
    void deleteUserPlan(int id) throws Exception;
    void updateUserPlan(UserPlan userPlan) throws Exception;
    List<UserPlan> selectAllUserPlanById(String id) throws Exception;
}