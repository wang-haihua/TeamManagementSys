package cn.dagongren8.teamplus.service.Impl;

import cn.dagongren8.teamplus.entity.SubjectAuthority;
import cn.dagongren8.teamplus.mapper.SubjectAuthorityMapper;
import cn.dagongren8.teamplus.service.SubjectAuthorityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

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
public class SubjectAuthorityServiceImpl implements SubjectAuthorityService {

    private final SubjectAuthorityMapper subjectAuthorityMapper;

    public SubjectAuthorityServiceImpl(SubjectAuthorityMapper subjectAuthorityMapper) {
        this.subjectAuthorityMapper = subjectAuthorityMapper;
    }

    @Override
    public List<SubjectAuthority> getAllSubjectAuthorities() {
        return subjectAuthorityMapper.getAllSubjectAuthorities();
    }

    @Override
    public List<SubjectAuthority> getAllSubjectAuthorityOfUser(int userId) {
        return subjectAuthorityMapper.getAllSubjectAuthorityOfUser(userId);
    }

    @Override
    public List<SubjectAuthority> getAllSubjectUsers(int subjectId) {
        return subjectAuthorityMapper.getAllSubjectUsers(subjectId);
    }

    @Override
    public PageInfo<SubjectAuthority> getAllSubjectUsers(int subjectId, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<SubjectAuthority> users = subjectAuthorityMapper.getAllSubjectUsers(subjectId);
        return new PageInfo<>(users);
    }

    @Override
    public List<SubjectAuthority> getSubjectUsersByAuthorityType(int subjectId, int subjectAuthorityType) {
        return subjectAuthorityMapper.getSubjectUsersByAuthorityType(subjectId, subjectAuthorityType);
    }

    @Override
    public SubjectAuthority getSubjectAuthority(int subjectId, int userId) {
        return subjectAuthorityMapper.getSubjectAuthority(subjectId, userId);
    }

    @Override
    public int insertSubjectAuthority(SubjectAuthority subjectAuthority) {
        return subjectAuthorityMapper.insertSubjectAuthority(subjectAuthority);
    }

    @Override
    public int updateSubjectAuthority(SubjectAuthority subjectAuthority) {
        return subjectAuthorityMapper.updateSubjectAuthority(subjectAuthority);
    }

    @Override
    public int deleteSubjectAuthority(int subjectId, int userId) {
        return subjectAuthorityMapper.deleteSubjectAuthority(subjectId, userId);
    }
}
