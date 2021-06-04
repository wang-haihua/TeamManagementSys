package cn.dagongren8.teamplus.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */

public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer documentId;

    private Directory directory;

    private String documentName;

    private String documentKey;

    private User documentCreator;

    private Date documentCreatetime;

    private String documentType;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentKey() {
        return documentKey;
    }

    public void setDocumentKey(String documentKey) {
        this.documentKey = documentKey;
    }

    public User getDocumentCreator() {
        return documentCreator;
    }

    public void setDocumentCreator(User documentCreator) {
        this.documentCreator = documentCreator;
    }

    public Date getDocumentCreatetime() {
        return documentCreatetime;
    }

    public void setDocumentCreatetime(Date documentCreatetime) {
        this.documentCreatetime = documentCreatetime;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @Override
    public String toString() {
        return "Document{" +
                "documentId=" + documentId +
                ", directory=" + directory +
                ", documentName='" + documentName + '\'' +
                ", documentKey='" + documentKey + '\'' +
                ", documentCreator=" + documentCreator +
                ", documentCreatetime=" + documentCreatetime +
                ", documentType='" + documentType + '\'' +
                '}';
    }
}
