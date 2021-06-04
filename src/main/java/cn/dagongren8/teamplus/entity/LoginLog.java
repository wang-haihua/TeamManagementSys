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

public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer logId;

    private User loginUser;

    private Date logCreatetime;

    private String loginIp;

    private String loginOpsys;

    private String loginBrowser;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public User getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    public Date getLogCreatetime() {
        return logCreatetime;
    }

    public void setLogCreatetime(Date logCreatetime) {
        this.logCreatetime = logCreatetime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginOpsys() {
        return loginOpsys;
    }

    public void setLoginOpsys(String loginOpsys) {
        this.loginOpsys = loginOpsys;
    }

    public String getLoginBrowser() {
        return loginBrowser;
    }

    public void setLoginBrowser(String loginBrowser) {
        this.loginBrowser = loginBrowser;
    }

    @Override
    public String toString() {
        return "LoginLog{" +
                "logId=" + logId +
                ", loginUser=" + loginUser +
                ", logCreatetime=" + logCreatetime +
                ", loginIp='" + loginIp + '\'' +
                ", loginOpsys='" + loginOpsys + '\'' +
                ", loginBrowser='" + loginBrowser + '\'' +
                '}';
    }
}
