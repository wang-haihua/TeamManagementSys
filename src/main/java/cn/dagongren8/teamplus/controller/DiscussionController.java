package cn.dagongren8.teamplus.controller;

import cn.dagongren8.teamplus.component.NotificationCenter;
import cn.dagongren8.teamplus.entity.Message;
import cn.dagongren8.teamplus.entity.Subject;
import cn.dagongren8.teamplus.entity.Team;
import cn.dagongren8.teamplus.entity.User;
import cn.dagongren8.teamplus.service.MessageService;
import cn.dagongren8.teamplus.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 课题讨论功能模块
 */
@Controller
public class DiscussionController {

    private final SubjectService subjectService;
    private final MessageService messageService;
    private final NotificationCenter notificationCenter;
    Logger logger = LoggerFactory.getLogger(getClass());

    public DiscussionController(SubjectService subjectService, MessageService messageService, NotificationCenter notificationCenter) {
        this.subjectService = subjectService;
        this.messageService = messageService;
        this.notificationCenter = notificationCenter;
    }

    /**
     * 课题讨论页面
     */
    @GetMapping("/s/{subject_identifier}/discussion")
    public String subjectDiscussionPage(Model model, HttpSession session,
                                        @PathVariable("subject_identifier") String subjectIdentifier) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        List<Message> messages = messageService.getAllSubjectMessages(subject.getSubjectId());
        model.addAttribute("messages", messages);

        return "/subject/subject_discussion";
    }

    /**
     * 处理课题讨论信息发布请求
     */
    @PostMapping("/s/{subject_identifier}/discussion/add")
    public String handleMessagePublish(Model model, HttpSession session,
                                       @PathVariable("subject_identifier") String subjectIdentifier,
                                       @RequestParam("content") String content) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        Message message = new Message();
        message.setSubject(subject);
        message.setUser(user);
        message.setMessageContent(content);

        if (messageService.insertMessage(message) == 0) {
            logger.error("发布失败：插入失败" + message);
            return "redirect:/s/" + subject.getSubjectIdentification() + "/discussion";
        }

        notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "在课题「" + subject.getSubjectName() + "」中发布了新讨论：" + message.getMessageContent());

        logger.info("讨论发布成功：" + message);

        return "redirect:/s/" + subject.getSubjectIdentification() + "/discussion";
    }

}
