package com.ssafy.groute.service;

import com.ssafy.groute.dto.RouteDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RouteDetailService {
    void insertRouteDetail(RouteDetail routeDetail) throws Exception;
    RouteDetail selectRouteDetail(int id) throws Exception;
    List<RouteDetail> selectAllRouteDetail() throws Exception;
    void deleteRouteDetail(int id) throws Exception;
    void updateRouteDetail(RouteDetail routeDetail) throws Exception;
}
