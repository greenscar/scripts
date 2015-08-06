package com.freeipodsoftware.abc;


/**
 * Implementation of a strategy for suggesting tags for the outputfile.
 * 
 * @author Flo
 * 
 */
public class TagSuggestionStrategy implements EventListener {

	private TagEditor tagEditor;

	private InputFileSelection inputFileSelection;

	private String firstInputFileName;

	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		eventDispatcher.addListener(this);
	}

	public void setTagEditor(TagEditor tagEditor) {
		this.tagEditor = tagEditor;
	}

	public void setInputFileSelection(InputFileSelection inputFileSelection) {
		this.inputFileSelection = inputFileSelection;
	}

	public void onEvent(String eventId) {
		if (eventId.equals(InputFileSelection.FILE_LIST_CHANGED_EVENT)) {
			suggestTags();
		}
	}

	private void suggestTags() {
		if (inputFileSelection.getFileList().length > 0) {
			if (!inputFileSelection.getFileList()[0].equals(firstInputFileName)) {
				firstInputFileName = inputFileSelection.getFileList()[0];

				tagEditor.clear();

				Mp4Tags tags = Util.readTagsFromInputFile(firstInputFileName);
				tagEditor.setArtist(tags.getArtist());
				tagEditor.setTitle(tags.getTitle());
				tagEditor.setAlbum(tags.getAlbum());
				tagEditor.setGenre(tags.getGenre());
				tagEditor.setYear(tags.getYear());
				tagEditor.setTrack(tags.getTrack());
				tagEditor.setComment(tags.getComment());
			}
		}
	}
}
