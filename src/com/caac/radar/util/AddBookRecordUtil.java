package com.caac.radar.util;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

import com.caac.radar.MainActivity;
import com.caac.radar.activity.ContentBooks;
import com.caac.radar.bean.BorrowBook;
import com.caac.radar.bean.RecordBook;

public class AddBookRecordUtil {

	public static void addTherecord(final Context context,RecordBook recordBook)
	{
		RecordBook recoBook = new RecordBook();
		recoBook.setR_time(new BmobDate(new Date()));
		recoBook.setR_start(recordBook.getR_start());
		recoBook.setR_peoadd(recordBook.getR_peoadd());
		recoBook.setR_bookId(recordBook.getR_bookId());
		recoBook.save(context, new SaveListener() {

		    @Override
		    public void onSuccess() {
		        // TODO Auto-generated method stub
		    	Toast.makeText(context, "记录成功", Toast.LENGTH_SHORT).show();
		      
		    }

			@Override
		    public void onFailure(int code, String msg) {
		        // TODO Auto-generated method stub
		    	Toast.makeText(context, "记录失败", Toast.LENGTH_SHORT).show();
		    }
		});
	}
}
