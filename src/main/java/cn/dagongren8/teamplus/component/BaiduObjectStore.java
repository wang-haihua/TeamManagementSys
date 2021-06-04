package cn.dagongren8.teamplus.component;

import cn.dagongren8.teamplus.util.Commons;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.PutObjectResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;

@Component
public class BaiduObjectStore {

    private final BCEProperties bceProperties;
    private final BosClient client;

    public BaiduObjectStore(BCEProperties bceProperties) {
        this.bceProperties = bceProperties;

        // 初始化一个BosClient
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(bceProperties.getAccessKey(), bceProperties.getSecretKey()));
        config.setEndpoint(bceProperties.getEndPoint());
        client = new BosClient(config);
    }

    /**
     * 将二进制上传至BCE
     *
     * @param bytes 二进制数据
     * @param key   识别该二进制数据的key
     * @return 文件存储的URL
     */
    public String putBytesToBCE(byte[] bytes, String key) {
        PutObjectResponse response = client.putObject(bceProperties.getBucketName(), key, bytes);
        return getResourceURL(key);
    }

    /**
     * 将二进制上传至BCE
     *
     * @param bytes 二进制数据
     * @return 文件存储的URL
     */
    public String putBytesToBCE(byte[] bytes) {
        return putBytesToBCE(bytes, generateRandomKey());
    }

    /**
     * 将文件上传至BCE
     *
     * @param file 文件
     * @param key  识别该文件的key
     * @return 文件存储的URL
     */
    public String putFileToBCE(File file, String key) {
        PutObjectResponse response = client.putObject(bceProperties.getBucketName(), key, file);
        return getResourceURL(key);
    }

    /**
     * 将文件上传至BCE
     *
     * @param file 文件
     * @return 文件存储的URL
     */
    public String putFileToBCE(File file) {
        return putFileToBCE(file, generateRandomKey());
    }

    /**
     * 获取资源的URL
     *
     * @param key 文件保存时使用的key
     * @return 资源的URL
     */
    public String getResourceURL(String key) {
        return bceProperties.getCdnURL() + key;
    }


    /**
     * 随机生成不重复的长度为32个字符的key
     *
     * @return 随机生成的key
     */
    public String generateRandomKey() {
        String uuid = Commons.getRandomUUID();
        String currentTime = System.currentTimeMillis() + "";
        String md5 = Commons.getMD5(uuid, currentTime);
        return md5.substring(0, 32 - currentTime.length()) + currentTime;
    }

    /**
     * 从BCE获取对象
     *
     * @param key 文件识别key
     * @return 文件的输入流（需要关闭）
     */
    public InputStream getFileFromBCE(String key) {
        BosObject object = client.getObject(bceProperties.getBucketName(), key);
        return object.getObjectContent();
    }

    /**
     * 配置BCE的各种参数
     */
    @Component
    @ConfigurationProperties(prefix = "teamplus.bce")
    public static class BCEProperties {
        private String endPoint;
        private String accessKey;
        private String secretKey;
        private String bucketName;
        private String cdnURL;

        public String getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(String endPoint) {
            this.endPoint = endPoint;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getCdnURL() {
            return cdnURL;
        }

        public void setCdnURL(String cdnURL) {
            this.cdnURL = cdnURL;
        }
    }
}
