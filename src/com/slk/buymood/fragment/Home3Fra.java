package com.slk.buymood.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
/**
 * home3
 * @author andye
 *
 */
public class Home3Fra extends Fragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(com.slk.buymood.ui.R.layout.fragment_3, null);

		init(v);

		return v;
	}

	private void init(View v) {
		// main_bg = (LinearLayout)v.findViewById(R.id.message_layout_bg);
		// title_bg = (RelativeLayout)v.findViewById(R.id.title_message);

	}

	/**
	 * ��ť����¼�����
	 */
	@Override
	public void onClick(View v) {
		// switch (v.getId()) {
		// case R.id.message_delete:
		//
		// break;
		//
		// default:
		// break;
		// }
	}
}
