package com.caac.radar.bean;

import java.io.Serializable;

import com.zhy.tree.bean.TreeNodeId;
import com.zhy.tree.bean.TreeNodeLabel;
import com.zhy.tree.bean.TreeNodePid;

public class BookBean implements Serializable{

	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		@TreeNodeId  
	    private int _id;  
	    @TreeNodePid  
	    private int parentId;  
	    @TreeNodeLabel  
	    private String name;  
	    private long length;  
	    private String desc;
	    private Book book;
	    
		public BookBean(int _id, int parentId, String name,Book book) {
			super();
			this._id = _id;
			this.parentId = parentId;
			this.name = name;
			this.book = book;
		}

		public Book getBook() {
			return book;
		}

		public void setBook(Book book) {
			this.book = book;
		}
		
		
}
