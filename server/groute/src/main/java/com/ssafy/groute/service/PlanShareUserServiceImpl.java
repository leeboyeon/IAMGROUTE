package com.ssafy.groute.service;

import com.ssafy.groute.dto.PlanShareUser;
import com.ssafy.groute.mapper.PlanShareUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanShareUserServiceImpl implements PlanShareUserService {
    @Autowired
    PlanShareUserMapper planShareUserMapper;

    @Override
    public void insertPlanShareUser(PlanShareUser planShareUser) throws Exception {
        planShareUserMapper.insertPlanShareUser(planShareUser);
    }

    @Override
    public PlanShareUser selectPlanShareUser(int id) throws Exception {
        return planShareUserMapper.selectPlanShareUser(id);
    }

    @Override
    public List<PlanShareUser> selectAllPlanShareUser() throws Exception {
        return planShareUserMapper.selectAllPlanShareUser();
    }

    @Override
    public void deletePlanShareUser(int id) throws Exception {
        planShareUserMapper.deletePlanShareUser(id);
    }

    @Override
    public void updatePlanShareUser(PlanShareUser planShareUser) throws Exception {
        planShareUserMapper.updatePlanShareUser(planShareUser);
    }
}
