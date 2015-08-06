package com.freeipodsoftware.abc.conversionstrategy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javazoom.jl.converter.Converter.PrintWriterProgressListener;
import javazoom.jl.converter.Converter.ProgressListener;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.Obuffer;

import org.apache.commons.io.input.CountingInputStream;
import org.eclipse.swt.widgets.Shell;

import com.freeipodsoftware.abc.ConversionException;
import com.freeipodsoftware.abc.FinishListener;
import com.freeipodsoftware.abc.Mp4Tags;
import com.freeipodsoftware.abc.ResamplingOutputStream;
import com.freeipodsoftware.abc.StreamOBuffer;
import com.freeipodsoftware.abc.Util;

/**
 * Implements common behaviour of all conversion strategies.
 * 
 * @author Flo
 * 
 */
public abstract class AbstractConversionStrategy implements ConversionStrategy {

	public class RemainingTimeCalculatorThread extends Thread {
		public void run() {
			long lastTimeStamp, currentTimeStamp;

			lastTimeStamp = startTime;
			while (!finished) {
				double percentFinishedDouble = (((double) inputBytesProcessed / overallInputSize) * 100);
				percentFinished = (int) percentFinishedDouble;

				percentFinishedForCurrentOutputFile = calcPercentFinishedForCurrentOutputFile();

				currentTimeStamp = System.currentTimeMillis();
				if (!paused) {
					elapsedTime += currentTimeStamp - lastTimeStamp;
				}
				lastTimeStamp = currentTimeStamp;

				try {
					remainingTime = new Double(((double) elapsedTime)
							/ percentFinishedDouble
							* (100 - percentFinishedDouble)).longValue();
				} catch (Exception e) {
				}
				try {
					Thread.sleep(500);
				} catch (Exception e) {
				}
			}
		}
	}

	protected String[] inputFileList;

	protected FinishListener finishListener;

	protected boolean finished;

	protected long overallInputSize;

	protected long inputBytesProcessed;

	protected int percentFinished;

	protected int percentFinishedForCurrentOutputFile;

	protected long elapsedTime;

	protected long startTime;

	protected long remainingTime;

	protected boolean canceled;

	private RemainingTimeCalculatorThread remainingTimeCalculatorThread;

	protected Mp4Tags mp4Tags;

	private boolean paused;

	protected long currentInputFileSize;

	protected long currentInputFileBytesProcessed;

	public void setInputFileList(String[] inputFileList) {
		this.inputFileList = inputFileList;
	}

	/**
	 * Return the progress of the current outputfile in percent (0-100).
	 * 
	 * @return
	 */
	public abstract int calcPercentFinishedForCurrentOutputFile();

	public void setFinishListener(FinishListener finishListener) {
		this.finishListener = finishListener;
	}

	public void setMp4Tags(Mp4Tags mp4Tags) {
		this.mp4Tags = mp4Tags;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public String getInfoText() {
		return ""; //$NON-NLS-1$
	}

	public int getProgress() {
		return percentFinished;
	}

	public int getProgressForCurrentOutputFile() {
		return percentFinishedForCurrentOutputFile;
	}

	public long getRemainingTime() {
		return remainingTime;
	}

	public boolean isFinished() {
		return finished;
	}

	public void start(Shell shell) {
		overallInputSize = determineInputSize();
		canceled = false;
		finished = false;
		inputBytesProcessed = 0;
		startTime = System.currentTimeMillis();

		startConversion();

		remainingTimeCalculatorThread = new RemainingTimeCalculatorThread();
		remainingTimeCalculatorThread.start();
	}

	/**
	 * Start the conversion thread in this method.
	 * 
	 */
	abstract protected void startConversion();

	public void cancel() {
		canceled = true;
	}

	protected long determineInputSize() {
		long size = 0;
		for (int i = 0; i < inputFileList.length; i++) {
			File file = new File(inputFileList[i]);
			if (!file.exists()) {
				throw new ConversionException(
						Messages
								.getString("AbstractConversionStrategy.fileNotFound") + ": " //$NON-NLS-1$ //$NON-NLS-2$
								+ inputFileList[i]);
			}
			size += file.length();
		}
		return size;
	}

	protected String getMp4TagsFaacOptions() {
		if (mp4Tags == null)
			return ""; //$NON-NLS-1$

		StringBuffer buffer = new StringBuffer();

		appendFaacOptionIfNotEmpty(buffer, "--artist", mp4Tags.getArtist()); //$NON-NLS-1$
		appendFaacOptionIfNotEmpty(buffer, "--writer", mp4Tags.getWriter()); //$NON-NLS-1$
		appendFaacOptionIfNotEmpty(buffer, "--title", mp4Tags.getTitle()); //$NON-NLS-1$
		appendFaacOptionIfNotEmpty(buffer, "--album", mp4Tags.getAlbum()); //$NON-NLS-1$
		appendFaacOptionIfNotEmpty(buffer, "--genre", mp4Tags.getGenre()); //$NON-NLS-1$
		appendFaacOptionIfNotEmpty(buffer, "--year", mp4Tags.getYear()); //$NON-NLS-1$
		appendFaacOptionIfNotEmpty(buffer, "--track", mp4Tags.getTrack()); //$NON-NLS-1$
		appendFaacOptionIfNotEmpty(buffer, "--disc", mp4Tags.getDisc()); //$NON-NLS-1$
		appendFaacOptionIfNotEmpty(buffer, "--comment", mp4Tags.getComment()); //$NON-NLS-1$

		return buffer.toString();
	}

	private void appendFaacOptionIfNotEmpty(StringBuffer buffer, String option,
			String text) {
		if (Util.hasText(text)) {
			buffer.append(option);
			buffer.append(" \""); //$NON-NLS-1$
			buffer.append(filterEscapeChars(text));
			buffer.append("\" "); //$NON-NLS-1$
		}
	}

	private String filterEscapeChars(String text) {
		if (text == null)
			return null;
		//return text.replace("\"", "\\\"");
		return text.replace("\"", "/\"");
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	protected void decodeInputFile(String filename, OutputStream destination,
			int channels, int frequency) throws Exception {

		long processedSoFar = inputBytesProcessed;

		currentInputFileSize = getFileSize(filename);

		PrintWriterProgressListener progressListener = PrintWriterProgressListener
				.newStdOut(PrintWriterProgressListener.NO_DETAIL);

		CountingInputStream countingInputStream = new CountingInputStream(
				new FileInputStream(filename));

		BufferedInputStream sourceStream = new BufferedInputStream(
				countingInputStream);

		progressListener.converterUpdate(ProgressListener.UPDATE_FRAME_COUNT,
				-1, 0);

		Obuffer output = null;
		Decoder decoder = new Decoder(null);
		Bitstream stream = new Bitstream(sourceStream);

		ResamplingOutputStream resamplingOutputStream = null;

		int frame = 0;
		int frameCount = -1;
		if (frameCount == -1)
			frameCount = Integer.MAX_VALUE;

		for (; frame < frameCount; frame++) {
			if (canceled) {
				break;
			}
			while (paused) {
				Thread.sleep(100);
				if (canceled) {
					break;
				}
			}

			currentInputFileBytesProcessed = countingInputStream.getCount();
			inputBytesProcessed = processedSoFar
					+ currentInputFileBytesProcessed;

			try {
				Header header = stream.readFrame();
				if (header == null)
					break;

				progressListener.readFrame(frame, header);

				if (output == null) {
					int fileChannels = (header.mode() == Header.SINGLE_CHANNEL) ? 1
							: 2;
					int fileFrequency = header.frequency();
					resamplingOutputStream = new ResamplingOutputStream(
							destination, fileChannels, channels, fileFrequency,
							frequency);
					output = new StreamOBuffer(resamplingOutputStream,
							fileChannels);
					decoder.setOutputBuffer(output);
				}

				Obuffer decoderOutput = decoder.decodeFrame(header, stream);

				if (decoderOutput != output)
					throw new InternalError("Output buffers are different."); //$NON-NLS-1$

				progressListener.decodedFrame(frame, header, output);

				stream.closeFrame();

				resamplingOutputStream.close();

			} catch (Exception ex) {
				boolean stop = !progressListener.converterException(ex);

				if (stop) {
					throw new JavaLayerException(ex.getLocalizedMessage(), ex);
				}
			}
		}

	}

	private long getFileSize(String filename) {
		File file = new File(filename);
		if (file.exists()) {
			return file.length();
		}
		return 0;
	}

	public String getAdditionalFinishedMessage() {
		return ""; //$NON-NLS-1$
	}
}
