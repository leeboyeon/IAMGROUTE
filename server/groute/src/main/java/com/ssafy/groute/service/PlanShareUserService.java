package com.ssafy.groute.service;

import com.ssafy.groute.dto.PlanShareUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlanShareUserService {
    void insertPlanShareUser(PlanShareUser planShareUser) throws Exception;
    PlanShareUser selectPlanShareUser(int id) throws Exception;
    List<PlanShareUser> selectAllPlanShareUser() throws Exception;
    void deletePlanShareUser(int id) throws Exception;
    void updatePlanShareUser(PlanShareUser planShareUser) throws Exception;
    List<PlanShareUser> selectByUserId(String userId) throws Exception;
    List<PlanShareUser> selectByPlanId(int planId) throws Exception;
}
