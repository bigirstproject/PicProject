package com.sunsun.picproject.bean;

import java.util.List;

public class MainPagerTable extends commonTable {

	private static final long serialVersionUID = 7232503367963356115L;
	private String title;
	private List<GalleryTable> galleryTwo;
	private List<GalleryTable> galleryThree;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<GalleryTable> getGalleryTwo() {
		return galleryTwo;
	}

	public void setGalleryTwo(List<GalleryTable> galleryTwo) {
		this.galleryTwo = galleryTwo;
	}

	public List<GalleryTable> getGalleryThree() {
		return galleryThree;
	}

	public void setGalleryThree(List<GalleryTable> galleryThree) {
		this.galleryThree = galleryThree;
	}

}
