package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.PlaceReview;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlaceReviewMapper {
    void insertPlaceReview(PlaceReview placeReview) throws Exception;
    PlaceReview selectPlaceReview(int id) throws Exception;
    List<PlaceReview> selectAllPlaceReview() throws Exception;
    void deletePlaceReview(int id) throws Exception;
    void updatePlaceReview(PlaceReview placeReview) throws Exception;
    List<PlaceReview> selectByUserId(String userId) throws Exception;
    List<PlaceReview> selectByPlaceId(int planId) throws Exception;
    void deletePlaceReviewByUserId(String userId) throws Exception;
    void deletePlaceReviewByPlaceId(int placeId) throws Exception;
    int selectAvgRateByPlaceId(int placeId) throws Exception;
    int selectCntRateByPlaceId(int placeId) throws Exception;
}
