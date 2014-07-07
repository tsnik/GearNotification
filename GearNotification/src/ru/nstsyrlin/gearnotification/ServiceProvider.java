package ru.nstsyrlin.gearnotification;

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
	
	HashMap<Integer, GearNotificationConnection> mConnectionsMap = null;
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

			if (mConnectionsMap != null) {
				mConnectionsMap.remove(mConnectionId);
			}
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
	protected void onFindPeerAgentResponse(SAPeerAgent arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onServiceConnectionResponse(SASocket thisConnection, int result) {
		// TODO Auto-generated method stub
		if (result == CONNECTION_SUCCESS) { 
			 if (thisConnection != null) { 
				 GearNotificationConnection myConnection = (GearNotificationConnection) thisConnection; 
			 if (mConnectionsMap == null) { 
			 mConnectionsMap = new HashMap<Integer, GearNotificationConnection>(); 
			 } 
			 myConnection.mConnectionId = (int) (System.currentTimeMillis() & 255); 
			 Log.d(TAG, "onServiceConnection connectionID = " + myConnection.mConnectionId); 
			 mConnectionsMap.put(myConnection.mConnectionId, myConnection); 
			 
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
	
	public void sendNotify(String text)
	{
		Log.e(TAG, "Notification received: "+text);
	}
	class MyBinder extends Binder {
	    ServiceProvider getService() {
	      return ServiceProvider.this;
	    }
	  }

}
