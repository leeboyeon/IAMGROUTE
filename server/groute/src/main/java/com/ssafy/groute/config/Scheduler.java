package com.ssafy.groute.config;

import com.ssafy.groute.dto.User;
import com.ssafy.groute.service.FirebaseCloudMessageService;
import com.ssafy.groute.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class Scheduler {

    @Autowired
    UserService userService;

    @Autowired
    FirebaseCloudMessageService fcmService;

    /**
     * 매일 14시 정각에 전 날 여행 일정이 종료된 사용자에게 여행 공유 & 리뷰 작성 요청
     */
    @Scheduled(cron = "0 0 14 * * *")
//    @Scheduled(cron = "0 */1 * * * *")    // 1분 마다
    public void endedPlanYesterdayScheduler() throws Exception {
        List<User> userList = userService.selectEndedPlanYesterday();

        for (User tmp : userList) {
            if (tmp != null) {
                fcmService.sendMessageTo(tmp.getToken(), "User", "아이엠그루트 속에서 여행은 즐거우셨나요?●'◡'●\n" + tmp.getNickname() + "님의 여행을 공유해보세요!", "UserPlanFragment");
            }
        }

    }
}
