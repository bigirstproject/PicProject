
package com.sunsun.picproject.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kugou.framework.component.base.AppException;

/**
 * 描述: 上传重试（包括语音上传，图片上传，赞，回复）
 * 
 */
public class HttpUploadHelper {

    /**
     * 默认每个任务重试次数
     */
    private final int mTryTimes = 1;

    /**
     * 单线程池
     */
    private ExecutorService pool = null;

    private volatile static HttpUploadHelper mInstance = null;

    public static HttpUploadHelper getInstance() {
        if (mInstance == null) {
            synchronized (HttpUploadHelper.class) {
                if (mInstance == null) {
                    mInstance = new HttpUploadHelper();
                }
            }
        }

        return mInstance;
    }

    private HttpUploadHelper() {
        pool = Executors.newSingleThreadExecutor();
    }

    /**
     * 上传
     * 
     * @param task
     * @param requestPackage
     * @param responsePackage
     */
    public void putUploadTask(IUploadTask task) {

        pool.execute(new ThreadPoolTask(task));

    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        pool.shutdownNow();
    }

    public interface IUploadTask extends RequestPackage, ResponsePackage {

        /**
         * 执行成功
         */
        public void successTask();

        /**
         * 执行失败
         */
        public void failTask(AppException e);

    }

    private class ThreadPoolTask implements Runnable {

        private IUploadTask mTask = null;

        private int mTriedTimes = 0;

        public ThreadPoolTask(IUploadTask task) {
            this.mTask = task;
            this.mTriedTimes = 0;

        }

        @Override
        public void run() {
            AppException ae = null;
            while (mTriedTimes++ <= mTryTimes) {

                try {
                    KGHttpClient.request(mTask, mTask);
                    mTask.successTask();
                    return;
                } catch (AppException e) {
                    ae = e;
                    e.printStackTrace();
                }
            }

            // 如果失败
            mTask.failTask(ae);
        }

    }
}
