package com.ssafy.groute.service;

import com.ssafy.groute.dto.UserPlan;
import com.ssafy.groute.mapper.UserPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPlanServiceImpl implements UserPlanService {
    @Autowired
    UserPlanMapper userPlanMapper;

    @Override
    public void insertUserPlan(UserPlan userPlan) throws Exception {
        userPlanMapper.insertUserPlan(userPlan);
    }

    @Override
    public UserPlan selectUserPlan(int id) throws Exception {
        return userPlanMapper.selectUserPlan(id);
    }

    @Override
    public List<UserPlan> selectAllUserPlan() throws Exception {
        return userPlanMapper.selectAllUserPlan();
    }

    @Override
    public void deleteUserPlan(int id) throws Exception {
        userPlanMapper.deleteUserPlan(id);
    }

    @Override
    public void updateUserPlan(UserPlan userPlan) throws Exception {
        userPlanMapper.updateUserPlan(userPlan);
    }
}
