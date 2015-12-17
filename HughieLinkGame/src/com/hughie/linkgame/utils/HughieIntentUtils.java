package com.hughie.linkgame.utils;

import android.content.Intent;
import android.os.Parcelable;

public class HughieIntentUtils {
	
	public static void setValueToIntent(Intent intent, String key, Object val) {
		if( null == key || null == val) return;
		if(val instanceof Boolean)
			intent.putExtra(key, (boolean) val);
		else if(val instanceof Boolean[])
			intent.putExtra(key, (Boolean[]) val);
		else if(val instanceof String)
			intent.putExtra(key, (String)val);
		else if(val instanceof String[])
			intent.putExtra(key, (String[])val);
		else if(val instanceof Integer)
			intent.putExtra(key, (Integer) val);
		else if(val instanceof Integer[])
			intent.putExtra(key, (Integer[]) val);
		else if(val instanceof Long)
			intent.putExtra(key, (Long) val);
		else if(val instanceof Long[])
			intent.putExtra(key, (Long[]) val);
		else if(val instanceof Double)
			intent.putExtra(key, (Double) val);
		else if(val instanceof Double[])
			intent.putExtra(key, (Double[]) val);
		else if(val instanceof Float)
			intent.putExtra(key, (Float) val);
		else if(val instanceof Float[])
			intent.putExtra(key, (Float[]) val);
		else if(val instanceof Parcelable)
			intent.putExtra(key, (Parcelable) val);
		else if(val instanceof Parcelable[])
			intent.putExtra(key, (Parcelable[]) val);
		else
			throw new IllegalArgumentException("Not support data Type!");
	}
}
