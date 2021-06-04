package cn.dagongren8.teamplus.service;

import cn.dagongren8.teamplus.entity.Directory;
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
public interface DirectoryService {
    List<Directory> getAllChildDirectories(int directoryId);

    Directory getDirectoryById(int directoryId);

    List<Directory> getAbsolutePath(int directoryId);

    int insertDirectory(Directory directory);

    int updateDirectoryById(Directory directory);

    int deleteTeamById(int directoryId);
}
