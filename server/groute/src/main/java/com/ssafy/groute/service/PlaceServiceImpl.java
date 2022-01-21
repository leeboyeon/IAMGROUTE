package com.ssafy.groute.service;

import com.ssafy.groute.dto.Place;
import com.ssafy.groute.mapper.PlaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {
    @Autowired
    PlaceMapper placeMapper;

    @Override
    public void insertPlace(Place place) throws Exception {
        placeMapper.insertPlace(place);
    }

    @Override
    public Place selectPlace(int id) throws Exception {
        return placeMapper.selectPlace(id);
    }

    @Override
    public List<Place> selectAllPlace() throws Exception {
        return placeMapper.selectAllPlace();
    }

    @Override
    public void deletePlace(int id) throws Exception {
        placeMapper.deletePlace(id);
    }

    @Override
    public void updatePlace(Place place) throws Exception {
        placeMapper.updatePlace(place);
    }

    @Override
    public void modifyImgNames() throws Exception {
        List<Place> places = placeMapper.selectAllPlace();

        for(Place p: places){
            File file = new File("C:/Users/pdj16/Desktop/place/"+p.getName()+".jpg");
            File newFile = new File("C:/Users/pdj16/Desktop/place/"+p.getType()+"/"+p.getName()+".jpg");
            if(file.renameTo(newFile)){
            }else{
                System.out.println("fail: "+newFile.getPath());
            }
        }
    }
}
