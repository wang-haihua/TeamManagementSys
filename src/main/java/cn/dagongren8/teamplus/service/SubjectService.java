package cn.dagongren8.teamplus.service;

import cn.dagongren8.teamplus.entity.Subject;
import cn.dagongren8.teamplus.entity.SubjectAuthority;
import com.github.pagehelper.PageInfo;
import cn.dagongren8.teamplus.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Service
public interface SubjectService {
    List<Subject> getSubjectsOfUser(int userId);

    PageInfo<SubjectAuthority> getSubjectsOfUser(int userId, int pageIndex, int pageSize);

    List<Subject> getAllSubject();

    List<User> getSubjectAllUsers(int subjectId);

    Subject getSubjectById(int subjectId);

    Subject getSubjectByIdentification(String subjectIdentification);

    int insertSubject(Subject subject);

    int updateSubjectById(Subject subject);

    int deleteSubjectById(int subjectId);
}
