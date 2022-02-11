package com.ssafy.groute.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessage {
    private boolean validate_only;
    private Message message;

//    @Builder
//    @AllArgsConstructor
//    @Getter
//    public static class Apns{
//        private Payload payload;
//
//    }
//    @Builder
//    @AllArgsConstructor
//    @Getter
//    public static class Payload{
//        private Aps aps;
//
//    }
//    @Builder
//    @AllArgsConstructor
//    @Getter
//    public static class Aps{
//        private String sound;
//    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private String token;
        private Notification notification;
        private Data data;
//        private Apns apns;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Data {
        private String path;
//        private String title;
//        private String body;
    }
}
