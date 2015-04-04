
package com.kugou.framework.component.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

/**
 * 描述:所有Fragment的父类,提供刷新UI的Handler
 * 
 */
public class BaseFragment extends Fragment {

    protected View mView;

    protected Activity mActivity;

    public void setShownIndex(int index) {
        Bundle args = new Bundle();
        args.putInt("index", index);
        setArguments(args);
    }

    public int getShownIndex() {
        return getArguments() == null ? 0 : getArguments().getInt("index", 0);
    }

    private Toast mToast;

    protected Handler mUiHandler;

    @SuppressLint("HandlerLeak")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mView = getView();
        mActivity = getActivity();
        mUiHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (isAdded()) {
                    super.handleMessage(msg);
                    handleUiMessage(msg);
                }
            };
        };
    }

    /**
     * 处理更新UI任务
     * 
     * @param msg
     */
    protected void handleUiMessage(Message msg) {
    }

    /**
     * 发送UI更新操作
     * 
     * @param msg
     */
    protected void sendUiMessage(Message msg) {
        mUiHandler.sendMessage(msg);
    }

    /**
     * 发送UI更新操作
     * 
     * @param what
     */
    protected void sendEmptyUiMessage(int what) {
        mUiHandler.sendEmptyMessage(what);
    }

    /**
     * 显示一个Toast类型的消息
     * 
     * @param msg 显示的消息
     */
    public void showToast(final String msg) {
        if (getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
                }
                mToast.setText(msg);
                mToast.show();
            }
        });

    }

    /**
     * 显示{@link Toast}通知
     * 
     * @param strResId 字符串资源id
     */
    public void showToast(int strResId) {
        if (getActivity() == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(strResId);
        mToast.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // MobclickAgent.onResume(getActivity());

    }

    @Override
    public void onPause() {
        super.onPause();
        // MobclickAgent.onPause(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // MobclickAgent.onError(getActivity());
    }

}
