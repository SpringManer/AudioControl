package com.itheima.audiocontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class SlideFragment extends Fragment implements OnClickListener {

	private FragmentActivity activity;
	private TextView tv_setting;
	private TextView tv_about;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragement_content = View.inflate(activity,
				R.layout.fragement_slide, null);

		tv_setting = (TextView) fragement_content.findViewById(R.id.tv_setting);
		tv_about = (TextView) fragement_content.findViewById(R.id.tv_about);

		tv_setting.setOnClickListener(this);
		tv_about.setOnClickListener(this);

		return fragement_content;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_setting:

			Intent intent = new Intent(activity, SettingActivity.class);

			startActivity(intent);
			activity.overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);

			break;
		case R.id.tv_about:

			Intent about = new Intent(activity, AboutActivity.class);

			startActivity(about);
			activity.overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);

			break;
		}

	}

}
