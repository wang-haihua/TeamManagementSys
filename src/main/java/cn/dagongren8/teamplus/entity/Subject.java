package cn.dagongren8.teamplus.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */

public class Subject implements Serializable {

    public static final Integer NOT_STARTED = 0;
    public static final Integer STARTED = 1;
    public static final Integer FINISHED = 2;
    public static final Integer ABANDONED = 3;

    private static final long serialVersionUID = 1L;

    private Integer subjectId;

    private Directory directory;

    private String subjectName;

    private String subjectIdentification;

    private Integer subjectStatus;

    private String subjectAvatar;

    private String subjectDescription;

    private Date subjectCreatetime;

    private List<SubjectAuthority> subjectAuthorities;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectIdentification() {
        return subjectIdentification;
    }

    public void setSubjectIdentification(String subjectIdentification) {
        this.subjectIdentification = subjectIdentification;
    }

    public Integer getSubjectStatus() {
        return subjectStatus;
    }

    public void setSubjectStatus(Integer subjectStatus) {
        this.subjectStatus = subjectStatus;
    }

    public String getSubjectAvatar() {
        return subjectAvatar;
    }

    public void setSubjectAvatar(String subjectAvatar) {
        this.subjectAvatar = subjectAvatar;
    }

    public String getSubjectDescription() {
        return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
        this.subjectDescription = subjectDescription;
    }

    public Date getSubjectCreatetime() {
        return subjectCreatetime;
    }

    public void setSubjectCreatetime(Date subjectCreatetime) {
        this.subjectCreatetime = subjectCreatetime;
    }

    public List<SubjectAuthority> getSubjectAuthorities() {
        return subjectAuthorities;
    }

    public void setSubjectAuthorities(List<SubjectAuthority> subjectAuthorities) {
        this.subjectAuthorities = subjectAuthorities;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subjectId=" + subjectId +
                ", directory=" + directory +
                ", subjectName='" + subjectName + '\'' +
                ", subjectIdentification='" + subjectIdentification + '\'' +
                ", subjectStatus=" + subjectStatus +
                ", subjectAvatar='" + subjectAvatar + '\'' +
                ", subjectDescription='" + subjectDescription + '\'' +
                ", subjectCreatetime=" + subjectCreatetime +
                ", subjectAuthorities=" + subjectAuthorities +
                '}';
    }
}
