package cn.dagongren8.teamplus.controller;

import cn.dagongren8.teamplus.component.NotificationCenter;
import cn.dagongren8.teamplus.entity.*;
import cn.dagongren8.teamplus.service.SubjectAuthorityService;
import cn.dagongren8.teamplus.service.SubjectService;
import cn.dagongren8.teamplus.service.TaskService;
import cn.dagongren8.teamplus.service.UserService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 课题任务管理
 */
@Controller
public class TaskController {

    private final UserService userService;
    private final SubjectService subjectService;
    private final SubjectAuthorityService subjectAuthorityService;
    private final TaskService taskService;
    private final NotificationCenter notificationCenter;
    Logger logger = LoggerFactory.getLogger(getClass());

    public TaskController(UserService userService, SubjectService subjectService, SubjectAuthorityService subjectAuthorityService, TaskService taskService, NotificationCenter notificationCenter) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.subjectAuthorityService = subjectAuthorityService;
        this.taskService = taskService;
        this.notificationCenter = notificationCenter;
    }

    /**
     * 处理课题任务删除请求
     */
    @PostMapping("/s/{subject_identifier}/tasks/delete")
    public String handleTaskDelete(Model model, HttpSession session,
                                   @PathVariable("subject_identifier") String subjectIdentifier,
                                   @RequestParam("taskId") Integer taskId) {

        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        Task task = taskService.getTaskById(taskId);

        SubjectAuthority subjectAuthority = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), user.getUserId());

        if (subjectAuthority == null
                || (subjectAuthority.getSubjectAuthorityType() != User.CREATOR
                && subjectAuthority.getSubjectAuthorityType() != User.MANAGER)) {
            // 用户不属于该 或 用户权限不足
            logger.info("删除任务失败，用户权限不足：" + user);
            return "redirect:/s/" + subject.getSubjectIdentification() + "/tasks";
        }

        if (taskService.deleteTaskById(taskId) == 0) {
            logger.error("删除任务失败：taskId=" + taskId);
            return "redirect:/s/" + subject.getSubjectIdentification() + "/tasks";
        }

        notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "删除了课题「" + subject.getSubjectName() + "」中的任务「" + task.getTaskTitle() + "」");

        logger.info("删除任务成功：taskId=" + taskId);

        return "redirect:/s/" + subject.getSubjectIdentification() + "/tasks";
    }


    /**
     * 课题任务页面
     */
    @GetMapping("/s/{subject_identifier}/tasks")
    public String subjectTasksPage(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                   @RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize,
                                   Model model, HttpSession session,
                                   @PathVariable("subject_identifier") String subjectIdentifier) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        PageInfo<Task> tasks = taskService.getSubjectTasks(subject.getSubjectId(), pageIndex, pageSize);
        model.addAttribute("tasks", tasks);

        return "/subject/subject_tasks";
    }

    /**
     * 课题任务添加页面
     */
    @GetMapping("/s/{subject_identifier}/tasks/add")
    public String subjectTaskAddPage(Model model, HttpSession session,
                                     @PathVariable("subject_identifier") String subjectIdentifier) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);
        List<User> subjectUsers = subjectService.getSubjectAllUsers(subject.getSubjectId());
        model.addAttribute("subjectUsers", subjectUsers);

        return "/subject/subject_task_add";
    }

    /**
     * 处理课题任务添加请求
     */
    @PostMapping("/s/{subject_identifier}/tasks/add")
    public String handleSubjectTaskAdd(Model model, HttpSession session,
                                       @PathVariable("subject_identifier") String subjectIdentifier,
                                       @RequestParam("taskTitle") String taskTitle,
                                       @RequestParam("taskContent") String taskContent,
                                       @RequestParam("processorId") Integer processorId,
                                       @RequestParam("taskRank") Integer taskRank,
                                       @RequestParam("taskStarttime") Date taskStarttime,
                                       @RequestParam("taskDeadline") Date taskDeadline,
                                       @RequestParam("taskDuration") Integer taskDuration) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);
        List<User> subjectUsers = subjectService.getSubjectAllUsers(subject.getSubjectId());
        model.addAttribute("subjectUsers", subjectUsers);

        model.addAttribute("inputTaskTitle", taskTitle);
        model.addAttribute("inputTaskContent", taskContent);
        model.addAttribute("inputProcessorId", processorId);
        model.addAttribute("inputTaskRank", taskRank);
        model.addAttribute("inputTaskStarttime", taskStarttime);
        model.addAttribute("inputTaskDeadline", taskDeadline);
        model.addAttribute("inputTaskDuration", taskDuration);

        if (taskTitle.isEmpty()) {
            logger.info("添加任务失败：任务标题为空");
            model.addAttribute("error_info", "任务标题不能为空！");
            return "/subject/subject_task_add";
        }

        Task task = new Task();
        task.setTaskTitle(taskTitle);
        task.setTaskContent(taskContent);
        task.setProcessor(userService.getUserById(processorId));
        task.setTaskRank(taskRank);
        task.setTaskStarttime(taskStarttime);
        task.setTaskDeadline(taskDeadline);
        task.setTaskDuration(taskDuration);
        task.setTaskCreator(user);
        task.setSubject(subject);
        task.setTaskStatus(0);

        if (taskService.insertTask(task) == 0) {
            logger.error("创建任务时发生错误：" + task);
            model.addAttribute("error_info", "创建任务失败，请联系管理员");
            return "/subject/subject_task_add";
        }

        notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "创建了新任务「" + task.getTaskTitle() + "」");
        logger.info("创建任务成功：" + task);

        return "redirect:/s/" + subject.getSubjectIdentification() + "/tasks";
    }

    /**
     * 课题任务修改页面
     */
    @GetMapping("/s/{subject_identifier}/task/{task_id}")
    public String subjectTaskEditPage(Model model, HttpSession session,
                                      @PathVariable("subject_identifier") String subjectIdentifier,
                                      @PathVariable("task_id") Integer taskId) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);
        Task task = taskService.getTaskById(taskId);
        model.addAttribute("task", task);
        List<User> subjectUsers = subjectService.getSubjectAllUsers(subject.getSubjectId());
        model.addAttribute("subjectUsers", subjectUsers);


        return "/subject/subject_task_edit";
    }

    /**
     * 处理修改任务请求
     */
    @PostMapping("/s/{subject_identifier}/task/{task_id}")
    public String handleSubjectTaskEditUpdate(Model model, HttpSession session,
                                              @PathVariable("subject_identifier") String subjectIdentifier,
                                              @PathVariable("task_id") Integer taskId,
                                              @RequestParam("taskTitle") String taskTitle,
                                              @RequestParam("taskContent") String taskContent,
                                              @RequestParam("processorId") Integer processorId,
                                              @RequestParam("taskRank") Integer taskRank,
                                              @RequestParam("taskStarttime") Date taskStarttime,
                                              @RequestParam("taskDeadline") Date taskDeadline,
                                              @RequestParam("taskDuration") Integer taskDuration) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);
        List<User> subjectUsers = subjectService.getSubjectAllUsers(subject.getSubjectId());
        model.addAttribute("subjectUsers", subjectUsers);
        Task task = taskService.getTaskById(taskId);
        model.addAttribute("task", task);

        model.addAttribute("inputTaskTitle", taskTitle);
        model.addAttribute("inputTaskContent", taskContent);
        model.addAttribute("inputProcessorId", processorId);
        model.addAttribute("inputTaskRank", taskRank);
        model.addAttribute("inputTaskStarttime", taskStarttime);
        model.addAttribute("inputTaskDeadline", taskDeadline);
        model.addAttribute("inputTaskDuration", taskDuration);

        if (taskTitle.isEmpty()) {
            logger.info("修改任务失败：任务标题为空");
            model.addAttribute("error_info", "任务标题不能为空！");
            return "/subject/subject_task_edit";
        }

        task.setTaskTitle(taskTitle);
        task.setTaskContent(taskContent);
        if (processorId != 0) {
            User u = new User();
            u.setUserId(processorId);
            task.setProcessor(u);
        }
        task.setTaskRank(taskRank);
        task.setTaskStarttime(taskStarttime);
        task.setTaskDeadline(taskDeadline);
        task.setTaskDuration(taskDuration);
        task.setTaskCreator(user);
        task.setSubject(subject);
        task.setTaskStatus(0);

        if (taskService.updateTaskById(task) == 0) {
            logger.error("修改任务时发生错误：" + task);
            model.addAttribute("error_info", "修改任务失败，请联系管理员");
            return "/subject/subject_task_edit";
        }

        notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "修改了任务「" + task.getTaskTitle() + "」");
        logger.info("修改任务成功：" + task);

        return "redirect:/s/" + subject.getSubjectIdentification() + "/tasks";
    }
}
