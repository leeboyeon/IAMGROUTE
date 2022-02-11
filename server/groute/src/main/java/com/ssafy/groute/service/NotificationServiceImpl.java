package com.ssafy.groute.service;

import com.ssafy.groute.dto.Notification;
import com.ssafy.groute.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    NotificationMapper notificationMapper;

    @Override
    public void insertNotification(Notification notification) throws Exception {
        notificationMapper.insertNotification(notification);
    }

    @Override
    public Notification selectNotification(int id) throws Exception {
        return notificationMapper.selectNotification(id);
    }

    @Override
    public List<Notification> selectNotificationByUserId(String userId) throws Exception {
        return notificationMapper.selectNotificationByUserId(userId);
    }

    @Override
    public void deleteNotification(int id) throws Exception {
        notificationMapper.deleteNotification(id);
    }

    @Override
    public void updateNotification(Notification notification) throws Exception {
        notificationMapper.updateNotification(notification);
    }
}
