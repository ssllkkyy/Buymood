package com.slk.buymood.utils;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.slk.buymood.utils.log.LogUtil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.media.ExifInterface;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class CommonUtil {
	private static Context context=BuymoodApplication.getContext();
	public static RequestQueue queue=BuymoodApplication.getInstance().getRequestQueue();
	private static final String TAG="CommonUtil";
	
	public static String getSDPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        Log.d(TAG, "getSDPath sdCardExist:" + sdCardExist);
        if (sdCardExist) {
            // 获取跟目录
            return Environment.getExternalStorageDirectory().toString();
        } else {
            return "";
        }
    }
	
	public static BuymoodApplication getApp(){
		return BuymoodApplication.getInstance();
	}
	 /**
     * @author: lihs
     * @Title: getPackageInfo
     * @Description: 应用程序的版本号，版本名称，当前版本的包名
     * @return
     * @date: 2013-8-2 下午3:42:56
     */
    public static PackageInfo getPackageInfo() {

        PackageInfo info = new PackageInfo();
        try {
            info = context
                    .getPackageManager()
                    .getPackageInfo(
                    		context.getPackageName(),
                            0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            LogUtil.e("getPackageInfo", e);
        }
        return info;
    }
	
	
	/**是否是10M以上外存
	 * @return  
	 */
	public static boolean availableExStorageMemory() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {

            /** 获取存储卡路径 */
            File sdcardDir = Environment.getExternalStorageDirectory();
            /** StatFs 看文件系统空间使用情况 */
            StatFs statFs = new StatFs(sdcardDir.getPath());
            /** Block 的 size */
            long blockSize = statFs.getBlockSize();
            /** 总 Block 数量 */
            int totalBlocks = statFs.getBlockCount();
            /** 已使用的 Block 数量 */
            int availableBlocks = statFs.getAvailableBlocks();

            if (availableBlocks * blockSize > 10 * 1024 * 1024) {
                return true;
            }
        }
        return false;
    }

	  public static String[] getClassMethod(Exception e, String defaultClass,
	            String defaultMethod) {
	        String methodName = "";
	        String className = "";
	        StackTraceElement el = null;
	        try {
	            el = e.getStackTrace()[1];
	            className = el.getClassName();
	            className = className.substring(className.lastIndexOf(".") + 1);
	            methodName = el.getMethodName();
	        } catch (Exception ex) {
	            LogUtil.e(TAG, "getClassMethod", "Exception", ex);
	            if (TextUtils.isEmpty(className)) {
	                className = defaultClass;
	            }
	            if (TextUtils.isEmpty(methodName)) {
	                methodName = defaultMethod;
	            }
	        }
	        el = null;
	        return new String[] { className, methodName };
	    }
	     /**弹toast
	     * @param str
	     */
	    public  static void  toast(String str){
	    	 Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	     }
	    
	     
	     /**post map请求
	     * @param urlApi
	     * @param paramsMap
	     */
	    public static void postMap(String urlApi,final Map paramsMap){
	 	    StringRequest sr = new StringRequest(Request.Method.POST,urlApi, new Response.Listener<String>() {
	 	        @Override
	 	        public void onResponse(String response) {
	 	        	   LogUtil.d("HTTP response postMap:" + response);
	 	        	   CommonUtil.toast(response);
	 	        }
	 	    }, new Response.ErrorListener() {
	 	        @Override
	 	        public void onErrorResponse(VolleyError error) {
	 	        	error.getMessage();
	                 LogUtil.d("HTTP response:" + error);
	                 CommonUtil.toast(error.toString());
	 	        }
	 	    }){
	 	        protected Map<String,String> getParams(){
	 	            Map<String,String> params = paramsMap;
	 	            return params;
	 	        }
	 	        public Map<String, String> getHeaders() throws AuthFailureError {
	 	            Map<String,String> params = new HashMap<String, String>();
	 	            params.put("Content-Type","application/x-www-form-urlencoded");
	 	            return params;
	 	        }
//	 	        @Override
//	 	        public com.android.volley.Request.Priority getPriority() {
//	 	        	// 优先级最高
//	 	        	return Priority.HIGH;
//	 	        }
	 	    };
//	 	    sr.setTag("login");
	 	    queue.add(sr);
	 	  //取消请求
//	         queue.cancelAll("login");
	 	}
	    /**post map请求   
	     * @param urlApi   apiurl
	     * @param paramsMap   map参数
	     * @param apiname  api名字  为取消做准备
	     */
	    public static void postMap(String urlApi,final   Map myParams,String apiname ){
	    	StringRequest sr = new StringRequest(Request.Method.POST,urlApi, new Response.Listener<String>() {
	    		@Override
	    		public void onResponse(String response) {
	    			LogUtil.d("HTTP response postMap:" + response);
	    			CommonUtil.toast(response);
	    		}
	    	}, new Response.ErrorListener() {
	    		@Override
	    		public void onErrorResponse(VolleyError error) {
	    			error.getMessage();
	    			LogUtil.d("HTTP response:" + error);
	    			CommonUtil.toast(error.toString());
	    		}
	    	}){
	    		protected Map<String,String> getParams(){
	    			Map<String,String> params = myParams;
	    			return params;
	    		}
	    		public Map<String, String> getHeaders() throws AuthFailureError {
	    			Map<String,String> params = new HashMap<String, String>();
	    			params.put("Content-Type","application/x-www-form-urlencoded");
	    			return params;
	    		}
//	 	        @Override
//	 	        public com.android.volley.Request.Priority getPriority() {
//	 	        	// 优先级最高
//	 	        	return Priority.HIGH;
//	 	        }
	    	};
	    	BuymoodApplication.getInstance().addToRequestQueue(sr);
	    }
	     /**post json请求
	     * @param urlApi  url接口
	     * @param jsonObj  json对象参数
	     * @param apiname  接口名字  为取消做准备
	     */
	    /**
	     * @param urlApi
	     * @param jsonObj
	     * @param apiname
	     */
	    public static void postJson(String urlApi, JSONObject jsonObj,String apiname){
	    	
	    	JsonObjectRequest objRequest = new JsonObjectRequest(
	    			 urlApi, jsonObj,
		                new Response.Listener<JSONObject>() {
		                    @Override
		                    public void onResponse(JSONObject obj) {
		                        LogUtil.d("HTTP response:" + obj);
		                        CommonUtil.toast(obj.toString());
		                    }
		                }, new Response.ErrorListener() {
		                    @Override
		                    public void onErrorResponse(VolleyError error) {
		                        error.getMessage();
		                        LogUtil.d("HTTP response:" + error);
		                    }
		                })
		        {
//		            public com.android.volley.Request.Priority getPriority() {
//		                return Priority.HIGH;
//		            }
		        };
		        BuymoodApplication.getInstance().addToRequestQueue(objRequest,apiname);
	     }
	     public  static  void cancelAllpost(String apiname){
	    	 BuymoodApplication.getInstance().cancelPendingRequests(apiname);
	     }
	     
	     /**拿到资源配置string  value等
	     * @param resId
	     * @return
	     */
	    public static String getString(int resId){
	    	 return context.getString(resId);
	     }
	     /**  
	      * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5  
	      * @param String s 需要得到长度的字符串  
	      * @return int 得到的字符串长度  
	      */   
	     public static double getLength(String s) {  
	      double valueLength = 0;    
	         String chinese = "[\u4e00-\u9fa5]";    
	         // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1    
	         for (int i = 0; i < s.length(); i++) {    
	             // 获取一个字符    
	             String temp = s.substring(i, i + 1);    
	             // 判断是否为中文字符    
	             if (temp.matches(chinese)) {    
	                 // 中文字符长度为1    
	                 valueLength += 1;    
	             } else {    
	                 // 其他字符长度为0.5    
	                 valueLength += 0.5;    
	             }    
	         }    
	         //进位取整    
	         return  Math.ceil(valueLength);    
	     }  
	   //转换dip为px 
	        public static int convertDipOrPx( int dip) { 
	            float scale = context.getResources().getDisplayMetrics().density; 
	            return (int)(dip*scale + 0.5f*(dip>=0?1:-1)); 
	        } 
	         
	        //转换px为dip 
	        public static int convertPxOrDip( int px) { 
	            float scale = context.getResources().getDisplayMetrics().density; 
	         return (int)(px/scale + 0.5f*(px>=0?1:-1)); 
	      } 
	      
	       public static int sp2px( float spValue) {
	             float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
	             return (int) (spValue * fontScale + 0.5f);
	         }
	   
	       public static int px2sp( float pxValue) {
	           float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
	           return (int) (pxValue / fontScale + 0.5f);
	       }
	     
	       /**
	        * @author: zhaguitao
	        * @Title: getImageRotationByPath
	        * @Description: 根据图片路径获得其旋转角度
	        * @param ctx
	        * @param path
	        * @return
	        * @date: 2013-10-16 下午12:53:34
	        */
	       public static int getImageRotationByPath(Context ctx, String path) {
	           int rotation = 0;
	           if (TextUtils.isEmpty(path)) {
	               return rotation;
	           }

	           Cursor cursor = null;
	           try {
	               cursor = ctx.getContentResolver().query(
	                       MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	                       new String[] { MediaStore.Images.Media.ORIENTATION },
	                       MediaStore.Images.Media.DATA + " = ?",
	                       new String[] { "" + path }, null);
	               if (cursor != null && cursor.getCount() > 0) {
	                   cursor.moveToFirst();
	                   rotation = cursor.getInt(0);
	               } else {
	                   rotation = getImageRotationFromUrl(path);
	               }
	           } catch (Exception e) {
	               LogUtil.e(TAG, "getImageRotationByPath", "Exception", e);
	           } finally {
	               if (cursor != null) {
	                   cursor.close();
	                   cursor = null;
	               }
	           }

	           return rotation;
	       }    
	       /**
	        * @Description: 获取下载图片的旋转角度，从网络传输图片，只能用带方法
	        * @param path
	        *            :图片路径
	        * @return 返回图片角度
	        */
	       public static int getImageRotationFromUrl(String path) {
	           int orientation = 0;
	           try {
	               ExifInterface exifInterface = new ExifInterface(path);
	               orientation = exifInterface.getAttributeInt(
	                       ExifInterface.TAG_ORIENTATION,
	                       ExifInterface.ORIENTATION_NORMAL);
	               switch (orientation) {
	               case ExifInterface.ORIENTATION_ROTATE_90:
	                   orientation = 90;
	                   break;
	               case ExifInterface.ORIENTATION_ROTATE_180:
	                   orientation = 180;
	                   break;
	               case ExifInterface.ORIENTATION_ROTATE_270:
	                   orientation = 270;
	                   break;
	               default:
	                   orientation = 0;
	               }
	           } catch (Exception e) {
	               LogUtil.e(TAG, "getImageRotationFromUrl", "Exception", e);
	               orientation = 0;
	           }
	           return orientation;
	       }

}
