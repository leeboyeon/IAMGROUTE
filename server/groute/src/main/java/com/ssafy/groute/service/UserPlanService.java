package com.ssafy.groute.service;

import com.ssafy.groute.dto.UserPlan;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserPlanService {
    void insertUserPlan(UserPlan userPlan) throws Exception;
    UserPlan selectUserPlan(int id) throws Exception;
    List<UserPlan> selectAllUserPlan() throws Exception;
    void deleteUserPlan(int id) throws Exception;
    void updateUserPlan(UserPlan userPlan) throws Exception;
}
