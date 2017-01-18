package com.itheima.audiocontrol;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

public class SettingActivity extends Activity implements
		android.widget.CompoundButton.OnCheckedChangeListener {

	private CheckBox cb_music;
	private CheckBox cb_alarm;
	private CheckBox cb_notify;
	private CheckBox cb_ring;
	private CheckBox cb_system;
	private SharedPreferences checkbox_state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);

		checkbox_state = getSharedPreferences("checkbox_state",
				Context.MODE_PRIVATE);
		
		
		

		ImageButton ib_back = (ImageButton) findViewById(R.id.ib_setting_back);

		cb_music = (CheckBox) findViewById(R.id.cb_music);
		cb_alarm = (CheckBox) findViewById(R.id.cb_alarm);
		cb_notify = (CheckBox) findViewById(R.id.cb_notify);
		cb_ring = (CheckBox) findViewById(R.id.cb_ring);
		cb_system = (CheckBox) findViewById(R.id.cb_system);

		cb_music.setOnCheckedChangeListener(this);
		cb_alarm.setOnCheckedChangeListener(this);
		cb_notify.setOnCheckedChangeListener(this);
		cb_ring.setOnCheckedChangeListener(this);
		cb_system.setOnCheckedChangeListener(this);

		// 进入之前获取sp中的状态进行同步

		boolean cb_music_state = checkbox_state.getBoolean("cb_music", true);
		boolean cb_ring_state = checkbox_state.getBoolean("cb_ring", true);
		boolean cb_notify_state = checkbox_state.getBoolean("cb_notify", true);
		boolean cb_alarm_state = checkbox_state.getBoolean("cb_alarm", true);
		boolean cb_system_state = checkbox_state.getBoolean("cb_system", true);

		// 更新状态
		cb_music.setChecked(cb_music_state);
		cb_alarm.setChecked(cb_alarm_state);
		cb_notify.setChecked(cb_notify_state);
		cb_ring.setChecked(cb_ring_state);
		cb_system.setChecked(cb_system_state);

		// 返回监听
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// 把当前的选中状态保存进sp，默认为false；
		int id = buttonView.getId();
		switch (id) {
		case R.id.cb_music:

			System.out.println("R.id.cb_music" + isChecked + "被点击");

			checkbox_state.edit().putBoolean("cb_music", isChecked).commit();

			break;

		case R.id.cb_alarm:

			System.out.println("R.id.cb_alarm" + isChecked + "被点击");
			checkbox_state.edit().putBoolean("cb_alarm", isChecked).commit();

			break;
		case R.id.cb_notify:

			System.out.println("R.id.cb_notify" + isChecked + "被点击");
			checkbox_state.edit().putBoolean("cb_notify", isChecked).commit();

			break;
		case R.id.cb_ring:

			System.out.println("R.id.cb_ring" + isChecked + "被点击");
			checkbox_state.edit().putBoolean("cb_ring", isChecked).commit();

			break;
		case R.id.cb_system:

			System.out.println("R.id.cb_system" + isChecked + "被点击");
			checkbox_state.edit().putBoolean("cb_system", isChecked).commit();

			break;
		}
	}

}
