package com.caac.radar.bean;


import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Book extends BmobObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String autor;
	
	private String addres;
	
	private Integer time;//次数
	
	private BmobFile pic;
	
	private Boolean isBorn;//状态
	
	private String sort;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getAddres() {
		return addres;
	}

	public void setAddres(String addres) {
		this.addres = addres;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public BmobFile getPic() {
		return pic;
	}

	public void setPic(BmobFile pic) {
		this.pic = pic;
	}

	public Boolean getIsBorn() {
		return isBorn;
	}

	public void setIsBorn(Boolean isBorn) {
		this.isBorn = isBorn;
	}

	public Book(String name, String autor, String addres, Integer time,
			BmobFile pic, Boolean isBorn, String sort) {
		super();
		this.name = name;
		this.autor = autor;
		this.addres = addres;
		this.time = time;
		this.pic = pic;
		this.isBorn = isBorn;
		this.sort = sort;
	}

	public Book() {
		// TODO Auto-generated constructor stub
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
}
