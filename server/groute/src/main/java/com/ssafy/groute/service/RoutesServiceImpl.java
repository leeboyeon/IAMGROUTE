package com.ssafy.groute.service;

import com.ssafy.groute.dto.Routes;
import com.ssafy.groute.dto.Routes;
import com.ssafy.groute.mapper.RoutesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutesServiceImpl implements RoutesService {
    @Autowired
    RoutesMapper routesMapper;

    @Override
    public void insertRoutes(Routes routes) throws Exception {
        routesMapper.insertRoutes(routes);
    }

    @Override
    public Routes selectRoutes(int id) throws Exception {
        return routesMapper.selectRoutes(id);
    }

    @Override
    public List<Routes> selectAllRoutes() throws Exception {
        return routesMapper.selectAllRoutes();
    }

    @Override
    public void deleteRoutes(int id) throws Exception {
        routesMapper.deleteRoutes(id);
    }

    @Override
    public void updateRoutes(Routes routes) throws Exception {
        routesMapper.updateRoutes(routes);
    }

    @Override
    public List<Routes> selectByRouteId(int routeId) throws Exception {
        return routesMapper.selectByRouteId(routeId);
    }

    @Override
    public List<Routes> selectByPlanId(int planId) throws Exception {
        return routesMapper.selectByPlanId(planId);
    }
}
