package com.ssafy.groute.service;

import com.ssafy.groute.dto.Route;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RouteService {
    void insertRoute(Route route) throws Exception;
    Route selectRoute(int id) throws Exception;
    List<Route> selectAllRoute() throws Exception;
    void deleteRoute(int id) throws Exception;
    void updateRoute(Route route) throws Exception;
    List<Route> selectBestRoute() throws Exception;
}
