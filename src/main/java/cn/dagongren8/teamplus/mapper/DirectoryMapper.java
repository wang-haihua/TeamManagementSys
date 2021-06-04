package cn.dagongren8.teamplus.mapper;

import cn.dagongren8.teamplus.entity.Directory;
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
public interface DirectoryMapper {

    List<Directory> getAllChildDirectories(int directoryId);

    Directory getDirectoryById(int directoryId);

    int insertDirectory(Directory directory);

    int updateDirectoryById(Directory directory);

    int deleteTeamById(int directoryId);

}
