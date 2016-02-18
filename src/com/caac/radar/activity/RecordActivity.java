package com.caac.radar.activity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.caac.adapter.RecordAdapter;
import com.caac.radar.R;
import com.caac.radar.R.layout;
import com.caac.radar.R.menu;
import com.caac.radar.bean.Book;
import com.caac.radar.bean.BorrowBook;
import com.caac.radar.bean.RecordBook;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordActivity extends Activity implements OnClickListener{

	private ListView lv_record;
	private ImageButton bt_back_reco;
	private TextView tv_book_name;
	private Book booksData;
	private List<RecordBook> listDatas = new ArrayList<RecordBook>();
	private RecordAdapter adapter;
	private String bid; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.activity_record);
		initView();
		booksData = (Book) getIntent().getSerializableExtra("book_record");
		bid = booksData.getObjectId();
		tv_book_name.setText(booksData.getName());
		initDatas();
	}
	
	private void initDatas() {
		// TODO Auto-generated method stub
		BmobQuery<RecordBook> query = new BmobQuery<RecordBook>();
		query.addWhereEqualTo("r_bookId", bid);
		//query.include("u_name");
		//返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		//执行查询方法
		query.findObjects(this, new FindListener<RecordBook>() {
		        @Override
		        public void onSuccess(List<RecordBook> object) {
		            // TODO Auto-generated method stub
		        	listDatas = object;
		        	adapter = new RecordAdapter(RecordActivity.this, R.layout.item_record, listDatas);
		        	lv_record.setAdapter(adapter);
		        	adapter.notifyDataSetChanged();
		            }

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
		        
		        
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		lv_record = (ListView) findViewById(R.id.lv_record);
		bt_back_reco = (ImageButton) findViewById(R.id.reco_ib_back);
		tv_book_name = (TextView) findViewById(R.id.reco_tv_name);
		bt_back_reco.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.reco_ib_back:
			finish();
			break;

		default:
			break;
		}
	}


}
