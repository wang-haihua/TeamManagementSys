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
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer teamId;

    private String teamName;

    private String teamIdentification;

    private String teamAvatar;

    private String teamDescription;

    private Date teamCreatetime;

    private String inviteToken;

    private boolean inviteTokenEnabled;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamIdentification() {
        return teamIdentification;
    }

    public void setTeamIdentification(String teamIdentification) {
        this.teamIdentification = teamIdentification;
    }

    public String getTeamAvatar() {
        return teamAvatar;
    }

    public void setTeamAvatar(String teamAvatar) {
        this.teamAvatar = teamAvatar;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public Date getTeamCreatetime() {
        return teamCreatetime;
    }

    public void setTeamCreatetime(Date teamCreatetime) {
        this.teamCreatetime = teamCreatetime;
    }

    public String getInviteToken() {
        return inviteToken;
    }

    public void setInviteToken(String inviteToken) {
        this.inviteToken = inviteToken;
    }

    public boolean isInviteTokenEnabled() {
        return inviteTokenEnabled;
    }

    public void setInviteTokenEnabled(boolean inviteTokenEnabled) {
        this.inviteTokenEnabled = inviteTokenEnabled;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", teamIdentification='" + teamIdentification + '\'' +
                ", teamAvatar='" + teamAvatar + '\'' +
                ", teamDescription='" + teamDescription + '\'' +
                ", teamCreatetime=" + teamCreatetime +
                ", inviteToken='" + inviteToken + '\'' +
                ", inviteTokenEnabled=" + inviteTokenEnabled +
                '}';
    }
}
