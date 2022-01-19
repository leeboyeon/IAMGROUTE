package com.ssafy.groute.service;

import com.ssafy.groute.dto.Routes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoutesService {
    void insertRoutes(Routes routes) throws Exception;
    Routes selectRoutes(int id) throws Exception;
    List<Routes> selectAllRoutes() throws Exception;
    void deleteRoutes(int id) throws Exception;
    void updateRoutes(Routes routes) throws Exception;
}
