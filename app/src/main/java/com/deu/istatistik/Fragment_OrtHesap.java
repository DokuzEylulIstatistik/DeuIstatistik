package com.deu.istatistik;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Fragment_OrtHesap extends Fragment implements OnClickListener,
		OnFocusChangeListener {

	public Fragment_OrtHesap() {
	}

	public static Fragment_OrtHesap newInstance() {

		Fragment_OrtHesap fragment = new Fragment_OrtHesap();
		return fragment;
	}

	Spinner sinifsecim;
	Kutuphane kutuphane = new Kutuphane();

	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootview = inflater.inflate(R.layout.orthes, container, false);
		rootView = rootview;
		String actionbarSubTitle = getResources().getString(
				R.string.subtitle_orthes);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(actionbarSubTitle);
		actionBar.setTitle("Not Hesaplama");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.show();

		TabHost tabhost = (TabHost) rootview.findViewById(R.id.tabhost);
		tabhost.setup();
		TabHost.TabSpec tag1, tag2;

		tag1 = tabhost.newTabSpec("tag1");
		tag1.setContent(R.id.istatistik);
		tag1.setIndicator("İstatistik");
		tabhost.addTab(tag1);

		tag2 = tabhost.newTabSpec("tag2");
		tag2.setContent(R.id.diger);
		tag2.setIndicator("Diğer");
		tabhost.addTab(tag2);

		tabhost.setCurrentTab(0);
		// ////
		getDersListesi1();
		getSatirDoldurDiger();
		// ////
		Button btn_hesapla = (Button) rootview.findViewById(R.id.btn_hesapla);
		btn_hesapla.setOnClickListener(this);

		Button btn_hesapla_diger = (Button) rootview
				.findViewById(R.id.btn_hesapla_diger);
		btn_hesapla_diger.setOnClickListener(this);
		// ////
		setHasOptionsMenu(true);
		return rootview;

		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);

	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	public void MyListView(int sinif) {

		MyListViewTemizle();

		LinearLayout[] llx;
		TextView[] tx;
		TextView[] tx1;
		Spinner[] spn;

		List<Dersler> dersler = new ArrayList<Dersler>();

		String dersadlari[] = getResources().getStringArray(sinif);

		for (int a = 0; a < dersadlari.length; a++) {
			String drs = dersadlari[a];
			int virgulindex = drs.indexOf(",");
			String dersinadi = drs.substring(0, virgulindex);
			String dersinkredisi = drs.substring(virgulindex + 1);

			dersler.add(new Dersler(dersinadi, dersinkredisi));
		}

		ArrayAdapter<String> harfnotlistesi_adap = new ArrayAdapter<String>(
				getActivity(), R.layout.spinnernotharf, HarfNotListesiGetir());

		LinearLayout ll = (LinearLayout) rootView
				.findViewById(R.id.listViewistatistik);
		llx = new LinearLayout[dersler.size()];
		tx = new TextView[dersler.size()];
		tx1 = new TextView[dersler.size()];
		spn = new Spinner[dersler.size()];

		for (int i = 0; i < dersler.size(); i++) {

			llx[i] = new LinearLayout(getActivity());
			llx[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT, 3f));
			llx[i].setOrientation(LinearLayout.HORIZONTAL);
			llx[i].setWeightSum(3f);
			llx[i].setPadding(5, 5, 5, 5);

			tx[i] = new TextView(getActivity());
			tx1[i] = new TextView(getActivity());
			spn[i] = new Spinner(getActivity());

			tx[i].setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.5f));
			tx[i].setPadding(5, 5, 5, 5);
			tx[i].setGravity(Gravity.CENTER_VERTICAL);
			tx[i].setTag("dersadi_" + i);

			tx1[i].setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.75f));
			tx1[i].setPadding(5, 5, 5, 5);
			tx1[i].setGravity(Gravity.CENTER_VERTICAL);
			tx1[i].setTag("derskredi_" + i);

			spn[i].setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.75f));
			spn[i].setPadding(5, 5, 5, 5);
			// spn[i].setGravity(Gravity.CENTER_VERTICAL);
			spn[i].setTag("dersnot_" + i);

			tx[i].setText(dersler.get(i).getDers_adi());
			tx1[i].setText(dersler.get(i).getKredi());
			spn[i].setAdapter(harfnotlistesi_adap);

			llx[i].setId(i);
			llx[i].setClickable(true);
			// final int j = i;
			llx[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// msg(tx[j].getText().toString());
				}
			});

			llx[i].addView(tx[i]);
			llx[i].addView(tx1[i]);
			llx[i].addView(spn[i]);

			LinearLayout lnr_qwe = llx[i];
			ll.addView(lnr_qwe);

		}
	}

	String secilensinif;

	public void getDersListesi1() {

		sinifsecim = (Spinner) rootView.findViewById(R.id.sinifsecim);

		sinifsecim.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				secilensinif = sinifsecim.getItemAtPosition(pos).toString();

				if (secilensinif.equals("1.Sınıf 1.Dönem")) {
					MyListView(R.array.sinif1donem1);
					Toast.makeText(getActivity(), "1.Sınıf 1.Dönem",
							Toast.LENGTH_LONG).show();

				} else if (secilensinif.equals("1.Sınıf 2.Dönem")) {
					MyListView(R.array.sinif1donem2);
					Toast.makeText(getActivity(), "1.Sınıf 2.Dönem",
							Toast.LENGTH_LONG).show();

				} else if (secilensinif.equals("2.Sınıf 1.Dönem")) {
					MyListView(R.array.sinif2donem1);
					Toast.makeText(getActivity(), "2.Sınıf 1.Dönem",
							Toast.LENGTH_LONG).show();

				} else if (secilensinif.equals("2.Sınıf 2.Dönem")) {
					MyListView(R.array.sinif2donem2);
					Toast.makeText(getActivity(), "2.Sınıf 2.Dönem",
							Toast.LENGTH_LONG).show();

				} else if (secilensinif.equals("3.Sınıf 1.Dönem")) {
					MyListView(R.array.sinif3donem1);
					Toast.makeText(getActivity(), "3.Sınıf 1.Dönem",
							Toast.LENGTH_LONG).show();

				} else if (secilensinif.equals("3.Sınıf 2.Dönem")) {
					MyListView(R.array.sinif3donem2);
					Toast.makeText(getActivity(), "3.Sınıf 2.Dönem",
							Toast.LENGTH_LONG).show();

				} else if (secilensinif.equals("4.Sınıf 1.Dönem")) {
					MyListView(R.array.sinif4donem1);
					Toast.makeText(getActivity(), "4.Sınıf 1.Dönem",
							Toast.LENGTH_LONG).show();

				} else if (secilensinif.equals("4.Sınıf 2.Dönem")) {
					MyListView(R.array.sinif4donem2);
					Toast.makeText(getActivity(), "4.Sınıf 2.Dönem",
							Toast.LENGTH_LONG).show();

				}
				SharedPreferences_NotlariDoldur();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				secilensinif = "1.Sınıf 1.Dönem";
				MyListView(R.array.sinif1donem1);
				SharedPreferences_NotlariDoldur();
			}

		});

	}

	private void SharedPreferences_NotlariDoldur() {

		LinearLayout satirlar = (LinearLayout) rootView
				.findViewById(R.id.listViewistatistik);

		String name = kutuphane.TemizString(secilensinif);
		SharedPreferences shared = getActivity().getSharedPreferences(name,
				getActivity().MODE_PRIVATE);
		for (int a = 0; a < satirlar.getChildCount(); a++) {
			View satir = satirlar.getChildAt(a);
			Spinner harfnot = (Spinner) satir.findViewWithTag("dersnot_" + a);

			int position = shared.getInt("dersnot_" + a, 0);
			harfnot.setSelection(position);

		}

	}

	public void btn_Hesapla_Click1() {

		Kutuphane.hideKeyboard(getActivity());

		getActivity();
		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				kutuphane.TemizString(secilensinif), Context.MODE_PRIVATE);

		LinearLayout satirlar = (LinearLayout) rootView
				.findViewById(R.id.listViewistatistik);

		double toplam = 0;
		double toplamkredi = 0;

		for (int a = 0; a < satirlar.getChildCount(); a++) {

			View satir = satirlar.getChildAt(a);

			Spinner harfnot = (Spinner) satir.findViewWithTag("dersnot_" + a);
			double harfdeger = HarfPuan(harfnot.getSelectedItemPosition());

			TextView kredi = (TextView) satir.findViewWithTag("derskredi_" + a);
			int kredim = Integer.parseInt(kredi.getText().toString());

			if (harfnot.getSelectedItemPosition() != 0)
				toplamkredi += kredim;

			toplam += harfdeger * kredim;

			// ////////////////////////////////////

			sharedpref
					.edit()
					.putInt("dersnot_" + a, harfnot.getSelectedItemPosition())
					.putString("dersnot_harf_" + a,
							harfnot.getSelectedItem().toString())
					.putInt("dersnot_kredi_" + a, kredim).commit();

		}

		double ortalama = (toplam / toplamkredi);
		// Virg�lden sonra 2 basamak
		String ortalamam = new DecimalFormat("0.00").format(ortalama);

		kutuphane.getAlertDialog(getActivity(), "Not Ortalaması",
				" Dönemlik Not Ortalamanız : " + String.valueOf(ortalamam)
						+ "\n Kümülatif : " + KumulatifHesapla());

	}

	// SharedPreferences kay�tlar�ndan k�m�latif notu hesaplar
	private String KumulatifHesapla() {
		String[] siniflar = getResources().getStringArray(R.array.siniflar);
		double toplamkredi = 0;
		double toplam = 0;
		for (String i : siniflar) {
			String name = kutuphane.TemizString(i);
			SharedPreferences shared = getActivity().getSharedPreferences(name,
					getActivity().MODE_PRIVATE);

			int kacdeger = shared.getAll().size();

			for (int j = 0; j < kacdeger; j++) {

				String dersnot_harf = shared.getString("dersnot_harf_" + j,
						"qwerty");
				double harf = HarfPuan(shared.getInt("dersnot_" + j, 0));
				int kredi = shared.getInt("dersnot_kredi_" + j, 0);

				if (harf != 0 || dersnot_harf.contentEquals("FF")) {
					toplamkredi += kredi;
				}

				toplam += harf * kredi;
			}

		}
		double ortalama = (toplam / toplamkredi);
		// Virg�lden sonra 2 basamak
		String ortalamam = new DecimalFormat("0.00").format(ortalama);
		return ortalamam;
	}

	// Spinner dan se�ilen AA , BA gibi harf notunun kredi derecesini getirir
	public double HarfPuan(int selectionitemID) {
		String notlar[] = getResources().getStringArray(R.array.puanlar);
		String harfpuanim = notlar[selectionitemID];
		int yer = harfpuanim.indexOf(",");
		String kredidegeri = harfpuanim.substring(yer + 1);

		double kredi = Double.parseDouble(kredidegeri);
		return kredi;
	}

	// Dersad� ve kredisini tutan Dersler nesnesi
	public class Dersler implements Serializable {
		private static final long serialVersionUID = 1L;

		private String ders_adi;
		private String kredi;

		public Dersler(String ders_adi, String kredi) {
			// TODO Auto-generated constructor stub
			super();
			this.ders_adi = ders_adi;
			this.kredi = kredi;
		}

		public String getDers_adi() {
			return ders_adi;
		}

		public void setDers_adi(String ders_adi) {
			this.ders_adi = ders_adi;
		}

		public String getKredi() {
			return kredi;
		}

		public void setKredi(String kredi) {
			this.kredi = kredi;
		}

	}

	// AA,3 ; BA,2 �eklinde belirtilen puan sistemini AA , BA �eklinde par�alar
	private ArrayList<String> HarfNotListesiGetir() {
		ArrayList<String> notlararray = new ArrayList<String>();
		String notlar[] = getResources().getStringArray(R.array.puanlar);

		for (int a = 0; a < notlar.length; a++) {
			int yer = notlar[a].indexOf(",");
			String harf = notlar[a].substring(0, yer);

			notlararray.add(harf);
		}

		return notlararray;

	}

	// MyListView in i�indeki Layoutlar� se�erek i�inin temizlenmesini sa�lar.
	public void MyListViewTemizle() {
		final LinearLayout satirlar = (LinearLayout) rootView
				.findViewById(R.id.listViewistatistik);
		int sayac = satirlar.getChildCount();

		for (int a = sayac - 1; a > -1; a--) {
			final View vi = (LinearLayout) satirlar.getChildAt(a);

			satirlar.removeView(vi);

		}
	}

	// Di�er b�l�m�n�n sat�rlar�n� doldur
	public void getSatirDoldurDiger() {
		LinearLayout dgr_llx[];
		EditText dgr_tx[];
		Spinner dgr_tx1[];
		Spinner dgr_spn[];

		int derssayisi = getResources().getInteger(R.integer.diger_derssayisi);

		LinearLayout dgr_ll = (LinearLayout) rootView
				.findViewById(R.id.listViewdiger);

		String notlar[] = getResources().getStringArray(R.array.krediler);
		ArrayAdapter<String> krediler = new ArrayAdapter<String>(getActivity(),
				R.layout.spinnernotharf, notlar);
		ArrayAdapter<String> harfnotlistesi_adap = new ArrayAdapter<String>(
				getActivity(), R.layout.spinnernotharf, HarfNotListesiGetir());

		dgr_llx = new LinearLayout[derssayisi];
		dgr_tx = new EditText[derssayisi];
		dgr_tx1 = new Spinner[derssayisi];
		dgr_spn = new Spinner[derssayisi];

		for (int i = 0; i < derssayisi; i++) {

			dgr_llx[i] = new LinearLayout(getActivity());
			dgr_llx[i].setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3f));
			dgr_llx[i].setOrientation(LinearLayout.HORIZONTAL);
			dgr_llx[i].setWeightSum(3f);
			dgr_llx[i].setPadding(5, 5, 5, 5);

			dgr_tx[i] = new EditText(getActivity());
			dgr_tx1[i] = new Spinner(getActivity());
			dgr_spn[i] = new Spinner(getActivity());

			dgr_tx[i].setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1f));
			dgr_tx[i].setPadding(5, 5, 5, 5);
			// dgr_tx[i].setFocusable(false);
			// dgr_tx[i].setFocusableInTouchMode(true);
			dgr_tx[i].setGravity(Gravity.CENTER_VERTICAL);
			dgr_tx[i].setTag("dersadidiger_" + i);

			dgr_tx1[i].setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1f));
			dgr_tx1[i].setPadding(5, 5, 5, 5);
			// dgr_tx1[i].setGravity(Gravity.CENTER_VERTICAL);
			dgr_tx1[i].setTag("derskredidiger_" + i);

			dgr_spn[i].setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1f));
			dgr_spn[i].setPadding(5, 5, 5, 5);
			// dgr_spn[i].setGravity(Gravity.CENTER_VERTICAL);
			dgr_spn[i].setTag("dersnotdiger_" + i);

			dgr_tx[i].setText("Ders" + i);
			dgr_tx[i].setClickable(true);
			dgr_tx[i].setOnFocusChangeListener(this);
			dgr_tx1[i].setAdapter(krediler);
			dgr_spn[i].setAdapter(harfnotlistesi_adap);

			dgr_llx[i].setId(i);
			dgr_llx[i].setClickable(true);
			// final int j = i;
			dgr_llx[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// msg(tx[j].getText().toString());
				}
			});

			dgr_llx[i].addView(dgr_tx[i]);
			dgr_llx[i].addView(dgr_tx1[i]);
			dgr_llx[i].addView(dgr_spn[i]);
			dgr_ll.addView(dgr_llx[i]);

		}

	}

	// Di�er b�l�m�n�n hesaplamas�n� yap
	public void btn_Hesapla_Diger_Click() {
		Kutuphane.hideKeyboard(getActivity());
		LinearLayout satirlar = (LinearLayout) rootView
				.findViewById(R.id.listViewdiger);

		double toplam = 0;
		double toplamkredi = 0;

		for (int a = 0; a < satirlar.getChildCount(); a++) {

			View satir = satirlar.getChildAt(a);

			Spinner harfnot = (Spinner) satir.findViewWithTag("dersnotdiger_"
					+ a);
			double harfdeger = HarfPuan(harfnot.getSelectedItemPosition());

			Spinner kredi = (Spinner) satir.findViewWithTag("derskredidiger_"
					+ a);
			int kredim = Integer.parseInt(kredi.getSelectedItem().toString());

			if (harfnot.getSelectedItemPosition() != 0)
				toplamkredi += kredim;

			toplam += harfdeger * kredim;
		}

		double ortalama = (toplam / toplamkredi);
		// Virg�lden sonra 2 basamak
		String ortalamam = new DecimalFormat("0.00").format(ortalama);

		kutuphane.getAlertDialog(getActivity(), "DEU İstatistik",
				"Not Ortalamanız : " + String.valueOf(ortalamam));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btn_hesapla:
			btn_Hesapla_Click1();
			break;
		case R.id.btn_hesapla_diger:
			btn_Hesapla_Diger_Click();
			break;

		default:
			// Kutuphane.hideKeyboard(getActivity());
			break;
		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub

		if (v.hasFocus()) {
			EditText edttxt = (EditText) v;
			String txt = edttxt.getText().toString().trim();
			if (txt != null && txt != "" && txt.length() > 4) {
				if (txt.substring(0, 4).equals("Ders")) {
					edttxt.setText("");
				}
			}
		}
	}

}
