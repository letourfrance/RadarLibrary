package com.caac.adapter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.bmob.BmobProFile;
import com.caac.radar.R;
import com.caac.radar.bean.Book;
import com.caac.radar.util.GetImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BooksSortAdapter extends ArrayAdapter<Book> {

	private int resourceId;
	private ListView mListView;
	/** 
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。 
     */  
    private LruCache<String, BitmapDrawable> mMemoryCache;  
    
	public BooksSortAdapter(Context context, int resource, List<Book> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
		 
		// 获取应用程序最大可用内存  
        int maxMemory = (int) Runtime.getRuntime().maxMemory();  
        int cacheSize = maxMemory / 8;  
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {  
            @Override  
            protected int sizeOf(String key, BitmapDrawable drawable) {  
                return drawable.getBitmap().getByteCount();  
            }  
        };  
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (mListView == null) {    
            mListView = (ListView) parent;    
        }
		Book book = getItem(position);
		
        View view;
		ViewHolder viewHolder;
		
		if(convertView == null)
		{
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_book_s = (ImageView) view.findViewById(R.id.iv_books_so);
			viewHolder.tv_name_s = (TextView) view.findViewById(R.id.tv_book_name_so);
			viewHolder.tv_autor_s = (TextView) view.findViewById(R.id.tv_auto_so);
			viewHolder.tv_addrs_s = (TextView) view.findViewById(R.id.tv_adds_so);
			viewHolder.tv_stat_s = (TextView) view.findViewById(R.id.tv_stats_so);
			viewHolder.tv_sort_s = (TextView) view.findViewById(R.id.tv_sort);
			viewHolder.tv_time_s = (TextView) view.findViewById(R.id.tv_b_time);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		
		if(book.getPic()!=null){
			
			String url =BmobProFile.getInstance(getContext()).signURL(book.getPic().getFilename(),book.getPic().getFileUrl(getContext()),"adfb01134ad0162e6fd5b49c48a7eb88",0,null);
			viewHolder.iv_book_s.setTag(url);
			BitmapDrawable drawable = getBitmapFromMemoryCache(url);
			if (drawable != null) {  
				viewHolder.iv_book_s.setImageDrawable(drawable);  
	        } else {  
	            BitmapWorkerTask task = new BitmapWorkerTask();  
	            task.execute(url);  
	        } 
			
		}
		viewHolder.tv_name_s.setText(book.getName());
		viewHolder.tv_autor_s.setText(book.getAutor());
		viewHolder.tv_addrs_s.setText(book.getAddres());
		if(book.getIsBorn()){
			viewHolder.tv_stat_s.setText("借出");
		}else{
			viewHolder.tv_stat_s.setText("未借出");
		}
		viewHolder.tv_sort_s.setText(position+1+"");
		viewHolder.tv_time_s.setText(book.getTime()+"");
		return view;
	}
	
	
	class ViewHolder
	{
		ImageView iv_book_s;
		TextView tv_name_s;
		TextView tv_autor_s;
		TextView tv_addrs_s;
	    TextView tv_stat_s;
	    TextView tv_sort_s;
	    TextView tv_time_s;
	}
	
	 /** 
     * 将一张图片存储到LruCache中。 
     *  
     * @param key 
     *            LruCache的键，这里传入图片的URL地址。 
     * @param drawable 
     *            LruCache的值，这里传入从网络上下载的BitmapDrawable对象。 
     */  
    public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {  
        if (getBitmapFromMemoryCache(key) == null) {  
            mMemoryCache.put(key, drawable);  
        }  
    }
    
    /** 
     * 从LruCache中获取一张图片，如果不存在就返回null。 
     *  
     * @param key 
     *            LruCache的键，这里传入图片的URL地址。 
     * @return 对应传入键的BitmapDrawable对象，或者null。 
     */  
    public BitmapDrawable getBitmapFromMemoryCache(String key) {  
        return mMemoryCache.get(key);  
    }  
    
    /** 
     * 异步下载图片的任务。 
     *  
     * @author guolin 
     */  
    class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {  
  
        private String imageUrl; 
  
  
        @Override  
        protected BitmapDrawable doInBackground(String... params) {  
            imageUrl = params[0];  
            // 在后台开始下载图片  
            Bitmap bitmap = downloadBitmap(imageUrl);  
            BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);  
            addBitmapToMemoryCache(imageUrl, drawable);  
            return drawable;  
        }  
  
        @Override  
        protected void onPostExecute(BitmapDrawable drawable) {  
            ImageView imageView = (ImageView) mListView.findViewWithTag(imageUrl);    
            if (imageView != null && drawable != null) {    
                imageView.setImageDrawable(drawable);    
            }   
        }  
        /** 
         * 建立HTTP请求，并获取Bitmap对象。 
         *  
         * @param imageUrl 
         *            图片的URL地址 
         * @return 解析后的Bitmap对象 
         */  
        private Bitmap downloadBitmap(String imageUrl) {  
            Bitmap bitmap = null;  
            HttpURLConnection con = null;  
            try {  
                URL url = new URL(imageUrl);  
                con = (HttpURLConnection) url.openConnection();  
                con.setConnectTimeout(5 * 1000);  
                con.setReadTimeout(10 * 1000);  
                bitmap = BitmapFactory.decodeStream(con.getInputStream());  
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally {  
                if (con != null) {  
                    con.disconnect();  
                }  
            }  
            return bitmap;  
        }  
  
    } 
	
}
