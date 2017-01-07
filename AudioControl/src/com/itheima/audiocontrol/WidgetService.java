package com.itheima.audiocontrol;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetService extends Service {

	private RemoteViews remoteViews;
	private AppWidgetManager aWM;
	private ComponentName componentName;
	private InnerReceiver mInnerReceiver;
	private MainActivity mainActivity;
	private ContentFragment contentFragment;

	@Override
	public IBinder onBind(Intent intent) {
		return null;

//		return new ContorlAudio();
	}

//	public class ContorlAudio extends Binder {
//
//		public Service getService() {
//
//			return WidgetService.this;
//
//		}
//
//	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		unregisterReceiver(mInnerReceiver);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		updateAppWidget();

		// 注册开锁,解锁广播接受者
		IntentFilter intentFilter = new IntentFilter();
		// 开锁action
		intentFilter.addAction("Intent.ACTION_SILENT");
		// 解锁action

		mInnerReceiver = new InnerReceiver();
		registerReceiver(mInnerReceiver, intentFilter);
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
		remoteViews.setOnClickPendingIntent(R.id.ib_widget_silent, broadcast);

		componentName = new ComponentName(this, MyWidgetProvider.class);
		// 更新窗体小部件
		aWM.updateAppWidget(componentName, remoteViews);
	}

	class InnerReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("Intent.ACTION_SILENT")) {

				Toast.makeText(getApplicationContext(), "已经全面静音", 0).show();
				// System.out.println("Intent.ACTION_CHANGE_TEXT收到了。。。。。");

				// 实现静音逻辑
//				contentFragment.oneKeySilent();
				
				AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				
				
				
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
				
				

				// 更新窗体小部件
				aWM.updateAppWidget(componentName, remoteViews);

			}
		}
	}

	// public void test() {
	//
	// Toast.makeText(getApplicationContext(),
	// "MyWidgetProvider被acyivity调用到了。。。。。。。。", 0).show();
	//
	// System.out.println("MyWidgetProvider被acyivity调用到了。。。。。。。。");
	//
	// }

//	public void getMainActivity(MainActivity mainActivity) {
//
//		this.mainActivity = mainActivity;
//
//		contentFragment = mainActivity.getContentFragment();
//
//	}

}
