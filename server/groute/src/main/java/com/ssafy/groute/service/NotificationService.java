package com.ssafy.groute.service;

import com.ssafy.groute.dto.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    void insertNotification(Notification notification) throws Exception;
    Notification selectNotification(int id) throws Exception;
    List<Notification> selectNotificationByUserId(String userId) throws Exception;
    void deleteNotification(int id) throws Exception;
    void updateNotification(Notification notification) throws Exception;
}
