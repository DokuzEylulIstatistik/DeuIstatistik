package com.deu.istatistik;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aka.qwerty.dbSqLite;
import com.aka.qwerty.obj_IstatistikPaylasim;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Fragment_IstatistikPaylasim extends Fragment implements
		OnClickListener {

	private static final String TAG_TO_FRAGMENT = "TAG_TO_FRAGMENT";
	private LinearLayout viewroot;
	private Activity activity;
	private String istatistikPaylasimURL = "http://www.aykutasil.com/api/DeuIstatistikApi/DeuIstList";
	dbSqLite db;
	Kutuphane kutuphane = new Kutuphane();

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		viewroot = (LinearLayout) inflater.inflate(
				R.layout.fragment_istatistikpaylasim, container, false);

		activity = getActivity();
		setHasOptionsMenu(true);

		// ////////////////
		// ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(
		// activity).build();
		// ImageLoader.getInstance().init(conf);
		// ///////////
		String actionbarSubTitle = getResources().getString(
				R.string.subtitle_istatistikpaylasim);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(actionbarSubTitle);
		actionBar.setTitle(actionbarSubTitle);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.show();
		// //////
		setHasOptionsMenu(true);

		// /////
		db = new dbSqLite(activity);
		if (db.getIstatistikList().size() <= 0) {
			new getIstatistikPaylasim().execute(istatistikPaylasimURL);

		} else {
			// setIstatistikList();
			getIstatistikList();
			showIstatistikList();
		}
		return viewroot;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub

		inflater.inflate(R.menu.fragment_istatistikpaylasim_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	List<obj_IstatistikPaylasim> listaykutasilIstatistik = null;

	private class getIstatistikPaylasim extends
			AsyncTask<String, Integer, String> {

		ProgressDialog dialog = null;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(activity, "DEU İstatistik",
					"Yükleniyor...", true, true);

		}

		@Override
		protected String doInBackground(String... params) {

			String json = "";
			try {
				InputStream stream = kutuphane.getdownloadUrl(params[0]);
				json = kutuphane.getStringtoInputStream(stream);
				Log.i("Veri indirildi", stream.toString());

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return json;
		}

		@Override
		protected void onPostExecute(String result) {

			String json = result;
			Log.i("Fragment_IstPylsm", json);
			JSONObject obje = kutuphane.getJsonObjecttoString(json);

			if (obje != null) {
				setIstatistikList(obje);
				getIstatistikList();
				showIstatistikList();

				Log.i("Veri çekildi", istatistikPaylasimURL);
			}
			dialog.dismiss();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			// seekbar.setProgress(values[0]);
		}

		// private Bitmap downloadBitmap(String url) {
		// // initilize the default HTTP client object
		// final DefaultHttpClient client = new DefaultHttpClient();
		//
		// //forming a HttoGet request
		// final HttpGet getRequest = new HttpGet(url);
		// try {
		//
		// HttpResponse response = client.execute(getRequest);
		//
		// //check 200 OK for success
		// final int statusCode = response.getStatusLine().getStatusCode();
		//
		// if (statusCode != HttpStatus.SC_OK) {
		// Log.w("ImageDownloader", "Error " + statusCode +
		// " while retrieving bitmap from " + url);
		// return null;
		//
		// }
		//
		// final HttpEntity entity = response.getEntity();
		// if (entity != null) {
		// InputStream inputStream = null;
		// try {
		// // getting contents from the stream
		// inputStream = entity.getContent();
		//
		// // decoding stream data back into image Bitmap that android
		// understands
		// final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
		//
		// return bitmap;
		// } finally {
		// if (inputStream != null) {
		// inputStream.close();
		// }
		// entity.consumeContent();
		// }
		// }
		// } catch (Exception e) {
		// // You Could provide a more explicit error message for IOException
		// getRequest.abort();
		// Log.e("ImageDownloader", "Something went wrong while" +
		// " retrieving bitmap from " + url + e.toString());
		// }
		//
		// return null;
		// }

	}

	private void setIstatistikList(JSONObject obje) {
		try {
			JSONArray veriler = obje.getJSONArray("Data");
			for (int i = 0; i < veriler.length(); i++) {
				obj_IstatistikPaylasim deuist = new obj_IstatistikPaylasim();
				JSONObject veri = veriler.getJSONObject(i);

				deuist.setIst_ID(veri.getInt("ist_ID"));
				deuist.setIst_konu(Kutuphane.changeCharset(veri
						.getString("ist_konu")));
				deuist.setIst_okunmasayisi(veri.getInt("ist_okunmasayisi"));
				deuist.setIst_tarih(veri.getString("ist_tarih"));
				deuist.setIst_tag(veri.getString("ist_tag"));
				deuist.setIst_yazi(veri.getString("ist_yazi"));

				// listaykutasilIstatistik.add(deuist);

				db.insertIstatistik(deuist);

				Log.i("Json Array Liste", String.valueOf(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getIstatistikList() {

		listaykutasilIstatistik = null;
		listaykutasilIstatistik = new ArrayList<obj_IstatistikPaylasim>();
		listaykutasilIstatistik = db.getIstatistikList();
	}

	private void showIstatistikList() {
		istatistikPaylasimAdaptor adap = new istatistikPaylasimAdaptor(
				activity, listaykutasilIstatistik);
		ListView aykutasilComListView = (ListView) viewroot
				.findViewById(R.id.IstatistikPaylasimListView);
		aykutasilComListView.setAdapter(adap);
	}

	private class istatistikPaylasimAdaptor extends BaseAdapter {
		// private Context context;
		private List<obj_IstatistikPaylasim> list;
		private LayoutInflater inflater;

		public istatistikPaylasimAdaptor(Context context,
				List<obj_IstatistikPaylasim> list) {
			// this.context = context;
			this.list = list;

			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		private class MyViewHolder {
			public TextView title;
			public TextView content;
			public ImageView image;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {

			View view = arg1;
			MyViewHolder viewholder;

			if (arg1 == null) {
				view = inflater.inflate(R.layout.fragment_istatikpaylasim_row,
						null);

				viewholder = new MyViewHolder();
				viewholder.title = (TextView) view
						.findViewById(R.id.fragmentEntryUst);
				viewholder.content = (TextView) view
						.findViewById(R.id.fragmentEntryAlt);
				viewholder.image = (ImageView) view
						.findViewById(R.id.fragmanetEntryImage);

				view.setTag(viewholder);

			} else {
				viewholder = (MyViewHolder) view.getTag();
			}

			if (list.size() <= 0) {
				viewholder.title.setText("Veri Yok.");
			} else {
				obj_IstatistikPaylasim aa = list.get(arg0);
				viewholder.title.setText(aa.getIst_konu());
				viewholder.content.setText("DEU İstatistik");

				// String url =
				// "http://www.aykutasil.com/Content/images/ResimYukle/30122013AcomA4a099e7c-c597-4132-8747-6a0e0c4a668fAcomAUntitled-262x300.png";

				// ImageLoader.getInstance().displayImage(url,
				// viewholder.image);

				// new getUrlImage(viewholder.image, url);
			}

			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					int a = (int) event.getX();
					// int b = (int) event.getHistoricalX(1);

					Log.i("a", String.valueOf(a));
					// Log.i("b",String.valueOf(b));
					return false;
				}
			});

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					Log.i("view.setOnClickListener(new OnClickListener()",
//							"...");

					Animation anim = AnimationUtils.loadAnimation(activity,
							R.anim.mytranslateanimation);

					v.startAnimation(anim);
					// /////////
					TextView altyazi = (TextView) v
							.findViewById(R.id.fragmentEntryAlt);
					Animation anim_altyazi = AnimationUtils.loadAnimation(
							activity, R.anim.myrotateanimation);
					altyazi.startAnimation(anim_altyazi);
					// ////////

					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							aykutasilOnclickListener(arg0, list.get(arg0)
									.getIst_yazi(), list.get(arg0)
									.getIst_konu());

						}
					}, 1000);

				}
			});
			return view;
		}
	}

	private void aykutasilOnclickListener(int position, String icerik,
			String title) {
		//Log.i("aykutasilOnclickListener T�kland�", String.valueOf(position));

		showEntry(icerik, title);

	}

	private void showEntry(String icerik, String title) {
		Bundle args = new Bundle();
		args.putString("name", "aykut");
		args.putString("icerik", icerik);
		args.putString("title", title);
		Fragment toFragment = new Fragment_IstatistikPaylasim_Show();
		toFragment.setArguments(args);
		getFragmentManager().beginTransaction()
				.replace(R.id.container, toFragment, TAG_TO_FRAGMENT)
				.addToBackStack(TAG_TO_FRAGMENT).commit();
	}

	@Override
	public void onClick(View v) {
		Log.i("onClick", "...");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.yenile:

			if (kutuphane.internetErisimi(getActivity())) {
				String path = kutuphane.getPathSqliteDatabase("db_depo",
						activity);
				SQLiteDatabase sqlite = SQLiteDatabase.openOrCreateDatabase(
						path, null);
				db.ReloadTables(sqlite);

				new getIstatistikPaylasim().execute(istatistikPaylasimURL);
			} else {
				kutuphane.getAlertDialog(getActivity(), "Uyarı",
						"Güncellemek için Lütfen internetinizi açınız.");
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
