package com.hughie.linkgame.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.hughie.link.support.common.HughieLoggerManager;
import com.hughie.linkgame.R;
import com.hughie.linkgame.common.HughieSPManager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.SoundPool;

/**
 * 游戏声音操作帮助类
 * @ClassName: HughieSoundUtils
 * @author hughiezhang
 * @since 2015-10-09 17:08
 */
public class HughieSoundUtils implements OnErrorListener {
	private SoundPool mSoundPool = null;
	
	public MediaPlayer mGameMusicBg1;					//背景音乐1
	public MediaPlayer mGameMusicBg2;					//背景音乐2
	public MediaPlayer mGameMusicBg3;					//背景音乐3
	
	private int mGameMusicBgPos1;						//背景音乐1的进度
	private int mGameMusicBgPos2;						//背景音乐2的进度
	private int mGameMusicBgPos3;						//背景音乐3的进度
	
	private Map<Integer, Integer> mSoundPoolMap = null;	
	
	private int mStreamVolume;								//默认音量
	
	private Context mContext;
	
	public static final int SOUND_CLICK = 0;					//click sound
	public static final int SOUND_BOOM = 1;					//bomb sound
	public static final int SOUND_FAIL = 2;						//sound fail
	public static final int SOUND_WIN = 3;						//sound win
	public static final int SOUND_GOOD = 4;					//sound good
	public static final int SOUND_TOUCH = 5;					//touch sound
	public static final int SOUND_DAO = 6;						//dao sound
	public static final int SOUND_WARNING = 7;				//warning sound
	public static final int SOUND_FRESH = 8;					//fresh sound
	public static final int SOUND_HINT = 9;						//hint sound
	
	public HughieSoundUtils(Context context) {
		/*
		 * maxStreams: 同时播放的流的最大数量
		 * streamType: 流的类型，一般为STREAM_MUSIC(具体在AudioManager类中列出)
		 * 					  STREAM_VOICE_CALL：通话
		 * 					  STREAM_SYSTEM：系统
		 * 					  STREAM_RING：铃声 
		 * 					  STREAM_MUSIC：音乐回放即媒体音量
		 * 					  STREAM_ALARM：警报，闹钟
		 * 					  STREAM_NOTIFICATION：窗口顶部状态栏Notification
		 * 					  STREAM_DTMF：双音多频,
		 * srcQuality：采样率转化质量，当前无效果，使用0作为默认值
		 * */
		this.mSoundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 100);
		this.mSoundPoolMap = new HashMap<Integer, Integer>();
		this.mContext = context;
		addGameBackGroundMusic();
		addGameMusic();
		mStreamVolume = ((AudioManager)mContext.getSystemService("audio")).getStreamVolume(SOUND_WIN);
	}
	
	//添加游戏的背景音乐
	private void addGameBackGroundMusic() {
		//游戏背景音乐1
		mGameMusicBg1 = MediaPlayer.create(mContext, R.raw.gamemusicbg1);
		if(mGameMusicBg1 != null) {
			mGameMusicBg1.setLooping(true);
			mGameMusicBg1.setOnErrorListener(this);
		}
		
		//游戏背景音乐2
		mGameMusicBg2 = MediaPlayer.create(mContext, R.raw.gamemusicbg2);
		if(mGameMusicBg2 != null) {
			mGameMusicBg2.setLooping(true);
			mGameMusicBg2.setOnErrorListener(this);
		}
		
		//游戏背景音乐3
		mGameMusicBg3 = MediaPlayer.create(mContext, R.raw.gamemusicbg3);
		if(mGameMusicBg3 != null) {
			mGameMusicBg3.setLooping(true);
			mGameMusicBg3.setOnErrorListener(this);
		}
	}
	
	//添加游戏的铃声 
	private void addGameMusic() {
		//click sound
		mSoundPoolMap.put(Integer.valueOf(SOUND_CLICK), 
				Integer.valueOf(mSoundPool.load(mContext, R.raw.sound_chickbtn, 1)));
		//bomb sound
		mSoundPoolMap.put(Integer.valueOf(SOUND_BOOM), 
				Integer.valueOf(mSoundPool.load(mContext, R.raw.sound_boom, 1)));
		//fail sound
		mSoundPoolMap.put(Integer.valueOf(SOUND_FAIL), 
				Integer.valueOf(mSoundPool.load(mContext, R.raw.sound_faile, 1)));
		//win sound
		mSoundPoolMap.put(Integer.valueOf(SOUND_WIN), 
				Integer.valueOf(mSoundPool.load(mContext, R.raw.sound_success, 1)));
		//good sound
		mSoundPoolMap.put(Integer.valueOf(SOUND_GOOD), 
				Integer.valueOf(mSoundPool.load(mContext, R.raw.sound_good, 1)));
		//touch sound
		mSoundPoolMap.put(Integer.valueOf(SOUND_TOUCH), 
				Integer.valueOf(mSoundPool.load(mContext, R.raw.sound_click, 1)));
		//dao sound
		mSoundPoolMap.put(Integer.valueOf(SOUND_DAO), 
				Integer.valueOf(mSoundPool.load(mContext, R.raw.touch, 1)));
		//warning sound
		mSoundPoolMap.put(Integer.valueOf(SOUND_WARNING), 
				Integer.valueOf(mSoundPool.load(mContext, R.raw.warning6, 1)));
		//refresh sound
		mSoundPoolMap.put(Integer.valueOf(SOUND_FRESH), 
				Integer.valueOf(mSoundPool.load(mContext, R.raw.refresh, 1)));
		//hint sound
		mSoundPoolMap.put(Integer.valueOf(SOUND_HINT), 
				Integer.valueOf(mSoundPool.load(mContext, R.raw.hint, 1)));
	}
	
	//销毁游戏背景音乐1
	public void destroyGameMusic1() {
		if(mGameMusicBg1 != null) {
			if(mGameMusicBg1.isPlaying()) {
				mGameMusicBg1.stop();
			}
			
			mGameMusicBg1.release();
		}
	}
	
	//销毁游戏背景音乐2
	public void destroyGameMusic2() {
		if(mGameMusicBg2 != null) {
			if(mGameMusicBg2.isPlaying()) {
				mGameMusicBg2.stop();
			}
			
			mGameMusicBg2.release();
		}
	}
	
	//销毁游戏背景音乐3
	public void destroyGameMusic3() {
		if(mGameMusicBg3 != null) {
			if(mGameMusicBg3.isPlaying()) {
				mGameMusicBg3.stop();
			}
			
			mGameMusicBg3.release();
		}
	}
	
	//销毁音乐池
	public void destroyGameSound() {
		if(mSoundPool != null) {
			mSoundPool.release();
		}
	}
	
	//播放游戏背景音乐1
	public void playBgGameMusic1() {
		if(mContext.getSharedPreferences(HughieSPManager.SPDefault, Context.MODE_PRIVATE)
				.getBoolean(HughieSPManager.SP_GameBackMusic, true)) {
			if(mGameMusicBg1 == null) {
				addGameBackGroundMusic();
			}
			
			mGameMusicBg1.seekTo(mGameMusicBgPos1);
			mGameMusicBg1.start();
			mGameMusicBg1.setLooping(true);
		}
	}
	
	//播放游戏背景音乐2
	public void playBgGameMusic2() {
		if(mContext.getSharedPreferences(HughieSPManager.SPDefault, Context.MODE_PRIVATE)
				.getBoolean(HughieSPManager.SP_GameBackMusic, true)) {
			if(mGameMusicBg2 == null) {
				addGameBackGroundMusic();
			}
			
			mGameMusicBg2.seekTo(mGameMusicBgPos2);
			mGameMusicBg2.start();
			mGameMusicBg2.setLooping(true);
		}
	}
	
	//播放游戏背景音乐3
	public void playBgGameMusic3() {
		if(mContext.getSharedPreferences(HughieSPManager.SPDefault, Context.MODE_PRIVATE)
				.getBoolean(HughieSPManager.SP_GameBackMusic, true)) {
			if(mGameMusicBg3 == null) {
				addGameBackGroundMusic();
			}
			
			mGameMusicBg3.seekTo(mGameMusicBgPos3);
			mGameMusicBg3.start();
			mGameMusicBg3.setLooping(true);
		}
	}
	
	//播放游戏背景音乐
	public void playBgGameMusic() {
		switch(new Random().nextInt(3)) {
			case 0:
				playBgGameMusic1();
				stopBgGameMusic2();
				stopBgGameMusic3();
				break;
			case 1:
				stopBgGameMusic1();
				playBgGameMusic2();
				stopBgGameMusic3();
				break;
			case 2:
				stopBgGameMusic1();
				stopBgGameMusic2();
				playBgGameMusic3();
				break;
			default:
				break;
		}
	}
	
	//播放游戏操作铃声
	public void playGameSoundByid(int param1, int param2) {
		if(mContext.getSharedPreferences(HughieSPManager.SPDefault, Context.MODE_PRIVATE)
				.getBoolean(HughieSPManager.SP_GameBackMusic, true) && mSoundPool != null) {
			/*
			 * soundID：Load()函数返回的声音ID号
			 * leftVolume：左声道音量设置
			 * rightVolume：右声道音量设置
			 * priority：指定播放声音的优先级，数值越高，优先级越大
			 * loop：指定是否循环。-1表示无限循环，0表示不循环，其他值表示要重复播放的次数
			 * rate：指定播放速率
			 *          1.0的播放率可以使声音按照其原始频率
			 *          而2.0的播放速率，可以使声音按照其原始频率的两倍播放
			 *          如果为0.5的播放率，则播放速率是原始频率的一半
			 *          播放速率的取值范围是0.5至2.0
			 * */
			mSoundPool.play(((Integer)mSoundPoolMap.get(Integer.valueOf(param1))).intValue(), 0.2F * (mStreamVolume % 100), 
					0.2F * (mStreamVolume % 100), 1, param2, 1.0F);
		}
	}
	
	//暂停游戏背景音乐1
	public void stopBgGameMusic1() {
		if(mGameMusicBg1 != null && mGameMusicBg1.isPlaying()) {
			mGameMusicBg1.pause();
			mGameMusicBgPos1 = mGameMusicBg1.getCurrentPosition();
		}
	}
	
	//暂停游戏背景音乐2
	public void stopBgGameMusic2() {
		if(mGameMusicBg2 != null && mGameMusicBg2.isPlaying()) {
			mGameMusicBg2.pause();
			mGameMusicBgPos2 = mGameMusicBg2.getCurrentPosition();
		}
	}
	
	//暂停游戏背景音乐3
	public void stopBgGameMusic3() {
		if(mGameMusicBg3 != null && mGameMusicBg3.isPlaying()) {
			mGameMusicBg3.pause();
			mGameMusicBgPos3 = mGameMusicBg3.getCurrentPosition();
		}
	}
	
	//暂停背景音乐
	public void stopBgMusic() {
		stopBgGameMusic1();
		stopBgGameMusic2();
		stopBgGameMusic3();
	}
	
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		if(mp == mGameMusicBg1) {
			try {
				mGameMusicBg1.release();
				mGameMusicBg1 = MediaPlayer.create(mContext, R.raw.gamemusicbg1);
				mGameMusicBg1.prepare();
				return false;
			} catch (IllegalStateException e) {
				HughieLoggerManager.printStackTrace(e);
			} catch (IOException e) {
				HughieLoggerManager.printStackTrace(e);
			}
		} else if(mp == mGameMusicBg2) {
			try {
				mGameMusicBg2.release();
				mGameMusicBg2 = MediaPlayer.create(mContext, R.raw.gamemusicbg2);
				mGameMusicBg2.prepare();
				return false;
			} catch (IllegalStateException e) {
				HughieLoggerManager.printStackTrace(e);
			} catch (IOException e) {
				HughieLoggerManager.printStackTrace(e);
			}
		} else if(mp == mGameMusicBg3) {
			try {
				mGameMusicBg3.release();
				mGameMusicBg3 = MediaPlayer.create(mContext, R.raw.gamemusicbg3);
				mGameMusicBg3.prepare();
				return false;
			} catch (IllegalStateException e) {
				HughieLoggerManager.printStackTrace(e);
			} catch (IOException e) {
				HughieLoggerManager.printStackTrace(e);
			}
		}
		
		return false;
	}

}
