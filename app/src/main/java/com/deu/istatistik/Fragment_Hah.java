package com.deu.istatistik;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Fragment_Hah extends Fragment {

	private LinearLayout viewroot;
	private FragmentActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		viewroot = (LinearLayout) inflater.inflate(R.layout.hah, container,
				false);
		activity = getActivity();
		// ////////////////
		Gallery glry = (Gallery) viewroot.findViewById(R.id.gallery1);
		glry.setBackgroundColor(getResources().getColor(R.color.blue));

		Gallery1Adapter adap = new Gallery1Adapter(activity);
		glry.setAdapter(adap);

		
		////
		
		HorizontalScrollView hor = new HorizontalScrollView(activity);
		hor.computeScroll();
		////
		
		
		
		return viewroot;
	}

	public class Gallery1Adapter extends BaseAdapter {
		private Context context;
		private String[] listImages = new String[] { "sd", "sdsd" };
		private LayoutInflater inflater;

		public Gallery1Adapter(Context context) {
			this.context = context;
			inflater = (LayoutInflater) activity
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return listImages.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listImages[position];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			
			//LayoutParams layu = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			
			TextView view = new TextView(context);
			view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, 200));
			view.setText(listImages[position]);
			
			return view;
		}
	}
}
