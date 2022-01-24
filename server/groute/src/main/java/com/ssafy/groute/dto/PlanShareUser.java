package com.ssafy.groute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanShareUser {
    private int id;
    private String userId;
    private int planId;

    public PlanShareUser(String userId, int userPlanId) {
        this.userId = userId;
        this.planId = userPlanId;
    }
}
