package com.deu.istatistik;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.ValueDependentColor;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FragmentTablo extends Fragment implements AnimationListener,
		OnClickListener {

	View rootview;
	FragmentActivity activity;
	public Spinner n1;
	public Spinner n2;
	Kutuphane kutuphane = new Kutuphane();

	ArrayList<String> degerler_kikare;
	ArrayList<String> degerler_student_t;
	ArrayList<String> degerler_tukey01;
	ArrayList<String> degerler_tukey05;
	ArrayList<String> degerler_spearmankorelasyon;
	ArrayList<String> degerler_f;
	// ////
	Spinner spin_tukey05;
	Spinner spin_tukey01;
	Spinner spin_kikare;
	Spinner spin_student_t;
	Spinner spin_spearmankorelasyon;

	private void SpinnerDoldur() {

		tDoldur();
		chiSquareDoldur();
		tukey01Doldur();
		tukey05Doldur();
		spearmanDoldur();
	}

	private void spearmanDoldur() {
		degerler_spearmankorelasyon = kutuphane.getDosya(activity,
				"spearmankorelasyon.txt");
		String[] alfadegerler = degerler_spearmankorelasyon.get(0).split(";");
		// alfadegerler[0] = "a";
		ArrayAdapter<String> adapp = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_dropdown_item, alfadegerler);

		spin_spearmankorelasyon = (Spinner) rootview
				.findViewById(R.id.spinner_spearmankorelasyon);
		spin_spearmankorelasyon.setAdapter(adapp);
	}

	private void chiSquareDoldur() {
		degerler_kikare = kutuphane.getDosya(activity, "kikare.txt");
		String[] alfadegerler = degerler_kikare.get(0).split(";");
		// alfadegerler[0] = "a";
		ArrayAdapter<String> adapp = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_dropdown_item, alfadegerler);

		spin_kikare = (Spinner) rootview.findViewById(R.id.spinner_kikare_alfa);
		spin_kikare.setAdapter(adapp);
	}

	private void tukey01Doldur() {
		degerler_tukey01 = kutuphane.getDosya(activity, "tukeytesti01.txt");
		String[] alfadegerler2 = degerler_tukey01.get(0).split(";");
		// alfadegerler[0] = "a";
		ArrayAdapter<String> adaptukey = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_dropdown_item, alfadegerler2);

		spin_tukey01 = (Spinner) rootview.findViewById(R.id.spinner_tukey01);
		spin_tukey01.setAdapter(adaptukey);
	}

	private void tukey05Doldur() {
		degerler_tukey05 = kutuphane.getDosya(activity, "tukeytesti05.txt");
		String[] alfadegerler3 = degerler_tukey05.get(0).split(";");
		// alfadegerler[0] = "a";
		ArrayAdapter<String> adaptukey05 = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_dropdown_item, alfadegerler3);

		spin_tukey05 = (Spinner) rootview.findViewById(R.id.spinner_tukey05);
		spin_tukey05.setAdapter(adaptukey05);
	}

	private void tDoldur() {
		degerler_student_t = kutuphane.getDosya(activity, "student_t.txt");
		String[] alfadegerler1 = degerler_student_t.get(0).split(";");
		// alfadegerler[0] = "a";
		ArrayAdapter<String> adap = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_dropdown_item, alfadegerler1);

		spin_student_t = (Spinner) rootview
				.findViewById(R.id.spinner_student_alfa);
		spin_student_t.setAdapter(adap);
	}

	InputMethodManager imm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		View vi = inflater.inflate(R.layout.tablo, container, false);
		rootview = vi;
		activity = super.getActivity();

		kutuphane.startFlurry(activity);
		FlurryAgent.logEvent("Tablo Activity");

		String actionbarsubTitle = getResources().getString(
				R.string.subtitle_tablo);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(actionbarsubTitle);
		actionBar.setTitle("Tablolar");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.show();

		SpinnerDoldur();
		//
		final Spinner f_alfa = (Spinner) rootview
				.findViewById(R.id.spinner_f_alfa);
		n1 = (Spinner) rootview.findViewById(R.id.f_n1);
		n2 = (Spinner) rootview.findViewById(R.id.f_n2);
		//
		final String[] tablolar = getResources().getStringArray(
				R.array.Tablolar);
		ArrayAdapter<String> adap = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_dropdown_item, tablolar);
		Spinner spinnerTablolar = (Spinner) rootview
				.findViewById(R.id.spinnerTablolar);
		spinnerTablolar.setAdapter(adap);

		spinnerTablolar.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View vi, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				VisibilityTable();
				int secilenindis = parent.getSelectedItemPosition();

				switch (secilenindis) {
				case 0:
					break;
				// Z
				case 1:
					LinearLayout lyt0 = (LinearLayout) rootview
							.findViewById(R.id.layout_z);
					lyt0.setVisibility(View.VISIBLE);
					break;
				// Student T
				case 2:
					LinearLayout lyt = (LinearLayout) rootview
							.findViewById(R.id.layout_student_t);
					lyt.setVisibility(View.VISIBLE);
					break;
				// Chi-Square
				case 3:
					LinearLayout lyt1 = (LinearLayout) rootview
							.findViewById(R.id.layout_kikare);
					lyt1.setVisibility(View.VISIBLE);
					break;
				case 4:
					LinearLayout lyt2 = (LinearLayout) rootview
							.findViewById(R.id.layout_tukey01);
					lyt2.setVisibility(View.VISIBLE);
					break;
				case 5:
					LinearLayout lyt3 = (LinearLayout) rootview
							.findViewById(R.id.layout_tukey05);
					lyt3.setVisibility(View.VISIBLE);
					break;
				case 6:
					LinearLayout lyt4 = (LinearLayout) rootview
							.findViewById(R.id.layout_spearmankorelasyon);
					lyt4.setVisibility(View.VISIBLE);
					break;
				case 7: // f

					LinearLayout lyt5 = (LinearLayout) rootview
							.findViewById(R.id.layout_f);
					lyt5.setVisibility(View.VISIBLE);

					String stralfa[] = { "0.01", "0.025", "0.05", "0.10" };
					final ArrayAdapter<String> arrayalfa = new ArrayAdapter<String>(
							activity,
							android.R.layout.simple_list_item_checked,
							android.R.id.text1, stralfa);

					f_alfa.setAdapter(arrayalfa);

					f_alfa.setOnItemSelectedListener(new OnItemSelectedListener() {
						public void onItemSelected(AdapterView<?> parent,
								View view, int pos, long id) {
							String[] alfadegerler;

							String[] alfadegerler2 = { "Seçiniz", "1", "2",
									"3", "4", "5", "6", "7", "8", "9", "10",
									"11", "12", "13", "14", "15", "16", "17",
									"18", "19", "20", "21", "22", "23", "24",
									"25", "26", "27", "28", "29", "30", "40",
									"60", "120", "" };
							ArrayAdapter<String> adapp;
							ArrayAdapter<String> adapp2;
							switch (pos) {

							// aşağıdaki kısımlar normali ile aynı sadace
							// MainActivity.this yerine orda this yazıyor.
							case 0:

								degerler_f = kutuphane.getDosya(activity,
										"f001.txt");
								alfadegerler = degerler_f.get(0).split(";");

								adapp2 = new ArrayAdapter<String>(
										activity,
										android.R.layout.simple_spinner_dropdown_item,
										alfadegerler2);
								adapp = new ArrayAdapter<String>(
										activity,
										android.R.layout.simple_spinner_dropdown_item,
										alfadegerler);
								n1.setAdapter(adapp);
								n2.setAdapter(adapp2);
								break;

							case 1:

								degerler_f = kutuphane.getDosya(activity,
										"f0025.txt");
								alfadegerler = degerler_f.get(0).split(";");
								adapp2 = new ArrayAdapter<String>(
										activity,
										android.R.layout.simple_spinner_dropdown_item,
										alfadegerler2);
								adapp = new ArrayAdapter<String>(
										activity,
										android.R.layout.simple_spinner_dropdown_item,
										alfadegerler);
								n1.setAdapter(adapp);
								n2.setAdapter(adapp2);
								break;

							case 2:

								degerler_f = kutuphane.getDosya(activity,
										"f005.txt");
								alfadegerler = degerler_f.get(0).split(";");
								adapp2 = new ArrayAdapter<String>(
										activity,
										android.R.layout.simple_spinner_dropdown_item,
										alfadegerler2);
								adapp = new ArrayAdapter<String>(
										activity,
										android.R.layout.simple_spinner_dropdown_item,
										alfadegerler);
								n1.setAdapter(adapp);
								n2.setAdapter(adapp2);
								break;

							case 3:

								degerler_f = kutuphane.getDosya(activity,
										"f010.txt");
								alfadegerler = degerler_f.get(0).split(";");
								adapp2 = new ArrayAdapter<String>(
										activity,
										android.R.layout.simple_spinner_dropdown_item,
										alfadegerler2);
								adapp = new ArrayAdapter<String>(
										activity,
										android.R.layout.simple_spinner_dropdown_item,
										alfadegerler);
								n1.setAdapter(adapp);
								n2.setAdapter(adapp2);
								break;

							default:

								break;
							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {

						}
					});
					// case 7 end
					break;
				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		rootview.setOnClickListener(this);
		ButtonClickListener();

		imm = (InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);

		return vi;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	private void ButtonClickListener() {
		Button btn_z = (Button) rootview.findViewById(R.id.btn_z);
		btn_z.setOnClickListener(this);

		Button btn_tukey05 = (Button) rootview.findViewById(R.id.btn_tukey05);
		btn_tukey05.setOnClickListener(this);

		Button btn_tukey01 = (Button) rootview.findViewById(R.id.btn_tukey01);
		btn_tukey01.setOnClickListener(this);

		Button btn_student_t = (Button) rootview
				.findViewById(R.id.btn_student_t);
		btn_student_t.setOnClickListener(this);

		Button btn_spearmankorelasyon = (Button) rootview
				.findViewById(R.id.btn_spearmankorelasyon);
		btn_spearmankorelasyon.setOnClickListener(this);

		Button btn_kikare = (Button) rootview.findViewById(R.id.btn_kikare);
		btn_kikare.setOnClickListener(this);

		Button btn_tersz = (Button) rootview.findViewById(R.id.btn_tersz);
		btn_tersz.setOnClickListener(this);

		Button btn_f = (Button) rootview.findViewById(R.id.btn_f);
		btn_f.setOnClickListener(this);

	}

	private void VisibilityTable() {
		LinearLayout layoutTablolar = (LinearLayout) rootview
				.findViewById(R.id.layoutTablolar);
		int a = layoutTablolar.getChildCount();

		for (int i = 0; i < a; i++) {
			LinearLayout lyt = (LinearLayout) layoutTablolar.getChildAt(i);
			lyt.setVisibility(View.GONE);

		}
	}

	public void btn_z_Click(View vi) {
		try {

			LinearLayout layout = (LinearLayout) rootview
					.findViewById(R.id.layout_z);

			// if (layout.getChildCount() == 3) {
			// View ww = layout.getChildAt(layout.getChildCount() - 1);
			// layout.removeView(ww);
			// }
			final String[] ztablo = getResources().getStringArray(
					R.array.ztablo);

			TextView textview_z_sonuc = (TextView) rootview
					.findViewById(R.id.textview_z_sonuc);
			EditText KulGiris = (EditText) rootview
					.findViewById(R.id.editTxt_z);
			// KulGiris.setFilters(new InputFilter[] { new InputFilterMinMax(0,
			// 4) });
			imm.hideSoftInputFromWindow(KulGiris.getWindowToken(), 0);

			final double x = Double.parseDouble(KulGiris.getText().toString());
			if (x < 3.5) {
				double z1 = Double.parseDouble(ztablo[(int) (x * 100)]
						.substring(0, 4));

				double z2 = Double.parseDouble(ztablo[(int) (x * 100) + 1]
						.substring(0, 4));

				double y1 = Double.parseDouble(ztablo[(int) (x * 100)]
						.substring(ztablo[(int) (x * 100)].length() - 6,
								ztablo[(int) (x * 100)].length()));

				double y2 = Double.parseDouble(ztablo[(int) (x * 100) + 1]
						.substring(ztablo[(int) (x * 100) + 1].length() - 6,
								ztablo[(int) (x * 100) + 1].length()));

				double sonuc = (y2 - y1) * (x - z1) / (z2 - z1);



				textview_z_sonuc.setText("Sonuç : "
						+ (new DecimalFormat("#.####").format(sonuc + y1)));
			

				GraphView graphView = new BarGraphView(activity, "");

				GraphViewSeriesStyle seriesStyle = new GraphViewSeriesStyle();

				seriesStyle.setValueDependentColor(new ValueDependentColor() {
					@Override
					public int get(GraphViewDataInterface data) {
						// the higher the more red

						if (data.getX() >= 0 && data.getX() < x) {
							return getResources().getColor(R.color.z_icalan);
						} else {
							return getResources().getColor(R.color.z_disalan);
						}

					}
				});
				GraphViewDataInterface[] dat = new GraphViewDataInterface[] { new GraphViewData(
						0, 0) };
				GraphViewSeries exampleSeries = new GraphViewSeries("Tablo",
						seriesStyle, dat);
				int countNumber = 200;
				double number = -4;
				for (int i = 0; i < countNumber; i++) {

					GraphViewData dt = new GraphViewData(number,
							StandartNormal(number));
					exampleSeries.appendData(dt, true, countNumber);

					double q = (countNumber / 8);
					q = (1 / q);

					number += q;

				}

				//
				Animation animation = AnimationUtils.loadAnimation(activity,
						R.anim.tabloanim);
				animation.setDuration(1000);
				animation.setRepeatMode(1);
				animation.setAnimationListener(this);

				// graphView.setBackground(getResources().getDrawable(
				// R.drawable.edittext));
				// graphView.setScalable(true);
				// graphView.setScrollable(true);
				graphView.setHorizontalLabels(new String[] { "" });
				graphView.setVerticalLabels(new String[] { "" });
				graphView.addSeries(exampleSeries);
				graphView.setAnimation(animation);
				// graphView.setDrawingCacheBackgroundColor(Color.BLACK);

				// GraphViewSeriesStyle sdf = new GraphViewSeriesStyle();

				// graphView.setHorizontalLabels(new String[] { "2 days ago",
				// / "yesterday", "today", "tomorrow" });
				// graphView.setVerticalLabels(new String[] { "high", "middle",
				// "low" });
				// graphView.getGraphViewStyle().setGridColor(Color.GREEN);
				// graphView.getGraphViewStyle().setHorizontalLabelsColor(
				// Color.YELLOW);
				// graphView.getGraphViewStyle().setVerticalLabelsColor(Color.RED);
				graphView.getGraphViewStyle().setTextSize(14);

				// getResources().getDimension(R.dimen.big));
				// graphView.getGraphViewStyle().setNumHorizontalLabels(5);
				// graphView.getGraphViewStyle().setNumVerticalLabels(4);
				// graphView.getGraphViewStyle().setVerticalLabelsWidth(300);
				// data

				LinearLayout view_tabloresimalan = (LinearLayout) layout
						.findViewById(R.id.view_tabloresimalan);

				view_tabloresimalan.removeAllViews();
				view_tabloresimalan.addView(graphView);

			}

			else {
				textview_z_sonuc.setText("Sonuç : " + Double.toString(0.4999));
			}
		} catch (Exception e) {
			kutuphane.getAlertDialog(activity, "Hata",
					"Lütfen doğru giriş yaptığınızdan emin olunuz.");
		}

	}

	public void btn_tersz_Click(View vi) {
		try {
			Kutuphane.hideKeyboard(activity);
			final String[] ztablo = getResources().getStringArray(
					R.array.ztablo);
			double[] arama = new double[ztablo.length];
			for (int i = 0; i < ztablo.length; i++) {
				arama[i] = Double.parseDouble(ztablo[i].substring(
						ztablo[i].length() - 6, ztablo[i].length()));
			}

			EditText KulGiris = (EditText) rootview
					.findViewById(R.id.editTxt_z);
			final double s = Double.parseDouble(KulGiris.getText().toString());

			if (s <= 0.4998) {
				int deger = zsearch(arama, s);

				String mini[] = (ztablo[deger].split(","));
				String maxi[] = (ztablo[deger + 1].split(","));

				double ters = (Double.parseDouble(mini[0]) - Double
						.parseDouble(maxi[0]))
						* (s - Double.parseDouble(mini[1]))
						/ (Double.parseDouble(mini[1]) - Double
								.parseDouble(maxi[1]));

				double sonuc = Double.parseDouble(mini[0]) + ters;
				kutuphane.getAlertDialog(activity, "Sonuç : ",
						new DecimalFormat("#.#####").format(sonuc));
			} else {
				Toast.makeText(activity,
						"Girilen değer 0 ile 0.4998 arasında olmalıdır.",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			kutuphane.getAlertDialog(activity, "Hata",
					"Lütfen doğru giriş yaptığınızdan emin olunuz.");
		}
	}

	public static int zsearch(double[] array, double s) {
		// veriler s�ralanm�� olarak gelecektir.
		int n = array.length;
		int q1, q2, q3, q4;

		q1 = n / 4;
		q2 = n * 2 / 4;
		q3 = n * 3 / 4;
		q4 = n;
		int mini = 0;
		if (s <= array[q1]) {
			for (int i = 0; i <= q1; i++) {
				if (s <= array[i]) {
					mini = i - 1;
					break;
				}
			}
		} else if (s <= array[q2]) {
			for (int i = q1; i <= q2; i++) {
				if (s <= array[i]) {
					mini = i - 1;
					break;
				}
			}
		} else if (s <= array[q3]) {
			for (int i = q2; i <= q3; i++) {
				if (s <= array[i]) {
					mini = i - 1;
					break;
				}
			}
		} else if (s <= array[q4 - 1]) {
			for (int i = q3; i <= q4; i++) {
				if (s <= array[i]) {
					mini = i - 1;
					break;
				}
			}
		}
		return mini;
	}

	private double StandartNormal(double x) {
		double sonuc = 0;
		sonuc = (1 / Math.sqrt(2 * Math.PI)) * Math.exp(-(Math.pow(x, 2) / 2));

		return sonuc;
	}

	public void btn_kikare_Click(View vi) {

		try {

			LinearLayout layout = (LinearLayout) rootview
					.findViewById(R.id.layout_kikare);

			EditText alfadeger = (EditText) rootview
					.findViewById(R.id.editTxt_kikare_sd);
			imm.hideSoftInputFromWindow(alfadeger.getWindowToken(), 0);
			String deger = alfadeger.getText().toString();
			final int deger_int = Integer.parseInt(deger);

			if (deger_int > 0 && deger_int <= 30) {

				String[] satir = degerler_kikare.get(deger_int).split(";");

				int sutun = spin_kikare.getSelectedItemPosition();

				String sonuc = satir[sutun];

				// kutuphane.getAlertDialog(activity, "Sonuç : ", sonuc);

				TextView textview_kikare_sonuc = (TextView) rootview
						.findViewById(R.id.textview_kikare_sonuc);
				textview_kikare_sonuc.setText("Sonuç : " + sonuc);

				// /////////////////////

			} else {
				kutuphane.getAlertDialog(activity, "Aralık Hatası",
						"Lütfen 1 ile 30 arasında değer giriniz.");
			}
		} catch (Exception ex) {
			kutuphane.getAlertDialog(activity, "Giriş Hatası",
					"Lütfen doğru giriş yaptığınızdan emin olunuz.");
		}

	}

	public void btn_student_t_Click(View vi) {

		try {

			EditText alfadeger = (EditText) rootview
					.findViewById(R.id.editTxt_studentSd);

			imm.hideSoftInputFromWindow(alfadeger.getWindowToken(), 0);
			String deger = alfadeger.getText().toString();
			int deger_int = Integer.parseInt(deger);

			if (deger_int > 0 && deger_int <= 30) {

				String[] satir = degerler_student_t.get(deger_int).split(";");

				int sutun = spin_student_t.getSelectedItemPosition();

				String sonuc = satir[sutun];

				// kutuphane.getAlertDialog(activity, "Sonuç : ", sonuc);

				TextView textview_student_T_sonuc = (TextView) rootview
						.findViewById(R.id.textview_student_T_sonuc);
				textview_student_T_sonuc.setText("Sonuç : " + sonuc);

			} else {
				kutuphane.getAlertDialog(activity, "Aralık Hatası",
						"Lütfen 1 ile 30 arasında değer giriniz");
			}
		} catch (Exception ex) {
			kutuphane.getAlertDialog(activity, "Giriş Hatası",
					"Lütfen doğru giriş yaptığınızdan emin olunuz.");
		}
	}

	public void btn_tukey01_Click(View vi) {

		try {

			EditText alfadeger = (EditText) rootview
					.findViewById(R.id.editTxt_tukey01);
			imm.hideSoftInputFromWindow(alfadeger.getWindowToken(), 0);
			String deger = alfadeger.getText().toString();
			int deger_int = Integer.parseInt(deger);

			if (deger_int > 0 && deger_int <= 20) {

				String[] satir = degerler_tukey01.get(deger_int).split(";");

				int sutun = spin_tukey01.getSelectedItemPosition();

				String sonuc = satir[sutun];

				// kutuphane.getAlertDialog(activity, "Sonuç : ", sonuc);
				TextView textview_tukey01_sonuc = (TextView) rootview
						.findViewById(R.id.textview_tukey01_sonuc);
				textview_tukey01_sonuc.setText("Sonuç : " + sonuc);

			} else {
				kutuphane.getAlertDialog(activity, "Aralık Hatası",
						"Lütfen 1 ile 20 arasında değer giriniz");
			}
		} catch (Exception ex) {
			kutuphane.getAlertDialog(activity, "Giriş Hatası",
					"Lütfen doğru giriş yaptığınızdan emin olunuz.");
		}

	}

	public void btn_tukey05_Click(View vi) {

		try {

			EditText alfadeger = (EditText) rootview
					.findViewById(R.id.editTxt_tukey05);
			imm.hideSoftInputFromWindow(alfadeger.getWindowToken(), 0);
			String deger = alfadeger.getText().toString();
			int deger_int = Integer.parseInt(deger);

			if (deger_int > 0 && deger_int <= 20) {

				String[] satir = degerler_tukey05.get(deger_int).split(";");

				int sutun = spin_tukey05.getSelectedItemPosition();

				String sonuc = satir[sutun];

				// kutuphane.getAlertDialog(activity, "Sonuç : ", sonuc);
				TextView textview_tukey05_sonuc = (TextView) rootview
						.findViewById(R.id.textview_tukey05_sonuc);
				textview_tukey05_sonuc.setText("Sonuç : " + sonuc);

			} else {
				kutuphane.getAlertDialog(activity, "Aralık Hatası",
						"Lütfen 1 ile 20 arasında değer giriniz");
			}
		} catch (Exception ex) {
			kutuphane.getAlertDialog(activity, "Giriş Hatası",
					"Lütfen doğru giriş yaptığınızdan emin olunuz.");
		}

	}

	public void btn_spearmankorelasyon_Click(View vi) {

		try {

			EditText alfadeger = (EditText) rootview
					.findViewById(R.id.editTxt_spearmankorelasyon);
			imm.hideSoftInputFromWindow(alfadeger.getWindowToken(), 0);
			String deger = alfadeger.getText().toString();
			int deger_int = Integer.parseInt(deger);

			if (deger_int > 4 && deger_int <= 22) {

				String[] satir = degerler_spearmankorelasyon.get(deger_int - 4)
						.split(";");

				int sutun = spin_spearmankorelasyon.getSelectedItemPosition();

				String sonuc = satir[sutun];

				// kutuphane.getAlertDialog(activity, "Sonuç : ", sonuc);

				TextView textview_spearman_sonuc = (TextView) rootview
						.findViewById(R.id.textview_spearman_sonuc);
				textview_spearman_sonuc.setText("Sonuç : " + sonuc);

			} else {
				kutuphane.getAlertDialog(activity, "Aralık Hatası",
						"Lütfen 5 ile 22 arasında değer giriniz");
			}
		} catch (Exception ex) {
			kutuphane.getAlertDialog(activity, "Giriş Hatası",
					"Lütfen doğru giriş yaptığınızdan emin olunuz.");
		}

	}

	public void btn_f_Click(View vi) {
		try {
			// LinearLayout layout = (LinearLayout) findViewById(R.id.layout_f);

			// Spinner n2spinner = (Spinner) findViewById(R.id.f_n2);
			// Spinner n1spinner=(Spinner) findViewById(R.id.f_n1);

			int deger = n2.getSelectedItemPosition();
			String[] satir = degerler_f.get(deger).split(";");
			int sutun = n1.getSelectedItemPosition();
			String sonuc = satir[sutun];
			kutuphane.getAlertDialog(activity, "Sonuç", sonuc);

		} catch (Exception e) {
			kutuphane.getAlertDialog(activity, "Hata !",
					"Lütfen değerleri kontrol ediniz.");
		}

	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		kutuphane.stopFlurry(activity);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		// activity.getMenuInflater().inflate(R.menu.menutablo, menu);
		super.onCreateOptionsMenu(menu, inflater);
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
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btn_z:
			btn_z_Click(v);
			break;
		case R.id.btn_student_t:
			btn_student_t_Click(v);
			break;
		case R.id.btn_spearmankorelasyon:
			btn_spearmankorelasyon_Click(v);
			break;
		case R.id.btn_kikare:
			btn_kikare_Click(v);
			break;
		case R.id.btn_tukey01:
			btn_tukey01_Click(v);
			break;
		case R.id.btn_tukey05:
			btn_tukey05_Click(v);
			break;
		case R.id.btn_tersz:
			btn_tersz_Click(v);
			break;
		case R.id.btn_f:
			btn_f_Click(v);
			break;
		default:
			break;
		}

	}
}
