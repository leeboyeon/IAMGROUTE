package com.ssafy.groute.service;

import com.ssafy.groute.dto.User;
import com.ssafy.groute.service.board.BoardDetailLikeService;
import com.ssafy.groute.service.board.BoardDetailService;
import com.ssafy.groute.service.board.BoardService;
import com.ssafy.groute.service.board.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractServiceTest {
    public static User user = new User("id", "pw", "nickname", "phone", "F", "1999-01-01", "email@email.com", "none", null, null, null, null);

    @Autowired
    AccountService accountService;

    @Autowired
    AreaService areaService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    PlaceReviewService placeReviewService;

    @Autowired
    PlaceService placeService;

    @Autowired
    PlanReviewService planReviewService;

    @Autowired
    PlanShareUserService planShareUserService;

    @Autowired
    RouteDetailService routeDetailService;

    @Autowired
    RouteService routeService;

    @Autowired
    StorageService storageService;

    @Autowired
    ThemeService themeService;

    @Autowired
    UserPlanService userPlanService;

    @Autowired
    UserService userService;

    @Autowired
    BoardDetailLikeService boardDetailLikeService;

    @Autowired
    BoardDetailService boardDetailService;

    @Autowired
    BoardService boardService;

    @Autowired
    CommentService commentService;

    @Autowired
    FirebaseCloudMessageService fcmService;
}
