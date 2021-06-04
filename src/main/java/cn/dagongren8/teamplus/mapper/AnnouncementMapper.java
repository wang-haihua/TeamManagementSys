package cn.dagongren8.teamplus.mapper;

import cn.dagongren8.teamplus.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnnouncementMapper {
    List<Announcement> getAllAnnouncementOfSubject(Integer subjectId);

    Integer insertAnnouncement(Announcement announcement);

    Integer updateAnnouncement(Announcement announcement);

    Integer deleteAnnouncementById(Integer announcementId);
}
