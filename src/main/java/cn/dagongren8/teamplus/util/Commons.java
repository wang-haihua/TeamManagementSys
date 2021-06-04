package cn.dagongren8.teamplus.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 常用工具类
 */
public class Commons {

    /**
     * 比较字符串是否相等
     *
     * @param a 字符串a
     * @param b 字符串b
     * @return 相等则为true，否则false
     */
    public static boolean stringEquals(String a, String b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    /**
     * 将字符串转换为加盐的MD5，用于密码的储存
     *
     * @param str  字符串
     * @param salt 盐
     * @return 加盐后的md5
     */
    public static String getMD5(String str, String salt) {
        String base = str + salt + str;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 生成随机的UUID
     *
     * @return UUID
     */
    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 格式化日期
     *
     * @param pattern 格式
     * @param date    日期
     * @return 格式化的日期字符串
     */
    public static String formatDate(String pattern, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * 若字符串为null则返回空字符串
     *
     * @param s 字符串
     * @return 非null字符串
     */
    public static String nullAsEmptyString(String s) {
        return s == null ? "" : s;
    }

    /**
     * base64编码字符串
     *
     * @param text 字符串
     * @return base64编码后的字符串
     */
    public static String base64Encoding(String text) {
        Base64 base64 = new Base64();
        byte[] textByte = text.getBytes(StandardCharsets.UTF_8);
        return base64.encodeToString(textByte);
    }
}
