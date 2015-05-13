package com.deu.istatistik;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.RemoteViews;

import com.deu.deuistatistik.gcm.ServerUtilities;
import com.google.android.gcm.*;

import static com.deu.deuistatistik.gcm.CommonUtilities.SENDER_ID;
import static com.deu.deuistatistik.gcm.CommonUtilities.displayMessage;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	Kutuphane kutuphane = new Kutuphane();

	public GCMIntentService() {
		super(SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Aygıt Kayıt Edildi: regId = " + registrationId);
		// displayMessage(context, "Your device registred with GCM");
		// Log.d("NAME", com.deu.deuistatistik.gcm.QwertyActivity.name);
		String packagename = getPackageName();
		String versionname = kutuphane
				.getPackageVersionName(getApplicationContext());
		String versioncode = Integer.toString(kutuphane
				.getPackageVersionCode(getApplicationContext()));
		String androidversioncode = Integer.toString(kutuphane
				.getAndroidSdkVersionCode());
		ServerUtilities.register(context, "name", "email", registrationId,
				packagename, versionname, versioncode, androidversioncode);
	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Aygıtın Kaydı Silindi");
		// displayMessage(context, getString(R.string.gcm_unregistered));
		ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Mesaj Alındı");
		String message = intent.getExtras().getString("message");

		displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Alınan Mesaj Bildirimleri Silindi.");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received Hata: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();

		RemoteViews contentView = new RemoteViews(context.getPackageName(),
				R.layout.custum_notification);
		contentView.setImageViewResource(R.id.image, R.drawable.ic_launcher);
		contentView.setTextViewText(R.id.title,
				context.getString(R.string.notification_mainTitle));
		contentView.setTextViewText(R.id.text, message);
		contentView.setTextViewText(R.id.text1, "Aykut Asil");
	

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);

		Intent notificationIntent = new Intent(context, MainActivity.class);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		// notification.setLatestEventInfo(context, title, message, intent);

		// Notification noti = new Notification.BigTextStyle(
		// new Notification.Builder(context).setContentText("sds")).build();

		NotificationCompat.Builder bb = new Builder(context);
		bb.setContentTitle(title).setContentText(message)
				.setContentIntent(intent).setSmallIcon(icon)
				.setDefaults(Notification.DEFAULT_ALL)
				.setSubText("Dokuz Eylül Üniversitesi").setTicker("DEU İstatistik ")
				.setContent(contentView);

		// notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		// notification.defaults |= Notification.DEFAULT_SOUND;

		// notification.sound = Uri.parse("android.resource://" +
		// context.getPackageName() + "your_sound_file_name.mp3");

		// Vibrate if vibrate is enabled
		// notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, bb.build());

	}
}
