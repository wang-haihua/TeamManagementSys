package cn.dagongren8.teamplus.controller;

import cn.dagongren8.teamplus.component.NotificationCenter;
import cn.dagongren8.teamplus.entity.Notification;
import cn.dagongren8.teamplus.entity.Subject;
import cn.dagongren8.teamplus.entity.Team;
import cn.dagongren8.teamplus.entity.User;
import cn.dagongren8.teamplus.service.SubjectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 处理通知中心的相关请求
 */
@Controller
public class NotificationController {

    private final SubjectService subjectService;
    private final NotificationCenter notificationCenter;
    Logger logger = LoggerFactory.getLogger(getClass());

    public NotificationController(SubjectService subjectService, NotificationCenter notificationCenter) {
        this.subjectService = subjectService;
        this.notificationCenter = notificationCenter;
    }

    /**
     * 跳转到通知中心页面
     */
    @GetMapping("/notification")
    public String notificationPage() {
        return "redirect:/notification/all";
    }

    /**
     * 显示通知中心页面
     */
    @GetMapping("/notification/{type}")
    public String notificationPage(Model model, HttpSession session,
                                   @PathVariable("type") String type,
                                   @RequestParam(value = "q", defaultValue = "") String query,
                                   @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        model.addAttribute("type", type);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("query", "");

        List<Notification> list = null;

        PageHelper.startPage(pageIndex, pageSize);
        switch (type) {
            case "all":
                list = notificationCenter.getAllNotifications(user.getUserId());
                model.addAttribute("title", "全部通知");
                model.addAttribute("url", "/notification/all");
                break;
            case "system":
                list = notificationCenter.getAllSystemNotifications(user.getUserId());
                model.addAttribute("title", "系统通知");
                model.addAttribute("url", "/notification/system");
                break;
            case "team":
                list = notificationCenter.getAllTeamNotifications(user.getUserId());
                model.addAttribute("title", "课题组通知");
                model.addAttribute("url", "/notification/team");
                break;
            case "subject":
                list = notificationCenter.getAllSubjectNotifications(user.getUserId());
                model.addAttribute("title", "课题通知");
                model.addAttribute("url", "/notification/subject");
                break;
            case "search":
                list = notificationCenter.findNotificationsContains(user.getUserId(), query);
                model.addAttribute("title", "搜索结果");
                model.addAttribute("url", "/notification/search");
                model.addAttribute("query", "&q=" + query);
                break;
            default:
                return "error/404";
        }

        PageInfo<Notification> notifications = new PageInfo<>(list);

        model.addAttribute("notifications", notifications);

        List<Subject> subjects = subjectService.getSubjectsOfUser(user.getUserId());
        model.addAttribute("subjects", subjects);

        int[] cnt = notificationCenter.getUnreadCounts(user.getUserId());
        model.addAttribute("cnt", cnt);

        return "/user/notification";
    }

    /**
     * 显示通知中心页面（课题通知）
     */
    @GetMapping("/notification/subject/{subjectIdentifier}")
    public String notificationSubjectPage(Model model, HttpSession session,
                                          @PathVariable("subjectIdentifier") String subjectIdentifier,
                                          @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        model.addAttribute("type", "subject");
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("subjectIdentifier", subjectIdentifier);

        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);

        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Notification> notifications = new PageInfo<>(notificationCenter.getAllSubjectNotifications(user.getUserId(), subject.getSubjectId()));
        model.addAttribute("notifications", notifications);

        model.addAttribute("title", "课题「" + subject.getSubjectName() + "」通知");
        model.addAttribute("url", "/notification/subject/" + subjectIdentifier);

        List<Subject> subjects = subjectService.getSubjectsOfUser(user.getUserId());
        model.addAttribute("subjects", subjects);

        int[] cnt = notificationCenter.getUnreadCounts(user.getUserId());
        model.addAttribute("cnt", cnt);

        return "/user/notification";
    }

    /**
     * 处理请求标记通知已读
     */
    @PostMapping("/notification/read")
    public String handleNotificationRead(Model model, HttpSession session,
                                         @RequestParam("type") String type,
                                         @RequestParam("notificationIds") List<Integer> notificationIds,
                                         @RequestParam(value = "op", defaultValue = "read") String op,
                                         @RequestParam(value = "query", defaultValue = "") String query,
                                         @RequestParam(value = "subjectIdentifier", defaultValue = "") String subjectIdentifier,
                                         @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        if (op.equals("read")) {
            if (!notificationCenter.setAsRead(notificationIds)) {
                logger.error("设置通知已读出错");
            }
            logger.info("设置通知已读成功");
        } else if (op.equals("delete")) {
            if (!notificationCenter.deleteNotifications(notificationIds)) {
                logger.error("删除通知出错");
            }
            logger.info("删除通知成功");
        }

        if (!subjectIdentifier.isEmpty())
            return "redirect:/notification/subject/" + subjectIdentifier + "?pageIndex=" + pageIndex;
        if (query == null || !query.isEmpty())
            return "redirect:/notification/";
        return "redirect:/notification/" + type + "?pageIndex=" + pageIndex;
    }

    /**
     * 处理请求标记所有通知已读
     */
    @RequestMapping("/notification/read/all")
    public String handleNotificationAllRead(Model model, HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        if (!notificationCenter.setAllAsRead(user.getUserId())) {
            logger.error("设置通知已读出错");
        }
        logger.info("设置通知已读成功");

        return "redirect:/notification";
    }

    /**
     * 返回未读通知数信息
     */
    @ResponseBody
    @RequestMapping("/notification/count")
    public Integer getUnreadNotificationCount(Model model, HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        int[] counts = notificationCenter.getUnreadCounts(user.getUserId());
        return counts[0];
    }
}
