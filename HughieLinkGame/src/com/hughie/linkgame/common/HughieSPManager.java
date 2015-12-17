package com.hughie.linkgame.common;

/**
 * 用于管理SharedPreferences文件
 * @ClassName: HughieSPManager
 * @author hughiezhang
 * @since 2015-08-27 11:55
 */
public class HughieSPManager {
	public static final String SPDefault = "com.hughie.linkgame.user";						// 默认文件
	
	public static final String SP_versionCode = "versionCode";									//以展示过的欢迎页版本号
	public static final String SP_LastUpdateTime = "LastUpdateTime";						//体检报告最后更新时间
	
	//game main tab
	public static final String SP_GameGridSize="GridSize";										//game grid的初始化大小
	
	//game 
	public static final String SP_GameBackMusic = "game_music";								//game background music
	public static final String SP_GameSort = "game_sort";											//game sort
	public static final String SP_GameMode = "game_mode";									//game mode
	public static final String SP_GameFood = "game_food";										//food sort
	public static final String SP_GameAnimal = "game_animal";									//animal sort
	public static final String SP_GameFruit = "game_fruit";											//fruit sort
	public static final String SP_GameLevel = "game_level";										//game level
	public static final String SP_GameMaxLevel = "game_max_level";							//game max level
	public static final String SP_GameFrame = "game_frame";									//game frame
	public static final String SP_GameLevelStatus = "game_level_status";						//game level status
	public static final String SP_GameRefresh = "game_refresh";								//game refresh num
	public static final String SP_GameBomb = "game_bomb";									//game bomb num
	public static final String SP_GameHint = "game_hint";											//game hint num
	public static final String SP_GameFreeze = "game_freeze";									//game freeze number
	public static final String SP_GameFinishStatus = "game_finish_status";					//game finish status 
	public static final String SP_GameScore = "game_score";										//game score
}
