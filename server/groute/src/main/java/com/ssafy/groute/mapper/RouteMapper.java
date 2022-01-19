package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.Route;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RouteMapper {
    void insertRoute(Route route) throws Exception;
    Route selectRoute(int id) throws Exception;
    List<Route> selectAllRoute() throws Exception;
    void deleteRoute(int id) throws Exception;
    void updateRoute(Route route) throws Exception;
}
