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
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

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
	    List<String> notify_text=getText(not);
		Log.e(TAG, "Notification received: "+notify_text);
		Log.d(TAG, bound.toString());
		myService.sendNotify(notify_text.get(0),notify_text.get(1));
		}

	
	//http://stackoverflow.com/questions/9292032/extract-notification-text-from-parcelable-contentview-or-contentintent
	public static List<String> getText(Notification notification)
	{
	    // We have to extract the information from the view
	    RemoteViews        views = notification.bigContentView;
	    if (views == null) views = notification.contentView;
	    if (views == null) return null;

	    // Use reflection to examine the m_actions member of the given RemoteViews object.
	    // It's not pretty, but it works.
	    List<String> text = new ArrayList<String>();
	    try
	    {
	        Field field = views.getClass().getDeclaredField("mActions");
	        field.setAccessible(true);

	        @SuppressWarnings("unchecked")
	        ArrayList<Parcelable> actions = (ArrayList<Parcelable>) field.get(views);

	        // Find the setText() and setTime() reflection actions
	        for (Parcelable p : actions)
	        {
	            Parcel parcel = Parcel.obtain();
	            p.writeToParcel(parcel, 0);
	            parcel.setDataPosition(0);

	            // The tag tells which type of action it is (2 is ReflectionAction, from the source)
	            int tag = parcel.readInt();
	            if (tag != 2) continue;

	            // View ID
	            parcel.readInt();

	            String methodName = parcel.readString();
	            if (methodName == null) continue;

	            // Save strings
	            else if (methodName.equals("setText"))
	            {
	                // Parameter type (10 = Character Sequence)
	                parcel.readInt();

	                // Store the actual string
	                String t = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel).toString().trim();
	                text.add(t);
	            }

	            // Save times. Comment this section out if the notification time isn't important
	            else if (methodName.equals("setTime"))
	            {
	                // Parameter type (5 = Long)
	                parcel.readInt();

	                String t = new SimpleDateFormat("h:mm a").format(new Date(parcel.readLong()));
	                text.add(t);
	            }

	            parcel.recycle();
	        }
	    }

	    // It's not usually good style to do this, but then again, neither is the use of reflection...
	    catch (Exception e)
	    {
	        Log.e("NotificationClassifier", e.toString());
	    }

	    return text;
	}
	
	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		// TODO Auto-generated method stub

	}

}
