package com.kris.note;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	private static final String MODEL = "model";
	private static final String PREF_LAST_POSITION = "last_position";
	private static final String PREF_SAVE_LAST_POSITION = "save_last_position";
	private static final String PREF_KEEP_SCREEN_ON = "keep_screen_on";

	private ViewPager pager = null;
	private ContentsAdapter adapter = null;
	private ModelFragment mFrag = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupStrictMode();
		setContentView(R.layout.activity_main);
		pager = (ViewPager) findViewById(R.id.pager);
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventBus.getDefault().register(this);
		if (adapter == null) {
			mFrag = (ModelFragment) getFragmentManager().findFragmentByTag(MODEL);
			if (mFrag == null) {
				mFrag = new ModelFragment();
				getFragmentManager().beginTransaction().add(mFrag, MODEL).commit();
			} else if (mFrag.getBook() != null) {
				setupPager(mFrag.getBook());
			}
		}

		if (mFrag.getPrefs() != null) {
			pager.setKeepScreenOn(mFrag.getPrefs().getBoolean(PREF_KEEP_SCREEN_ON, false));
		}
	}

	@Override
	protected void onPause() {
		EventBus.getDefault().unregister(this);
		if (mFrag.getPrefs() != null) {
			int position = pager.getCurrentItem();
			mFrag.getPrefs().edit().putInt(PREF_LAST_POSITION, position).apply();
		}
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			pager.setCurrentItem(0, false);
			return true;
		case R.id.about:
			Intent i = new Intent(this, SimpleContentActivity.class);
			i.putExtra(SimpleContentActivity.EXTRA_FILE, "file:///android_asset/misc/about.html");
			startActivity(i);
			return true;
		case R.id.help:
			i = new Intent(this, SimpleContentActivity.class);
			i.putExtra(SimpleContentActivity.EXTRA_FILE, "file:///android_asset/misc/help.html");
			startActivity(i);
			return true;

		case R.id.settings:
			startActivity(new Intent(this, Preferences.class));
			return true;

		case R.id.notes:
			i = new Intent(this, NoteActivity.class);
			i.putExtra(NoteActivity.EXTRA_POSITION, pager.getCurrentItem());
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onEventMainThread(BookLoadedEvent event) {
		setupPager(event.getBook());
	}

	private void setupPager(BookContents contents) {
		adapter = new ContentsAdapter(this, contents);
		pager.setAdapter(adapter);
		findViewById(R.id.progressBar).setVisibility(View.GONE);
		pager.setVisibility(View.VISIBLE);
		
		SharedPreferences prefs = mFrag.getPrefs();
		if (prefs != null) {
			if (prefs.getBoolean(PREF_SAVE_LAST_POSITION, false)) {
				pager.setCurrentItem(prefs.getInt(PREF_LAST_POSITION, 0));
			}
			pager.setKeepScreenOn(prefs.getBoolean(PREF_KEEP_SCREEN_ON, false));
		}
	}

	private void setupStrictMode() {
		StrictMode.ThreadPolicy.Builder builder = new Builder().detectNetwork();
		if (BuildConfig.DEBUG) {
			builder.penaltyDeath();
		} else {
			builder.penaltyLog();
		}

		StrictMode.setThreadPolicy(builder.build());
	}

}
