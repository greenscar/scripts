package com.freeipodsoftware.abc;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javazoom.jl.decoder.Obuffer;

/**
 * An Output Buffer for the MP3 Decoder which writes the PCM samples to an
 * OutputStream. The whole buffer thing in this class is just because PCM
 * requires for stereo files to send alternating samples for ch0, ch1, ch0, ch1
 * and so on. But the MP3 decoder sends lots of samples for ch0, then lots of
 * samples for ch1 and so on.
 * 
 * @author Flo
 * 
 */
public class StreamOBuffer extends Obuffer {

	private OutputStream output;

	private int channels;

	private short[][] buffers;

	private int[] bufferPointerNext;

	private int[] bufferPointerFirst;

	private int pointer;

	public StreamOBuffer(OutputStream output, int channels) {
		this.output = output;
		this.channels = channels;
		buffers = new short[channels][65535];
		bufferPointerNext = new int[channels];
		bufferPointerFirst = new int[channels];
		pointer = 0;
	}

	public void append(int arg0, short arg1) {
		buffers[arg0][bufferPointerNext[arg0]] = arg1;
		bufferPointerNext[arg0]++;
		while (bufferPointerNext[pointer] > bufferPointerFirst[pointer]) {
			short value = buffers[pointer][bufferPointerFirst[pointer]];
			bufferPointerFirst[pointer]++;
			try {
				output.write((byte) ((value >> 8) & 0xFF));
				output.write((byte) ((value & 0xFF)));
			} catch (Exception e) {
				StringWriter stackTrace = new StringWriter();
				e.printStackTrace(new PrintWriter(stackTrace));
				throw new RuntimeException(stackTrace.getBuffer().toString());
			}
			pointer++;
			if (pointer >= channels) {
				pointer = 0;
			}
		}
	}

	public void clear_buffer() {
		bufferPointerNext = new int[channels];
		bufferPointerFirst = new int[channels];
		pointer = 0;
	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void set_stop_flag() {
		// TODO Auto-generated method stub

	}

	public void write_buffer(int arg0) {
		// TODO Auto-generated method stub

	}

}
