package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.PlanShareUser;

import java.util.List;

public interface PlanShareUserMapper {
    void insertPlanShareUser(PlanShareUser planShareUser) throws Exception;
    PlanShareUser selectPlanShareUser(int id) throws Exception;
    List<PlanShareUser> selectAllPlanShareUser() throws Exception;
    void deletePlanShareUser(int id) throws Exception;
    void updatePlanShareUser(PlanShareUser planShareUser) throws Exception;
}
