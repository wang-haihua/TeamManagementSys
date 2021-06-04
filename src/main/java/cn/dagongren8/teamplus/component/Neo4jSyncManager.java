package cn.dagongren8.teamplus.component;

import cn.dagongren8.teamplus.entity.Subject;
import cn.dagongren8.teamplus.entity.SubjectAuthority;
import cn.dagongren8.teamplus.entity.Team;
import cn.dagongren8.teamplus.entity.User;
import cn.dagongren8.teamplus.service.*;
import cn.dagongren8.teamplus.util.Commons;
import org.neo4j.driver.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

@Component
public class Neo4jSyncManager {

    private final UserService userService;
    private final SubjectService subjectService;
    private final SubjectAuthorityService subjectAuthorityService;
    private final TeamService teamService;
    private final Neo4jProperties neo4jProperties;
    Logger logger = LoggerFactory.getLogger(getClass());

    private Driver driver;
    private HashMap<Integer, Integer> subjectTeamRelationCached;

    public Neo4jSyncManager(UserService userService, SubjectService subjectService, SubjectAuthorityService subjectAuthorityService, TeamService teamService, TaskService taskService, AnnouncementService announcementService, DirectoryService directoryService, DocumentService documentService, Neo4jProperties neo4jProperties) {
        this.userService = userService;
        this.subjectService = subjectService;
        this.subjectAuthorityService = subjectAuthorityService;
        this.teamService = teamService;
        this.neo4jProperties = neo4jProperties;

        this.subjectTeamRelationCached = new HashMap<>();
    }

    /**
     * 建立连接
     */
    public void initConnection() {
        this.driver = GraphDatabase.driver(neo4jProperties.getBolt(), AuthTokens.basic(neo4jProperties.getUsername(), neo4jProperties.getPassword()));
    }

    /**
     * 关闭连接
     */
    public void close() {
        driver.close();
    }

    /**
     * 添加课题组结点
     */
    public void addTeam(Team team) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> tx.run("MERGE (a:Team {teamId: $teamId, teamName: $teamName, teamIdentifier: $teamIdentifier, teamAvatar: $teamAvatar, teamDescription: $teamDescription, teamCreatedTime: $teamCreatedTime})",
                    parameters("teamId", team.getTeamId(), "teamName", team.getTeamName(), "teamIdentifier", team.getTeamIdentification(), "teamAvatar", Commons.nullAsEmptyString(team.getTeamAvatar()), "teamDescription", Commons.nullAsEmptyString(team.getTeamDescription()), "teamCreatedTime", Commons.formatDate("yyyy-MM-dd", team.getTeamCreatetime()))));
        }
    }

    /**
     * 添加课题组结点
     */
    public void addTeam(List<Team> teams) {
        try (Session session = driver.session()) {
            for (Team team : teams) {
                session.run("MERGE (a:Team {teamId: $teamId, teamName: $teamName, teamIdentifier: $teamIdentifier, teamAvatar: $teamAvatar, teamDescription: $teamDescription, teamCreatedTime: $teamCreatedTime})",
                        parameters("teamId", team.getTeamId(), "teamName", team.getTeamName(), "teamIdentifier", team.getTeamIdentification(), "teamAvatar", Commons.nullAsEmptyString(team.getTeamAvatar()), "teamDescription", Commons.nullAsEmptyString(team.getTeamDescription()), "teamCreatedTime", Commons.formatDate("yyyy-MM-dd", team.getTeamCreatetime())));
            }
        }
    }

    /**
     * 添加课题结点
     */
    public void addSubject(Subject subject) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> tx.run("MERGE (a:Subject {subjectId: $subjectId, subjectName: $subjectName, subjectIdentifier: $subjectIdentifier, subjectAvatar: $subjectAvatar, subjectDescription: $subjectDescription, subjectDescription: $subjectDescription, subjectCreatedTime: $subjectCreatedTime})",
                    parameters("subjectId", subject.getSubjectId(), "subjectName", subject.getSubjectName(), "subjectIdentifier", subject.getSubjectIdentification(), "subjectAvatar", Commons.nullAsEmptyString(subject.getSubjectAvatar()), "subjectDescription", Commons.nullAsEmptyString(subject.getSubjectDescription()), "subjectCreatedTime", Commons.formatDate("yyyy-MM-dd", subject.getSubjectCreatetime()))));
        }
    }

    /**
     * 添加课题结点
     */
    public void addSubject(List<Subject> subjects) {
        try (Session session = driver.session()) {
            for (Subject subject : subjects) {
                session.run("MERGE (a:Subject {subjectId: $subjectId, subjectName: $subjectName, subjectIdentifier: $subjectIdentifier, subjectAvatar: $subjectAvatar, subjectDescription: $subjectDescription, subjectDescription: $subjectDescription, subjectCreatedTime: $subjectCreatedTime})",
                        parameters("subjectId", subject.getSubjectId(), "subjectName", subject.getSubjectName(), "subjectIdentifier", subject.getSubjectIdentification(), "subjectAvatar", Commons.nullAsEmptyString(subject.getSubjectAvatar()), "subjectDescription", Commons.nullAsEmptyString(subject.getSubjectDescription()), "subjectCreatedTime", Commons.formatDate("yyyy-MM-dd", subject.getSubjectCreatetime())));

            }
        }
    }

    /**
     * 添加用户结点以及用户所属的课题组关系
     */
    public void addUser(User user) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> tx.run("MERGE (a:User {userId: $userId, userName: $userName, userAvatar: $userAvatar, userEmail: $userEmail, userCreatedTime: $userCreatedTime})",
                    parameters("userId", user.getUserId(), "userName", user.getUserName(), "userAvatar", Commons.nullAsEmptyString(user.getUserAvatar()), "userEmail", Commons.nullAsEmptyString(user.getUserEmail()), "userCreatedTime", Commons.formatDate("yyyy-MM-dd", user.getUserCreatetime()))));
            session.run("MATCH (a:User {userId: $userId}) " +
                    "MATCH (b:Team {teamId: $teamId}) " +
                    "MERGE (a)-[:TEAM_MEMBER_OF {teamAuthority: $teamAuthority}]->(b)", parameters("userId", user.getUserId(), "teamId", user.getTeam().getTeamId(), "teamAuthority", user.getTeamAuthority()));
        }
    }

    /**
     * 添加用户结点以及用户所属的课题组关系
     */
    public void addUser(List<User> users) {
        try (Session session = driver.session()) {
            for (User user : users) {
                session.run("MERGE (a:User {userId: $userId, userName: $userName, userAvatar: $userAvatar, userEmail: $userEmail, teamAuthority: $teamAuthority, userCreatedTime: $userCreatedTime})",
                        parameters("userId", user.getUserId(), "userName", user.getUserName(), "userAvatar", Commons.nullAsEmptyString(user.getUserAvatar()), "userEmail", Commons.nullAsEmptyString(user.getUserEmail()), "teamAuthority", user.getTeamAuthority(), "userCreatedTime", Commons.formatDate("yyyy-MM-dd", user.getUserCreatetime())));
                session.run("MATCH (a:User {userId: $userId}) " +
                        "MATCH (b:Team {teamId: $teamId}) " +
                        "MERGE (a)-[:TEAM_MEMBER_OF {teamAuthority: $teamAuthority}]->(b)", parameters("userId", user.getUserId(), "teamId", user.getTeam().getTeamId(), "teamAuthority", user.getTeamAuthority()));
            }
        }
    }

    /**
     * 添加课题成员关系
     */
    public void addSubjectRelation(List<SubjectAuthority> subjectAuthorities) {
        try (Session session = driver.session()) {
            for (SubjectAuthority sa : subjectAuthorities) {
                session.run("MATCH (a:User {userId: $userId}) " +
                        "MATCH (b:Subject {subjectId: $subjectId}) " +
                        "MERGE (a)-[:SUBJECT_MEMBER_OF {subjectAuthority: $subjectAuthority}]->(b)", parameters("userId", sa.getUser().getUserId(), "subjectId", sa.getSubject().getSubjectId(), "subjectAuthority", sa.getSubjectAuthorityType()));

                if (!subjectTeamRelationCached.containsKey(sa.getSubject().getSubjectId())) {
                    session.run("MATCH (a:Subject {subjectId: $subjectId}) " +
                            "MATCH (b:Team {teamId: $teamId}) " +
                            "MERGE (a)-[:SUBJECT_OF]->(b)", parameters("subjectId", sa.getSubject().getSubjectId(), "teamId", sa.getUser().getTeamId()));
                    subjectTeamRelationCached.put(sa.getSubject().getSubjectId(), sa.getUser().getTeamId());
                }
            }
        }
    }

    /**
     * 创建一个记录更新时间的结点
     */
    public void addUpdateTimeNode() {
        try (Session session = driver.session()) {
            session.run("MERGE (a:UpdateTime {id: 1, time: $time})", parameters("time", Commons.formatDate("yyyy-MM-dd HH:mm:ss", new Date())));
        }
    }

    /**
     * 获取更新日期字符串
     *
     * @return 更新日期字符串
     */
    public String getUpdateTime() {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (a:UpdateTime {id: 1}) RETURN a.time AS time");
            if (result.hasNext()) {
                Record record = result.next();
                return record.get("time").asString();
            }
        }
        return "";
    }

    /**
     * 清空数据库
     */
    public void clearDatabase() {
        subjectTeamRelationCached.clear();
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> tx.run("MATCH (n) DETACH DELETE n"));
        }
    }

    /**
     * 重建数据库
     */
    public void reconstructDatabase() {

        logger.info("开始同步neo4j数据库...");

        long startTime = System.currentTimeMillis();

        initConnection(); // 建立连接

        clearDatabase(); // 清空数据库

        // 创建结点以及关系
        addTeam(teamService.getAllTeams());
        addSubject(subjectService.getAllSubject());
        addUser(userService.getAllUsers());
        addSubjectRelation(subjectAuthorityService.getAllSubjectAuthorities());

        addUpdateTimeNode();

        close(); // 释放连接

        double duration = 1. * (System.currentTimeMillis() - startTime) / 1000;

        logger.info(String.format("同步neo4j数据库完成，用时：%.2fs", duration));
    }

    public Neo4jProperties getProperties() {
        return neo4jProperties;
    }

    @Component
    @ConfigurationProperties(prefix = "teamplus.neo4j")
    public static class Neo4jProperties {
        private String bolt;
        private String http;
        private String database;
        private String username;
        private String password;

        public String getBolt() {
            return bolt;
        }

        public void setBolt(String bolt) {
            this.bolt = bolt;
        }

        public String getHttp() {
            return http;
        }

        public void setHttp(String http) {
            this.http = http;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
