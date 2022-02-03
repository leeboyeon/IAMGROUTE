package com.ssafy.groute.service;

import com.ssafy.groute.dto.PlaceReview;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlaceReviewService {
    void insertPlaceReview(PlaceReview placeReview) throws Exception;
    PlaceReview selectPlaceReview(int id) throws Exception;
    List<PlaceReview> selectAllPlaceReview() throws Exception;
    void deletePlaceReview(int id) throws Exception;
    void updatePlaceReview(PlaceReview placeReview) throws Exception;
    List<PlaceReview> selectByUserId(String userId) throws Exception;
    List<PlaceReview> selectByPlaceId(int planId) throws Exception;
}
