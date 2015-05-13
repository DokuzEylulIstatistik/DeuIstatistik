package com.aka.qwerty;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class getUrlImage {

	private ImageView _imageview;

	public getUrlImage(ImageView imageview, String url) {
		_imageview = imageview;
		new GetXMLTask().execute(url);

	}

	private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... urls) {
			Bitmap map = null;

			map = downloadImage(urls[0]);
			
			return map;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			_imageview.setImageBitmap(result);
		}

		private Bitmap downloadImage(String url) {
			Bitmap bitmap = null;
			InputStream stream = null;
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = 1;

			try {
				stream = getHttpConnection(url);
				bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
				stream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return bitmap;
		}

		private InputStream getHttpConnection(String urlString)
				throws IOException {
			InputStream stream = null;
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			try {
				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod("GET");
				httpConnection.connect();

				if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					stream = httpConnection.getInputStream();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return stream;
		}
	}

}
