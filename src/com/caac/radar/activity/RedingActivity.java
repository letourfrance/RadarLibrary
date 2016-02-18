package com.caac.radar.activity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.caac.adapter.BooksAdapter;
import com.caac.adapter.RecordAdapter;
import com.caac.radar.R;
import com.caac.radar.R.layout;
import com.caac.radar.R.menu;
import com.caac.radar.bean.Book;
import com.caac.radar.bean.BorrowBook;
import com.caac.radar.bean.MyUser;
import com.caac.radar.fragment.LibraryFragment;
import com.caac.radar.fragment.MyHomeFragment;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RedingActivity extends Activity {

	private ImageButton ib_back;
	private ListView lv_reding;
	private List<Book> listbooks = new ArrayList<Book>();
	private BooksAdapter adapter;
	private MyUser user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.activity_reding);
		//获取当前用户
	    user = BmobUser.getCurrentUser(this, MyUser.class);
	    
	    initDatas();
	    
		lv_reding = (ListView) findViewById(R.id.lv_reding);
		lv_reding.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Book bookss = listbooks.get(arg2);
				Intent intent = new Intent(RedingActivity.this, ContentBooks.class);
				intent.putExtra("book_data", bookss);
				startActivity(intent);
				finish();
			}
		});
		ib_back = (ImageButton) findViewById(R.id.ib_reding_back);
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		BmobQuery<BorrowBook> query = new BmobQuery<BorrowBook>();
		query.addWhereEqualTo("u_name", user);
		query.addWhereEqualTo("isBorrow", true);
		query.include("b_name");
		//返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		//执行查询方法
		query.findObjects(this, new FindListener<BorrowBook>() {
		        @Override
		        public void onSuccess(List<BorrowBook> object) {
		            // TODO Auto-generated method stub
		           
		        	//listbooks = object.get;
		        	for(int i =0; i<=object.size()-1;i++){
		        		listbooks.add(object.get(i).getB_name());
		        	}
		        	adapter = new BooksAdapter(RedingActivity.this, R.layout.item_book, listbooks);
		        	lv_reding.setAdapter(adapter);
		        	adapter.notifyDataSetChanged();
		        	//listbooks.clear();
		            }

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(RedingActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
				}
		        
		        
		});
	}

	

}
