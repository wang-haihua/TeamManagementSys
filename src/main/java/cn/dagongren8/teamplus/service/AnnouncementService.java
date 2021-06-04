package cn.dagongren8.teamplus.service;

import cn.dagongren8.teamplus.entity.Announcement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnnouncementService {
    List<Announcement> getAllAnnouncementOfSubject(Integer subjectId);

    Integer insertAnnouncement(Announcement announcement);

    Integer updateAnnouncement(Announcement announcement);

    Integer deleteAnnouncementById(Integer announcementId);
}
