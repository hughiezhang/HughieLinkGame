package com.hughie.linkgame.entity;

import java.io.Serializable;

/**
 * 为了更好地操作sidebar而定义的一个entity
 * @ClassName: DABLInfoYearList
 * @author hughiezhang
 * @since 2015-01-19 09:58
 */
public class ScreenDesktopSidebarDetail implements Serializable {
	private static final long serialVersionUID = -7229578702809795371L;
	
	private int sidebarImgv;
	private String sidebarName;
	
	public int getSidebarImgv() {
		return sidebarImgv;
	}
	
	public void setSidebarImgv(int sidebarImgv) {
		this.sidebarImgv = sidebarImgv;
	}
	
	public String getSidebarName() {
		return sidebarName;
	}
	
	public void setSidebarName(String sidebarName) {
		this.sidebarName = sidebarName;
	}

	@Override
	public String toString() {
		return "ScreenDesktopSidebarDetail [sidebarImgv=" + sidebarImgv
				+ ", sidebarName=" + sidebarName + "]";
	}
}
