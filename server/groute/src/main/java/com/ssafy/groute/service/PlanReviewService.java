package com.ssafy.groute.service;

import com.ssafy.groute.dto.PlanReview;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlanReviewService {
    void insertPlanReview(PlanReview planReview) throws Exception;
    PlanReview selectPlanReview(int id) throws Exception;
    List<PlanReview> selectAllPlanReview() throws Exception;
    void deletePlanReview(int id) throws Exception;
    void updatePlanReview(PlanReview planReview) throws Exception;
    List<PlanReview> selectByUserId(String userId) throws Exception;
    List<PlanReview> selectByPlanId(int planId) throws Exception;
}
