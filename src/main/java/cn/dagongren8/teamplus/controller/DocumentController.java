package cn.dagongren8.teamplus.controller;

import cn.dagongren8.teamplus.component.BaiduObjectStore;
import cn.dagongren8.teamplus.component.NotificationCenter;
import cn.dagongren8.teamplus.entity.*;
import cn.dagongren8.teamplus.service.DirectoryService;
import cn.dagongren8.teamplus.service.DocumentService;
import cn.dagongren8.teamplus.service.SubjectAuthorityService;
import cn.dagongren8.teamplus.service.SubjectService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;
import java.util.Objects;

/**
 * 处理文档管理相关请求
 */
@Controller
public class DocumentController {

    private final SubjectService subjectService;
    private final SubjectAuthorityService subjectAuthorityService;
    private final BaiduObjectStore baiduObjectStore;
    private final DirectoryService directoryService;
    private final DocumentService documentService;
    private final NotificationCenter notificationCenter;
    Logger logger = LoggerFactory.getLogger(getClass());

    public DocumentController(SubjectService subjectService, SubjectAuthorityService subjectAuthorityService, BaiduObjectStore baiduObjectStore, DirectoryService directoryService, DocumentService documentService, NotificationCenter notificationCenter) {
        this.subjectService = subjectService;
        this.subjectAuthorityService = subjectAuthorityService;
        this.baiduObjectStore = baiduObjectStore;
        this.directoryService = directoryService;
        this.documentService = documentService;
        this.notificationCenter = notificationCenter;
    }


    /**
     * 跳转到文档浏览页面
     */
    @GetMapping("/s/{subject_identifier}/files")
    public String subjectFilePageRedirect(@PathVariable("subject_identifier") String subjectIdentifier) {
        return "redirect:/s/" + subjectIdentifier + "/files/all";
    }

    /**
     * 跳转到文档浏览页面
     */
    @GetMapping("/s/{subject_identifier}/files/all")
    public String subjectFilePageRedirect2(@PathVariable("subject_identifier") String subjectIdentifier) {
        return "redirect:/s/" + subjectIdentifier + "/files/all/0";
    }

    /**
     * 文档浏览页面
     */
    @GetMapping("/s/{subject_identifier}/files/all/{directoryId}")
    public String subjectFilePage(Model model, HttpSession session,
                                  @PathVariable("subject_identifier") String subjectIdentifier,
                                  @PathVariable("directoryId") Integer directoryId) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        model.addAttribute("navId", directoryId);

        SubjectAuthority subjectAuthority = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), user.getUserId());
        if (subjectAuthority == null) {
            return "redirect:/workbench";
        }

        // 如果没有创建过根目录，则创建该课题的根目录
        if (subject.getDirectory() == null) {
            logger.info("课题目录为null，正在创建新根目录...");

            Directory directory = new Directory();
            directory.setDirectoryParentId(0);
            if (directoryService.insertDirectory(directory) == 0) {
                logger.error("创建课题根目录失败：" + directory);
                return "/error/500";
            }

            subject.setDirectory(directory);
            if (subjectService.updateSubjectById(subject) == 0) {
                logger.error("创建课题根目录失败：更新课题信息失败，" + subject);
                return "/error/500";
            }

            logger.info("课题根目录创建成功：" + directory);
        }

        Directory directory;
        if (directoryId == 0) { // 未指明文件路径，表明访问根目录
            directory = subject.getDirectory();
        } else {
            directory = directoryService.getDirectoryById(directoryId);
        }

        // 获取完整路径
        List<Directory> path = directoryService.getAbsolutePath(directory.getDirectoryId());

        // 判断传入的文件夹ID是否对应该课题
        if (path.isEmpty() || !Objects.equals(path.get(0).getDirectoryId(), subject.getDirectory().getDirectoryId())) {
            // 传入的参数表示的文件夹不是该文件目录的，显示根目录
            return "redirect:/s/" + subjectIdentifier + "/files/all/0";
        }

        List<Directory> directories = directoryService.getAllChildDirectories(directory.getDirectoryId());
        List<Document> documents = documentService.getAllDocuments(directory.getDirectoryId());
        model.addAttribute("path", path);
        model.addAttribute("directories", directories);
        model.addAttribute("documents", documents);
        model.addAttribute("currentDirectory", directory);

        return "/subject/subject_files";
    }

    /**
     * 处理文件夹添加请求
     */
    @PostMapping("/s/{subject_identifier}/files/addDirectory")
    public String handleDirectoryAdding(Model model, HttpSession session,
                                        @PathVariable("subject_identifier") String subjectIdentifier,
                                        @RequestParam("directoryId") Integer directoryId,
                                        @RequestParam("directoryName") String directoryName) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        SubjectAuthority subjectAuthority = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), user.getUserId());
        if (subjectAuthority == null) {
            return "redirect:/subjects";
        }

        Directory directory = new Directory();
        directory.setDirectoryName(directoryName);
        directory.setDirectoryParentId(directoryId);
        directory.setDirectoryCreator(user);
        if (directoryService.insertDirectory(directory) == 0) {
            logger.error("新建文件夹失败：" + directory);
            return "redirect:/s/" + subjectIdentifier + "/files/all/" + directoryId;
        }

        notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "在课题「" + subject.getSubjectName() + "」中创建了文件夹「" + directory.getDirectoryName() + "」");

        logger.info("新建文件夹成功：" + directory);
        return "redirect:/s/" + subjectIdentifier + "/files/all/" + directoryId;
    }

    /**
     * 处理文件上传请求
     */
    @PostMapping("/s/{subject_identifier}/files/upload")
    public String handleDocumentUpload(Model model, HttpSession session,
                                       @PathVariable("subject_identifier") String subjectIdentifier,
                                       @RequestParam("directoryId") Integer directoryId,
                                       @RequestParam("file") MultipartFile file) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        SubjectAuthority subjectAuthority = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), user.getUserId());
        if (subjectAuthority == null) {
            return "redirect:/subjects";
        }

        String key = baiduObjectStore.generateRandomKey();
        String url = null;
        try {
            url = baiduObjectStore.putBytesToBCE(file.getBytes(), key);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (url == null) {
            logger.error("上传文件失败：BCE上传失败");
            return "redirect:/s/" + subjectIdentifier + "/files/all/" + directoryId;
        }

        Document document = new Document();
        Directory directory = new Directory();
        directory.setDirectoryId(directoryId);
        document.setDirectory(directory);
        document.setDocumentCreator(user);
        document.setDocumentKey(key);
        document.setDocumentName(file.getOriginalFilename());

        if (documentService.insertDocument(document) == 0) {
            logger.error("上传文件失败：数据库错误" + document);
            return "redirect:/s/" + subjectIdentifier + "/files/all/" + directoryId;
        }

        notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "在课题「" + subject.getSubjectName() + "」中上传了文件「" + document.getDocumentName() + "」");

        logger.info("上传文件成功：" + document);
        return "redirect:/s/" + subjectIdentifier + "/files/all/" + directoryId;
    }

    /**
     * 处理文件下载请求
     */
    @GetMapping("/s/{subject_identifier}/files/download/{documentId}")
    public String handleDocumentDownload(Model model, HttpSession session, HttpServletResponse response,
                                         @PathVariable("subject_identifier") String subjectIdentifier,
                                         @PathVariable("documentId") Integer documentId) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        SubjectAuthority subjectAuthority = subjectAuthorityService.getSubjectAuthority(subject.getSubjectId(), user.getUserId());
        if (subjectAuthority == null) {
            return "redirect:/subjects";
        }

        Document document = documentService.getDocumentById(documentId);
        if (document == null) {
            logger.info("文件下载失败，文件不存在");
            return null;
        }

        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(document.getDocumentName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("文件下载失败，文件名编码错误", e);
        }

        byte[] buffer = new byte[1024];
        InputStream fis = baiduObjectStore.getFileFromBCE(document.getDocumentKey()); //文件输入流
        BufferedInputStream bis = null;
        OutputStream os = null; //输出流
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(fis);
            IOUtils.copy(bis, os);
        } catch (Exception e) {
            logger.error("文件下载失败", e);
        }
        try {
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
            logger.error("BCE流关闭失败", e);
        }

        notificationCenter.publishSubjectNotification(user.getUserId(), subject.getSubjectId(), user.getUserName() + "在课题「" + subject.getSubjectName() + "」中下载了文件「" + document.getDocumentName() + "」");

        return null;
    }

    @PostMapping("/s/{subject_identifier}/files/delete")
    public String handleFileDelete(Model model, HttpSession session,
                                   @PathVariable("subject_identifier") String subjectIdentifier,
                                   @RequestParam("type") String type,
                                   @RequestParam("idToDelete") Integer id,
                                   @RequestParam("navId") Integer navId) {
        Team team = (Team) session.getAttribute("loginTeam");
        User user = (User) session.getAttribute("loginUser");
        model.addAttribute("team", team);
        model.addAttribute("user", user);
        Subject subject = subjectService.getSubjectByIdentification(subjectIdentifier);
        model.addAttribute("subject", subject);

        switch (type) {
            case "document":
                documentService.deleteDocumentById(id);
                break;
            case "directory":
                directoryService.deleteTeamById(id);
                break;
        }

        return "redirect:/s/" + subjectIdentifier + "/files/all/" + navId;
    }
}
