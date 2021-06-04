package cn.dagongren8.teamplus.mapper;

import cn.dagongren8.teamplus.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
public interface MessageMapper {

    List<Message> getAllSubjectMessages(int subjectId);

    Message getMessageById(int messageId);

    int insertMessage(Message message);

    int updateMessageById(Message message);

    int deleteMessageById(int messageId);
}
