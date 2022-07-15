package com.lj.httpdemo.Request;

import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class HttpTool {

    public static RequestBody convertToRequestBody(String param) {
        return RequestBody.create(MediaType.parse("text/plain"), param);
    }

    public static String signTopRequest(Map<String, Object> params, String secret, String signMethod) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        for (String key : keys) {
            Object value = params.get(key);
            if (value instanceof String) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty((String) value)) {
                    query.append(key).append(value);
                }
            } else if (value instanceof File) {
                String strVal = getFileContent((File) value);
                query.append(key).append(strVal);
                params.remove(key);
            }
        }

        // 第三步：使用MD5/HMAC加密
        byte[] bytes;
        if ("HMAC".equals(signMethod)) {
            bytes = encryptHMAC(query.toString(), secret);
        } else {
            bytes = encryptMD5(query.toString() + secret);
        }

        // 第四步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }

    public static byte[] encryptHMAC(String data, String secret) throws IOException {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes("UTF-8"));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }

    public static byte[] encryptMD5(String data) throws IOException {
        byte[] md5Byte = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");  // 创建一个md5算法对象
            byte[] messageByte = data.getBytes("UTF-8");
            md5Byte = md.digest(messageByte);              // 获得MD5字节数组,16*8=128位
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5Byte;
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toLowerCase());
        }
        return sign.toString();
    }

        // 将文件进行SHA1加密
    public static String getFileContent(File file) {
        try {
            StringBuffer sb = new StringBuffer();
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            FileInputStream fin = new FileInputStream(file);
            int len = -1;
            byte[] buffer = new byte[1024];//设置输入流的缓存大小 字节
            //将整个文件全部读入到加密器中
            while ((len = fin.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            //对读入的数据进行加密
            byte[] bytes = digest.digest();
            for (byte b : bytes) {
                // 数byte 类型转换为无符号的整数
                int n = b & 0XFF;
                // 将整数转换为16进制
                String s = Integer.toHexString(n);
                // 如果16进制字符串是一位，那么前面补0
                if (s.length() == 1) {
                    sb.append("0" + s);
                } else {
                    sb.append(s);
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static TreeMap getTreeCrc(TreeMap maps) {
        try {
            //todo: 修改
            maps.put("app_key", "");
            maps.put("sign_method", "md5");
            maps.put("format", "json");
            maps.put("timestamp", TimeUtils.getNowString());
            maps.put("sign", signTopRequest(maps, "SECRET", "MD5"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }
}
