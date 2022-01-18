package com.ssafy.groute.service;

import com.ssafy.groute.dto.Area;
import com.ssafy.groute.mapper.AreaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    AreaMapper areaMapper;

    @Override
    public void insertArea(Area area) throws Exception {
        areaMapper.insertArea(area);
    }
}
