package cn.dagongren8.teamplus.component;

import cn.dagongren8.teamplus.entity.User;
import cn.dagongren8.teamplus.util.Commons;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class PasswordManager {

    private final LoginProperties loginProperties;

    public PasswordManager(LoginProperties loginProperties) {
        this.loginProperties = loginProperties;
    }

    /**
     * 验证密码
     *
     * @param user        用户对象
     * @param rawPassword 用户输入的密码
     * @return 是否验证成功
     */
    public boolean checkPassword(User user, String rawPassword) {
        if (user == null) return false;
        return Commons.stringEquals(user.getUserPassword(),
                Commons.getMD5(rawPassword, loginProperties.getSalt()));
    }

    /**
     * 将用户输入的密码转换为数据库中存储的编码
     *
     * @param rawPassword 用户输入的密码
     * @return 数据库中存储的密码
     */
    public String generateMD5Password(String rawPassword) {
        return Commons.getMD5(rawPassword, loginProperties.getSalt());
    }

    @Component
    @ConfigurationProperties(prefix = "teamplus.login")
    public static class LoginProperties {
        private String salt;
        private boolean check = true;

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }
    }
}
