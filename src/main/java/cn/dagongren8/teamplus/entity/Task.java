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

public class Task implements Serializable {

    public static final int LOW = 3; // 优先级低
    public static final int MEDIUM = 2; // 优先级中
    public static final int HIGH = 1; // 优先级高
    public static final int EMERGENCY = 0; // 紧急

    private static final long serialVersionUID = 1L;

    private Integer taskId;

    private Subject subject;

    private User processor;

    private User taskCreator;

    private String taskTitle;

    private String taskContent;

    private Integer taskRank;

    private Integer taskStatus;

    private Integer taskDuration;

    private Date taskCreatetime;

    private Date taskStarttime;

    private Date taskDeadline;

    private Date taskEndtime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getProcessor() {
        return processor;
    }

    public void setProcessor(User processor) {
        this.processor = processor;
    }

    public User getTaskCreator() {
        return taskCreator;
    }

    public void setTaskCreator(User taskCreator) {
        this.taskCreator = taskCreator;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public Integer getTaskRank() {
        return taskRank;
    }

    public void setTaskRank(Integer taskRank) {
        this.taskRank = taskRank;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getTaskDuration() {
        return taskDuration;
    }

    public void setTaskDuration(Integer taskDuration) {
        this.taskDuration = taskDuration;
    }

    public Date getTaskCreatetime() {
        return taskCreatetime;
    }

    public void setTaskCreatetime(Date taskCreatetime) {
        this.taskCreatetime = taskCreatetime;
    }

    public Date getTaskStarttime() {
        return taskStarttime;
    }

    public void setTaskStarttime(Date taskStarttime) {
        this.taskStarttime = taskStarttime;
    }

    public Date getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(Date taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public Date getTaskEndtime() {
        return taskEndtime;
    }

    public void setTaskEndtime(Date taskEndtime) {
        this.taskEndtime = taskEndtime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", subject=" + subject +
                ", processor=" + processor +
                ", taskCreator=" + taskCreator +
                ", taskTitle='" + taskTitle + '\'' +
                ", taskContent='" + taskContent + '\'' +
                ", taskRank=" + taskRank +
                ", taskStatus=" + taskStatus +
                ", taskDuration=" + taskDuration +
                ", taskCreatetime=" + taskCreatetime +
                ", taskStarttime=" + taskStarttime +
                ", taskDeadline=" + taskDeadline +
                ", taskEndtime=" + taskEndtime +
                '}';
    }
}
