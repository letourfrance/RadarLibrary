package com.caac.radar.view;


import com.caac.radar.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


/**
 * <p>Title：ChangeColorIconWithText</P>
 * <p>Description：自定义底部view</P>
 * <p>Company：Digital China</P>
 * @author ouzw
 * @date 2015-12-18 
 * @version  1.0
 */
public class ChangeColorIconWithText extends View {

	private String mText = "约购";
	private int mColor = 0xFF000000;
	private int mTextSize = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
	private float mAlpha;
	private Canvas mCanvas;
	private Bitmap mIconBitmap;
	private Bitmap mBitmap;
	private Paint mPaint;
	private Paint mTextPaint;
	private Rect mIconRect;
	private Rect mTextBound;

	//constructor
	public ChangeColorIconWithText(Context context) {
		this(context, null);
	}
	public ChangeColorIconWithText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	/**
	 * constructor
	 * 获得自定义属性的值
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public ChangeColorIconWithText(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ChangeColorIconWithText);
		
		int n = a.getIndexCount();
		
		for(int i=0; i < n; i++){
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.ChangeColorIconWithText_icon:
				BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
				mIconBitmap = drawable.getBitmap();
				break;
			case R.styleable.ChangeColorIconWithText_color:
				mColor = a.getColor(attr,0xFF000000);
				break;
			case R.styleable.ChangeColorIconWithText_text:
				mText = (String) a.getText(attr);
				break;
			case R.styleable.ChangeColorIconWithText_text_size:
				mTextSize = (int) a.getDimension(attr,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
				break;
			}
		}
		a.recycle();
		mTextBound = new Rect();
		mTextPaint = new Paint();
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(0Xff0000);
		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
	}
	
	//用于测量并定义图标的大小
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int cionWidth = Math.min(getMeasuredWidth()-getPaddingLeft()-getPaddingRight(), getMeasuredHeight()
				-getPaddingTop()-getPaddingBottom()-mTextBound.height());
		int left = getMeasuredWidth()/2-cionWidth/2;
		int top = getMeasuredHeight()/2-mTextBound.height()/2-cionWidth/2;
		
		mIconRect = new Rect(left,top,left+cionWidth,top+cionWidth-5);
	}
	
	//绘制图标
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mIconBitmap,null,mIconRect,null);
		//内存去准备mBitmap，setAlpha，纯色，xfermode，图标
		int alpha = (int) Math.ceil(255*mAlpha);
		setupTargetBitmap(alpha);
		//先绘制原文本，再绘制变色的文本
		drawSourceText(canvas,alpha);
		drawTargetText(canvas,alpha);
		
		canvas.drawBitmap(mBitmap, 0,0, null);
	}

	//绘制原文本
	private void drawSourceText(Canvas canvas, int alpha) {
		mTextPaint.setColor(0Xff000000);
		mTextPaint.setAlpha(255-alpha);
		int x = getMeasuredWidth()/2-mTextBound.width()/2;
		int y = mIconRect.bottom + mTextBound.height();
		canvas.drawText(mText, x, y, mTextPaint);
	}
	
	//绘制变色文本
		private void drawTargetText(Canvas canvas, int alpha) {
			mTextPaint.setColor(mColor);
			mTextPaint.setAlpha(alpha);
			
			int x = getMeasuredWidth()/2-mTextBound.width()/2;
			int y = mIconRect.bottom + mTextBound.height();
			canvas.drawText(mText, x, y, mTextPaint);
		}

	//在内存中绘制可变色的ICON
	private void setupTargetBitmap(int alpha) {
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(),Config.ARGB_8888);
		
		mCanvas = new Canvas(mBitmap);
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setAlpha(alpha);
		mCanvas.drawRect(mIconRect, mPaint);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mPaint.setAlpha(255);
		mCanvas.drawBitmap(mIconBitmap, null, mIconRect,mPaint);
	}
	
	//设置图标透明度
	public void setIconAlpha(float alpha){
		this.mAlpha = alpha;
		invalidateView();
	}

	//重绘
	private void invalidateView() {
		if(Looper.getMainLooper() == Looper.myLooper()){
			invalidate();
		}else{
			postInvalidate();
		}
	}
	
	/**
	 * activity回收后的恢复
	 */
	private static final String INSTANCE_STATUS = "instance_status";
	private static final String STATUS_ALPHA = "status_alpha";
	
	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
		bundle.putFloat(STATUS_ALPHA, mAlpha);
		return super.onSaveInstanceState();
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if(state instanceof Bundle){
			Bundle bundle = (Bundle)state;
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
			return;
		}
		super.onRestoreInstanceState(state);
	}
}
