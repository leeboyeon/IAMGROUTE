package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.Area;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AreaMapper {
    void insertArea(Area area) throws Exception;
    Area selectArea(String name) throws Exception;
    List<Area> selectAllArea() throws Exception;
    void deleteArea(int id) throws Exception;
    void updateArea(Area area) throws Exception;
}
