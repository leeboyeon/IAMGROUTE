package com.ssafy.groute.service;

import com.ssafy.groute.dto.RouteDetail;
import com.ssafy.groute.mapper.RouteDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteDetailServiceImpl implements RouteDetailService{

    @Autowired
    RouteDetailMapper routeDetailMapper;

    @Override
    public void insertRouteDetail(RouteDetail routeDetail) throws Exception {
        routeDetailMapper.insertRouteDetail(routeDetail);
    }

    @Override
    public RouteDetail selectRouteDetail(int id) throws Exception {
        return routeDetailMapper.selectRouteDetail(id);
    }

    @Override
    public List<RouteDetail> selectAllRouteDetail() throws Exception {
        return routeDetailMapper.selectAllRouteDetail();
    }

    @Override
    public void deleteRouteDetail(int id) throws Exception {
        routeDetailMapper.deleteRouteDetail(id);
    }

    @Override
    public void updateRouteDetail(RouteDetail routeDetail) throws Exception {
        routeDetailMapper.updateRouteDetail(routeDetail);
    }

    @Override
    public List<RouteDetail> selectByRouteId(int routeId) throws Exception {
        return routeDetailMapper.selectByRouteId(routeId);
    }

    @Override
    public List<RouteDetail> selectByPlaceId(int placeId) throws Exception {
        return routeDetailMapper.selectByPlaceId(placeId);
    }


}
