
package com.sunsun.picproject.http;

import com.kugou.framework.component.base.IEntity;

/**
 * 描述:通用http响应类
 * 
 */
public class HttpResponse implements IEntity {

    // 结果是否正确
    private boolean isOk;

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean isOk) {
        this.isOk = isOk;
    }

}
