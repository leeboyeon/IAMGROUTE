package com.ssafy.groute.service;

import com.ssafy.groute.dto.Area;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AreaService {
    void insertArea(Area area) throws Exception;
    Area selectArea(String name) throws Exception;
    List<Area> selectAllArea() throws Exception;
}
