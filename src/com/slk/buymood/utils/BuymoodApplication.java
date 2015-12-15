package com.slk.buymood.utils;

import java.util.Locale;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DaoConfig;
import com.lidroid.xutils.exception.DbException;
import com.slk.buymood.utils.log.LogUtil;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class BuymoodApplication extends Application {
	public static final String TAG = BuymoodApplication.class.getSimpleName();
	private static Context context;
	private RequestQueue mRequestQueue;
	private static BuymoodApplication mInstance;
	static DbUtils db ;
	// 手机性能参数
		public static float phonePerformValue = 0;
	@Override
	public void onCreate() {
		super.onCreate();
		
		context = getApplicationContext();
		 mInstance = this;
		 // chinese set
        Locale.setDefault(Locale.CHINESE);
        Configuration config = getResources().getConfiguration();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        config.locale = Locale.SIMPLIFIED_CHINESE;
        getResources().updateConfiguration(config, metrics);
        
        initAppDatabase();
        
        
        try {
            // cpu核心数
            int cpuCoreCnt = AndroidUtil.getNumCores();
            // cpu频率（单位MHz）
            float cpuFreq = Float.parseFloat(AndroidUtil.getCpuFrequence()) / 1000.0f;
            // 总RAM（单位M）
            float totalRam = AndroidUtil.getTotalMemory(this);
            // 可用RAM（单位M）
            float freeRam = AndroidUtil.getFreeMemory(this);
            if (cpuCoreCnt < 1 || cpuFreq <= 0 || totalRam <= 0 || freeRam <= 0) {
                // 性能数据未成功取到，做默认处理
                phonePerformValue = -1;
            } else {
                phonePerformValue = getPhonePerformanceValue(cpuCoreCnt,
                        cpuFreq, totalRam, freeRam);
            }
        } catch (Exception e) {
            LogUtil.e("手机性能检测异常", e);
            phonePerformValue = -1;
        }
	}
	private void initAppDatabase(){
		 DaoConfig config = new DaoConfig(context);
		  config.setDbName("RealApplication"); //db名
		  config.setDbVersion(1);  //db版本
		  db = DbUtils.create(config);//db还有其他的一些构造方法，比如含有更新表版本的监听器的
		  try {
			db.createTableIfNotExist(AppData.class);//创建一个表全应用表
		} catch (DbException e) {
			LogUtil.d("e="+e.toString());
			e.printStackTrace();
		} 
	}
	/** 
     * @author: songlk
     * @Title: getPhonePerformanceValue 
     * @Description: 自定义手机硬件性能值
     * @param cpuCoreCnt
     * @param cpuFreq
     * @param totalRam
     * @param freeRam
     * @return 
     * @date: 2014-1-21 下午3:23:36
     */
    private float getPhonePerformanceValue(int cpuCoreCnt, float cpuFreq,
            float totalRam, float freeRam) {
        return (cpuCoreCnt * cpuFreq) * 0.5f + totalRam * 0.05f + freeRam
                * 0.45f;
    }
    
	 public static synchronized BuymoodApplication getInstance() {
	        return mInstance;
	    }
	 public RequestQueue getRequestQueue() {
	        if (mRequestQueue == null) {
	            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
	        }
	 
	        return mRequestQueue;
	    }
	 public <T> void addToRequestQueue(Request<T> req, String tag) {
	        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
	        getRequestQueue().add(req);
	    }
	 
	    public <T> void addToRequestQueue(Request<T> req) {
	        req.setTag(TAG);
	        getRequestQueue().add(req);
	    }
	 
	    public void cancelPendingRequests(Object tag) {
	        if (mRequestQueue != null) {
	            mRequestQueue.cancelAll(tag);
	        }
	    }
	/** 对外接口---全局context
	 * @return
	 */
	public static Context getContext() {
		return context;
	}
}
