package com.slk.buymood.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.slk.buymood.fragment.MainHomeFra;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.volleytest.TestActivity;
import com.slk.buymood.utils.AppData;
import com.slk.buymood.utils.BuymoodApplication;
import com.slk.buymood.utils.CommonConstant;
import com.slk.buymood.utils.CommonDialog;
import com.slk.buymood.utils.CommonDialog.BtnClickedListener;
import com.slk.buymood.utils.CommonUtil;
import com.slk.buymood.utils.log.LogUtil;

public class LoginActivity extends Activity {
   TextView tv_title;
   Button  btn_login; 
   Button  btn_registrt;
   //登陆注册login  register用户名
   TableRow tb_user;  
   EditText edt_user;
   //只有注册register时候有
   EditText edt_reg_addr;
   EditText edt_reglicense;
   EditText edt_regpass;
   TableRow tb_regpass;
   TableRow tb_reglicense;
   TableRow tb_regaddr;
   //login的时候是密码    register的时候是email  备用登录名
   EditText edt_pass;
   TableRow tb_loginpass;
   private CommonDialog buymoodStateDlg = null;
   private boolean butelStated = true;
   // 标志存储空间是否大于10M,小于10M直接退出应用
   private boolean isSpaceEnough = true;
   String footTag;
   Map <String ,String > paramMap=new HashMap<String, String>();
   static boolean isCommonReg=false;
   static boolean isMerchantReg=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buymood_login);
		 if (CommonUtil.availableExStorageMemory() == false) {
	            // 外部存储空间不足10M判断
	            isSpaceEnough = false;
	            CommonDialog spaceNotEnough = new CommonDialog(this,
	                    getLocalClassName(), 400);
	            spaceNotEnough.getDialog().setCancelable(false);
	            spaceNotEnough.getDialog().setCanceledOnTouchOutside(false);
	            // spaceNotEnough.setTitle(R.string.alert);
	            spaceNotEnough.setMessage(CommonUtil.getString(R.string.no_space_content));
	            spaceNotEnough.setPositiveButton(new BtnClickedListener() {
	                @Override
	                public void onBtnClicked() {
	                    LogUtil.d("外部存储空间不足10M，点击确定，退出应用");
	                    LoginActivity.this.finish();
	                }
	            }, R.string.btn_ok);
	            spaceNotEnough.showDialog();
	            LogUtil.d("显示外部存储空间不足10M对话框");
	        }
		 
		 footTag=new String (
				 "手机基本信息 -厂商：" + Build.MANUFACTURER+
				 "\n"+"手机基本信息 -型号：" + Build.MODEL+
				 "\n"+"手机基本信息 -版本：" + Build.VERSION.RELEASE +" "+ Build.VERSION.SDK_INT +
				 "\n"+ "手机基本信息 -CPU架构：" + Build.CPU_ABI+
				 "\n"+"手机基本信息 -软件版本号：" + CommonUtil.getPackageInfo().versionName 
				 ).toString();
		 LogUtil.d("\n"+footTag
		         );
	}
	@Override
		protected void onStart() {
			super.onStart();
		}
	@Override
		protected void onResume() {
			super.onResume();
			initView();
			setClickListener();
			edt_user.setText("james");
			edt_pass.setText("james");
		}
	@Override
		protected void onRestart() {
			super.onRestart();
		}
	 private static Boolean isQuit = false;
	 private Timer timer = new Timer();
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//after 2s destroy
//            if (isQuit == false) {
//                isQuit = true;
//                Toast.makeText(getBaseContext(), "再次点击确定退出软件", Toast.LENGTH_SHORT).show();
//                TimerTask task = null;
//                task = new TimerTask() {
//                    @Override
//                    public void run() {
//                        isQuit = false;
//                    }
//                };
//                timer.schedule(task, 2000);
//            } else {
            	 LoginActivity.this.finish();
//            }
        } else {
        }
        return false;
    }
	
	
	private void initView() {
		tv_title=(TextView)findViewById(R.id.tv_title);
		
		btn_login=(Button)findViewById(R.id.btn_login);
		btn_registrt=(Button)findViewById(R.id.btn_registrt);
		
		 //登陆注册login  register用户名
		    tb_user= (TableRow)findViewById(R.id.tb_user); 
		    edt_user= (EditText)findViewById(R.id.edt_user);
		   //只有注册register时候有
		    edt_reg_addr= (EditText)findViewById(R.id.edt_reg_addr);
		    edt_reglicense= (EditText)findViewById(R.id.edt_reglicense);
		    edt_regpass= (EditText)findViewById(R.id.edt_regpass);
		    tb_regpass= (TableRow)findViewById(R.id.tb_regpass); 
		    tb_reglicense= (TableRow)findViewById(R.id.tb_reglicense);
		    tb_regaddr= (TableRow)findViewById(R.id.tb_regaddr);
		   //login的时候是密码    register的时候是email  备用登录名
		    edt_pass= (EditText)findViewById(R.id.edt_pass);
		    tb_loginpass= (TableRow)findViewById(R.id.tb_loginpass);
	}
	
	
	private void setClickListener(){
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
//				startActivity(new Intent(getBaseContext(),
//		    			TestActivity.class));
				
				if(btn_login.getText().toString().trim().equals(getString(R.string.login_login))){
					btn_login.setText(R.string.login_logining);
					btn_login.setTextColor(getResources().getColor(R.color.color_black));
				     
					if(edt_pass.getText().toString().trim().equals("")||edt_user.getText().toString().trim().equals("")){
						CommonUtil.toast("-----传 入 空 值-----");
						return ;
					}
					
					LogUtil.d("user="+edt_user.getText().toString().trim()+";  pass="+edt_pass.getText().toString().trim());
					//注意  一下两种方式post请求参数的区别
					paramMap.clear();
					paramMap.put("name",edt_user.getText().toString().trim());
					paramMap.put("pass",edt_pass.getText().toString().trim());
//					CommonUtil.postMap(CommonConstant.LOGIN_URL,paramMap,"login");
					StringRequest sr = new StringRequest(Request.Method.POST,CommonConstant.LOGIN_URL, new Response.Listener<String>() {
			    		@Override
			    		public void onResponse(String response) {
			    			LogUtil.d("HTTP response postMap:" + response);
			    			CommonUtil.toast(response);
			    			JSONObject result=null;
			    			 try {
								 result=new JSONObject(response);
							} catch (JSONException e) {
								LogUtil.d("---JSONException="+e.toString());
								e.printStackTrace();
							} 
			    			 String code=result.optString("code");
			    			 if(code.equals("10000")){
						        LogUtil.d("---login ok---");
						    	
								 LoginActivity.this.finish();
			    			 }else{
			    				 CommonUtil.toast("网络不给力，请稍后再试");
			    			 }
//			response postMap:{
//			    			"code":"10000",
//			    			"message":"Login ok",
//			    			"result":
//			    			{"Customer":
//			    			{"id":1,"name":"james","sign":"Happying","face":"1","blogcount":0,"fanscount":0,"uptime":"2011-11-29 18:11:24","sid":"lonkvmt9ps6ooqlo63tjv7ltcnklljj5"}}}
			    		}
			    	}, new Response.ErrorListener() {
			    		@Override
			    		public void onErrorResponse(VolleyError error) {
			    			error.getMessage();
			    			LogUtil.d("HTTP response:" + error);
			    			CommonUtil.toast(error.toString());
			    			//恢复到登陆前的状态
			    			btn_login.setText(R.string.login_login);
							btn_login.setTextColor(getResources().getColor(R.color.login_text));
			    		}
			    	}){
			    		protected Map<String,String> getParams(){
			    			Map<String,String> params = paramMap;
			    			return params;
			    		}
			    		public Map<String, String> getHeaders() throws AuthFailureError {
			    			Map<String,String> params = new HashMap<String, String>();
			    			params.put("Content-Type","application/x-www-form-urlencoded");
			    			return params;
			    		}
			    	};
//			    	BuymoodApplication.getInstance().addToRequestQueue(sr);
                     CommonUtil.getApp().addToRequestQueue(sr, "login");
//					paramMap.clear();
				                     
				                      
				}else if(btn_login.getText().toString().trim().equals(getString(R.string.login_logining))){
					btn_login.setText(R.string.login_login);
					btn_login.setTextColor(getResources().getColor(R.color.login_text));
				}else if(btn_login.getText().toString().trim().equals(getString(R.string.reg_btn))){
					if(isCommonReg){//普通用户注册
                      
					}else if(isMerchantReg){//商家用户注册
						
					}
				}
				/**/	
			}
		});
		btn_registrt.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// 按下
					btn_registrt.setTextColor(Color.GRAY);			
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					// 弹起
					btn_registrt.setTextColor(Color.WHITE);
					if(btn_registrt.getText().toString().trim().equals("注册新用户")){
						btn_registrt.setText("取   消");
						showRegStatement(); 
                    }else if(btn_registrt.getText().toString().equals("取   消")){
                    	btn_registrt.setText("注册新用户");
                    	 textSeting(getString(R.string.login_title),getString(R.string.login_login));
                    	 isCommonReg=false;
                    	 isMerchantReg=false;
                    }
				}
				return false;
			}
		});
		
		
	}
	 private void showRegStatement() {
	        if (!isSpaceEnough) {
	            return;
	        }
//	        String hideButelStatement = daoPre.getKeyValue(
//	                DaoPreference.PrefType.HIDE_BUTEL_STATEMENT, "0");
	        if (true
//	        		!"1".equals(hideButelStatement)
	        		) {
	            butelStated = false;

	        	buymoodStateDlg = new CommonDialog(LoginActivity.this,
	                    getLocalClassName(), 106);
	        	buymoodStateDlg.addView(R.layout.dialog_statement_layout);
	        	buymoodStateDlg.getDialog().setCanceledOnTouchOutside(false);
	        	buymoodStateDlg.getDialog().setCancelable(false);
	        	buymoodStateDlg.setTitle(CommonUtil.getString(R.string.buymoodStateDlg_title));
	        	CheckBox cb = (CheckBox) buymoodStateDlg
                        .getContentView().findViewById(
                                R.id.hide_butel_statement);
//                if (!cb.isChecked()) {
//                	//不同意声明协议  取消注册
//                	buymoodStateDlg.dismiss();
//                	LogUtil.d("not agree");
//                }
	        	cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
						if(!isChecked){
							buymoodStateDlg.dismiss();
							btn_registrt.setText("注册新用户");
							textSeting(getString(R.string.login_title),getString(R.string.login_login));
						}
					}
				});
	        	
	        	
	        	buymoodStateDlg.setPositiveButton(
	                    new CommonDialog.BtnClickedListener() {
	                        @Override
	                        public void onBtnClicked() {
	                            butelStated = true;

	                            
	                           //普通注册 
	                            LogUtil.d("普通注册" 
//	                            + cb.isChecked()
	                            );
	                            
	                            textSeting("普通用户注册","注     册");
	                            isCommonReg=true;
//	                            queryInterfaceAddress();
	                        }
	                    }, R.string.confirm_message);
	        	buymoodStateDlg.setSellerRegBtn(
	        			new CommonDialog.BtnClickedListener() {
	        				@Override
	        				public void onBtnClicked() {
	        					butelStated = true;
	        					
	        					LogUtil.d("商家注册" );
	        					textSeting("实体商家注册","注     册");
	        					isMerchantReg=true;
	        				}
	        			}, CommonUtil.getString(R.string.confirm_message1));
	        	
	        	
	        	buymoodStateDlg.setCancleButton(
	                    new CommonDialog.BtnClickedListener() {
	                        @Override
	                        public void onBtnClicked() {
	                        	 LogUtil.d("取消");
	                        	 btn_registrt.setText(getString(R.string.login_register));
	                        	 textSeting(getString(R.string.login_login),getString(R.string.login_login));
//	                            MainActivity.this.finish();
	                        }
	                    }, R.string.cancel_message);
	        	buymoodStateDlg.showDialog();
	        	LogUtil.d("显示软件使用声明对话框");
	        }
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	protected void onDestroy() {
			super.onDestroy();
			CommonUtil.cancelAllpost("login");
		}
	private void textSeting(String titleStr,String buttonStr){
		tv_title.setText(titleStr);
		tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP ,28);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin=10;//px
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		tv_title.setLayoutParams(params);
		btn_login.setText(buttonStr);
		
	    if(titleStr.contains("商家")){
	    	 //只有注册register时候有                                      
	    	  edt_reg_addr.setVisibility(View.VISIBLE);                                 
	    	  edt_reglicense.setVisibility(View.VISIBLE);                               
	    	  edt_regpass.setVisibility(View.VISIBLE);                                   
	    	  tb_regpass.setVisibility(View.VISIBLE);                                     
	    	  tb_reglicense.setVisibility(View.VISIBLE);                                 
	    	  tb_regaddr.setVisibility(View.VISIBLE);                                    
	    	 //login的时候是密码          register的时候是email     备用登录名              
	    	  edt_pass.setVisibility(View.VISIBLE);                                      
	    	  tb_loginpass.setVisibility(View.VISIBLE); 
	    	  
	    	  edt_reg_addr.setHint(CommonUtil.getString(R.string.reg_seller_addr));
	    	  edt_reg_addr.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
	    	  edt_reglicense.setHint(CommonUtil.getString(R.string.reg_seller_license));
	    	  edt_reglicense.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
	    	  edt_regpass.setHint(CommonUtil.getString(R.string.reg_pass)) ;
	    	  edt_regpass.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
	    	  edt_pass.setHint(CommonUtil.getString(R.string.reg_email));
	    }	else if(titleStr.contains("普通用户")){
	    	  edt_reg_addr.setVisibility(View.GONE);                                 
	    	  edt_reglicense.setVisibility(View.GONE);                               
	    	  edt_regpass.setVisibility(View.VISIBLE);                                   
	    	  tb_regpass.setVisibility(View.VISIBLE);                                     
	    	  tb_reglicense.setVisibility(View.GONE);                                 
	    	  tb_regaddr.setVisibility(View.GONE);
	    	  
	    	  edt_regpass.setHint(CommonUtil.getString(R.string.reg_pass)) ;
	    	  edt_regpass.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
	    	  edt_pass.setHint(CommonUtil.getString(R.string.reg_email));
//	    	  edt_pass.setTypeface(tf);
	    }else{
	    	  edt_reg_addr.setVisibility(View.GONE);                                 
	    	  edt_reglicense.setVisibility(View.GONE);                               
	    	  edt_regpass.setVisibility(View.GONE);                                   
	    	  tb_regpass.setVisibility(View.GONE);                                     
	    	  tb_reglicense.setVisibility(View.GONE);                                 
	    	  tb_regaddr.setVisibility(View.GONE);
	    	  
	  		params.topMargin=CommonUtil.sp2px(38);//px
	  		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
	  		tv_title.setLayoutParams(params);//    
	    	tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP,38);
	        edt_pass.setHint(CommonUtil.getString(R.string.reg_pass));
	    }
		
		
		
	}
	
	
}
