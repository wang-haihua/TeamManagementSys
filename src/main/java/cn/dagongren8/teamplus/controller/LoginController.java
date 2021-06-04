package cn.dagongren8.teamplus.controller;

import cn.dagongren8.teamplus.component.NotificationCenter;
import cn.dagongren8.teamplus.component.PasswordManager;
import cn.dagongren8.teamplus.entity.LoginLog;
import cn.dagongren8.teamplus.entity.Team;
import cn.dagongren8.teamplus.entity.User;
import cn.dagongren8.teamplus.service.LoginLogService;
import cn.dagongren8.teamplus.service.TeamService;
import cn.dagongren8.teamplus.service.UserService;
import cn.dagongren8.teamplus.util.Commons;
import cn.dagongren8.teamplus.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 处理登录相关请求
 */
@Controller
public class LoginController {

    private final PasswordManager passwordManager;
    private final UserService userService;
    private final TeamService teamService;
    private final NotificationCenter notificationCenter;
    private final LoginLogService loginLogService;
    Logger logger = LoggerFactory.getLogger(getClass());

    public LoginController(PasswordManager passwordManager, UserService userService, TeamService teamService, NotificationCenter notificationCenter, LoginLogService loginLogService) {
        this.passwordManager = passwordManager;
        this.userService = userService;
        this.teamService = teamService;
        this.notificationCenter = notificationCenter;
        this.loginLogService = loginLogService;
    }

    /**
     * 课题组选择页面
     */
    @GetMapping("/login")
    public String loginTeamPage() {
        return "/login/login_team";
    }

    /**
     * 课题组成员登录页面
     */
    @GetMapping("/login/{team_identifier}")
    public String loginUserPage(Model model, @PathVariable("team_identifier") String teamIdentifier) {

        Team team = teamService.getTeamByTeamIdentifier(teamIdentifier);

        if (team == null) {
            // 课题组不存在
            model.addAttribute("error_info", "课题组不存在，请创建课题组。");
            return "/login/login_team";
        }

        model.addAttribute("team", team);
        return "/login/login_user";
    }

    /**
     * 处理登录请求
     */
    @PostMapping("/login/{team_identifier}")
    public String handleLogin(@PathVariable("team_identifier") String teamIdentifier,
                              @RequestParam(value = "email", required = false) String email,
                              @RequestParam(value = "password", required = false) String password,
                              Model model,
                              HttpSession session,
                              HttpServletRequest request) {

        logger.info("登录请求：[teamIdentifier：" + teamIdentifier + ", email：" + email + ", password: " + password + "]");

        Team team = teamService.getTeamByTeamIdentifier(teamIdentifier);

        if (team == null) {
            // 课题组不存在
            logger.info("用户登录失败：课题组不存在");
            model.addAttribute("error_info", "课题组不存在。");
            return "/login/login_team";
        }

        User user = userService.getUserByTeamIdentifierAndEmail(teamIdentifier, email);

        model.addAttribute("team", team);
        model.addAttribute("input_email", email);
        model.addAttribute("input_password", password);

        if (user == null) {
            // 用户不存在
            logger.info("用户登录失败：用户不存在");
            model.addAttribute("error_info", "用户不存在!");
            return "/login/login_user";
        }

        if (!passwordManager.checkPassword(user, password)) {
            // 密码错误
            logger.info("用户登录失败：密码不正确");
            notificationCenter.publishSystemNotification(user.getUserId(), "您于" + Commons.formatDate("yyyy年MM月dd日 HH:mm:ss", new Date()) + "尝试登录失败，原因：密码错误");
            model.addAttribute("error_info", "密码不正确!");
            return "/login/login_user";
        }

        notificationCenter.publishSystemNotification(user.getUserId(), "您于" + Commons.formatDate("yyyy年MM月dd日 HH:mm:ss", new Date()) + "成功登录系统");
        logger.info("登录验证成功：" + user);

        // 记录登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setLoginUser(user);
        String browser = RequestUtils.getBrowserInfo(request);
        String ip = RequestUtils.getIpAddr(request);
        String os = RequestUtils.getOsInfo(request);
        loginLog.setLoginBrowser(browser);
        loginLog.setLoginIp(ip);
        loginLog.setLoginOpsys(os);
        loginLogService.insertLoginLog(loginLog);

        session.setAttribute("loginTeam", team);
        session.setAttribute("loginUser", user);
        return "redirect:/workbench";
    }

    /**
     * 处理登出请求
     */
    @RequestMapping("/logout")
    public String handleLogout(HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        session.removeAttribute("loginTeam");
        session.removeAttribute("loginUser");

        return "redirect:/login";
    }

    /**
     * 课题组注册页面
     */
    @GetMapping("/register")
    public String registerTeamPage() {
        return "/login/register_team";
    }

    /**
     * 处理课题组注册请求
     */
    @PostMapping("/register")
    public String handleTeamRegister(@RequestParam("team_name") String teamName,
                                     @RequestParam("team_identifier") String teamIdentifier,
                                     @RequestParam("username") String username,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     Model model) {

        model.addAttribute("inputTeamName", teamName);
        model.addAttribute("inputTeamIdentifier", teamIdentifier);
        model.addAttribute("inputUsername", username);
        model.addAttribute("inputEmail", email);
        model.addAttribute("inputPassword", password);

        Team team = teamService.getTeamByTeamIdentifier(teamIdentifier);
        if (team != null) {
            // 标识已被使用
            model.addAttribute("error_info", "课题组标识已被注册，请重新输入！");
            return "/login/register_team";
        }
        team = new Team();
        team.setTeamIdentification(teamIdentifier);
        team.setTeamName(teamName);

        String md5Password = passwordManager.generateMD5Password(password);
        User user = new User();
        user.setUserName(username);
        user.setUserPassword(md5Password);
        user.setUserEmail(email);
        user.setTeam(team);
        user.setTeamAuthority(User.CREATOR); // 设置权限为创建者

        if (teamService.insertTeam(team) == 0) {
            logger.error("注册课题组失败：team: " + team + ", user: " + user);
            model.addAttribute("error_info", "注册课题组失败，请联系管理员！");
            return "/login/register_team";
        }

        if (userService.insertUser(user) == 0) {
            logger.error("注册课题组创建者失败：team: " + team + ", user: " + user);
            model.addAttribute("error_info", "注册课题组失败，请联系管理员！");
            return "/login/register_team";
        }

        notificationCenter.publishSystemNotification(user.getUserId(), "恭喜您成功注册课题组「" + team.getTeamName() + "」，您可以在课题组信息维护页面邀请成员注册");

        logger.info("课题组注册成功，team: " + team + ", 创建者: " + user);

        return "redirect:/login/" + teamIdentifier;
    }

    /**
     * 邀请注册页面
     */
    @GetMapping("/invite/{team_identifier}")
    public String registerUserPage(@PathVariable("team_identifier") String teamIdentifier,
                                   @RequestParam(value = "token", defaultValue = "") String inviteToken,
                                   Model model) {
        Team team = teamService.getTeamByTeamIdentifier(teamIdentifier);
        if (team == null || !team.isInviteTokenEnabled() || !Commons.stringEquals(team.getInviteToken(), inviteToken)) {
            // 课题组不存在或链接失效
            return "/login/register_user_invalid";
        }
        model.addAttribute("team", team);
        model.addAttribute("inputToken", inviteToken);
        return "/login/register_user";
    }

    /**
     * 处理注册请求
     */
    @PostMapping("/register/{team_identifier}")
    public String handleRegisterUser(@PathVariable("team_identifier") String teamIdentifier,
                                     @RequestParam(value = "token", defaultValue = "") String inviteToken,
                                     @RequestParam(value = "username", defaultValue = "") String username,
                                     @RequestParam(value = "email", defaultValue = "") String email,
                                     @RequestParam(value = "password", defaultValue = "") String password,
                                     Model model) {
        logger.info("接收到用户注册请求");
        Team team = teamService.getTeamByTeamIdentifier(teamIdentifier);
        if (team == null || !team.isInviteTokenEnabled() || !Commons.stringEquals(team.getInviteToken(), inviteToken)) {
            // 课题组不存在或链接失效
            logger.info("用户注册失败：课题组不存在或链接失效");
            return "/login/register_user_invalid";
        }
        model.addAttribute("inputUsername", username);
        model.addAttribute("inputEmail", email);
        model.addAttribute("inputPassword", password);
        model.addAttribute("inputToken", inviteToken);
        model.addAttribute("team", team);

        User user = userService.getUserByTeamIdentifierAndEmail(teamIdentifier, email);
        if (user != null) {
            // 邮箱已被注册
            logger.info("用户注册失败：邮箱已被注册");
            model.addAttribute("error_info", "邮箱已被注册！");
            return "/login/register_user";
        }

        user = new User();
        user.setTeam(team);
        user.setUserName(username);
        user.setUserEmail(email);
        user.setUserPassword(passwordManager.generateMD5Password(password));
        user.setTeamAuthority(User.MEMBER);

        if (userService.insertUser(user) == 0) {
            logger.error("成员注册失败：team: " + team + ", user: " + user);
            model.addAttribute("error_info", "注册用户失败，请联系管理员！");
            return "/login/register_user";
        }

        notificationCenter.publishSystemNotification(user.getUserId(), "恭喜您成功加入「" + team.getTeamName() + "」课题组，您现在可以进行创建课题查看日程等操作，也可以通知课题管理员将您加入指定课题");

        logger.info("用户注册成功：" + user);

        return "redirect:/login/" + teamIdentifier;
    }
}
