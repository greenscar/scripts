package com.freeipodsoftware.abc;

public interface FinishListener {

	void finishedWithError(String errorMessage);

	void finished();

	void canceled();

}
