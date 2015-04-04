package com.sunsun.slidingmenu.bean;

import java.util.List;

public class MainPagerTable extends commonTable {

	private static final long serialVersionUID = 7232503367963356115L;
	public static final int ONE_STYLE = 0;
	public static final int TWO_STYLE = 1;
	public static final int THREE_STYLE = 2;
	private int type;
	private String title;
	private List<GalleryItemTable> galleryTwo;
	private List<GalleryItemTable> galleryThree;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<GalleryItemTable> getGalleryTwo() {
		return galleryTwo;
	}

	public void setGalleryTwo(List<GalleryItemTable> galleryTwo) {
		this.galleryTwo = galleryTwo;
	}

	public List<GalleryItemTable> getGalleryThree() {
		return galleryThree;
	}

	public void setGalleryThree(List<GalleryItemTable> galleryThree) {
		this.galleryThree = galleryThree;
	}

}
