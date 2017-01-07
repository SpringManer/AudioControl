package com.itheima.audiocontrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);

		ImageButton ib_back = (ImageButton) findViewById(R.id.ib_about_back);

		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});

	}

}
