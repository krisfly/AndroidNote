package com.kris.note;

import android.app.Activity;
import android.app.Fragment;
import android.support.v13.app.FragmentStatePagerAdapter;

public class ContentsAdapter extends FragmentStatePagerAdapter {

	private BookContents contents = null;

	public ContentsAdapter(Activity ctxt, BookContents contents) {
		super(ctxt.getFragmentManager());
		this.contents = contents;
	}

	@Override
	public Fragment getItem(int arg0) {
		String path = contents.getChapterFile(arg0);
		return SimpleContentFragment.newInstance("file:///android_asset/book/" + path);
	}

	@Override
	public int getCount() {
		return contents.getChapterCount();
	}

}
