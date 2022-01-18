package com.ssafy.groute.service;

import com.ssafy.groute.dto.Area;
import com.ssafy.groute.mapper.AreaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    AreaMapper areaMapper;

    @Override
    public void insertArea(Area area) throws Exception {
        areaMapper.insertArea(area);
    }

    @Override
    public Area selectArea(int id) throws Exception {
        return areaMapper.selectArea(id);
    }

    @Override
    public List<Area> selectAllArea() throws Exception {
        return areaMapper.selectAllArea();
    }

    @Override
    public void deleteArea(int id) throws Exception {
        areaMapper.deleteArea(id);
    }

    @Override
    public void updateArea(Area area) throws Exception {
        areaMapper.updateArea(area);
    }


}
