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
 * ��Ϸ��������������
 * @ClassName: HughieSoundUtils
 * @author hughiezhang
 * @since 2015-10-09 17:08
 */
public class HughieSoundUtils implements OnErrorListener {
	private SoundPool mSoundPool = null;
	
	public MediaPlayer mGameMusicBg1;					//��������1
	public MediaPlayer mGameMusicBg2;					//��������2
	public MediaPlayer mGameMusicBg3;					//��������3
	
	private int mGameMusicBgPos1;						//��������1�Ľ���
	private int mGameMusicBgPos2;						//��������2�Ľ���
	private int mGameMusicBgPos3;						//��������3�Ľ���
	
	private Map<Integer, Integer> mSoundPoolMap = null;	
	
	private int mStreamVolume;								//Ĭ������
	
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
		 * maxStreams: ͬʱ���ŵ������������
		 * streamType: �������ͣ�һ��ΪSTREAM_MUSIC(������AudioManager�����г�)
		 * 					  STREAM_VOICE_CALL��ͨ��
		 * 					  STREAM_SYSTEM��ϵͳ
		 * 					  STREAM_RING������ 
		 * 					  STREAM_MUSIC�����ֻطż�ý������
		 * 					  STREAM_ALARM������������
		 * 					  STREAM_NOTIFICATION�����ڶ���״̬��Notification
		 * 					  STREAM_DTMF��˫����Ƶ,
		 * srcQuality��������ת����������ǰ��Ч����ʹ��0��ΪĬ��ֵ
		 * */
		this.mSoundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 100);
		this.mSoundPoolMap = new HashMap<Integer, Integer>();
		this.mContext = context;
		addGameBackGroundMusic();
		addGameMusic();
		mStreamVolume = ((AudioManager)mContext.getSystemService("audio")).getStreamVolume(SOUND_WIN);
	}
	
	//�����Ϸ�ı�������
	private void addGameBackGroundMusic() {
		//��Ϸ��������1
		mGameMusicBg1 = MediaPlayer.create(mContext, R.raw.gamemusicbg1);
		if(mGameMusicBg1 != null) {
			mGameMusicBg1.setLooping(true);
			mGameMusicBg1.setOnErrorListener(this);
		}
		
		//��Ϸ��������2
		mGameMusicBg2 = MediaPlayer.create(mContext, R.raw.gamemusicbg2);
		if(mGameMusicBg2 != null) {
			mGameMusicBg2.setLooping(true);
			mGameMusicBg2.setOnErrorListener(this);
		}
		
		//��Ϸ��������3
		mGameMusicBg3 = MediaPlayer.create(mContext, R.raw.gamemusicbg3);
		if(mGameMusicBg3 != null) {
			mGameMusicBg3.setLooping(true);
			mGameMusicBg3.setOnErrorListener(this);
		}
	}
	
	//�����Ϸ������ 
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
	
	//������Ϸ��������1
	public void destroyGameMusic1() {
		if(mGameMusicBg1 != null) {
			if(mGameMusicBg1.isPlaying()) {
				mGameMusicBg1.stop();
			}
			
			mGameMusicBg1.release();
		}
	}
	
	//������Ϸ��������2
	public void destroyGameMusic2() {
		if(mGameMusicBg2 != null) {
			if(mGameMusicBg2.isPlaying()) {
				mGameMusicBg2.stop();
			}
			
			mGameMusicBg2.release();
		}
	}
	
	//������Ϸ��������3
	public void destroyGameMusic3() {
		if(mGameMusicBg3 != null) {
			if(mGameMusicBg3.isPlaying()) {
				mGameMusicBg3.stop();
			}
			
			mGameMusicBg3.release();
		}
	}
	
	//�������ֳ�
	public void destroyGameSound() {
		if(mSoundPool != null) {
			mSoundPool.release();
		}
	}
	
	//������Ϸ��������1
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
	
	//������Ϸ��������2
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
	
	//������Ϸ��������3
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
	
	//������Ϸ��������
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
	
	//������Ϸ��������
	public void playGameSoundByid(int param1, int param2) {
		if(mContext.getSharedPreferences(HughieSPManager.SPDefault, Context.MODE_PRIVATE)
				.getBoolean(HughieSPManager.SP_GameBackMusic, true) && mSoundPool != null) {
			/*
			 * soundID��Load()�������ص�����ID��
			 * leftVolume����������������
			 * rightVolume����������������
			 * priority��ָ���������������ȼ�����ֵԽ�ߣ����ȼ�Խ��
			 * loop��ָ���Ƿ�ѭ����-1��ʾ����ѭ����0��ʾ��ѭ��������ֵ��ʾҪ�ظ����ŵĴ���
			 * rate��ָ����������
			 *          1.0�Ĳ����ʿ���ʹ����������ԭʼƵ��
			 *          ��2.0�Ĳ������ʣ�����ʹ����������ԭʼƵ�ʵ���������
			 *          ���Ϊ0.5�Ĳ����ʣ��򲥷�������ԭʼƵ�ʵ�һ��
			 *          �������ʵ�ȡֵ��Χ��0.5��2.0
			 * */
			mSoundPool.play(((Integer)mSoundPoolMap.get(Integer.valueOf(param1))).intValue(), 0.2F * (mStreamVolume % 100), 
					0.2F * (mStreamVolume % 100), 1, param2, 1.0F);
		}
	}
	
	//��ͣ��Ϸ��������1
	public void stopBgGameMusic1() {
		if(mGameMusicBg1 != null && mGameMusicBg1.isPlaying()) {
			mGameMusicBg1.pause();
			mGameMusicBgPos1 = mGameMusicBg1.getCurrentPosition();
		}
	}
	
	//��ͣ��Ϸ��������2
	public void stopBgGameMusic2() {
		if(mGameMusicBg2 != null && mGameMusicBg2.isPlaying()) {
			mGameMusicBg2.pause();
			mGameMusicBgPos2 = mGameMusicBg2.getCurrentPosition();
		}
	}
	
	//��ͣ��Ϸ��������3
	public void stopBgGameMusic3() {
		if(mGameMusicBg3 != null && mGameMusicBg3.isPlaying()) {
			mGameMusicBg3.pause();
			mGameMusicBgPos3 = mGameMusicBg3.getCurrentPosition();
		}
	}
	
	//��ͣ��������
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
