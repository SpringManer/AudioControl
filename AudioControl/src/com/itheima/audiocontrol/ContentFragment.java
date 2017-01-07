package com.itheima.audiocontrol;

import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ContentFragment extends Fragment implements
		OnSeekBarChangeListener, OnClickListener {

	private TextView music_volume;
	private TextView ring_volume;
	private TextView system_volume;
	private AudioManager am;
	private SeekBar sb_music_volume;
	private SeekBar sb_ring_volume;
	private SeekBar sb_alarm_volume;
	private SeekBar sb_system_volume;
	private ImageButton ib_silent;
	private SeekBar sb_notify_volume;

	private FragmentActivity activity;
	private ImageButton ib_setting;
	private ImageButton ib_recovery;
	private SharedPreferences current_volume;
	private SharedPreferences checkbox_state;
	private LinearLayout ll_content_root;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();

		current_volume = activity.getSharedPreferences("current_volume",
				Context.MODE_PRIVATE);

		checkbox_state = activity.getSharedPreferences("checkbox_state",
				Context.MODE_PRIVATE);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View fragement_slide = View.inflate(activity,
				R.layout.fragement_content, null);

		intUI(fragement_slide);

		// 动态随机更换壁纸
		randomChangeBg();

		return fragement_slide;
	}

	/**
	 * 随机更换壁纸
	 */
	private void randomChangeBg() {
		int[] pic = new int[] { R.drawable.pc1, R.drawable.pc2, R.drawable.pc3,
				R.drawable.pc4, R.drawable.pc5, R.drawable.pc6,R.drawable.pc7 };

		Random random = new Random();
		int picNum = random.nextInt(7);

		ll_content_root.setBackgroundResource(pic[picNum]);
	}

	private void intUI(View fragement_slide) {
		music_volume = (TextView) fragement_slide
				.findViewById(R.id.tv_show_music_volume);
		ring_volume = (TextView) fragement_slide
				.findViewById(R.id.tv_show_ring_volume);
		system_volume = (TextView) fragement_slide
				.findViewById(R.id.tv_show_system_volume);
		ib_silent = (ImageButton) fragement_slide.findViewById(R.id.ib_silent);
		ib_recovery = (ImageButton) fragement_slide
				.findViewById(R.id.ib_recovery);
		ib_setting = (ImageButton) fragement_slide
				.findViewById(R.id.ib_setting);

		ll_content_root = (LinearLayout) fragement_slide
				.findViewById(R.id.ll_content_root);

		sb_music_volume = (SeekBar) fragement_slide
				.findViewById(R.id.sb_music_volume);
		sb_ring_volume = (SeekBar) fragement_slide
				.findViewById(R.id.sb_ring_volume);
		sb_alarm_volume = (SeekBar) fragement_slide
				.findViewById(R.id.sb_alarm_volume);
		sb_system_volume = (SeekBar) fragement_slide
				.findViewById(R.id.sb_system_volume);
		sb_notify_volume = (SeekBar) fragement_slide
				.findViewById(R.id.sb_notify_volume);

		am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);

		// 获取最大媒体音量
		int maxMusicVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		// 获取最大闹钟音量
		int maxAlarmVolume = am.getStreamMaxVolume(AudioManager.STREAM_ALARM);
		// 获取最大铃声音量
		int maxRingVolume = am.getStreamMaxVolume(AudioManager.STREAM_RING);

		// 获取最大系统音量
		int maxSystemVolume = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
		// 获取最大通知音量
		int maxNotifyVolume = am
				.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
		// 更新seekBar进度
		updateSeekBarProgress();

		// 设置seekBar最大值
		sb_music_volume.setMax(maxMusicVolume);
		sb_ring_volume.setMax(maxRingVolume);
		sb_alarm_volume.setMax(maxAlarmVolume);
		sb_system_volume.setMax(maxSystemVolume);
		sb_notify_volume.setMax(maxNotifyVolume);

		// 设置seekBar监听
		sb_music_volume.setOnSeekBarChangeListener(this);
		sb_ring_volume.setOnSeekBarChangeListener(this);
		sb_alarm_volume.setOnSeekBarChangeListener(this);
		sb_system_volume.setOnSeekBarChangeListener(this);
		sb_notify_volume.setOnSeekBarChangeListener(this);

		// 设置一键静音监听
		ib_silent.setOnClickListener(this);
		// 设置一键还原监听
		ib_recovery.setOnClickListener(this);
		// 设置侧边栏监听
		ib_setting.setOnClickListener(this);
	}

	/**
	 * 各种点点击监听
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ib_setting:

			toggle();

			break;
		case R.id.ib_silent:

			// 获取当前音量存入sp

			// 获取当前系统音量

			int currentMusicVolume = am
					.getStreamVolume(AudioManager.STREAM_MUSIC);

			int currentRingVolume = am
					.getStreamVolume(AudioManager.STREAM_RING);

			int currentSystemVolume = am
					.getStreamVolume(AudioManager.STREAM_SYSTEM);

			int currentAlarmVolume = am
					.getStreamVolume(AudioManager.STREAM_ALARM);
			int currentNotifyVolume = am
					.getStreamVolume(AudioManager.STREAM_NOTIFICATION);

			Editor edit = current_volume.edit();

			edit.putInt("currentMusicVolume", currentMusicVolume);
			edit.putInt("currentRingVolume", currentRingVolume);
			edit.putInt("currentSystemVolume", currentSystemVolume);
			edit.putInt("currentAlarmVolume", currentAlarmVolume);
			edit.putInt("currentNotifyVolume", currentNotifyVolume);

			edit.commit();

			oneKeySilent();
			// 更新seekBar进度
			updateSeekBarProgress();

			break;
		case R.id.ib_recovery:

			// 从sp获取之前的音量

			int currentMusicVolume1 = current_volume.getInt(
					"currentMusicVolume", 0);
			int currentRingVolume1 = current_volume.getInt("currentRingVolume",
					0);
			int currentSystemVolume1 = current_volume.getInt(
					"currentSystemVolume", 0);
			int currentAlarmVolume1 = current_volume.getInt(
					"currentAlarmVolume", 0);
			int currentNotifyVolume1 = current_volume.getInt(
					"currentNotifyVolume", 0);

			// 设置当前音量

			// 设置所有音量存储的音量值
			am.setStreamVolume(AudioManager.STREAM_MUSIC, currentMusicVolume1,
					AudioManager.FLAG_PLAY_SOUND);
			am.setStreamVolume(AudioManager.STREAM_SYSTEM,
					currentSystemVolume1, AudioManager.FLAG_PLAY_SOUND);
			am.setStreamVolume(AudioManager.STREAM_RING, currentRingVolume1,
					AudioManager.FLAG_PLAY_SOUND);
			am.setStreamVolume(AudioManager.STREAM_ALARM, currentAlarmVolume1,
					AudioManager.FLAG_PLAY_SOUND);
			am.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
					currentNotifyVolume1, AudioManager.FLAG_PLAY_SOUND);
			// 更新seekBar进度
			updateSeekBarProgress();

			break;

		}

	}

	public  void oneKeySilent() {
		// 设置所有音量为零
		am.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
				AudioManager.FLAG_PLAY_SOUND);
		am.setStreamVolume(AudioManager.STREAM_SYSTEM, 0,
				AudioManager.FLAG_PLAY_SOUND);
		am.setStreamVolume(AudioManager.STREAM_RING, 0,
				AudioManager.FLAG_PLAY_SOUND);
		am.setStreamVolume(AudioManager.STREAM_ALARM, 0,
				AudioManager.FLAG_PLAY_SOUND);
		am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0,
				AudioManager.FLAG_PLAY_SOUND);
	}

	private void toggle() {
		MainActivity mianActivity = (MainActivity) activity;
		SlidingMenu slidingMenu = mianActivity.getSlidingMenu();
		slidingMenu.toggle();
	}

	/**
	 * 可以显示界面和交互的时候，更新音量界面
	 */
	@Override
	public void onResume() {

		super.onResume();

		
		//更新音量界面
		updateSeekBarProgress();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 监听系统音量按键
	 * 
	 * @param keyCode
	 * @param event
	 * @param b
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event, boolean b) {

		// 获取保存在sp的checkBox状态，进行相应的设置
		boolean cb_music_state = checkbox_state.getBoolean("cb_music", true);
		boolean cb_ring_state = checkbox_state.getBoolean("cb_ring", true);
		boolean cb_notify_state = checkbox_state.getBoolean("cb_notify", true);
		boolean cb_alarm_state = checkbox_state.getBoolean("cb_alarm", true);
		boolean cb_system_state = checkbox_state.getBoolean("cb_system", true);

		// 获取手机当前音量值
		switch (keyCode) {

		// 音量减小
		case KeyEvent.KEYCODE_VOLUME_DOWN:

			// // 调节音乐音量
			// am.adjustStreamVolume(AudioManager.STREAM_MUSIC,
			// AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
			// // 调节铃声音量
			// am.adjustStreamVolume(AudioManager.STREAM_RING,
			// AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
			//
			// // 调节闹钟音量
			// am.adjustStreamVolume(AudioManager.STREAM_ALARM,
			// AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
			// // 调节系统音量
			// am.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
			// AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
			// // 调节通知音量
			// am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
			// AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);

			if (cb_music_state) {

				// 调节音乐音量
				am.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
			}

			if (cb_ring_state) {
				// 调节铃声音量
				am.adjustStreamVolume(AudioManager.STREAM_RING,
						AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
			}

			if (cb_notify_state) {
				// 调节通知音量
				am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
						AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
			}
			if (cb_alarm_state) {
				// 调节闹钟音量
				am.adjustStreamVolume(AudioManager.STREAM_ALARM,
						AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
			}
			if (cb_system_state) {

				// 调节系统音量
				am.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
						AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);

			}

			// 更新seekBar进度
			updateSeekBarProgress();

			return true;
			// 音量增大
		case KeyEvent.KEYCODE_VOLUME_UP:

			// // 调节音乐音量
			// am.adjustStreamVolume(AudioManager.STREAM_MUSIC,
			// AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
			// // 调节铃声音量
			// am.adjustStreamVolume(AudioManager.STREAM_RING,
			// AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
			//
			// // 调节闹钟音量
			// am.adjustStreamVolume(AudioManager.STREAM_ALARM,
			// AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
			// // 调节系统音量
			// am.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
			// AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
			// // 调节通知音量
			// am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
			// AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

			if (cb_music_state) {

				// 调节音乐音量
				am.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
			}

			if (cb_ring_state) {
				// 调节铃声音量
				am.adjustStreamVolume(AudioManager.STREAM_RING,
						AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
			}

			if (cb_notify_state) {
				// 调节通知音量
				am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
						AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
			}
			if (cb_alarm_state) {
				// 调节闹钟音量
				am.adjustStreamVolume(AudioManager.STREAM_ALARM,
						AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
			}
			if (cb_system_state) {

				// 调节系统音量
				am.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
						AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

			}

			// 更新seekBar进度
			updateSeekBarProgress();

			return true;
		}

		return b;

	}

	/**
	 * 获取当前各种音量值，且更新seekBar进度
	 */
	public void updateSeekBarProgress() {

		// 获取当前系统音量

		int currentMusicVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);

		int currentRingVolume = am.getStreamVolume(AudioManager.STREAM_RING);

		int currentSystemVolume = am
				.getStreamVolume(AudioManager.STREAM_SYSTEM);

		int currentAlarmVolume = am.getStreamVolume(AudioManager.STREAM_ALARM);
		int currentNotifyVolume = am
				.getStreamVolume(AudioManager.STREAM_NOTIFICATION);

		// 更新seekBar
		sb_music_volume.setProgress(currentMusicVolume);
		sb_ring_volume.setProgress(currentRingVolume);
		sb_alarm_volume.setProgress(currentAlarmVolume);
		sb_system_volume.setProgress(currentSystemVolume);
		sb_notify_volume.setProgress(currentNotifyVolume);

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

		int id = seekBar.getId();
		switch (id) {
		case R.id.sb_music_volume:
			System.out.println("当前当前媒体音量进度progress" + progress);

			// 设置当前媒体音量值
			am.setStreamVolume(AudioManager.STREAM_MUSIC, progress,
					AudioManager.FLAG_PLAY_SOUND);

			break;
		case R.id.sb_ring_volume:
			System.out.println("当前当前铃声音量值进度progress" + progress);
			// 设置当前铃声音量值
			am.setStreamVolume(AudioManager.STREAM_RING, progress,
					AudioManager.FLAG_PLAY_SOUND);

			break;
		case R.id.sb_alarm_volume:
			System.out.println("当前当前闹钟音量进度progress" + progress);
			// 设置当前闹钟音量值
			am.setStreamVolume(AudioManager.STREAM_ALARM, progress,
					AudioManager.FLAG_PLAY_SOUND);

			break;
		case R.id.sb_system_volume:
			System.out.println("当前当前系统音量进度progress" + progress);
			// 设置当前系统音量值
			am.setStreamVolume(AudioManager.STREAM_SYSTEM, progress,
					AudioManager.FLAG_PLAY_SOUND);

		case R.id.sb_notify_volume:
			System.out.println("当前当前通知音量进度progress" + progress);
			// 设置当前系统音量值
			am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, progress,
					AudioManager.FLAG_PLAY_SOUND);

			break;

		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

}
