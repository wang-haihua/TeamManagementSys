package cn.dagongren8.teamplus.component;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于对web端进行登录检测的拦截器
 */
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

    PasswordManager.LoginProperties loginProperties;

    public LoginHandlerInterceptor(PasswordManager.LoginProperties loginProperties) {
        this.loginProperties = loginProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!loginProperties.isCheck()) {
            // 不使用登录验证
            return true;
        }

        Object loginUser = request.getSession().getAttribute("loginUser");
        Object loginTeam = request.getSession().getAttribute("loginTeam");
        if (loginUser == null || loginTeam == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
