package com.itheima.audiocontrol;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class WidgetService extends Service {

	private RemoteViews remoteViews;
	private AppWidgetManager aWM;
	private ComponentName componentName;

	@Override
	public IBinder onBind(Intent intent) {
		return null;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		updateAppWidget();

	}

	protected void updateAppWidget() {
		aWM = AppWidgetManager.getInstance(this);
		remoteViews = new RemoteViews(getPackageName(), R.layout.widget_layout);

		// 点击窗体小部件,进入应用
		// 1:在那个控件上响应点击事件2:延期的意图
		// Intent intent = new Intent("android.intent.action.HOME");
		// intent.addCategory("android.intent.category.DEFAULT");
		// PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
		// intent, PendingIntent.FLAG_CANCEL_CURRENT);
		// remoteViews.setOnClickPendingIntent(R.id.btn_demo, pendingIntent);

		// //通过延期意图发送广播,在广播接受者中杀死进程,匹配规则看action
		Intent broadCastintent = new Intent("Intent.ACTION_SILENT");
		PendingIntent broadcast = PendingIntent.getBroadcast(this, 0,
				broadCastintent, PendingIntent.FLAG_CANCEL_CURRENT);

		// 注册点击
		remoteViews.setOnClickPendingIntent(R.id.ib_widget_guide, broadcast);

		componentName = new ComponentName(this, MyWidgetProvider.class);
		// 更新窗体小部件
		aWM.updateAppWidget(componentName, remoteViews);
	}

}
