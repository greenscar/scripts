package com.freeipodsoftware.abc;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.v1.ID3V1Tag;
import org.blinkenlights.jid3.v2.ID3V2Tag;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

public class Util {

	public static String nullToEmptyString(String string) {
		if (string == null) {
			return ""; //$NON-NLS-1$
		}
		return string;
	}

	public static boolean hasText(String text) {
		if (text == null)
			return false;
		return text.trim().length() > 0;
	}

	public static void centerDialog(Shell parent, Shell shell) {
		Rectangle parentSize = parent.getBounds();
		Rectangle mySize = shell.getBounds();

		int locationX, locationY;
		locationX = (parentSize.width - mySize.width) / 2 + parentSize.x;
		locationY = (parentSize.height - mySize.height) / 2 + parentSize.y;

		shell.setLocation(new Point(locationX, locationY));
	}

	/**
	 * Checks, if the provided filename doesn't exist yet. If it exists, then an
	 * (n) is added to the filename to make the filename unique.
	 * 
	 * @param filename
	 * @return
	 */
	public static String makeFilenameUnique(String filename) {
		Pattern extPattern = Pattern.compile("\\.(\\w+)$"); //$NON-NLS-1$
		Matcher extMatcher = extPattern.matcher(filename);
		if (extMatcher.find()) {
			try {
				String extension = extMatcher.group(1);

				File outputFile = new File(filename);
				while (outputFile.exists()) {

					Pattern pattern = Pattern.compile("(?i)(.*)\\((\\d+)\\)\\." //$NON-NLS-1$
							+ extension + "$"); //$NON-NLS-1$
					Matcher matcher = pattern.matcher(filename);
					if (matcher.find()) {
						filename = matcher.group(1)
								+ "(" //$NON-NLS-1$
								+ (Integer.parseInt(matcher.group(2)) + 1)
								+ ")." + extension; //$NON-NLS-1$
					} else {
						filename = filename.replaceAll("." + extension + "$", //$NON-NLS-1$ //$NON-NLS-2$
								"(1)." + extension); //$NON-NLS-1$
					}

					outputFile = new File(filename);
				}
				return filename;
			} catch (Exception e) {
				throw new RuntimeException(Messages
						.getString("Util.connotUseFilename") + " " + filename); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			throw new RuntimeException(Messages
					.getString("Util.connotUseFilename") + " " + filename //$NON-NLS-1$ //$NON-NLS-2$
					+ " (2)"); //$NON-NLS-1$
		}
	}

	public static Mp4Tags readTagsFromInputFile(String filename) {
		Mp4Tags tags = new Mp4Tags();
		try {
			MP3File file = new MP3File(new File(filename));

			ID3V2Tag v2Tag = file.getID3V2Tag();
			if (v2Tag != null) {
				importV2Tags(tags, v2Tag);
			} else {
				ID3V1Tag v1Tag = file.getID3V1Tag();
				if (v1Tag != null) {
					importV1Tags(tags, v1Tag);
				}
			}

		} catch (Exception e) {
		}
		return tags;
	}

	private static void importV1Tags(Mp4Tags tags, ID3V1Tag v1Tag) {
		tags.setArtist(filterTag(v1Tag.getArtist()));
		tags.setTitle(filterTag(v1Tag.getTitle()));
		tags.setAlbum(filterTag(v1Tag.getAlbum()));
		tags.setGenre(filterTag(v1Tag.getGenre().toString()));
		tags.setYear(filterTag(v1Tag.getYear()));
		tags.setComment(filterTag(v1Tag.getComment()));
	}

	private static void importV2Tags(Mp4Tags tags, ID3V2Tag v2Tag)
			throws ID3Exception {
		tags.setArtist(filterTag(v2Tag.getArtist()));
		tags.setTitle(filterTag(v2Tag.getTitle()));
		tags.setAlbum(filterTag(v2Tag.getAlbum()));
		tags.setGenre(filterTag(v2Tag.getGenre()));
		tags.setYear(String.valueOf(v2Tag.getYear()));
		if (v2Tag.getTrackNumber() > 0 && v2Tag.getTotalTracks() > 0) {
			tags.setTrack("" + v2Tag.getTrackNumber() + "/" //$NON-NLS-1$ //$NON-NLS-2$
					+ v2Tag.getTotalTracks());
		} else if (v2Tag.getTrackNumber() > 0) {
			tags.setTrack("" + v2Tag.getTrackNumber()); //$NON-NLS-1$
		} else {
			tags.setTrack(""); //$NON-NLS-1$
		}
		tags.setComment(filterTag(v2Tag.getComment()));
	}

	private static String filterTag(String tag) {
		if (tag == null)
			return "";
		return tag.trim();
	}
}
