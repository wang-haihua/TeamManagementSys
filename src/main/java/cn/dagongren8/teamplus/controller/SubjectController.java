package cn.dagongren8.teamplus.controller;

import cn.dagongren8.teamplus.component.BaiduObjectStore;
import cn.dagongren8.teamplus.component.NotificationCenter;
import cn.dagongren8.teamplus.component.PasswordManager;
import cn.dagongren8.teamplus.entity.*;
import cn.dagongren8.teamplus.service.AnnouncementService;
import cn.dagongren8.teamplus.service.SubjectAuthorityService;
import cn.dagongren8.teamplus.service.SubjectService;
import cn.dagongren8.teamplus.service.UserService;
import cn.dagongren8.teamplus.util.Commons;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * 处理课题相关请求
 */
@Controller
public class SubjectController {

    private final UserService userService;
    private final SubjectService subjectService;
    private final SubjectAuthorityService subjectAuthorityService;
    private final BaiduObjectStore baiduObjectStore;
    private final AnnouncementService announcementService;
    private final PasswordManager passwordManager;
    private final NotificationCenter notificationCenter;
    Logger logger = LoggerFactory.getLogger(getClass());

    public SubjectController(UserService userService, SubjectService subjectService, SubjectAuthorityService subjectAuthorityService, BaiduObjectStore baiduObjectStore, AnnouncementService announcementService, PasswordManager passwordManager, NotificationCenter notificationCenter) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.subjectAuthorityService = subjectAuthorityService;
        this.baiduObjectStore = baiduObjectStore;
        this.announcementService = announcementService;
        this.passwordManager = passwordManager;
        this.notificationCenter = notificationCenter;
    }

    /**
     * 课题浏览页面
     */
    @GetMapping("/subjects")
    public String subjectsPage(Model model, HttpSession session,
                               @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        PageInfo<SubjectAuthority> subjects = subjectService.getSubjectsOfUser(user.getUserId(), pageIndex, pageSize);
        for (SubjectAuthority sa : subjects.getList()) {
            // 获取每个课题的成员列表
            sa.getSubject().setSubjectAuthorities(subjectAuthorityService.getAllSubjectUsers(sa.getSubject().getSubjectId()));
        }

        model.addAttribute("subjects", subjects);

        return "/user/subjects";
    }

    /**
     * 课题添加页面
     */
    @GetMapping("/subjects/add")
    public String subjectAddPage(Model model, HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        List<User> teamUsers = userService.getAllTeamUsers(team.getTeamId());
        teamUsers.removeIf(x -> Objects.equals(x.getUserId(), user.getUserId()));
        model.addAttribute("teamUsers", teamUsers);

        return "/subject/subject_add";
    }

    /**
     * 处理课题的添加请求
     */
    @PostMapping("/subjects/add")
    public String handleSubjectAdd(Model model, HttpSession session,
                                   @RequestParam("subjectName") String subjectName,
                                   @RequestParam("subjectIdentifier") String subjectIdentifier,
                                   @RequestParam("subjectDescription") String subjectDescription,
                                   @RequestParam("subjectAvatar") MultipartFile file,
                                   @RequestParam(value = "members", defaultValue = "") List<Integer> members) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        model.addAttribute("inputSubjectName", subjectName);
        model.addAttribute("inputSubjectIdentifier", subjectIdentifier);
        model.addAttribute("inputSubjectDescription", subjectDescription);

        List<User> teamUsers = userService.getAllTeamUsers(team.getTeamId());
        teamUsers.removeIf(x -> Objects.equals(x.getUserId(), user.getUserId()));
        model.addAttribute("teamUsers", teamUsers);

        if (subjectName.isEmpty()) {
            logger.info("添加课题失败：课题名称为空");
            model.addAttribute("error_info", "课题名称不能为空！");
            return "/subject/subject_add";
        }

        if (subjectService.getSubjectByIdentification(subjectIdentifier) != null) {
            logger.info("添加课题失败：存在重复标识（" + subjectIdentifier + "）");
            model.addAttribute("error_info", "标识重复，请重新输入！");
            return "/subject/subject_add";
        }

        Subject subject = new Subject();
        subject.setSubjectName(subjectName);
        subject.setSubjectIdentification(subjectIdentifier);
        subject.setSubjectDescription(subjectDescription);
        subject.setSubjectStatus(Subject.NOT_STARTED);

        if (!file.isEmpty()) {
            String url = null;
            try {
                url = baiduObjectStore.putBytesToBCE(file.getBytes());
            } catch (IOException e) {
                logger.error("修改头像失败", e);
                model.addAttribute("error_info", "头像修改失败，请联系管理员");
                return "/subject/subject_add";
            }
            subject.setSubjectAvatar(url);
        }

        if (subjectService.insertSubject(subject) == 0) {
            logger.error("添加课题失败：" + subject);
            model.addAttribute("error_info", "添加课题失败，请联系管理员");
            return "/subject/subject_add";
        }

        SubjectAuthority subjectAuthority = new SubjectAuthority();
        subjectAuthority.setUser(user);
        subjectAuthority.setSubject(subject);
        subjectAuthority.setSubjectAuthorityType(User.CREATOR);

        if (subjectAuthorityService.insertSubjectAuthority(subjectAuthority) == 0) {
            logger.error("添加课题失败，创建权限时发生错误：" + subject + "， " + subjectAuthority);
            model.addAttribute("error_info", "添加课题失败，请联系管理员");
            return "/subject/subject_add";
        }

        for (Integer uid : members) {
            SubjectAuthority sa = new SubjectAuthority();
            User u = new User();
            u.setUserId(uid);
            sa.setUser(u);
            sa.setSubject(subject);
            sa.setSubjectAuthorityType(User.MEMBER);

            if (subjectAuthorityService.insertSubjectAuthority(sa) == 0) {
                logger.error("添加课题失败，创建权限时发生错误：" + subject + "， " + sa);
                model.addAttribute("error_info", "添加课题失败，请联系管理员");
                return "/subject/subject_add";
            }
        }

        notificationCenter.publishSystemNotification(user.getUserId(), "您已成功创建课题「" + subject.getSubjectName() + "」");

        logger.info("添加课题成功：" + subject);
        return "redirect:/subjects";
    }

    /**
     * 跳转到课题概览页面
     */
    @GetMapping("/s/{subject_identifier}")
    public String subjectPage(Model model, HttpSession session,
                              @PathVariable("subject_identifier") String subjectIdentifier) {
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        return "redirect:/s/" + subjectIdentifier + "/status";
    }

    /**
     * 课题概览页面
     */
    @GetMapping("/s/{subject_identifier}/status")
    public String subjectStatusPage(Model model, HttpSession session,
                                    @PathVariable("subject_identifier") String subjectIdentifier) {
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        List<SubjectAuthority> users = subjectAuthorityService.getAllSubjectUsers(subject.getSubjectId());

        int totalMembers = 0;
        int totalManagers = 0;
        User creator = null;
        for (SubjectAuthority sa : users) {
            if (sa.getSubjectAuthorityType() == User.MEMBER) totalMembers++;
            if (sa.getSubjectAuthorityType() == User.MANAGER) totalManagers++;
            if (sa.getSubjectAuthorityType() == User.CREATOR) creator = sa.getUser();
        }

        model.addAttribute("creator", creator);
        model.addAttribute("totalMembers", totalMembers);
        model.addAttribute("totalManagers", totalManagers);

        List<Announcement> announcements = announcementService.getAllAnnouncementOfSubject(subject.getSubjectId());
        model.addAttribute("announcements", announcements);

        return "/subject/subject_status";
    }


    /**
     * 课题成员页面
     */
    @GetMapping("/s/{subject_identifier}/members")
    public String subjectMembersPage(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                     @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize,
                                     Model model, HttpSession session,
                                     @PathVariable("subject_identifier") String subjectIdentifier) {

        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        SubjectAuthority subjectAuthority = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), user.getUserId());
        model.addAttribute("subjectAuthority", subjectAuthority);

        PageInfo<SubjectAuthority> users = subjectAuthorityService.getAllSubjectUsers(subject.getSubjectId(), pageIndex, pageSize);

        model.addAttribute("users", users);


        // 处理添加成员的列表
        List<SubjectAuthority> allSubjectUsers = subjectAuthorityService.getAllSubjectUsers(subject.getSubjectId());
        HashSet<Integer> ids = new HashSet<>();
        for (SubjectAuthority sa : allSubjectUsers) ids.add(sa.getUser().getUserId());
        List<User> teamUsers = userService.getAllTeamUsers(team.getTeamId());
        teamUsers.removeIf(x -> ids.contains(x.getUserId()));
        model.addAttribute("teamUsers", teamUsers);

        return "/subject/subject_member";
    }

    /**
     * 处理课题成员添加请求
     */
    @PostMapping("/s/{subject_identifier}/members/add")
    public String handleMemberAdding(Model model, HttpSession session,
                                     @PathVariable("subject_identifier") String subjectIdentifier,
                                     @RequestParam("members") List<Integer> members) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        List<SubjectAuthority> allSubjectUsers = subjectAuthorityService.getAllSubjectUsers(subject.getSubjectId());
        HashSet<Integer> ids = new HashSet<>();
        for (SubjectAuthority sa : allSubjectUsers) ids.add(sa.getUser().getUserId());

        for (Integer uid : members) {
            if (ids.contains(uid)) {
                // 如果该成员已经是课题成员，则略过
                continue;
            }

            SubjectAuthority sa = new SubjectAuthority();
            User u = new User();
            u.setUserId(uid);
            sa.setUser(u);
            sa.setSubject(subject);
            sa.setSubjectAuthorityType(User.MEMBER);

            if (subjectAuthorityService.insertSubjectAuthority(sa) == 0) {
                logger.error("添加成员失败，创建权限时发生错误：" + subject + "， " + sa);
                return "redirect:/s/" + subjectIdentifier + "/members";
            }

            notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "将" + u.getUserName() + "添加进课题「" + subject.getSubjectName() + "」");
        }

        logger.info("添加成员成功，共添加" + members.size() + "成员，" + subject);

        return "redirect:/s/" + subjectIdentifier + "/members";
    }

    /**
     * 处理课题成员权限变更请求
     */
    @GetMapping("/s/{subject_identifier}/authority")
    public String handleAuthorityChange(Model model, HttpSession session,
                                        @PathVariable("subject_identifier") String subjectIdentifier,
                                        @RequestParam("uid") int uid,
                                        @RequestParam("op") int op) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        SubjectAuthority subjectAuthority_user = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), user.getUserId());
        if (subjectAuthority_user.getSubjectAuthorityType()!= 1 && subjectAuthority_user.getSubjectAuthorityType()!= 2) {
            logger.info("用户权限修改失败：操作发起者权限不足（uid=" + uid + "）");
            return "redirect:/s/" + subject.getSubjectIdentification() + "/members";
        }

        User u = userService.getUserById(uid);

        if (u == null) {
            logger.info("用户权限修改失败：不存在对应的用户（uid=" + uid + "）");
            return "redirect:/s/" + subject.getSubjectIdentification() + "/members";
        }

        SubjectAuthority subjectAuthority = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), u.getUserId());

        if (subjectAuthority.getSubject().getSubjectId() != subjectAuthority_user.getSubject().getSubjectId()) {
            logger.info("用户权限修改失败：操作发起者与被修改用户不在同一课题组（uid=" + uid + "）");
            return "redirect:/s/" + subject.getSubjectIdentification() + "/members";

        }

        if (op == 0) {
            subjectAuthority.setSubjectAuthorityType(User.MEMBER);
            notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "将" + u.getUserName() + "在课题「" + subject.getSubjectName() + "」中的权限修改为成员");
        } else if (op == 2) {
            subjectAuthority.setSubjectAuthorityType(User.MANAGER);
            notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "将" + u.getUserName() + "在课题「" + subject.getSubjectName() + "」中的权限修改为管理员");
        } else {
            logger.info("用户权限修改失败：操作错误（uid=" + uid + "）");
            return "redirect:/s/" + subject.getSubjectIdentification() + "/members";
        }

        subjectAuthorityService.updateSubjectAuthority(subjectAuthority);
        logger.info("用户权限修改成功" + u);
        return "redirect:/s/" + subject.getSubjectIdentification() + "/members";
    }

    /**
     * 课题信息维护页面
     */
    @GetMapping("/s/{subject_identifier}/setting")
    public String subjectSettingPage(Model model, HttpSession session,
                                     @PathVariable("subject_identifier") String subjectIdentifier) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);
        SubjectAuthority subjectAuthority = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), user.getUserId());
        model.addAttribute("subjectAuthority", subjectAuthority);

        List<SubjectAuthority> subjectUsers = subjectAuthorityService.getAllSubjectUsers(subject.getSubjectId());
        model.addAttribute("subjectUsers", subjectUsers);

        return "/subject/subject_info";
    }

    /**
     * 处理课题转让请求
     */
    @PostMapping("/s/{subject_identifier}/transfer")
    public String handleSubjectTransfer(Model model, HttpSession session,
                                        @PathVariable("subject_identifier") String subjectIdentifier,
                                        @RequestParam("toUserId") Integer toUserId) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        SubjectAuthority oldCreator = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), user.getUserId());
        if (oldCreator == null || oldCreator.getSubjectAuthorityType() != User.CREATOR) {
            logger.info("转让课题失败，权限不足");
            return "redirect:/s/" + subjectIdentifier + "/setting";
        }

        SubjectAuthority newCreator = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), toUserId);
        if (newCreator == null) {
            logger.info("转让课题失败，所选择的用户不是该课题的成员");
            return "redirect:/s/" + subjectIdentifier + "/setting";
        }

        newCreator.setSubjectAuthorityType(User.CREATOR);
        oldCreator.setSubjectAuthorityType(User.MANAGER);

        if (subjectAuthorityService.updateSubjectAuthority(newCreator) == 0) {
            logger.info("转让课题失败，数据库错误");
            return "redirect:/s/" + subjectIdentifier + "/setting";
        }

        if (subjectAuthorityService.updateSubjectAuthority(oldCreator) == 0) {
            logger.info("转让课题失败，数据库错误");
            return "redirect:/s/" + subjectIdentifier + "/setting";
        }

        notificationCenter.publishSubjectNotification(newCreator.getUser().getUserId(), subject.getSubjectId(), oldCreator.getUser().getUserName() + "将课题「" + subject.getSubjectName() + "」转让给" + newCreator.getUser().getUserName());

        logger.info("转让课题成功");

        return "redirect:/s/" + subjectIdentifier + "/setting";
    }

    /**
     * 处理课题信息变更请求
     */
    @PostMapping("/s/{subject_identifier}/setting")
    public String handleSubjectUpdate(Model model, HttpSession session,
                                      @PathVariable("subject_identifier") String subjectIdentifier,
                                      @RequestParam("subjectName") String subjectName,
                                      @RequestParam("subjectIdentifier") String newSubjectIdentifier,
                                      @RequestParam("subjectDescription") String subjectDescription) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        Subject subjectByIdentification = subjectService.getSubjectByIdentification(newSubjectIdentifier);
        if (subjectByIdentification != null && !Commons.stringEquals(subjectIdentifier, newSubjectIdentifier)) {
            model.addAttribute("error_info", "课题标识已被使用！");
            Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
            model.addAttribute("subject", subject);
            SubjectAuthority subjectAuthority = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), user.getUserId());
            model.addAttribute("subjectAuthority", subjectAuthority);

            List<SubjectAuthority> subjectUsers = subjectAuthorityService.getAllSubjectUsers(subject.getSubjectId());
            model.addAttribute("subjectUsers", subjectUsers);
            return "/subject/subject_info";
        }

        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        subject.setSubjectName(subjectName);
        subject.setSubjectIdentification(newSubjectIdentifier);
        subject.setSubjectDescription(subjectDescription);

        if (subjectService.updateSubjectById(subject) == 0) {
            logger.error("修改课题信息失败：" + subject);
            return "/subject/subject_info";
        }

        notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "修改了课题信息");

        return "redirect:/s/" + newSubjectIdentifier + "/setting";
    }

    /**
     * 处理课题头像变更请求
     */
    @PostMapping("/s/{subject_identifier}/setting/avatar")
    public String handleSubjectAvatarChange(@RequestParam(value = "subjectAvatar", defaultValue = "") MultipartFile file,
                                            @PathVariable("subject_identifier") String subjectIdentifier,
                                            HttpSession session, Model model) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);
        if (file == null) {
            logger.info("修改头像失败：输入的文件为空");
            return "/subject/subject_info";
        }

        String url = null;

        try {
            url = baiduObjectStore.putBytesToBCE(file.getBytes());
        } catch (IOException e) {
            logger.error("修改头像失败", e);
            return "/subject/subject_info";
        }

        subject.setSubjectAvatar(url);
        subjectService.updateSubjectById(subject);

        notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "更改了课题「" + subject.getSubjectName() + "」的头像");

        return "redirect:/s/" + subjectIdentifier + "/setting";
    }

    /**
     * 处理课题公告添加请求
     */
    @PostMapping("/s/{subject_identifier}/announcement")
    public String handleAnnouncementPublish(Model model, HttpSession session,
                                            @PathVariable("subject_identifier") String subjectIdentifier,
                                            @RequestParam("content") String content) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        Announcement announcement = new Announcement();
        announcement.setSubject(subject);
        announcement.setUser(user);
        announcement.setAnnouncementContent(content);

        if (announcementService.insertAnnouncement(announcement) == 0) {
            logger.error("公告发布失败：插入失败" + announcement);
            return "redirect:/s/" + subject.getSubjectIdentification();
        }

        notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "在课题「" + subject.getSubjectName() + "」中发布了新公告：" + announcement.getAnnouncementContent());

        logger.info("公告发布成功：" + announcement);

        return "redirect:/s/" + subject.getSubjectIdentification();
    }

    /**
     * 处理课题删除请求
     */
    @PostMapping("/s/{subject_identifier}/delete")
    public String handleSubjectDelete(Model model, HttpSession session,
                                      @PathVariable("subject_identifier") String subjectIdentifier,
                                      @RequestParam("password") String password) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        SubjectAuthority subjectAuthority = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), user.getUserId());
        if (subjectAuthority == null || subjectAuthority.getSubjectAuthorityType() != User.CREATOR) {
            logger.info("删除课题失败：权限不足");
            model.addAttribute("tab", 2);
            model.addAttribute("delete_error", true);
            return "/subject/subject_info";
        }

        if (!passwordManager.checkPassword(user, password)) {
            logger.info("删除课题失败：密码错误");
            model.addAttribute("tab", 2);
            model.addAttribute("delete_error", true);
            return "/subject/subject_info";
        }

        if (subjectService.deleteSubjectById(subject.getSubjectId()) == 0) {
            logger.error("删除课题失败：数据库错误");
            model.addAttribute("tab", 2);
            model.addAttribute("delete_error", true);
            return "/subject/subject_info";
        }

        notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "解散了课题「" + subject.getSubjectName() + "」");

        logger.info("删除课题成功" + subject);

        return "redirect:/subjects";
    }

    /**
     * 处理课题成员移除请求
     */
    @GetMapping("/s/{subject_identifier}/members/remove/{userId}")
    public String handleSubjectMemberRemove(Model model, HttpSession session,
                                            @PathVariable("subject_identifier") String subjectIdentifier,
                                            @PathVariable("userId") Integer userId) {

        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        SubjectAuthority subjectAuthority = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), user.getUserId());
        if (subjectAuthority == null || subjectAuthority.getSubjectAuthorityType() == User.MEMBER) {
            logger.info("移除成员失败，权限不足");
            return "redirect:/s/" + subjectIdentifier + "/members";
        }

        if (subjectAuthorityService.deleteSubjectAuthority(subject.getSubjectId(), userId) == 0) {
            logger.error("移除成员失败，可能该成员不属于该课题，userId=" + userId + ", subject=" + subject);
            return "redirect:/s/" + subjectIdentifier + "/members";
        }

        notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "将" + subjectAuthority.getUser().getUserName() + "移出了课题「" + subject.getSubjectName() + "」");
        logger.info("移除成员成功，userId=" + userId + ", subject=" + subject);

        return "redirect:/s/" + subjectIdentifier + "/members";
    }


    /**
     * 处理任务状态变更请求
     */
    @PostMapping("/s/{subject_identifier}/status")
    public String handleSubjectStatusChange(Model model, HttpSession session,
                                            @PathVariable("subject_identifier") String subjectIdentifier,
                                            @RequestParam("status") Integer status) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        subject.setSubjectStatus(status);

        if (subjectService.updateSubjectById(subject) == 0) {
            logger.info("修改课题状态失败：数据库错误");
            return "redirect:/s/" + subjectIdentifier + "/setting";
        }
        logger.info("修改课题状态成功：" + subject);
        return "redirect:/s/" + subjectIdentifier + "/setting";
    }
}