package cn.dagongren8.teamplus.mapper;

import cn.dagongren8.teamplus.entity.Subject;
import cn.dagongren8.teamplus.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Mapper
public interface SubjectMapper {

    List<Subject> getAllSubject();

    List<User> getSubjectAllUsers(int subjectId);

    Subject getSubjectById(int subjectId);

    Subject getSubjectByIdentification(String subjectIdentification);

    int insertSubject(Subject subject);

    int updateSubjectById(Subject subject);

    int deleteSubjectById(int subjectId);

}
