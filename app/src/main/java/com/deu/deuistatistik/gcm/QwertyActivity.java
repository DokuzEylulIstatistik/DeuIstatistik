package com.deu.deuistatistik.gcm;

import static com.deu.deuistatistik.gcm.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.deu.deuistatistik.gcm.CommonUtilities.EXTRA_MESSAGE;
import static com.deu.deuistatistik.gcm.CommonUtilities.SENDER_ID;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.deu.istatistik.R;
import com.google.android.gcm.GCMRegistrar;

public class QwertyActivity extends Activity {
	// label to display gcm messages
	TextView lblMessage;

	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	// Connection detector
	ConnectionDetector cd;

	public static String name;
	public static String email;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qwertylayout);

		cd = new ConnectionDetector(getApplicationContext());

		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(QwertyActivity.this,
					"�nternet Ba�lant� Hatas�",
					"L�tfen internet ba�lant�n�z� a��n", false);
			return;
		}

		// Getting name, email from intent
		Intent i = getIntent();

		name = i.getStringExtra("name");
		email = i.getStringExtra("email");

		
//		GoogleCloudMessaging goclme = GoogleCloudMessaging.getInstance(this);
//		try {
//			goclme.register(SENDER_ID,"sds");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		lblMessage = (TextView) findViewById(R.id.lblMessage);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id

		final String regId = GCMRegistrar.getRegistrationId(this);
		//ServerUtilities.unregister(this, regId);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.cfc
				
				Log.i("GcmDurum", "Zaten kay�t edilmi�");
				// Toast.makeText(getApplicationContext(),
				// "Already registered with GCM", Toast.LENGTH_LONG)
				// .show();
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						//ServerUtilities.register(context, name, email, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}

	
	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			lblMessage.append(newMessage + "\n");
			// Toast.makeText(getApplicationContext(),
			// "New Message: " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
