package com.caac.radar.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class RecordBook extends BmobObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String r_bookId;
	
	private BmobDate r_time;
	
	private String r_start;
	
	private String r_peoadd;

	public BmobDate getR_time() {
		return r_time;
	}

	public void setR_time(BmobDate r_time) {
		this.r_time = r_time;
	}

	public String getR_start() {
		return r_start;
	}

	public void setR_start(String r_start) {
		this.r_start = r_start;
	}

	public String getR_peoadd() {
		return r_peoadd;
	}

	public void setR_peoadd(String r_peoadd) {
		this.r_peoadd = r_peoadd;
	}


	public String getR_bookId() {
		return r_bookId;
	}

	public void setR_bookId(String r_bookId) {
		this.r_bookId = r_bookId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public RecordBook(BmobDate r_time, String r_start, String r_peoadd, String r_bookid) {
		super();
		this.r_time = r_time;
		this.r_start = r_start;
		this.r_peoadd = r_peoadd;
		this.r_bookId = r_bookid;
	}
	
public RecordBook()
{
	
}
	
}
