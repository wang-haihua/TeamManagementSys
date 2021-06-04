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

public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer attachmentId;

    private Task task;

    private String attachmentName;

    private String attachmentFilepath;

    private String attachmentType;

    private Date attachmentCreatetime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentFilepath() {
        return attachmentFilepath;
    }

    public void setAttachmentFilepath(String attachmentFilepath) {
        this.attachmentFilepath = attachmentFilepath;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Date getAttachmentCreatetime() {
        return attachmentCreatetime;
    }

    public void setAttachmentCreatetime(Date attachmentCreatetime) {
        this.attachmentCreatetime = attachmentCreatetime;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "attachmentId=" + attachmentId +
                ", task=" + task +
                ", attachmentName='" + attachmentName + '\'' +
                ", attachmentFilepath='" + attachmentFilepath + '\'' +
                ", attachmentType='" + attachmentType + '\'' +
                ", attachmentCreatetime=" + attachmentCreatetime +
                '}';
    }
}
