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
            if(planShareUserMapper.selectByUserIdPlanId(new PlanShareUser(userId,userPlan.getId()))==0) {
                planShareUserMapper.insertPlanShareUser(new PlanShareUser(userId, userPlan.getId()));
            }
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

    @Transactional
    @Override
    public void copyPlan(UserPlan userPlan, int planId, int day) throws Exception{
        UserPlan originPlan = userPlanMapper.selectUserPlan(planId);
        List<Routes> userRoutesList = routesMapper.selectByPlanId(userPlan.getId());
        List<Routes> originRoutesList = routesMapper.selectByPlanId(originPlan.getId());

        for(Routes routes:userRoutesList){
            int routeId = routes.getRouteId();
            if(day <= routeMapper.selectRoute(routeId).getDay() && routeMapper.selectRoute(routeId).getDay() < day + originPlan.getTotalDate()){
                routeDetailMapper.deleteAllRouteDetailByRouteId(routeId);
            }
        }

        for(Routes routes: originRoutesList){
            if(day>userPlan.getTotalDate())
                break;
            List<RouteDetail> routeDetailList = routeDetailMapper.selectByRouteId(routes.getRouteId());
            int routeId = userRoutesList.get((day++)-1).getRouteId();
            for(RouteDetail routeDetail:routeDetailList){
                RouteDetail newRouteDetail = routeDetail;
                newRouteDetail.setRouteId(routeId);
                routeDetailMapper.insertRouteDetail(newRouteDetail);
            }
        }
    }

    @Transactional
    @Override
    public List<UserPlan> selectAllByPlaceId(List<Integer> placeIds,int flag) throws Exception {
        List<UserPlan> planList = selectTUserPlan();
        if(placeIds == null || placeIds.size()==0) {
            return planList;
        }
        List<UserPlan> userPlans = new ArrayList<>();
        if(flag==1) {
            for (UserPlan userPlan : planList) {
                List<Integer> placeList = userPlanMapper.selectPlaceListByPlanId(userPlan.getId());
                if (placeList.containsAll(placeIds)) {
                    userPlans.add(userPlan);
                }
            }
        }else{
            for (UserPlan userPlan : planList) {
                List<Integer> placeList = userPlanMapper.selectPlaceListByPlanId(userPlan.getId());
                placeList.retainAll(placeIds);
                if (placeList.size()==0) {
                    userPlans.add(userPlan);
                }
            }
        }
        return userPlans;
    }

    @Override
    public List<UserPlan> bestPlanList() throws Exception {

        return userPlanMapper.bestPlanList();
    }

    @Override
    public List<UserPlan> findEndPlanById(String userId) throws Exception {
        return userPlanMapper.findEndPlanById(userId);
    }

    @Override
    public List<UserPlan> findNotEndPlanById(String userId) throws Exception {
        return userPlanMapper.findNotEndPlanById(userId);
    }

    @Override
    public List<UserPlan> selectTUserPlan() throws Exception {
        return setThemeList(userPlanMapper.selectTUserPlan());
    }

    @Override
    public List<UserPlan> selectAllPlanByUserId(String userId) throws Exception {
        return userPlanMapper.selectAllPlanByUserId(userId);
    }

    @Transactional
    @Override
    public Map<String,Object> selectUserPlan(int id) throws Exception {
        Map<String,Object> res = new HashMap<>();
        UserPlan userPlan = userPlanMapper.selectUserPlan(id);

        List<Routes> routesList = routesMapper.selectByPlanId(userPlan.getId());
        List<Route> routeList = new ArrayList<>();
        for(Routes r: routesList) {
            Route route = routeMapper.selectRoute(r.getRouteId());
            List<RouteDetail> routeDetailList = routeDetailMapper.selectByRouteId(r.getRouteId());
            for(RouteDetail routeDetail: routeDetailList){
                routeDetailList.get(routeDetail.getPriority()-1)
                        .setPlace(placeMapper.selectPlace(routeDetail.getPlaceId()));
            }
            route.setRouteDetailList(routeDetailList);
            routeList.add(route);
        }
        userPlan.setThemeIdList(userPlanMapper.selectThemeListByPlanId(userPlan.getId()));
        res.put("userPlan",userPlan);
        res.put("routeList",routeList);
        return res;
    }

    @Override
    public List<UserPlan> selectAllUserPlan() throws Exception {
        return setThemeList(userPlanMapper.selectAllUserPlan());
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
        accountMapper.deleteByUserPlanId(id);
        userPlanMapper.deleteUserPlan(id);
    }

    @Override
    public void updateUserPlan(UserPlan userPlan) throws Exception {
        userPlanMapper.updateUserPlan(userPlan);
    }

    @Transactional
    @Override
    public List<UserPlan> selectAllUserPlanById(String id) throws Exception {
        return userPlanMapper.selectAllUserPlanByUserId(id);
    }

    @Transactional
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

    public List<UserPlan> setThemeList(List<UserPlan> userPlans) throws Exception{

        for(UserPlan userPlan:userPlans){
            userPlan.setThemeIdList(userPlanMapper.selectThemeListByPlanId(userPlan.getId()));
        }

        return userPlans;
    }

    @Override
    public void deleteRouteDetail(int routeDetailId) throws Exception {
        RouteDetail routeDetail = routeDetailMapper.selectRouteDetail(routeDetailId);
        routeDetailMapper.deleteRouteDetail(routeDetailId);
        List<RouteDetail> routeDetailList = routeDetailMapper.selectByRouteId(routeDetail.getRouteId());
        for(int i=1;i<=routeDetailList.size();i++){
            routeDetailList.get(i-1).setPriority(i);
            routeDetailMapper.updatePriority(routeDetailList.get(i-1));
        }

    }


    private List<RouteDetail> routeDetailList;
    private List<Place> placeList;
    private double min;
    private int[] minArr;
    private boolean[] isSelected;

    @Override
    public List<RouteDetail> shortestPath(int start, int end, int routeId) throws Exception{
        routeDetailList = routeDetailMapper.selectByRouteId(routeId);

        placeList = new ArrayList<>();
        placeList.add(placeMapper.selectPlace(routeDetailList.get(start-1).getPlaceId()));
        for(int i=1;i<=routeDetailList.size();i++){
            if(i==start || i== end)
                continue;
            placeList.add(placeMapper.selectPlace(routeDetailList.get(i-1).getPlaceId()));
        }
        placeList.add(placeMapper.selectPlace(routeDetailList.get(end-1).getPlaceId()));
        isSelected = new boolean[placeList.size()];
        min = Double.MAX_VALUE;
        perm(0,0, 0, new int[placeList.size()-2]);


        for(int i=0; i<minArr.length; i++){
            for(int j=0; j<routeDetailList.size();j++){
                if(routeDetailList.get(j).getPlaceId()==placeList.get(minArr[i]).getId()){
                    routeDetailList.get(j).setPriority(i+2);
                }
            }
        }
        routeDetailList.get(start-1).setPriority(1);
        routeDetailList.get(end-1).setPriority(routeDetailList.size());

        return routeDetailList;
    }

    private void perm(int cnt, double sum, int cur, int[] arr){
        if(sum>=min)
            return;
        if(cnt == placeList.size()-2){

            sum += calDistance(placeList.get(arr[arr.length-1]),placeList.get(placeList.size()-1));
            if(sum < min){
                min = sum;
                minArr = arr.clone();
            }
            return;
        }

        for(int i=1; i<placeList.size() - 1; i++){
            if(isSelected[i])
                continue;
            isSelected[i] = true;
            arr[cnt] = i;
            perm(cnt+1,sum+calDistance(placeList.get(cur),placeList.get(i)),i,arr);
            isSelected[i] = false;
        }
    }

    private double calDistance(Place a, Place b){
        return Math.pow(Math.abs(Double.parseDouble(a.getLat()) - Double.parseDouble(b.getLat())),2)
                +Math.pow(Math.abs(Double.parseDouble(a.getLng()) - Double.parseDouble(b.getLng())),2);
    }
}
