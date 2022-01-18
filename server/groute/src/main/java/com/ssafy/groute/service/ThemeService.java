package com.ssafy.groute.service;

import com.ssafy.groute.dto.Theme;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ThemeService {
    void insertTheme(Theme theme) throws Exception;
    Theme selectTheme(int id) throws Exception;
    List<Theme> selectAllTheme() throws Exception;
    void deleteTheme(int id) throws Exception;
    void updateTheme(Theme theme) throws Exception;
}
