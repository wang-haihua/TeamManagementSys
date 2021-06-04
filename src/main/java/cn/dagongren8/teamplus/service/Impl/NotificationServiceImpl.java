package cn.dagongren8.teamplus.service.Impl;

import cn.dagongren8.teamplus.entity.Notification;
import cn.dagongren8.teamplus.mapper.NotificationMapper;
import cn.dagongren8.teamplus.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    public List<Notification> getAllNotifications(int userId) {
        return notificationMapper.getAllNotifications(userId);
    }

    @Override
    public List<Notification> getAllSystemNotifications(int userId) {
        return notificationMapper.getAllSystemNotifications(userId);
    }

    @Override
    public List<Notification> getAllTeamNotifications(int userId) {
        return notificationMapper.getAllTeamNotifications(userId);
    }

    @Override
    public List<Notification> getAllSubjectNotifications(Integer userId) {
        return notificationMapper.getAllSubjectNotifications(userId);
    }

    @Override
    public List<Notification> getAllSubjectNotifications(int userId, int subjectId) {
        return notificationMapper.getSubjectNotifications(userId, subjectId);
    }

    @Override
    public List<Notification> getAllUnreadNotifications(Integer userId) {
        return notificationMapper.getAllUnreadNotifications(userId);
    }

    @Override
    public Notification getNotificationById(int notificationId) {
        return notificationMapper.getNotificationById(notificationId);
    }

    @Override
    public int insertNotification(Notification notification) {
        return notificationMapper.insertNotification(notification);
    }

    @Override
    public int updateNotification(Notification notification) {
        return notificationMapper.updateNotification(notification);
    }

    @Override
    public int deleteNotificationById(int notificationId) {
        return notificationMapper.deleteNotificationById(notificationId);
    }

    @Override
    public boolean serAllAsRead(Integer userId) {
        return notificationMapper.setAllAsRead(userId) != 0;
    }

    @Override
    public int getUnreadNotificationCount(Integer userId) {
        return notificationMapper.getUnreadNotificationCount(userId);
    }

    @Override
    public int getUnreadSystemNotificationCount(Integer userId) {
        return notificationMapper.getUnreadSystemNotificationCount(userId);
    }

    @Override
    public int getUnreadTeamNotificationCount(Integer userId) {
        return notificationMapper.getUnreadTeamNotificationCount(userId);
    }

    @Override
    public int getUnreadSubjectNotificationCount(Integer userId) {
        return notificationMapper.getUnreadSubjectNotificationCount(userId);
    }

    @Override
    public List<Notification> findNotificationsContains(Integer userId, String query) {
        return notificationMapper.findNotificationsContains(userId, query);
    }
}
