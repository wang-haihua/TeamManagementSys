package cn.dagongren8.teamplus.service;

import cn.dagongren8.teamplus.entity.Attachment;
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
public interface AttachmentService{

    List<Attachment> getAllTaskAttachments(int taskId);

    Attachment getAttachmentById(int attachmentId);

    int insertAttachment(Attachment attachment);

    int updateAttachmentById(Attachment attachment);

    int deleteUserById(int attachmentId);
}
