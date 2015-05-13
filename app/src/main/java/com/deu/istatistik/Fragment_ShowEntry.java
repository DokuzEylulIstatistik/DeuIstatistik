package com.deu.istatistik;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

public class Fragment_ShowEntry extends Fragment {

	Activity activity;
	RelativeLayout viewroot;
	Kutuphane kutuphane = new Kutuphane();

	@SuppressLint({ "SetJavaScriptEnabled" })
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		viewroot = (RelativeLayout) inflater.inflate(
				R.layout.fragment_show_entry, container, false);
		activity = getActivity();
		// ////////////

		Bundle args = getArguments();
		String baslik = args.getString("baslik");
		String icerik = args.getString("icerik");

		WebView web = (WebView) viewroot.findViewById(R.id.showentryWebView);
		web.setContentDescription("Description");
		web.setAlwaysDrawnWithCacheEnabled(true);

		WebSettings settings = web.getSettings();
		settings.setJavaScriptEnabled(true);
		// settings.setBuiltInZoomControls(true);
		settings.setLoadsImagesAutomatically(true);

		showtheWebView(baslik, icerik, web);

		return viewroot;
	}

	private void showtheWebView(String baslik, String icerik, WebView web) {

		StringBuilder sb = new StringBuilder();
		sb.append("<HTML><HEAD><LINK href=\"css/bootstrap.min.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body><div class=\"container\">");
		sb.append("<h3>DEU - <small>" + baslik + "</small></h3><hr/>");
		sb.append(icerik.toString());
		sb.append("</div></body></HTML>");
		web.loadDataWithBaseURL("file:///android_asset/", sb.toString(),
				"text/html", "utf-8", null);
	}

}
