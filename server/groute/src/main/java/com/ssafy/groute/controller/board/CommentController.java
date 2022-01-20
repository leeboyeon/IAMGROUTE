package com.ssafy.groute.controller.board;


import com.ssafy.groute.dto.board.Comment;
import com.ssafy.groute.service.board.CommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
@RequestMapping("/comment")
public class CommentController {


    @Autowired
    CommentService commentService;

    @ApiOperation(value = "comment 추가",notes = "comment 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertComment(@RequestBody Comment req){

        try {
            commentService.insertComment(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "comment 검색",notes = "이름으로 comment 하나 검색")
    @GetMapping(value = "/detail")
    public ResponseEntity<?> detailComment(@RequestParam("id") int id) throws Exception{

        Comment res = commentService.selectComment(id);
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Comment>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list comment",notes = "모든 comment 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listComment() throws Exception{

        List<Comment> res = commentService.selectAllComment();
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Comment>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete comment",notes = "comment 삭제")
    @DeleteMapping(value = "/del")
    public ResponseEntity<?> deleteComment(@RequestParam("id") int id) throws Exception{

        try {
            commentService.deleteComment(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "updateComment",notes = "comment 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateComment(@RequestBody Comment comment) throws Exception{

        try {
            commentService.updateComment(comment);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }
}
