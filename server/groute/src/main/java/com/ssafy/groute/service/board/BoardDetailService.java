package com.ssafy.groute.service.board;

import com.ssafy.groute.dto.board.BoardDetail;
import com.ssafy.groute.dto.board.BoardDetailLike;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardDetailService {
    void insertBoardDetail(BoardDetail boardDetail) throws Exception;
    BoardDetail selectBoardDetail(int id) throws Exception;
    List<BoardDetail> selectAllBoardDetail() throws Exception;
    void deleteBoardDetail(int id) throws Exception;
    void updateBoardDetail(BoardDetail boardDetail) throws Exception;
}
