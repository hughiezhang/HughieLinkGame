package com.hughie.linkgame.common;

import com.hughie.link.support.common.HughieLoggerManager;
import com.hughie.linkgame.R;
import com.hughie.linkgame.utils.HughieActivityUtils;
import com.hughie.linkgame.utils.HughieGameController;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Toast管理器
 * @ClassName: HughieToastManager
 * @author hughiezhang
 * @since 2015-09-06 10:56
 */
public class HughieToastManager {
	public static final void showInfo(Context context, String s) {
		if(context != null){
			if(context instanceof Activity) {
				if(!((Activity)context).isFinishing()){
					Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public static final void showInfo(Context context, int s) {
		if(context != null) {
			if(context instanceof Activity) {
				if(!((Activity)context).isFinishing()) {
					Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * @title showGameInfo
	 * @description 自定义游戏toast显示
	 * @param context：游戏的上下文
	 * @param toolCode: 功能操作说明
	 * @return 
	 */
//	public static final void showGameInfo(Context context, int toolCode) {
//		if(context != null) {
//			if(context instanceof Activity) {
//				if(!((Activity) context).isFinishing()) {
//					// 自定义toast，用于游戏提示
//					Toast mGameToast = new Toast(context);
//					// 自定义view，用于toast的布局
//					@SuppressWarnings("static-access")
//					View mGameToastView  = ((Activity) context).getLayoutInflater().from(context).inflate(R.layout.layout_hughie_game_define_toast, null);
//					// toast title
//					TextView mToastTitleTv = (TextView) mGameToastView.findViewById(R.id.game_toast_title_tv);
//					mToastTitleTv.setTypeface(HughieActivityUtils.getFontType(context, "hobostd.otf"));
//					mToastTitleTv.setText(context.getResources().getString(R.string.str_game_main_toast_title_txt));
//					
//					// toast imageview
//					ImageView mToastDisplayImgv = (ImageView) mGameToastView.findViewById(R.id.game_toast_display_imgv);
//					
//					// toast content
//					TextView mToastContentTv = (TextView) mGameToastView.findViewById(R.id.game_toast_content_tv);
//					mToastContentTv.setTypeface(HughieActivityUtils.getFontType(context, "hobostd.otf"));
//					
//					// 通过toolCode来进行判断toast显示的内容
//					// 无刷新
//					if(toolCode == HughieGameController.STATUS_TOAST_NO_REFRESH) {
//						mGameToastView.setBackgroundResource(R.drawable.hughie_game_main_toast_error_background);
//						mToastDisplayImgv.setImageResource(R.drawable.imgv_game_indicator_cancel);
//						mToastContentTv.setTextColor(R.color.color_black);
//						mToastContentTv.setText(R.string.str_game_main_toast_refresh_content_txt);
//					// 无炸弹
//					} else if(toolCode == HughieGameController.STATUS_TOAST_NO_BOMB) {
//						mGameToastView.setBackgroundResource(R.drawable.hughie_game_main_toast_error_background);
//						mToastDisplayImgv.setImageResource(R.drawable.imgv_game_indicator_cancel);
//						mToastContentTv.setTextColor(R.color.color_black);
//						mToastContentTv.setText(R.string.str_game_main_toast_bomb_content_txt);
//					// 无提示
//					} else if(toolCode == HughieGameController.STATUS_TOAST_NO_HINT) {
//						mGameToastView.setBackgroundResource(R.drawable.hughie_game_main_toast_error_background);
//						mToastDisplayImgv.setImageResource(R.drawable.imgv_game_indicator_cancel);
//						mToastContentTv.setTextColor(R.color.color_black);
//						mToastContentTv.setText(R.string.str_game_main_toast_hint_content_txt);
//					// 无冻结
//					} else if(toolCode == HughieGameController.STATUS_TOAST_NO_FREEZE) {
//						mGameToastView.setBackgroundResource(R.drawable.hughie_game_main_toast_error_background);
//						mToastDisplayImgv.setImageResource(R.drawable.imgv_game_indicator_cancel);
//						mToastContentTv.setTextColor(R.color.color_black);
//						mToastContentTv.setText(R.string.str_game_main_toast_freeze_content_txt);
//					// 还在冻结时间
//					} else if(toolCode == HughieGameController.STATUS_TOAST_FREEZE_IN_TIME) {
//						mGameToastView.setBackgroundResource(R.drawable.hughie_game_main_toast_error_background);
//						mToastDisplayImgv.setImageResource(R.drawable.imgv_game_indicator_cancel);
//						mToastContentTv.setTextColor(R.color.color_black);
//						mToastContentTv.setText(R.string.str_game_main_toast_freeze_in_time_content_txt);
//					}
//					
//					mGameToast.setGravity(Gravity.CENTER, 0, 180);
//					mGameToast.setDuration(Toast.LENGTH_SHORT);
//					mGameToast.setView(mGameToastView);
//					mGameToast.show();
//				}
//			} else {
//				
//			}
//		}
//	}
}
