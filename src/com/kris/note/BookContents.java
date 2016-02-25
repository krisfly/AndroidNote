package com.kris.note;

import java.util.List;

public class BookContents {

	private String title;
	private List<BookContents.Chapter> chapters;

	public int getChapterCount() {
		return chapters.size();
	}

	public String getChapterFile(int position) {
		return chapters.get(position).file;
	}

	public String getTitle() {
		return title;
	}

	static class Chapter {
		String file;
		String title;
	}
}
