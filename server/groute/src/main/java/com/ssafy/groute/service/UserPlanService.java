package com.ssafy.groute.service;

import com.ssafy.groute.dto.PlanLike;
import com.ssafy.groute.dto.User;
import com.ssafy.groute.dto.UserPlan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserPlanService {
    void insertUserPlan(UserPlan userPlan, List<String> userIds, int planId) throws Exception;
    Map<String,Object> selectUserPlan(int id) throws Exception;
    List<UserPlan> selectAllUserPlan() throws Exception;
    void deleteUserPlan(int id) throws Exception;
    void updateUserPlan(UserPlan userPlan) throws Exception;
    List<UserPlan> selectAllUserPlanById(String id) throws Exception;
    void likePlan(PlanLike planLike) throws Exception;
    PlanLike isLike(PlanLike planLike) throws Exception;
    void copyPlan(UserPlan userPlan, int planId, int day) throws Exception;
    List<UserPlan> selectAllByPlaceId(List<Integer> placeIds, int day) throws Exception;
    List<UserPlan> bestPlanList() throws Exception;
}