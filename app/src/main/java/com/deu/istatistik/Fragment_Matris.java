package com.deu.istatistik;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Fragment_Matris extends Fragment implements
		android.view.View.OnClickListener {

	Kutuphane kutuphane = new Kutuphane();

	LinearLayout rootview;
	FragmentActivity activity;
	Resources resources;

	// LayoutInflater inflater;
	private android.support.v7.app.ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout vi = (LinearLayout) inflater.inflate(
				R.layout.layout_matris, container, false);
		rootview = vi;
		activity = getActivity();
		resources = getResources();
		// inflater = (LayoutInflater) activity
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// /////////////////////

		String actionbarSubTitle = getResources().getString(
				R.string.subtitle_matris);

		android.support.v7.app.ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(actionbarSubTitle);
		actionBar.setTitle("Not Hesaplama");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.show();
		// /////////////////
		setHasOptionsMenu(true);
		setClickListener();

		IslemListDoldur();
		return vi;

	}

	private void setClickListener() {
		Button btn_islemsec = (Button) rootview.findViewById(R.id.btn_islemSec);
		btn_islemsec.setOnClickListener(this);

		Button btn_islemyap = (Button) rootview.findViewById(R.id.btn_islemyap);
		btn_islemyap.setOnClickListener(this);

	}

	double[][] matrix;
	double[][] birimmatrix;

	private void btn_islemyapClick() {

		Kutuphane.hideKeyboard(activity);
		if (!arrayMatrix()) {
			kutuphane.getAlertDialog(activity, "Hata",
					"Lütfen düzgün giriş yaptığınızdan emin olunuz.");
			return;
		}
		arraybirimMatris();

		int row = kutuphane.getsharedPreferenceInt(activity, SATIR);
		int col = kutuphane.getsharedPreferenceInt(activity, SUTUN);

		double d, k;
		for (int i = 0; i < row; i++) {
			d = matrix[i][i];

			for (int j = 0; j < col; j++) {
				matrix[i][j] = matrix[i][j] / d;
				birimmatrix[i][j] = birimmatrix[i][j] / d;
			}
			for (int x = 0; x < row; x++) {
				if (x != i) {
					k = matrix[x][i];
					for (int j = 0; j < row; j++) {
						matrix[x][j] = matrix[x][j] - (matrix[i][j] * k);
						birimmatrix[x][j] = birimmatrix[x][j]
								- (birimmatrix[i][j] * k);
					}

				}
			}
		}

		MatrisTextViewEkle();
		// for (int i = 0; i < row; i++) {
		// for (int j = 0; j < row; j++) {
		// // System.out.println("sdds");
		//
		// System.out.print(birimmatrix[i][j]);
		// System.out.print("\n");
		// }
		// }

	}

	private void btn_islemSecClick(View vi) {
		Spinner spin = (Spinner) rootview.findViewById(R.id.spinner_islemlist);
		int selectposition = spin.getSelectedItemPosition();

		Button btn_islemyap = (Button) rootview.findViewById(R.id.btn_islemyap);
		btn_islemyap.setVisibility(View.INVISIBLE);
		switch (selectposition) {
		// �ki Matrisi Topla
		case 1:
			getCustomAlertDialog(1);
			break;
		case 2:
			getCustomAlertDialog(2);
			break;
		case 3:
			getCustomAlertDialog(3);
			break;
		case 4:
			getCustomAlertDialog(4);
			break;
		default:
			break;
		}

	}

	private static String SATIR = "satir";
	private static String SUTUN = "sutun";

	private void getCustomAlertDialog(int position) {
		LayoutInflater inflater = activity.getLayoutInflater();
		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		final Button btn_islemyap = (Button) rootview
				.findViewById(R.id.btn_islemyap);

		switch (position) {
		// Matrisin Tersini Al
		case 1:

			// Kutuphane.InputFilterMinMax input = new InputFilterMinMax(1, 5);

			alert.setTitle("Matris");
			alert.setCancelable(true);
			View vi = inflater.inflate(R.layout.customview_alertmatrislayout,
					null);

			final EditText satir = (EditText) vi
					.findViewById(R.id.edittext_satir);
			satir.setFilters(new InputFilter[] { new InputFilterMinMax(2, 5) });
			final EditText sutun = (EditText) vi
					.findViewById(R.id.edittext_sutun);
			sutun.setFilters(new InputFilter[] { new InputFilterMinMax(2, 5) });

			alert.setPositiveButton("İlerle", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (satir.getText().toString().equals("")
							|| sutun.getText().toString().equals("")) {
						getCustomAlertDialog(1);
						btn_islemyap.setVisibility(View.GONE);
						return;
					}

					// //////
					int satir_ = Integer.parseInt(satir.getText().toString());
					int sutun_ = Integer.parseInt(sutun.getText().toString());
					kutuphane.sharedPreferencesEdit(activity, SATIR, satir_);
					kutuphane.sharedPreferencesEdit(activity, SUTUN, sutun_);
					if (satir_ == sutun_ && satir_ != 0 && sutun_ != 0) {

						MatrisEditTextEkle(satir_, sutun_);
						dialog.dismiss();
						btn_islemyap.setVisibility(View.VISIBLE);
					} else {
						getCustomAlertDialog(1);
					}

				}
			});

			alert.setView(vi);

			break;

		default:
			break;
		}

		alert.create().show();

	}

	private static String MATRIS_NUM_BOX = "matris_num_box";
	private static String MATRIS_ROW = "matris_row";

	private void MatrisEditTextEkle(int row, int col) {

		LinearLayout linearlayout_matrisContainer = (LinearLayout) rootview
				.findViewById(R.id.linearlayout_matrisContainer);

		linearlayout_matrisContainer.removeAllViews();

		LinearLayout[] root = new LinearLayout[row];
		EditText[] edit = new EditText[row * col];
		// LinearLayout root = new LinearLayout(activity);

		for (int i = 0; i < row; i++) {

			root[i] = new LinearLayout(activity);
			root[i].setTag(MATRIS_ROW + "_" + String.valueOf(i));
			root[i].setWeightSum(col);
			root[i].setPadding(5, 5, 5, 5);

			for (int j = 0; j < col; j++) {
				edit[j] = (EditText) activity.getLayoutInflater().inflate(
						R.layout.matris_number_edittext, null);
				edit[j].setTag(MATRIS_NUM_BOX + "_" + String.valueOf(j));
				edit[j].setLayoutParams(new LayoutParams(0,
						LayoutParams.WRAP_CONTENT, 1f));

				root[i].addView(edit[j]);

			}
			linearlayout_matrisContainer.addView(root[i]);
		}

	}

	private static String TEXT_MATRIS_NUM_BOX = "text_matris_num_box";
	private static String TEXT_MATRIS_ROW = "text_matris_row";

	private void MatrisTextViewEkle() {

		// ////////////
		int row = kutuphane.getsharedPreferenceInt(activity, SATIR);
		int col = kutuphane.getsharedPreferenceInt(activity, SUTUN);
		// ////////////
		LinearLayout matris_sonucContainer = (LinearLayout) rootview
				.findViewById(R.id.matris_sonucContainer);

		matris_sonucContainer.removeAllViews();

		// /////////////
		LinearLayout[] root = new LinearLayout[row];
		TextView[] text = new TextView[row * col];

		for (int i = 0; i < row; i++) {

			root[i] = new LinearLayout(activity);
			root[i].setTag(TEXT_MATRIS_NUM_BOX + "_" + String.valueOf(i));
			root[i].setWeightSum(col);
			root[i].setPadding(5, 5, 5, 5);

			for (int j = 0; j < col; j++) {
				text[j] = (TextView) activity.getLayoutInflater().inflate(
						R.layout.matris_number_textview, null);
				text[j].setTag(TEXT_MATRIS_ROW + "_" + String.valueOf(j));
				text[j].setLayoutParams(new LayoutParams(0,
						LayoutParams.WRAP_CONTENT, 1f));

				Double d_deger = birimmatrix[i][j];

				if (Double.isInfinite(d_deger) || Double.isNaN(d_deger)) {
					kutuphane.getAlertDialog(activity, "Bilgilendirme",
							"Matrisin tersi bulunmamaktadır.");
					return;
				}
				String deger = new DecimalFormat("0.#####")
						.format(birimmatrix[i][j]);
				text[j].setText(deger);

				root[i].addView(text[j]);

			}
			matris_sonucContainer.addView(root[i]);
		}
		// //////////////
	}

	private boolean arrayMatrix() {
		try {
			int row = kutuphane.getsharedPreferenceInt(activity, SATIR);
			int col = kutuphane.getsharedPreferenceInt(activity, SUTUN);

			matrix = new double[row][col];

			LinearLayout satirlar = (LinearLayout) rootview
					.findViewById(R.id.linearlayout_matrisContainer);

			for (int i = 0; i < row; i++) {
				LinearLayout matrissatir = (LinearLayout) satirlar
						.findViewWithTag(MATRIS_ROW + "_" + String.valueOf(i));
				for (int j = 0; j < col; j++) {
					EditText number = (EditText) matrissatir
							.findViewWithTag(MATRIS_NUM_BOX + "_"
									+ String.valueOf(j));

					matrix[i][j] = Double.parseDouble(number.getText()
							.toString());

				}
			}
			return true;
		} catch (Exception e) {
			// kutuphane.getAlertDialog(activity, "baslik", "mesaj");

			return false;
		}
	}

	private void arraybirimMatris() {
		int row = kutuphane.getsharedPreferenceInt(activity, SATIR);
		int col = kutuphane.getsharedPreferenceInt(activity, SUTUN);

		birimmatrix = new double[row][col];

		for (int i = 0; i < row; i++) {

			for (int j = 0; j < col; j++) {
				if (i == j) {
					birimmatrix[i][j] = 1;
				} else {
					birimmatrix[i][j] = 0;
				}
			}
		}
	}

	private ArrayList<String> matrisIslemListMap() {

		ArrayList<String> sonuc = new ArrayList<String>();
		sonuc.add("Lütfen işlem seçiniz.");

		String[] islemlistesi = resources.getStringArray(R.array.islemlist);
		// String[] islemlistesikey = resources
		// .getStringArray(R.array.islemlist_val);

		for (int i = 0; i < islemlistesi.length; i++) {
			sonuc.add(islemlistesi[i]);

		}
		return sonuc;
	}

	private void IslemListDoldur() {

		Spinner islemlistspinner = (Spinner) rootview
				.findViewById(R.id.spinner_islemlist);
		ArrayAdapter<String> adap = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_dropdown_item,
				matrisIslemListMap());
		islemlistspinner.setAdapter(adap);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_islemSec:
			btn_islemSecClick(v);
			break;
		case R.id.btn_islemyap:
			btn_islemyapClick();
			break;
		default:
			break;
		}

	}

}
