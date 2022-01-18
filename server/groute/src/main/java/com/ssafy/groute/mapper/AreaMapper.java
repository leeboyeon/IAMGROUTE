package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.Area;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AreaMapper {
    void insertArea(Area area) throws Exception;
}
