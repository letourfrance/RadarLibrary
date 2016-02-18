package com.caac.radar.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.FindListener;

import com.caac.adapter.BooksAdapter;
import com.caac.adapter.BooksSortAdapter;
import com.caac.radar.R;
import com.caac.radar.activity.ContentBooks;
import com.caac.radar.bean.Book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SortFragment extends Fragment {

	public View view;
    private ListView lv_s_book;
    private List<Book> booksList = new ArrayList<Book>();
	private BooksSortAdapter booksAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
        view = inflater.inflate(R.layout.fg_sort,null);
        lv_s_book = (ListView) view.findViewById(R.id.lv_sort_book);
        
        initData();
        
        lv_s_book.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Book books = booksList.get(arg2);
				Intent intent = new Intent(SortFragment.this.getActivity(), ContentBooks.class);
				intent.putExtra("book_data", books);
				SortFragment.this.startActivity(intent);
			}
		});
		return view;
	}

	private void initData() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				BmobQuery<Book> query = new BmobQuery<Book>();
				//query.addWhereEqualTo("age", 25);
				query.setLimit(100);
				query.order("time,createdAt");
				//query.order("-score,createdAt");
				//判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
//				boolean isCache = query.hasCachedResult(SortFragment.this.getActivity(),Book.class);
//				if(isCache){
//				    query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//				}else{
//				    query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//				}
				query.findObjects(SortFragment.this.getActivity(), new FindListener<Book>() {

				    @Override
				    public void onSuccess(List<Book> object) {
				        // TODO Auto-generated method stub
				        //Log.i("smile","查询个数："+object.size())
				    	//Toast.makeText(SortFragment.this.getActivity(), object.size()+"", Toast.LENGTH_SHORT).show();
				    	booksList = object;
				    	Collections.reverse(booksList);
				    	booksAdapter = new BooksSortAdapter(SortFragment.this.getActivity(), R.layout.item_sort_books, booksList);
				        lv_s_book.setAdapter(booksAdapter);
				        booksAdapter.notifyDataSetChanged();
				    }

				    @Override
				    public void onError(int code, String msg) {
				        // TODO Auto-generated method stub
				       // Log.i("smile","查询失败："+object.size())
				    	Toast.makeText(SortFragment.this.getActivity(), "获取服务器数据失败", Toast.LENGTH_SHORT).show();
				    }
				});
			}
	}


