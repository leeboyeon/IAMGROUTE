package com.ssafy.groute.service;

import com.ssafy.groute.dto.*;
import com.ssafy.groute.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Autowired
    PlaceMapper placeMapper;

    @Override
    @Transactional
    public void insertUserPlan(UserPlan userPlan, List<String> userIds, int planId) throws Exception {
        userPlanMapper.insertUserPlan(userPlan);
        for(String userId: userIds){
            planShareUserMapper.insertPlanShareUser(new PlanShareUser(userId, userPlan.getId()));
        }
        for(int i=1;i<=userPlan.getTotalDate();i++) {
            Route route = new Route("Day" + i, i, "", "Y");
            routeMapper.insertRoute(route);
            routesMapper.insertRoutes(new Routes(route.getId(),userPlan.getId()));
        }
        if(planId!=0){
            copyPlan(userPlan, planId, 0);
        }
    }

    @Override
    public void copyPlan(UserPlan userPlan, int planId, int day) throws Exception{
        UserPlan originPlan = userPlanMapper.selectUserPlan(planId);
        List<Routes> userRoutesList = routesMapper.selectByPlanId(userPlan.getId());
        List<Routes> originRoutesList = routesMapper.selectByPlanId(originPlan.getId());


        List<Routes> routesList = routesMapper.selectByPlanId(userPlan.getId());
        for(Routes routes:routesList){
            int routeId = routes.getRouteId();
            if(day == 0 || routeMapper.selectRoute(routeId).getDay()==day){
                routeDetailMapper.deleteAllRouteDetailByRouteId(routeId);
            }
        }


        int i=0;
        for(Routes routes: originRoutesList){
            List<RouteDetail> routeDetailList = routeDetailMapper.selectByRouteId(routes.getRouteId());
            int routeId = 0;
            if(day==0) {
                routeId = userRoutesList.get(i++).getRouteId();
            }else{
                for(Routes userRoutes:userRoutesList){
                    if(routeMapper.selectRoute(userRoutes.getRouteId()).getDay()==day){
                        routeId = userRoutes.getRouteId();
                    }
                }
            }
            for(RouteDetail routeDetail:routeDetailList){
                RouteDetail newRouteDetail = routeDetail;
                newRouteDetail.setRouteId(routeId);
                routeDetailMapper.insertRouteDetail(newRouteDetail);
            }
        }
    }

    @Override
    public List<UserPlan> selectAllByPlaceId(List<Integer> placeIds, int day) throws Exception {
        List<Integer> planIdList = userPlanMapper.selectAllUserPlanByTotalDate(day);
        List<UserPlan> userPlans = new ArrayList<>();
        for(int planId: planIdList){
            List<Integer> placeList = userPlanMapper.selectPlaceListByPlanId(planId);
            if(placeList.containsAll(placeIds)){
                userPlans.add(userPlanMapper.selectUserPlan(planId));
            }
        }
        return userPlans;
    }

    @Override
    public Map<String,Object> selectUserPlan(int id) throws Exception {
        Map<String,Object> res = new HashMap<>();
        UserPlan userPlan = userPlanMapper.selectUserPlan(id);
        res.put("userPlan",userPlan);
        List<Routes> routesList = routesMapper.selectByPlanId(userPlan.getId());
        List<Route> routeList = new ArrayList<>();
        for(Routes r: routesList) {
            Route route = routeMapper.selectRoute(r.getRouteId());
            List<RouteDetail> routeDetailList = routeDetailMapper.selectByRouteId(r.getRouteId());
            for(RouteDetail routeDetail: routeDetailList){
                routeDetailList.get(routeDetail.getPriority()-1)
                        .setPlace(placeMapper.selectPlace(routeDetail.getPlaceId()));
            }
            route.setRouteDetailList(routeDetailList);;
            routeList.add(route);
        }
        res.put("routeList",routeList);
        return res;
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
            routesMapper.deleteRoutes(r.getId());
            routeMapper.deleteRoute(r.getRouteId());
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
