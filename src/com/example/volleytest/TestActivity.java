package com.example.volleytest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.slk.buymood.ui.R;
import com.slk.buymood.utils.CommonConstant;
import com.slk.buymood.utils.CompressUtil;
import com.slk.buymood.utils.log.LogUtil;

public class TestActivity extends Activity {

	Button bt;
	private static RequestQueue mSingleQueue;
	private static String TAG = "test";
    String encodedString;
    private static final int MSG_IMAGE_COMPRESSED=0;
//压缩文件保存的文件夹
  public static String FILE_COMPRESS_DIR = "compress";
	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			
			case MSG_IMAGE_COMPRESSED:{
				LogUtil.d("----");
				Bundle data = msg.getData();
				String srcpath = data.getString("compress_srcpath");
        		String outpath = data.getString("compress_outpath");
        		boolean success = data.getBoolean("compress_success");
        			LogUtil.d("compress_success ="+success);
				
        		  //xutils  传输file

//				Map<String, String> params = new HashMap<String, String>();
//				params.put("name", "slk");
                File file=new File("/mnt/sdcard/DCIM/Camera/123.jpg");
				if(!file.exists()){
					Toast.makeText(TestActivity.this, "没有此文件目录", 1).show();
				    return;
				}
                String uri = CommonConstant.UPLOAD_URL;
				RequestParams params = new RequestParams();
				params.addBodyParameter("uploadfile", file);
				params.addBodyParameter("name","slk");
				HttpUtils http = new HttpUtils();
				http.configTimeout(120*1000);
				http.send(HttpMethod.POST,
						uri,
				    params,
				    new RequestCallBack<String>() {

				        @Override
				        public void onStart() {
				            LogUtil.d("conn...");
				        }

				        @Override
				        public void onLoading(long total, long current, boolean isUploading) {
				            if (isUploading) {
				                LogUtil.d("upload: " + current + "/" + total);
				            } else {
				               LogUtil.d("reply: " + current + "/" + total);
				            }
				        }

				        @Override
				        public void onSuccess(ResponseInfo<String> responseInfo) {
				            LogUtil.d("reply: " + responseInfo.result);
				        
				        }

				        @Override
				        public void onFailure(HttpException error, String msg) {
				            LogUtil.d(error.getExceptionCode() + ":" + msg);
				        }
				});	
			}	break;

			default:
				break;
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		mSingleQueue = Volley.newRequestQueue(this, new MultiPartStack());

		bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				compressImageThread("/mnt/sdcard/DCIM/Camera/123.jpg");
				/*volley
				Bitmap myImg = BitmapFactory.decodeFile("/mnt/sdcard/DCIM/Camera/123.png");
		        ByteArrayOutputStream stream = new ByteArrayOutputStream();
		        // Must compress the Image to reduce image size to make upload easy
		        myImg.compress(Bitmap.CompressFormat.PNG, 50, stream);
		        byte[] byte_arr = stream.toByteArray();
		        // Encode Image to String
		        encodedString = Base64.encodeToString(byte_arr, 0);
				    String url = CommonConstant.UPLOAD_URL;
				    Log.d("URL", url);
				    StringRequest stringRequest = new StringRequest(Request.Method.POST,
				            url, new Response.Listener<String>() {

				        @Override
				        public void onResponse(String response) {
				                LogUtil.d("http-response"+ response);

				                Toast.makeText(getBaseContext(),
				                		response, Toast.LENGTH_SHORT)
				                        .show();
				        }

				    }, new Response.ErrorListener() {
				        @Override
				        public void onErrorResponse(VolleyError error) {
				            Log.d("ERROR", "Error [" + error + "]");
				            Toast.makeText(getBaseContext(),
				                    "Cannot connect to server", Toast.LENGTH_LONG)
				                    .show();
				        }
				    }) {
				        @Override
				        protected Map<String, String> getParams() {
				            Map<String, String> params = new HashMap<String, String>();
				            params.put("image", encodedString);
				            params.put("name", "slk");
				            return params;

				        }

				    };
				    CommonUtil.getApp().addToRequestQueue(stringRequest, "upload");
				*/
				
			}
		});
	}

	/***
	 * 压缩图片（独立工作线程），完成后（不论成功与否）handler通知主线程
	 * @param uuid
	 * @param filepath
	 */
	private void compressImageThread( final String filepath){
		new Thread(new Runnable(){
				@Override
				public void run() {
                    LogUtil.d("compressImageThread," + "|filepath=" + filepath);

					String outpath = getCompressFilePath(filepath);
					boolean success = CompressUtil.compressImage(filepath, outpath);
					LogUtil.d("CompressUtil.compressImage,outpath=" + outpath + "|success=" + success);

//					Message msg = syncHandler.obtainMessage();
//					msg.what = MSG_IMAGE_COMPRESSED;
//					Bundle data = new Bundle();
//					data.putBoolean("compress_success", success);
//					data.putString("compress_uuid", uuid);
//					data.putString("compress_srcpath", filepath);
//					data.putString("compress_outpath", outpath);
//					msg.setData(data);
//					syncHandler.sendMessage(msg);
					Message msg=new Message();
					msg.what = MSG_IMAGE_COMPRESSED;
					Bundle data = new Bundle();
					data.putString("compress_srcpath", filepath);
					data.putBoolean("compress_success", success);
					data.putString("compress_outpath", outpath);
					msg.setData(data);
					myHandler.sendMessage(msg);
				}
			}
		).start();
	}
	private String getCompressFilePath(String srcfilepath) {
	    LogUtil.d("srcfilepath:" + srcfilepath);
		
		int index = srcfilepath.lastIndexOf(File.separator);
        String lastpart = srcfilepath.substring(index);
        String path = Environment.getExternalStorageDirectory()
                + File.separator + CommonConstant.APP_ROOT_FOLDER 
                + File.separator + FILE_COMPRESS_DIR
                + lastpart;
        LogUtil.d("path:" + path);
        return path;
	}

}
