package cn.dagongren8.teamplus.service;

import cn.dagongren8.teamplus.entity.Task;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Service
public interface TaskService{

    List<Task> getSubjectTasks(int subjectId);

    PageInfo<Task> getSubjectTasks(int subjectId, int pageIndex, int pageSize);

    List<Task> getUserTasks(int userId);

    Task getTaskById(int taskId);

    int insertTask(Task task);

    int updateTaskById(Task task);

    int deleteTaskById(int taskId);

}
