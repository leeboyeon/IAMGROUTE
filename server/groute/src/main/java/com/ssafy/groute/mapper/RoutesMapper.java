package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.Routes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoutesMapper {
    void insertRoutes(Routes routes) throws Exception;
    Routes selectRoutes(int id) throws Exception;
    List<Routes> selectAllRoutes() throws Exception;
    void deleteRoutes(int id) throws Exception;
    void updateRoutes(Routes routes) throws Exception;
    List<Routes> selectByRouteId(int routeId) throws Exception;
    List<Routes> selectByPlanId(int planId) throws Exception;
    List<Integer> findAllRoutesIdsByPId(int planId) throws Exception;
    void deleteAllRoutesByPId(int planId) throws Exception;
}
