package com.caac.radar.activity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

import com.caac.radar.R;
import com.caac.radar.R.layout;
import com.caac.radar.R.menu;
import com.caac.radar.bean.MyUser;
import com.caac.radar.fragment.MyHomeFragment;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 2016-1-22
 * @author Android_Ouzw
 * 修改资料
 */
public class SettingActivty extends Activity implements OnClickListener{

	private EditText et_set_name,et_set_addrs,et_passw;
	private Button bt_set_sure;
	private ImageButton ib_set_back;
	private MyUser user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.layout_setting);
		user = BmobUser.getCurrentUser(this, MyUser.class);
		InitView();
		et_set_name.setText(user.getUsername());
		et_set_addrs.setText(user.getAddrs());
		
	}
	private void InitView() {
		// TODO Auto-generated method stub
		et_set_name = (EditText) findViewById(R.id.et_set_uname);
		et_set_addrs = (EditText) findViewById(R.id.et_set_uadd);
		et_passw = (EditText) findViewById(R.id.et_set_paw);
		bt_set_sure = (Button) findViewById(R.id.bt_set_sure);
		ib_set_back = (ImageButton) findViewById(R.id.ib_set_back);
		bt_set_sure.setOnClickListener(this);
		ib_set_back.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_set_sure:
			MyUser newUser = new MyUser();
			newUser.setUsername(et_set_name.getText().toString());
			newUser.setAddrs(et_set_addrs.getText().toString());
			
			if(et_passw.getText()+"" != "" && et_passw.getText() != null){
				newUser.setPassword(et_passw.getText().toString());
				//Toast.makeText(SettingActivty.this, "请输入密码", Toast.LENGTH_LONG).show();
			}
			
			newUser.update(this,user.getObjectId(),new UpdateListener() {
			    @Override
			    public void onSuccess() {
			        // TODO Auto-generated method stub
			       // toast("更新用户信息成功:");
			    	Toast.makeText(SettingActivty.this, "修改成功", Toast.LENGTH_LONG).show();
			    	finish();
			    }
			    @Override
			    public void onFailure(int code, String msg) {
			        // TODO Auto-generated method stub
			       // toast("更新用户信息失败:" + msg);
			    }
			});
			break;
			
		case R.id.ib_set_back:
			finish();
			break;
			
		default:
			break;
		}
	}


}
