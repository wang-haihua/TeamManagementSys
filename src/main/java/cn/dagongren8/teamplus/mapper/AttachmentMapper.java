package cn.dagongren8.teamplus.mapper;

import cn.dagongren8.teamplus.entity.Attachment;
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
public interface AttachmentMapper {

    List<Attachment> getAllTaskAttachments(int taskId);

    Attachment getAttachmentById(int attachmentId);

    int insertAttachment(Attachment attachment);

    int updateAttachmentById(Attachment attachment);

    int deleteUserById(int attachmentId);

}
