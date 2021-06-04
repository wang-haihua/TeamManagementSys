package cn.dagongren8.teamplus.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */

public class Notification implements Serializable {

    public static final Integer SYSTEM_NOTIFICATION = 0;    // 系统通知
    public static final Integer TEAM_NOTIFICATION = 1;      // 课题组通知
    public static final Integer SUBJECT_NOTIFICATION = 2;   // 课题通知

    private static final long serialVersionUID = 1L;

    private Integer notificationId;

    private User user;

    private String notificationContent;

    private String notificationTitle;

    private User notificationAnnouncer;

    private Date notificationCreatetime;

    private Integer notificationType;

    private boolean notificationRead;

    private Subject subject;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public User getNotificationAnnouncer() {
        return notificationAnnouncer;
    }

    public void setNotificationAnnouncer(User notificationAnnouncer) {
        this.notificationAnnouncer = notificationAnnouncer;
    }

    public Date getNotificationCreatetime() {
        return notificationCreatetime;
    }

    public void setNotificationCreatetime(Date notificationCreatetime) {
        this.notificationCreatetime = notificationCreatetime;
    }

    public Integer getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(Integer notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isNotificationRead() {
        return notificationRead;
    }

    public void setNotificationRead(boolean notificationRead) {
        this.notificationRead = notificationRead;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", user=" + user +
                ", notificationContent='" + notificationContent + '\'' +
                ", notificationTitle='" + notificationTitle + '\'' +
                ", notificationAnnouncer=" + notificationAnnouncer +
                ", notificationCreatetime=" + notificationCreatetime +
                '}';
    }
}
