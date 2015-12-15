package com.slk.buymood.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.slk.buymood.ui.R;
import com.slk.buymood.utils.BuymoodApplication;
import com.slk.buymood.utils.image.CirculaireNetworkImageView;

/**
 * home4
 * 
 * @author songlk
 * 
 */
public class Home4Fra extends Fragment implements OnClickListener {
	 private View v;
	 private String id;
	 private RelativeLayout main_bg;
	 private RelativeLayout title_bg;
	 private TextView mTV_Pwd;
	 private TextView mTV_version;
	 private TextView mTV_help, mTV_theme;
	 private TextView setting_list_about;
	 private Button mB_exit;
	 private LinearLayout themeLL;
	 
	 
	 private RelativeLayout layout_personal;
	 private RelativeLayout personal_top_layout;
	 private ImageView personal_background_image;
	 private CirculaireNetworkImageView networkImageView;
	 
	 RequestQueue mQueue = BuymoodApplication.getInstance().getRequestQueue();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.mysetting, null);
//		v = inflater.inflate(R.layout.fragment_4, null);
		init(v);
		return v;
	}

	private void init(View v) {
//		 main_bg = (RelativeLayout) v.findViewById(R.id.setting_bg);
//		 title_bg = (RelativeLayout) v.findViewById(R.id.title_setting);
//		 mTV_version = (TextView) v.findViewById(R.id.setting_list_version);//版本升级
//		 mTV_help = (TextView) v.findViewById(R.id.setting_list_help);//帮助
//		 setting_list_about = (TextView) v.findViewById(R.id.setting_list_about);//关于
//		 mB_exit=(Button)v.findViewById(R.id.setting_exit);
//		 mB_exit.setOnClickListener(this);
//		 setting_list_about.setOnClickListener(this);
//		 mTV_help.setOnClickListener(this);
		layout_personal=(RelativeLayout)v.findViewById(R.id.layout_personal);
		personal_top_layout=(RelativeLayout)v.findViewById(R.id.personal_top_layout);
		personal_background_image=(ImageView)v.findViewById(R.id.personal_background_image);
		ImageLoader imageLoader = new ImageLoader(BuymoodApplication.getInstance().getRequestQueue(), new com.slk.buymood.utils.image.ImageFileCache());
		CirculaireNetworkImageView	networkImageView =(CirculaireNetworkImageView)v.findViewById(R.id.login_circle_img);
		
		networkImageView.setErrorImageResId(R.drawable.cat);
		networkImageView.setDefaultImageResId(R.drawable.cat);
		networkImageView.setImageUrl("http://10.130.63.37:8002/faces/default/face_0.png", imageLoader);
	}
	/*
	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		 case R.id.setting_list_version:
		 {
			 LogUtil.d("---setting_list_version"); 
		 }
		 break;
		 case R.id.setting_list_about:
		 {
			 LogUtil.d("---setting_list_about");
		 }
		 break;
		 case R.id.setting_list_help:
		 {
			 Intent homeIntent = new Intent( getActivity(),
		    			LoginActivity.class);
				startActivity(homeIntent);
			 LogUtil.d("---setting_list_help");
		 }
		 break;
		
		 case R.id.setting_exit:
		 {
			 LogUtil.d("---setting_exit");
		 }
		 break;
		 default:
		 break;
		 }
	}
*/

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
