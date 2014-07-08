package ru.nstsyrlin.gearnotification;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private LinearLayout layoutAccessibilityService;
	private LinearLayout layoutNotificationListener;
	private TextView linkText;
	private Button openNotificationAccess;
	private Button openSettingsButton;
	private Button createNotifactionButton;
	private TextView serviceStatusText;
	
	private View.OnClickListener createNotificationOnClick = new View.OnClickListener()
	  {
	    public void onClick(View paramAnonymousView)
	    {
	      if ((Util.isAccessibilityServiceEnabled(MainActivity.this.getApplicationContext())) || (Build.VERSION.SDK_INT >= 18))
	      {
	        Intent localIntent = new Intent();
	        localIntent.setClass(MainActivity.this.getApplicationContext(), MainActivity.class);
	        PendingIntent localPendingIntent = PendingIntent.getActivity(MainActivity.this.getApplicationContext(), 0, localIntent, 0);
	        NotificationCompat.Builder localBuilder = new NotificationCompat.Builder(MainActivity.this.getApplicationContext()).setSmallIcon(R.drawable.ic_launcher).setContentTitle(MainActivity.this.getText(R.string.app_name).toString()).setContentText(MainActivity.this.getText(R.string.test_notif_content).toString() + " [" + Math.round(100.0D * Math.random()) + "]").setTicker(MainActivity.this.getText(R.string.app_name).toString()).setContentIntent(localPendingIntent).setAutoCancel(true);
	        ((NotificationManager)MainActivity.this.getSystemService("notification")).notify(0, localBuilder.build());
	        return;
	      }
	      AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(MainActivity.this);
	      localBuilder1.setTitle(R.string.notif_error_header);
	      localBuilder1.setMessage(R.string.notif_error_text);
	      localBuilder1.setNegativeButton(R.string.notif_error_close, null);
	      localBuilder1.show();
	    }
	  };
	
	  private View.OnClickListener openNotificationAccessOnClick = new View.OnClickListener()
	  {
	    public void onClick(View paramAnonymousView)
	    {
	      MainActivity.this.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
	    }
	  };
	  
	  private View.OnClickListener openSettingsOnClick = new View.OnClickListener()
	  {
	    public void onClick(View paramAnonymousView)
	    {
	      Intent localIntent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
	      MainActivity.this.startActivityForResult(localIntent, 0);
	    }
	  };
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//startActivity(new Intent( "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
		this.layoutAccessibilityService=(LinearLayout)findViewById(R.id.layoutAccessibilityService);
		this.layoutNotificationListener=(LinearLayout)findViewById(R.id.layoutNotificationListener);
		this.openNotificationAccess=(Button)findViewById(R.id.buttonNotificationListener);
		this.openSettingsButton=(Button)findViewById(R.id.buttonAccessibilityService);
		this.createNotifactionButton=(Button)findViewById(R.id.buttonTest);
		this.serviceStatusText=(TextView)findViewById(R.id.textAccessibilityServiceStatus);
		if (Build.VERSION.SDK_INT >= 18)
	    {
	      this.layoutAccessibilityService.setVisibility(8);
	      this.layoutNotificationListener.setVisibility(0);
	    }
		openNotificationAccess.setOnClickListener(openNotificationAccessOnClick);
		openSettingsButton.setOnClickListener(openSettingsOnClick);
		createNotifactionButton.setOnClickListener(createNotificationOnClick);
		if ((Util.isAccessibilityServiceEnabled(getApplicationContext())) && (Build.VERSION.SDK_INT >= 18))
	    {
	      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
	      localBuilder.setTitle(R.string.sdk18_title);
	      localBuilder.setMessage(R.string.sdk18_text);
	      localBuilder.setPositiveButton(R.string.sdk18_open, new DialogInterface.OnClickListener()
	      {
	        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
	        {
	          Intent localIntent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
	          MainActivity.this.startActivityForResult(localIntent, 0);
	        }
	      });
	      localBuilder.setNegativeButton(R.string.sdk18_close, new DialogInterface.OnClickListener()
	      {
	        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
	        {
	        }
	      });
	      localBuilder.show();
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		startActivity(new Intent(this, Preferences.class));
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Resources localResources = getResources();
		if (Util.isAccessibilityServiceEnabled(getApplicationContext()))
	    {
	      this.serviceStatusText.setText(Html.fromHtml(localResources.getString(R.string.text_accessibility_service_status_enabled)));
	      return;
	    }
	    this.serviceStatusText.setText(Html.fromHtml(localResources.getString(R.string.text_accessibility_service_status_disabled)));
	}
}
