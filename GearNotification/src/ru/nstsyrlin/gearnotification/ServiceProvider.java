package ru.nstsyrlin.gearnotification;

import java.io.IOException;
import java.util.HashMap;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

public class ServiceProvider extends SAAgent{
	
	public static final String TAG = "GearNotificationProviderService";
	String title="";
	String text="";
	MyBinder binder = new MyBinder();
	public class GearNotificationConnection extends SASocket{
		private int mConnectionId; 

		protected GearNotificationConnection(String arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}
		
		public GearNotificationConnection()
		{
			super(GearNotificationConnection.class.getName());
		}
		@Override
		public void onError(int arg0, String errorString, int error) {
			// TODO Auto-generated method stub
			Log.e(TAG, "Connection is not alive ERROR: " + errorString + "  "
					+ error);
		}

		@Override
		public void onReceive(int arg0, byte[] arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void onServiceConnectionLost(int errorCode) {
			// TODO Auto-generated method stub
			Log.e(TAG, "onServiceConectionLost  for peer = " + mConnectionId
					+ "error code =" + errorCode);

		}
		
	}
	protected ServiceProvider(String arg0, Class<? extends SASocket> arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	
	public ServiceProvider() {
		super(TAG, GearNotificationConnection.class);
		Log.d(TAG, "Start service");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onFindPeerAgentResponse(SAPeerAgent peerAgent, int result) {
		// TODO Auto-generated method stub
		if(result==PEER_AGENT_FOUND)
		{
			requestServiceConnection(peerAgent);
		}
	}

	@Override
	protected void onServiceConnectionResponse(SASocket thisConnection, int result) {
		// TODO Auto-generated method stub
		if (result == CONNECTION_SUCCESS) { 
			 if (thisConnection != null) { 
				 GearNotificationConnection myConnection = (GearNotificationConnection) thisConnection; 
			
			 Log.d(TAG, "onServiceConnection connectionID = " + myConnection.mConnectionId); 
			 try {
				myConnection.send(104, title.getBytes());
				myConnection.send(104, text.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 myConnection.close();
			 //Toast.makeText(getBaseContext(), R.string.ConnectionEstablishedMsg, Toast.LENGTH_LONG).show(); 
			 } else 
			 Log.e(TAG, "SASocket object is null"); 
			 } else if (result == CONNECTION_ALREADY_EXIST) { 
			 Log.e(TAG, "onServiceConnectionResponse, CONNECTION_ALREADY_EXIST"); 
			 } else { 
			 Log.e(TAG, "onServiceConnectionResponse result error =" + result); 
			 } 

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
	
	
	public void sendNotify(String title, String text)
	{
		findPeerAgents();
		this.title="0"+title;
		this.text="1"+text;
		Log.e(TAG, "Notification received: "+text);
		/*new Thread(new Runnable() {
			public void run() {
				try {
					conn.send(104, tmp.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();*/
	}
	class MyBinder extends Binder {
	    ServiceProvider getService() {
	      return ServiceProvider.this;
	    }
	  }

}
