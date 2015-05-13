package com.deu.istatistik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		// if (android.os.Build.VERSION.SDK_INT > 9) {
		// StrictMode.ThreadPolicy policy = new
		// StrictMode.ThreadPolicy.Builder()
		// .permitAll().build();
		// StrictMode.setThreadPolicy(policy);
		// }
	}

	public class loadFragment {
		private FragmentManager fragmentManager = getSupportFragmentManager();
		private Fragment fragment;
		private android.support.v4.app.FragmentTransaction transaction;
		private int animationID_1;
		private int animationID_2;

		public loadFragment() {

			this.animationID_1 = android.R.anim.slide_in_left;
			this.animationID_2 = android.R.anim.slide_out_right;
			this.transaction = fragmentManager.beginTransaction();
		}

		public void setFragment(Fragment frg) {
			this.fragment = frg;
		}

		public void setAnimation(int anim1, int anim2) {
			this.animationID_1 = anim1;
			this.animationID_2 = anim2;
		}

		public void commitFragment() {

			transaction.setCustomAnimations(animationID_1, animationID_2)
					.replace(R.id.container, fragment).commit();
		}

		public void addToBackStack() {
			transaction.addToBackStack("backstage");
		}

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {

		loadFragment load = new loadFragment();

		if (position == 0) {

			load.setFragment(new Fragment_Main());
			load.setAnimation(android.R.anim.fade_in, android.R.anim.fade_out);
			load.commitFragment();

		} else if (position == 1) {
			load.setFragment(new Fragment_OrtHesap());
			// load.setAnimation(android.R.anim.fade_in,
			// android.R.anim.fade_out);
			load.commitFragment();

		} else if (position == 3) {
			load.setFragment(new Fragment_DescStats());
			load.commitFragment();

		} else if (position == 2) {

			Intent intent = new Intent(this, HaritaActivity.class);

			startActivity(intent);

		} else if (position == 4) {
			load.setFragment(new FragmentTablo());
			load.commitFragment();

		} else if (position == 5) {
			load.setFragment(new Fragment_Matris());
			load.commitFragment();

		} else if (position == 6) {
			load.setFragment(new Fragment_Dagilim());
			load.commitFragment();

		} else if (position == 7) {
			load.setFragment(new Fragment_IstatistikPaylasim());
			load.commitFragment();

		}

	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			// getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();

			// Kutuphane kutuphane = new Kutuphane();
			// kutuphane.getAlertDialog(this, "baslik", "mesaj");
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			Kutuphane kutuphane = new Kutuphane();
			kutuphane.getAlertDialog(this, "baslik", "mesaj");

			return true;
		case R.id.action_contact:
			// onNavigationDrawerItemSelected(3);

			mNavigationDrawerFragment.mDrawerLayout.closeDrawers();
			mNavigationDrawerFragment.mDrawerToggle
					.onDrawerClosed(mNavigationDrawerFragment.mDrawerLayout);
			Fragment_Iletisim iletisimfragment = new Fragment_Iletisim();
			getSupportFragmentManager()
					.beginTransaction()
					.setCustomAnimations(android.R.anim.slide_in_left,
							android.R.anim.slide_out_right)
					.replace(R.id.container, iletisimfragment).commit();
			break;
		default:

			break;
		}
		// if (id == R.id.action_settings) {
		// Kutuphane kutuphane = new Kutuphane();
		// kutuphane.getAlertDialog(this, "baslik", "mesaj");
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
			this.finish();
		} else {
			getSupportFragmentManager().popBackStack();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(
						"Uygulamadan çýkmak istediðinize emin misiniz?")
						.setCancelable(false)
						.setPositiveButton("Evet",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										finish();
									}
								})
						.setNegativeButton("Hayýr",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			} else {

				getSupportFragmentManager().popBackStack();
				return false;
			}
		} else {

			return super.onKeyDown(keyCode, event);
		}

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.tablo, container, false);
			// TextView textView = (TextView) rootView
			// .findViewById(R.id.section_label);
			// textView.setText(Integer.toString(getArguments().getInt(
			// ARG_SECTION_NUMBER)));
			return rootView;

			// View rootView = inflater.inflate(R.layout.fragment_main,
			// container,
			// false);
			// TextView textView = (TextView) rootView
			// .findViewById(R.id.section_label);
			// textView.setText(Integer.toString(getArguments().getInt(
			// ARG_SECTION_NUMBER)));
			// return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
