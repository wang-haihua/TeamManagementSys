package cn.dagongren8.teamplus.service;

import cn.dagongren8.teamplus.entity.SubjectAuthority;
import com.github.pagehelper.PageInfo;
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
public interface SubjectAuthorityService {
    List<SubjectAuthority> getAllSubjectAuthorities();

    List<SubjectAuthority> getAllSubjectAuthorityOfUser(int userId);

    List<SubjectAuthority> getAllSubjectUsers(int subjectId);

    PageInfo<SubjectAuthority> getAllSubjectUsers(int subjectId, int pageIndex, int pageSize);

    List<SubjectAuthority> getSubjectUsersByAuthorityType(int subjectId, int subjectAuthorityType);

    SubjectAuthority getSubjectAuthority(int subjectId, int userId);

    int insertSubjectAuthority(SubjectAuthority subjectAuthority);

    int updateSubjectAuthority(SubjectAuthority subjectAuthority);

    int deleteSubjectAuthority(int subjectId, int userId);
}
