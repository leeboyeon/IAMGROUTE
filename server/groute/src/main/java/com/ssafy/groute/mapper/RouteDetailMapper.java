package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.Route;
import com.ssafy.groute.dto.RouteDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RouteDetailMapper {
    void insertRouteDetail(RouteDetail routeDetail) throws Exception;
    RouteDetail selectRouteDetail(int id) throws Exception;
    List<RouteDetail> selectAllRouteDetail() throws Exception;
    void deleteRouteDetail(int id) throws Exception;
    void updateRouteDetail(RouteDetail routeDetail) throws Exception;
    List<RouteDetail> selectByRouteId(int routeId) throws Exception;
    List<RouteDetail> selectByPlaceId(int placeId) throws Exception;
}
