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
     * utc time 05:00 == seoul 14:00
     * 매일 14시 정각에 전 날 여행 일정이 종료된 사용자에게 여행 공유 & 리뷰 작성 요청
     */
    @Scheduled(cron = "0 0 5 * * *")
    public void endedPlanYesterdayScheduler() throws Exception {
        List<User> userList = userService.selectEndedPlanYesterday();

        for (User tmp : userList) {
            if (tmp != null) {
                fcmService.sendMessageTo(tmp.getToken(), "User", "아이엠그루트 속에서 여행은 즐거우셨나요?●'◡'●\n" + tmp.getNickname() + "님의 소중한 추억을 공유해보세요!", "UserPlanFragment");
            }
        }

    }

    /**
     * utc time 9:00 == seoul 18:00
     * 매일 18시 정각에 여행 일정이 1일, 3일, 7일 남은 사용자에게 알림 전송
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void dayBeforeTravel() throws Exception {

        List<User> oneDayBeforeUser = userService.selectOneDayBeforeTravel();
        List<User> threeDayBeforeUser = userService.selectThreeDayBeforeTravel();
        List<User> weekBeforeUser = userService.selectWeekBeforeTravel();

        for (User tmp : oneDayBeforeUser) {
            if (tmp != null) {
                fcmService.sendMessageTo(tmp.getToken(), "User", "여행 1일 전입니다! 준비물은 다 챙기셨나요?\n" + tmp.getNickname() + " 님, 안전하고 즐거운 여행 다녀오세요( ღ'ᴗ'ღ )", "UserPlanFragment");
            }
        }

        for (User tmp : threeDayBeforeUser) {
            if (tmp != null) {
                fcmService.sendMessageTo(tmp.getToken(), "User", "여행 3일 전입니다!\n" + tmp.getNickname() + " 님, 여행 일정에서 빠트리신 장소는 없는지 확인해 보세요!", "UserPlanFragment");
            }
        }

        for (User tmp : weekBeforeUser) {
            if (tmp != null) {
                fcmService.sendMessageTo(tmp.getToken(), "User", tmp.getNickname() + " 님! 혹시 잊으신건 없으신가요?\n저와 여행떠나기 일주일 전이에요!", "UserPlanFragment");
            }
        }
    }

}
