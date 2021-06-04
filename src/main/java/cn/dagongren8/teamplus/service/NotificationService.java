package cn.dagongren8.teamplus.service;

import cn.dagongren8.teamplus.entity.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Service
public interface NotificationService {
    List<Notification> getAllNotifications(int userId);

    List<Notification> getAllSystemNotifications(int userId);

    List<Notification> getAllTeamNotifications(int userId);

    List<Notification> getAllSubjectNotifications(Integer userId);

    List<Notification> getAllSubjectNotifications(int userId, int subjectId);

    List<Notification> getAllUnreadNotifications(Integer userId);

    Notification getNotificationById(int notificationId);

    int insertNotification(Notification notification);

    int updateNotification(Notification notification);

    int deleteNotificationById(int notificationId);

    boolean serAllAsRead(Integer userId);

    int getUnreadNotificationCount(Integer userId);

    int getUnreadSystemNotificationCount(Integer userId);

    int getUnreadTeamNotificationCount(Integer userId);

    int getUnreadSubjectNotificationCount(Integer userId);

    List<Notification> findNotificationsContains(Integer userId, String query);
}
