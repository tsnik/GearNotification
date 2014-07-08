package ru.nstsyrlin.gearnotification;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import java.util.ArrayList;
import ru.nstsyrlin.gearnotification.StatisticsDb;
import ru.nstsyrlin.gearnotification.Util;

public class Preferences extends PreferenceActivity
{
  private ArrayList<CheckBoxPreference> cbList = new ArrayList();
  private ArrayList<String> pnList = new ArrayList();

  @SuppressLint({"NewApi"})
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    addPreferencesFromResource(R.xml.prefs);
    Cursor localCursor = new StatisticsDb(this).getStatistics();
    PreferenceCategory localPreferenceCategory=null;
    Resources localResources=null;
    if (localCursor != null)
    {
      PreferenceScreen localPreferenceScreen1 = getPreferenceScreen();
      localPreferenceCategory = new PreferenceCategory(this);
      localPreferenceCategory.setTitle(R.string.prefs_category_apps);
      localPreferenceScreen1.addPreference(localPreferenceCategory);
      PreferenceScreen localPreferenceScreen2 = getPreferenceManager().createPreferenceScreen(this);
      localPreferenceScreen2.setSummary(R.string.prefs_summary_apps);
      localPreferenceCategory.addPreference(localPreferenceScreen2);
      getPreferenceManager().setSharedPreferencesMode(MODE_MULTI_PROCESS);
      getPreferenceManager().setSharedPreferencesName(this.getPackageName() + "_preferences");
      if (localCursor.getCount() > 0)
        localResources = getResources();
    }
    for (int i = 0; ; i++)
    {
      if (i >= localCursor.getCount())
      {
        if (Build.VERSION.SDK_INT >= 11)
          new LoadIcons().execute(new Void[0]);
        localCursor.close();
        return;
      }
      CheckBoxPreference localCheckBoxPreference = new CheckBoxPreference(this);
      String str1 = localCursor.getString(localCursor.getColumnIndex("app"));
      String str2 = localCursor.getString(localCursor.getColumnIndex("package"));
      String str3 = localCursor.getString(localCursor.getColumnIndex("count"));
      localCheckBoxPreference.setTitle(str1);
      localCheckBoxPreference.setSummary(String.format(localResources.getString(R.string.prefs_summary_count), new Object[] { str3 }));
      localCheckBoxPreference.setEnabled(true);
      localCheckBoxPreference.setDefaultValue(Boolean.valueOf(true));
      localCheckBoxPreference.setKey("PREF_APP_" + str2);
      if (Build.VERSION.SDK_INT >= 11)
        localCheckBoxPreference.setIcon(R.drawable.ic_launcher);
      if (str2.equals("ru.nstsyrlin.gearnotification"))
        localCheckBoxPreference.setEnabled(false);
      if (!str2.equals("com.android.systemui"))
      {
        this.cbList.add(localCheckBoxPreference);
        this.pnList.add(str2);
      }
      localPreferenceCategory.addPreference(localCheckBoxPreference);
      localCursor.moveToNext();
    }
  }

  private class LoadIcons extends AsyncTask<Void, Void, ArrayList<Drawable>>
  {
    private LoadIcons()
    {
    }

    protected ArrayList<Drawable> doInBackground(Void[] paramArrayOfVoid)
    {
      ArrayList localArrayList = new ArrayList();
      for (int i = 0; ; i++)
      {
        if (i >= Preferences.this.cbList.size())
          return localArrayList;
        localArrayList.add(Util.getAppIconFromPackage(Preferences.this, (String)Preferences.this.pnList.get(i)));
      }
    }

    @SuppressLint({"NewApi"})
    protected void onPostExecute(ArrayList<Drawable> paramArrayList)
    {
      for (int i = 0; ; i++)
      {
        if (i >= Preferences.this.cbList.size())
        {
          super.onPostExecute(paramArrayList);
          return;
        }
        ((CheckBoxPreference)Preferences.this.cbList.get(i)).setIcon((Drawable)paramArrayList.get(i));
      }
    }
  }
}
