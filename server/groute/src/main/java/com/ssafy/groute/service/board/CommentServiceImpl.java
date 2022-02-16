package com.ssafy.groute.service.board;

import com.ssafy.groute.dto.board.BoardDetail;
import com.ssafy.groute.dto.board.Comment;
import com.ssafy.groute.mapper.board.BoardDetailMapper;
import com.ssafy.groute.mapper.board.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    BoardDetailMapper boardDetailMapper;

    @Transactional
    @Override
    public void insertComment(Comment comment) throws Exception {
        BoardDetail boardDetail = boardDetailMapper.selectBoardDetail(comment.getBoardDetailId());
        int groupNum = commentMapper.selectAllByBoardDetailId(comment.getBoardDetailId()).size();
        if(comment.getLevel()==0) {
            comment.setGroupNum(groupNum + 1);
        }
        boardDetail.setCommentCnt(boardDetail.getCommentCnt()+1);
        commentMapper.insertComment(comment);
        boardDetailMapper.updateBoardDetailHitCntOrLikeOrCommentCnt(boardDetail);
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
        Comment comment = commentMapper.selectComment(id);
        int boardDetailId = comment.getBoardDetailId();
        BoardDetail boardDetail = boardDetailMapper.selectBoardDetail(boardDetailId);
        if(comment.getLevel()==1){
            commentMapper.deleteReComment(id);
            boardDetail.setCommentCnt(boardDetail.getCommentCnt()-1);
        }else {
            int cnt = commentMapper.deleteComment(comment);
            boardDetail.setCommentCnt(boardDetail.getCommentCnt()-cnt);
        }
        boardDetailMapper.updateBoardDetailHitCntOrLikeOrCommentCnt(boardDetail);
    }

    @Override
    public void updateComment(Comment comment) throws Exception {
        commentMapper.updateComment(comment);
    }

    @Override
    public List<Comment> selectAllByBoardDetailId(int id) throws Exception {
        return commentMapper.selectAllByBoardDetailId(id);
    }
}
