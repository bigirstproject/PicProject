package com.sunsun.picproject.http;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.sunsun.picproject.http.KGHttpClient.IUploadListener;

/**
 * 描述:能监听上传进度的OutputStream
 * 
 */
class CountingOutputStream extends FilterOutputStream {

	private long mUploadSize;

	private long mTotalSize;

	private IUploadListener mUploadListener;

	public CountingOutputStream(OutputStream out, long totalSize,
			IUploadListener uploadListener) {
		super(out);
		mTotalSize = totalSize;
		mUploadListener = uploadListener;
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		mUploadSize += length;
		super.write(buffer, offset, length);
		if (mUploadListener != null) {
			mUploadListener.onProgressChanged(getProgress(mUploadSize,
					mTotalSize));
		}
	}

	@Override
	public void write(int oneByte) throws IOException {
		mUploadSize++;
		super.write(oneByte);
		if (mUploadListener != null) {
			mUploadListener.onProgressChanged(getProgress(mUploadSize,
					mTotalSize));
		}
	}

	private int getProgress(long haveRead, long totalSize) {
		int progress = 0;
		if (totalSize > 0) {
			progress = (int) (haveRead / (float) totalSize * 100);
			if (progress > 100) {
				progress = 100;
			}
		}
		return progress;
	}

}
