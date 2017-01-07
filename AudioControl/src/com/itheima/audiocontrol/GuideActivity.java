package com.itheima.audiocontrol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class GuideActivity extends Activity {

	private ViewPager vp_guide;
	private int[] guideResources;
	private ImageButton btn_guide;
	private SharedPreferences system_config;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);

		system_config = getSharedPreferences("system_config",
				Context.MODE_PRIVATE);

		guideResources = new int[] { R.drawable.guide, R.drawable.guide2 };

		vp_guide = (ViewPager) findViewById(R.id.vp_guide);
		btn_guide = (ImageButton) findViewById(R.id.btn_guide);

		GuideAdaptor guideAdaptor = new GuideAdaptor();
		vp_guide.setAdapter(guideAdaptor);

		// System.out.println("guideResources的长度为"+guideResources.length);

		vp_guide.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {

				if (pos == guideResources.length - 1) {

					btn_guide.setVisibility(View.VISIBLE);

				} else {
					btn_guide.setVisibility(View.INVISIBLE);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		btn_guide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转到主页面

				finish();

				system_config.edit().putBoolean("isFirstComApp", false)
						.commit();

			}
		});

	}

	class GuideAdaptor extends PagerAdapter {

		@Override
		public int getCount() {
			return guideResources.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView((View) object);

		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			ImageView imageView = new ImageView(getApplicationContext());

			// Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
			// guideResources[position]);
			//
			// imageView.setImageBitmap(bitmap);
			imageView.setImageResource(guideResources[position]);
			imageView.setScaleType(ScaleType.FIT_XY);

			container.addView(imageView);

			return imageView;
		}

	}

}
