package com.deu.istatistik;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class GCMBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// Explicitly specify that GcmIntentService will handle the intent.
		ComponentName comp = new ComponentName(context.getPackageName(),
				GCMIntentService.class.getName());
		// Start the service, keeping the device awake while it is launching.
		context.startService(intent.setComponent(comp));
		setResultCode(Activity.RESULT_OK);
	}

}
