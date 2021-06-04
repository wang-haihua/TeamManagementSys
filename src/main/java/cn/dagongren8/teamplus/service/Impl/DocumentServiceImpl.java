package cn.dagongren8.teamplus.service.Impl;

import cn.dagongren8.teamplus.entity.Document;
import cn.dagongren8.teamplus.mapper.DocumentMapper;
import cn.dagongren8.teamplus.service.DocumentService;
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
public class DocumentServiceImpl implements DocumentService {

    private final DocumentMapper documentMapper;

    public DocumentServiceImpl(DocumentMapper documentMapper) {
        this.documentMapper = documentMapper;
    }

    @Override
    public List<Document> getAllDocuments(int directoryId) {
        return documentMapper.getAllDocuments(directoryId);
    }

    @Override
    public Document getDocumentById(int documentId) {
        return documentMapper.getDocumentById(documentId);
    }

    @Override
    public int insertDocument(Document document) {
        return documentMapper.insertDocument(document);
    }

    @Override
    public int updateDocumentById(Document document) {
        return documentMapper.updateDocumentById(document);
    }

    @Override
    public int deleteDocumentById(int documentId) {
        return documentMapper.deleteDocumentById(documentId);
    }
}
