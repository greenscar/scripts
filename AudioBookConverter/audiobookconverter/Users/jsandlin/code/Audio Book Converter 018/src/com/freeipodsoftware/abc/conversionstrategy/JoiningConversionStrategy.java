package com.freeipodsoftware.abc.conversionstrategy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.freeipodsoftware.abc.Messages;
import com.freeipodsoftware.abc.StreamDumper;

/**
 * Conversion strategy which combines all input files to one big output file.
 * 
 * @author Flo
 * 
 */
public class JoiningConversionStrategy extends AbstractConversionStrategy
		implements Runnable {

	private String outputFileName;

	private int currentFileNumber;

	private int channels;

	private int frequency;

	public long getOutputSize() {
		if (canceled) {
			return 0;
		}
		return new File(outputFileName).length();
	}

	@Override
	public int calcPercentFinishedForCurrentOutputFile() {
		return getProgress();
	}

	@Override
	public String getInfoText() {
		return Messages.getString("JoiningConversionStrategy.file") + " " + currentFileNumber + "/" + inputFileList.length; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public boolean supportsTagEditor() {
		return true;
	}

	public boolean makeUserInterview(Shell shell) {
		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		fileDialog.setFilterNames(new String[] { "" + " (*.m4b)" }); //$NON-NLS-1$ //$NON-NLS-2$
		fileDialog.setFilterExtensions(new String[] { "*.m4b" }); //$NON-NLS-1$
		fileDialog.setFileName(getOuputFilenameSuggestion());
		outputFileName = fileDialog.open();
		if (outputFileName != null) {
			if (!outputFileName.toUpperCase().endsWith(".m4b".toUpperCase())) { //$NON-NLS-1$
				outputFileName += ".m4b"; //$NON-NLS-1$
			}
			return true;
		}
		return false;
	}

	private String getOuputFilenameSuggestion() {
		if (inputFileList.length > 0) {
			String mp3Filename = inputFileList[0];
			String m4bFilename = mp3Filename.replaceFirst("\\.\\w*$", ".m4b"); //$NON-NLS-1$ //$NON-NLS-2$
			return m4bFilename;
		}
		return ""; //$NON-NLS-1$
	}

	@Override
	protected void startConversion() {
		new Thread(this).start();
	}

	public void run() {
		try {
			determineMaxChannelsAndFrequency();

			String commandLine = "external/faac.exe -P -C " + channels + " -R " //$NON-NLS-1$ //$NON-NLS-2$
					+ frequency
					+ " " + getMp4TagsFaacOptions() + " -o \"" + outputFileName + "\" -"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			Process proc = Runtime.getRuntime().exec(commandLine);
			StreamDumper streamDumper = new StreamDumper(proc.getInputStream());
			OutputStream faacOutput = proc.getOutputStream();

			for (int i = 0; i < inputFileList.length; i++) {
				if (!canceled) {
					currentFileNumber = i + 1;
					decodeInputFile(inputFileList[i], faacOutput, channels,
							frequency);
				}
			}

			streamDumper.stop();
			faacOutput.close();

			percentFinished = 100;

			finished = true;
			if (canceled) {
				finishListener.canceled();
				overallInputSize = 0;
				inputBytesProcessed = 0;
				percentFinished = 0;
			} else {
				finishListener.finished();
			}

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			finishListener.finishedWithError(e.getMessage() + "; " //$NON-NLS-1$
					+ sw.getBuffer().toString());
		} finally {
			finished = true;
		}
	}

	private void determineMaxChannelsAndFrequency() {
		channels = 0;
		frequency = 0;

		try {
			for (int i = 0; i < inputFileList.length; i++) {
				BufferedInputStream sourceStream = new BufferedInputStream(
						new FileInputStream(inputFileList[i]));
				Bitstream stream = new Bitstream(sourceStream);
				Header header = stream.readFrame();

				int fileChannels = (header.mode() == Header.SINGLE_CHANNEL) ? 1
						: 2;
				if (fileChannels > channels) {
					channels = fileChannels;
				}

				int fileFrequency = header.frequency();
				if (fileFrequency > frequency) {
					frequency = fileFrequency;
				}

				stream.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getAdditionalFinishedMessage() {
		return Messages.getString("JoiningConversionStrategy.outputFilename") + ":\n" + outputFileName; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
