package com.ssafy.groute.service;

import com.ssafy.groute.dto.User;
import com.ssafy.groute.mapper.*;
import com.ssafy.groute.mapper.board.BoardDetailLikeMapper;
import com.ssafy.groute.mapper.board.BoardDetailMapper;
import com.ssafy.groute.mapper.board.CommentMapper;
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
    private final UserPlanMapper userPlanMapper;
    private final PlaceMapper placeMapper;
    private final RoutesMapper routesMapper;
    private final AccountMapper accountMapper;
    private final RouteDetailMapper routeDetailMapper;

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
        // user_id로 게시글 있으면 삭제
        boardDetailMapper.deleteAllBoardDetailByUid(userId);
        // user_id로 함께 여행 있으면 삭제
        planShareUserMapper.deleteAllPlanShareUserByUId(userId);
        // user_id로 plan_id 찾기
        List<Integer> planIds = userPlanMapper.findAllPlanIdsByUId(userId);
        for (int planId:planIds) {
            List<Integer> routesIds = routesMapper.findAllRoutesIdsByPId(planId);
            for (int routesId: routesIds) {
                // user_id로 찾은 plan_id로 찾은 routes_id로 가계부 있으면 삭제
                accountMapper.deleteAllAccountByRId(routesId);
            }
            // user_id로 찾은 plan_id로 routes 삭제
            routesMapper.deleteAllRoutesByPId(planId);
        }
        // user_id로 여행계획 삭제
        userPlanMapper.deleteAllUserPlanByUId(userId);
        // user_id로 찾은 place_id로 routedetail 삭제
        List<Integer> placeIds = placeMapper.findAllPlaceByUId(userId);
        System.out.println(placeIds);
        for (int placeId: placeIds) {
            routeDetailMapper.deleteAllRouteDetailByPId(placeId);
        }
        // user_id로 관광지 삭제
        placeMapper.deleteAllPlaceByUId(userId);
        // 유저 삭제
        userMapper.deleteUser(userId);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }
}
