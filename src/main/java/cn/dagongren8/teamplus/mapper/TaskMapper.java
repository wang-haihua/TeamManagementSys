package cn.dagongren8.teamplus.mapper;

import cn.dagongren8.teamplus.entity.Task;
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
public interface TaskMapper {

    List<Task> getSubjectTasks(int subjectId);

    List<Task> getUserTasks(int userId);

    Task getTaskById(int taskId);

    int insertTask(Task task);

    int updateTaskById(Task task);

    int deleteTaskById(int taskId);

}
