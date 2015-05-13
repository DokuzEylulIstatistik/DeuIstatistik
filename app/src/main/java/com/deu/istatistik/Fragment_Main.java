package com.deu.istatistik;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aka.qwerty.getNetworkSlider;
import com.deu.deuistatistik.gcm.ServerUtilities;
import com.deu.deuistatistik.gcm.WakeLocker;
import com.deu.istatistik.estatXmlParser.Entry;
import com.flurry.android.FlurryAgent;
import com.google.android.gcm.GCMRegistrar;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.deu.deuistatistik.gcm.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.deu.deuistatistik.gcm.CommonUtilities.EXTRA_MESSAGE;
import static com.deu.deuistatistik.gcm.CommonUtilities.SENDER_ID;

public class Fragment_Main extends Fragment implements OnMenuItemClickListener {

	private static String TAG = "Fragment_Main";
	Kutuphane kutuphane = new Kutuphane();
	private static String URL = "";
	// ///////////////////////
	public static final String WIFI = "Wi-Fi";
	public static final String ANY = "Any";
	private static final String estatURL = "http://estat.deu.edu.tr/syndication.php?m=duyuru";
	private static final String sliderURL = "http://www.aykutasil.com/api/DeuIstatistikApi/DeuIstSlider";
	// Whether the display should be refreshed.
	public static boolean refreshDisplay = true;

	// The user's current network preference setting.
	public static String sPref = null;

	// The BroadcastReceiver that tracks network connectivity changes.
	private NetworkReceiver receiver = new NetworkReceiver();

	// /////////////////
	SharedPreferences shr = null;

	// GcmRegistrar AsyncTask
	AsyncTask<Void, Void, Void> asy;

	// Progress Diaolog for download to acilisjSonVeriler
	ProgressDialog prgdialog = null;

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View vi = inflater.inflate(R.layout.fragment_main, container, false);
		rootView = vi;

		kutuphane.startFlurry(getActivity());

		FlurryAgent.logEvent("Fragment_Main");
		//Log.v(TAG, "onCreateView �al��t�");

		String subtitle = getResources().getString(R.string.subtitle_duyurular);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(subtitle);
		actionBar.setTitle("İstatistik");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.show();

		// IntentFilter filter = new IntentFilter(
		// ConnectivityManager.CONNECTIVITY_ACTION);
		// receiver = new NetworkReceiver();
		// getActivity().registerReceiver(receiver, filter);

		if (kutuphane.internetErisimi(getActivity())) {
			GcmKayitIslemleri();
		}

		AcilisListviewgetRow();
		new getNetworkSlider(sliderURL, getActivity(), rootView);

		return vi;

	}

	private void AcilisListviewgetRow() {

		String listviewsatirsayisi = kutuphane.getsharedPreference(
				getActivity(), "listviewsatirsayisi");
		if (entrylistem.size() > 0) {
			entrylistem.clear();
		}

		if (listviewsatirsayisi != null || listviewsatirsayisi == "") {
			ListView listview_Acilis = (ListView) rootView
					.findViewById(R.id.listview_Acilis);

			Entry entry = new Entry();
			for (int i = 0; i < Integer.parseInt(listviewsatirsayisi); i++) {
				String sharedbaslik = "acilisliste_" + entry.getTitle() + "_"
						+ i;
				String sharedyazi = "acilisliste_" + entry.getDescription()
						+ "_" + i;
				String sharedhtmltitle = "acilisliste_" + entry.getHtml_title()
						+ "_" + i;
				String sharedhtmldescription = "acilisliste_"
						+ entry.getHtml_description() + "_" + i;

				String baslik = kutuphane.getsharedPreference(getActivity(),
						sharedbaslik);
				String yazi = kutuphane.getsharedPreference(getActivity(),
						sharedyazi);
				String htmltitle = kutuphane.getsharedPreference(getActivity(),
						sharedhtmltitle);
				String htmldescription = kutuphane.getsharedPreference(
						getActivity(), sharedhtmldescription);

				Entry entry1 = new Entry();

				entry1.setTitle(baslik);
				entry1.setDescription(yazi);
				entry1.setHtml_title(htmltitle);
				entry1.setHtml_description(htmldescription);
				listviewSatirDoldur(entry1);
			}

			AcilisListViewAdapter adapp = new AcilisListViewAdapter(
					getActivity(), entrylistem);
			listview_Acilis.setAdapter(adapp);
		} else {
			kutuphane
					.getAlertDialog(getActivity(), "Uyarı",
							"Son duyuruları almak için lütfen internetiniz açınız ve sayfayı yenileyiniz");
		}
	}

	ArrayList<Entry> entrylistem = new ArrayList<Entry>();

	private void listviewSatirDoldur(Entry entry) {
		entrylistem.add(entry);
	}

	private class fillAcilisListView extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			try {
				entrylistem.clear();
				// Entry ent = new Entry();
				// listviewSatirDoldur(ent);
				return loadXmlFromNetwork(urls[0]);
			} catch (IOException e) {
				// return getResources().getString(R.string.connection_error);
				return e.getMessage();
			} catch (XmlPullParserException e) {
				// return getResources().getString(R.string.xml_error);
				return e.getMessage();
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// setContentView(R.layout.activity_main);
			// getActivity().setContentView(R.layout.fragment_main);

			// Displays the HTML string in the UI via a WebView
			// WebView myWebView = (WebView) findViewById(R.id.webview);
			// myWebView.loadData(result, "text/html; charset=utf-8", null);
			// super.onPostExecute(result);
			int acilislisterownumbers = getResources().getInteger(
					R.integer.AcilisListerownumbers);

			int sayac = 0;
			Entry ent = new Entry();

			for (Entry entry : entrylistem) {
				// listviewSatirDoldur(entry);

				if (sayac < acilislisterownumbers) {
					kutuphane.sharedPreferencesEdit(getActivity(),
							"acilisliste_" + ent.getTitle() + "_" + sayac,
							entry.getTitle());
					kutuphane
							.sharedPreferencesEdit(getActivity(),
									"acilisliste_" + ent.getDescription() + "_"
											+ sayac, entry.getDescription());
					kutuphane.sharedPreferencesEdit(getActivity(),
							"acilisliste_" + ent.getHtml_title() + "_" + sayac,
							entry.getHtml_title());
					kutuphane.sharedPreferencesEdit(getActivity(),
							"acilisliste_" + ent.getHtml_description() + "_"
									+ sayac, entry.getHtml_description());
					kutuphane.sharedPreferencesEdit(getActivity(),
							"listviewsatirsayisi", Integer.toString(sayac + 1));

					sayac++;

				}
			}

			ListView listview = (ListView) rootView
					.findViewById(R.id.listview_Acilis);
			AcilisListViewAdapter adap = new AcilisListViewAdapter(
					getActivity(), entrylistem);
			listview.setAdapter(adap);

			prgdialog.dismiss();

		}

	}

	private class AcilisListViewAdapter extends BaseAdapter implements
			OnClickListener {

		private Activity activity;
		private ArrayList<Entry> data;
		private LayoutInflater inflater = null;
		Entry tempValues = null;
		int i = 0;

		public AcilisListViewAdapter(Activity a, ArrayList<Entry> d) {
			activity = a;
			data = d;

			/*********** Layout inflator to call external xml layout () ***********/
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			if (data.size() <= 0)
				return 1;
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public class ViewHolder {

			public TextView text;
			public TextView text1;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
			ViewHolder holder;

			if (convertView == null) {

				vi = inflater.inflate(R.layout.acilisveriler, null);

				holder = new ViewHolder();
				holder.text = (TextView) vi.findViewById(R.id.acilis_txtBaslik);
				holder.text1 = (TextView) vi.findViewById(R.id.acilis_txtYazi);

				vi.setTag(holder);
			} else
				holder = (ViewHolder) vi.getTag();

			if (data.size() <= 0) {
				holder.text.setText("Duyuru bulunmamaktadır.");

			} else {
				/***** Get each Model object from Arraylist ********/
				tempValues = null;
				tempValues = (Entry) data.get(position);

				/************ Set Model values in Holder elements ***********/

				holder.text.setText(tempValues.getTitle());
				holder.text1.setText(tempValues.getDescription());
				// holder.image.setImageResource(res.getIdentifier(
				// "com.androidexample.customlistview:drawable/"
				// + tempValues.getImage(), null, null));

				/******** Set Item Click Listner for LayoutInflater for each row *******/

				vi.setOnClickListener(new OnItemClickListener(position, vi));
			}
			return vi;
		}

		@Override
		public void onClick(View v) {
			Log.v("CustomAdapter", "=====Row button clicked=====");
		}

		private class OnItemClickListener implements OnClickListener {
			private int mPosition;
			private View mvi;

			OnItemClickListener(int position, View vi) {
				mPosition = position;
				mvi = vi;
			}

			@Override
			public void onClick(View arg0) {

				Animation anim1 = AnimationUtils.loadAnimation(activity,
						R.anim.mytranslateanimation);
				mvi.startAnimation(anim1);

				// Animation anim2 = AnimationUtils.loadAnimation(activity,
				// R.anim.myrotateanimation);
				//
				// holder.text1.startAnimation(anim2);

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						onItemClick(mPosition);
					}
				}, 500);

			}
		}
	}

	public void onItemClick(int mPosition) {
		showEntry(mPosition);
		// Entry tempValues = (Entry) entrylistem.get(mPosition);
		//
		// LayoutInflater inflater = getActivity().getLayoutInflater();
		// View layout = inflater.inflate(R.layout.toastcustomestat,
		// (ViewGroup) rootView.findViewById(R.id.toast_custom_layout));
		//
		// WebView toast_custom_webview = (WebView) layout
		// .findViewById(R.id.toast_custom_webview);
		//
		// String data = tempValues.getHtml_description();
		// String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
		// + "<html><head>"
		// +
		// "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"
		// + "<head><body>";
		//
		// content += data + "</body></html>";
		//
		// toast_custom_webview.loadData(content, "text/html; charset=utf-8",
		// "UTF-8");
		//
		// AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		// getActivity());
		// alertDialogBuilder.setTitle(tempValues.getTitle());
		// alertDialogBuilder
		// .setCancelable(false)
		// .setView(layout)
		// .setPositiveButton("Tamam",
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// // TODO Auto-generated method stub
		// dialog.cancel();
		//
		// }
		// });
		//
		// AlertDialog alertDialog = alertDialogBuilder.create();
		// alertDialog.show();

	}

	private void showEntry(int position) {
		Entry value = (Entry) entrylistem.get(position);

		Bundle args = new Bundle();
		args.putString("baslik", value.getHtml_title());
		args.putString("icerik", value.getHtml_description());
		Fragment toFragment = new Fragment_ShowEntry();
		toFragment.setArguments(args);
		getFragmentManager().beginTransaction()
				.replace(R.id.container, toFragment, null).addToBackStack(null)
				.commit();

	}

	private void GcmKayitIslemleri() {

		GCMRegistrar.checkDevice(getActivity());
		GCMRegistrar.checkManifest(getActivity());

		getActivity().registerReceiver(mHandleMessageReceiver,
				new IntentFilter(DISPLAY_MESSAGE_ACTION));

		final String regId = GCMRegistrar.getRegistrationId(getActivity());

		if (regId.equals("")) {
			GCMRegistrar.register(getActivity(), SENDER_ID);
		} else {
			if (GCMRegistrar.isRegisteredOnServer(getActivity())) {

				Log.i("GcmDurum", "Zaten kayıt edilmiş");

			} else {
				final Context context = getActivity();
				asy = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {

						String packagename = getActivity().getPackageName();
						String versionname = kutuphane
								.getPackageVersionName(getActivity());
						String versioncode = Integer.toString(kutuphane
								.getPackageVersionCode(getActivity()));
						String androidversioncode = Integer.toString(kutuphane
								.getAndroidSdkVersionCode());
						ServerUtilities.register(context, "name", "email",
								regId, packagename, versionname, versioncode,
								androidversioncode);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						asy = null;

					}

				};
				asy.execute(null, null, null);
			}
		}

	}

	private BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getActivity());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			// lblMessage.append(newMessage + "\n");
			// Toast.makeText(getApplicationContext(),
			// "- " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	public void gcmregister_Click(View view) {
		Intent intent = new Intent("com.deu.deuistatistik.gcm.REGISTER");
		startActivity(intent);
	}

	public void clear_Click(View vi) {

		GCMRegistrar.unregister(getActivity());
	}

	public class NetworkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

			if (WIFI.equals(sPref) && networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

				refreshDisplay = true;

			} else if (ANY.equals(sPref) && networkInfo != null) {
				refreshDisplay = true;

			} else {
				refreshDisplay = false;

			}
		}
	}

	// private void updateConnectedFlags() {
	// ConnectivityManager connMgr = (ConnectivityManager) getActivity()
	// .getSystemService(Context.CONNECTIVITY_SERVICE);
	//
	// NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
	// if (activeInfo != null && activeInfo.isConnected()) {
	// wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
	// mobileConnected = activeInfo.getType() ==
	// ConnectivityManager.TYPE_MOBILE;
	// } else {
	// wifiConnected = false;
	// mobileConnected = false;
	// }
	// }

	private void loadPage() {

		if (kutuphane.internetErisimi(getActivity())) {
			new fillAcilisListView().execute(estatURL);
			new getNetworkSlider(sliderURL, getActivity(), rootView);

		} else {
			showErrorPage();
		}
		// if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected))
		// || ((sPref.equals(WIFI)) && (wifiConnected))) {
		// // AsyncTask subclass
		// // new DownloadXmlTask().execute(estatURL);
		// new fillAcilisListView().execute(estatURL);
		// } else {
		// showErrorPage();
		// }
	}

	private void showErrorPage() {

		kutuphane.getAlertDialog(getActivity(), "Hata", "Bağlantı hatası.");

	}

	private String loadXmlFromNetwork(String urlString)
			throws XmlPullParserException, IOException {
		InputStream stream = null;
		estatXmlParser estatxmlparser = new estatXmlParser();
		List<Entry> entries = null;

		try {
			stream = kutuphane.getdownloadUrl(urlString);
			entries = estatxmlparser.parse(stream);

		} finally {
			if (stream != null) {
				stream.close();
			}
		}

		for (Entry ent : entries) {

			// AcilisClass acilisclas = new AcilisClass();
			Entry entry = new Entry();
			entry.setTitle(kutuphane.TagTemizle(ent.getTitle()));
			entry.setDescription(kutuphane.TagTemizle(ent.getDescription()));
			entry.setHtml_title(ent.getTitle());
			entry.setHtml_description(ent.getDescription());
			entrylistem.add(entry);

			// htmlString.append("<p><a href='");
			// htmlString.append(entry.link);
			// htmlString.append("'>" + entry.title + "</a></p>");
			// // If the user set the preference to include summary text,
			// // adds it to the display.
			// // if (pref) {
			// htmlString.append(entry.description);
			// }
		}

		return "TRUE";
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		kutuphane.stopFlurry(getActivity());
		// getActivity().unregisterReceiver(mHandleMessageReceiver);
		// getActivity().unregisterReceiver(receiver);
		//Log.d("onStop", "onStop �al��t�");

	}

	@Override
	public void onDestroy() {

		if (asy != null) {
			asy.cancel(true);
		}
		// gcm.UnRegisterReceiverGcm();
		try {
			getActivity().unregisterReceiver(mHandleMessageReceiver);
			// getActivity().unregisterReceiver(receiver);
			GCMRegistrar.onDestroy(getActivity());

			Log.d("onDestroy",
					"Receiver silindi.GcmRegistrar.onDestroy çalışıtı.");
		} catch (Exception e) {
			Log.e("UnRgstr Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_yenile:

			if (kutuphane.internetErisimi(getActivity())) {
				prgdialog = ProgressDialog.show(getActivity(),
						"Lütfen Bekleyiniz...", "Yükleniyor.", true);
				loadPage();
			} else {
				kutuphane.getAlertDialog(getActivity(), "Uyarı",
						"Güncellemek için Lütfen internetinizi açınız.");
			}
			return true;
			// case R.id.action_btnOyla:
			// // SharedPrfGoster();
			// // loadPage();
			// return true;

		case android.R.id.home:
			// kutuphane.getAlertDialog(this, "Uyar�",
			// "G�ncellemek i�in L�tfen internetinizi a��n�z.");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_yenile:

			if (kutuphane.internetErisimi(getActivity())) {
				prgdialog = ProgressDialog.show(getActivity(),
						"Lütfen Bekleyiniz...", "Yükleniyor.", true);
				loadPage();
			} else {
				kutuphane.getAlertDialog(getActivity(), "Uyarı",
						"Güncellemek için Lütfen internetinizi açınız.");
			}
			return true;
			// case R.id.action_btnOyla:
			// // SharedPrfGoster();
			// // loadPage();
			// return true;

		case android.R.id.home:
			// kutuphane.getAlertDialog(this, "Uyar�",
			// "G�ncellemek i�in L�tfen internetinizi a��n�z.");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
