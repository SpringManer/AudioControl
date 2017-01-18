package com.itheima.audiocontrol;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

	private FragmentManager fm;
	public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	private SharedPreferences system_config;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.content);

		setBehindContentView(R.layout.slide);

		// 设置侧边栏
		SlidingMenu slidingMenu = getSlidingMenu();

		slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_FULLSCREEN);

		// 屏幕预留的像素数
		// 200/320 *width

		int width = getWindowManager().getDefaultDisplay().getWidth();

		slidingMenu.setBehindOffset((int) ((float) 250 / (float) 320 * width));

		system_config = getSharedPreferences("system_config",
				Context.MODE_PRIVATE);

		boolean isFirstComApp = system_config.getBoolean("isFirstComApp", true);

		// 第一次进入app
		if (isFirstComApp) {
			// 创建快捷方式提示
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this);

//			View inflate = View.inflate(this, R.layout.alert_dialog, null);

//			dialog.setView(inflate);
			dialog.setCancelable(false);
			dialog.setTitle("创建桌面快捷方式？？？");
			dialog.setNegativeButton("人家不要嘛", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 跳转欢迎页面
					//
					// Intent intent = new Intent(getApplicationContext(),
					// GuideActivity.class);
					//
					// startActivity(intent);

					system_config.edit().putBoolean("isFirstComApp", false)
							.commit();

				}
			});
			dialog.setPositiveButton("就这一次哦 ", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					addShortcut("音量控制");

					// // 跳转欢迎页面
					//
					// Intent intent = new Intent(getApplicationContext(),
					// GuideActivity.class);
					//
					// startActivity(intent);

					system_config.edit().putBoolean("isFirstComApp", false)
							.commit();

				}
			});

			dialog.show();

		}

		// 初始化Frgment

		fm = getSupportFragmentManager();

		FragmentTransaction transaction = fm.beginTransaction();

		transaction.replace(R.id.fl_content, new ContentFragment(),
				"ContentFragment");
		transaction
				.replace(R.id.fl_slide, new SlideFragment(), "SlideFragment");

		transaction.commit();

	}

	public ContentFragment getContentFragment() {

		ContentFragment contentFragment = (ContentFragment) fm
				.findFragmentByTag("ContentFragment");

		return contentFragment;

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		
		super.onDestroy();
	}

	/**
	 * 创建快捷方式
	 * 
	 * @param name
	 */
	private void addShortcut(String name) {
		Intent addShortcutIntent = new Intent(ACTION_ADD_SHORTCUT);

		// 不允许重复创建
		addShortcutIntent.putExtra("duplicate", false);// 经测试不是根据快捷方式的名字判断重复的
		// 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
		// 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
		// 屏幕上没有空间时会提示
		// 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式

		// 名字
		addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

		// 图标
		addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(MainActivity.this,
						R.drawable.ic_launcher));

		// 设置关联程序
		Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
		launcherIntent.setClass(MainActivity.this, MainActivity.class);
		launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		addShortcutIntent
				.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

		// 发送广播
		sendBroadcast(addShortcutIntent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		ContentFragment contentFragment = (ContentFragment) fm
				.findFragmentByTag("ContentFragment");

		boolean onKeyDown = contentFragment.onKeyDown(keyCode, event,
				super.onKeyDown(keyCode, event));

		return onKeyDown;

	}
}
