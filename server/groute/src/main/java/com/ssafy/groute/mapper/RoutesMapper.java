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
}
