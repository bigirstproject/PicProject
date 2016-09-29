
package com.sunsun.picproject.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * 描述:基本Http[Get、Post]
 */
public class SimpleHttp {

    /**
     * get 请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static byte[] RequestGet(String url) throws IOException {
        HttpURLConnection httpcon = (HttpURLConnection) ((new URL(url).openConnection()));

        httpcon.setDoOutput(true);
        httpcon.setRequestProperty("Content-Type", "application/json");
        httpcon.setRequestProperty("Accept", "application/json");
        httpcon.setRequestMethod("POST");
        httpcon.connect();

//        byte[] outputBytes = json.getBytes("UTF-8");
//        OutputStream os = httpcon.getOutputStream();
//        os.write(outputBytes);
//        os.close();
        int status = httpcon.getResponseCode();
        if (status == 200) {
            return InputStream2bytes(httpcon.getInputStream());
        }
        return null;
    }

    /**
     * post 请求
     *
     * @param url
     * @param json String json
     * @throws IOException
     * @throws MalformedURLException
     */
    public static byte[] RequestPost(String url, String json) throws
            IOException {
        HttpURLConnection httpcon = (HttpURLConnection) ((new URL(url).openConnection()));

        httpcon.setDoOutput(true);
        httpcon.setRequestProperty("Content-Type", "application/json");
        httpcon.setRequestProperty("Accept", "application/json");
        httpcon.setRequestMethod("POST");
        httpcon.connect();

        byte[] outputBytes = json.getBytes("UTF-8");
        OutputStream os = httpcon.getOutputStream();
        os.write(outputBytes);
        os.close();

        int status = httpcon.getResponseCode();
        if (status == 200) {
            return InputStream2bytes(httpcon.getInputStream());
        }
        return null;
    }

    /**
     * post 请求
     *
     * @param url
     * @throws IOException
     * @throws MalformedURLException
     */
    public static byte[] RequestPost(String url, HashMap<String, String> hashmap) throws
            IOException {
        HttpURLConnection httpcon = (HttpURLConnection) ((new URL(url).openConnection()));

        httpcon.setDoOutput(true);
        httpcon.setRequestProperty("Content-Type", "application/json");
        httpcon.setRequestProperty("Accept", "application/json");
        httpcon.setRequestMethod("POST");
        httpcon.connect();
        if (hashmap != null) {
            OutputStream os = httpcon.getOutputStream();
            os.write(hashmap.toString().getBytes("UTF-8"));
            os.close();
        }

        int status = httpcon.getResponseCode();
        if (status == 200) {
            return InputStream2bytes(httpcon.getInputStream());
        }
        return null;
    }


    private static byte[] InputStream2bytes(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = is.read(buff, 0, 100)) > 0) {
            bytestream.write(buff, 0, rc);
        }

        byte bytes[] = bytestream.toByteArray();
        bytestream.close();

        return bytes;
    }


}
