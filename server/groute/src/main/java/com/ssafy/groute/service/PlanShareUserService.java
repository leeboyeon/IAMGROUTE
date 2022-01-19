package com.ssafy.groute.service;

import com.ssafy.groute.dto.PlanShareUser;

import java.util.List;

public interface PlanShareUserService {
    void insertPlanShareUser(PlanShareUser planShareUser) throws Exception;
    PlanShareUser selectPlanShareUser(int id) throws Exception;
    List<PlanShareUser> selectAllPlanShareUser() throws Exception;
    void deletePlanShareUser(int id) throws Exception;
    void updatePlanShareUser(PlanShareUser planShareUser) throws Exception;
}
