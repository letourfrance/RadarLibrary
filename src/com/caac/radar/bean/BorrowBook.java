package com.caac.radar.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class BorrowBook extends BmobObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Book b_name;
	private MyUser u_name;
	private BmobDate time;
	private Boolean isBorrow;//是否借书，是true，还书flast
	
	
	public Boolean getIsBorrow() {
		return isBorrow;
	}
	public void setIsBorrow(Boolean isBorrow) {
		this.isBorrow = isBorrow;
	}
	public Book getB_name() {
		return b_name;
	}
	public void setB_name(Book b_name) {
		this.b_name = b_name;
	}
	public MyUser getU_name() {
		return u_name;
	}
	public void setU_name(MyUser u_name) {
		this.u_name = u_name;
	}
	public BmobDate getTime() {
		return time;
	}
	public void setTime(BmobDate time) {
		this.time = time;
	}
	
	
	
}
