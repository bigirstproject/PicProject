
package com.kugou.framework.component.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * 描述:可处理耗时操作的activity
 */
public abstract class BaseWorkerFragmentActivity extends BaseFragmentActivity {

    private HandlerThread mHandlerThread;

    protected BackgroundHandler mBackgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandlerThread = new HandlerThread("activity worker:" + getClass().getSimpleName());
        mHandlerThread.start();
        mBackgroundHandler = new BackgroundHandler(mHandlerThread.getLooper());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null && mBackgroundHandler.getLooper() != null) {
            mBackgroundHandler.getLooper().quit();
        }
    }

    /**
     * 处理后台操作
     */
    protected abstract void handleBackgroundMessage(Message msg);

    /**
     * 发送后台操作
     * 
     * @param msg
     */
    protected void sendBackgroundMessage(Message msg) {
        mBackgroundHandler.sendMessage(msg);
    }

    /**
     * 发送后台操作
     * 
     * @param what
     */
    protected void sendEmptyBackgroundMessage(int what) {
        mBackgroundHandler.sendEmptyMessage(what);
    }

    // 后台Handler
    public class BackgroundHandler extends Handler {

        BackgroundHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handleBackgroundMessage(msg);
        }
    }
}
