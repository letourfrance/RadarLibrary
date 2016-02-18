package com.caac.radar;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;

import com.caac.radar.R;
import com.caac.radar.activity.AddBook;
import com.caac.radar.activity.BookViewActivity;
import com.caac.radar.activity.CaptureActivity;
import com.caac.radar.activity.Login;
import com.caac.radar.activity.Register;
import com.caac.radar.bean.BookDouBan;
import com.caac.radar.bean.MyUser;
import com.caac.radar.fragment.ClassfitFragment;
import com.caac.radar.fragment.LibraryFragment;
import com.caac.radar.fragment.MyHomeFragment;
import com.caac.radar.fragment.SortFragment;
import com.caac.radar.util.BookUtil;
import com.caac.radar.util.MyActivityManager;
import com.caac.radar.view.ChangeColorIconWithText;
import com.caac.radar.view.Constants.Config;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * 2015-12-21
 * @author Android_Ouzw
 *
 */

public class MainActivity extends  FragmentActivity implements OnClickListener,OnPageChangeListener
{

	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();
	
	private ClassfitFragment mClassfitFragment;
	private LibraryFragment mLibraryFragment;
	private SortFragment mSortFragment;
	private MyHomeFragment myHomeFragment;
	private Context context;
	
	private Handler handler;
    private ProgressDialog progressDialog;
	
    MyActivityManager mam = MyActivityManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);
        mam.pushOneActivity(MainActivity.this);
        context = this.getApplicationContext();
        //ActionBar actionBar = getActionBar();
        //setOverflowShowingAlways();
        //actionBar.setDisplayShowHomeEnabled(false);
        // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, "d455b99543fad7044bbe01a41e21ccb1");
     // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, "d455b99543fad7044bbe01a41e21ccb1");
		initView();
		initDatas();
		mViewPager.setAdapter(mAdapter);
		initEvent();
		
		MyUser bmobUser =  BmobUser.getCurrentUser(context,MyUser.class);
		
		if(bmobUser != null){
		    // 允许用户使用应用
			//接收来自下载线程的消息
	        handler=new Handler(){
	            @Override
	            public void handleMessage(Message msg) {
	                // TODO Auto-generated method stub
	                super.handleMessage(msg);
	                BookDouBan book= (BookDouBan)msg.obj;
	                //进度条消失
	                progressDialog.dismiss();
	                if(book==null){
	                	Toast.makeText(MainActivity.this, "没有找到这本书", Toast.LENGTH_LONG).show();
	                }
	                else{
	                    Intent intent=new Intent(MainActivity.this,BookViewActivity.class);
	                    //通过Intent 传递 Object，需要让该实体类实现Parceable接口
	                    intent.putExtra(BookDouBan.class.getName(),book);
	                    startActivity(intent);
	                }
	            }
	        };
		}else{
		    //缓存用户对象为空时， 可打开用户注册界面…
			Intent intent = new Intent(MainActivity.this, Login.class);
			this.startActivity(intent);
			this.finish();
		}
    }

//    @Override
//	public boolean onCreateOptionsMenu(Menu menu)
//	{
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
    /**
	 * 初始化所有事件
	 */
	private void initEvent()
	{

		mViewPager.setOnPageChangeListener(this);

	}

	private void initDatas()
	{
		
		mClassfitFragment = new ClassfitFragment();
		mLibraryFragment = new LibraryFragment();
		mSortFragment = new SortFragment();
		myHomeFragment = new MyHomeFragment();
		
		mTabs.add(mLibraryFragment);
		mTabs.add(mClassfitFragment);
		mTabs.add(mSortFragment);
		mTabs.add(myHomeFragment);

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{

			@Override
			public int getCount()
			{
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int position)
			{
				return mTabs.get(position);
			}
		};
	}

	private void initView()
	{
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
		mTabIndicators.add(two);
		ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
		mTabIndicators.add(four);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);

		one.setIconAlpha(1.0f);

	}

	

	/**
	 * 显示actionBar上的“+”
	 */
//	private void setOverflowShowingAlways() {
//		try {
//			ViewConfiguration config = ViewConfiguration.get(this);
//			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
//			menuKeyField.setAccessible(true);
//			menuKeyField.setBoolean(config, false);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * 设置menu显示icon
	 */
//	@Override
//	public boolean onMenuOpened(int featureId, Menu menu)
//	{
//
//		if (featureId == Window.FEATURE_ACTION_BAR && menu != null)
//		{
//			if (menu.getClass().getSimpleName().equals("MenuBuilder"))
//			{
//				try
//				{
//					Method m = menu.getClass().getDeclaredMethod(
//							"setOptionalIconsVisible", Boolean.TYPE);
//					m.setAccessible(true);
//					m.invoke(menu, true);
//				} catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//			}
//		}
//
//		return super.onMenuOpened(featureId, menu);
//	}

	@Override
	public void onClick(View v)
	{
		clickTab(v);

	}

	/**
	 * 点击Tab按钮
	 * 
	 * @param v
	 */
	private void clickTab(View v)
	{
		resetOtherTabs();

		switch (v.getId())
		{
		case R.id.id_indicator_one:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.id_indicator_two:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.id_indicator_three:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.id_indicator_four:
			mTabIndicators.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);
			break;
		}
	}

	/**
	 * 重置其他的TabIndicator的颜色
	 */
	private void resetOtherTabs()
	{
		for (int i = 0; i < mTabIndicators.size(); i++)
		{
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels)
	{
		// Log.e("TAG", "position = " + position + " ,positionOffset =  "
		// + positionOffset);
		if (positionOffset > 0)
		{
			ChangeColorIconWithText left = mTabIndicators.get(position);
			ChangeColorIconWithText right = mTabIndicators.get(position + 1);
			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}

	}

	
	public void OnAddBook(View view)
	{
		AlertDialog.Builder builder = new Builder(this);
		 
        builder.setItems(getResources().getStringArray(R.array.ItemArray), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface arg0, int arg1)
            {
                // TODO 自动生成的方法存根
                switch (arg1) {
				case 0:
					
					Intent intent = new Intent(MainActivity.this, AddBook.class);
					startActivity(intent);
					
					break;

                case 1:
                	Intent intent1=new Intent(MainActivity.this,CaptureActivity.class);
                	startActivityForResult(intent1,100);
                	
					break;

				default:
					break;
				}
                arg0.dismiss();
            }
        });
        
        builder.show();
	}
	
	/*
     *	从MainActivity 开启扫描跳到 CaptureActivity，扫描到ISBN 返回到 MainActivity 
     *	返回的 ISBN码 绑定在Intent中
     */
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(((requestCode==100)||(resultCode==Activity.RESULT_OK))&&data!=null){
        	//判断网络是否连接
        	if(BookUtil.isNetworkConnected(this)){
	        	progressDialog=new ProgressDialog(this);
	        	progressDialog.setMessage("请稍候，正在读取信息...");
	        	progressDialog.show();
	            String urlstr="https://api.douban.com/v2/book/isbn/"+data.getExtras().getString("result");
	            //扫到ISBN后，启动下载线程下载图书信息
	            new LoadParseBookThread(urlstr).start();
        	}
        	else {
				Toast.makeText(this, "网络异常，请检查你的网络连接", Toast.LENGTH_LONG).show();
			}
        }
    }
    
    /**
     * <a href="http://fangjie.sinaapp.com">http://fangjie.sinaapp.com</a>
     * @version 1.0
     * @author JayFang
     * @describe 异步下载并解析图书信息的线程类，线程结束后会发送Message消息，带有解析之后的Book对象
     */
    private class LoadParseBookThread extends Thread
    {
        private String url;
        
        //通过构造函数传递url地址
        public LoadParseBookThread(String urlstr)
        {
            url=urlstr;
        }
        
        public void run()
        {
            Message msg=Message.obtain(); 
            String result=BookUtil.getHttpRequest(url);
            try {
	            BookDouBan book=new BookUtil().parseBookInfo(result);
	            //给主线程UI界面发消息，提醒下载信息，解析信息完毕
	            msg.obj=book;
			} catch (Exception e) {
				e.printStackTrace();
			}
            handler.sendMessage(msg);
        }
    }
	
	@Override
	public void onPageSelected(int position)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int state)
	{
		// TODO Auto-generated method stub

	}
  
	// 退出程序
		protected void dialog() {
			AlertDialog.Builder builder = new Builder(MainActivity.this);
			builder.setMessage("退出程序");
			builder.setTitle("退出程序");
			builder.setPositiveButton("确定",
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							//MainActivity.this.finish();
							mam.finishAllActivity();
						}
					});
			builder.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
		
		@Override
		     public boolean onKeyDown(int keyCode, KeyEvent event) {
		         if(keyCode == KeyEvent.KEYCODE_BACK) {
		        	 dialog();
		         }
		         return false;
		     }
    
}
