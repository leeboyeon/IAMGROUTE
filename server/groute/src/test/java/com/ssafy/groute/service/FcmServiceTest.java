package com.ssafy.groute.service;

import com.ssafy.groute.dto.User;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FcmServiceTest extends AbstractServiceTest {

    @Test
    @Order(1)
    void fcmBroadcastTest() {

        try {
            fcmService.broadCastMessage("Event", "함께 여행 가는 친구를 초대하고, 일정을 계획해보세요. 여행 일정 짜기가 더 쉬워져요!", "MyFragment");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @Order(2)
    void sendMessageToTest() {
        User user = userService.findById("admin");
        try {
            fcmService.sendMessageTo(user.getToken(), "Event", "2월에 가봐야 할 제주 여행지ღ'ᴗ'ღ", "RouteFragment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
