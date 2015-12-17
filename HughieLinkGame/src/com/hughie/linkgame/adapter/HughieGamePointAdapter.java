package com.hughie.linkgame.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hughie.linkgame.R;
import com.hughie.linkgame.common.HughieSPManager;
import com.hughie.linkgame.entity.LevelItemDetail;
import com.hughie.linkgame.ui.HughieGameMainActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 游戏关卡Adapter
 * @ClassName: HughieGamePointAdapter
 * @author hughiezhang
 * @since 2015-10-13 17:05
 */
public class HughieGamePointAdapter extends BaseAdapter {
	private List<LevelItemDetail> mGameLevelList;
	private OnGameLevelItemClickListener mOnGameLevelItemClickListener;
	
	private Context mContext;
	
	public HughieGamePointAdapter(Context context, List<LevelItemDetail> gameLevelList, OnGameLevelItemClickListener onGameLevelItemClickListener) {
		this.mContext = context;
		this.mGameLevelList = gameLevelList == null ? new ArrayList<LevelItemDetail>() : gameLevelList;
		this.mOnGameLevelItemClickListener = onGameLevelItemClickListener;
	}
	
	public void setGameLevelList(List<LevelItemDetail> gameLevelList) {
		this.mGameLevelList = gameLevelList == null ? new ArrayList<LevelItemDetail>() : gameLevelList;
	}
	
	public List<LevelItemDetail> getGameLevelList() {
		return mGameLevelList;
	}

	@Override
	public int getCount() {
		return mGameLevelList.size();
	}

	@Override
	public Object getItem(int position) {
		return mGameLevelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		GameLevelViewHolder mHolder = null;
		if(convertView == null) {
			mHolder = new GameLevelViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_hughie_game_level_item, null);
			mHolder.mLevelItemLayout = (RelativeLayout) convertView.findViewById(R.id.game_level_item_layout);
			mHolder.mLevelItemTv = (TextView) convertView.findViewById(R.id.game_level_item_tv);
			mHolder.mLevelItemImgv = (ImageView) convertView.findViewById(R.id.game_level_item_imgv);
			
			convertView.setTag(mHolder);
		} else {
			mHolder = (GameLevelViewHolder) convertView.getTag();
		}
		
		if(mGameLevelList.get(position).getStateFlag() == -1) {
			mHolder.mLevelItemTv.setBackgroundResource(R.drawable.imgv_game_level_lock_background);
			mHolder.mLevelItemTv.setText("");
		} else {
			int mGameLevel = ((LevelItemDetail)mGameLevelList.get(position)).getStateFlag();
			String strGameLevel = String.valueOf(mGameLevel);
			switch(mContext.getSharedPreferences(HughieSPManager.SPDefault, Context.MODE_PRIVATE)
					.getInt(HughieSPManager.SP_GameFinishStatus + strGameLevel, HughieGameMainActivity.TAG_GAME_MAIN_STATUS_FAIL)) {
				//游戏失败
				case HughieGameMainActivity.TAG_GAME_MAIN_STATUS_FAIL:
					mHolder.mLevelItemTv.setBackgroundResource(R.drawable.hughie_game_level_finish_status_fail_selector);
					break;
				//胜利并获得一颗星
				case HughieGameMainActivity.TAG_GAME_MAIN_STATUS_WIN_STAR1:
					mHolder.mLevelItemTv.setBackgroundResource(R.drawable.hughie_game_level_finish_status_win_star1_selector);
					break;
				//胜利并获得两颗星
				case HughieGameMainActivity.TAG_GAME_MAIN_STATUS_WIN_STAR2:
					mHolder.mLevelItemTv.setBackgroundResource(R.drawable.hughie_game_level_finish_status_win_star2_selector);
					break;
				//胜利并获得三颗星
				case HughieGameMainActivity.TAG_GAME_MAIN_STATUS_WIN_STAR3:
					mHolder.mLevelItemTv.setBackgroundResource(R.drawable.hughie_game_level_finish_status_win_star3_selector);
					break;
				default:
					break;
			}
			
			StringBuilder mGameLevelString = new StringBuilder("imgv_game_level_num");
			int mGameLevels = ((LevelItemDetail)mGameLevelList.get(position)).getStateFlag() % 16;
			String mStrGameLevels;
			if(mGameLevels == 0) {
				mStrGameLevels = mGameLevelString.append(16).toString();
			} else {
				mStrGameLevels = mGameLevelString.append(mGameLevels).toString();
			}
			
			String strPackageName = mContext.getPackageName();
			int mGameImgvRes = mContext.getResources().getIdentifier(mStrGameLevels, "drawable", strPackageName);
			mHolder.mLevelItemImgv.setImageResource(mGameImgvRes);
			
			mHolder.mLevelItemLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mOnGameLevelItemClickListener != null) {
						mOnGameLevelItemClickListener.OnGameLevelItemClick(position);
					}
				}
			});
		}
		
		return convertView;
	}
	
	
	private class GameLevelViewHolder {
		private RelativeLayout mLevelItemLayout; 
		private TextView mLevelItemTv;							//关卡背景显示
		private ImageView mLevelItemImgv;					//关卡数字显示
	}
	
	public interface OnGameLevelItemClickListener {
		public void OnGameLevelItemClick(int position);
	}
}
