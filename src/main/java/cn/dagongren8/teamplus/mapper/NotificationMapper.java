package cn.dagongren8.teamplus.mapper;

import cn.dagongren8.teamplus.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Mapper
public interface NotificationMapper {

    List<Notification> getAllNotifications(int userId);

    List<Notification> getAllSystemNotifications(int userId);

    List<Notification> getAllTeamNotifications(int userId);

    List<Notification> getAllSubjectNotifications(Integer userId);

    List<Notification> getSubjectNotifications(@Param("userId") int userId, @Param("subjectId") int subjectId);

    List<Notification> findNotificationsContains(@Param("userId") Integer userId, @Param("query") String query);

    List<Notification> getAllUnreadNotifications(Integer userId);

    Notification getNotificationById(int notificationId);

    int insertNotification(Notification notification);

    int updateNotification(Notification notification);

    int deleteNotificationById(int notificationId);

    int setAllAsRead(Integer userId);

    int getUnreadNotificationCount(Integer userId);

    int getUnreadSystemNotificationCount(Integer userId);

    int getUnreadTeamNotificationCount(Integer userId);

    int getUnreadSubjectNotificationCount(Integer userId);
}
