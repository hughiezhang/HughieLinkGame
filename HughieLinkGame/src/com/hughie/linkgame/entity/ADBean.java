package com.hughie.linkgame.entity;

import java.io.Serializable;

/**
 * 游戏广告信息实体
 * @ClassName:  ADBean
 * @author hughiezhang
 * @since 2015-02-02 17:49
 */
public class ADBean implements Serializable {
	private static final long serialVersionUID = 3300169346565210340L;
	
	private int postId;
	private int cricleId;
	private String title;
	private String imageUrl;
	
	public ADBean() {
		super();
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public int getCricleId() {
		return cricleId;
	}

	public void setCricleId(int cricleId) {
		this.cricleId = cricleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "ADBean [postId=" + postId + ", cricleId=" + cricleId
				+ ", title=" + title + ", imageUrl=" + imageUrl + "]";
	}
}
