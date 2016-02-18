package com.caac.radar.activity;

/**
 * 2016-1-6
 * 注册功能
 */

import cn.bmob.v3.listener.SaveListener;

import com.caac.radar.R;
import com.caac.radar.R.layout;
import com.caac.radar.R.menu;
import com.caac.radar.bean.MyUser;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener{
	
	private EditText et_name,et_password,et_addrs;
	private Button bt_register;
    
	private MyUser user = new MyUser();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.layout_resign);
		setView();
		
	}

	private void setView() {
		// TODO Auto-generated method stub
		et_name = (EditText) findViewById(R.id.et_user_re);
		et_password = (EditText) findViewById(R.id.et_password_re);
		et_addrs = (EditText) findViewById(R.id.et_addrs_rs);
		bt_register = (Button) findViewById(R.id.bt_login_re);
		bt_register.setOnClickListener(this);
	}

	//设置数据
	private MyUser setMyUser(){
		
		user.setUsername(et_name.getText().toString());
		user.setPassword(et_password.getText().toString());
		user.setAddrs(et_addrs.getText().toString());
		return user;
		
	}
	
	//判断是否有空
	private Boolean isEmptem(MyUser user){
		
		if(user.getUsername()==null||user.getUsername().isEmpty()||user.getAddrs()==null||user.getAddrs().isEmpty()){
			return false;
		}else{
			return true;
		}
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//点击注册成功
		switch (v.getId()) {
		case R.id.bt_login_re:
			Toast.makeText(this, "正在注册,请稍等...", Toast.LENGTH_SHORT).show();
			setMyUser();
			
			if (isEmptem(user)) {
				
				user.signUp(this, new SaveListener() {
				    @Override
				    public void onSuccess() {
				        // TODO Auto-generated method stub
				        //toast("注册成功:");
				    	AlertDialog.Builder dialog = new AlertDialog.Builder(Register.this);
				    	dialog.setTitle("注册成功");
				    	dialog.setMessage("注册成功马上登陆？");
				    	dialog.setCancelable(false);
				    	dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								//跳到登陆页面
								Intent intent = new Intent(Register.this, Login.class);
								Register.this.startActivity(intent);
								Register.this.finish();
							}
						});
				    	dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						});
				    	dialog.show();
				  
				    }
				    @Override
				    public void onFailure(int code, String msg) {
				        // TODO Auto-generated method stub
				       //toast("注册失败:"+msg);
				    	Toast.makeText(Register.this, "注册失败:"+msg, Toast.LENGTH_SHORT).show();
				    }
				});
			}
			else{
				Toast.makeText(this, "请填写完整信息再注册", Toast.LENGTH_SHORT).show();
			}
			
			
			break;

		default:
			break;
		}
	}
	}

