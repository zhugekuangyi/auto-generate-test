/*
 * uifuture.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package core.util;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author chuqi
 */
public class UrlUtil {
    private static Log log = new SystemStreamLog();

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr 地址
     * @param fileName 文件名称
     * @param savePath 保存路径
     * @throws IOException IO异常
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            if (!saveDir.mkdirs()) {
                log.info(savePath + " 文件路径不存在，AGT进行创建失败，请检查是否有权限");
                return;
            }
        }
        File file = new File(saveDir + File.separator + fileName);
        if(file.exists()){
            log.info("配置文件已经存在不进行下载，URL:" + urlStr + ",路径：" + savePath + ",文件名：" + fileName);
            return;
        }

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为30秒
        conn.setConnectTimeout(30 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        fos.close();
        inputStream.close();
        log.info("下载配置文件成功，URL:" + url + ",路径：" + savePath + ",文件名：" + fileName);
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream 输入流
     * @return 字节数组
     * @throws IOException 流处理IO异常
     */
    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}