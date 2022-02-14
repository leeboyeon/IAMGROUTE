package com.ssafy.groute.service;

import com.ssafy.groute.dto.Place;
import com.ssafy.groute.dto.PlaceReview;
import com.ssafy.groute.mapper.PlaceMapper;
import com.ssafy.groute.mapper.PlaceReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlaceReviewServiceImpl implements PlaceReviewService {
    @Autowired
    PlaceReviewMapper placeReviewMapper;
    @Autowired
    PlaceMapper placeMapper;
    @Transactional
    @Override
    public void insertPlaceReview(PlaceReview placeReview) throws Exception {
        placeReviewMapper.insertPlaceReview(placeReview);
        Place place = placeMapper.selectPlace(placeReview.getPlaceId());
        double rate = placeReviewMapper.selectAvgRateByPlaceId(placeReview.getPlaceId());
        place.setRate(rate);
        placeMapper.updatePlace(place);
    }

    @Override
    public PlaceReview selectPlaceReview(int id) throws Exception {
        return placeReviewMapper.selectPlaceReview(id);
    }

    @Override
    public List<PlaceReview> selectAllPlaceReview() throws Exception {
        return placeReviewMapper.selectAllPlaceReview();
    }

    @Transactional
    @Override
    public void deletePlaceReview(int id) throws Exception {
        PlaceReview placeReview = selectPlaceReview(id);
        Place place = placeMapper.selectPlace(placeReview.getPlaceId());
        placeReviewMapper.deletePlaceReview(id);
        double rate = placeReviewMapper.selectAvgRateByPlaceId(placeReview.getPlaceId());
        place.setRate(rate);
        placeMapper.updatePlace(place);
    }

    @Override
    public void updatePlaceReview(PlaceReview placeReview) throws Exception {
        placeReviewMapper.updatePlaceReview(placeReview);
    }

    @Override
    public List<PlaceReview> selectByUserId(String userId) throws Exception {
        return placeReviewMapper.selectByUserId(userId);
    }

    @Override
    public List<PlaceReview> selectByPlaceId(int planId) throws Exception {
        return placeReviewMapper.selectByPlaceId(planId);
    }

}
