package com.freeipodsoftware.abc;

import com.freeipodsoftware.abc.conversionstrategy.ConversionStrategy;

public class ProgressUpdateThread extends Thread {

	private ConversionStrategy converter;

	private ProgressView progressView;

	public ProgressUpdateThread(ConversionStrategy conversionStrategy,
			ProgressView progressView) {
		this.converter = conversionStrategy;
		this.progressView = progressView;
	}

	public void run() {
		while (!converter.isFinished()) {

			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}

			progressView.getDisplay().syncExec(new Runnable() {
				public void run() {
					progressView.setProgress(converter.getProgress());
					progressView.setElapsedTime(converter.getElapsedTime());
					progressView.setRemainingTime(converter.getRemainingTime());
					progressView
							.setEstimatedFinalOutputSize(calculateEstimatedFinalOutputSize());
					progressView.setInfoText(converter.getInfoText());
					converter.setPaused(progressView.isPaused());
					if (progressView.isCanceled())
						converter.cancel();
				}

				private long calculateEstimatedFinalOutputSize() {
					// Do no estimate until not at least 2% are done.
					if (converter.getProgressForCurrentOutputFile() < 2) {
						return -1;
					}
					try {
						return (long) ((float) converter.getOutputSize()
								/ converter.getProgressForCurrentOutputFile() * 100);
					} catch (RuntimeException e) {
						return 0;
					}
				}
			});
		}
	}
}
