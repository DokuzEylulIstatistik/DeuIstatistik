package com.deu.istatistik;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;

public class Fragment_IstatistikPaylasim_Show extends Fragment {

	Activity activity;
	LinearLayout viewroot;
	Kutuphane kutuphane = new Kutuphane();

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		viewroot = (LinearLayout) inflater.inflate(
				R.layout.fragement_istatistikpaylasim_show, container, false);
		activity = getActivity();
		// ////////////

		Bundle args = getArguments();
		String name = args.getString("name");
		String icerik = args.getString("icerik");
		String title = args.getString("title");

		WebView web = (WebView) viewroot
				.findViewById(R.id.fragmentshowentry_webview);
		web.setContentDescription("Description");
		web.setAlwaysDrawnWithCacheEnabled(true);

		// //////////////
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setBuiltInZoomControls(true);
		web.getSettings().setLoadsImagesAutomatically(true);
		web.getSettings().setAllowFileAccess(true);

		// /////////////

		web.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

//				injectScriptFile(view, "js/jquery-2.1.1.min.js");
//				injectScriptFile(view, "js/bootstrap.min.js");

			}
			
			

			private void injectScriptFile(WebView view, String scriptFile) {
				InputStream input;
				try {
					input = getActivity().getAssets().open(scriptFile);
					byte[] buffer = new byte[input.available()];
					input.read(buffer);
					input.close();

					// String-ify the script byte-array using BASE64 encoding
					// !!!
					String encoded = Base64.encodeToString(buffer,
							Base64.NO_WRAP);
					view.loadUrl("javascript:(function() {"
							+ "var parent = document.getElementsByTagName('head').item(0);"
							+ "var script = document.createElement('script');"
							+ "script.type = 'text/javascript';"
							+
							// Tell the browser to BASE64-decode the string into
							// your script !!!
							"script.innerHTML = window.atob('" + encoded
							+ "');" + "parent.appendChild(script)" + "})()");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		web.setWebChromeClient(new WebChromeClient());

		// web.loadUrl("https://www.google.com");
		showtheWebView(icerik, title, web);
		// showAnimButton();

		return viewroot;
	}

	private void showtheWebView(String icerik, String title, WebView web) {
		StringBuilder sb = new StringBuilder();
		sb.append("<HTML><HEAD>"
				+ "<LINK href=\"css/bootstrap.min.css\" type=\"text/css\" rel=\"stylesheet\"/>"
				// +
				// "<SCRIPT TYPE=\"text/javascript\" SRC=\"js/jquery-2.1.1.min.js\" />"
				// +
				// "<SCRIPT TYPE=\"text/javascript\" SRC=\"js/bootstrap.min.js\" />"
				+ "</HEAD>" + "<body>" + "<div class=\"container\">");
		sb.append("<h3>DEU - <small>" + title + "</small></h3><hr/>");
		sb.append(icerik.toString());
		sb.append("</div></body></HTML>");
		web.loadDataWithBaseURL("file:///android_asset/", sb.toString(),
				"text/html", "utf-8", null);
	}

	// private void showAnimButton() {
	//
	// LinearLayout linl = (LinearLayout) viewroot
	// .findViewById(R.id.dnm1layout);
	//
	// Button button1 = (Button) activity.getLayoutInflater().inflate(
	// R.layout.dnm1button, null);
	//
	// // Button button1 = (Button)
	// // activity.findViewById(R.id.dnm1ButtonTikla);
	// Animation anim = AnimationUtils.loadAnimation(activity, R.anim.dnm1);
	// linl.addView(button1);
	//
	// button1.startAnimation(anim);
	// }
}
