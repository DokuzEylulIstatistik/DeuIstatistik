package com.deu.istatistik;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Fragment_DescStats extends Fragment implements OnClickListener {

	private static String TAG = "DESCRIPTION_STATS";
	Kutuphane kutuphane = new Kutuphane();
	ArrayList<DescriptionStats> arraylist = new ArrayList<DescriptionStats>();
	double[] sayilar = null;

	public Fragment_DescStats() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.activity_descriptionstat,
				container, false);

		Log.e(TAG, "onCreate çalıştı");

		String subtitle = getResources().getString(R.string.subtitle_desc_stat);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(subtitle);
		actionBar.setTitle("Descriptive Stats");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.show();

		rootview.setOnClickListener(this);
		Button btn = (Button) rootview.findViewById(R.id.btn_descStatHesapla);
		btn.setOnClickListener(this);

		EditText editTextSayilar = (EditText) rootview
				.findViewById(R.id.editTextSayilar);
//		editTextSayilar.setOnEditorActionListener(new OnEditorActionListener() {
//			// @Override
//			// public boolean onEditorAction(TextView v, int actionId, KeyEvent
//			// event) {
//			// Log.d(TAG, "onEditorAction");
//			// if (event != null && event.getAction() != KeyEvent.ACTION_DOWN) {
//			// return false;
//			// } else if (actionId == EditorInfo.IME_ACTION_SEARCH
//			// || event == null
//			// || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//			//
//			// }
//			// return false;
//			// }
//
//			@Override
//			public boolean onEditorAction(TextView v, int actionId,
//
//			KeyEvent event) {
//				boolean handled = false;
//				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//
//					KeyEvent newevent = new KeyEvent(KeyEvent.ACTION_DOWN,
//							KeyEvent.KEYCODE_ENTER);
//					
//					// KeyEvent.changeAction(newevent, KeyEvent.ACTION_DOWN);
//					// event.changeAction(newevent, KeyEvent.ACTION_DOWN);
//
//					handled = true;
//				}
//				return handled;
//			}
//		});

		return rootview;
	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	public void btn_descriptionStats_Click() {
		arraylist.clear();
		fillStats();
		ListView ListView_dsc_stat_description = (ListView) getActivity()
				.findViewById(R.id.ListView_dsc_stat_description);

		Kutuphane.hideKeyboard(getActivity());

		CustomAdapter customadap = new CustomAdapter(getActivity(), arraylist);
		ListView_dsc_stat_description.setAdapter(customadap);
	}

	private void fillStats() {
		EditText editTextSayilar = (EditText) getActivity().findViewById(
				R.id.editTextSayilar);
		sayilar = parseEditTexttoDouble(editTextSayilar.getText().toString(),
				"\n");

		if (sayilar == null)
			return;

		double mean = calcMean(sayilar);
		double stdev = calcStDev(sayilar);
		double variance = Math.pow(stdev, 2);
		double min = calcMin(sayilar);
		double max = calcMax(sayilar);
		double sum = calcSum(sayilar);
		double N = calcN(sayilar);
		double range = calcRange(sayilar);

		DescriptionStats ds_range = new DescriptionStats();
		ds_range.setStats_key("Aralık");
		ds_range.setStats_val(new DecimalFormat("0.#####").format(range));

		DescriptionStats ds_sum = new DescriptionStats();
		ds_sum.setStats_key("Toplam");
		ds_sum.setStats_val(new DecimalFormat("0.#####").format(sum));

		DescriptionStats ds_mean = new DescriptionStats();
		ds_mean.setStats_key("Ortalama");
		ds_mean.setStats_val(new DecimalFormat("0.#####").format(mean));

		DescriptionStats ds_min = new DescriptionStats();
		ds_min.setStats_key("Minimum ");
		ds_min.setStats_val(new DecimalFormat("0.#####").format(min));

		DescriptionStats ds_max = new DescriptionStats();
		ds_max.setStats_key("Maksimum");
		ds_max.setStats_val(new DecimalFormat("0.#####").format(max));

		DescriptionStats ds_stdev = new DescriptionStats();
		ds_stdev.setStats_key("Standart Sapma");
		ds_stdev.setStats_val(new DecimalFormat("0.#####").format(stdev));

		DescriptionStats ds_var = new DescriptionStats();
		ds_var.setStats_key("Varyans");
		ds_var.setStats_val(new DecimalFormat("0.#####").format(variance));

		DescriptionStats ds_N = new DescriptionStats();
		ds_N.setStats_key("N");
		ds_N.setStats_val(new DecimalFormat("0.#####").format(N));

		arraylist.add(ds_mean);
		arraylist.add(ds_var);
		arraylist.add(ds_stdev);
		arraylist.add(ds_min);
		arraylist.add(ds_max);
		arraylist.add(ds_range);
		arraylist.add(ds_sum);
		arraylist.add(ds_N);

	}

	private double calcOutlier(double[] degerler) {

		return 0;
	}

	private double calcRange(double[] degerler) {

		double min = calcMin(degerler);
		double max = calcMax(degerler);

		return (max - min);
	}

	private double calcStDev(double[] degerler) {
		int n = degerler.length;
		double mean = calcMean(sayilar);

		double toplam = 0;
		for (int i = 0; i < degerler.length; i++) {

			double sonuc = Math.pow((degerler[i] - mean), 2);
			toplam += sonuc;
		}

		return Math.sqrt((toplam / (n - 1)));

	}

	private double calcSum(double[] degerler) {
		double toplam = 0;
		for (int i = 0; i < degerler.length; i++) {
			toplam += degerler[i];

		}
		return toplam;
	}

	private double calcMean(double[] degerler) {
		double toplam = 0;
		for (int i = 0; i < degerler.length; i++) {
			toplam += degerler[i];

		}
		return (toplam / degerler.length);
	}

	private double calcMin(double[] degerler) {
		double enkucuk = 0;
		double enkucukkontrol = degerler[0];
		for (int i = 0; i < degerler.length; i++) {

			if (degerler[i] <= enkucukkontrol) {
				for (int j = i + 1; j < degerler.length; j++) {

					if (degerler[i] < degerler[j]) {
						enkucuk = degerler[i];
					} else {
						enkucuk = degerler[j];
					}

				}
				enkucukkontrol = enkucuk;
			}

		}

		return enkucuk;

	}

	private double calcMax(double[] degerler) {
		double enbuyuk = 0;
		double enbuyukkontrol = degerler[0];
		for (int i = 0; i < degerler.length; i++) {

			if (degerler[i] >= enbuyukkontrol) {
				for (int j = i + 1; j < degerler.length; j++) {

					if (degerler[i] > degerler[j]) {
						enbuyuk = degerler[i];
					} else {
						enbuyuk = degerler[j];
					}

				}
				enbuyukkontrol = enbuyuk;
			}

		}

		return enbuyuk;

	}

	private double calcN(double[] degerler) {
		return degerler.length;
	}

	private double[] parseEditTexttoDouble(String deger, String regex) {

		double[] temp = null;
		try {
			String temizlenmisdeger = replaceCharacter(deger, ",", ".");

			String[] splitdeger = temizlenmisdeger.split(regex);
			temp = new double[splitdeger.length];
			for (int i = 0; i < splitdeger.length; i++) {
				if (splitdeger[i] == "\n")
					continue;
				temp[i] = Double.parseDouble(splitdeger[i]);
			}

		} catch (Exception e) {

			kutuphane.getAlertDialog(getActivity(), "Hatalı Giriş ! ",
					"Lütfen doğru giriş yaptığınızdan emin olunuz.");
			return null;
		}
		return temp;
	}

	private String replaceCharacter(String deger, String oldChar, String newChar) {
		return deger.replace(oldChar, newChar);

	}

	class DescriptionStats implements Serializable {

		private static final long serialVersionUID = 1L;

		private String stats_key;
		private String stats_val;

		public String getStats_key() {
			return stats_key;
		}

		public void setStats_key(String stats_key) {
			this.stats_key = stats_key;
		}

		public String getStats_val() {
			return stats_val;
		}

		public void setStats_val(String stats_val) {
			this.stats_val = stats_val;
		}

	}

	private class CustomAdapter extends BaseAdapter {
		ArrayList<DescriptionStats> arraylist;
		LayoutInflater inflater = null;
		DescriptionStats temp = null;

		public CustomAdapter(Activity activity,
				ArrayList<DescriptionStats> arraylist2) {

			arraylist = arraylist2;

			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arraylist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View vi = convertView;

			if (convertView == null) {
				vi = inflater.inflate(R.layout.descstatlayout, null);

				TextView textView_descstatlayout_title = (TextView) vi
						.findViewById(R.id.textView_descstatlayout_title);

				TextView textView_descstatlayout_content = (TextView) vi
						.findViewById(R.id.textView_descstatlayout_content);

				temp = (DescriptionStats) arraylist.get(position);

				textView_descstatlayout_title.setText(temp.getStats_key());
				textView_descstatlayout_content.setText(temp.getStats_val());

			} else {
				vi = convertView;
			}

			return vi;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_descStatHesapla:
			btn_descriptionStats_Click();
			break;

		default:
			break;
		}
	}

}
