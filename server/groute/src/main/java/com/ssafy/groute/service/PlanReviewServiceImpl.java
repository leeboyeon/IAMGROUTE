package com.ssafy.groute.service;

import com.ssafy.groute.dto.UserPlan;
import com.ssafy.groute.dto.PlanReview;
import com.ssafy.groute.mapper.UserPlanMapper;
import com.ssafy.groute.mapper.PlanReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlanReviewServiceImpl implements PlanReviewService {
    @Autowired
    PlanReviewMapper planReviewMapper;
    @Autowired
    UserPlanMapper userPlanMapper;
    @Override
    public void insertPlanReview(PlanReview planReview) throws Exception {
        planReviewMapper.insertPlanReview(planReview);
        UserPlan userPlan = userPlanMapper.selectUserPlan(planReview.getPlanId());
        double rate = planReviewMapper.selectAvgRateByPlanId(planReview.getPlanId());
        userPlan.setRate(rate);
        userPlanMapper.updateUserPlan(userPlan);
    }

    @Override
    public PlanReview selectPlanReview(int id) throws Exception {
        return planReviewMapper.selectPlanReview(id);
    }

    @Override
    public List<PlanReview> selectAllPlanReview() throws Exception {
        return planReviewMapper.selectAllPlanReview();
    }

    @Override
    public void deletePlanReview(int id) throws Exception {
        PlanReview planReview = selectPlanReview(id);
        UserPlan userPlan = userPlanMapper.selectUserPlan(planReview.getPlanId());
        planReviewMapper.deletePlanReview(id);
        double rate = planReviewMapper.selectAvgRateByPlanId(planReview.getPlanId());
        userPlan.setRate(rate);
        userPlanMapper.updateUserPlan(userPlan);
    }

    @Transactional
    @Override
    public void updatePlanReview(PlanReview planReview) throws Exception {
        planReviewMapper.updatePlanReview(planReview);
        UserPlan userPlan = userPlanMapper.selectUserPlan(planReview.getPlanId());
        double rate = planReviewMapper.selectAvgRateByPlanId(planReview.getPlanId());
        userPlan.setRate(rate);
        userPlanMapper.updateUserPlan(userPlan);
    }

    @Override
    public List<PlanReview> selectByUserId(String userId) throws Exception {
        return planReviewMapper.selectByUserId(userId);
    }

    @Override
    public List<PlanReview> selectByPlanId(int planId) throws Exception {
        return planReviewMapper.selectByPlanId(planId);
    }

}
