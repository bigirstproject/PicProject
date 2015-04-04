
package com.sunsun.picproject.http;

import java.util.Hashtable;
import java.util.Set;

import com.kugou.framework.component.base.PicProjectApplication;
import com.sunsun.picproject.http.NetWorkUtil.NetworkType;

/**
 * 抽象请求包
 * 
 */
public abstract class AbstractRequestPackage implements RequestPackage {

    protected Hashtable<String, Object> mParams;

    @Override
    public String getGetRequestParams() {
        if (mParams != null && mParams.size() >= 0) {
            StringBuilder builder = new StringBuilder();
            builder.append("/");
            final Set<String> keys = mParams.keySet();
            for (String key : keys) {
                builder.append(key).append("=").append(mParams.get(key)).append("&");
            }
            builder.deleteCharAt(builder.length() - 1);
            return builder.toString();
        }
        return "";
    }

    public Hashtable<String, Object> getParams() {
        return mParams;
    }

    public void setParams(Hashtable<String, Object> mParams) {
        this.mParams = mParams;
    }

    @Override
    public Hashtable<String, Object> getSettings() {
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        String networkType = NetWorkUtil.getNetworkType(PicProjectApplication.getInstance());
        if (NetworkType.WIFI.equals(networkType)) {
            params.put("conn-timeout", 15 * 1000);
            params.put("socket-timeout", 15 * 1000);
        } else {
            params.put("conn-timeout", 20 * 1000);
            params.put("socket-timeout", 20 * 1000);
        }
        return params;
    }
}
