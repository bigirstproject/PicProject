
package com.sunsun.picproject.http;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.sunsun.picproject.http.KGHttpClient.IUploadListener;


/**
 * 描述:可监听文件上传进度的RequestEntity
 * 
 */
public class CustomMultipartEntity extends MultipartRequestEntity {

    private long mTotalSize;

    private IUploadListener mUploadListener;

    public CustomMultipartEntity(Part[] parts, HttpMethodParams params, long totalSize,
            IUploadListener uploadListener) {
        super(parts, params);
        mTotalSize = totalSize;
        mUploadListener = uploadListener;
    }

    @Override
    public void writeRequest(OutputStream out) throws IOException {
        super.writeRequest(new CountingOutputStream(out, mTotalSize, mUploadListener));
    }
}
