package com.sunsun.slidingmenu.bean;

import java.io.Serializable;

public class commonTable implements Serializable{

	private static final long serialVersionUID = -8163911414009031997L;
	
	private int id;
	
	private long createTime;
	
	private long updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
	

}
