package ru.nstsyrlin.gearnotification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.Log;
import android.widget.RemoteViews;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.GZIPOutputStream;
import ru.nstsyrlin.gearnotification.StatisticsDb;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Util
{
  public static byte[] compress(String paramString)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(paramString.length());
    GZIPOutputStream localGZIPOutputStream = new GZIPOutputStream(localByteArrayOutputStream);
    localGZIPOutputStream.write(paramString.getBytes());
    localGZIPOutputStream.close();
    byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
    localByteArrayOutputStream.close();
    return arrayOfByte;
  }

  

  public static Drawable getAppIconFromPackage(Context paramContext, String paramString)
  {
    PackageManager localPackageManager = paramContext.getApplicationContext().getPackageManager();
    try
    {
      ApplicationInfo localApplicationInfo1 = localPackageManager.getApplicationInfo(paramString.toString(), 0);
      if (localApplicationInfo1 != null)
        return localPackageManager.getApplicationIcon(localApplicationInfo1);
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      
    }
    return null;
  }

  public static String getAppNameFromPackage(Context paramContext, String paramString)
  {
    PackageManager localPackageManager = paramContext.getApplicationContext().getPackageManager();
    try
    {
      ApplicationInfo localApplicationInfo1 = localPackageManager.getApplicationInfo(paramString.toString(), 0);
      if (localApplicationInfo1 != null)
      {
        String localObject = (String) localPackageManager.getApplicationLabel(localApplicationInfo1);
        return (String)localObject;
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
        String localObject = "Unknown";
        return localObject;
    }
	return "Unknown";
  }

  @SuppressLint({"NewApi"})
  public static long getInstallTime(Context paramContext)
  {
    if (Build.VERSION.SDK_INT >= 9)
      try
      {
        long l = paramContext.getPackageManager().getPackageInfo("org.hcilab.projects.notification", 0).firstInstallTime;
        return l;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        localNameNotFoundException.printStackTrace();
      }
    return -1L;
  }

  /*public static JSONObject getUserSettings(Context paramContext)
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(paramContext);
    String str1 = localSharedPreferences.getString("PREF_CODE", "");
    boolean bool1 = localSharedPreferences.getBoolean("PREF_WIFI", false);
    boolean bool2 = localSharedPreferences.getBoolean("PREF_PRIVACY", true);
    long l = getInstallTime(paramContext);
    JSONArray localJSONArray = new JSONArray();
    Cursor localCursor = new StatisticsDb(paramContext).getStatistics();
    int i;
    if (localCursor != null)
      i = 0;
    JSONObject localJSONObject1;
    while (true)
    {
      if (i >= localCursor.getCount())
      {
        localCursor.close();
        localJSONObject1 = new JSONObject();
      }
      try
      {
        localJSONObject1.put("code", str1);
        localJSONObject1.put("wifionly", bool1);
        localJSONObject1.put("privacy", bool2);
        localJSONObject1.put("version", 11);
        localJSONObject1.put("install", l);
        localJSONObject1.put("locale", paramContext.getResources().getConfiguration().locale.toString());
        localJSONObject1.put("model", Build.MODEL);
        localJSONObject1.put("device", Build.DEVICE);
        localJSONObject1.put("product", Build.PRODUCT);
        localJSONObject1.put("manufacturer", Build.MANUFACTURER);
        localJSONObject1.put("sdk", Build.VERSION.SDK_INT);
        localJSONObject1.put("timezone", TimeZone.getDefault().getID());
        localJSONObject1.put("apps", localJSONArray);
        return localJSONObject1;
        JSONObject localJSONObject2 = new JSONObject();
        String str2 = localCursor.getString(localCursor.getColumnIndex("package"));
        int j = localCursor.getInt(localCursor.getColumnIndex("count"));
        boolean bool3 = localSharedPreferences.getBoolean("PREF_APP_" + str2, true);
        try
        {
          localJSONObject2.put("package", str2);
          localJSONObject2.put("count", j);
          localJSONObject2.put("enabled", bool3);
          localJSONArray.put(localJSONObject2);
          localCursor.moveToNext();
          i++;
        }
        catch (JSONException localJSONException2)
        {
          while (true)
            localJSONException2.printStackTrace();
        }
      }
      catch (JSONException localJSONException1)
      {
        localJSONException1.printStackTrace();
      }
    }
    return localJSONObject1;
  }*/

  public static boolean isAccessibilityServiceEnabled(Context paramContext)
  {
	  int j=0;
    try
    {
      int i = Settings.Secure.getInt(paramContext.getContentResolver(), "accessibility_enabled");
      TextUtils.SimpleStringSplitter localSimpleStringSplitter = new TextUtils.SimpleStringSplitter(':');
      if (i == 1)
      {
        String str = Settings.Secure.getString(paramContext.getContentResolver(), "enabled_accessibility_services");
        if (str != null)
          localSimpleStringSplitter.setString(str);
      }
      do
        if (!localSimpleStringSplitter.hasNext())
          return false;
      while (!localSimpleStringSplitter.next().equalsIgnoreCase("ru.nstsyrlin.gearnotification/ru.nstsyrlin.gearnotification.MyAccessibilityService"));
      return true;
    }
    catch (Settings.SettingNotFoundException localSettingNotFoundException)
    {
        j= 0;
    }
    return false;
  }

  public static boolean isNetworkAvailable(Context paramContext)
  {
    NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    return (localNetworkInfo != null) && (localNetworkInfo.isConnected());
  }

  public static boolean isWifiConnected(Context paramContext)
  {
    return ((ConnectivityManager)paramContext.getSystemService("connectivity")).getNetworkInfo(1).isConnected();
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
}
