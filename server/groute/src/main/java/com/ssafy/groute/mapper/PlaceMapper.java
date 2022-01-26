package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.Place;
import com.ssafy.groute.dto.PlaceLike;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlaceMapper {
    void insertPlace(Place place) throws Exception;
    Place selectPlace(int id) throws Exception;
    List<Place> selectAllPlace() throws Exception;
    void deletePlace(int id) throws Exception;
    void updatePlace(Place place) throws Exception;
    void likePlace(PlaceLike placeLike) throws Exception;
    void deleteAllPlaceByUId(String userId) throws Exception;
    List<Integer> findAllPlaceByUId(String userId) throws Exception;
}
