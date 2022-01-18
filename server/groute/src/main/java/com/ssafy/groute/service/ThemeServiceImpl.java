package com.ssafy.groute.service;

import com.ssafy.groute.dto.Theme;
import com.ssafy.groute.mapper.ThemeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeServiceImpl implements ThemeService {

    @Autowired
    ThemeMapper themeMapper;

    @Override
    public void insertTheme(Theme theme) throws Exception {
        themeMapper.insertTheme(theme);
    }

    @Override
    public Theme selectTheme(String name) throws Exception {
        return themeMapper.selectTheme(name);
    }

    @Override
    public List<Theme> selectAllTheme() throws Exception {
        return themeMapper.selectAllTheme();
    }

    @Override
    public void deleteTheme(int id) throws Exception {
        themeMapper.deleteTheme(id);
    }

    @Override
    public void updateTheme(Theme theme) throws Exception {
        themeMapper.updateTheme(theme);
    }
}
