
package com.sunsun.picproject.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import android.content.pm.PackageInfo;
import android.text.TextUtils;

import com.kugou.framework.component.base.AppException;
import com.kugou.framework.component.base.BaseApplication;
import com.kugou.framework.component.debug.KGLog;


/**
 * HTTP请求客户端
 * 
 */
public class KGHttpClient {

    private final static String TAG = "KGHttpClient";

    // 最大重试次数
    private final static int MAX_RETRY_NUM = 1;

    private KGHttpClient() {
    }

    /**
     * 发起网络请求，并读取服务器返回的数据
     * 
     * @param requestPackage
     * @param responsePackage
     * @throws AppException
     */
    public static void request(RequestPackage requestPackage,
            ResponsePackage<Object> responsePackage) throws AppException {
        request(requestPackage, responsePackage, true);
    }

    /**
     * 发起网络请求，并读取服务器返回的数据
     * 
     * @param requestPackage
     * @param responsePackage
     * @param isRetry 是否重试
     * @throws AppException
     */
    public static void request(RequestPackage requestPackage,
            ResponsePackage<Object> responsePackage, boolean isRetry) throws AppException {
        HttpClient httpClient = null;
        HttpMethod httpMethod = null;

        int tryNum = 0;
        do {
            try {

                // MobclickAgent.onEvent(CommunityApplication.getInstance().getApplicationContext(),
                // EVENT_ID_REQUEST_COUNT);

                httpClient = createHttpClient(requestPackage);
                httpMethod = createHttpMethod(requestPackage);
                int statusCode = httpClient.executeMethod(httpMethod);
                if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED
                        && statusCode != HttpStatus.SC_NO_CONTENT
                        && statusCode != HttpStatus.SC_PARTIAL_CONTENT) {
                    if (responsePackage != null) {
                        responsePackage.setResponseError(httpMethod.getResponseBody());
                    }
                    throw AppException.http(statusCode);
                }
                if (responsePackage != null) {
                    responsePackage.setContext(httpMethod.getResponseBody());
                }
                break;
            } catch (HttpException e) {
                // MobclickAgent.onEvent(CommunityApplication.getInstance().getApplicationContext(),
                // UmengUtil.EVENT_ID_REQUEST_FAIL_COUNT, e.getMessage());
//                UmengUtil.addErrorMap(e.getMessage());
                tryNum++;
                if (tryNum < MAX_RETRY_NUM) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // 发生致命的异常，可能是协议不对或者返回的内容有问题
                // e.printStackTrace();
                throw AppException.http(e);
            } catch (IOException e) {

                // MobclickAgent.onEvent(CommunityApplication.getInstance().getApplicationContext(),
                // UmengUtil.EVENT_ID_REQUEST_FAIL_COUNT, e.getMessage());
//                UmengUtil.addErrorMap(e.getMessage());

                tryNum++;
                if (tryNum < MAX_RETRY_NUM) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // 发生网络异常
                // e.printStackTrace();
                throw AppException.network(e);
            } finally {
                // 释放连接
                if (httpMethod != null) {
                    // 释放连接
                    httpMethod.releaseConnection();
                }
                httpClient = null;
            }
        } while (isRetry && tryNum < MAX_RETRY_NUM);
    }

    /**
     * 图片下载
     * 
     * @param requestPackage
     * @param downloadListener
     * @throws AppException
     */
    public static void download(RequestPackage requestPackage, IDownloadListener downloadListener)
            throws AppException {
        HttpClient httpClient = null;
        HttpMethod httpMethod = null;

        long fileSize = 0;
        // 校验md5
        String hash = null;
        try {
            httpClient = createHttpClient(requestPackage);
            httpMethod = createHttpMethod(requestPackage);
            int statusCode = httpClient.executeMethod(httpMethod);
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED
                    && statusCode != HttpStatus.SC_NO_CONTENT
                    && statusCode != HttpStatus.SC_PARTIAL_CONTENT) {
                throw AppException.http(statusCode);
            }

            Header header = httpMethod.getResponseHeader("Content-Length");
            Header headerMd5 = httpMethod.getResponseHeader("Content-MD5");
            if (headerMd5 != null) {
                hash = headerMd5.getValue();
            }
            try {
                fileSize = Long.parseLong(header.getValue());
            } catch (Exception e) {
            }
            BufferedInputStream bis = new BufferedInputStream(httpMethod.getResponseBodyAsStream());
            byte[] data = new byte[8 * 1024];
            long haveRead = 0;
            int read = 0;
            int progress = 0;
            while ((read = bis.read(data)) != -1) {
                haveRead += read;
                if (fileSize > 0) {
                    progress = (int) ((float) haveRead / fileSize * 100);
                }
                downloadListener.onProgressChanged(data, 0, read, progress);
            }
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            // e.printStackTrace();
            throw AppException.http(e);
        } catch (IOException e) {
            // 发生网络异常
            // e.printStackTrace();
            throw AppException.network(e);
        } catch (Exception e) {
            // 保存数据时异常
            // e.printStackTrace();
            throw AppException.run(e);
        } finally {
            downloadListener.onProgressFinish(hash);
            if (httpMethod != null) {
                // 释放连接
                httpMethod.releaseConnection();
            }
            httpClient = null;
        }
    }

    /**
     * 资源包下载
     * 
     * @param requestPackage
     * @param downloadListener
     * @throws AppException
     */
    public static void download(RequestPackage requestPackage,
            IDownloadResListener downloadResListener) throws AppException {
        HttpClient httpClient = null;
        HttpMethod httpMethod = null;

        long fileSize = 0;
        try {
            httpClient = createHttpClient(requestPackage);
            httpMethod = createHttpMethod(requestPackage);
            int statusCode = httpClient.executeMethod(httpMethod);
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED
                    && statusCode != HttpStatus.SC_NO_CONTENT
                    && statusCode != HttpStatus.SC_PARTIAL_CONTENT) {
                throw AppException.http(statusCode);
            }

            downloadResListener.onDownloadStart(httpMethod);
            Header header = httpMethod.getResponseHeader("Content-Length");
            try {
                fileSize = Long.parseLong(header.getValue());
            } catch (Exception e) {
            }
            BufferedInputStream bis = new BufferedInputStream(httpMethod.getResponseBodyAsStream());
            byte[] data = new byte[8 * 1024];
            long haveRead = 0;
            int read = 0;
            int progress = 0;
            while ((read = bis.read(data)) != -1) {
                haveRead += read;
                if (fileSize > 0) {
                    progress = (int) ((float) haveRead / fileSize * 100);
                }
                downloadResListener.onProgressChanged(data, 0, read, progress);
            }
            downloadResListener.onProgressFinish();
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            // e.printStackTrace();
            throw AppException.http(e);
        } catch (IOException e) {
            // 发生网络异常
            // e.printStackTrace();
            throw AppException.network(e);
        } catch (Exception e) {
            // 保存数据时异常
            // e.printStackTrace();
            throw AppException.run(e);
        } finally {
            if (httpMethod != null) {
                // 释放连接
                httpMethod.releaseConnection();
            }
            httpClient = null;
        }
    }

    // 创建一个HttpClient对象
    private static HttpClient createHttpClient(RequestPackage requestPackage) {
        HttpClient httpClient = new HttpClient();
        // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策略
        httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());

        Hashtable<String, Object> params = requestPackage.getSettings();
        if (params != null) {
            if (params.containsKey("conn-timeout")) {
                // 设置 连接超时时间
                httpClient.getHttpConnectionManager().getParams()
                        .setConnectionTimeout((Integer) params.get("conn-timeout"));
            }
            if (params.containsKey("socket-timeout")) {
                // 设置 读数据超时时间
                httpClient.getHttpConnectionManager().getParams()
                        .setSoTimeout((Integer) params.get("socket-timeout"));
            }
        }

        // cmwap设置代理
        boolean isCmwap = NetWorkUtil.isCmwap(BaseApplication.getInstance());
        if (isCmwap) {
            httpClient.getHostConfiguration().setProxy("10.0.0.172", 80);
        }
        return httpClient;
    }

    // 创建一个HttpClient对象
    public static HttpClient createHttpClient() {
        HttpClient httpClient = new HttpClient();
        // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策略
        httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        // cmwap设置代理
        boolean isCmwap = NetWorkUtil.isCmwap(BaseApplication.getInstance());
        if (isCmwap) {
            httpClient.getHostConfiguration().setProxy("10.0.0.172", 80);
        }
        return httpClient;
    }

    // 创建一个HttpMethod对象
    private static HttpMethod createHttpMethod(RequestPackage requestPackage) {
        String prevUrl = requestPackage.getUrl();
        String params = requestPackage.getGetRequestParams();
        KGLog.d(TAG, prevUrl + params);

        HttpMethod httpMethod = null;
        if (requestPackage.getRequestType() == RequestPackage.TYPE_GET) {
            httpMethod = new GetMethod(prevUrl + params);
        } else {
            httpMethod = new PostMethod(prevUrl);
            ((PostMethod) httpMethod).setRequestEntity(requestPackage.getPostRequestEntity());
        }

        Hashtable<String, Object> settings = requestPackage.getSettings();
        if (settings != null) {
            if (settings.containsKey("socket-timeout")) {
                // 设置 请求超时时间
                httpMethod.getParams().setSoTimeout((Integer) settings.get("socket-timeout"));
            }
        }

        // 设置头
        Hashtable<String, String> headers = requestPackage.getRequestHeaders();
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                if (!"Connection".equalsIgnoreCase(key) && !"User-Agent".equalsIgnoreCase(key)) {
                    // 屏弊自定义Connection和User-Agent
                    httpMethod.setRequestHeader(key, headers.get(key));
                }
            }
        }
        httpMethod.setRequestHeader("Connection", "Keep-Alive");
        httpMethod.setRequestHeader("User-Agent", getUserAgent());

        return httpMethod;
    }

    private static String sUserAgent;

    public static String getUserAgent() {
        if (TextUtils.isEmpty(sUserAgent)) {
            PackageInfo pi = BaseApplication.getInstance().getPackageInfo();
            StringBuilder ua = new StringBuilder();
            // 产品名称
            ua.append("Platform");
            // 软件版本
            ua.append("/ver_" + pi.versionCode);
            // 系统版本
            ua.append("/sdk_" + android.os.Build.VERSION.SDK_INT);
            sUserAgent = ua.toString();
        }
        return sUserAgent;
    }

    /**
     * 图片下载回调
     */
    public interface IDownloadListener {

        void onProgressChanged(byte[] data, int offset, int length, int progress);

        /**
         * 校验md5
         * 
         * @param hash
         */
        void onProgressFinish(String hash);
    }

    /**
     * 资源下载回调
     */
    public interface IDownloadResListener {

        void onDownloadStart(HttpMethod httpMethod);

        void onProgressChanged(byte[] data, int offset, int length, int progress);

        void onProgressFinish();
    }

    /**
     * 上传回调
     */
    public interface IUploadListener {

        void onProgressChanged(int progress);
    }

}
