package com.hughie.linkgame.widget;

import java.util.ArrayList;
import java.util.HashMap;

import com.hughie.link.support.common.HughieLoggerManager;
import com.hughie.linkgame.R;
import com.hughie.linkgame.widget.HughieGridViewItem.OnGridItemClickListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * 可拖拽和删除的gridview
 * @ClassName: HughieDragGridView
 * @author hughiezhang
 * @since 2015-09-17 16:57
 */
public class HughieDragGridView extends LinearLayout {
	// 九个 Item
	private HughieGridViewItem mGridItem11, mGridItem12, mGridItem13, mGridItem21,
			mGridItem22, mGridItem23;
	
	// 四条分割线
	private View mGridLineV1, mGridLineV2, mGridLineH1;
	
	// 装入九个 Item
	private ArrayList<HughieGridViewItem> mGridItemList;
	
	// 装载每个 Item 中的数据
	private ArrayList<HashMap<String, String>> mGridList;
	
	// 可编辑状态的 Item 指针
	private HughieGridViewItem mGridItemEdit;
	// 是否可以移动 Item 的标志位
	boolean mCanMove = false;
	
	//触摸移动的偏移量
	private float mLastX, mLastY;
	//触摸移动的新的位置
	private float mEndX, mEndY;
	//触摸移动的旧的位置
	private float mStartX, mStartY;
	
	//grid item的宽度和高度
	private int mGridItemWidth, mGridItemHeight;
	
	private ScaleAnimation mDeleteAnimation;					//删除Item的动画
	private TranslateAnimation mRight2Up1Animation;		//右2上1动画
	private TranslateAnimation mLeftAnimation;					//向左移动一格的动画
	
	private OnDragGridItemClickListener mOnDragGridItemClickListener;
	
	public HughieDragGridView(Context context) {
		super(context);
	}
	
	public HughieDragGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGridView(context);
	}
	
	//初始化布局，将布局加进来
	private void initGridView(Context context){
		LayoutInflater mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLayoutInflater.inflate(R.layout.layout_hughie_game_grid_view, this);
		
		mGridItem11 = (HughieGridViewItem) findViewById(R.id.game_grid_item_11_gvim);
		mGridItem12 = (HughieGridViewItem) findViewById(R.id.game_grid_item_12_gvim);
		mGridItem13 = (HughieGridViewItem) findViewById(R.id.game_grid_item_13_gvim);
		mGridItem21 = (HughieGridViewItem) findViewById(R.id.game_grid_item_21_gvim);
		mGridItem22 = (HughieGridViewItem) findViewById(R.id.game_grid_item_22_gvim);
		mGridItem23 = (HughieGridViewItem) findViewById(R.id.game_grid_item_23_gvim);
		
		mGridLineV1 = findViewById(R.id.game_grid_vertical_divided_line01);
		mGridLineV2 = findViewById(R.id.game_grid_vertical_divided_line02);
		mGridLineH1 = findViewById(R.id.game_grid_horizonal_divided_line01);
		
		// 用此 list 存储所有的 Item
		mGridItemList = new ArrayList<HughieGridViewItem>();
		mGridItemList.add(mGridItem11);
		mGridItemList.add(mGridItem12);
		mGridItemList.add(mGridItem13);
		mGridItemList.add(mGridItem21);
		mGridItemList.add(mGridItem22);
		mGridItemList.add(mGridItem23);
		
		//设置item click监听器
		setGridItemClickListener();
		
	}
	
	// 在这里先截取整个layout 的onTouch事件然后在继续往下分发，用于控制 Item 的移动
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// 若是点击事件则获取位置装入变量中
		if(ev.getAction() == MotionEvent.ACTION_DOWN) {
			mLastX = (int) ev.getRawX();
			mLastY = (int) ev.getRawY();
		}
		
		if (mGridItemEdit != null) {
			switch(ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 判断点击的位置是否在当前可编辑 Item 的范围内
					if(ev.getX() <= mGridItemEdit.getRight() && ev.getX() >= mGridItemEdit.getLeft() &&
							ev.getY() <= mGridItemEdit.getBottom() && ev.getY() >= mGridItemEdit.getTop()){
						mCanMove = true;
						mGridItemEdit.bringToFront();
					} else {
						// 点击的非子项的空白处，取消编辑状态
						if(mGridItemEdit != null) {
							HughieGridViewItem mGridCancel = point2Item(ev.getX(), ev.getY());
							if(mGridCancel == null) {
								cancelEditState();
							} else if(mGridCancel.getVisibility() == View.INVISIBLE) {
								cancelEditState();
							}
						}
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if(mCanMove == true) {
						// 若up与down都为同一个点则认为是点击事件不做处理继续往下传递
						if(Math.abs(ev.getRawX() - mLastX) > 0.5 && Math.abs(ev.getRawY() - mLastY) > 0.5) {
							mEndX = (int)ev.getRawX() - mLastX;
							mEndY = (int)ev.getRawY() - mLastY;
							
							// 建立动画移动 Item，从旧的位置移动到新的位置
							TranslateAnimation mTranslateAnimation = new TranslateAnimation(
									mStartX, mEndX, mStartY, mEndY);
							mTranslateAnimation.setDuration(0);
							mTranslateAnimation.setFillAfter(true);
							
							//旧的位置
							mStartX = (int)ev.getRawX() - mLastX;
							mStartY = (int)ev.getRawY() - mLastY;
						} else {
							// 点击事件向下传递
							return false;
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					// 若up与down都为同一个点则认为是点击事件不做处理继续往下传递
					if(Math.abs(ev.getRawX() - mLastX) <= 1 && Math.abs(ev.getRawY() - mLastY) <= 1) {
						mCanMove = false;
						endGridMove();
						return false;
					}
					
					if(mCanMove == true) {
						for(int i = 0; i < mGridList.size(); i++) {
							// 先不对自身进行比对
							if(i == mGridItemEdit.getmGridNum()) {
								// 若 mc 正好是最后一个 Item 的话直接返回原处,若不是则先跳过
								if(i == mGridList.size() - 1) {
									moveGridItemToOrigin(ev, mGridItemEdit, ev.getRawX() - mLastX, ev.getRawY() - mLastY);
									mCanMove = false;
									endGridMove();
									return true;
								}
								continue;
							}
							
							// 判断up的区域在哪个Item的范围内
							HughieGridViewItem mGvim = mGridItemList.get(i);
							int t = mGvim.getTop();
							int b =mGvim.getBottom();
							int l = mGvim.getLeft();
							int r = mGvim.getRight();
							
							// 判断up事件的范围是否在Item内
							if(ev.getX() >= l && ev.getX() <= r && ev.getY() >= t && ev.getY() <= b) {
								// 移动对换 Item
								changeGridItemPosWithAnimation(mGridItemEdit.getmGridNum(), i, ev.getRawX() - mLastX, 
										ev.getRawY() - mLastY);
								endGridMove();
								mGridItemEdit = null;
								// 找到相应的Item进行交换以后退出循环
								return true;
							}
							
							// 若没有找到则回到原位
							if(i == mGridList.size() - 1) {
								// 使被移动的Item返回原位,ox oy 为view当前的位置
								moveGridItemToOrigin(ev, mGridItemEdit, ev.getRawX() - mLastX, ev.getRawY() - mLastY);
								mCanMove = false;
								endGridMove();
								return true;
							}
						}
					}
					
					mCanMove = false;
					endGridMove();
					break;
			}
		}
		
		return false;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
		int mHeightSize = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(mWidthSize, mHeightSize);
		resetGridViewSize(mWidthSize, mHeightSize);
		int mCount = getChildCount();
		for(int i = 0; i < mCount; i++) {
			View mChildView = getChildAt(i);
			mChildView.measure(widthMeasureSpec, heightMeasureSpec);
		}
	}
	
	/**
	 * 重新配置控件的大小
	 * @param width
	 * @param height
	 * @return
	 */
	public void resetGridViewSize(int width, int height) {
		ViewGroup.LayoutParams mLayoutParams = mGridItem11.getLayoutParams();
		mLayoutParams.width = width / 3;
		mLayoutParams.height = height / 2;
		mGridItem11.setLayoutParams(mLayoutParams);
		
		mLayoutParams = mGridItem12.getLayoutParams();
		mLayoutParams.width = width / 3;
		mLayoutParams.height = height / 2;
		mGridItem12.setLayoutParams(mLayoutParams);
		
		mLayoutParams = mGridItem13.getLayoutParams();
		mLayoutParams.width = width / 3;
		mLayoutParams.height = height / 2;
		mGridItem13.setLayoutParams(mLayoutParams);
		
		mLayoutParams = mGridItem21.getLayoutParams();
		mLayoutParams.width = width / 3;
		mLayoutParams.height = height / 2;
		mGridItem21.setLayoutParams(mLayoutParams);
		
		mLayoutParams = mGridItem22.getLayoutParams();
		mLayoutParams.width = width / 3;
		mLayoutParams.height = height / 2;
		mGridItem22.setLayoutParams(mLayoutParams);
		
		mLayoutParams = mGridItem23.getLayoutParams();
		mLayoutParams.width = width / 3;
		mLayoutParams.height = height / 2;
		mGridItem23.setLayoutParams(mLayoutParams);
	}
	
	// 使用 list 来给所有的 item 装入数据
	// 外部内部均有调用
	public void setGridItemList(ArrayList<HashMap<String, String>> list) {
		this.mGridList = list;
		int mSize = mGridList.size();
		for(int i = 0; i < 6; i++) {
			HughieGridViewItem mItem = mGridItemList.get(i);
			mItem.clean();
			mItem.setVisibility(i <= mSize ? View.VISIBLE : View.INVISIBLE);
		}
		
		for(int i = 0; i < mGridList.size(); i++) {
			try {
				HashMap<String, String> map = mGridList.get(i);
				String str1 = map.get("str1");
				String code = map.get("code");
				setGridItemText(i, str1, code);
				int mIdImg = Integer.parseInt(map.get("img"));
				setGridItemImage(i, mIdImg);
			} catch(Exception e) {
				HughieLoggerManager.printStackTrace(e);
				continue;
			}
		}
	}
	
	// 给单个item设置名称
	public void setGridItemText(int i, String str1, String code) {
		HughieGridViewItem mItem = mGridItemList.get(i);
		mItem.setGridItemText(str1, code);
	}
	
	// 给单个item设置图标
	public void setGridItemImage(int i, int resid) {
		int img[] = {R.drawable.imgv_game_grid_item_games, R.drawable.imgv_game_grid_item_rankings, 
				R.drawable.imgv_game_grid_item_shop, R.drawable.imgv_game_grid_item_kit, 
				R.drawable.imgv_game_grid_item_about};
		HughieGridViewItem mItem = mGridItemList.get(i);
		mItem.setItemImage(img[resid - 1]);
	}
	
	public ArrayList<HashMap<String, String>> getGridItemList() {
		return this.mGridList;
	}
	
	public void setOnDragGridItemClickListener(OnDragGridItemClickListener onDragGridItemClickListener) {
		this.mOnDragGridItemClickListener = onDragGridItemClickListener;
	}
	
	// 初始化grid item宽高和需要的动画
	private void gridItemWHAndInitAnimation() {
		mGridItemWidth = mGridItem11.getWidth();
		mGridItemHeight = mGridItem11.getHeight();
		
		mDeleteAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mDeleteAnimation.setDuration(300);	// 设置动画持续时间
		mDeleteAnimation.setInterpolator(new LinearInterpolator());
		
		// 向左移动一格
		mLeftAnimation = new TranslateAnimation(0, -mGridItemWidth, 0, 0);
		mLeftAnimation.setDuration(300); 	//设置动画持续时间
		mLeftAnimation.setInterpolator(new LinearInterpolator());
		
		// 右2上1
		mRight2Up1Animation = new TranslateAnimation(0, mGridItemWidth * 2, 0, -mGridItemHeight);
		mRight2Up1Animation.setDuration(300);
		mRight2Up1Animation.setInterpolator(new LinearInterpolator());
	}
	
	// 点击删除号后实现的动画和自动排版
	public void startGridMove(int number) {
		// 隐藏
		int mSize = mGridList.size();
		for(int i = mSize; i < 6; i++) {
			mGridItemList.get(i).setVisibility(View.INVISIBLE);
		}
		
		gridItemWHAndInitAnimation(); 	// 初始化grid item宽高和需要的动画
		
		// 移动的动画
		mGridList.remove(number);
		HughieGridViewItem mItemDelete = mGridItemList.get(number);
		for(int i = number; i < mSize; i++) {
			if(i == 3) {
				mGridItemList.get(i).startAnimation(mRight2Up1Animation);
			} else {
				mGridItemList.get(i).startAnimation(mLeftAnimation);
			}
		}
		
		mItemDelete.startAnimation(mDeleteAnimation);
		setAnimationListener(mItemDelete, mDeleteAnimation);
	}
	
	// 结束移动或编辑状态
	private void endGridMove() {
		mCanMove = false;
		mGridLineV1.bringToFront(); 
		mGridLineV2.bringToFront();
		mGridLineH1.bringToFront();
	}
	
	// 检测有没有处于编辑状态的项，取消编辑状态
	private boolean cancelEditState() {
		boolean b = false;
		//检查是否存在另一个Item是处于编辑状态
		for(int i = 0; i < mGridItemList.size(); i++) {
			HughieGridViewItem c = mGridItemList.get(i);
			//若找到有处于编辑状态的Item
			if(c.getmItemState() == HughieGridViewItem.GRID_ITEM_EDIT) {
				b = true;
				c.cancelEditState();
				//取消处于编辑状态的Item的编辑状态
				mGridItemEdit = null;
				mCanMove = false;
				endGridMove();
			}
		}
		
		return b;
	}
	
	// 使被移动的Item返回原位, ox oy 为 view 当前的位置
	private void moveGridItemToOrigin(MotionEvent ev, final HughieGridViewItem gvim,
			float ox, float oy) {
		TranslateAnimation mTranslateAnimation = new TranslateAnimation(ox, 0, oy, 0);
		mTranslateAnimation.setDuration(300);	// 设置动画持续时间
		mTranslateAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// 返回原位后进行放大
				ScaleAnimation mScaleAnimation = new ScaleAnimation(1.0f, 1.05f, 1.0f, 1.05f, 
						Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				mScaleAnimation.setDuration(200);
				mScaleAnimation.setFillAfter(true);
				gvim.startAnimation(mScaleAnimation);
			}
		});
		
		gvim.startAnimation(mTranslateAnimation);
	}
	
	// 移动的移动新位置，其它顺序排列
	// i是主动移动的一个，j是新位置
	private void changeGridItemPosWithAnimation(int i, int j, float ox, float oy) {
		// 如果i比j小, 它后面的依次减1, i=新位置
		if(i < j) {
			HughieGridViewItem mItemi = mGridItemList.get(i);
			HughieGridViewItem mItemj = mGridItemList.get(j);
			
			for(int k = i; k < j; k++) {
				changeGridMap(k, k + 1);
				HughieGridViewItem mItemk = mGridItemList.get(k);
				HughieGridViewItem mItemk1 = mGridItemList.get(k + 1);
				createPosChangedTranslateAnimation(mItemk1, 0, mItemk.getLeft() - mItemk1.getLeft(), 
						0, mItemk.getBottom() - mItemk1.getBottom());
			}
			
			createPosChangedTranslateAnimation(mItemi, ox, mItemj.getLeft() - mItemi.getLeft(), 
					oy, mItemj.getBottom() - mItemi.getBottom());
		// 如果i比j大,它前面的依次加1,i=新位置
		}else if(j < i) {
			HughieGridViewItem mItemi = mGridItemList.get(i);
			HughieGridViewItem mItemj = mGridItemList.get(j);
			
			for(int k = i; k > j; k--) {
				changeGridMap(k, k - 1);
				HughieGridViewItem mItemk = mGridItemList.get(k);
				HughieGridViewItem mItemk1 = mGridItemList.get(k - 1); 
				createPosChangedTranslateAnimation(mItemk1, 0, mItemk.getLeft() - mItemk1.getLeft(), 
						0, mItemk.getBottom() - mItemk1.getBottom());
			}
			
			createPosChangedTranslateAnimation(mItemi, ox, mItemj.getLeft() - mItemi.getLeft(), 
					oy, mItemj.getBottom() - mItemi.getBottom());
		}
		
		if(mOnDragGridItemClickListener != null) {
			mOnDragGridItemClickListener.OnGridItemPosChanged();
		}
	}
	
	//交换两个位置的mGridList的内容
	private void changeGridMap(int i, int j) {
		//确保 j > i
		if (i > j) {
			int t = j;
			j = i;
			i = t;
		} else if(i == j) {
			return;
		}
		
		HashMap<String, String> map1 = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		map1 = mGridList.get(i);
		map2 = mGridList.get(j);
		
		mGridList.remove(j);
		mGridList.remove(i);
		mGridList.add(i, map2);
		mGridList.add(j, map1);
	}
	
	// 创建位移动画(此方法专门用于 changeGridItemPosWithAnimation（） 方法中，别的方法不使用)
	private void createPosChangedTranslateAnimation(View v, float fx, float tx,
			float fy, float ty) {
		TranslateAnimation mTranslateAnimation = new TranslateAnimation(fx, tx, fy, ty);
		mTranslateAnimation.setDuration(300);
		mTranslateAnimation.setInterpolator(new LinearInterpolator());
		mTranslateAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				setGridItemList(mGridList);
			}
		});
		
		v.startAnimation(mTranslateAnimation);
	} 
	
	// 检测点(x,y)处于哪个项中
	private HughieGridViewItem point2Item(float x, float y) {
		for(int i = 0; i < mGridItemList.size(); i++) {
			HughieGridViewItem c = mGridItemList.get(i);
			int t = c.getTop();
			int b = c.getBottom();
			int l = c.getLeft();
			int r = c.getRight();
			
			// 判断up事件的范围是否在Item内, 状态栏高度为25
			if(x >= l && x <= r && y >= t && y <= b){
				return c;
			}
		}
		
		return null;
	}
	
	// 给Item设置Listener
	private void setGridItemClickListener() {
		for(int i = 0; i < mGridItemList.size(); i++) {
			HughieGridViewItem c = mGridItemList.get(i);
			
			// 设置编号
			c.setmGridNum(i);
			// 长按接口（正常状态下长按调用）
			c.setOnGridItemClickListener(mOnGridItemClickListener);
		}
	}
	
	private void setAnimationListener(final View v, Animation animation) {
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				setGridItemList(mGridList);
				v.setVisibility(View.VISIBLE);
				v.clearAnimation();
			}
		});
	}
	
	// 取消其他的 Item 的叉号显示被点击的 Item 的叉号
	private OnGridItemClickListener mOnGridItemClickListener = new OnGridItemClickListener() {
		@Override
		public void OnGridItemLongClick(View v) {
			// 取消其他Item的叉号
			if(mGridItemEdit != null) {
				mGridItemEdit.cancelEditState();
			}
			
			// 显示选中的 Item 的叉号使其处于可编辑的状态
			((HughieGridViewItem)v).displayEditState();
			// 将mGridItemEdit指针指向被 LongClick 的 Item
			mGridItemEdit = (HughieGridViewItem) v;
			mGridItemEdit.bringToFront();
			
			mCanMove = true;
		}

		@Override
		public void OnGridItemAfterDeleteClick(int number) {
			boolean bDelete = true;
			if(mOnDragGridItemClickListener != null) {
				bDelete = mOnDragGridItemClickListener.OnGridItemDelete(number);
			}
			
			if(bDelete) {
				startGridMove(number);
				mGridItemEdit = null;
			} else {
				mGridItemEdit.cancelEditState();
			}
		}

		@Override
		public void OnGridItemWhenEditClick(View v, int number) {
			((HughieGridViewItem)v).cancelEditState();
			mGridItemEdit = null;
		}

		@Override
		public void OnGridItemWhenNormalClick(String code) {
			// 等于 false 时表示没有显示叉号的 Item, 可以直接交给上层调用
			boolean b = cancelEditState();
			if(b == false) {
				// 通过（没有 Item 处于编辑状态）,留给上层调用
				mOnDragGridItemClickListener.OnGridItemWhenNormalState(code);
			}
		}

		@Override
		public void OnGridItemWhenBlankClick() {
			// 如果有处理编辑状态的项，先取消
			if(!cancelEditState()) {
				mOnDragGridItemClickListener.OnGridItemWhenBlankState();
			}
		}
	};
	
	
	public interface OnDragGridItemClickListener {
		public void OnGridItemPosChanged();							//移动grid的位置
		public boolean OnGridItemDelete(int number);				//是否删除grid item
		public void OnGridItemWhenNormalState(String code);	//grid item为正常状态下的点击监听
		public void OnGridItemWhenBlankState();						//grid item为空的状态下的点击监听
	}
}
