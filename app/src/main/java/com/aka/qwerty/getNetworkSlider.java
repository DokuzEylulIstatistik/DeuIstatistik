package com.aka.qwerty;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderLayout.PresetIndicators;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.deu.istatistik.Fragment_IstatistikPaylasim_Show;
import com.deu.istatistik.Kutuphane;
import com.deu.istatistik.R;

public class getNetworkSlider {

	Kutuphane kutuphane = new Kutuphane();
	private String address = "";
	private FragmentActivity activity;
	SliderLayout la = null;

	public getNetworkSlider(String url, FragmentActivity fragmentActivity,
			View rootview) {
		this.address = url;
		this.activity = fragmentActivity;
		la = (SliderLayout) rootview.findViewById(R.id.slider);
		la.removeAllSliders();
		la.setDuration(10000);
		la.setPresetIndicator(PresetIndicators.Right_Bottom);
		la.setPresetTransformer(com.daimajia.slider.library.SliderLayout.Transformer.FlipPage);

		if (kutuphane.internetErisimi(fragmentActivity)) {
			new asyncProgress().execute(address);
		} else {
			TextSliderView slayt = new TextSliderView(fragmentActivity);
			slayt.description("DEU Ýstatistik");
			slayt.image(R.drawable.icon_100);
			slayt.setScaleType(com.daimajia.slider.library.SliderTypes.BaseSliderView.ScaleType.CenterInside);
			la.addSlider(slayt);
		}
	}

	private class asyncProgress extends
			AsyncTask<String, Integer, ArrayList<objSlider>> {

		@Override
		protected ArrayList<objSlider> doInBackground(String... params) {
			ArrayList<objSlider> listSlider = null;
			InputStream inp;
			try {
				inp = kutuphane.getdownloadUrl(address);
				String str_inp = kutuphane.getStringtoInputStream(inp);
				listSlider = getListSlider(str_inp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return listSlider;
		}

		@Override
		protected void onPostExecute(ArrayList<objSlider> result) {

			for (int i = 0; i < result.size(); i++) {

				objSlider slyt = result.get(i);

				final TextSliderView slayt = new TextSliderView(activity);
				slayt.description(slyt.getSlider_name());
				slayt.image(slyt.getSlider_src());


				slayt.setScaleType(com.daimajia.slider.library.SliderTypes.BaseSliderView.ScaleType.CenterInside);

				Bundle bundle = slayt.getBundle();
				bundle.putString("imageDesc", slyt.getSlider_desc());

				// //
				slayt.setOnSliderClickListener(new OnSliderClickListener() {

					@Override
					public void onSliderClick(BaseSliderView slider) {
						Log.i("ImageLoadListener", "onSliderClick");
						Bundle bundle = slider.getBundle();
						// /
						String imageURL = slider.getUrl();
						String imageTitle = slider.getDescription();
						String imageDesc = bundle.getString("imageDesc");
						// ///
						Log.d("onSliderClick", imageURL);
						Log.d("onSliderClick",
								String.valueOf(slider.getError()));
						Log.d("onSliderClick",
								String.valueOf(slider.getEmpty()));

						// View view = slider.getView();

						sliderClick(imageURL, imageTitle, imageDesc);

					}
				});

				la.addSlider(slayt);
			}

		}
	}

	private void sliderClick(String url, String title, String desc) {

		String TAG_TO_FRAGMENT = "tagSlider";
		Bundle args = new Bundle();
		args.putString("name", url);
		args.putString("title", title);
		args.putString("icerik", desc);
		Fragment tofragment = new Fragment_IstatistikPaylasim_Show();
		tofragment.setArguments(args);
		activity.getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, tofragment, TAG_TO_FRAGMENT)
				.addToBackStack(TAG_TO_FRAGMENT).commit();

	}

	private class objSlider implements Serializable {

		private static final long serialVersionUID = 1L;
		private int slider_ID = 0;
		private String slider_name = "slider_name";
		private String slider_desc = "slider_desc";
		private String slider_src = "slider_src";

		public int getSlider_ID() {
			return slider_ID;
		}

		public void setSlider_ID(int slider_ID) {
			this.slider_ID = slider_ID;
		}

		public String getSlider_name() {
			return slider_name;
		}

		public void setSlider_name(String slider_name) {
			this.slider_name = slider_name;
		}

		public String getSlider_desc() {
			return slider_desc;
		}

		public void setSlider_desc(String slider_desc) {
			this.slider_desc = slider_desc;
		}

		public String getSlider_src() {
			return slider_src;
		}

		public void setSlider_src(String slider_src) {
			this.slider_src = slider_src;
		}

	}

	private ArrayList<objSlider> getListSlider(String jsonString) {

		ArrayList<objSlider> list = new ArrayList<getNetworkSlider.objSlider>();

		try {
			JSONObject jsonobj = new JSONObject(jsonString);

			JSONArray jsonData = jsonobj.getJSONArray("Data");

			for (int i = 0; i < jsonData.length(); i++) {

				objSlider slider = new objSlider();
				JSONObject jo = jsonData.getJSONObject(i);

				slider.setSlider_ID(jo.getInt("slider_ID"));
				slider.setSlider_name(jo.getString(slider.getSlider_name()));
				slider.setSlider_desc(jo.getString(slider.getSlider_desc()));
				slider.setSlider_src(jo.getString(slider.getSlider_src()));

				list.add(slider);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
