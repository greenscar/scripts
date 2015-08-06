package com.freeipodsoftware.abc;

import java.text.DecimalFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;

public class ProgressView extends ProgressViewGui {

	public ProgressView(Composite parent, int style) {
		super(parent, style);

		infoLabel.setText(""); //$NON-NLS-1$

		cancelButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						MessageBox msgbox = new MessageBox(ProgressView.this
								.getShell(), SWT.ICON_QUESTION | SWT.YES
								| SWT.NO);
						msgbox.setText(Messages
								.getString("ProgressView.confirmation")); //$NON-NLS-1$
						msgbox.setMessage(Messages
								.getString("ProgressView.cancelConfirmText")); //$NON-NLS-1$
						int result = msgbox.open();
						if (result == SWT.YES) {
							canceled = true;
							cancelButton.setEnabled(false);
							pauseButton.setSelection(false);
							pauseButton.setEnabled(false);
						}
					}
				});
	}

	public void setButtonWidthHint(int buttonWidthHint) {
		setWidthHintForControl(buttonWidthHint, cancelButton);
		setWidthHintForControl(buttonWidthHint, pauseButton);
	}

	private void setWidthHintForControl(int buttonWidthHint, Control widget) {
		GridData gridData = (GridData) widget.getLayoutData();
		gridData.widthHint = buttonWidthHint;
	}

	public void setProgress(int progress) {
		progressBar.setSelection(progress);
	}

	public void setElapsedTime(long elapsedTime) {
		elapsedTimeLabel
				.setText(Messages.getString("ProgressView.timeElapsed") + " " + formatTime(elapsedTime)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private String formatTime(long millis) {
		DecimalFormat decimalFormat = new DecimalFormat("00"); //$NON-NLS-1$
		long sec = (millis / 1000) % 60;
		long min = (millis / 60 / 1000) % 60;
		long hrs = (millis / 60 / 60 / 1000);
		return hrs + ":" + decimalFormat.format(min) + ":" //$NON-NLS-1$ //$NON-NLS-2$
				+ decimalFormat.format(sec);
	}

	/**
	 * Set the estimate final output file size of the current output file. Set
	 * -1 when you cannot provide an decent estimate.
	 * 
	 * @param bytes
	 */
	public void setEstimatedFinalOutputSize(long bytes) {
		if (bytes == -1) {
			outputFileSizeValueLabel.setText("---");
		} else {
			DecimalFormat mbFormat = new DecimalFormat("0.0"); //$NON-NLS-1$
			outputFileSizeValueLabel.setText(mbFormat.format((double) bytes
					/ (1024 * 1024))
					+ " MB"); //$NON-NLS-1$
		}
	}

	public void setRemainingTime(long remainingTime) {
		remainingTimeLabel.setText(Messages
				.getString("ProgressView.timeRemaining") //$NON-NLS-1$
				+ " " + formatTime(remainingTime)); //$NON-NLS-1$
	}

	public void setInfoText(String infoText) {
		infoLabel.setText(infoText);
	}

	public boolean isCanceled() {
		return canceled;
	}

	public boolean isPaused() {
		return pauseButton.getSelection();
	}

	public void reset() {
		canceled = false;
		cancelButton.setEnabled(true);
		pauseButton.setEnabled(true);
	}

	public void finished() {
		cancelButton.setEnabled(false);
		pauseButton.setSelection(false);
		pauseButton.setEnabled(false);
		progressBar.setSelection(0);
	}

}
