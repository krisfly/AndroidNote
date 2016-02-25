package com.kris.note;

public class NoteLoadedEvent {
	private int position;
	private String prose;

	public NoteLoadedEvent(int position, String prose) {
		this.position = position;
		this.prose = prose;
	}

	public int getPositon() {
		return (position);
	}

	public String getProse() {
		return prose;
	}
}
