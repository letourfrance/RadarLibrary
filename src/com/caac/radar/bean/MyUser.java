package com.caac.radar.bean;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser{

	/**
	 * 2016-1-6
	 * 用户类继承BmobUser
	 */
	private static final long serialVersionUID = 1L;

	private String addrs;

	public String getAddrs() {
		return addrs;
	}

	public void setAddrs(String addrs) {
		this.addrs = addrs;
	}
	
}
