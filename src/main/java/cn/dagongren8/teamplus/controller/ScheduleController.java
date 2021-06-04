package cn.dagongren8.teamplus.controller;

import cn.dagongren8.teamplus.entity.Event;
import cn.dagongren8.teamplus.entity.Task;
import cn.dagongren8.teamplus.entity.Team;
import cn.dagongren8.teamplus.entity.User;
import cn.dagongren8.teamplus.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * 处理日程相关请求
 */
@Controller
public class ScheduleController {

    private final TaskService taskService;
    Logger logger = LoggerFactory.getLogger(getClass());

    public ScheduleController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 日程页面
     */
    @GetMapping("/schedule")
    public String schedulePage(Model model, HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        return "/user/schedule";
    }

    /**
     * 获取所有用户的日程
     */
    @ResponseBody
    @RequestMapping("/user/events")
    public List<Event> getUserEvents(Model model, HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        List<Task> userTasks = taskService.getUserTasks(user.getUserId());
        LinkedList<Event> res = new LinkedList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Task t : userTasks) {

            Event event = new Event();
            event.setTitle(t.getTaskTitle());
            if (t.getTaskStarttime() != null) {
                event.setStart(sdf.format(t.getTaskStarttime()));
            } else {
                event.setStart(sdf.format(t.getTaskCreatetime()));
            }
            if (t.getTaskDeadline() != null) {
                event.setEnd(sdf.format(t.getTaskDeadline()));
            }
            res.add(event);
        }

        return res;
    }
}
