package com.hughie.linkgame.common;

import java.io.File;

import com.hughie.link.support.common.HughieLoggerManager;

import android.os.Environment;
import android.text.format.Time;

/**
 * 文件管理类
 * @ClassName: HughieFileManager
 * @author hughiezhang
 * @since 2015-09-02 09:42
 */
public final class HughieFileManager {
	private static File mRootDir;
	
	static {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			mRootDir = Environment.getExternalStorageDirectory();
			HughieLoggerManager.println("有SD卡，本地文件根目录：" + mRootDir);
		} else {
			mRootDir = Environment.getExternalStorageDirectory();
			HughieLoggerManager.println("没有SD卡，本地文件根目录：" + mRootDir);
		}
		
		File dir = new File(mRootDir, "/HughieLinkGame/.nomedia");
		if(!dir.exists()){
			dir.mkdirs();
		}
	}
	
	public static final File getTempRecordFile() {
		try {
			File mRecordDir = new File(mRootDir, "/HughieLinkGame/Record");
			if(!mRecordDir.exists()){
				mRecordDir.mkdirs();
			}
			
			File mRecordFile = new File(mRecordDir, "temp.wav");
			if(mRecordFile.exists()){
				mRecordFile.delete();
			}
			
			return new File(mRecordDir, "temp.wav");
		} catch(Exception e) {
			HughieLoggerManager.printStackTrace(e);
			HughieLoggerManager.println("创建录音文件出错");
		}
		
		return new File(Environment.getDownloadCacheDirectory(), "temp");
	}
	
	public static final File getLogFile() {
		try {
			File mLogDir = new File(Environment.getExternalStorageDirectory(), "/HughieLinkGame/Log");
			if(!mLogDir.exists()){
				mLogDir.mkdirs();
			}
			
			Time t = new Time();
			t.set(System.currentTimeMillis());
			String mTimeFileString = t.year + "-" + (t.month + 1) + "-" + t.monthDay + "_"
					+ t.hour + "-" + t.minute + "-" + t.second + ".log";
			return new File(mLogDir, mTimeFileString);
		} catch(Exception e){
			HughieLoggerManager.printStackTrace(e);
			HughieLoggerManager.println("创建日志文件出错");
		}
		
		return getTempRecordFile();
	}
}
