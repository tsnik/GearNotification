package ru.nstsyrlin.gearnotification;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationReceiver extends NotificationListenerService {
	public static final String TAG = "NotificationReceiver";
	
	 Boolean bound = false;
	  ServiceConnection sConn;
	  Intent intent;
	  ServiceProvider myService;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		intent = new Intent(this, ServiceProvider.class);
		sConn = new ServiceConnection() {
			  @Override
		      public void onServiceDisconnected(ComponentName name) {
		        Log.d(TAG, "MainActivity onServiceDisconnected");
		        bound = false;
		      }
			  @Override
		      public void onServiceConnected(ComponentName name, IBinder binder) {
			        Log.d(TAG, "MainActivity onServiceConnected");
			        myService = ((ServiceProvider.MyBinder) binder).getService(); 
			        bound = true;
			  }
		    };
		    Boolean res=bindService(intent, sConn, BIND_AUTO_CREATE);
		    Log.d(TAG, res.toString());
		    Log.d(TAG, bound.toString());
		super.onCreate();
	}
	
	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		// TODO Auto-generated method stub
		Notification not=sbn.getNotification();
		Log.e(TAG, "Notification received: "+not.tickerText);
		Log.d(TAG, bound.toString());
		myService.sendNotify((String) not.tickerText);
		}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		// TODO Auto-generated method stub

	}

}
