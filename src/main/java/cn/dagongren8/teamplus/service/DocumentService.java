package cn.dagongren8.teamplus.service;

import cn.dagongren8.teamplus.entity.Document;
import org.apache.ibatis.annotations.Param;
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
public interface DocumentService {
    List<Document> getAllDocuments(@Param("directoryId") int directoryId);

    Document getDocumentById(int documentId);

    int insertDocument(Document document);

    int updateDocumentById(Document document);

    int deleteDocumentById(int documentId);
}
