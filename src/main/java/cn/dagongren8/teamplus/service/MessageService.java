package cn.dagongren8.teamplus.service;

import cn.dagongren8.teamplus.entity.Message;
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
public interface MessageService {

    List<Message> getAllSubjectMessages(int subjectId);

    Message getMessageById(int messageId);

    int insertMessage(Message message);

    int updateMessageById(Message message);

    int deleteMessageById(int messageId);
}
