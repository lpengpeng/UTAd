package cn.utsoft.commons.ADView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Create by 李俊鹏 on 2016/12/7 9:43
 * Function：创建一个新的类 UTHttpTool，将公共的操作抽象出来
 * 为了避免调用sendRequest方法时需实例化，设置为静态方法
 * 传入HttpCallbackListener对象为了方法回调
 * 因为网络请求比较耗时，一般在子线程中进行，
 * 为了获得服务器返回的数据，需要使用java的回调机制
 * Desc：
 */

public class UTHttpTool {
    /**
     * 从网络Url中下载文件
     */
    public static void downLoadFromUrl(final String downLoadUrl, final File file, final HttpDownLoadListener listener) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(downLoadUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    if (conn instanceof HttpsURLConnection) {
                        SSLContext sc = SSLContext.getInstance("SSL");
                        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
                        ((HttpsURLConnection) conn).setSSLSocketFactory(sc.getSocketFactory());
                        ((HttpsURLConnection) conn).setHostnameVerifier(new TrustAnyHostnameVerifier());
                    }
                    //设置超时间为5秒
                    conn.setConnectTimeout(5 * 1000);
                    //防止屏蔽程序抓取而返回403错误
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    //得到输入流
                    InputStream inputStream = conn.getInputStream();
                    //获取自己数组
                    byte[] getData = readInputStream(inputStream);
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(getData);
                    fos.close();
                    listener.onSuccess(file);
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure();
                    if (conn != null) {
                        conn.disconnect();
                    }
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 从输入流中获取字节数组
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    public interface HttpDownLoadListener {
        void onSuccess(File file);

        void onFailure();
    }

    public static void getAdData(final String address, final Map<String, String> paramMap,
                                 final UtLoadAdListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    if (connection instanceof HttpsURLConnection) {
                        SSLContext sc = SSLContext.getInstance("SSL");
                        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
                        ((HttpsURLConnection) connection).setSSLSocketFactory(sc.getSocketFactory());
                        ((HttpsURLConnection) connection).setHostnameVerifier(new TrustAnyHostnameVerifier());
                    }
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    StringBuffer buffer = new StringBuffer();
                    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                        buffer = buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                    }
                    String s = buffer.toString();
                    s = s.substring(0, s.length() - 1);
                    OutputStream os = connection.getOutputStream();
                    os.write(s.getBytes("utf-8"));
                    os.close();
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    if (listener != null) {
                        listener.loadSuccess(builder.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.loadFailure(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
