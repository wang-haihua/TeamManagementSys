package cn.dagongren8.teamplus.component;

import cn.dagongren8.teamplus.entity.*;
import cn.dagongren8.teamplus.service.NotificationService;
import cn.dagongren8.teamplus.service.SubjectAuthorityService;
import cn.dagongren8.teamplus.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationCenter {

    private final NotificationService notificationService;
    private final UserService userService;
    private final SubjectAuthorityService subjectAuthorityService;

    public NotificationCenter(NotificationService notificationService, UserService userService, SubjectAuthorityService subjectAuthorityService) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.subjectAuthorityService = subjectAuthorityService;
    }

    /**
     * 发布一条通知
     *
     * @param announcerId      发布者ID
     * @param userId           用户ID
     * @param notificationType 通知类型（0：系统通知，1：课题组通知，2：课题通知）
     * @param subjectId        课题ID（当通知类型为'课题通知'时有效）
     * @param content          通知内容
     * @return 是否添加成功
     */
    public boolean publish(Integer announcerId, Integer userId, Integer notificationType, Integer subjectId, String content) {
        Notification notification = new Notification();
        User announcer = new User();
        announcer.setUserId(announcerId);
        User user = new User();
        user.setUserId(userId);
        notification.setNotificationAnnouncer(announcer);
        notification.setUser(user);
        notification.setNotificationType(notificationType);
        Subject subject = new Subject();
        subject.setSubjectId(subjectId);
        notification.setSubject(subject);
        notification.setNotificationContent(content);
        notification.setNotificationRead(false);
        return notificationService.insertNotification(notification) != 0;
    }

    /**
     * 发布系统通知
     *
     * @param userId  用户ID
     * @param content 通知内容
     * @return 是否添成功
     */
    public boolean publishSystemNotification(Integer userId, String content) {
        return publish(0, userId, Notification.SYSTEM_NOTIFICATION, null, content);
    }

    /**
     * 发布课题组通知
     *
     * @param announcerId 发布者ID
     * @param content     通知内容
     * @return 是否添加成功
     */
    public boolean publishTeamNotification(Integer announcerId, String content) {
        User announcer = userService.getUserById(announcerId);
        Team team = announcer.getTeam();
        List<User> teamUsers = userService.getAllTeamUsers(team.getTeamId());
        for (User u : teamUsers) {
            if (!publish(announcerId, u.getUserId(), Notification.TEAM_NOTIFICATION, null, content))
                return false;
        }
        return true;
    }

    /**
     * 发布课题通知
     *
     * @param announcerId 发布者ID
     * @param subjectId   课题ID
     * @param content     通知内容
     * @return 是否添加成功
     */
    public boolean publishSubjectNotification(Integer announcerId, Integer subjectId, String content) {
        List<SubjectAuthority> allSubjectUsers = subjectAuthorityService.getAllSubjectUsers(subjectId);
        for (SubjectAuthority sa : allSubjectUsers) {
            if (!publish(announcerId, sa.getUser().getUserId(), Notification.SUBJECT_NOTIFICATION, subjectId, content))
                return false;
        }
        return true;
    }

    /**
     * 通过ID获取通知
     *
     * @param notificationId 通知ID
     * @return 通知
     */
    public Notification getNotificationById(Integer notificationId) {
        return notificationService.getNotificationById(notificationId);
    }

    /**
     * 获取用户的所有通知
     *
     * @param userId 用户ID
     * @return 通知列表
     */
    public List<Notification> getAllNotifications(Integer userId) {
        return notificationService.getAllNotifications(userId);
    }

    /**
     * 获取用户的所有系统通知
     *
     * @param userId 用户ID
     * @return 通知列表
     */
    public List<Notification> getAllSystemNotifications(Integer userId) {
        return notificationService.getAllSystemNotifications(userId);
    }

    /**
     * 获取用户的所有课题组通知
     *
     * @param userId 用户ID
     * @return 通知列表
     */
    public List<Notification> getAllTeamNotifications(Integer userId) {
        return notificationService.getAllTeamNotifications(userId);
    }

    /**
     * 获取用户所有课题的通知
     *
     * @param userId 用户ID
     * @return 通知列表
     */
    public List<Notification> getAllSubjectNotifications(Integer userId) {
        return notificationService.getAllSubjectNotifications(userId);
    }

    /**
     * 获取用户指定课题的所有通知
     *
     * @param userId    用户ID
     * @param subjectId 课题ID
     * @return 通知列表
     */
    public List<Notification> getAllSubjectNotifications(Integer userId, Integer subjectId) {
        return notificationService.getAllSubjectNotifications(userId, subjectId);
    }

    /**
     * 获取所有未读通知
     *
     * @param userId 用户ID
     * @return 未读通知列表
     */
    public List<Notification> getAllUnreadNotifications(Integer userId) {
        return notificationService.getAllUnreadNotifications(userId);
    }

    /**
     * 将通知设置为已读
     *
     * @param notificationIds 通知ID的列表
     * @return 是否设置成功
     */
    public boolean setAsRead(List<Integer> notificationIds) {
        for (Integer nid : notificationIds) {
            Notification notification = notificationService.getNotificationById(nid);
            notification.setNotificationRead(true);
            if (notificationService.updateNotification(notification) == 0) return false;
        }
        return true;
    }

    /**
     * 将用户的所有通知设置为已读
     *
     * @param userId 用户ID
     * @return 是否设置成功
     */
    public boolean setAllAsRead(Integer userId) {
        return notificationService.serAllAsRead(userId);
    }

    /**
     * 获取用户的未读消息数量
     *
     * @param userId 用户ID
     * @return 一个长度为4的数组，分别表示：全部未读数量、系统通知未读数量、课题组通知未读数量、课题通知未读数量
     */
    public int[] getUnreadCounts(Integer userId) {

        int all = notificationService.getUnreadNotificationCount(userId);
        int sys = notificationService.getUnreadSystemNotificationCount(userId);
        int team = notificationService.getUnreadTeamNotificationCount(userId);
        int subject = notificationService.getUnreadSubjectNotificationCount(userId);

        return new int[]{all, sys, team, subject};
    }

    /**
     * 删除指定的通知
     *
     * @param notificationIds 通知ID
     * @return 是否删除成功
     */
    public boolean deleteNotifications(List<Integer> notificationIds) {
        for (Integer nid : notificationIds)
            if (notificationService.deleteNotificationById(nid) == 0) return false;
        return true;
    }

    public List<Notification> findNotificationsContains(Integer userId, String query) {
        return notificationService.findNotificationsContains(userId, query);
    }
}
