package com.ssafy.groute.controller.board;

import com.ssafy.groute.dto.board.BoardDetail;
import com.ssafy.groute.dto.board.Comment;
import com.ssafy.groute.service.board.BoardDetailService;
import com.ssafy.groute.service.board.CommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
@RequestMapping("/boardDetail")
public class BoardDetailController {


    @Autowired
    BoardDetailService boardDetailService;
    @Autowired
    CommentService commentService;

    @ApiOperation(value = "boardDetail 추가",notes = "boardDetail 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertBoardDetail(@RequestBody BoardDetail req){

        try {
            boardDetailService.insertBoardDetail(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "boardDetail 검색",notes = "이름으로 boardDetail 하나 검색")
    @GetMapping(value = "/detail")
    public ResponseEntity<?> detailBoardDetail(@RequestParam("id") int id) throws Exception{
        Map<String,Object> res = new HashMap<>();
        BoardDetail board = boardDetailService.selectBoardDetail(id);
        List<Comment> comments = commentService.selectAllByBoardDetailId(id);
        res.put("boardDetail",board);
        res.put("comments",comments);
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Map<String,Object>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list boardDetail",notes = "모든 boardDetail 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listBoardDetail() throws Exception{

        List<BoardDetail> res = boardDetailService.selectAllBoardDetail();
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<BoardDetail>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete boardDetail",notes = "boardDetail 삭제")
    @DeleteMapping(value = "/del")
    public ResponseEntity<?> deleteBoardDetail(@RequestParam("id") int id) throws Exception{

        try {
            boardDetailService.deleteBoardDetail(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "updateBoardDetail",notes = "boardDetail 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateBoardDetail(@RequestBody BoardDetail boardDetail) throws Exception{

        try {
            boardDetailService.updateBoardDetail(boardDetail);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }
}
