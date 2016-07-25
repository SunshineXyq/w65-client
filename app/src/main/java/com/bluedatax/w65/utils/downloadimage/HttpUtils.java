package com.bluedatax.w65.utils.downloadimage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.bluedatax.w65.activity.BirthdaySound;
import com.bluedatax.w65.activity.LeaveMessage;

/**
 * 
 * @author Administrator
 * @date 2014.05.10
 * @version V1.0
 */
public class HttpUtils {

    /**
     * 下载图片
     * @param urlString  输入图片地址
     * @return           返回bitmap位图
     */

    public static Bitmap getNetWorkBitmap(String urlString) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(urlString);
            HttpURLConnection urlConn = (HttpURLConnection) imgUrl.openConnection();
            urlConn.setRequestProperty("ServerProvider", "BDX");
            urlConn.setConnectTimeout(5000);
            urlConn.setRequestMethod("GET");
            urlConn.setDoInput(true);
            urlConn.connect();

            InputStream is = urlConn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("[getNetWorkBitmap->]MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[getNetWorkBitmap->]IOException");
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     *上传图片
     * @param urlString   上传图片地址
     * @return            判断上传是否成功
     */

    public static String getResponse(String urlString) {
        URL imgUrl = null;
        String sb = null;
        try {
            imgUrl = new URL(urlString);
            HttpURLConnection urlConn = (HttpURLConnection) imgUrl.openConnection();
            urlConn.setRequestProperty("ServerProvider", "BDX");
            urlConn.setConnectTimeout(5000);
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);
            OutputStream out = urlConn.getOutputStream();
            FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory() + "/w65/icon_bitmap/" + "myicon.jpg");
            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            fis.close();
            urlConn.connect();

            InputStream is = urlConn.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            Log.i("返回的数组", b.toString());
            sb = b.toString();
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("[getNetWorkBitmap->]MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[getNetWorkBitmap->]IOException");
            e.printStackTrace();
        }
        return sb;
    }

    /**
     * 上传铃声
     * @param urlString    铃声地址
     * @return             判断是否上传成功
     */

    public static String uploadRing(String urlString,String type) {
        URL ringUrl = null;
        String message = null;
        HttpURLConnection urlConn = null;
        String recordPath = null;
        try {
            ringUrl = new URL(urlString);
            urlConn = (HttpURLConnection) ringUrl.openConnection();
            urlConn.setRequestProperty("ServerProvider", "BDX");
            urlConn.setConnectTimeout(5000);
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);
            OutputStream out = urlConn.getOutputStream();
            if (type.equals("telephoneMessage")) {
                 recordPath = LeaveMessage.databaseLast.getRecordPath();      //电话留言语音文件路径
            } else if (type.equals("ring")) {
                 recordPath = BirthdaySound.ringLast.getRingRecordPath();     //生日提醒中铃声语音文件路径
            }
            FileInputStream fis = new FileInputStream(recordPath);
            System.out.println(BirthdaySound.ringLast.getRingRecordPath());
            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            fis.close();
            urlConn.connect();

            int responseCode = urlConn.getResponseCode();
            System.out.println("错误码-----------"+responseCode);
            message = urlConn.getResponseMessage();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("[getNetWorkBitmap->]MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[getNetWorkBitmap->]IOException");
            e.printStackTrace();
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return message;
    }
}