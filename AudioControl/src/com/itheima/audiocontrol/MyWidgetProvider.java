package com.itheima.audiocontrol;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class MyWidgetProvider extends AppWidgetProvider {

	private SharedPreferences system_config;

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		context.startService(new Intent(context, WidgetService.class));
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onEnabled(Context context) {

		system_config = context.getSharedPreferences("system_config",
				Context.MODE_PRIVATE);

		system_config.edit().putBoolean("isWidgetRuning", true).commit();

		// Toast.makeText(context, "onEnabled小窗口部件已经创建了", 0).show();

		context.startService(new Intent(context, WidgetService.class));
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		system_config = context.getSharedPreferences("system_config",
				Context.MODE_PRIVATE);

		system_config.edit().putBoolean("isWidgetRuning", false).commit();

		context.stopService(new Intent(context, WidgetService.class));
		super.onDisabled(context);
	}

}
