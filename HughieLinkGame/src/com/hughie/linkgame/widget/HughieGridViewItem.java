package com.hughie.linkgame.widget;

import com.hughie.linkgame.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ����ק��ɾ����gridview��item����
 * @ClassName: HughieGridViewItem
 * @author hughiezhang
 * @since 2015-09-17 17:52
 */
public class HughieGridViewItem extends LinearLayout {
	private RelativeLayout mItemLayout;	//item����
	private ImageView mItemIconImgv;	//item icon
	private TextView mItemNameTv;		//item name
	private ImageButton mItemDeleteImgb;
	
	// ����״̬
	private int mItemState;
	
	public final static int GRID_ITEM_NORMAL = 1;	//������ʾ����״̬
	public final static int GRID_ITEM_EDIT = 2;	//�ɱ༭״̬
	public final static int GRID_ITEM_BLANK = 3;	//�ȴ��������״̬
	
	private int mGridNum = -1;	//item�ı��
	private String mGridCode;		//item������
	
	private OnGridItemClickListener mOnGridItemClickListener;
	
	public HughieGridViewItem(Context context) {
		super(context);
	}
	
	public HughieGridViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGridItem(context);
	}
	
	//��ʼ�����֣������ּӽ���
	private void initGridItem(Context context) {
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(R.layout.layout_hughie_game_grid_item, this);
		
		mItemState = GRID_ITEM_BLANK;
		
		initEmptyGridItemListener();
		
		mItemLayout = (RelativeLayout) findViewById(R.id.game_grid_item_layout);
		mItemIconImgv = (ImageView) findViewById(R.id.game_grid_item_icon_imgv);
		mItemNameTv = (TextView) findViewById(R.id.game_grid_item_name_tv);
		mItemDeleteImgb = (ImageButton) findViewById(R.id.game_grid_item_delete_imgb);
		
		mItemLayout.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// ������ʾ״̬�±� LongClick
				if(mOnGridItemClickListener != null && mItemState == GRID_ITEM_NORMAL) {
					mOnGridItemClickListener.OnGridItemLongClick(HughieGridViewItem.this);
				}
				
				return true;
			}
		});
		
		// ��ű��������մ�Item
		mItemDeleteImgb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ��ִ����С���������µ���ִ����� Item ����ز���
				ScaleAnimation mScaleAnimation = new ScaleAnimation(1.05f, 1.0f, 1.05f, 1.0f, 
						Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				mScaleAnimation.setDuration(200);
				mScaleAnimation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// ���ݸ� HughieDragGridView ����,�� HughieDragGridView ��ִ����ص��������
						mOnGridItemClickListener.OnGridItemAfterDeleteClick(mGridNum);
					}
				});
				
				startAnimation(mScaleAnimation);
			}
		});
		
		mItemLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ������ʾ��������� Click
				if(mItemState == GRID_ITEM_NORMAL) {
					mOnGridItemClickListener.OnGridItemWhenNormalClick(mGridCode);
				}
				
				// ���ڱ༭״̬�� Click
				if(mItemState == GRID_ITEM_EDIT) {
					mOnGridItemClickListener.OnGridItemWhenEditClick(HughieGridViewItem.this, mGridNum);
				// ���ڵȴ����״̬�±� Click
				} else if(mItemState == GRID_ITEM_BLANK) {
					mOnGridItemClickListener.OnGridItemWhenBlankClick();
				}
			}
		});
	}
	
	public void displayEditState() {
		mItemState = GRID_ITEM_EDIT;
		mItemDeleteImgb.setVisibility(View.VISIBLE);
		mItemLayout.setBackgroundColor(0xcceaeaea);
		
		// �Ŵ󶯻�
		ScaleAnimation mScaleAnimation = new ScaleAnimation(1.0f, 1.05f, 1.0f, 1.05f, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mScaleAnimation.setDuration(200);
		mScaleAnimation.setFillAfter(true);
		startAnimation(mScaleAnimation);
	}
	
	// ȡ�����,���ص�������״̬
	public void cancelEditState() {
		mItemState = GRID_ITEM_NORMAL;
		mItemLayout.setBackgroundColor(0xfff9f9f9);
		
		ScaleAnimation mScaleAnimation = new ScaleAnimation(1.05f, 1.0f, 1.05f, 1.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mScaleAnimation.setDuration(200);
		mScaleAnimation.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mItemDeleteImgb.setVisibility(View.GONE);
			}	
		});
		
		startAnimation(mScaleAnimation);
	}
	
	public void setGridItemText(String str1, String code) {
		setItemNameText(str1);
		this.mGridCode = code;
		mItemState = GRID_ITEM_NORMAL;
	}
	
	public void clean() {
		mItemLayout.setBackgroundColor(0xfff9f9f9);
		mItemDeleteImgb.setVisibility(View.GONE);
		mItemIconImgv.setVisibility(View.VISIBLE);
		mItemIconImgv.setImageResource(R.drawable.imgv_game_grid_item_more);
		mItemNameTv.setVisibility(View.VISIBLE);
		mItemNameTv.setText(R.string.str_game_grid_item_more_txt);
		mGridCode = null;
		mItemState = GRID_ITEM_BLANK;
	}
	
	// ��ʼ���հ׵ļ�������ֹ��ָ��
	private void initEmptyGridItemListener(){
		mOnGridItemClickListener = new OnGridItemClickListener() {
			@Override
			public void OnGridItemLongClick(View v) {
				
			}

			@Override
			public void OnGridItemAfterDeleteClick(int number) {
				
			}

			@Override
			public void OnGridItemWhenEditClick(View v, int number) {
				
			}

			@Override
			public void OnGridItemWhenNormalClick(String code) {
				
			}

			@Override
			public void OnGridItemWhenBlankClick() {
				
			}
		};
	}
	
	//����item���Ƶ�������Ϣ
	public void setItemNameText(String text) {
		mItemNameTv.setText(text);
		mItemState = GRID_ITEM_NORMAL;
	}
	
	public void setItemImage(int resId) {
		mItemIconImgv.setImageResource(resId);
	}

	public int getmItemState() {
		return mItemState;
	}

	public void setmItemState(int mItemState) {
		this.mItemState = mItemState;
	}

	public int getmGridNum() {
		return mGridNum;
	}

	public void setmGridNum(int mGridNum) {
		this.mGridNum = mGridNum;
	}
	
	public String getmGridCode() {
		return mGridCode;
	}

	public void setmGridCode(String mGridCode) {
		this.mGridCode = mGridCode;
	}
	
	
	// �����ϲ�����������Ӧ�ļ�����
	public void setOnGridItemClickListener(OnGridItemClickListener onGridItemClickListener) {
		this.mOnGridItemClickListener = onGridItemClickListener;
	}
	
	
	public interface OnGridItemClickListener {
		public void OnGridItemLongClick(View v);								// �����ӿڣ�����״̬�³������ã�
		public void OnGridItemAfterDeleteClick(int number);				// ɾ���ӿڣ�ɾ�������
		public void OnGridItemWhenEditClick(View v, int number);		// �༭״̬�µ������
		public void OnGridItemWhenNormalClick(String code);			//	����״̬�µ������
		public void OnGridItemWhenBlankClick();
	}
}
