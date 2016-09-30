package com.kugou.framework.component.base;

import com.kugou.framework.component.preference.AppCommonPref;
import com.sunsun.picproject.Image.SwitchImageLoader;
import com.sunsun.picproject.R;
import com.sunsun.picproject.config.AppConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.push.FeedbackPush;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.onlineconfig.OnlineConfigCallBack;

public class PicProjectApplication extends BaseApplication {
    private static PicProjectApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initMobclickAgent();
        SwitchImageLoader.init(this);
        initYouMi();
    }

    /**
     * 获取Application实例
     *
     * @return
     */
    public static PicProjectApplication getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("Application is not created.");
        }
        return mInstance;
    }


    private void initMobclickAgent() {
        FeedbackPush.getInstance(this).init(false);
        MobclickAgent.setDebugMode(false);
        MobclickAgent.openActivityDurationTrack(false);
    }

    private void initYouMi() {
        AdManager.getInstance(this).init(AppConfig.YOUMI_APP_ID,
                AppConfig.YOUMI_APP_SECRET, false);
        AdManager.getInstance(this).setUserDataCollect(true);
        OffersManager.getInstance(this).onAppLaunch();
        AdManager.getInstance(this).asyncGetOnlineConfig(
                getApplicationContext().getResources().getString(
                        R.string.channel), new OnlineConfigCallBack() {

                    /**
                     * 获取在线参数成功就会回调本方法（本回调方法执行在UI线程中）
                     */
                    @Override
                    public void onGetOnlineConfigSuccessful(String key,
                                                            String value) {
                        AppCommonPref.getInstance().putHasOpen(
                                Integer.valueOf(value));
                        // DWToast.makeText(
                        // getInstance(),
                        // String.format("在线参数获取结果：\nkey=%s, value=%s",
                        // key, value), Toast.LENGTH_LONG).show();
                    }

                    /**
                     * 获取在线参数失败就会回调本方法（本回调方法执行在UI线程中）
                     */
                    @Override
                    public void onGetOnlineConfigFailed(String key) {

                        // 获取在线参数失败，可能原因有：键值未设置或为空、网络异常、服务器异常
                        // Toast.makeText(
                        // getInstance(),
                        // String.format(
                        // "在线参数获取结果：\n获取在线key=%s失败!\n具体失败原因请查看Log，Log标签：YoumiSdk",
                        // key), Toast.LENGTH_LONG).show();
                    }
                });

    }

}
