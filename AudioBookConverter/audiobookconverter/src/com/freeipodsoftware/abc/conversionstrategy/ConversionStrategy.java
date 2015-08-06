package com.freeipodsoftware.abc.conversionstrategy;

import org.eclipse.swt.widgets.Shell;

import com.freeipodsoftware.abc.FinishListener;
import com.freeipodsoftware.abc.Mp4Tags;

/**
 * Common interface for all conversion strategies.
 * 
 * @author Flo
 * 
 */
public interface ConversionStrategy {

	void setInputFileList(String[] inputFileList);

	void setFinishListener(FinishListener finishListener);

	/**
	 * Ask user for output filename or conversion options in this method.
	 * 
	 * @param shell
	 *            Shell for opening dialogs.
	 * @return true when conversion can begin. False, wenn user had canceled.
	 */
	boolean makeUserInterview(Shell shell);

	/**
	 * Start the conversion. Implement this method non-blocking.
	 * 
	 */
	void start(Shell shell);

	void cancel();

	boolean isFinished();

	/**
	 * 0-100 % finished.
	 * 
	 * @return percentage finished.
	 */
	int getProgress();

	/**
	 * 0-100 % finished, for current outputfile. In joining-mode this is the
	 * same as getProgress();
	 * 
	 * @return
	 */
	int getProgressForCurrentOutputFile();

	long getElapsedTime();

	long getRemainingTime();

	long getOutputSize();

	/**
	 * Additional info text that gets displayed in the progress view.
	 * 
	 * @return infoText
	 */
	String getInfoText();

	boolean supportsTagEditor();

	void setMp4Tags(Mp4Tags mp4Tags);

	String getAdditionalFinishedMessage();

	void setPaused(boolean paused);
}
