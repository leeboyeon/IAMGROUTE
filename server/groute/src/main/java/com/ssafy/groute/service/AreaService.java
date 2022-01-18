package com.ssafy.groute.service;

import com.ssafy.groute.dto.Area;
import org.springframework.stereotype.Service;

@Service
public interface AreaService {
    void insertArea(Area area) throws Exception;
}
