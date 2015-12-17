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
 * ����ק��ɾ����gridview
 * @ClassName: HughieDragGridView
 * @author hughiezhang
 * @since 2015-09-17 16:57
 */
public class HughieDragGridView extends LinearLayout {
	// �Ÿ� Item
	private HughieGridViewItem mGridItem11, mGridItem12, mGridItem13, mGridItem21,
			mGridItem22, mGridItem23;
	
	// �����ָ���
	private View mGridLineV1, mGridLineV2, mGridLineH1;
	
	// װ��Ÿ� Item
	private ArrayList<HughieGridViewItem> mGridItemList;
	
	// װ��ÿ�� Item �е�����
	private ArrayList<HashMap<String, String>> mGridList;
	
	// �ɱ༭״̬�� Item ָ��
	private HughieGridViewItem mGridItemEdit;
	// �Ƿ�����ƶ� Item �ı�־λ
	boolean mCanMove = false;
	
	//�����ƶ���ƫ����
	private float mLastX, mLastY;
	//�����ƶ����µ�λ��
	private float mEndX, mEndY;
	//�����ƶ��ľɵ�λ��
	private float mStartX, mStartY;
	
	//grid item�Ŀ�Ⱥ͸߶�
	private int mGridItemWidth, mGridItemHeight;
	
	private ScaleAnimation mDeleteAnimation;					//ɾ��Item�Ķ���
	private TranslateAnimation mRight2Up1Animation;		//��2��1����
	private TranslateAnimation mLeftAnimation;					//�����ƶ�һ��Ķ���
	
	private OnDragGridItemClickListener mOnDragGridItemClickListener;
	
	public HughieDragGridView(Context context) {
		super(context);
	}
	
	public HughieDragGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGridView(context);
	}
	
	//��ʼ�����֣������ּӽ���
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
		
		// �ô� list �洢���е� Item
		mGridItemList = new ArrayList<HughieGridViewItem>();
		mGridItemList.add(mGridItem11);
		mGridItemList.add(mGridItem12);
		mGridItemList.add(mGridItem13);
		mGridItemList.add(mGridItem21);
		mGridItemList.add(mGridItem22);
		mGridItemList.add(mGridItem23);
		
		//����item click������
		setGridItemClickListener();
		
	}
	
	// �������Ƚ�ȡ����layout ��onTouch�¼�Ȼ���ڼ������·ַ������ڿ��� Item ���ƶ�
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// ���ǵ���¼����ȡλ��װ�������
		if(ev.getAction() == MotionEvent.ACTION_DOWN) {
			mLastX = (int) ev.getRawX();
			mLastY = (int) ev.getRawY();
		}
		
		if (mGridItemEdit != null) {
			switch(ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// �жϵ����λ���Ƿ��ڵ�ǰ�ɱ༭ Item �ķ�Χ��
					if(ev.getX() <= mGridItemEdit.getRight() && ev.getX() >= mGridItemEdit.getLeft() &&
							ev.getY() <= mGridItemEdit.getBottom() && ev.getY() >= mGridItemEdit.getTop()){
						mCanMove = true;
						mGridItemEdit.bringToFront();
					} else {
						// ����ķ�����Ŀհ״���ȡ���༭״̬
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
						// ��up��down��Ϊͬһ��������Ϊ�ǵ���¼���������������´���
						if(Math.abs(ev.getRawX() - mLastX) > 0.5 && Math.abs(ev.getRawY() - mLastY) > 0.5) {
							mEndX = (int)ev.getRawX() - mLastX;
							mEndY = (int)ev.getRawY() - mLastY;
							
							// ���������ƶ� Item���Ӿɵ�λ���ƶ����µ�λ��
							TranslateAnimation mTranslateAnimation = new TranslateAnimation(
									mStartX, mEndX, mStartY, mEndY);
							mTranslateAnimation.setDuration(0);
							mTranslateAnimation.setFillAfter(true);
							
							//�ɵ�λ��
							mStartX = (int)ev.getRawX() - mLastX;
							mStartY = (int)ev.getRawY() - mLastY;
						} else {
							// ����¼����´���
							return false;
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					// ��up��down��Ϊͬһ��������Ϊ�ǵ���¼���������������´���
					if(Math.abs(ev.getRawX() - mLastX) <= 1 && Math.abs(ev.getRawY() - mLastY) <= 1) {
						mCanMove = false;
						endGridMove();
						return false;
					}
					
					if(mCanMove == true) {
						for(int i = 0; i < mGridList.size(); i++) {
							// �Ȳ���������бȶ�
							if(i == mGridItemEdit.getmGridNum()) {
								// �� mc ���������һ�� Item �Ļ�ֱ�ӷ���ԭ��,��������������
								if(i == mGridList.size() - 1) {
									moveGridItemToOrigin(ev, mGridItemEdit, ev.getRawX() - mLastX, ev.getRawY() - mLastY);
									mCanMove = false;
									endGridMove();
									return true;
								}
								continue;
							}
							
							// �ж�up���������ĸ�Item�ķ�Χ��
							HughieGridViewItem mGvim = mGridItemList.get(i);
							int t = mGvim.getTop();
							int b =mGvim.getBottom();
							int l = mGvim.getLeft();
							int r = mGvim.getRight();
							
							// �ж�up�¼��ķ�Χ�Ƿ���Item��
							if(ev.getX() >= l && ev.getX() <= r && ev.getY() >= t && ev.getY() <= b) {
								// �ƶ��Ի� Item
								changeGridItemPosWithAnimation(mGridItemEdit.getmGridNum(), i, ev.getRawX() - mLastX, 
										ev.getRawY() - mLastY);
								endGridMove();
								mGridItemEdit = null;
								// �ҵ���Ӧ��Item���н����Ժ��˳�ѭ��
								return true;
							}
							
							// ��û���ҵ���ص�ԭλ
							if(i == mGridList.size() - 1) {
								// ʹ���ƶ���Item����ԭλ,ox oy Ϊview��ǰ��λ��
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
	 * �������ÿؼ��Ĵ�С
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
	
	// ʹ�� list �������е� item װ������
	// �ⲿ�ڲ����е���
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
	
	// ������item��������
	public void setGridItemText(int i, String str1, String code) {
		HughieGridViewItem mItem = mGridItemList.get(i);
		mItem.setGridItemText(str1, code);
	}
	
	// ������item����ͼ��
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
	
	// ��ʼ��grid item��ߺ���Ҫ�Ķ���
	private void gridItemWHAndInitAnimation() {
		mGridItemWidth = mGridItem11.getWidth();
		mGridItemHeight = mGridItem11.getHeight();
		
		mDeleteAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mDeleteAnimation.setDuration(300);	// ���ö�������ʱ��
		mDeleteAnimation.setInterpolator(new LinearInterpolator());
		
		// �����ƶ�һ��
		mLeftAnimation = new TranslateAnimation(0, -mGridItemWidth, 0, 0);
		mLeftAnimation.setDuration(300); 	//���ö�������ʱ��
		mLeftAnimation.setInterpolator(new LinearInterpolator());
		
		// ��2��1
		mRight2Up1Animation = new TranslateAnimation(0, mGridItemWidth * 2, 0, -mGridItemHeight);
		mRight2Up1Animation.setDuration(300);
		mRight2Up1Animation.setInterpolator(new LinearInterpolator());
	}
	
	// ���ɾ���ź�ʵ�ֵĶ������Զ��Ű�
	public void startGridMove(int number) {
		// ����
		int mSize = mGridList.size();
		for(int i = mSize; i < 6; i++) {
			mGridItemList.get(i).setVisibility(View.INVISIBLE);
		}
		
		gridItemWHAndInitAnimation(); 	// ��ʼ��grid item��ߺ���Ҫ�Ķ���
		
		// �ƶ��Ķ���
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
	
	// �����ƶ���༭״̬
	private void endGridMove() {
		mCanMove = false;
		mGridLineV1.bringToFront(); 
		mGridLineV2.bringToFront();
		mGridLineH1.bringToFront();
	}
	
	// �����û�д��ڱ༭״̬���ȡ���༭״̬
	private boolean cancelEditState() {
		boolean b = false;
		//����Ƿ������һ��Item�Ǵ��ڱ༭״̬
		for(int i = 0; i < mGridItemList.size(); i++) {
			HughieGridViewItem c = mGridItemList.get(i);
			//���ҵ��д��ڱ༭״̬��Item
			if(c.getmItemState() == HughieGridViewItem.GRID_ITEM_EDIT) {
				b = true;
				c.cancelEditState();
				//ȡ�����ڱ༭״̬��Item�ı༭״̬
				mGridItemEdit = null;
				mCanMove = false;
				endGridMove();
			}
		}
		
		return b;
	}
	
	// ʹ���ƶ���Item����ԭλ, ox oy Ϊ view ��ǰ��λ��
	private void moveGridItemToOrigin(MotionEvent ev, final HughieGridViewItem gvim,
			float ox, float oy) {
		TranslateAnimation mTranslateAnimation = new TranslateAnimation(ox, 0, oy, 0);
		mTranslateAnimation.setDuration(300);	// ���ö�������ʱ��
		mTranslateAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// ����ԭλ����зŴ�
				ScaleAnimation mScaleAnimation = new ScaleAnimation(1.0f, 1.05f, 1.0f, 1.05f, 
						Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				mScaleAnimation.setDuration(200);
				mScaleAnimation.setFillAfter(true);
				gvim.startAnimation(mScaleAnimation);
			}
		});
		
		gvim.startAnimation(mTranslateAnimation);
	}
	
	// �ƶ����ƶ���λ�ã�����˳������
	// i�������ƶ���һ����j����λ��
	private void changeGridItemPosWithAnimation(int i, int j, float ox, float oy) {
		// ���i��jС, ����������μ�1, i=��λ��
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
		// ���i��j��,��ǰ������μ�1,i=��λ��
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
	
	//��������λ�õ�mGridList������
	private void changeGridMap(int i, int j) {
		//ȷ�� j > i
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
	
	// ����λ�ƶ���(�˷���ר������ changeGridItemPosWithAnimation���� �����У���ķ�����ʹ��)
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
	
	// ����(x,y)�����ĸ�����
	private HughieGridViewItem point2Item(float x, float y) {
		for(int i = 0; i < mGridItemList.size(); i++) {
			HughieGridViewItem c = mGridItemList.get(i);
			int t = c.getTop();
			int b = c.getBottom();
			int l = c.getLeft();
			int r = c.getRight();
			
			// �ж�up�¼��ķ�Χ�Ƿ���Item��, ״̬���߶�Ϊ25
			if(x >= l && x <= r && y >= t && y <= b){
				return c;
			}
		}
		
		return null;
	}
	
	// ��Item����Listener
	private void setGridItemClickListener() {
		for(int i = 0; i < mGridItemList.size(); i++) {
			HughieGridViewItem c = mGridItemList.get(i);
			
			// ���ñ��
			c.setmGridNum(i);
			// �����ӿڣ�����״̬�³������ã�
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
	
	// ȡ�������� Item �Ĳ����ʾ������� Item �Ĳ��
	private OnGridItemClickListener mOnGridItemClickListener = new OnGridItemClickListener() {
		@Override
		public void OnGridItemLongClick(View v) {
			// ȡ������Item�Ĳ��
			if(mGridItemEdit != null) {
				mGridItemEdit.cancelEditState();
			}
			
			// ��ʾѡ�е� Item �Ĳ��ʹ�䴦�ڿɱ༭��״̬
			((HughieGridViewItem)v).displayEditState();
			// ��mGridItemEditָ��ָ�� LongClick �� Item
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
			// ���� false ʱ��ʾû����ʾ��ŵ� Item, ����ֱ�ӽ����ϲ����
			boolean b = cancelEditState();
			if(b == false) {
				// ͨ����û�� Item ���ڱ༭״̬��,�����ϲ����
				mOnDragGridItemClickListener.OnGridItemWhenNormalState(code);
			}
		}

		@Override
		public void OnGridItemWhenBlankClick() {
			// ����д���༭״̬�����ȡ��
			if(!cancelEditState()) {
				mOnDragGridItemClickListener.OnGridItemWhenBlankState();
			}
		}
	};
	
	
	public interface OnDragGridItemClickListener {
		public void OnGridItemPosChanged();							//�ƶ�grid��λ��
		public boolean OnGridItemDelete(int number);				//�Ƿ�ɾ��grid item
		public void OnGridItemWhenNormalState(String code);	//grid itemΪ����״̬�µĵ������
		public void OnGridItemWhenBlankState();						//grid itemΪ�յ�״̬�µĵ������
	}
}
