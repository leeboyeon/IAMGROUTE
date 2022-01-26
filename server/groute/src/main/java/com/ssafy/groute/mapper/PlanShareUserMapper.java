package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.PlanShareUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanShareUserMapper {
    void insertPlanShareUser(PlanShareUser planShareUser) throws Exception;
    PlanShareUser selectPlanShareUser(int id) throws Exception;
    List<PlanShareUser> selectAllPlanShareUser() throws Exception;
    void deletePlanShareUser(int id) throws Exception;
    void updatePlanShareUser(PlanShareUser planShareUser) throws Exception;
    List<PlanShareUser> selectByUserId(String userId) throws Exception;
    List<PlanShareUser> selectByPlanId(int planId) throws Exception;
    void deleteAllPlanShareUserByUId(String userId) throws Exception;
}
