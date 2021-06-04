package cn.dagongren8.teamplus.service.Impl;

import cn.dagongren8.teamplus.entity.Attachment;
import cn.dagongren8.teamplus.mapper.AttachmentMapper;
import cn.dagongren8.teamplus.service.AttachmentService;
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
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Override
    public List<Attachment> getAllTaskAttachments(int taskId) {
        return attachmentMapper.getAllTaskAttachments(taskId);
    }

    @Override
    public Attachment getAttachmentById(int attachmentId) {
        return attachmentMapper.getAttachmentById(attachmentId);
    }

    @Override
    public int insertAttachment(Attachment attachment) {
        return attachmentMapper.insertAttachment(attachment);
    }

    @Override
    public int updateAttachmentById(Attachment attachment) {
        return attachmentMapper.updateAttachmentById(attachment);
    }

    @Override
    public int deleteUserById(int attachmentId) {
        return attachmentMapper.deleteUserById(attachmentId);
    }
}
