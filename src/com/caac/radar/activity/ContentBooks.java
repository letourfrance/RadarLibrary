package com.caac.radar.activity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.bmob.BmobProFile;
import com.caac.radar.MainActivity;
import com.caac.radar.R;
import com.caac.radar.R.layout;
import com.caac.radar.R.menu;
import com.caac.radar.bean.Book;
import com.caac.radar.bean.BorrowBook;
import com.caac.radar.bean.MyUser;
import com.caac.radar.bean.RecordBook;
import com.caac.radar.util.AddBookRecordUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContentBooks extends Activity implements OnClickListener{

	private ImageView iv_con_book;
	private TextView tv_con_name,tv_con_autor,tv_con_addrs,tv_con_claf,tv_con_sta;
	private Button bt_co_born,bt_record;
	private Book book;
	private ImageButton btn_back;
	//获取当前用户
	private	MyUser user,mUser;
	private String BorrowId;
	private String uid1,uid2;
	
	private Boolean isBorn = true;
	/** 
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。 
     */  
    private LruCache<String, BitmapDrawable> mMemoryCache;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.content_books);
		
		initView();
		
		book = (Book) getIntent().getSerializableExtra("book_data");
		user = BmobUser.getCurrentUser(ContentBooks.this, MyUser.class);
		setDatas(book);
	}


	private void setDatas(Book book) {
		// TODO Auto-generated method stub
		if(book.getPic()!=null){
			String url =BmobProFile.getInstance(ContentBooks.this).signURL(book.getPic().getFilename(),book.getPic().getFileUrl(ContentBooks.this),"adfb01134ad0162e6fd5b49c48a7eb88",0,null);
			
	            BitmapWorkerTask task = new BitmapWorkerTask(iv_con_book);  
	            task.execute(url);  
		}
		tv_con_name.setText(book.getName());
		tv_con_autor.setText(book.getAutor());
		tv_con_addrs.setText(book.getAddres());
		tv_con_claf.setText(book.getSort());
		if(book.getIsBorn()){
			tv_con_sta.setText("已借出");
			bt_co_born.setText("还书");
		}else{
			tv_con_sta.setText("未借出");
			
		}
	}


	private void initView() {
		// TODO Auto-generated method stub
		iv_con_book = (ImageView) findViewById(R.id.co_book_img);
		tv_con_name = (TextView) findViewById(R.id.co_book_name);
		tv_con_autor = (TextView) findViewById(R.id.co_book_autor);
		tv_con_addrs = (TextView) findViewById(R.id.co_book_addrs);
		tv_con_claf = (TextView) findViewById(R.id.co_book_sort);
		tv_con_sta = (TextView) findViewById(R.id.co_book_start);
		bt_co_born = (Button) findViewById(R.id.co_book_sure);
		btn_back = (ImageButton) findViewById(R.id.ib_back_co);
		bt_record = (Button) findViewById(R.id.bt_record);
		bt_co_born.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		bt_record.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.co_book_sure:
			
			Toast.makeText(ContentBooks.this, "正在处理...", Toast.LENGTH_SHORT).show();
			
			//判断书本是否借出
			if(book.getIsBorn())
			{
				findTheBorrow();//找到借书用户和借书id
				
			}else{
				//借书成功，修改book信息
				addTheTime(book);
			}
			
			break;
			
		case R.id.ib_back_co:
			finish();
			break;

		case R.id.bt_record:
			
			Intent intent = new Intent(ContentBooks.this, RecordActivity.class);
			//intent.putExtra("book_id", book.getObjectId());
			intent.putExtra("book_record", book);
			startActivity(intent);
			
			break;
			
		default:
			break;
		}
	}

	//结束表中添加数据
	private void addTheBorrow(Book books) {
		// TODO Auto-generated method stub
		
		BorrowBook borrowBook = new BorrowBook();
		borrowBook.setB_name(books);
		borrowBook.setU_name(user);
		borrowBook.setIsBorrow(true);
		borrowBook.setTime(new BmobDate(new Date()));
		borrowBook.save(ContentBooks.this, new SaveListener() {

		    @Override
		    public void onSuccess() {
		        // TODO Auto-generated method stub
		    	//添加记录
				RecordBook recordBook = new RecordBook(new BmobDate(new Date()), "借阅", user.getAddrs()+" "+user.getUsername(),book.getObjectId().toString());
				AddBookRecordUtil.addTherecord(ContentBooks.this, recordBook);
				
		       Toast.makeText(ContentBooks.this, "借阅书籍成功，等待工作人员处理。。。", Toast.LENGTH_SHORT).show();
		       Intent intent = new Intent(ContentBooks.this, MainActivity.class);
		       startActivity(intent);
		       ContentBooks.this.finish();
		    }

			@Override
		    public void onFailure(int code, String msg) {
		        // TODO Auto-generated method stub
		    	Toast.makeText(ContentBooks.this, "借阅书籍失败，请重新借阅", Toast.LENGTH_SHORT).show();
		    }
		});
	}


	//借阅次数+1
	private void addTheTime(Book b) {
		// TODO Auto-generated method stub
		Book book2 = new Book();
		book2.setValue("time", b.getTime()+1);
		book2.setValue("isBorn", true);
		book2.update(ContentBooks.this, b.getObjectId(), new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				
				//重新在数据库中获取book对象
				getThisBook(1);
				
				//后续加上短信功能发到管理员手上
				sendMessage();
				
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(ContentBooks.this, "借阅书籍失败，请重新借阅...", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	//重新在数据库中获取修改后book对象
	private void getThisBook(final int m) {
		// TODO Auto-generated method stub
		BmobQuery<Book> query1 = new BmobQuery<Book>();
		query1.getObject(ContentBooks.this, book.getObjectId(), new GetListener<Book>() {

			@Override
			public void onSuccess(Book arg0) {
				// TODO Auto-generated method stub
				if(m==1){
					//新增借书表
					//判断Borrow表中是否存在该书的借阅信息，否就新增，是就修改不新增。
					if(isHaveBorn(arg0)){
						ChangeTheBookData();
					}else{
						//只是修改Borrow中数据
						addTheBorrow(arg0);
					}
					
				}else
				{
					//还书
					returnBook(arg0);
				}
				
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(ContentBooks.this, "操作", Toast.LENGTH_SHORT).show();
			}
			
		});
	}
	
	//添加还书数据
	private void returnBook(final Book books)
	{
		BorrowBook returnBook = new BorrowBook();
		returnBook.setValue("isBorrow", false);
		returnBook.update(ContentBooks.this, BorrowId, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				//添加记录
				RecordBook recordBook = new RecordBook(new BmobDate(new Date()), "归还", user.getAddrs()+" "+user.getUsername(),books.getObjectId().toString());
				AddBookRecordUtil.addTherecord(ContentBooks.this, recordBook);
				
				Toast.makeText(ContentBooks.this, "归还还书籍成功，等待工作人员处理。。。", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ContentBooks.this, MainActivity.class);
			    startActivity(intent);
			    ContentBooks.this.finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(ContentBooks.this, "归还书籍失败，请重新操作", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	//发短信
	private void sendMessage() {
		// TODO Auto-generated method stub
		
	}
	
	//知道书本查找借书人
	private void findTheBorrow()
	{
		
		BmobQuery<BorrowBook> query = new BmobQuery<BorrowBook>();
		//查询playerName叫“比目”的数据
		query.addWhereEqualTo("b_name", book);
		query.addWhereEqualTo("isBorrow", true);
		//返回50条数据，如果不加上这条语句，默认返回10条数据
		query.include("u_name");
		//query.setLimit(50);
		//执行查询方法
		query.findObjects(this, new FindListener<BorrowBook>() {
		        @Override
		        public void onSuccess(List<BorrowBook> object) {
		            // TODO Auto-generated method stub
		            //toast("查询成功：共"+object.size()+"条数据。");
		        	mUser = object.get(0).getU_name();
		        	BorrowId = object.get(0).getObjectId();
		        	uid1 = mUser.getObjectId().toString();
		        	uid2 = user.getObjectId().toString();
		        	//Toast.makeText(ContentBooks.this, uid1+"和"+uid2, Toast.LENGTH_SHORT).show();
		        	if(uid1.equals(uid2)){
		        		returnTheBook();
		        	}else{
		        		Toast.makeText(ContentBooks.this, "该书籍不是您的，请重新操作...", Toast.LENGTH_SHORT).show();
		        	}
		        }
		        @Override
		        public void onError(int code, String msg) {
		            // TODO Auto-generated method stub
		           // toast("查询失败："+msg);
		        }
		});
		
	}
	
	//还书操作修改isborn
	private void returnTheBook()
	{

		Book book3 = new Book();
		//还书
		book3.setValue("isBorn", false);
		book3.update(ContentBooks.this, book.getObjectId(), new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				getThisBook(0);
				//Toast.makeText(ContentBooks.this, "还书成功，等待工作人员处理。。。", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(ContentBooks.this, "还书失败，请重新操作...", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	//判断Borrow表中是否存在该书的借阅信息，否就新增，是就修改不新增。
	private boolean isHaveBorn(Book book)
	{
		
		BmobQuery<BorrowBook> query = new BmobQuery<BorrowBook>();
		//查询playerName叫“比目”的数据
		query.addWhereEqualTo("b_name", book);
		query.addWhereEqualTo("isBorrow", false);
		//返回50条数据，如果不加上这条语句，默认返回10条数据
		//执行查询方法
		query.findObjects(this, new FindListener<BorrowBook>() {
		        @Override
		        public void onSuccess(List<BorrowBook> object) {
		            // TODO Auto-generated method stub
		            //toast("查询成功：共"+object.size()+"条数据。");
		        	//Toast.makeText(ContentBooks.this, object.toString()+"999"+object.size()+"", Toast.LENGTH_SHORT).show();
		        	if(object.size() <= 0 || object+"" == "[]"){
		        		isBorn = false;
		        	}else{
		        		BorrowId = object.get(0).getObjectId();
		        	}
		        }
		        @Override
		        public void onError(int code, String msg) {
		            // TODO Auto-generated method stub
		           // toast("查询失败："+msg);
		        }
		});
		
		return isBorn;
		
	}
	
	//修改借书数据
	private void ChangeTheBookData()
	{
		BorrowBook setBorrBook = new BorrowBook();
		setBorrBook.setValue("isBorrow", true);
		setBorrBook.setValue("u_name", user);
		setBorrBook.setValue("time", new BmobDate(new Date()));
		setBorrBook.update(ContentBooks.this, BorrowId, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				//添加记录
				RecordBook recordBook = new RecordBook(new BmobDate(new Date()), "借阅", user.getAddrs()+" "+user.getUsername(),book.getObjectId().toString());
				AddBookRecordUtil.addTherecord(ContentBooks.this, recordBook);
				
				Toast.makeText(ContentBooks.this, "借书成功,等待管理员处理", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ContentBooks.this, MainActivity.class);
			    startActivity(intent);
			    ContentBooks.this.finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(ContentBooks.this, "借书失败，请重新操作", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	
//	/** 
//     * 将一张图片存储到LruCache中。 
//     *  
//     * @param key 
//     *            LruCache的键，这里传入图片的URL地址。 
//     * @param drawable 
//     *            LruCache的值，这里传入从网络上下载的BitmapDrawable对象。 
//     */  
//    public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {  
//        if (getBitmapFromMemoryCache(key) == null) {  
//            mMemoryCache.put(key, drawable);  
//        }  
//    }
//    
//    /** 
//     * 从LruCache中获取一张图片，如果不存在就返回null。 
//     *  
//     * @param key 
//     *            LruCache的键，这里传入图片的URL地址。 
//     * @return 对应传入键的BitmapDrawable对象，或者null。 
//     */  
//    public BitmapDrawable getBitmapFromMemoryCache(String key) {  
//        return mMemoryCache.get(key);  
//    }  
    
    /** 
     * 异步下载图片的任务。 
     *  
     * @author guolin 
     */  
    class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {  
  
    	private ImageView mImageView;  
    	  
        public BitmapWorkerTask(ImageView imageView) {  
            mImageView = imageView;  
        }  
  
  
        @Override  
        protected BitmapDrawable doInBackground(String... params) {  
           String imageUrl = params[0];  
            // 在后台开始下载图片  
            Bitmap bitmap = downloadBitmap(imageUrl);  
            BitmapDrawable drawable = new BitmapDrawable(ContentBooks.this.getResources(), bitmap);  
            //addBitmapToMemoryCache(imageUrl, drawable);  
            return drawable;  
        }  
  
        @Override  
        protected void onPostExecute(BitmapDrawable drawable) {  
            if (mImageView != null && drawable != null) {  
                mImageView.setImageDrawable(drawable);  
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
