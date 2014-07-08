package ru.nstsyrlin.gearnotification;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService
{
  public static final String TAG = MyAccessibilityService.class.getName();
	
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
  private void handleNotificationStateChanged(AccessibilityEvent paramAccessibilityEvent)
  {
	  Notification not=(Notification)paramAccessibilityEvent.getParcelableData();
	  List<String> notify_text=Util.getText(not);
	  String str2 = paramAccessibilityEvent.getPackageName().toString();
      String str3 = Util.getAppNameFromPackage(getApplicationContext(), str2);
      long l = System.currentTimeMillis();
      SharedPreferences localSharedPreferences = this.getSharedPreferences(this.getPackageName() + "_preferences", MODE_MULTI_PROCESS);
      boolean a=localSharedPreferences.getBoolean("PREF_ENABLE", true);
	  if((Blacklist.contains(str2)) || (!localSharedPreferences.getBoolean("PREF_ENABLE", true)))
	    		return;
	  new StatisticsDb(this).insertNotification(str3, str2, l);
	  if (!localSharedPreferences.getBoolean("PREF_APP_" + str2, true))
			return;
	  myService.sendNotify(notify_text.get(0),notify_text.get(1));
  }


  @SuppressLint({"UseSparseArrays"})
  public void onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    if (paramAccessibilityEvent.getEventType() == 64)
      handleNotificationStateChanged(paramAccessibilityEvent);
    while ((paramAccessibilityEvent.getEventType() == 1) && (paramAccessibilityEvent.getPackageName().equals("com.android.systemui")))
      return;
    paramAccessibilityEvent.getEventType();
  }

  public void onInterrupt()
  {
  }

  protected void onServiceConnected()
  {
    AccessibilityServiceInfo localAccessibilityServiceInfo = new AccessibilityServiceInfo();
    localAccessibilityServiceInfo.eventTypes = 97;
    localAccessibilityServiceInfo.feedbackType = 16;
    localAccessibilityServiceInfo.notificationTimeout = 100L;
    setServiceInfo(localAccessibilityServiceInfo);
  }
}
