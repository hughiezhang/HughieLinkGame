package com.hughie.linkgame.common;

import java.util.List;
import java.util.Stack;

import com.hughie.link.support.common.HughieLoggerManager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

/**
 * ���������,����Activity����ͳ����˳�
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
	 * ��һʵ��
	 */
	public static final synchronized HughieApplicationManager getInstance(){
		if (instance == null) {
			instance = new HughieApplicationManager();
		}
		
		return instance;
	}
	
	/**
	 * ���Activity����ջ
	 */
	public final void addActivity(Activity activity) {
		activityStack.add(activity);
	}
	
	/**
	 * ��ȡ��ǰActivity����ջ�����һ��ѹ��ģ�
	 */
	public final Activity currentActivity(){
		Activity activity = activityStack.lastElement();
		return activity;
	}
	
	/**
	 * ������ǰActivity����ջ�����һ��ѹ��ģ�
	 */
	public final void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}
	
	/**
	 * @description ����ָ����Activity
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
	 * @description ����ָ��������Activity
	 * @param Class<?>
	 */
	public final void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls)){
				HughieLoggerManager.println("������ָ��Activity��" + cls);
				finishActivity(activity);
			}
		}
	}
	
	/**
	 * ��������Activity
	 */
	public final void finishAllActivity() {
		HughieLoggerManager.println("Activityջ����: " + activityStack.size());
		for(int i = 0, size = activityStack.size(); i < size; i++){
			if (null != activityStack.get(i)) {
				HughieLoggerManager.println("����һ��Activity��" + activityStack.get(i).getClass().getSimpleName());
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}
	
	/**
	 * �˳�Ӧ�ó���
	 */
	public final void exitApp(Context context) {
		try {
			finishAllActivity();
			ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
			am.restartPackage(context.getPackageName());
			System.exit(0);
		} catch(Exception e){
			HughieLoggerManager.println("�˳�Ӧ�ó���ʱ����!");
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
	 * ��ȡָ��������Activity
	 */
	public final Activity getActivity(Class<?> cls) {
		for(Activity activity : activityStack){
			if(activity.getClass().equals(cls)){
				HughieLoggerManager.println("��ȡָ��Activity��" + cls);
				return activity;
			}
		}
		
		return null;
	}
	
	/**
	 * ջ���Ƿ����ָ��������Activity
	 */
	public final boolean hasActivity(Class<?> cls){
		return activityStack.contains(getActivity(cls));
	}
	
	/**
	 * ����ָ����Activity�ϲ��Activity
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
