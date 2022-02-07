package com.ssafy.groute.src.service

import com.ssafy.groute.src.dto.Theme
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Response

class ThemeService {
    suspend fun getThemeById(id: Int) : Response<Theme> {
        return RetrofitUtil.themeService.getThemeById(id)
    }
}