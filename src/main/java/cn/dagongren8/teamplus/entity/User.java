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
public class User implements Serializable {

    public static final int MEMBER = 0; // 成员
    public static final int CREATOR = 1; // 创建者
    public static final int MANAGER = 2; // 管理员
    public static final int LOGOUT = 3; //已注销用户

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Team team;

    private Integer teamId;

    private String userName;

    private String userPassword;

    private String userAvatar;

    private String userEmail;

    private Integer teamAuthority;

    private Date userCreatetime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Integer getTeamId() {
        return team == null ? teamId : team.getTeamId();
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getTeamAuthority() {
        return teamAuthority;
    }

    public void setTeamAuthority(Integer teamAuthority) {
        this.teamAuthority = teamAuthority;
    }

    public Date getUserCreatetime() {
        return userCreatetime;
    }

    public void setUserCreatetime(Date userCreatetime) {
        this.userCreatetime = userCreatetime;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", team=" + team +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", teamAuthority=" + teamAuthority +
                ", userCreatetime=" + userCreatetime +
                '}';
    }
}
