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
public class SubjectAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer saId;

    private User user;

    private Subject subject;

    private Integer subjectAuthorityType;

    private Date subjectAuthorityCreatetime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getSaId() {
        return saId;
    }

    public void setSaId(Integer saId) {
        this.saId = saId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Integer getSubjectAuthorityType() {
        return subjectAuthorityType;
    }

    public void setSubjectAuthorityType(Integer subjectAuthorityType) {
        this.subjectAuthorityType = subjectAuthorityType;
    }

    public Date getSubjectAuthorityCreatetime() {
        return subjectAuthorityCreatetime;
    }

    public void setSubjectAuthorityCreatetime(Date subjectAuthorityCreatetime) {
        this.subjectAuthorityCreatetime = subjectAuthorityCreatetime;
    }

    @Override
    public String toString() {
        return "SubjectAuthority{" +
                "saId=" + saId +
                ", user=" + user +
                ", subject=" + subject +
                ", subjectAuthorityType=" + subjectAuthorityType +
                ", subjectAuthorityCreatetime=" + subjectAuthorityCreatetime +
                '}';
    }
}
