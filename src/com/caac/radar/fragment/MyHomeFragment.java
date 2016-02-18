package com.caac.radar.fragment;


import cn.bmob.v3.BmobUser;

import com.caac.radar.MainActivity;
import com.caac.radar.R;
import com.caac.radar.activity.AddBook;
import com.caac.radar.activity.CaptureActivity;
import com.caac.radar.activity.ContentBooks;
import com.caac.radar.activity.Login;
import com.caac.radar.activity.RedingActivity;
import com.caac.radar.activity.SettingActivty;
import com.caac.radar.bean.MyUser;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MyHomeFragment extends Fragment implements OnClickListener{
	
	private View view;
	private TextView tv_name,tv_addrs;
	private LinearLayout ll_about,ll_mybook,ll_setting,ll_quit,ll_news;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fg_myhome,null);
		
        setView();
        
      //获取当前用户
		MyUser user = BmobUser.getCurrentUser(MyHomeFragment.this.getActivity(), MyUser.class);
		tv_name.setText(user.getUsername().toString());
		tv_addrs.setText(user.getAddrs().toString());
		return view;

	}

	private void setView() {
		// TODO Auto-generated method stub
		tv_name = (TextView) view.findViewById(R.id.my_tv_name);
		tv_addrs = (TextView) view.findViewById(R.id.my_tv_adds);
		ll_about = (LinearLayout) view.findViewById(R.id.my_tv_about);
		ll_mybook = (LinearLayout) view.findViewById(R.id.my_reding);
		ll_setting = (LinearLayout) view.findViewById(R.id.my_tv_setting);
		ll_quit = (LinearLayout) view.findViewById(R.id.my_tv_exit);
		ll_news = (LinearLayout) view.findViewById(R.id.my_tv_news);
		ll_about.setOnClickListener(this);
		ll_mybook.setOnClickListener(this);
		ll_setting.setOnClickListener(this);
		ll_quit.setOnClickListener(this);
		ll_news.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.my_tv_about:
			Toast.makeText(MyHomeFragment.this.getActivity(), "中南空管局技术保障中心雷达室开发1.0版", Toast.LENGTH_SHORT).show();
			break;

		case R.id.my_reding:
			Toast.makeText(MyHomeFragment.this.getActivity(), "正在借阅", Toast.LENGTH_SHORT).show();
			Intent intent1 = new Intent(MyHomeFragment.this.getActivity(), RedingActivity.class);
			startActivity(intent1);
			break;
			
		case R.id.my_tv_setting:
			Toast.makeText(this.getActivity(), "设置", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(MyHomeFragment.this.getActivity(), SettingActivty.class);
			startActivity(intent);
			
			break;
			
		case R.id.my_tv_exit:
			
			MyUser.logOut(this.getActivity());   //清除缓存用户对象
			MyUser currentUser = BmobUser.getCurrentUser(this.getActivity(),MyUser.class); // 现在的currentUser是null了
			Toast.makeText(MyHomeFragment.this.getActivity(), "退出登录", Toast.LENGTH_SHORT).show();
			this.getActivity().finish();
			Intent intent2 = new Intent(MyHomeFragment.this.getActivity(), Login.class);
			startActivity(intent2);
			break;
			
		case R.id.my_tv_news:
			Toast.makeText(MyHomeFragment.this.getActivity(), "已是最新版本", Toast.LENGTH_SHORT).show();
			break;
			
		default:
			break;
		}
	}


}
