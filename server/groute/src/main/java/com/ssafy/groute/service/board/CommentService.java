package com.ssafy.groute.service.board;

import com.ssafy.groute.dto.board.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    void insertComment(Comment comment) throws Exception;
    Comment selectComment(int id) throws Exception;
    List<Comment> selectAllComment() throws Exception;
    void deleteComment(int id) throws Exception;
    void updateComment(Comment comment) throws Exception;
    List<Comment> selectAllByBoardDetailId(int id) throws Exception;
}
