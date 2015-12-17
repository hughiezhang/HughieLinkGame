package com.hughie.linkgame.entity;

import java.io.Serializable;

/**
 * 游戏关卡item实体
 * @ClassName: LevelItemDetail
 * @author hughiezhang
 * @since 2015-10-13 16:20
 */
public class LevelItemDetail implements Serializable {
	private static final long serialVersionUID = 2940088371515246767L;
	
	private int stateFlag;							//item的状态flag
	private boolean unlockFlag = true;	//item的unlock状态
	
	public int getStateFlag() {
		return stateFlag;
	}
	public void setStateFlag(int stateFlag) {
		this.stateFlag = stateFlag;
	}
	public boolean isUnlockFlag() {
		return unlockFlag;
	}
	public void setUnlockFlag(boolean unlockFlag) {
		this.unlockFlag = unlockFlag;
	}
	
	@Override
	public String toString() {
		return "LevelItemDetail [stateFlag=" + stateFlag + ", unlockFlag="
				+ unlockFlag + "]";
	}
}
