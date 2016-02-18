package com.caac.radar.util;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;

public class GetImageUtil {
	
	public static Bitmap getBitmap(String stringUrl){
		Bitmap bitmap = null;
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			//初始化一个URL对象、
			URL url = new URL(stringUrl);
			//获取httpconnect网络连接对象
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			connection.setConnectTimeout(5*1000);
			connection.setDoInput(true);
			connection.connect();
			
			//获取输入流
			InputStream isInputStream = connection.getInputStream();
			Log.d("TAG", "inputStream is"+isInputStream);
			bitmap = BitmapFactory.decodeStream(isInputStream);
			Log.d("TAG", "bitmap is"+bitmap);
			//关闭输入流
			isInputStream.close();
			//关闭连接
			connection.disconnect();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return bitmap;
		
	} 

}
