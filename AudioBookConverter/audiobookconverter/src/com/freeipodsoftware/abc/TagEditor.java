package com.freeipodsoftware.abc;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public class TagEditor extends TagEditorGui {

	public TagEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		if (isVisible()) {
			return super.computeSize(wHint, hHint, changed);
		} else {
			return new Point(0, 0);
		}
	}

	public void setArtist(String artist) {
		artistText.setText(Util.nullToEmptyString(artist));
	}

	public void setWriter(String writer) {
		writerText.setText(Util.nullToEmptyString(writer));
	}

	public void setTitle(String title) {
		titleText.setText(Util.nullToEmptyString(title));
	}

	public void setAlbum(String album) {
		albumText.setText(Util.nullToEmptyString(album));
	}

	public void setGenre(String genre) {
		genreCombo.setText(Util.nullToEmptyString(genre));
	}

	public void setYear(String year) {
		yearText.setText(Util.nullToEmptyString(year));
	}

	public void setTrack(String track) {
		trackText.setText(Util.nullToEmptyString(track));
	}

	public void setDisc(String disc) {
		discText.setText(disc);
	}

	public void setComment(String comment) {
		commentText.setText(Util.nullToEmptyString(comment));
	}

	public void clear() {
		setArtist(""); //$NON-NLS-1$
		setWriter(""); //$NON-NLS-1$
		setTitle(""); //$NON-NLS-1$
		setAlbum(""); //$NON-NLS-1$
		setGenre(""); //$NON-NLS-1$
		setYear(""); //$NON-NLS-1$
		setTrack(""); //$NON-NLS-1$
		setDisc(""); //$NON-NLS-1$
		setComment(""); //$NON-NLS-1$
	}

	public Mp4Tags getMp4Tags() {
		Mp4Tags tags = new Mp4Tags();
		tags.setArtist(artistText.getText());
		tags.setWriter(writerText.getText());
		tags.setTitle(titleText.getText());
		tags.setAlbum(albumText.getText());
		tags.setGenre(genreCombo.getText());
		tags.setYear(yearText.getText());
		tags.setTrack(trackText.getText());
		tags.setDisc(discText.getText());
		tags.setComment(commentText.getText());
		return tags;
	}
}
