package com.ssafy.groute.service;

import com.ssafy.groute.dto.User;
import com.ssafy.groute.mapper.*;
import com.ssafy.groute.mapper.board.BoardDetailLikeMapper;
import com.ssafy.groute.mapper.board.BoardDetailMapper;
import com.ssafy.groute.mapper.board.CommentMapper;
import com.ssafy.groute.service.board.BoardDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final BoardDetailLikeMapper boardDetailLikeMapper;
    private final CommentMapper commentMapper;
    private final BoardDetailMapper boardDetailMapper;
    private final PlanShareUserMapper planShareUserMapper;
    private final PlanReviewMapper planReviewMapper;
    private final UserPlanMapper userPlanMapper;
    private final PlaceMapper placeMapper;
    private final PlaceReviewMapper placeReviewMapper;

    private final BoardDetailService boardDetailService;
    private final PlaceService placeService;
    private final UserPlanService userPlanService;

    @Override
    public void registerUser(User user){
        userMapper.registerUser(user);
    }

    @Override
    public User findById(String userId){
        return userMapper.findById(userId);
    }

    @Override
    public User findByUidType(String userId, String type){
        return userMapper.findById(userId);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) throws Exception{
        // user_id로 좋아요 있으면 삭제
        boardDetailLikeMapper.deleteAllBoardDetailLikeByUId(userId);
        // user_id로 댓글 있으면 삭제
        commentMapper.deleteAllCommentByUId(userId);
        // user_id로 게시글 있으면 삭제(service가져오기)
        List<Integer> boardDetailIds = boardDetailMapper.findBoardDetailIdsByUid(userId);
        for (int boardDetailId: boardDetailIds) {
            boardDetailService.deleteBoardDetail(boardDetailId);
        }

        // user_id로 함께 여행 있으면 삭제
        planShareUserMapper.deleteAllPlanShareUserByUId(userId);
        // user_id로 planlike 삭제
        userPlanMapper.deleteUserPlanByUSerId(userId);
        // user_id로 planreview 삭제
        placeReviewMapper.deletePlaceReviewByUserId(userId);
        // user_id로 plan_id 찾기
        List<Integer> planIds = userPlanMapper.findAllPlanIdsByUId(userId);
        for (int planId:planIds) {
            userPlanService.deleteUserPlan(planId);
        }

        // user_id로 placelike 삭제
        placeMapper.deletePlaceLikeByUserId(userId);
        // user_id로 placereview 삭제
        placeReviewMapper.deletePlaceReviewByUserId(userId);
        // user_id로 찾은 place_id로 routedetail 삭제
        List<Integer> placeIds = placeMapper.findAllPlaceByUId(userId);
        for (int placeId: placeIds) {
            placeService.deletePlace(placeId);
        }

        // 유저 삭제
        userMapper.deleteUser(userId);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public List<User> selectByPlanId(int planId) throws Exception {
        return userMapper.selectByPlanId(planId);
    }
}
