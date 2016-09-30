package com.sunsun.picproject.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dexian on 2016/5/5.
 */
public class CommonUtil {


    /**
     * get density
     *
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * get scale density
     *
     * @param context
     * @return
     */
    public static float getScaleDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * get width pixels
     *
     * @param context
     * @return
     */
    public static float getWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * get height pixels
     *
     * @param context
     * @return
     */
    public static float getHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 检查 SDCard 是否装载
     */
    public static boolean isSDCardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * get device id
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context
                .getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    /**
     * SD卡剩余空间大小
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getSDFreeSize() {
        if (!isSDCardMounted()) {
            return 0;
        }
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize();
        long freeBlocks = sf.getAvailableBlocks();
        return (freeBlocks * blockSize);
    }

    /**
     * SD卡总容量
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getSDAllSize() {
        if (!isSDCardMounted()) {
            return 0;
        }
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize();
        long allBlocks = sf.getBlockCount();
        return (allBlocks * blockSize);
    }

    /**
     * 检查网络状态
     */
    public static boolean checkNetworkState(Context context) {
        boolean connected = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            connected = true;
        } else if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
            connected = true;
        }
        return connected;
    }

    /**
     * 网络状态是否是wifi
     */
    public static boolean isWifiNetworkState(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            return true;
        }
        return false;
    }

    /**
     * 获取版本号
     *
     * @return app_version
     */
    public static String getAppVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        String version = "1.0";
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取版本号
     *
     * @return app_versionCode
     */
    public static int getAppVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        int versionCode = 100;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 当前进程name
     *
     * @return
     */
    private static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        if (infos != null && infos.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo appProcess : infos) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        }
        return "";
    }


    /**
     * 获取package
     *
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * get androidmenifest matadata
     *
     * @param key
     * @return
     */
    public static String getMetaValue(Context context, String key) {
        if (key == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (ai != null && ai.metaData != null) {
                return ai.metaData.getString(key);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }


    /**
     * 生成请求用的user-agent
     *
     * @return
     */
    public static String generateUserAgent(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("supermaket/").append(getAppVersion(context)).append(" (");
        stringBuilder.append("linux;android ")
                .append(android.os.Build.VERSION.RELEASE).append(";");
        stringBuilder.append(Locale.getDefault().getLanguage()).append("-")
                .append(Locale.getDefault().getCountry()).append(";");
        stringBuilder.append(android.os.Build.MODEL).append(";)");
        return stringBuilder.toString();
    }


    /**
     * 是否是手机号码
     *
     * @param phone
     * @return
     */
    public static boolean isMobilePhone(String phone) {
        Pattern pattern = Pattern
                .compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[6-8])|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }


}
