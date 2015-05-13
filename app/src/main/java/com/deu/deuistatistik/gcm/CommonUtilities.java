package com.deu.deuistatistik.gcm;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	
	// give your server registration url here
    public static final String SERVER_URL = "http://aykutasil.com/Gcm/Register"; 
    static final String SERVER_UNREGISTER_URL = "http://aykutasil.com/Gcm/UnRegister";

    // Google project id
    public static final String SENDER_ID = "491174981564"; 

    /**
     * Tag used on log messages.
     */
    public static final String TAG = "DEU Istatistik";

    public static final String DISPLAY_MESSAGE_ACTION =
            "com.deu.deuistatistik.gcm.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
