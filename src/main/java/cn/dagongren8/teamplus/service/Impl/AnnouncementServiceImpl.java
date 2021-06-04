package cn.dagongren8.teamplus.service.Impl;

import cn.dagongren8.teamplus.entity.Announcement;
import cn.dagongren8.teamplus.mapper.AnnouncementMapper;
import cn.dagongren8.teamplus.service.AnnouncementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;

    public AnnouncementServiceImpl(AnnouncementMapper announcementMapper) {
        this.announcementMapper = announcementMapper;
    }

    @Override
    public List<Announcement> getAllAnnouncementOfSubject(Integer subjectId) {
        return announcementMapper.getAllAnnouncementOfSubject(subjectId);
    }

    @Override
    public Integer insertAnnouncement(Announcement announcement) {
        return announcementMapper.insertAnnouncement(announcement);
    }

    @Override
    public Integer updateAnnouncement(Announcement announcement) {
        return announcementMapper.updateAnnouncement(announcement);
    }

    @Override
    public Integer deleteAnnouncementById(Integer announcementId) {
        return announcementMapper.deleteAnnouncementById(announcementId);
    }
}
