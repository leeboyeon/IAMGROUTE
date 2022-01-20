package com.ssafy.groute.controller.board;


import com.ssafy.groute.dto.board.Board;
import com.ssafy.groute.service.board.BoardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
@RequestMapping("/board")
public class BoardController {


    @Autowired
    BoardService boardService;

    @ApiOperation(value = "board 추가",notes = "board 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertBoard(@RequestBody Board req){

        try {
            boardService.insertBoard(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "board 검색",notes = "이름으로 board 하나 검색")
    @GetMapping(value = "/detail")
    public ResponseEntity<?> detailBoard(@RequestParam("id") int id) throws Exception{

        Board res = boardService.selectBoard(id);
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Board>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list board",notes = "모든 board 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listBoard() throws Exception{

        List<Board> res = boardService.selectAllBoard();
        if(res==null){
            return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Board>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete board",notes = "board 삭제")
    @DeleteMapping(value = "/del")
    public ResponseEntity<?> deleteBoard(@RequestParam("id") int id) throws Exception{

        try {
            boardService.deleteBoard(id);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }

    @ApiOperation(value = "updateBoard",notes = "board 수정")
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateBoard(@RequestBody Board board) throws Exception{

        try {
            boardService.updateBoard(board);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("FAIL", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<String>("SUCCESS",HttpStatus.OK);
    }
}
