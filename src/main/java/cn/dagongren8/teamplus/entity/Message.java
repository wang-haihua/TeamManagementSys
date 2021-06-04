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
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer messageId;

    private Subject subject;

    private User user;

    private String messageContent;

    private Date messageCreatetime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
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

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Date getMessageCreatetime() {
        return messageCreatetime;
    }

    public void setMessageCreatetime(Date messageCreatetime) {
        this.messageCreatetime = messageCreatetime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", subject=" + subject +
                ", user=" + user +
                ", messageContent='" + messageContent + '\'' +
                ", messageCreatetime=" + messageCreatetime +
                '}';
    }
}
