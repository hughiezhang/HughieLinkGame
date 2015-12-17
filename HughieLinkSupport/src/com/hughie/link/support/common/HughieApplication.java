package com.hughie.link.support.common;

import com.hughie.link.support.R;
import com.hughie.link.support.util.HughiePropertiesUtils;
import com.hughie.link.support.util.HughieScreenManager;
import com.hughie.link.support.util.HughieSharePreferenceManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.LimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class HughieApplication extends Application {
	private ConnectivityManager mConnManager;
	private static HughieApplication mHughieApplication;
	
	private boolean wifi;// �Ƿ���wifi״̬
	private boolean connected;// �Ƿ�����
	private boolean cmwap;// cmwap����
	private boolean fast;// ����״̬
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// ��ʼ��SharePreferences
		HughieSharePreferenceManager.init(getApplicationContext());
		initImageLoader();
		mHughieApplication = this;
		HughiePropertiesUtils.init(getApplicationContext());
		
		initScreenManager();
	}
	
	public void initScreenManager() {
		HughieScreenManager hsm = HughieScreenManager.getInstance(getApplicationContext());
		hsm.init();
	}
	
	/**
	 * ��ʼ��ImageLoader
	 */
	public void initImageLoader() {
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.imgv_support_noimage_default)
			.showImageForEmptyUri(R.drawable.imgv_support_noimage_default)
			.showImageOnFail(R.drawable.imgv_support_noimage_default)
			.cacheOnDisc(true).cacheInMemory(true)
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.bitmapConfig(Bitmap.Config.RGB_565).build();
		
		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
		LimitedMemoryCache memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.memoryCache(memoryCache)
			.defaultDisplayImageOptions(defaultOptions)
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.FIFO).build();
		imageLoader.init(config);
	}
	
	public synchronized static HughieApplication getInstance(){
		return mHughieApplication;
	}
	
	/** ����Ҫͨ��{@link #isConnected()}���ж��Ƿ����������ӣ�Ȼ����ô˷����жϵ�ǰ�����Ƿ�wifi */
	public boolean isWifi() {
		return wifi;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	/** ����Ҫͨ��{@link #isConnected()}���ж��Ƿ����������ӣ�Ȼ����ô˷����жϵ�ǰ�����Ƿ�cmwap */
	public boolean isCmwap() {
		return cmwap;
	}
	
	/** ���ٿ첻��ѽ�� */
	public boolean isFast() {
		return fast;
	}
	
	void checkNetworkState() {
		if(mConnManager == null) {
			mConnManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		
		connected = wifi = cmwap = fast = false;
		
		NetworkInfo info = mConnManager.getActiveNetworkInfo();
		
		if(info != null && info.isConnected()) {
			connected = true;
			int type = info.getType();
			if(type == ConnectivityManager.TYPE_WIFI){
				wifi = true;
				fast = true;
			} else if(type == ConnectivityManager.TYPE_MOBILE) {
				String extra = info.getExtraInfo();
				cmwap = !TextUtils.isEmpty(extra) && "cmwap".equals(info.getExtraInfo().toLowerCase());
				// ����2G�� NETWORK_TYPE_CDMA
				// �ƶ�2G�� 2 NETWORK_TYPE_EDGE
				// ��ͨ��2G 1 NETWORK_TYPE_GPRS
				int subtype = info.getSubtype();
				switch(subtype){
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_GPRS:
						HughieLoggerManager.logD("��������ˡ�");
						fast = false;
						break;
					default:
						fast = true;
						break;
				}
			}
		}
	}
}
