package com.caac.radar.util;

import java.util.Stack;

import android.app.Activity;

/**
 * activity管理
 * @author Android_Ouzw
 *
 */
public class MyActivityManager {
		private static MyActivityManager instance;
		private Stack<Activity> activityStack;//activity栈
		private MyActivityManager() {
		}
		//单例模式
		public static MyActivityManager getInstance() {
		    if (instance == null) {
		        instance = new MyActivityManager();
		    }
		    return instance;
		}
		//把一个activity压入栈中
		public void pushOneActivity(Activity actvity) {
		    if (activityStack == null) {
		        activityStack = new Stack<Activity>();
		    }
		    activityStack.add(actvity);
		}
		//获取栈顶的activity，先进后出原则
		public Activity getLastActivity() {
		    return activityStack.lastElement();
		}
		//移除一个activity
		public void popOneActivity(Activity activity) {
		    if (activityStack != null && activityStack.size() > 0) {
		        if (activity != null) {
		            activity.finish();
		            activityStack.remove(activity);
		            activity = null;
		        }
		    }
		}
		//退出所有activity
		public void finishAllActivity() {
		    if (activityStack != null) {
		        while (activityStack.size() > 0) {
		            Activity activity = getLastActivity();
		            if (activity == null) break;
		            popOneActivity(activity);
		        }
		    }
		}
}
