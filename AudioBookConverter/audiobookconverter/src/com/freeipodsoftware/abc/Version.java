package com.freeipodsoftware.abc;

public class Version {
	public static final int MAJOR = 0;

	public static final int MINOR = 18;

	public static String getVersionString() {
		return MAJOR + "." + MINOR; //$NON-NLS-1$
	}
}
