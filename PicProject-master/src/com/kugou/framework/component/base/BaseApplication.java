
package com.kugou.framework.component.base;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import java.util.Hashtable;

/**
 * 描述:全局Application
 * 
 * @author chenys
 * @since 2013-8-7 下午6:16:10
 */
public abstract class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 获取App安装包信息
     * 
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

}
