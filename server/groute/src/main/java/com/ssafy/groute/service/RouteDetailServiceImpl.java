package com.ssafy.groute.service;

import com.ssafy.groute.dto.RouteDetail;
import com.ssafy.groute.mapper.PlaceMapper;
import com.ssafy.groute.mapper.RouteDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteDetailServiceImpl implements RouteDetailService{

    @Autowired
    RouteDetailMapper routeDetailMapper;
    @Autowired
    PlaceMapper placeMapper;

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
        List<RouteDetail> routeDetailList = routeDetailMapper.selectByRouteId(routeId);
        for(RouteDetail routeDetail: routeDetailList){
            routeDetailList.get(routeDetail.getPriority()-1)
                    .setPlace(placeMapper.selectPlace(routeDetail.getPlaceId()));
        }
        return routeDetailList;
    }

    @Override
    public List<RouteDetail> selectByPlaceId(int placeId) throws Exception {
        return routeDetailMapper.selectByPlaceId(placeId);
    }

    @Override
    public void updatePriority(List<RouteDetail> routeDetailList) throws Exception {
        for(RouteDetail routeDetail: routeDetailList){
            routeDetailMapper.updatePriority(routeDetail);
        }
    }


}
