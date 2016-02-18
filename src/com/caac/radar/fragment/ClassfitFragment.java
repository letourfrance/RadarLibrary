package com.caac.radar.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.FindListener;

import com.caac.adapter.SimpleTreeAdapter;
import com.caac.radar.R;
import com.caac.radar.activity.ContentBooks;
import com.caac.radar.bean.Book;
import com.caac.radar.bean.BookBean;
import com.zhy.tree.bean.TreeListViewAdapter;

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

public class ClassfitFragment extends Fragment {
	
	public View view;
	private List<BookBean> mDatas = new ArrayList<BookBean>();  
    private ListView mTree;  
    private TreeListViewAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fg_classifc,null);
		mTree = (ListView) view.findViewById(R.id.cf_lv);
		initDatas();
		
			try  
	        {  
	              
	            mAdapter = new SimpleTreeAdapter<BookBean>(mTree, ClassfitFragment.this.getActivity(), mDatas, 100);  
	            mTree.setAdapter(mAdapter);
	            mAdapter.notifyDataSetChanged();
//	            mTree.setOnItemClickListener(new OnItemClickListener(){
//
//					@Override
//					public void onItemClick(AdapterView<?> arg0, View arg1,
//							int arg2, long arg3) {
//						// TODO Auto-generated method stub
//						BookBean boo = mDatas.get(arg2);
//						if(boo.getBook()!=null){
//							Intent intent = new Intent(ClassfitFragment.this.getActivity(), ContentBooks.class);
//							intent.putExtra("book_data", boo.getBook());
//							ClassfitFragment.this.startActivity(intent);
//						}
//						
//					}
//					
//				});
	            mDatas.clear();
	        } catch (IllegalAccessException e)  
	        {  
	            e.printStackTrace();  
	        }  
		
			
		
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	private void initDatas() {
		// TODO Auto-generated method stub
		mDatas.add(new BookBean(1, 0, "雷达书库",null));
		mDatas.add(new BookBean(2, 1, "工作类",null));
		mDatas.add(new BookBean(3, 1, "科技类",null));
		mDatas.add(new BookBean(4, 1, "人文类",null));
		getBookDatas();
	}

	//从服务器端获取书籍信息
		private void getBookDatas() {
			// TODO Auto-generated method stub
			BmobQuery<Book> query = new BmobQuery<Book>();
			//query.addWhereEqualTo("sort", "科技类");
			query.setLimit(100);
			query.order("createdAt");
			//判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
//			boolean isCache = query.hasCachedResult(ClassfitFragment.this.getActivity(),Book.class);
//			if(isCache){
//			    query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//			}else{
//			    query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//			}
			query.findObjects(ClassfitFragment.this.getActivity(), new FindListener<Book>() {

			    @Override
			    public void onSuccess(List<Book> object) {
			        // TODO Auto-generated method stub
			        //Log.i("smile","查询个数："+object.size())
			    	//Toast.makeText(ClassfitFragment.this.getActivity(), object.size()+"", Toast.LENGTH_SHORT).show();
			    	//booksList = object;
			    	for(int i = 0 ; i <= object.size()-1 ; i++){
			    		if(object.get(i).getSort().equals("科技类")){
			    			mDatas.add(new BookBean(i+5, 3, object.get(i).getName(),object.get(i)));
			    		}else if(object.get(i).getSort().equals("人文类")){
			    			mDatas.add(new BookBean(i+5, 4, object.get(i).getName(),object.get(i)));
			    		}else{
			    			mDatas.add(new BookBean(i+5, 2, object.get(i).getName(),object.get(i)));
			    		}
			    	}
			    	
			    }

			    @Override
			    public void onError(int code, String msg) {
			        // TODO Auto-generated method stub
			       // Log.i("smile","查询失败："+object.size())
			    	Toast.makeText(ClassfitFragment.this.getActivity(), "获取服务器数据失败", Toast.LENGTH_SHORT).show();
			    }
			});
		}
	
}
