package com.ssafy.groute.service;

import com.ssafy.groute.dto.Place;
import com.ssafy.groute.dto.PlaceLike;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlaceService {
    void insertPlace(Place place) throws Exception;
    Place selectPlace(int id) throws Exception;
    List<Place> selectAllPlace() throws Exception;
    void deletePlace(int id) throws Exception;
    void updatePlace(Place place) throws Exception;
    void likePlace(PlaceLike placeLike) throws Exception;
}
