package com.ssafy.groute.controller.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.groute.controller.UserController;
import com.ssafy.groute.dto.PlaceReview;
import com.ssafy.groute.dto.board.BoardDetail;
import com.ssafy.groute.dto.board.BoardDetailLike;
import com.ssafy.groute.dto.board.Comment;
import com.ssafy.groute.service.StorageService;
import com.ssafy.groute.service.UserService;
import com.ssafy.groute.service.board.BoardDetailLikeService;
import com.ssafy.groute.service.board.BoardDetailService;
import com.ssafy.groute.service.board.CommentService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = { "*" }, methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE }, maxAge = 6000)
@RequestMapping("/boardDetail")
public class BoardDetailController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    BoardDetailService boardDetailService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    BoardDetailLikeService boardDetailLikeService;

    @Autowired
    StorageService storageService;

    @Autowired
    private ObjectMapper mapper;

//    @Value("${spring.servlet.multipart.location}")
    @Value("${spring.http.multipart.location}")
    private String uploadPath;

//    public BoardDetailController() {
//    }

    @ApiOperation(value = "boardDetail 추가",notes = "boardDetail 추가")
    @PostMapping(value = "/insert")
    public ResponseEntity<?> insertBoardDetail(@RequestPart(value = "board") String board, @RequestPart(value = "img", required = false) MultipartFile img) throws Exception {
        try {
//            logger.debug("boardDetail : {}", board);
            BoardDetail req = mapper.readValue(board, BoardDetail.class);
//            logger.debug("boardDetail : {}", req.getContent());

            if (img != null) {
                String filePath = "";
                if (req.getBoardId() == 1) {
                    filePath = "board/freeBoard";
                } else if(req.getBoardId() == 2) {
                    filePath = "board/qnaBoard";
                }
                String fileName = storageService.store(img, uploadPath + filePath);
                req.setImg(filePath + "/" + fileName);
            } else {
                req.setImg(null);
            }
            boardDetailService.insertBoardDetail(req);
            return new ResponseEntity<Integer>(req.getBoardId(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Integer>(0, HttpStatus.NOT_ACCEPTABLE);
        }

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
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Map<String,Object>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list boardDetail",notes = "모든 boardDetail 반환")
    @GetMapping(value = "/list")
    public ResponseEntity<?> listBoardDetail() throws Exception{

        List<BoardDetail> res = boardDetailService.selectAllBoardDetail();
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<BoardDetail>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "list boardDetail division",notes = "boardDetail를 구분하여 반환")
    @GetMapping(value = "/list/division")
    public ResponseEntity<?> listBoardDetailDivision(@RequestParam("boardId") int boardId) throws Exception{
        List<BoardDetail> res = boardDetailService.selectBoardDetailSeparetedByTag(boardId);
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
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
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "updateBoardDetail",notes = "boardDetail 수정")
    @PutMapping(value = "/update")
//    public ResponseEntity<?> updateBoardDetail(@RequestBody BoardDetail boardDetail) throws Exception{
    public ResponseEntity<?> updateBoardDetail(@RequestPart(value = "board") String board, @RequestPart(value = "img", required = false) MultipartFile img) throws Exception {

        try {
//            logger.debug("updateBoardDetail : {}", board);
            BoardDetail boardDetail = mapper.readValue(board, BoardDetail.class);
            String beforeImg = boardDetailService.selectBoardDetail(boardDetail.getId()).getImg();
//            logger.debug("updateBoardDetail : {}", boardDetail.getImg());

            if (img != null) {
                String filePath = "";
                if (boardDetail.getBoardId() == 1) {
                    filePath = "board/freeBoard";
                } else if(boardDetail.getBoardId() == 2) {
                    filePath = "board/qnaBoard";
                }
                String fileName = storageService.store(img, uploadPath + filePath);
                boardDetail.setImg(filePath + "/" + fileName);
            } else {    // img == null
                if(beforeImg.equals("") || beforeImg.equals("null")){
                    boardDetail.setImg(null);
                } else {
                    boardDetail.setImg(beforeImg);
                }
            }

            boardDetailService.updateBoardDetail(boardDetail);
            return new ResponseEntity<Integer>(boardDetail.getBoardId(), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Integer>(0, HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @ApiOperation(value = "boardDetail like 확인",notes = "boardDetail like 확인")
    @PostMapping(value = "/isLike")
    public boolean boardDetailIsLike(@RequestParam("userId") String userId, @RequestParam("boardDetailId") int boardDetailId) throws Exception{
        BoardDetailLike boardDetailLike = boardDetailLikeService.findBoardLikeByUIdBDId(userId, boardDetailId);
        if (boardDetailLike != null) {
            return true;
        }
        return false;
    }

        @ApiOperation(value = "boardDetail like",notes = "boardDetail like")
    @PostMapping(value = "/like")
    public ResponseEntity<?> boardDetailLike(@RequestParam("userId") String userId, @RequestParam("boardDetailId") int boardDetailId) throws Exception{
        try {
            BoardDetailLike boardDetailLike = boardDetailLikeService.findBoardLikeByUIdBDId(userId, boardDetailId);
            if (boardDetailLike != null) {
                // 좋아요가 존재하면 삭제
                boardDetailLikeService.deleteBoardDetailLike(boardDetailLike.getId());
            } else {
                // 좋아요가 존재하지 않으면 추가
                boardDetailLikeService.insertBoardDetailLike(userId, boardDetailId);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "comment 추가",notes = "comment 추가")
    @PostMapping(value = "/comment/insert")
    public ResponseEntity<?> insertComment(@RequestBody Comment req){

        try {
            commentService.insertComment(req);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @ApiOperation(value = "comment 검색",notes = "이름으로 comment 하나 검색")
    @GetMapping(value = "/comment/detail")
    public ResponseEntity<?> detailComment(@RequestParam("id") int id) throws Exception {

        Comment res = commentService.selectComment(id);
        if (res == null) {
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Comment>(res, HttpStatus.OK);

    }



    @ApiOperation(value = "list comment",notes = "모든 comment 반환")
    @GetMapping(value = "/comment/list")
    public ResponseEntity<?> listComment() throws Exception{

        List<Comment> res = commentService.selectAllComment();
        if(res==null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Comment>>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "delete comment",notes = "comment 삭제")
    @DeleteMapping(value = "/comment/del")
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
    @PutMapping(value = "/comment/update")
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
