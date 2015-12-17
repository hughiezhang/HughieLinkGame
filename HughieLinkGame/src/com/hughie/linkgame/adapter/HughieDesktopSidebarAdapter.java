package com.hughie.linkgame.adapter;

import java.util.ArrayList;

import com.hughie.linkgame.R;
import com.hughie.linkgame.entity.ScreenDesktopSidebarDetail;
import com.hughie.linkgame.utils.HughieActivityUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 桌面菜单Adapter
 * @ClassName: HughieScreenDesktopAdapter
 * @author hughiezhang
 * @since 2015-09-09 11:56
 */
public class HughieDesktopSidebarAdapter extends BaseAdapter {
	private ArrayList<ScreenDesktopSidebarDetail> mSidebarDetailList;
	private OnDesktopSidebarItemClickListener mOnDesktopSidebarItemClickListener;
	
	private Context mContext;
	
	public HughieDesktopSidebarAdapter(Context context, ArrayList<ScreenDesktopSidebarDetail> sidebarDetailList,
			OnDesktopSidebarItemClickListener onDesktopSidebarItemClickListener) {
		this.mContext = context;
		this.mSidebarDetailList = sidebarDetailList == null ? new ArrayList<ScreenDesktopSidebarDetail>() : sidebarDetailList;
		this.mOnDesktopSidebarItemClickListener = onDesktopSidebarItemClickListener;
	}
	
	public void setSidebarDetailList(ArrayList<ScreenDesktopSidebarDetail> sidebarDetailList) {
		this.mSidebarDetailList = sidebarDetailList == null ? new ArrayList<ScreenDesktopSidebarDetail>() : sidebarDetailList;
	}
	
	public ArrayList<ScreenDesktopSidebarDetail> getSidebarDetailList(){
		return this.mSidebarDetailList;
	}
	
	@Override
	public int getCount() {
		return mSidebarDetailList.size();
	}

	@Override
	public Object getItem(int position) {
		return mSidebarDetailList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		SidebarViewHolder holder = null;
		if(convertView == null){
			holder = new SidebarViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_hughie_screen_desktop_sidebar_item, null);
			holder.mSidebarItemlayout = (LinearLayout) convertView.findViewById(R.id.desktop_sidebar_item_layout);
			holder.mSidebarItemImgv = (ImageView) convertView.findViewById(R.id.desktop_sidebar_item_imgv);
			holder.mSidebarItemTv = (TextView) convertView.findViewById(R.id.desktop_sidebar_item_tv);
			
			convertView.setTag(holder);
		} else {
			holder = (SidebarViewHolder) convertView.getTag();
		}
		
		ScreenDesktopSidebarDetail mSidebarDetail = mSidebarDetailList.get(position);
		//设置背景
		holder.mSidebarItemlayout.setBackgroundResource(R.drawable.hughie_sidebar_item_bg_selector);
		//设置icom图片
		holder.mSidebarItemImgv.setImageResource(mSidebarDetail.getSidebarImgv());
		//设置name字体
		holder.mSidebarItemTv.setTypeface(HughieActivityUtils.getFontType(mContext, "hobostd.otf"));
		//设置name的值
		holder.mSidebarItemTv.setText(mSidebarDetail.getSidebarName());
		
		//点击操作效果
		holder.mSidebarItemlayout.setClickable(true);
		holder.mSidebarItemlayout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(mOnDesktopSidebarItemClickListener != null){
					mOnDesktopSidebarItemClickListener.onDesktopSidebarItemClick(position);
				}
			}
		});
		
		return convertView;
	}
	
	
	class SidebarViewHolder {
		private LinearLayout mSidebarItemlayout;//布局
		private ImageView mSidebarItemImgv;//icon
		private TextView mSidebarItemTv;//名称
	}
	
	public interface OnDesktopSidebarItemClickListener {
		public void onDesktopSidebarItemClick(int position);
	}
}
