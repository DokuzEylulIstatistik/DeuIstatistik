package com.deu.istatistik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.flurry.android.FlurryAgent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Kutuphane {

	String flurryID = "K4DRB4T2CM8DY746HB5Q";

	public void getAlertDialog(Context context, String baslik, String mesaj) {
		// AlertDialog.Builder ile AlertDialog nesnesini d�zenle
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setTitle(baslik);
		alertDialogBuilder
				.setMessage(mesaj)
				.setCancelable(false)
				.setPositiveButton("Tamam",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();

							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public ArrayList<String> getDosya(Context context, String dosyaadi) {
		ArrayList<String> dizi = new ArrayList<String>();

		try {
			InputStream dosya = context.getAssets().open(dosyaadi);

			// InputStream dss =
			// context.getResources().openRawResource(R.raw.qwerty);

			BufferedReader qq = new BufferedReader(new InputStreamReader(dosya));
			String receivestring = qq.readLine();

			while (receivestring != null) {

				dizi.add(receivestring);

				receivestring = qq.readLine();
			}

			qq.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return dizi;

	}

	public String TemizString(String deger) {
		String yenideger = deger.trim().replace(".", "").replace(" ", "")
				.replace("�", "o").replace("�", "u").replace(",", "")
				.replace("�", "i");
		return "_" + yenideger;
	}

	// android.permission.ACCESS_NETWORK_STATE iznini almay� unutma
	public boolean internetErisimi(Context context) {

		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public String TagTemizle(String deger) {

		String sonuc = deger.trim();
		sonuc = Html.fromHtml(sonuc).toString();
		while (sonuc.contains("<")) {

			StringBuilder strbuild = new StringBuilder(sonuc);

			int tagBaslangicIndis = strbuild.indexOf("<");
			int tagBitisIndis = strbuild.indexOf(">", tagBaslangicIndis);

			strbuild.delete(tagBaslangicIndis, tagBitisIndis + 1);

			sonuc = strbuild.toString();

		}
		return sonuc;
	}

	public void startFlurry(Context context) {
		FlurryAgent.init(context,flurryID);
		FlurryAgent.onStartSession(context, flurryID);

	}

	public void stopFlurry(Context context) {
		FlurryAgent.onEndSession(context);

	}

	public void sharedPreferencesEdit(Context context, String key, String value) {

		SharedPreferences shr = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = shr.edit();
		editor.putString(key, value);
		editor.commit();

	}

	public void sharedPreferencesEdit(Context context, String key, int value) {

		SharedPreferences shr = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = shr.edit();
		editor.putInt(key, value);
		editor.commit();

	}

	public String getsharedPreference(Context context, String key) {
		SharedPreferences shr = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);

		return shr.getString(key, null);

	}

	public int getsharedPreferenceInt(Context context, String key) {
		SharedPreferences shr = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);

		return shr.getInt(key, 0);

	}

	public String getPackageVersionName(Context context) {
		try {
			String versionname = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName.toString();
			return versionname;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public int getPackageVersionCode(Context context) {
		try {
			int versioncode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
			return versioncode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	public int getAndroidSdkVersionCode() {
		int androidverisonsdkint = android.os.Build.VERSION.SDK_INT;
		return androidverisonsdkint;
	}

	public String getAndroidSdkVersionName() {
		String androidverisonsdkint = android.os.Build.VERSION.CODENAME;
		return androidverisonsdkint;
	}

	public static void hideKeyboard(Activity activity) {
		InputMethodManager inputManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (activity.getCurrentFocus() != null) {
			inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target)
				&& android.util.Patterns.EMAIL_ADDRESS.matcher(target)
						.matches();
	}

	public static String changeCharset(String veri) {
		String name = "";
		try {
			name = new String(veri.getBytes("iso-8859-9"), "iso-8859-9");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		String decodedName = Html.fromHtml(name).toString();
		return decodedName;
	}

	public InputStream getdownloadUrl(String urlString) throws IOException {

		InputStream stream = null;
		try {

			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpPost = new HttpGet(urlString);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String resultsStringg = EntityUtils.toString(entity, "ISO-8859-9");
			stream = new ByteArrayInputStream(
					resultsStringg.getBytes("ISO-8859-9"));
		} catch (Exception e) {
			Log.e("Kutuphane", e.getMessage());

		}
		return stream;
	}

	public InputStream getdownloadUrl_DefaultConnection(String urlString)
			throws IOException {

		InputStream stream = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();

			stream = conn.getInputStream();
			// conn.disconnect();

		} catch (Exception e) {
			Log.e("Kutuphane", e.getMessage());

		}
		return stream;
	}

	public String getStringtoInputStream(InputStream is) {
		String sonuc = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-9"), 8); // iso-8859-1

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			sonuc = sb.toString();
		} catch (Exception e) {
			Log.i("Buffer Error", "Error converting result " + e.toString());
		}
		return sonuc;

	}

	public JSONObject getJsonObjecttoString(String json) {
		JSONObject jObj = null;
		try {
			if (json != null) {
				jObj = new JSONObject(json);
			} else {
				jObj = null;
			}

		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data "
					+ e.getMessage().toString());
		}
		return jObj;
	}

	public String getPathSqliteDatabase(String db_name, Activity activity) {
		String dbPath = activity.getFilesDir().getParent();
		String dbPath1 = dbPath + "/databases/" + db_name;

		return dbPath1;
	}











}
