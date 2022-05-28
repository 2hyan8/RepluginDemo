package com.zhyan8.host.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.qihoo360.replugin.RePlugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownLoadUtils {
    private static final String TAG = "DownLoadUtils";

    public static void download(String urlPath, Handler handler, String downloadDir) {
        try {
            URL url = new URL(urlPath);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            Log.v(TAG, "before connect");
            httpURLConnection.connect();
            Log.v(TAG, "after connect");
            int fileLength = httpURLConnection.getContentLength();
            String filePathUrl = httpURLConnection.getURL().getFile();
            String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);
            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());
            String path = downloadDir + File.separatorChar + fileFullName;
            File file = new File(path);
            if (file.exists()) {
                Log.v(TAG, "文件已存在， 无需下载");
                return;
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(file);
            int size = 0;
            float progress = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                out.write(buf, 0, size);
                progress += size * 100.0 / fileLength;
                sendMsg(String.valueOf(Math.round(progress)), handler);
                Log.v(TAG, "下载了-------> " + Math.round(progress));
            }
            bin.close();
            out.close();
            Log.v(TAG, "下载完成 : " + path);
        } catch (MalformedURLException e) {
            Log.v(TAG, e.getMessage());
        } catch (IOException e) {
            Log.v(TAG, e.getMessage());
        } finally {
            Log.v(TAG, "finally");
        }
    }

    private static void sendMsg(String str, Handler handler) {
        Message msg = handler.obtainMessage();
        msg.obj = str;
        handler.sendMessage(msg);
    }
}
