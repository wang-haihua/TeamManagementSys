package cn.dagongren8.teamplus.controller;

import cn.dagongren8.teamplus.component.BaiduObjectStore;
import cn.dagongren8.teamplus.component.NotificationCenter;
import cn.dagongren8.teamplus.component.PasswordManager;
import cn.dagongren8.teamplus.entity.Team;
import cn.dagongren8.teamplus.entity.User;
import cn.dagongren8.teamplus.service.TeamService;
import cn.dagongren8.teamplus.service.UserService;
import cn.dagongren8.teamplus.util.Commons;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 处理课题组相关请求
 */
@Controller
public class TeamController {

    private final UserService userService;
    private final TeamService teamService;
    private final BaiduObjectStore baiduObjectStore;
    private final PasswordManager passwordManager;
    private final NotificationCenter notificationCenter;
    Logger logger = LoggerFactory.getLogger(getClass());

    public TeamController(UserService userService, TeamService teamService, BaiduObjectStore baiduObjectStore, PasswordManager passwordManager, NotificationCenter notificationCenter) {
        this.userService = userService;
        this.teamService = teamService;
        this.baiduObjectStore = baiduObjectStore;
        this.passwordManager = passwordManager;
        this.notificationCenter = notificationCenter;
    }

    /**
     * 课题组信息维护页面
     */
    @GetMapping("/team/info")
    public String teamPage(Model model, HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        List<User> teamUsers = userService.getAllTeamUsers(team.getTeamId());
        model.addAttribute("teamUsers", teamUsers);

        return "/team/team";
    }

    /**
     * 处理请求修改课题组信息
     */
    @PostMapping("/team/info")
    public String handleTeamInfoUpdate(Model model, HttpSession session,
                                       @RequestParam("teamId") int teamId,
                                       @RequestParam("teamName") String teamName,
                                       @RequestParam("teamIdentifier") String teamIdentifier,
                                       @RequestParam("teamDescription") String teamDescription) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        model.addAttribute("inputTeamId", teamId);
        model.addAttribute("inputTeamName", teamName);
        model.addAttribute("inputTeamIdentifier", teamIdentifier);
        model.addAttribute("inputTeamDescription", teamDescription);

        List<User> teamUsers = userService.getAllTeamUsers(team.getTeamId());
        model.addAttribute("teamUsers", teamUsers);

        Team teamByTeamIdentifier = teamService.getTeamByTeamIdentifier(teamIdentifier);

        if (teamByTeamIdentifier != null && !Objects.equals(teamByTeamIdentifier.getTeamId(), team.getTeamId())) {
            // 新输入的标识重复了
            logger.info("修改课题组信息失败：已存在相同的课题组标识（" + teamIdentifier + "）");
            model.addAttribute("error_info", "输入的标识已经存在，请更换一个标识");
            return "/team/team";
        }

        team.setTeamName(teamName);
        team.setTeamIdentification(teamIdentifier);
        team.setTeamDescription(teamDescription);

        if (teamService.updateTeamById(team) == 0) {
            // 更新失败
            logger.error("修改课题组信息失败：课题组信息：" + team);
            model.addAttribute("error_info", "更新课题组信息失败，请联系管理员或稍后再试");
            return "/team/team";
        }

        notificationCenter.publishTeamNotification(user.getUserId(), user.getUserName() + "更新了课题组基本信息");

        logger.info("修改课题组信息成功：" + team);

        return "/team/team";
    }

    /**
     * 处理修改课题组头像请求
     */
    @PostMapping("/team/info/avatar")
    public String handleTeamAvatarChange(@RequestParam(value = "teamAvatar", defaultValue = "") MultipartFile file,
                                         HttpSession session, Model model) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        List<User> teamUsers = userService.getAllTeamUsers(team.getTeamId());
        model.addAttribute("teamUsers", teamUsers);

        if (file == null) {
            logger.info("修改头像失败：输入的文件为空");
            return "/team/team";
        }

        String url = null;

        try {
            url = baiduObjectStore.putBytesToBCE(file.getBytes());
        } catch (IOException e) {
            logger.error("修改头像失败", e);
            return "/team/team";
        }

        team.setTeamAvatar(url);
        teamService.updateTeamById(team);

        notificationCenter.publishTeamNotification(user.getUserId(), user.getUserName() + "更新了课题组头像");

        return "/team/team";
    }

    /**
     * 开启邀请链接，或重置邀请链接
     */
    @RequestMapping("/team/info/invite/enable")
    public String handleInviteEnable(Model model, HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        List<User> teamUsers = userService.getAllTeamUsers(team.getTeamId());
        model.addAttribute("teamUsers", teamUsers);

        // 随机生成一个token
        String token = Commons.getMD5(Commons.getRandomUUID(), "");

        team.setInviteTokenEnabled(true);
        team.setInviteToken(token);
        teamService.updateTeamById(team);

        model.addAttribute("tab", 2);

        notificationCenter.publishTeamNotification(user.getUserId(), user.getUserName() + "开启了邀请注册链接");

        return "/team/team";
    }

    /**
     * 停用邀请链接
     */
    @RequestMapping("/team/info/invite/disable")
    public String handleInviteDisable(Model model, HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        team.setInviteTokenEnabled(false);
        teamService.updateTeamById(team);

        model.addAttribute("tab", 2);

        List<User> teamUsers = userService.getAllTeamUsers(team.getTeamId());
        model.addAttribute("teamUsers", teamUsers);

        notificationCenter.publishTeamNotification(user.getUserId(), user.getUserName() + "停用了邀请注册链接");

        return "/team/team";
    }

    /**
     * 课题组成员页面
     */
    @GetMapping("/team/member")
    public String memberPage(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                             @RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize,
                             Model model, HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        PageInfo<User> users = userService.getAllTeamUsers(team.getTeamId(), pageIndex, pageSize);
        model.addAttribute("users", users);

        return "/team/member";
    }

    /**
     * 修改用户课题组权限
     */
    @GetMapping("/team/member/authority/{uid}")
    public String handleAuthorityChange(Model model, HttpSession session,
                                        @PathVariable("uid") int uid,
                                        @RequestParam("op") int op) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        if (user.getTeamAuthority() != User.CREATOR && user.getTeamAuthority() != User.MANAGER) {
            logger.info("用户权限修改失败：操作发起者权限不足（uid=" + uid + "）");
            return "redirect:/team/member";
        }


        User u = userService.getUserById(uid);

        if (u == null) {
            logger.info("用户权限修改失败：不存在对应的用户（uid=" + uid + "）");
            return "redirect:/team/member";
        }

        if (!Objects.equals(u.getTeam().getTeamId(), team.getTeamId())) {
            logger.info("用户权限修改失败：操作发起者与被修改用户不在同一课题组（uid=" + uid + "）");
            return "redirect:/team/member";

        }

        if (op == 0) {
            u.setTeamAuthority(User.MEMBER);
            notificationCenter.publishTeamNotification(user.getUserId(), user.getUserName() + "将" + u.getUserName() + "的课题组权限修改为成员");
        } else if (op == 2) {
            u.setTeamAuthority(User.MANAGER);
            notificationCenter.publishTeamNotification(user.getUserId(), user.getUserName() + "将" + u.getUserName() + "的课题组权限修改为管理员");
        } else {
            logger.info("用户权限修改失败：操作错误（uid=" + uid + "）");
            return "redirect:/team/member";
        }

        userService.updateUserById(u);
        logger.info("用户权限修改成功" + u);
        return "redirect:/team/member";
    }

    /**
     * 处理用户删除请求
     */
    @PostMapping("/team/delete")
    public String handleTeamDelete(Model model, HttpSession session,
                                   @RequestParam("password") String password) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        List<User> teamUsers = userService.getAllTeamUsers(team.getTeamId());
        model.addAttribute("teamUsers", teamUsers);

        if (user.getTeamAuthority() != User.CREATOR) {
            logger.info("解散课题组失败：用户权限不足" + user);
            model.addAttribute("delete_error", true);
            model.addAttribute("tab", 2);
            return "/team/team";
        }

        if (!passwordManager.checkPassword(user, password)) {
            logger.info("解散课题组失败：密码错误");
            model.addAttribute("delete_error", true);
            model.addAttribute("tab", 2);
            return "/team/team";
        }

        if (teamService.deleteTeamById(team.getTeamId()) == 0) {
            logger.error("解散课题组失败：数据库错误");
            model.addAttribute("delete_error", true);
            model.addAttribute("tab", 2);
            return "/team/team";
        }

        notificationCenter.publishTeamNotification(user.getUserId(), user.getUserName() + "解散了课题组");

        logger.info("解散课题组成功：" + team);

        return "redirect:/logout";
    }

    @GetMapping("/team/member/remove/{userId}")
    public String handleTeamMemberDelete(Model model, HttpSession session,
                                         @PathVariable("userId") Integer userId) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        if (user.getTeamAuthority() != User.CREATOR && user.getTeamAuthority() != User.MANAGER) {
            logger.info("移除成员失败：权限不足");
            return "redirect:/team/member";
        }

        User userById = userService.getUserById(userId);
        if (userById == null || !Objects.equals(userById.getTeam().getTeamId(), team.getTeamId())) {
            logger.info("移除成员失败：用户不存在，或用户不属于该课题组");
            return "redirect:/team/member";
        }

        if (userService.deleteUserById(userId) == 0) {
            logger.error("移除成员失败：数据库错误");
            return "redirect:/team/member";
        }

        notificationCenter.publishTeamNotification(user.getUserId(), user.getUserName() + "将" + userById.getUserName() + "移出了课题组");

        logger.info("移除成员成功：userId=" + userId);

        return "redirect:/team/member";
    }

    /**
     * 处理课题组转让请求
     */
    @PostMapping("/team/transfer")
    public String handleTeamTransfer(Model model, HttpSession session,
                                     @RequestParam("toUserId") Integer toUserId) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        if (user.getTeamAuthority() != User.CREATOR) {
            logger.info("转移课题组失败，权限不足");
            return "redirect:/team/info";
        }

        User toUser = userService.getUserById(toUserId);

        if (toUser == null || !Objects.equals(toUser.getTeam().getTeamId(), team.getTeamId())) {
            logger.info("转移课题组失败，所选成员不是课题组成员");
            return "redirect:/team/info";
        }

        user.setTeamAuthority(User.MANAGER);
        toUser.setTeamAuthority(User.CREATOR);

        if (userService.updateUserById(toUser) == 0) {
            logger.error("转移课题组失败，数据库错误");
            return "redirect:/team/info";
        }

        if (userService.updateUserById(user) == 0) {
            logger.error("转移课题组失败，数据库错误");
            return "redirect:/team/info";
        }

        notificationCenter.publishTeamNotification(user.getUserId(), user.getUserName() + "将课题组转让给" + toUser.getUserName());

        logger.info("转移课题组成功，team=" + team + ", newCreator=" + toUser);

        return "redirect:/team/info";
    }
}
