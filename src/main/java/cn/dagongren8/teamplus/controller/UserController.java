package cn.dagongren8.teamplus.controller;

import cn.dagongren8.teamplus.component.BaiduObjectStore;
import cn.dagongren8.teamplus.component.NotificationCenter;
import cn.dagongren8.teamplus.component.PasswordManager;
import cn.dagongren8.teamplus.entity.Task;
import cn.dagongren8.teamplus.entity.Team;
import cn.dagongren8.teamplus.entity.User;
import cn.dagongren8.teamplus.service.TaskService;
import cn.dagongren8.teamplus.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 用户页面相关请求
 */
@Controller
public class UserController {

    private final PasswordManager passwordManager;
    private final UserService userService;
    private final TaskService taskService;
    private final BaiduObjectStore baiduObjectStore;
    private final NotificationCenter notificationCenter;
    Logger logger = LoggerFactory.getLogger(getClass());

    public UserController(PasswordManager passwordManager, UserService userService, TaskService taskService, BaiduObjectStore baiduObjectStore, NotificationCenter notificationCenter) {
        this.passwordManager = passwordManager;
        this.userService = userService;
        this.taskService = taskService;
        this.baiduObjectStore = baiduObjectStore;
        this.notificationCenter = notificationCenter;
    }

    /**
     * 用户信息维护页面
     */
    @GetMapping("/user/info")
    public String userPage(Model model, HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        return "/user/user";
    }

    /*
     * 用户信息修改
     */
    @PostMapping("/user/info")
    public String handleUserInfoUpdate(Model model, HttpSession session,
                                       @RequestParam("userId") int userId,
                                       @RequestParam("userName") String userName,
                                       @RequestParam("userEmail") String userEmail,
                                       @RequestParam("userPassword") String userPassword,
                                       @RequestParam("passwordAgain") String passwordAgain){
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        model.addAttribute("inputUserId", userId);
        model.addAttribute("inputUserName", userName);
        model.addAttribute("inputUserEmail", userEmail);
        model.addAttribute("inputUserPassword", userPassword);
        model.addAttribute("inputPasswordAgain", passwordAgain);

        /*
         * 修改邮箱验证邮箱是否重复
         * 有一个问题：如果将邮箱作为一个键唯一存在，同时用户退出课题组时没有删除记录，那么那个用户的邮箱就无法重复使用
         */
        User userByEmail = userService.getUserByTeamIdentifierAndEmail(team.getTeamIdentification(), userEmail);
        if(userByEmail != null && !Objects.equals(userByEmail.getUserId(),user.getUserId())){
            logger.info("修改个人信息失败：（" + userEmail + "）该邮箱已被注册");
            model.addAttribute("error_info", "该邮箱已被注册，请更换一个有效邮箱地址");
            model.addAttribute("tab",2);
            return "/user/user";
        }

        /*
         *  验证新密码两次修改是否一致
         */
        if (userPassword != null && !Objects.equals(userPassword, passwordAgain)) {
            logger.info("修改个人信息失败：新密码输入不一致");
            model.addAttribute("error_info", "新密码输入不一致，请重新输入");
            model.addAttribute("tab", 2);
            return "/user/user";
        }

        if (userPassword != null && !userPassword.isEmpty()) {
            String md5Password = passwordManager.generateMD5Password(userPassword);
            user.setUserPassword(md5Password);
            notificationCenter.publishSystemNotification(user.getUserId(), "您已成功修改密码");
        }

        user.setUserName(userName);
        user.setUserEmail(userEmail);

        if (userService.updateUserById(user) == 0) {
            // 更新失败
            logger.error("修改个人信息失败：个人信息：" + user);
            model.addAttribute("error_info", "更新个人信息失败，请联系管理员或稍后再试");
            model.addAttribute("tab", 2);
            return "/user/user";
        }

        notificationCenter.publishSystemNotification(user.getUserId(), "您已成功修改个人信息");
        logger.info("修改个人信息成功：" + user);

        return "/user/user";
    }

    /*
     * 退出课题组
     */
    @RequestMapping("/user/info/quit")
    public String handleUserQuitTeam(Model model, HttpSession session){
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        user.setTeamAuthority(User.LOGOUT);

        if (userService.updateUserById(user) == 0) {
            // 更新失败
            logger.error("退出课题组操作失败：退出用户：" + user);
            model.addAttribute("error_info", "退出课题组操作失败，请联系管理员或稍后再试");
            return "/user/user";
        }

        notificationCenter.publishSystemNotification(user.getUserId(), "您已成功退出课题组");

        logger.info("用户成功退出课题组：" + user);

        return "/login/login_team";
    }

    /**
     * 处理用户头像变更请求
     */
    @PostMapping("/user/info/avatar")
    public String handleTeamAvatarChange(@RequestParam(value = "userAvatar", defaultValue = "") MultipartFile file,
                                         HttpSession session, Model model) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        if (file == null) {
            logger.info("修改头像失败：输入的文件为空");
            return "/user/user";
        }

        String url = null;

        try {
            url = baiduObjectStore.putBytesToBCE(file.getBytes());
        } catch (IOException e) {
            logger.error("修改头像失败", e);
            return  "/user/user";
        }

        user.setUserAvatar(url);

        if (userService.updateUserById(user) == 0) {
            // 更新失败
            logger.error("用户头像修改失败：用户：" + user);
            model.addAttribute("error_info", "头像修改失败，请联系管理员或稍后再试");
            return "/user/user";
        }

        notificationCenter.publishSystemNotification(user.getUserId(), "您已成功修改头像");

        logger.info("用户修改头像成功：" + user);

        return "/user/user";
    }

    /**
     * 工作台页面
     */
    @GetMapping("/workbench")
    public String workbenchPage(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize,
                                Model model, HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        PageHelper.startPage(pageIndex, pageSize);
        List<Task> list = taskService.getUserTasks(user.getUserId());
        PageInfo<Task> tasks = new PageInfo<>(list);
        model.addAttribute("tasks", tasks);

        return "/user/workbench";
    }

    /**
     * 处理任务状态变更请求
     */
    @PostMapping("/workbench/status")
    public String handleUpdateStatus(Model model, HttpSession session,
                                     @RequestParam("taskId") Integer taskId,
                                     @RequestParam("status") Integer status) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        Task task = taskService.getTaskById(taskId);
        if (task.getProcessor() == null || task.getProcessor().getUserId() != user.getUserId()) {
            logger.info("修改任务状态失败：权限不足");
            return "redirect:/workbench";
        }

        task.setTaskStatus(status);
        if (taskService.updateTaskById(task) == 0) {
            logger.info("修改任务状态失败：数据库错误");
            return "redirect:/workbench";
        }

        logger.info("修改任务状态成功：" + task);

        return "redirect:/workbench";
    }
}
