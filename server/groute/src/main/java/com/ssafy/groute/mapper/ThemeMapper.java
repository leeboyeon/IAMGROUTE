package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.Theme;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ThemeMapper {
    void insertTheme(Theme theme) throws Exception;
    Theme selectTheme(int id) throws Exception;
    List<Theme> selectAllTheme() throws Exception;
    void deleteTheme(int id) throws Exception;
    void updateTheme(Theme theme) throws Exception;
}
