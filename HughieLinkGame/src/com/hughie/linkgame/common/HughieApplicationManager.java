package com.hughie.linkgame.common;

import java.util.List;
import java.util.Stack;

import com.hughie.link.support.common.HughieLoggerManager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

/**
 * 程序管理类,用于Activity管理和程序退出
 * @ClassName: HughieApplicationManager
 * @author hughiezhang
 * @since 2015-08-26 16:17
 */
public class HughieApplicationManager {
	private Stack<Activity> activityStack;
	private static HughieApplicationManager instance;
	
	public HughieApplicationManager() {
		activityStack = new Stack<Activity>();
	}
	
	/**
	 * 单一实例
	 */
	public static final synchronized HughieApplicationManager getInstance(){
		if (instance == null) {
			instance = new HughieApplicationManager();
		}
		
		return instance;
	}
	
	/**
	 * 添加Activity到堆栈
	 */
	public final void addActivity(Activity activity) {
		activityStack.add(activity);
	}
	
	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public final Activity currentActivity(){
		Activity activity = activityStack.lastElement();
		return activity;
	}
	
	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public final void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}
	
	/**
	 * @description 结束指定的Activity
	 * @param Activity
	 */
	public final void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
			System.gc();
		}
	}
	
	/**
	 * @description 结束指定类名的Activity
	 * @param Class<?>
	 */
	public final void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls)){
				HughieLoggerManager.println("销毁了指定Activity：" + cls);
				finishActivity(activity);
			}
		}
	}
	
	/**
	 * 结束所有Activity
	 */
	public final void finishAllActivity() {
		HughieLoggerManager.println("Activity栈中有: " + activityStack.size());
		for(int i = 0, size = activityStack.size(); i < size; i++){
			if (null != activityStack.get(i)) {
				HughieLoggerManager.println("销毁一个Activity：" + activityStack.get(i).getClass().getSimpleName());
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}
	
	/**
	 * 退出应用程序
	 */
	public final void exitApp(Context context) {
		try {
			finishAllActivity();
			ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
			am.restartPackage(context.getPackageName());
			System.exit(0);
		} catch(Exception e){
			HughieLoggerManager.println("退出应用程序时出错!");
			HughieLoggerManager.printStackTrace(e);
		}
	}
	
	public final void printStack(Context context){
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = manager.getRunningTasks(100);
		for (RunningTaskInfo r : tasks) {
			HughieLoggerManager.println("Activity: " + r.numActivities + ","
					+ r.numRunning + "," + r.baseActivity + ","
					+ r.topActivity + "," + r.description);
		}
	}
	
	/**
	 * 获取指定类名的Activity
	 */
	public final Activity getActivity(Class<?> cls) {
		for(Activity activity : activityStack){
			if(activity.getClass().equals(cls)){
				HughieLoggerManager.println("获取指定Activity：" + cls);
				return activity;
			}
		}
		
		return null;
	}
	
	/**
	 * 栈中是否存在指定类名的Activity
	 */
	public final boolean hasActivity(Class<?> cls){
		return activityStack.contains(getActivity(cls));
	}
	
	/**
	 * 结束指定的Activity上层的Activity
	 */
	public final boolean finishActivityTo(Class<?> cls ) {
		if(!hasActivity(cls)) return false;
		Activity curActivity = activityStack.lastElement();
		while(!curActivity.getClass().equals(cls)){
			finishActivity(curActivity);
			curActivity = activityStack.lastElement();
		}
		
		return true;
	}
}
