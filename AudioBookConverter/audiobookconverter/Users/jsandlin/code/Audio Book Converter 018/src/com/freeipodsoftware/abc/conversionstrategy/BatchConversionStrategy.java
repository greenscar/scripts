package com.freeipodsoftware.abc.conversionstrategy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;

import org.eclipse.swt.widgets.Shell;

import com.freeipodsoftware.abc.BatchModeOptionsDialog;
import com.freeipodsoftware.abc.StreamDumper;
import com.freeipodsoftware.abc.Util;

/**
 * Conversion strategy which converts each input file to an separate output
 * file.
 * 
 * @author Flo
 * 
 */
public class BatchConversionStrategy extends AbstractConversionStrategy
		implements Runnable {

	private boolean intoSameFolder;

	private String folder;

	private int currentFileNumber;

	private int channels;

	private int frequency;

	private String outputFileName;

	public long getOutputSize() {
		if (canceled) {
			return 0;
		}
		return new File(outputFileName).length();
	}

	@Override
	public int calcPercentFinishedForCurrentOutputFile() {
		if (currentInputFileSize > 0) {
			return (int) (((double) currentInputFileBytesProcessed / currentInputFileSize) * 100);
		} else {
			return 0;
		}
	}

	public boolean makeUserInterview(Shell shell) {
		BatchModeOptionsDialog options = new BatchModeOptionsDialog(shell);
		options.setFolder(getSuggestedFolder());
		if (options.open()) {
			intoSameFolder = options.isIntoSameFolder();
			folder = options.getFolder();
			return true;
		} else {
			return false;
		}
	}

	private String getSuggestedFolder() {
		if (inputFileList != null && inputFileList.length > 0) {
			try {
				return new File(inputFileList[0]).getParentFile()
						.getAbsolutePath();
			} catch (Exception e) {
				return ""; //$NON-NLS-1$
			}
		}
		return ""; //$NON-NLS-1$
	}

	@Override
	protected void startConversion() {
		new Thread(this).start();
	}

	public boolean supportsTagEditor() {
		return false;
	}

	public void run() {
		for (int i = 0; i < inputFileList.length; i++) {
			currentFileNumber = i + 1;

			outputFileName = determineOutputFilename(inputFileList[i]);
			determineChannelsAndFrequency(inputFileList[i]);
			mp4Tags = Util.readTagsFromInputFile(inputFileList[i]);

			String commandLine = "external/faac.exe -P -C " + channels + " -R " //$NON-NLS-1$ //$NON-NLS-2$
					+ frequency
					+ " " + getMp4TagsFaacOptions() + " -o \"" + outputFileName + "\" -"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			try {

				Process proc = Runtime.getRuntime().exec(commandLine);
				StreamDumper streamDumper = new StreamDumper(proc
						.getInputStream());
				OutputStream faacOutput = proc.getOutputStream();

				decodeInputFile(inputFileList[i], faacOutput, channels,
						frequency);

				streamDumper.stop();
				faacOutput.close();

			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				finishListener.finishedWithError(e.getMessage() + "; " //$NON-NLS-1$
						+ sw.getBuffer().toString());
				break;
			}

		}
		finished = true;
		finishListener.finished();
	}

	private String determineOutputFilename(String inputFilename) {
		String outputFilename;
		if (intoSameFolder) {
			outputFilename = inputFilename.replaceAll("(?i)\\.mp3", ".m4b"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			File file = new File(inputFilename);
			File outFile = new File(folder, file.getName());
			outputFilename = outFile.getAbsolutePath().replaceAll("(?i)\\.mp3", //$NON-NLS-1$
					".m4b"); //$NON-NLS-1$
		}
		if (!outputFilename.endsWith(".m4b")) { //$NON-NLS-1$
			outputFilename = outputFilename + ".m4b"; //$NON-NLS-1$
		}
		return Util.makeFilenameUnique(outputFilename);
	}

	@Override
	public String getInfoText() {
		return Messages.getString("BatchConversionStrategy.file") + " " + currentFileNumber + "/" + inputFileList.length; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private void determineChannelsAndFrequency(String filename) {
		channels = 0;
		frequency = 0;

		try {

			BufferedInputStream sourceStream = new BufferedInputStream(
					new FileInputStream(filename));
			Bitstream stream = new Bitstream(sourceStream);
			Header header = stream.readFrame();

			channels = (header.mode() == Header.SINGLE_CHANNEL) ? 1 : 2;

			frequency = header.frequency();

			stream.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
