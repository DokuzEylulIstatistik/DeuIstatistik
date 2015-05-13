package com.deu.istatistik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Fragment_Iletisim extends Fragment implements OnClickListener,
		OnFocusChangeListener {

	Kutuphane kutuphane = new Kutuphane();

	LinearLayout rootview;
	FragmentActivity activity;

	// /
	Button btn_iletisim_Gonder;
	EditText edttxt_iletisim_adsoyad;
	EditText edttxt_iletisim_eposta;
	EditText edttxt_iletisim_mesaj;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ///
		rootview = (LinearLayout) inflater.inflate(R.layout.fragment_iletisim,
				container, false);
		activity = getActivity();
		// //
		edttxt_iletisim_adsoyad = (EditText) rootview
				.findViewById(R.id.edttxt_iletisim_adsoyad);
		edttxt_iletisim_eposta = (EditText) rootview
				.findViewById(R.id.edttxt_iletisim_eposta);
		edttxt_iletisim_mesaj = (EditText) rootview
				.findViewById(R.id.edttxt_iletisim_mesaj);

		btn_iletisim_Gonder = (Button) rootview
				.findViewById(R.id.btn_iletisim_Gonder);

		// //

		setClicks();
		setFocus();
		return rootview;
	}

	private void setFocus() {
		edttxt_iletisim_adsoyad.setOnFocusChangeListener(this);
		edttxt_iletisim_eposta.setOnFocusChangeListener(this);
		edttxt_iletisim_mesaj.setOnFocusChangeListener(this);
	}

	private void setClicks() {

		btn_iletisim_Gonder.setOnClickListener(this);

	}

	private void btn_iletisim_Gonder_Click(View vi) {

		String adsoyad = edttxt_iletisim_adsoyad.getText().toString();
		String eposta = edttxt_iletisim_eposta.getText().toString();
		String mesaj = edttxt_iletisim_mesaj.getText().toString();

		if (!Kutuphane.isValidEmail(eposta)) {
			kutuphane.getAlertDialog(activity, "Hatalý Giriþ",
					"Lütfen E-Posta adresinizi kontrol ediniz.");
			return;
		}

		String[] emailList = new String[] { "aykuttasil@gmail.com",
				"hilal.aybatan@gmail.com", "ozbekgorkem@gmail.com",
				"gasongun@gmail.com" };

		// String[] emailList = new String[] { "aykuttasil@gmail.com" };
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_SUBJECT,
				activity.getString(R.string.app_name) + " - " + adsoyad);
		intent.putExtra(Intent.EXTRA_EMAIL, emailList);
		intent.putExtra(Intent.EXTRA_TEXT, mesaj + "\n\n\n E-Posta : " + eposta);
		Intent mailer = Intent.createChooser(intent, "Mail Gönder");
		startActivity(mailer);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_iletisim_Gonder:
			btn_iletisim_Gonder_Click(v);
			break;
		default:
			break;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		EditText edt = (EditText) v;
		String text = edt.getText().toString();
		if (text.contains("Ad Soyad") || text.contains("E-Posta")
				|| text.contains("Mesajýnýz ...")) {
			edt.setText("");
			
		}
		
		
	}

}
