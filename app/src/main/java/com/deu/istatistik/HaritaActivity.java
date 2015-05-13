package com.deu.istatistik;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HaritaActivity extends FragmentActivity implements
		LocationListener, OnMarkerClickListener, OnInfoWindowClickListener {

	private LocationManager locman;
	private GoogleMap googleHarita;
	private String provider;
	private double _latitude;
	private double _longitude;

	Kutuphane kutuphane = new Kutuphane();
	ArrayList<Konumlar> listkonum = new ArrayList<Konumlar>();

	// private ActionBar getActionBar() {
	// return ((ActionBarActivity) getActivity()).getSupportActionBar();
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.harita);

		// String actionbarsunTitle = getResources().getString(
		// R.string.actionbarsubtitle);
		//
		// android.app.ActionBar actionBar = getActionBar();
		// actionBar.setSubtitle(actionbarsunTitle);
		// actionBar.setTitle("Konum");
		// actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setDisplayShowHomeEnabled(true);
		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		// actionBar.show();

		kutuphane.startFlurry(this);

		getKonumParcala();

		harita_spinnerYerListesiDoldur();

		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = service
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!enabled) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}

		locman = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
	}

	private void harita_spinnerYerListesiDoldur() {

		ArrayList<String> yerisimleri = new ArrayList<String>();

		yerisimleri.add(getResources().getString(R.string.haritaSpinnerFirst));

		for (int a = 0; a < listkonum.size(); a++) {

			yerisimleri.add(listkonum.get(a).getKonumadi());

		}

		Spinner harita_spinnerYerListesi = (Spinner) findViewById(R.id.harita_spinnerYerListesi);
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
		ArrayAdapter<String> adap = new ArrayAdapter<String>(this,
				R.layout.spinnernotharf, yerisimleri);
		harita_spinnerYerListesi.setAdapter(adap);

	}

	private void initHarita() {
		googleHarita = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.haritafragment)).getMap();

		if (googleHarita != null) {
			googleHarita.setOnMarkerClickListener(this);
			googleHarita.setMyLocationEnabled(true);

			InfoWindowAdapter adap = new InfoWindowAdapter() {

				@Override
				public View getInfoWindow(Marker arg0) {
					return null;
				}

				@Override
				public View getInfoContents(Marker marker) {
					View myContentView = getLayoutInflater().inflate(
							R.layout.custommarker, null);
					TextView tvTitle = ((TextView) myContentView
							.findViewById(R.id.title));
					tvTitle.setText(marker.getTitle());

					ImageView image = (ImageView) myContentView
							.findViewById(R.id.harita_konum_image);

					InputStream imageUrl = null;
					try {
						imageUrl = getResources().getAssets().open(
								marker.getSnippet());
					} catch (IOException e) {
						e.printStackTrace();
					}

					image.setImageBitmap(BitmapFactory.decodeStream(imageUrl));

					// Button btn_harita_burayagit = (Button) myContentView
					// .findViewById(R.id.btn_harita_burayagit);
					// btn_harita_burayagit
					// .setOnClickListener(new View.OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// // startNavi(marker.get)
					// kutuphane.getAlertDialog(
					// getApplicationContext(), "baslik",
					// "mesaj");
					// }
					// });
					return myContentView;
				}
			};

			googleHarita.setInfoWindowAdapter(adap);
			googleHarita.setOnInfoWindowClickListener(this);
		}
	}

	private void HaritadaGoster(LatLng latlng) {
		//
		// googleHarita = ((SupportMapFragment) getSupportFragmentManager()
		// .findFragmentById(R.id.haritafragment)).getMap();
		// googleHarita.setOnMarkerClickListener(this);

		if (googleHarita != null) {
			LatLng Koordinat = latlng;

			googleHarita.clear();

			// googleHarita.setMyLocationEnabled(true);
			// googleHarita.addMarker(new MarkerOptions().position(Koordinat)
			// .title("Burdas�n"));
			// googleHarita.addMarker(new MarkerOptions()
			// .icon(BitmapDescriptorFactory
			// .fromResource(android.R.drawable.ic_input_add))
			// .title("Burdas�n").position(Koordinat));

			// getKonumParcala();
			HaritayaKonumEkle();
			// googleHarita.moveCamera(CameraUpdateFactory.newLatLngZoom(
			// Koordinat, 16));

			// googleHarita.addMarker(new MarkerOptions()
			// .icon(BitmapDescriptorFactory
			// .fromResource(R.drawable.ic_launcher))
			// .position(new LatLng(35, 23)).flat(true).rotation(245));

			// CameraPosition cameraPosition = CameraPosition.builder()
			// .target(Koordinat).zoom(13).bearing(90).build();

			// Animate the change in camera view over 2 seconds
			// googleHarita.animateCamera(
			// CameraUpdateFactory.newCameraPosition(cameraPosition),
			// 2000, null);

		}

	}

	private void addMarkertoMap(LatLng latlng, BitmapDescriptor icon,
			String title, String iconAssetName) {

		try {
			LatLng Koordinat = latlng;
			googleHarita.addMarker(new MarkerOptions()
					.position(Koordinat)
					.title(title)
					.snippet(iconAssetName)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

		} catch (Exception e) {
			kutuphane.getAlertDialog(this, "hata", "Konum Ekleme Hatası!");
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

			addMarkertoMap(latlng, icon, title, pathresimler[Spinnerindis]);

			googleHarita.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,
					16));

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
				addMarkertoMap(latlng, icon, title, pathresimler[a]);

			} catch (Exception e) {
				kutuphane.getAlertDialog(this, "Hata", "Konum Ekleme Hatası !");

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menuharita, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

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
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.harita_menuitem_back:

			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		locman.requestLocationUpdates(provider, 3000, 50, this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locman.removeUpdates(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		kutuphane.stopFlurry(this);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub

		// String baslik = marker.getTitle();
		// LatLng latlng = marker.getPosition();
		// String position = Double.toString(latlng.latitude) + ","
		// + Double.toString(latlng.longitude);
		// AlertDialogShow(baslik, "Yol tarifi ister misiniz ?", position);

		return false;
	}

	private void AlertDialogShow(String title, String message,
			final String position) {
		AlertDialog.Builder build = new AlertDialog.Builder(this);
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
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		LatLng latlng = marker.getPosition();
		String position = Double.toString(latlng.latitude) + ","
				+ Double.toString(latlng.longitude);

		startNavi(position);
		// kutuphane.getAlertDialog(this, "baslik", "mesaj");

	}

}