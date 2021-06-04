package cn.dagongren8.teamplus.mapper;


import cn.dagongren8.teamplus.entity.SubjectAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Mapper
public interface SubjectAuthorityMapper {
    List<SubjectAuthority> getAllSubjectAuthorities();

    List<SubjectAuthority> getAllSubjectAuthorityOfUser(int userId);

    List<SubjectAuthority> getAllSubjectUsers(int subjectId);

    List<SubjectAuthority> getSubjectUsersByAuthorityType(@Param("subjectId") int subjectId, @Param("subjectAuthorityType") int subjectAuthorityType);

    int insertSubjectAuthority(SubjectAuthority subjectAuthority);

    int updateSubjectAuthority(SubjectAuthority subjectAuthority);

    int deleteSubjectAuthority(@Param("subjectId") int subjectId, @Param("userId") int userId);

    SubjectAuthority getSubjectAuthority(@Param("subjectId") int subjectId, @Param("userId") int userId);
}
