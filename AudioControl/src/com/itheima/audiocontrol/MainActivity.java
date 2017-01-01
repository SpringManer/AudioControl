package com.itheima.audiocontrol;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

	private FragmentManager fm;

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

		// 初始化Frgment

		fm = getSupportFragmentManager();

		FragmentTransaction transaction = fm.beginTransaction();

		transaction.replace(R.id.fl_content, new ContentFragment(),
				"ContentFragment");
		transaction
				.replace(R.id.fl_slide, new SlideFragment(), "SlideFragment");

		transaction.commit();

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
