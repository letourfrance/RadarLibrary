package com.caac.radar.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ValueEventListener;

import com.caac.adapter.BooksAdapter;
import com.caac.radar.MainActivity;
import com.caac.radar.R;
import com.caac.radar.activity.AddBook;
import com.caac.radar.activity.ContentBooks;
import com.caac.radar.bean.Book;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

public class LibraryFragment extends Fragment {

	public View view;
	
	private ListView lv_book;
	private List<Book> booksList = new ArrayList<Book>();
	private BooksAdapter booksAdapter;
	BmobRealTimeData rtd = new BmobRealTimeData();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
        view = inflater.inflate(R.layout.fg_library,container,false);
		
        lv_book = (ListView) view.findViewById(R.id.lv_book);
        
        getTheDatas();
        
        
        updataDatas();
        lv_book.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Book bookss = booksList.get(arg2);
				Intent intent = new Intent(LibraryFragment.this.getActivity(), ContentBooks.class);
				intent.putExtra("book_data", bookss);
				LibraryFragment.this.startActivity(intent);
			}
		});
        
        
		return view;
	}

	private void updataDatas() {
		// TODO Auto-generated method stub
		rtd.start(LibraryFragment.this.getActivity(), new ValueEventListener() {
		    @Override
		    public void onDataChange(JSONObject data) {
		        // TODO Auto-generated method stub
		       // Log.d("bmob", "("+data.optString("action")+")"+"数据："+data);
		    	//Log.d("TAG", "("+data.optString("action")+")"+"数据："+data);
		    	Gson gson = new Gson();
		    	Book book = gson.fromJson(data.toString(), Book.class);
		    	booksList.add(book);
		    	Collections.reverse(booksList);
		    	booksAdapter.notifyDataSetChanged();
		    }

		    @Override
		    public void onConnectCompleted() {
		        // TODO Auto-generated method stub
		        //Log.d("bmob", "连接成功:"+rtd.isConnected());
		    }
		});
	}

	//从服务器端获取书籍信息
	private void getTheDatas() {
		// TODO Auto-generated method stub
		BmobQuery<Book> query = new BmobQuery<Book>();
		//query.addWhereEqualTo("age", 25);
		query.setLimit(100);
		query.order("createdAt");
//		//判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
//		boolean isCache = query.hasCachedResult(LibraryFragment.this.getActivity(),Book.class);
//		if(isCache){
//		    query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//		}else{
//		    query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//		}
		query.findObjects(LibraryFragment.this.getActivity(), new FindListener<Book>() {

		    @Override
		    public void onSuccess(List<Book> object) {
		        // TODO Auto-generated method stub
		        //Log.i("smile","查询个数："+object.size())
		    	//Toast.makeText(LibraryFragment.this.getActivity(), object.size()+"", Toast.LENGTH_SHORT).show();
		    	booksList = object;
		    	Collections.reverse(booksList);
		    	booksAdapter = new BooksAdapter(LibraryFragment.this.getActivity(), R.layout.item_book, booksList);
		        lv_book.setAdapter(booksAdapter);
		        booksAdapter.notifyDataSetChanged();
		    }

		    @Override
		    public void onError(int code, String msg) {
		        // TODO Auto-generated method stub
		       // Log.i("smile","查询失败："+object.size())
		    	Toast.makeText(LibraryFragment.this.getActivity(), "获取服务器数据失败", Toast.LENGTH_SHORT).show();
		    }
		});
	}

	

}
