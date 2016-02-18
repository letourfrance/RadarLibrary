package com.caac.radar.activity;

/**
 * 2016-1-6
 * 登陆功能
 */
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

import com.caac.radar.R;
import com.caac.radar.R.layout;
import com.caac.radar.R.menu;
import com.caac.radar.bean.MyUser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener{

	private EditText et_user,et_pass;
	private Button bt_login,bt_forget,bt_newuser;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.layout_login);
		setView();
		
	}

	

	private void setView() {
		// TODO Auto-generated method stub
		et_user = (EditText) findViewById(R.id.et_user);
		et_pass = (EditText) findViewById(R.id.et_password);
		bt_login = (Button) findViewById(R.id.bt_login);
		bt_forget = (Button) findViewById(R.id.bt_forget);
		bt_newuser = (Button) findViewById(R.id.bt_newuser);
		bt_login.setOnClickListener(this);
		bt_forget.setOnClickListener(this);
		bt_newuser.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_login:
			Toast.makeText(Login.this, "正在登陆，请稍等...", Toast.LENGTH_SHORT).show();
			MyUser.loginByAccount(this, et_user.getText().toString(), et_pass.getText().toString(), new LogInListener<MyUser>() {

	            @Override
	            public void done(MyUser user, BmobException e) {
	                // TODO Auto-generated method stub
	                if(user!=null){
	                    //Log.i("smile","用户登陆成功");
	                	//跳到主界面
	                	Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
	                	Intent intent = new Intent(Login.this, com.caac.radar.MainActivity.class);
	                	Login.this.startActivity(intent);
					    Login.this.finish();
	                }else{
	                	Toast.makeText(Login.this, "登录失败，请重新输入用户密码！", Toast.LENGTH_SHORT).show();
	                	et_user.setText("");
	                	et_pass.setText("");
	                }
	            }

	        });
			break;

		case R.id.bt_forget:
			Intent intent1 = new Intent(Login.this, Register.class);
        	Login.this.startActivity(intent1);
		    Login.this.finish();
			break;
			
        case R.id.bt_newuser:
        	Intent intent2 = new Intent(Login.this, Register.class);
        	Login.this.startActivity(intent2);
		    Login.this.finish();
			break;
			
		default:
			break;
		}
	}

	
}
