package cn.dagongren8.teamplus.service.Impl;

import cn.dagongren8.teamplus.entity.Directory;
import cn.dagongren8.teamplus.mapper.DirectoryMapper;
import cn.dagongren8.teamplus.service.DirectoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Service
public class DirectoryServiceImpl implements DirectoryService {

    private final DirectoryMapper directoryMapper;

    public DirectoryServiceImpl(DirectoryMapper directoryMapper) {
        this.directoryMapper = directoryMapper;
    }

    @Override
    public List<Directory> getAllChildDirectories(int directoryId) {
        return directoryMapper.getAllChildDirectories(directoryId);
    }

    @Override
    public Directory getDirectoryById(int directoryId) {
        return directoryMapper.getDirectoryById(directoryId);
    }

    /**
     * 获取指定文件夹的绝对路径
     *
     * @param directoryId 文件夹ID
     * @return 文件夹组成的List表示该文件夹的绝对路径
     */
    @Override
    public List<Directory> getAbsolutePath(int directoryId) {
        Stack<Directory> stack = new Stack<>();

        while (directoryId != 0) {
            Directory d = directoryMapper.getDirectoryById(directoryId);
            stack.push(d);
            directoryId = d.getDirectoryParentId();
        }

        List<Directory> path = new ArrayList<>();
        while (!stack.isEmpty()) {
            path.add(stack.pop());
        }

        return path;
    }

    @Override
    public int insertDirectory(Directory directory) {
        return directoryMapper.insertDirectory(directory);
    }

    @Override
    public int updateDirectoryById(Directory directory) {
        return directoryMapper.updateDirectoryById(directory);
    }

    @Override
    public int deleteTeamById(int directoryId) {
        return directoryMapper.deleteTeamById(directoryId);
    }
}
