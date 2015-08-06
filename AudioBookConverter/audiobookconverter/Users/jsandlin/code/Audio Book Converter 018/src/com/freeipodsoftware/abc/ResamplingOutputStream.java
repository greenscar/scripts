package com.freeipodsoftware.abc;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Sink for decoded 16-bit audio samples. Transforms the samples to a different
 * sampling frequency and writes them to an <code>OutputStream</code> (not
 * based on any specific knowledge on that topic). The decorated
 * <code>OutputStream</code> will not get closed when you call
 * <code>close()</code>.
 * 
 * @author Flo
 * 
 */
public class ResamplingOutputStream extends OutputStream {

	private final static int MAX_BUFFER_SIZE = 16384;

	private int channels;

	private OutputStream outputStream;

	private byte[][][] buffer;

	private int bufferSize;

	private int currentChannel;

	private int currentOctet;

	private double factor;

	private int newChannels;

	public ResamplingOutputStream(OutputStream outputStream, int channels,
			int newChannels, int sampleFrequency, int newSampleFrequency) {
		super();

		if (outputStream == null) {
			throw new NullPointerException("OutputStream must be set."); //$NON-NLS-1$
		}
		assertGreaterThanNull(channels, "Channels"); //$NON-NLS-1$
		assertGreaterThanNull(sampleFrequency, "FrequencyBefore"); //$NON-NLS-1$
		assertGreaterThanNull(newSampleFrequency, "FrequencyAfter"); //$NON-NLS-1$

		this.outputStream = outputStream;
		this.channels = channels;
		this.newChannels = newChannels;
		this.factor = (double) newSampleFrequency / sampleFrequency;

		buffer = new byte[channels][MAX_BUFFER_SIZE][2];
		bufferSize = 0;
		currentChannel = 0;
		currentOctet = 0;
	}

	public void write(int b) throws IOException {
		buffer[currentChannel][bufferSize][currentOctet] = (byte) (b & 0xFF);
		currentOctet++;
		if (currentOctet == 2) {
			currentOctet = 0;
			currentChannel++;
			if (currentChannel == channels) {
				currentChannel = 0;
				bufferSize++;
				if (bufferSize == MAX_BUFFER_SIZE) {
					writeOut();
				}
			}
		}
	}

	private void writeOut() throws IOException {
		for (int i = 0; i < bufferSize * factor; i++) {
			int channelIndex = 0;
			for (int y = 0; y < newChannels; y++) {
				channelIndex++;
				if (channelIndex == channels) {
					channelIndex = 0;
				}
				outputStream.write(buffer[channelIndex][(int) (i / factor)][0]);
				outputStream.write(buffer[channelIndex][(int) (i / factor)][1]);
			}
		}
		bufferSize = 0;
		currentChannel = 0;
	}

	public void flush() throws IOException {
		super.flush();
		writeOut();
	}

	private void assertGreaterThanNull(int value, String name) {
		if (value <= 0) {
			throw new RuntimeException(name + " must be set to a vaule > 0."); //$NON-NLS-1$
		}
	}

	public void close() throws IOException {
		flush();
		super.close();
	}
}
