package com.kugou.framework.component.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import org.apache.commons.codec.binary.Base64;

import android.os.AsyncTask;

import com.kugou.framework.component.preference.AppCommonPref;

public abstract class BaseCacheFragment extends BaseFragment {

	protected class SaveDiskCacheTask extends AsyncTask<Void, Void, Boolean> {
		private Object cache;
		private String name;

		public SaveDiskCacheTask(Object cache, String name) {
			this.cache = cache;
			this.name = name;
		}

		@Override
		public Boolean doInBackground(Void... params) {
			if (cache == null) {
				return false;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(cache);
				String oAuth_Base64 = new String(Base64.encodeBase64(baos
						.toByteArray()));
				return AppCommonPref.getInstance()
						.putString(name, oAuth_Base64);
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		public void onPostExecute(Boolean saveCacheSucess) {
			super.onPostExecute(saveCacheSucess);
			saveDisk(saveCacheSucess);
		}
	}

	/**
	 * 保存cache
	 * 
	 * @param saveCacheSucess
	 */
	protected void saveDisk(boolean saveCacheSucess) {

	}

	protected class GetDiskCacheTask extends AsyncTask<Void, Void, Object> {

		private String name;
		private int index;

		public GetDiskCacheTask(String name,int index) {
			this.name = name;
			this.index = index;
		}

		@Override
		public Object doInBackground(Void... params) {
			String data = AppCommonPref.getInstance().getString(name, null);
			if (data == null) {
				return null;
			}
			Object object = null;
			byte[] base64 = Base64.decodeBase64(data.getBytes());
			ByteArrayInputStream bais = new ByteArrayInputStream(base64);
			try {
				ObjectInputStream bis = new ObjectInputStream(bais);
				try {
					object = (Object) bis.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return object;
		}

		@Override
		public void onPostExecute(Object cache) {
			super.onPostExecute(cache);
			getDiskCache(cache,index);
		}

	}

	/**
	 * 获取cache
	 * 
	 * @param saveCacheSucess
	 */
	protected void getDiskCache(Object cache,int index) {

	}

}
