package com.ssafy.groute.service;

import com.ssafy.groute.dto.*;
import com.ssafy.groute.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserPlanServiceImpl implements UserPlanService {
    @Autowired
    UserPlanMapper userPlanMapper;
    @Autowired
    PlanShareUserMapper planShareUserMapper;
    @Autowired
    RouteMapper routeMapper;
    @Autowired
    RoutesMapper routesMapper;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    RouteDetailMapper routeDetailMapper;

    @Override
    @Transactional
    public void insertUserPlan(UserPlan userPlan, List<String> userIds) throws Exception {
        userPlanMapper.insertUserPlan(userPlan);
        for(String userId: userIds){
            planShareUserMapper.insertPlanShareUser(new PlanShareUser(userId, userPlan.getId()));
        }
        for(int i=1;i<=userPlan.getTotalDate();i++) {
            Route route = new Route("Day" + i, i, "", "Y");
            routeMapper.insertRoute(route);
            routesMapper.insertRoutes(new Routes(route.getId(),userPlan.getId()));
        }
    }

    @Override
    public UserPlan selectUserPlan(int id) throws Exception {
        return userPlanMapper.selectUserPlan(id);
    }

    @Override
    public List<UserPlan> selectAllUserPlan() throws Exception {
        return userPlanMapper.selectAllUserPlan();
    }

    @Transactional
    @Override
    public void deleteUserPlan(int id) throws Exception {
        List<PlanShareUser> planShareUsers = planShareUserMapper.selectByPlanId(id);
        for(PlanShareUser p: planShareUsers){
            planShareUserMapper.deletePlanShareUser(p.getId());
        }
        List<Routes> routes = routesMapper.selectByPlanId(id);
        for(Routes r:routes){
            List<RouteDetail> routeDetails = routeDetailMapper.selectByRouteId(r.getRouteId());
            for(RouteDetail rd: routeDetails){
                routeDetailMapper.deleteRouteDetail(rd.getId());
            }
            routeMapper.deleteRoute(r.getRouteId());
            routesMapper.deleteRoutes(r.getId());
        }
        userPlanMapper.deleteUserPlan(id);
    }

    @Override
    public void updateUserPlan(UserPlan userPlan) throws Exception {
        userPlanMapper.updateUserPlan(userPlan);
    }

    @Override
    public List<UserPlan> selectAllUserPlanById(String id) throws Exception {
        List<PlanShareUser> planShareUsers = planShareUserMapper.selectByUserId(id);
        List<UserPlan> userPlans = new ArrayList<>();
        for(PlanShareUser p: planShareUsers){
            userPlans.add(userPlanMapper.selectUserPlan(p.getPlanId()));
        }
        return userPlans;
    }

    @Override
    public void likePlan(PlanLike planLike) throws Exception {
        PlanLike p = userPlanMapper.isLike(planLike);
        UserPlan plan = userPlanMapper.selectUserPlan(planLike.getUserPlanId());
        if(p==null) {
            userPlanMapper.likePlan(planLike);
            plan.setHeartCnt(plan.getHeartCnt()+1);
            userPlanMapper.updateUserPlan(plan);
        }else{
            userPlanMapper.unLikePlan(p.getId());
            plan.setHeartCnt(plan.getHeartCnt()-1);
            userPlanMapper.updateUserPlan(plan);
        }
    }

    @Override
    public PlanLike isLike(PlanLike planLike) throws Exception {
        return userPlanMapper.isLike(planLike);
    }


}
