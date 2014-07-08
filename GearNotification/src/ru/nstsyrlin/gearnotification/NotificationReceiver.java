package ru.nstsyrlin.gearnotification;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import ru.nstsyrlin.gearnotification.Util;;

public class NotificationReceiver extends NotificationListenerService {
	public static final String TAG = "NotificationReceiver";
	
	 Boolean bound = false;
	  ServiceConnection sConn;
	  Intent intent;
	  ServiceProvider myService;
	
	@Override
	public void onCreate() {
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
	    List<String> notify_text=Util.getText(not);
		Log.e(TAG, "Notification received: "+notify_text);
		Log.d(TAG, bound.toString());
		String str2 = sbn.getPackageName();
	    String str3 = Util.getAppNameFromPackage(getApplicationContext(), str2);
	    long l = System.currentTimeMillis();
		SharedPreferences localSharedPreferences = this.getSharedPreferences(this.getPackageName() + "_preferences", MODE_MULTI_PROCESS);
	    if((Blacklist.contains(str2)) || (!localSharedPreferences.getBoolean("PREF_ENABLE", true)))
	    		return;
		new StatisticsDb(this).insertNotification(str3, str2, l);
		if (!localSharedPreferences.getBoolean("PREF_APP_" + str2, true))
			return;
		myService.sendNotify(notify_text.get(0),notify_text.get(1));
		}

	
	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		// TODO Auto-generated method stub

	}

}
