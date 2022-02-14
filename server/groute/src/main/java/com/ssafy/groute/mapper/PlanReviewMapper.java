package com.ssafy.groute.mapper;

import com.ssafy.groute.dto.PlanReview;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanReviewMapper {
    void insertPlanReview(PlanReview planReview) throws Exception;
    PlanReview selectPlanReview(int id) throws Exception;
    List<PlanReview> selectAllPlanReview() throws Exception;
    void deletePlanReview(int id) throws Exception;
    void updatePlanReview(PlanReview planReview) throws Exception;
    List<PlanReview> selectByUserId(String userId) throws Exception;
    List<PlanReview> selectByPlanId(int planId) throws Exception;
    void deletePlanReviewByUserId(String userId) throws Exception;
    int selectAvgRateByPlanId(int planId) throws Exception;
    int selectCntRateByPlanId(int planId) throws Exception;
}
