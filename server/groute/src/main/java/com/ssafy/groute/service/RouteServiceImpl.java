package com.ssafy.groute.service;

import com.ssafy.groute.dto.Route;
import com.ssafy.groute.dto.RouteDetail;
import com.ssafy.groute.mapper.PlaceMapper;
import com.ssafy.groute.mapper.RouteDetailMapper;
import com.ssafy.groute.mapper.RouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    RouteMapper routeMapper;
    @Autowired
    RouteDetailMapper routeDetailMapper;
    @Autowired
    PlaceMapper placeMapper;

    @Override
    public void insertRoute(Route route) throws Exception {
        routeMapper.insertRoute(route);
    }

    @Override
    public Route selectRoute(int id) throws Exception {
        Route route = routeMapper.selectRoute(id);
        List<RouteDetail> routeDetailList = routeDetailMapper.selectByRouteId(id);
        for(RouteDetail routeDetail: routeDetailList){
            routeDetailList.get(routeDetail.getPriority()-1)
                    .setPlace(placeMapper.selectPlace(routeDetail.getPlaceId()));
        }
        route.setRouteDetailList(routeDetailList);
        return route;
    }

    @Override
    public List<Route> selectAllRoute() throws Exception {
        return routeMapper.selectAllRoute();
    }

    @Override
    public void deleteRoute(int id) throws Exception {
        routeMapper.deleteRoute(id);
    }

    @Override
    public void updateRoute(Route route) throws Exception {
        routeMapper.updateRoute(route);
    }

}
