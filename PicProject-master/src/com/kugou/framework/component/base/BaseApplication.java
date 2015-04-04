
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

    protected static BaseApplication mApplication;

    // 国家
    protected String country = "中国";

    // 省份
    protected String province;

    // 城市
    protected String city;

    // 街道
    protected String road;

    // 经度
    protected double longitude = 360.0;

    // 纬度
    protected double latitude = 360.0;

    // 应用全局变量存储在这里
    private static Hashtable<String, Object> mAppParamsHolder;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppParamsHolder = new Hashtable<String, Object>();
    }

    /**
     * 获取Application实例
     * 
     * @return
     */
    public static BaseApplication getInstance() {
        if (mApplication == null) {
            throw new IllegalStateException("Application is not created.");
        }
        return mApplication;
    }

    /**
     * 存储全局数据
     * 
     * @param key
     * @param value
     */
    public void putValue(String key, Object value) {
        mAppParamsHolder.put(key, value);
    }

    /**
     * 获取全局数据
     * 
     * @param key
     * @return
     */
    public Object getValue(String key) {
        return mAppParamsHolder.get(key);
    }

    /**
     * 是否已存放
     * 
     * @param key
     * @return
     */
    public boolean containsKey(String key) {
        return mAppParamsHolder.containsKey(key);
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
