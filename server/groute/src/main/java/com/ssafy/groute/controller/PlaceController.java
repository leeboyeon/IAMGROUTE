package com.ssafy.groute.controller;

import com.ssafy.groute.dto.Place;
import com.ssafy.groute.dto.PlaceLike;
import com.ssafy.groute.service.PlaceService;
import com.ssafy.groute.service.StorageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
public class PlaceController {
    @Autowired
    PlaceService placeService;
    private final StorageService storageService;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @ApiOperation(value = "place 추가",notes = "place 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertPlace(@RequestBody Place req) throws Exception{
//        if (file != null) {
//            String fileName = storageService.store(file, uploadPath + "/place");
//            req.setImg(fileName);
//        } else {
//            req.setImg(null);
//        }
        try {
            placeService.insertPlace(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "place 검색",notes = "이름으로 place 하나 검색")
    @GetMapping(value = "/detail")
    public ResponseEntity<?> detailPlace(@RequestParam("id") int id) throws Exception{

        Place res = placeService.selectPlace(id);
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Place>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list place",notes = "모든 place 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listPlace() throws Exception{

        List<Place> res = placeService.selectAllPlace();
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Place>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete place",notes = "place 삭제")
    @DeleteMapping(value = "/del")
    public ResponseEntity<?> deletePlace(@RequestParam("id") int id) throws Exception{

        try {
            placeService.deletePlace(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "updatePlace",notes = "place 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updatePlace(@RequestBody Place place) throws Exception{
        try {
            placeService.updatePlace(place);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "isLike",notes = "해당 place 좋아요 했는지")
    @PostMapping(value ="/isLike")
    public ResponseEntity<?> isLike(@RequestBody PlaceLike placeLike){
        try {
            PlaceLike p = placeService.isLike(placeLike);
            if(p == null){
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "placeLike",notes = "해당 place 좋아요 또는 좋아요취소")
    @PostMapping(value = "/like")
    public ResponseEntity<?> placeLike(@RequestBody PlaceLike placeLike){
        try {
            placeService.likePlace(placeLike);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @ApiOperation(value = "placeBest",notes = "인기 관광지 다섯개")
    @GetMapping(value = "/best")
    public ResponseEntity<?> bestPlaceFive() throws Exception{
        List<Place> res = placeService.bestPlace();
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Place>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "my place",notes = "내가 좋아요한 place 리스트 반환")
    @GetMapping(value = "/like/{userId}")
    public ResponseEntity<?> myPlace(@PathVariable("userId") String userId) throws Exception{
        List<Place> res = placeService.selectAllPlaceIdByUserId(userId);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Place>>(res,HttpStatus.OK);
    }

}
