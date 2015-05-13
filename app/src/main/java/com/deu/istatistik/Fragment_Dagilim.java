package com.deu.istatistik;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Fragment_Dagilim extends Fragment implements OnClickListener {

	Kutuphane kutuphane = new Kutuphane();

	LinearLayout rootview;
	FragmentActivity activity;
	Resources resources;
	Spinner spinner_titleDagilimlar;
	String[] titles;

	// /////////////////

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LinearLayout vi = (LinearLayout) inflater.inflate(
				R.layout.fragment_dagilim, container, false);

		rootview = vi;
		activity = getActivity();
		resources = getResources();
		// ///////////////////////

		String actionbarSubTitle = getResources().getString(
				R.string.subtitle_dagilim);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(actionbarSubTitle);
		actionBar.setTitle("Dağılımlar");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.show();
		setHasOptionsMenu(true);
		// ///////

		titles = resources.getStringArray(R.array.title_dagilimlar);
		fillDagilimList();
		setClickListener();
		return vi;
	}

	private void fillDagilimList() {
		spinner_titleDagilimlar = (Spinner) rootview
				.findViewById(R.id.spinner_titleDagilimlar);
		ArrayAdapter<String> adap = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_dropdown_item, titles);
		spinner_titleDagilimlar.setAdapter(adap);
	}

	public void setClickListener() {

		Kutuphane.hideKeyboard(activity);
		Button btn_dagilimSecClick = (Button) rootview
				.findViewById(R.id.btn_dagilimSec);
		btn_dagilimSecClick.setOnClickListener(this);
	}

	public void btn_dagilimSecClick(View vi) {
		int position = spinner_titleDagilimlar.getSelectedItemPosition();

		setVisibility_sonucContainer(View.GONE);
		getCustomAlertDialog(position);

	}

	private void setVisibility_sonucContainer(int visibilty) {
		LinearLayout dagilim_sonucContainer = (LinearLayout) rootview
				.findViewById(R.id.dagilim_sonucContainer);
		dagilim_sonucContainer.setVisibility(visibilty);

	}

	private static String DAGILIM_CONTAINER = "dagilim_container";
	private static String DAGILIM_TEXT = "dagilim_text";
	private static String DAGILIM_EDIT = "dagilim_edit";

	private void getCustomAlertDialog(final int position) {

		AlertDialog.Builder alert = new AlertDialog.Builder(activity);

		LayoutInflater inflater = activity.getLayoutInflater();
		final LinearLayout vi_linear = (LinearLayout) inflater.inflate(
				R.layout.layout_dagilim, null);

		switch (position) {
		// Bernoulli
		case 0:
			try {
				final int rownumber = 2;
				// int colnumber = 2;
				String[] bernoulli_input = resources
						.getStringArray(R.array.bernoulli_input);

				createCustomAlertDialog(rownumber, position, bernoulli_input,
						vi_linear, R.drawable.bernoulli);

				alert.setView(vi_linear);
				alert.setPositiveButton("Hesapla",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									double[] sayidizi = new double[rownumber];
									for (int i = 0; i < rownumber; i++) {
										EditText sayi = (EditText) vi_linear
												.findViewWithTag(DAGILIM_EDIT
														+ "_"
														+ String.valueOf(i));

										double a = Double.parseDouble(sayi
												.getText().toString());
										sayidizi[i] = a;
									}

									calcBernoulli(sayidizi[0], sayidizi[1],
											position);
								} catch (Exception e) {
								}
							}

						});
				alert.setNegativeButton("İptal",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				break;
			} catch (Exception e) {
			}
			// Binom Distribution
		case 1:
			try {
				final int rownumber1 = 3;

				String[] binom_input = resources
						.getStringArray(R.array.binom_input);

				createCustomAlertDialog(rownumber1, position, binom_input,
						vi_linear, R.drawable.binom);

				alert.setView(vi_linear);

				alert.setPositiveButton("Hesapla",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								try {
									double[] sayidizi = new double[rownumber1];
									for (int i = 0; i < rownumber1; i++) {
										EditText sayi = (EditText) vi_linear
												.findViewWithTag(DAGILIM_EDIT
														+ "_"
														+ String.valueOf(i));
										double a = Double.parseDouble(sayi
												.getText().toString());
										sayidizi[i] = a;
									}

									calcBinom(sayidizi[0], sayidizi[1],
											sayidizi[2], position);
								} catch (Exception e) {
								}
							}
						});
				alert.setNegativeButton("İptal",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
			} catch (Exception e) {
			}
			break;
		// Negatif Binom Da��l�m�
		case 2:
			try {
				final int rownumber1 = 3;

				String[] negatif_binom_input = resources
						.getStringArray(R.array.negatif_binom_input);

				createCustomAlertDialog(rownumber1, position,
						negatif_binom_input, vi_linear,
						R.drawable.negatif_binom);

				alert.setView(vi_linear);

				alert.setPositiveButton("Hesapla",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								try {
									double[] sayidizi = new double[rownumber1];
									for (int i = 0; i < rownumber1; i++) {
										EditText sayi = (EditText) vi_linear
												.findViewWithTag(DAGILIM_EDIT
														+ "_"
														+ String.valueOf(i));
										double a = Double.parseDouble(sayi
												.getText().toString());
										sayidizi[i] = a;
									}

									calcNegatifBinom(sayidizi[0], sayidizi[1],
											sayidizi[2], position);
								} catch (Exception e) {
								}
							}
						});
				alert.setNegativeButton("İptal",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
			} catch (Exception e) {
			}

			break;
		// Poisson Da��l�m�
		case 3:
			try {
				final int rownumber = 2;

				String[] poisson_input = resources
						.getStringArray(R.array.poisson_input);

				createCustomAlertDialog(rownumber, position, poisson_input,
						vi_linear, R.drawable.poisson);

				alert.setView(vi_linear);
				alert.setPositiveButton("Hesapla",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									double[] sayidizi = new double[rownumber];
									for (int i = 0; i < rownumber; i++) {
										EditText sayi = (EditText) vi_linear
												.findViewWithTag(DAGILIM_EDIT
														+ "_"
														+ String.valueOf(i));

										double a = Double.parseDouble(sayi
												.getText().toString());
										sayidizi[i] = a;
									}

									calcPoisson(sayidizi[0], sayidizi[1],
											position);
								} catch (Exception e) {
								}
							}

						});
				alert.setNegativeButton("İptal",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				break;
			} catch (Exception e) {
			}
			break;
		// Geometrik Da��l�m
		case 4:
			try {
				final int rownumber = 2;

				String[] geometrik_input = resources
						.getStringArray(R.array.geometrik_input);

				createCustomAlertDialog(rownumber, position, geometrik_input,
						vi_linear, R.drawable.geometrik);

				alert.setView(vi_linear);
				alert.setPositiveButton("Hesapla",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									double[] sayidizi = new double[rownumber];
									for (int i = 0; i < rownumber; i++) {
										EditText sayi = (EditText) vi_linear
												.findViewWithTag(DAGILIM_EDIT
														+ "_"
														+ String.valueOf(i));

										double a = Double.parseDouble(sayi
												.getText().toString());
										sayidizi[i] = a;
									}

									calcGeomettik(sayidizi[0], sayidizi[1],
											position);
								} catch (Exception e) {
								}
							}

						});
				alert.setNegativeButton("İptal",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				break;
			} catch (Exception e) {
			}
			break;
		// HiperGeometrik Da��l�m
		case 5:
			try {
				final int rownumber = 4;

				String[] hipergeometrik_input = resources
						.getStringArray(R.array.hipergeometrik_input);

				createCustomAlertDialog(rownumber, position,
						hipergeometrik_input, vi_linear,
						R.drawable.hipergeometrik);

				alert.setView(vi_linear);
				alert.setPositiveButton("Hesapla",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									double[] sayidizi = new double[rownumber];
									for (int i = 0; i < rownumber; i++) {
										EditText sayi = (EditText) vi_linear
												.findViewWithTag(DAGILIM_EDIT
														+ "_"
														+ String.valueOf(i));

										double a = Double.parseDouble(sayi
												.getText().toString());
										sayidizi[i] = a;
									}

									calcHiperGeomettik(sayidizi[0],
											sayidizi[1], sayidizi[2],
											sayidizi[3], position);
								} catch (Exception e) {
								}
							}

						});
				alert.setNegativeButton("İptal",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				break;
			} catch (Exception e) {
			}
			break;

		default:
			break;
		}

		alert.setTitle(titles[position]);
		alert.create().show();

	}

	private void setSonucContainer(double sonuc, String dagilimTitle,
			String dagilimParametre, String ortvar, int imageID) {
		TextView text_sonuc = (TextView) rootview
				.findViewById(R.id.textview_dagilimSonuc);
		String str_sonuc = new DecimalFormat("#.#####").format(sonuc);
		text_sonuc.setText(str_sonuc);

		// //

		TextView dagilimBaslik = (TextView) rootview
				.findViewById(R.id.dagilimBaslik);
		dagilimBaslik.setText(dagilimTitle);

		// /////
		TextView dagilimParametreler = (TextView) rootview
				.findViewById(R.id.dagilimParametreler);
		dagilimParametreler.setText(dagilimParametre);
		// //

		TextView textview_dagilimOrtVar = (TextView) rootview
				.findViewById(R.id.textview_dagilimOrtVar);
		textview_dagilimOrtVar.setText(ortvar);

		ImageView dagilim_image = (ImageView) rootview
				.findViewById(R.id.dagilim_image);
		dagilim_image.setImageResource(imageID);

	}

	private void calcHiperGeomettik(double N, double M, double n, double x,
			int titlePosition) {
		try {
			if (N < M && M < x && N < n) {
				throw new Exception();
			}

			String[] hipergeometrik_input = resources
					.getStringArray(R.array.hipergeometrik_input);
			// //
			double sonuc = ((Kombinasyon.Hesapla((int) M, (int) x) * Kombinasyon
					.Hesapla((int) (N - M), (int) (n - x))) / Kombinasyon
					.Hesapla((int) N, (int) n));

			String dagilimParametreler = hipergeometrik_input[0] + " : "
					+ String.valueOf(N) + "\n" + hipergeometrik_input[1]
					+ " : " + String.valueOf(M) + "\n"
					+ hipergeometrik_input[2] + " : " + String.valueOf(n)
					+ "\n" + hipergeometrik_input[3] + " : "
					+ String.valueOf(x);
			// ///
			double ortalama = n * M / N;
			double varyans = n * M * (N - M) * (N - n)
					/ (Math.pow(N, 2) * (N - 1));
			String ortvar = "Ortalama : "
					+ new DecimalFormat("0.#####").format(ortalama) + "\n"
					+ "Varyans : "
					+ new DecimalFormat("0.#####").format(varyans);
			// //////////////////////////////////

			setSonucContainer(sonuc, titles[titlePosition],
					dagilimParametreler, ortvar, R.drawable.hipergeometrik);
			setVisibility_sonucContainer(View.VISIBLE);
		} catch (Exception e) {
			kutuphane.getAlertDialog(activity, "Hatalı Giriş",
					"N > M \n M > x \n N > n olmalıdır.");
		}

	}

	private void calcGeomettik(double p, double x, int titlePosition) {
		try {
			String[] geometrik_input = resources
					.getStringArray(R.array.geometrik_input);
			// //
			double sonuc = p * Math.pow((1 - p), (x - 1));

			String dagilimParametreler = geometrik_input[0] + " : "
					+ String.valueOf(p) + "\n" + geometrik_input[1] + " : "
					+ String.valueOf(x);
			// ///
			double ortalama = (double) (1 / p);
			double varyans = (1 - p) / (double) Math.pow(p, 2);
			String ortvar = "Ortalama : "
					+ new DecimalFormat("0.#####").format(ortalama) + "\n"
					+ "Varyans : "
					+ new DecimalFormat("0.#####").format(varyans);
			// //////////////////////////////////

			setSonucContainer(sonuc, titles[titlePosition],
					dagilimParametreler, ortvar, R.drawable.geometrik);
			setVisibility_sonucContainer(View.VISIBLE);
		} catch (Exception e) {

		}
	}

	private void calcPoisson(double lamda, double x, int titlePosition) {
		try {
			String[] poisson_input = resources
					.getStringArray(R.array.poisson_input);
			// //
			double sonuc = Math.pow(lamda, x) * Math.exp(-lamda)
					/ Kombinasyon.nFaktoriyel((int) x);

			String dagilimParametreler = poisson_input[0] + " : "
					+ String.valueOf(lamda) + "\n" + poisson_input[1] + " : "
					+ String.valueOf(x);
			// ///
			double ortalama = lamda;
			double varyans = lamda;
			String ortvar = "Ortalama : "
					+ new DecimalFormat("0.#####").format(ortalama) + "\n"
					+ "Varyans : "
					+ new DecimalFormat("0.#####").format(varyans);
			// //////////////////////////////////

			setSonucContainer(sonuc, titles[titlePosition],
					dagilimParametreler, ortvar, R.drawable.poisson);
			setVisibility_sonucContainer(View.VISIBLE);
		} catch (Exception e) {

		}
	}

	private void calcNegatifBinom(double x, double p, double k,
			int titlePosition) {
		try {

			if (x < k) {
				throw new Exception();
			}
			String[] negatif_binom_input = resources
					.getStringArray(R.array.negatif_binom_input);
			// //

			double sonuc = Kombinasyon.Hesapla((int) (x - 1), (int) (k - 1))
					* Math.pow(p, k) * Math.pow((1 - p), (x - k));

			String dagilimParametreler = negatif_binom_input[0] + " : "
					+ String.valueOf(x) + "\n" + negatif_binom_input[1] + " : "
					+ String.valueOf(p) + "\n" + negatif_binom_input[2] + " : "
					+ String.valueOf(k);

			// /
			double ortalama = k / p;

			double varyans = k * (1 - p) / Math.pow(p, 2);

			String ortvar = "Ortalama : "
					+ new DecimalFormat("0.#####").format(ortalama) + "\n"
					+ "Varyans : "
					+ new DecimalFormat("0.#####").format(varyans);
			// /

			setSonucContainer(sonuc, titles[titlePosition],
					dagilimParametreler, ortvar, R.drawable.negatif_binom);
			setVisibility_sonucContainer(View.VISIBLE);
		} catch (Exception e) {
			kutuphane.getAlertDialog(activity, "Hatalı Giriş",
					"x > k olmalıdır.");
		}
	}

	private void calcBernoulli(double p, double x, int titlePosition) {
		try {

			if (x != 0 && x != 1) {
				throw new Exception();
			}

			String[] bernoulli_input = resources
					.getStringArray(R.array.bernoulli_input);
			// //
			double sonuc = Math.pow(p, x) * Math.pow((1 - p), (1 - x));
			String dagilimParametreler = bernoulli_input[0] + " : "
					+ String.valueOf(p) + "\n" + bernoulli_input[1] + " : "
					+ String.valueOf(x);
			// ///
			double ortalama = p;
			double varyans = p * (1 - p);
			String ortvar = "Ortalama : "
					+ new DecimalFormat("0.#####").format(ortalama) + "\n"
					+ "Varyans : "
					+ new DecimalFormat("0.#####").format(varyans);
			// //////////////////////////////////

			setSonucContainer(sonuc, titles[titlePosition],
					dagilimParametreler, ortvar, R.drawable.bernoulli);
			setVisibility_sonucContainer(View.VISIBLE);
		} catch (Exception e) {
			kutuphane.getAlertDialog(activity, "Hatalı Giriş",
					"x = 1 veya x = 0 olmalıdır.");
		}
	}

	private void calcBinom(double n, double p, double x, int titlePosition) {

		try {
			if (n < x) {
				throw new Exception();
			}

			String[] binom_input = resources
					.getStringArray(R.array.binom_input);
			// //

			double sonuc = Kombinasyon.Hesapla((int) n, (int) x)
					* (Math.pow(p, x) * Math.pow((1 - p), (n - x)));

			String dagilimParametreler = binom_input[0] + " : "
					+ String.valueOf(n) + "\n" + binom_input[1] + " : "
					+ String.valueOf(p) + "\n" + binom_input[2] + " : "
					+ String.valueOf(x);

			// /
			double ortalama = n * p;

			double varyans = n * p * (1 - p);

			String ortvar = "Ortalama : "
					+ new DecimalFormat("0.#####").format(ortalama) + "\n"
					+ "Varyans : "
					+ new DecimalFormat("0.#####").format(varyans);
			// /

			setSonucContainer(sonuc, titles[titlePosition],
					dagilimParametreler, ortvar, R.drawable.binom);
			setVisibility_sonucContainer(View.VISIBLE);
		} catch (Exception e) {
			kutuphane.getAlertDialog(activity, "Hatalı Giriş",
					"n > x olmalıdır.");
		}
	}

	private LinearLayout createCustomAlertDialog(int rownumber, int position,
			String[] input_Titles, LinearLayout vi_linear, int imageID) {

		LinearLayout[] root = new LinearLayout[rownumber + 1];
		TextView[] text = new TextView[rownumber];
		EditText[] edit = new EditText[rownumber];

		for (int i = 0; i < rownumber; i++) {
			root[i] = new LinearLayout(activity);
			root[i].setTag(DAGILIM_CONTAINER + "_" + String.valueOf(i));
			root[i].setWeightSum(2);
			root[i].setPadding(5, 5, 5, 5);

			// ///////////////////

			text[i] = (TextView) activity.getLayoutInflater().inflate(
					R.layout.dagilim_customtextview, null);
			text[i].setTag(DAGILIM_TEXT + "_" + String.valueOf(i));
			text[i].setText(input_Titles[i]);
			// text[i].setGravity(Gravity.CENTER);
			text[i].setLayoutParams(new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1f));

			root[i].addView(text[i]);
			// ///////////////////
			edit[i] = (EditText) activity.getLayoutInflater().inflate(
					R.layout.dagilim_custom_edittext, null);
			edit[i].setTag(DAGILIM_EDIT + "_" + String.valueOf(i));
			if (input_Titles[i].equals("p")) {
				edit[i].setFilters(new InputFilter[] { new InputFilterMinMax(0,
						1) });
			}
			edit[i].setLayoutParams(new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1f));

			root[i].addView(edit[i]);

			// /////////////////

			vi_linear.addView(root[i]);
		}
		// // Da��l�m Resim Ekleme
		ImageView dagilim_image = new ImageView(activity);
		dagilim_image.setImageResource(imageID);
		root[rownumber] = new LinearLayout(activity);
		root[rownumber].setTag(DAGILIM_CONTAINER + "_"
				+ String.valueOf(rownumber));
		root[rownumber].setWeightSum(2);
		root[rownumber].setPadding(5, 5, 5, 5);
		root[rownumber].addView(dagilim_image);
		vi_linear.addView(root[rownumber]);
		// //
		return vi_linear;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_dagilimSec:
			btn_dagilimSecClick(v);
			break;

		default:
			break;
		}

	}

	public static class Kombinasyon {

		private static double nFaktoriyel(int a) {
			double nFakt = 1;
			for (int i = 1; i <= a; i++) {
				nFakt = nFakt * i;
			}

			return nFakt;
		}

		public static double Hesapla(int a, int b) {
			double sonuc;
			sonuc = nFaktoriyel(a) / (nFaktoriyel(b) * nFaktoriyel(a - b));
			return sonuc;
		}
	}
}
