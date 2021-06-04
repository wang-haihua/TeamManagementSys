package cn.dagongren8.teamplus.controller;

import cn.dagongren8.teamplus.component.Neo4jSyncManager;
import cn.dagongren8.teamplus.component.NotificationCenter;
import cn.dagongren8.teamplus.entity.Team;
import cn.dagongren8.teamplus.entity.User;
import cn.dagongren8.teamplus.util.Commons;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 处理知识图谱相关请求
 */
@Controller
public class KnowledgeGraphController {

    private final Neo4jSyncManager neo4jSyncManager;
    private final NotificationCenter notificationCenter;

    public KnowledgeGraphController(Neo4jSyncManager neo4jSyncManager, NotificationCenter notificationCenter) {
        this.neo4jSyncManager = neo4jSyncManager;
        this.notificationCenter = notificationCenter;
    }

    /**
     * 知识图谱页面
     */
    @GetMapping("/graph")
    public String knowledgeGraphPage(Model model, HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        neo4jSyncManager.initConnection();
        String updateTime = neo4jSyncManager.getUpdateTime();
        neo4jSyncManager.close();
        Neo4jSyncManager.Neo4jProperties neo4jProperties = neo4jSyncManager.getProperties();

        model.addAttribute("updateTime", updateTime);

        model.addAttribute("server", neo4jProperties.getHttp());
        model.addAttribute("database", neo4jProperties.getDatabase());
        model.addAttribute("authorization", "Basic " + Commons.base64Encoding(neo4jProperties.getUsername() + ":" + neo4jProperties.getPassword()));

        return "/user/knowledge_graph";
    }

    /**
     * 重建数据库请求，需要大约10s响应
     */
    @ResponseBody
    @RequestMapping("/graph/sync")
    public String updateNeo4jDatabase(Model model, HttpSession session) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);

        neo4jSyncManager.reconstructDatabase();
        notificationCenter.publishTeamNotification(user.getUserId(), "用户「" + user.getUserName() + "」重置了图数据库");
        return "OK";
    }
}
