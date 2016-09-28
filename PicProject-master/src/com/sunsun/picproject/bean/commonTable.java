package com.sunsun.picproject.bean;

import java.io.Serializable;

public class commonTable implements Serializable {

	private static final long serialVersionUID = -8163911414009031997L;

	public static final int TYPE_ONE = 0;
	public static final int TYPE_TWO = 1;
	public static final int TYPE_THREE = 2;

	private int id;

	private int type;

	private long createTime;

	private long updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
