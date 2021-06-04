package cn.dagongren8.teamplus.service.Impl;

import cn.dagongren8.teamplus.entity.Message;
import cn.dagongren8.teamplus.mapper.MessageMapper;
import cn.dagongren8.teamplus.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;

    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public List<Message> getAllSubjectMessages(int subjectId) {
        return messageMapper.getAllSubjectMessages(subjectId);
    }

    @Override
    public Message getMessageById(int messageId) {
        return messageMapper.getMessageById(messageId);
    }

    @Override
    public int insertMessage(Message message) {
        return messageMapper.insertMessage(message);
    }

    @Override
    public int updateMessageById(Message message) {
        return messageMapper.updateMessageById(message);
    }

    @Override
    public int deleteMessageById(int messageId) {
        return messageMapper.deleteMessageById(messageId);
    }
}
