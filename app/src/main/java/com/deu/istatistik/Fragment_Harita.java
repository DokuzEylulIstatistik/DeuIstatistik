package com.deu.istatistik;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;



public class Fragment_Harita extends Fragment implements LocationListener,
		OnMarkerClickListener {

	private static String TAG = "HARITA_FRAGMENT";
	View rootView;
	private FragmentActivity fa;
	private LocationManager locman;
	private GoogleMap googleHarita;
	private String provider;
	private double _latitude;
	private double _longitude;

	Kutuphane kutuphane = new Kutuphane();
	ArrayList<Konumlar> listkonum = new ArrayList<Konumlar>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.v(TAG, "onCreate View çalıştı");

		View vi = inflater.inflate(R.layout.harita, container, false);
		rootView = vi;
		fa = super.getActivity();

		String actionbarsunTitle = getResources().getString(
				R.string.actionbarsubtitle);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(actionbarsunTitle);
		actionBar.setTitle("Konum");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.show();

		kutuphane.startFlurry(getActivity());

		getKonumParcala();

		harita_spinnerYerListesiDoldur();

		LocationManager service = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = service
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!enabled) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}

		locman = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		criteria.setCostAllowed(true);

		provider = locman.getBestProvider(criteria, false);

		Location location = locman.getLastKnownLocation(provider);

		if (location != null) {
			onLocationChanged(location);
		} else {
			List<String> providerNames = locman.getAllProviders();
			for (String backupProvider : providerNames) {
				if (locman.isProviderEnabled(backupProvider))
					provider = backupProvider;
			}

			locman.requestLocationUpdates(provider, 3000, 50, this);
		}

		initHarita();
		// HaritayaKonumEkle();

		return vi;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);

		getActivity().getMenuInflater().inflate(R.menu.desc_stat_menu, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:

			// Intent intent = new Intent(this, Acilis.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent);

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		locman.requestLocationUpdates(provider, 3000, 50, this);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locman.removeUpdates(this);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		googleHarita.stopAnimation();

		kutuphane.stopFlurry(getActivity());
	}

	private void harita_spinnerYerListesiDoldur() {

		ArrayList<String> yerisimleri = new ArrayList<String>();

		yerisimleri.add(getResources().getString(R.string.haritaSpinnerFirst));

		for (int a = 0; a < listkonum.size(); a++) {

			yerisimleri.add(listkonum.get(a).getKonumadi());

		}

		Spinner harita_spinnerYerListesi = (Spinner) rootView
				.findViewById(R.id.harita_spinnerYerListesi);
		harita_spinnerYerListesi
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						int secilenindis = parent.getSelectedItemPosition();

						if (secilenindis == 0) {
							googleHarita.clear();
							HaritayaKonumEkle();
						} else {

							googleHarita.clear();
							HaritayaKonumEkle(secilenindis - 1);
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						googleHarita.clear();
						HaritayaKonumEkle();

					}
				});
		ArrayAdapter<String> adap = new ArrayAdapter<String>(getActivity(),
				R.layout.spinnernotharf, yerisimleri);
		harita_spinnerYerListesi.setAdapter(adap);

	}

	private void initHarita() {
		// if (googleHarita == null) {
		googleHarita = ((SupportMapFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(
						R.id.haritafragment)).getMap();
		// }
		googleHarita.setOnMarkerClickListener(this);

		googleHarita.setMyLocationEnabled(true);
	}

	
	private void addMarkertoMap(LatLng latlng, BitmapDescriptor icon,
			String title) {

		try {
			LatLng Koordinat = latlng;
			googleHarita.addMarker(new MarkerOptions().position(Koordinat)
					.title(title).snippet("Nabbbeer"));
		} catch (Exception e) {
			kutuphane.getAlertDialog(getActivity(), "hata",
					"Konum Ekleme Hatası!");
		}
	}

	private void HaritayaKonumEkle(int Spinnerindis) {
		String[] resimler = getResources().getStringArray(
				R.array.konumlar_resimler);

		String[] pathresimler = new String[resimler.length];

		for (int i = 0; i < resimler.length; i++) {
			pathresimler[i] = "HaritaMarkerImage/" + resimler[i];

		}

		try {
			Konumlar konumum = listkonum.get(Spinnerindis);

			LatLng latlng = new LatLng(Double.parseDouble(konumum
					.getKonum_latitude()), Double.parseDouble(konumum
					.getKonum_longitude()));

			BitmapDescriptor icon = BitmapDescriptorFactory
					.fromAsset(pathresimler[Spinnerindis]);
			String title = konumum.getKonumadi();

			// googleHarita.addMarker(new MarkerOptions().position(latlng)
			// .icon(icon).title(title));
			addMarkertoMap(latlng, icon, title);

		} catch (Exception e) {
		}

	}

	private void HaritayaKonumEkle() {

		String[] resimler = getResources().getStringArray(
				R.array.konumlar_resimler);

		String[] pathresimler = new String[resimler.length];

		for (int i = 0; i < resimler.length; i++) {
			pathresimler[i] = "HaritaMarkerImage/" + resimler[i];

		}

		for (int a = 0; a < listkonum.size(); a++) {
			try {
				Konumlar konumum = listkonum.get(a);

				LatLng latlng = new LatLng(Double.parseDouble(konumum
						.getKonum_latitude()), Double.parseDouble(konumum
						.getKonum_longitude()));

				BitmapDescriptor icon = BitmapDescriptorFactory
						.fromAsset(pathresimler[a]);
				String title = konumum.getKonumadi();

				// googleHarita.addMarker(new MarkerOptions().position(latlng)
				// .icon(icon).title(title));
				addMarkertoMap(latlng, icon, title);

			} catch (Exception e) {
				kutuphane.getAlertDialog(getActivity(), "Hata",
						"Konum Ekleme Hatası !");

			}
		}

	}

	public void btn_Nerdeyim_Click(View vi) {

		LatLng Koordinat = new LatLng(_latitude, _longitude);

		googleHarita.moveCamera(CameraUpdateFactory
				.newLatLngZoom(Koordinat, 16));

		CameraPosition cameraPosition = CameraPosition.builder()
				.target(Koordinat).zoom(13).bearing(30).build();
		googleHarita.animateCamera(
				CameraUpdateFactory.newCameraPosition(cameraPosition), 2000,
				null);
		getKonumParcala();
		HaritayaKonumEkle();

	}

	private class Konumlar {
		private String konumadi;
		private String konum_latitude;
		private String konum_longitude;

		public Konumlar(String konumadi, String konumlat, String konumlong) {
			this.konumadi = konumadi;
			this.konum_latitude = konumlat;
			this.konum_longitude = konumlong;
		}

		public String getKonumadi() {
			return konumadi;
		}

		public void setKonumadi(String konumadi) {
			this.konumadi = konumadi;
		}

		public String getKonum_latitude() {
			return konum_latitude;
		}

		public void setKonum_latitude(String konum_latitude) {
			this.konum_latitude = konum_latitude;
		}

		public String getKonum_longitude() {
			return konum_longitude;
		}

		public void setKonum_longitude(String konum_longitude) {
			this.konum_longitude = konum_longitude;
		}

	}

	private void getKonumParcala() {
		String konumlar[] = getResources().getStringArray(R.array.konumlar);

		Konumlar knm;
		String yer_isim;
		String latitude;
		String longitude;
		for (int a = 0; a < konumlar.length; a++) {
			int yerisimkonum = konumlar[a].indexOf(",");
			int arakonum = konumlar[a].indexOf("+");

			yer_isim = konumlar[a].substring(0, yerisimkonum);
			latitude = konumlar[a].substring(yerisimkonum + 1, arakonum);
			longitude = konumlar[a].substring(arakonum + 1);

			knm = new Konumlar(yer_isim, latitude, longitude);
			listkonum.add(knm);

		}

	}

	private void AlertDialogShow(String title, String message,
			final String position) {
		AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
		build.setTitle(title);
		build.setMessage(message);

		build.setPositiveButton("Evet", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				startNavi(position);
			}
		});
		build.setNegativeButton("Hayır", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		build.create().show();

	}

	private void startNavi(String position) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("google.navigation:q=" + position));
		startActivity(intent);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		String baslik = marker.getTitle();
		LatLng latlng = marker.getPosition();
		String position = Double.toString(latlng.latitude) + ","
				+ Double.toString(latlng.longitude);
		AlertDialogShow(baslik, "Yol tarifi ister misiniz ?", position);

		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		_latitude = latitude;
		_longitude = longitude;

		// latitude = 38.384634;
		// longitude = 27.180361;

		LatLng ltlng = new LatLng(latitude, longitude);

		// HaritaGoster(ltlng);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

}
