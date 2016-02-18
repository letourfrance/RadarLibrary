package com.caac.radar.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.caac.radar.MainActivity;
import com.caac.radar.R;
import com.caac.radar.R.layout;
import com.caac.radar.R.menu;
import com.caac.radar.bean.Book;
import com.caac.radar.bean.BookDouBan;
import com.caac.radar.bean.BorrowBook;
import com.caac.radar.bean.MyUser;
import com.caac.radar.bean.RecordBook;
import com.caac.radar.util.AddBookRecordUtil;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddBook extends Activity implements OnClickListener{

	private BmobFile bmobFile;
	private static int RESULT_LOAD_IMAGE = 1;
	private ImageView iv_addbook;
	private EditText et_add_name,et_add_autor,et_add_addrs,et_sort;
	private Button bt_add_book;
	private ImageButton ib_back;
	private Book books;
	
	private String filePath = null;
	private BookDouBan bookDatas;
	private MyUser mUser;
	
	private String bookId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.layout_add_books);
		setView();
		
		bookDatas = (BookDouBan) getIntent().getParcelableExtra("books_data");
		mUser = BmobUser.getCurrentUser(AddBook.this, MyUser.class);
		if(bookDatas!=null){
			iv_addbook.setImageBitmap(bookDatas.getBitmap());
			et_add_name.setText(bookDatas.getTitle());
			et_add_autor.setText(bookDatas.getAuthor());
			saveMyBitmap(bookDatas.getBitmap());
			filePath = "/sdcard/temp.png";
		}
		
	}

	private void setView() {
		// TODO Auto-generated method stub
		iv_addbook = (ImageView) findViewById(R.id.add_book_img);
		et_add_name = (EditText) findViewById(R.id.add_book_name);
		et_add_autor = (EditText) findViewById(R.id.add_book_autor);
		et_add_addrs = (EditText) findViewById(R.id.add_book_addrs);
		et_sort = (EditText) findViewById(R.id.add_book_sort);
		bt_add_book = (Button) findViewById(R.id.add_book_sure);
		ib_back = (ImageButton) findViewById(R.id.ib_back_add);
		bt_add_book.setOnClickListener(this);
		iv_addbook.setOnClickListener(this);
		ib_back.setOnClickListener(this);
	}
	
	private Book getBookDatas(){
		
		
		books = new Book(et_add_name.getText().toString(), et_add_autor.getText().toString(), et_add_addrs.getText().toString(), 0, null, false,et_sort.getText().toString());
		
		return books;
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add_book_sure:
			
			getBookDatas();
			
			if(books.getName()==null||books.getName().isEmpty()||books.getAutor()==null||books.getAutor().isEmpty()||books.getAddres()==null||books.getAddres().isEmpty())
			{
				Toast.makeText(AddBook.this, "请填写完整书籍信息！", Toast.LENGTH_SHORT).show();
			}else
			{
				Toast.makeText(AddBook.this, "正在处理，请稍等...", Toast.LENGTH_SHORT).show();
				//上传书籍图片
				if(filePath != null )
				{
					bmobFile = new BmobFile(new File(filePath));
					books.setPic(bmobFile);
					
					bmobFile.upload(this, new UploadFileListener() {

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							
							insertObject(books);
							
//							Toast.makeText(AddBook.this, "添加书籍成功", Toast.LENGTH_SHORT).show();
//							Intent intent = new Intent(AddBook.this, MainActivity.class);
//							AddBook.this.startActivity(intent);
//							AddBook.this.finish();
						}

						@Override
						public void onProgress(Integer arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(AddBook.this, "添加书籍失败，请重新上传", Toast.LENGTH_SHORT).show();
						}

					});
				}
				else{
					insertObject(books);
				}
				
			}
			
			
			break;

		case R.id.add_book_img:
			
			Intent intent = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent,RESULT_LOAD_IMAGE);
			
			break;
			
		case R.id.ib_back_add:
			finish();
			break;
			
		default:
			
			break;
		}
	}

	/*
	 * 
	 * 选中了一张图片返回
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub	
		
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			filePath= cursor.getString(columnIndex);
			cursor.close();
			//fileName = picturePath.substring(picturePath.lastIndexOf("/")+1,picturePath.length());
			iv_addbook.setImageBitmap(BitmapFactory.decodeFile(filePath));
			
		}
    
	}
	
	private void insertObject(final BmobObject obj){
	    obj.save(AddBook.this, new SaveListener() {

	        @Override
	        public void onSuccess() {
	            // TODO Auto-generated method stub
	        	findThisBook();
				
	        	Toast.makeText(AddBook.this, "添加书籍成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(AddBook.this, MainActivity.class);
				AddBook.this.startActivity(intent);
				AddBook.this.finish();
	        }

	        @Override
	        public void onFailure(int arg0, String arg1) {
	            // TODO Auto-generated method stub
	            //ShowToast("-->创建数据失败：" + arg0+",msg = "+arg1);
	        	Toast.makeText(AddBook.this, "添加书籍失败，请重新上传", Toast.LENGTH_SHORT).show();
	        }
	    });
	}
	
	private void findThisBook()
	{
		BmobQuery<Book> query1 = new BmobQuery<Book>();
		query1.addWhereEqualTo("name", books.getName());
		query1.addWhereEqualTo("autor", books.getAutor());
		query1.findObjects(AddBook.this, new FindListener<Book>() {
	        @Override
	        public void onSuccess(List<Book> object) {
	            // TODO Auto-generated method stub
	            //toast("查询成功：共"+object.size()+"条数据。");
	        	bookId = object.get(0).getObjectId();
	        	Toast.makeText(AddBook.this, bookId+"", Toast.LENGTH_SHORT).show();
	        	//添加记录
				RecordBook recordBook = new RecordBook(new BmobDate(new Date()), "录入", mUser.getAddrs()+" "+mUser.getUsername(),bookId);
				AddBookRecordUtil.addTherecord(AddBook.this, recordBook);
	        }
	        @Override
	        public void onError(int code, String msg) {
	            // TODO Auto-generated method stub
	           // toast("查询失败："+msg);
	        }
	});
	}

	//把bitmap写入到临时文件
	/** 保存方法 */
	public void saveMyBitmap(Bitmap mBitmap){
		 File f = new File("/sdcard/temp.png");
		 try {
		  f.createNewFile();
		 } catch (IOException e) {
		  // TODO Auto-generated catch block
		 }
		 FileOutputStream fOut = null;
		 try {
		  fOut = new FileOutputStream(f);
		 } catch (Exception e) {
		  e.printStackTrace();
		 }
		 mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		 try {
		  fOut.flush();
		 } catch (IOException e) {
		  e.printStackTrace();
		 }
		 try {
		  fOut.close();
		 } catch (IOException e) {
		  e.printStackTrace();
		 }
		}
	
}

