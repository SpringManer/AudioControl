package com.itheima.audiocontrol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
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
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ContentFragment extends Fragment implements
		OnSeekBarChangeListener, OnClickListener {

	private static final int CROP_PHOTO = 1;
	private static final int CHOICE_PHOTO = 2;
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
	private SharedPreferences system_config;
	private Bitmap bgResult;
	private ImageButton ib_chang_bg;

	Uri choice_imageUri;
	private Bitmap bitmap;
	private File choice_image;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();

		current_volume = activity.getSharedPreferences("current_volume",
				Context.MODE_PRIVATE);

		checkbox_state = activity.getSharedPreferences("checkbox_state",
				Context.MODE_PRIVATE);

		system_config = activity.getSharedPreferences("system_config",
				Context.MODE_PRIVATE);

		choice_image = new File(Environment.getExternalStorageDirectory(),
				"choice_image.jpg");

		choice_imageUri = Uri.fromFile(choice_image);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View fragement_slide = View.inflate(activity,
				R.layout.fragement_content, null);

		intUI(fragement_slide);

		// 动态随机更换壁纸
		// randomChangeBg();

		// 恢复之前设置的背景

		try {
			bitmap = BitmapFactory.decodeStream(activity.getContentResolver()
					.openInputStream(choice_imageUri));

			if (bitmap != null) {

				Drawable drawable = new BitmapDrawable(bitmap);

				// iv_bg.setImageBitmap(bitmap);

				ll_content_root.setBackground(drawable);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		boolean isWidgetRuning = system_config.getBoolean("isWidgetRuning",
				false);

		boolean serviceRunning = isServiceRunning();

		if (!serviceRunning) {

			if (isWidgetRuning) {

				Intent intent = new Intent(activity, WidgetService.class);

				activity.startService(intent);

				Toast.makeText(getActivity(), "后台一键静音服务已经重新启动...", 0).show();

			}

		}

		return fragement_slide;
	}

	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) activity
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {

			System.out.println("运行的服务" + service.service.getClassName());

			if ("com.itheima.audiocontrol.WidgetService".equals(service.service
					.getClassName())) {

				return true;
			}
		}
		return false;
	}

	// /**
	// * 随机更换壁纸
	// */
	// private void randomChangeBg() {
	// int[] pic = new int[] { R.drawable.pc1, R.drawable.pc2, R.drawable.pc3,
	// R.drawable.pc4, R.drawable.pc5, R.drawable.pc6, R.drawable.pc7 };
	//
	// Random random = new Random();
	// int picNum = random.nextInt(7);
	//
	// Options opts = new BitmapFactory.Options();
	// opts.inJustDecodeBounds = true;
	//
	// BitmapFactory.decodeResource(getResources(), pic[picNum], opts);
	// // 获取到图片的宽和高信息
	// int imageWidth = opts.outWidth;
	// int imageHeight = opts.outHeight;
	// // 获取到屏幕对象
	// Display display = activity.getWindowManager().getDefaultDisplay();
	// // 获取到屏幕的真是宽和高
	// int screenWidth = display.getWidth();
	// int screenHeight = display.getHeight();
	//
	// // 计算缩放比例
	// int widthScale = imageWidth / screenWidth;
	// int heightScale = imageHeight / screenHeight;
	// // 计算出最大的比例
	// int scale = widthScale > heightScale ? widthScale : heightScale;
	// // 使用缩放比例进行缩放加载图片
	// opts.inJustDecodeBounds = false; // 加载器就会返回图片了
	// // 配置该参数加载图片时 BitmapFactory 就会自动缩放图片
	// opts.inSampleSize = scale;
	// bgResult = BitmapFactory.decodeResource(getResources(), pic[picNum],
	// opts);
	// Drawable drawable = new BitmapDrawable(bgResult);
	//
	// ll_content_root.setBackground(drawable);
	//
	// // ll_content_root.setBackgroundResource(pic[picNum]);
	// }

	@Override
	public void onDestroyView() {

		if (bitmap != null) {

			bitmap.recycle();

		}

		super.onDestroyView();

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

		ib_chang_bg = (ImageButton) fragement_slide
				.findViewById(R.id.ib_chang_bg);

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
		ib_chang_bg.setOnClickListener(this);
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

		// 更换背景逻辑
		case R.id.ib_chang_bg:
			//
			// // 创建存储文件路径
			//
			// choice_image = new File(
			// Environment.getExternalStorageDirectory(),
			// "choice_image.jpg");
			try {
				if (choice_image.exists()) {

					choice_image.delete();
				}
				choice_image.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");// 相片类型
			startActivityForResult(intent, CHOICE_PHOTO);

			break;

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case CHOICE_PHOTO:

			if (resultCode == Activity.RESULT_OK) {

				// 启动裁剪程序

				int width = 500;
				int hight = 890;

				Uri data2 = data.getData();
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(data2, "image/*");
				// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
				intent.putExtra("crop", "true");
				intent.putExtra("scale", true);// 去黑边
				intent.putExtra("scaleUpIfNeeded", true);// 去黑边
				// <a target="_blank" href="http://www.2cto.com/kf/web/asp/"
				// class="keylink"
				// style="border:none; padding:0px; margin:0px; color:rgb(51,51,51); text-decoration:none; font-size:14px; background:none">asp</a>ectX
				// aspectY 是宽高的比例
				intent.putExtra("aspectX", 1);// 输出是X方向的比例
				intent.putExtra("aspectY", 1.7);
				// outputX outputY 是裁剪图片宽高，切忌不要再改动下列数字，会卡死
				intent.putExtra("outputX", 1080);// 输出X方向的像素
				intent.putExtra("outputY", 1920);
				intent.putExtra("outputFormat",
						Bitmap.CompressFormat.JPEG.toString());
				intent.putExtra("noFaceDetection", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, choice_imageUri);
				intent.putExtra("return-data", false);// 设置为不返回数据

				startActivityForResult(intent, CROP_PHOTO);

			}

			break;

		case CROP_PHOTO:

			if (resultCode == Activity.RESULT_OK) {

				// 启动显示程序

				try {
					bitmap = BitmapFactory.decodeStream(activity
							.getContentResolver().openInputStream(
									choice_imageUri));

					Drawable drawable = new BitmapDrawable(bitmap);

					// iv_bg.setImageBitmap(bitmap);

					ll_content_root.setBackground(drawable);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}

			break;
		}

	}

	public void oneKeySilent() {
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

		// 更新音量界面
		updateSeekBarProgress();

		boolean isWidgetRuning = system_config.getBoolean("isWidgetRuning",
				false);

		boolean serviceRunning = isServiceRunning();

		if (!serviceRunning) {

			if (isWidgetRuning) {

				Intent intent = new Intent(activity, WidgetService.class);

				activity.startService(intent);

				Toast.makeText(getActivity(), "后台一键静音服务已经重新启动...", 0).show();

			}

		}
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

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

}
