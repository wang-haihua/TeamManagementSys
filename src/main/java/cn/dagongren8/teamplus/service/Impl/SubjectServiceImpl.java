package cn.dagongren8.teamplus.service.Impl;

import cn.dagongren8.teamplus.entity.Subject;
import cn.dagongren8.teamplus.entity.SubjectAuthority;
import cn.dagongren8.teamplus.entity.User;
import cn.dagongren8.teamplus.mapper.SubjectMapper;
import cn.dagongren8.teamplus.service.SubjectAuthorityService;
import cn.dagongren8.teamplus.service.SubjectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectMapper subjectMapper;
    private final SubjectAuthorityService subjectAuthorityService;

    public SubjectServiceImpl(SubjectMapper subjectMapper,
                              SubjectAuthorityService subjectAuthorityService) {
        this.subjectMapper = subjectMapper;
        this.subjectAuthorityService = subjectAuthorityService;
    }

    @Override
    public List<Subject> getSubjectsOfUser(int userId) {
        List<SubjectAuthority> list = subjectAuthorityService.getAllSubjectAuthorityOfUser(userId);
        List<Subject> subjects = new ArrayList<>();
        for (SubjectAuthority sa : list) {
            subjects.add(sa.getSubject());
        }
        return subjects;
    }

    @Override
    public PageInfo<SubjectAuthority> getSubjectsOfUser(int userId, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<SubjectAuthority> list = subjectAuthorityService.getAllSubjectAuthorityOfUser(userId);
        PageInfo<SubjectAuthority> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<Subject> getAllSubject() {
        return subjectMapper.getAllSubject();
    }

    @Override
    public List<User> getSubjectAllUsers(int subjectId) {
        List<SubjectAuthority> list = subjectAuthorityService.getAllSubjectUsers(subjectId);
        List<User> subjectUsers = new ArrayList<>();
        for(SubjectAuthority sa : list){
            subjectUsers.add(sa.getUser());
        }
        return subjectUsers;
    }

    @Override
    public Subject getSubjectById(int subjectId) {
        return subjectMapper.getSubjectById(subjectId);
    }

    @Override
    public Subject getSubjectByIdentification(String subjectIdentification) {
        return subjectMapper.getSubjectByIdentification(subjectIdentification);
    }

    @Override
    public int insertSubject(Subject subject) {
        return subjectMapper.insertSubject(subject);
    }

    @Override
    public int updateSubjectById(Subject subject) {
        return subjectMapper.updateSubjectById(subject);
    }

    @Override
    public int deleteSubjectById(int subjectId) {
        return subjectMapper.deleteSubjectById(subjectId);
    }

}
