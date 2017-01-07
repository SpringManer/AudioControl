package com.itheima.audiocontrol;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyWidgetProvider extends AppWidgetProvider {

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

		context.startService(new Intent(context, WidgetService.class));
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {

		context.stopService(new Intent(context, WidgetService.class));
		super.onDisabled(context);
	}


}
