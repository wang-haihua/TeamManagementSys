package cn.dagongren8.teamplus.entity;

import java.util.Date;

public class Announcement {

    private Integer announcementId;

    private Subject subject;

    private User user;

    private String announcementContent;

    private Date announcementCreatetime;

    public Integer getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Integer announcementId) {
        this.announcementId = announcementId;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAnnouncementContent() {
        return announcementContent;
    }

    public void setAnnouncementContent(String announcementContent) {
        this.announcementContent = announcementContent;
    }

    public Date getAnnouncementCreatetime() {
        return announcementCreatetime;
    }

    public void setAnnouncementCreatetime(Date announcementCreatetime) {
        this.announcementCreatetime = announcementCreatetime;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "announcementId=" + announcementId +
                ", subject=" + subject +
                ", user=" + user +
                ", announcementContent='" + announcementContent + '\'' +
                ", announcementCreatetime=" + announcementCreatetime +
                '}';
    }
}
