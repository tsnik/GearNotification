package ru.nstsyrlin.gearnotification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StatisticsDb extends SQLiteOpenHelper
{
  private static final String CREATE_STATISTICS = "CREATE TABLE IF NOT EXISTS statistics(_id INTEGER PRIMARY KEY AUTOINCREMENT, app TEXT, package TEXT, count INTEGER, time TEXT);";
  public static final String DATABASE_NAME = "n_statistics.db";
  public static final int DATABASE_VERSION = 1;
  public static final String STATISTICS_APP = "app";
  public static final String STATISTICS_COUNT = "count";
  public static final String STATISTICS_ID = "_id";
  public static final String STATISTICS_PACKAGE = "package";
  public static final String STATISTICS_TIME = "time";
  public static final String TABLE_STATISTICS = "statistics";
  public static final String TAG = StatisticsDb.class.getName();
  public static String lock = "dblock";

  public StatisticsDb(Context paramContext)
  {
    super(paramContext, "n_statistics.db", null, 4);
  }

  public boolean exists(String paramString)
  {
    synchronized (lock)
    {
      SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
      Cursor localCursor = localSQLiteDatabase.query("statistics", null, "package=\"" + paramString + "\"", null, null, null, null);
      if (localCursor == null)
      {
        localSQLiteDatabase.close();
        return false;
      }
      int i = localCursor.getCount();
      boolean bool = false;
      if (i > 0)
        bool = true;
      localCursor.close();
      localSQLiteDatabase.close();
      return bool;
    }
  }

  public Cursor getStatistics()
  {
    synchronized (lock)
    {
      SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
      Cursor localCursor = localSQLiteDatabase.query("statistics", null, null, null, null, null, "app ASC");
      localCursor.moveToFirst();
      localSQLiteDatabase.close();
      return localCursor;
    }
  }

  public long insertNotification(String paramString1, String paramString2, long paramLong)
  {
    while (true)
    {
      SQLiteDatabase localSQLiteDatabase;
      Cursor localCursor;
      synchronized (lock)
      {
        localSQLiteDatabase = getWritableDatabase();
        localCursor = localSQLiteDatabase.query("statistics", null, "package=\"" + paramString2 + "\"", null, null, null, null);
        if (localCursor == null)
        {
          localSQLiteDatabase.close();
          return -1L;
        }
        if (localCursor.getCount() == 0)
        {
          ContentValues localContentValues1 = new ContentValues();
          localContentValues1.put("app", paramString1);
          localContentValues1.put("package", paramString2);
          localContentValues1.put("count", Integer.valueOf(1));
          localContentValues1.put("time", Long.valueOf(paramLong));
          long l1 = localSQLiteDatabase.insert("statistics", null, localContentValues1);
          localCursor.close();
          localSQLiteDatabase.close();
          return l1;
        }
      }
      localCursor.moveToFirst();
      long l2 = localCursor.getLong(localCursor.getColumnIndex("_id"));
      long l3 = 1L + localCursor.getLong(localCursor.getColumnIndex("count"));
      ContentValues localContentValues2 = new ContentValues();
      localContentValues2.put("count", Long.valueOf(l3));
      localSQLiteDatabase.update("statistics", localContentValues2, "_id=" + l2, null);
      long l1 = l2;
      return l1;
    }
  }

  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS statistics(_id INTEGER PRIMARY KEY AUTOINCREMENT, app TEXT, package TEXT, count INTEGER, time TEXT);");
  }

  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS statistics");
    onCreate(paramSQLiteDatabase);
  }
}
