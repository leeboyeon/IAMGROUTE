package com.ssafy.groute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Routes {
    private int id;
    private int routeId;
    private int planId;

    public Routes(int routeId, int planId) {
        this.routeId = routeId;
        this.planId = planId;
    }
}
