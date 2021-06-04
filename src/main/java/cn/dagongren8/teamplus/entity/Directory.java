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

public class Directory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer directoryId;

    private String directoryName;

    private Integer directoryParentId;

    private String directoryFullpath;

    private User directoryCreator;

    private User directoryUpdater;

    private Date directoryCreatetime;

    private Date directoryUpdatetime;

    private String directoryRemark;

    private Boolean directoryDeleteFlag;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(Integer directoryId) {
        this.directoryId = directoryId;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public Integer getDirectoryParentId() {
        return directoryParentId;
    }

    public void setDirectoryParentId(Integer directoryParentId) {
        this.directoryParentId = directoryParentId;
    }

    public String getDirectoryFullpath() {
        return directoryFullpath;
    }

    public void setDirectoryFullpath(String directoryFullpath) {
        this.directoryFullpath = directoryFullpath;
    }

    public User getDirectoryCreator() {
        return directoryCreator;
    }

    public void setDirectoryCreator(User directoryCreator) {
        this.directoryCreator = directoryCreator;
    }

    public User getDirectoryUpdater() {
        return directoryUpdater;
    }

    public void setDirectoryUpdater(User directoryUpdater) {
        this.directoryUpdater = directoryUpdater;
    }

    public Date getDirectoryCreatetime() {
        return directoryCreatetime;
    }

    public void setDirectoryCreatetime(Date directoryCreatetime) {
        this.directoryCreatetime = directoryCreatetime;
    }

    public Date getDirectoryUpdatetime() {
        return directoryUpdatetime;
    }

    public void setDirectoryUpdatetime(Date directoryUpdatetime) {
        this.directoryUpdatetime = directoryUpdatetime;
    }

    public String getDirectoryRemark() {
        return directoryRemark;
    }

    public void setDirectoryRemark(String directoryRemark) {
        this.directoryRemark = directoryRemark;
    }

    public Boolean getDirectoryDeleteFlag() {
        return directoryDeleteFlag;
    }

    public void setDirectoryDeleteFlag(Boolean directoryDeleteFlag) {
        this.directoryDeleteFlag = directoryDeleteFlag;
    }

    @Override
    public String toString() {
        return "Directory{" +
                "directoryId=" + directoryId +
                ", directoryName='" + directoryName + '\'' +
                ", directoryParentId=" + directoryParentId +
                ", directoryFullpath='" + directoryFullpath + '\'' +
                ", directoryCreator=" + directoryCreator +
                ", directoryUpdater=" + directoryUpdater +
                ", directoryCreatetime=" + directoryCreatetime +
                ", directoryUpdatetime=" + directoryUpdatetime +
                ", directoryRemark='" + directoryRemark + '\'' +
                ", directoryDeleteFlag=" + directoryDeleteFlag +
                '}';
    }
}
