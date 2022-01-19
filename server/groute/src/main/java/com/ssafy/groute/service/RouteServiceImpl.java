package com.ssafy.groute.service;

import com.ssafy.groute.dto.Route;
import com.ssafy.groute.mapper.RouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    RouteMapper routeMapper;

    @Override
    public void insertRoute(Route route) throws Exception {
        routeMapper.insertRoute(route);
    }

    @Override
    public Route selectRoute(int id) throws Exception {
        return routeMapper.selectRoute(id);
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
