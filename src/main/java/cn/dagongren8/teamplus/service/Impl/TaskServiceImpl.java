package cn.dagongren8.teamplus.service.Impl;

import cn.dagongren8.teamplus.entity.Task;
import cn.dagongren8.teamplus.mapper.TaskMapper;
import cn.dagongren8.teamplus.service.TaskService;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<Task> getSubjectTasks(int subjectId) {
        return taskMapper.getSubjectTasks(subjectId);
    }

    @Override
    public PageInfo<Task> getSubjectTasks(int subjectId, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Task> tasks = taskMapper.getSubjectTasks(subjectId);
        return new PageInfo<>(tasks);
    }

    @Override
    public List<Task> getUserTasks(int userId) {
        return taskMapper.getUserTasks(userId);
    }

    @Override
    public Task getTaskById(int taskId) {
        return taskMapper.getTaskById(taskId);
    }

    @Override
    public int insertTask(Task task) {
        return taskMapper.insertTask(task);
    }

    @Override
    public int updateTaskById(Task task) {
        return taskMapper.updateTaskById(task);
    }

    @Override
    public int deleteTaskById(int taskId) {
        return taskMapper.deleteTaskById(taskId);
    }
}
