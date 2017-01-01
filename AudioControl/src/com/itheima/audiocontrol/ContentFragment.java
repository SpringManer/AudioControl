package com.itheima.audiocontrol;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ContentFragment extends Fragment implements OnSeekBarChangeListener {

	private TextView music_volume;
	private TextView ring_volume;
	private TextView system_volume;
	private AudioManager am;
	private SeekBar sb_music_volume;
	private SeekBar sb_ring_volume;
	private SeekBar sb_alarm_volume;
	private SeekBar sb_system_volume;
	private Button btn_silent;
	private SeekBar sb_notify_volume;

	private FragmentActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View fragement_slide = View.inflate(activity, R.layout.fragement_content,
				null);

		music_volume = (TextView) fragement_slide
				.findViewById(R.id.tv_show_music_volume);
		ring_volume = (TextView) fragement_slide
				.findViewById(R.id.tv_show_ring_volume);
		system_volume = (TextView) fragement_slide
				.findViewById(R.id.tv_show_system_volume);
		btn_silent = (Button) fragement_slide.findViewById(R.id.btn_silent);

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

		btn_silent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

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
				// 更新seekBar进度
				updateSeekBarProgress();

			}
		});

		return fragement_slide;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event, boolean b) {

		// 获取手机当前音量值
		switch (keyCode) {

		// 音量减小
		case KeyEvent.KEYCODE_VOLUME_DOWN:

			// 调节音乐音量
			am.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);

			// 调节系统音量
			am.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);

			// 调节铃声音量
			am.adjustStreamVolume(AudioManager.STREAM_RING,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);

			// 调节闹钟音量
			am.adjustStreamVolume(AudioManager.STREAM_ALARM,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
			// 调节通知音量
			am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);

			// 更新seekBar进度
			updateSeekBarProgress();

			return true;
			// 音量增大
		case KeyEvent.KEYCODE_VOLUME_UP:

			// 调节音乐音量
			am.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
			// 调节铃声音量
			am.adjustStreamVolume(AudioManager.STREAM_RING,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

			// 调节闹钟音量
			am.adjustStreamVolume(AudioManager.STREAM_ALARM,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
			// 调节系统音量
			am.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
			// 调节通知音量
			am.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

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
