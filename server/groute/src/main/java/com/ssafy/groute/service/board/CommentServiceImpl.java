package com.ssafy.groute.service.board;

import com.ssafy.groute.dto.board.Comment;
import com.ssafy.groute.mapper.board.BoardDetailMapper;
import com.ssafy.groute.mapper.board.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    BoardDetailMapper boardDetailMapper;

    @Override
    public void insertComment(Comment comment) throws Exception {
        commentMapper.insertComment(comment);
    }

    @Override
    public Comment selectComment(int id) throws Exception {
        return commentMapper.selectComment(id);
    }

    @Override
    public List<Comment> selectAllComment() throws Exception {
        return commentMapper.selectAllComment();
    }

    @Override
    public void deleteComment(int id) throws Exception {
        commentMapper.deleteComment(id);
    }

    @Override
    public void updateComment(Comment comment) throws Exception {
        commentMapper.updateComment(comment);
    }
}
