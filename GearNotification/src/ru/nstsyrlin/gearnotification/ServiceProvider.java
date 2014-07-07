package ru.nstsyrlin.gearnotification;

import android.content.Intent;
import android.os.IBinder;

import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

public class ServiceProvider extends SAAgent {

	protected ServiceProvider(String arg0, Class<? extends SASocket> arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFindPeerAgentResponse(SAPeerAgent arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onServiceConnectionResponse(SASocket arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
