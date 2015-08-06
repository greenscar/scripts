package com.freeipodsoftware.abc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Properties;

public class AppProperties {

	public static final String WEBSITE_URL = "http://www.freeipodsoftware.com/"; //$NON-NLS-1$
	
	public static final String HELP_URL = "http://www.freeipodsoftware.com/converterhelp.php"; //$NON-NLS-1$

	private static final String APP_PROPERTIES = "app.properties"; //$NON-NLS-1$

	// private static final String APPDATA = "env.appdata"; //$NON-NLS-1$
	private static final String APPDATA = "APPDATA"; //$NON-NLS-1$

	private static final String MP3TOI_POD_AUDIO_BOOK_CONVERTER = "MP3toiPodAudioBookConverter"; //$NON-NLS-1$

	public static final String STAY_UPDATED = "stayUpdated"; //$NON-NLS-1$

	public static final String NO_UPDATECHECK_UNTIL = "noUpdateCheckUntil"; //$NON-NLS-1$

	private static Properties getAppProperties() {
		Properties defaultProperties = new Properties();
		defaultProperties.put(STAY_UPDATED, Boolean.TRUE.toString());
		defaultProperties.put(OptionPanel.OPTION_PANEL_SINGLE_OUTPUT_FILE_MODE,
				Boolean.TRUE.toString());

		Properties applicationProps = new Properties(defaultProperties);

		try {
			FileInputStream in = new FileInputStream(new File(new File(System
					.getenv(APPDATA), MP3TOI_POD_AUDIO_BOOK_CONVERTER),
					APP_PROPERTIES));
			applicationProps.load(in);
			in.close();
		} catch (Exception e) {
		}
		return applicationProps;
	}

	public static String getProperty(String key) {
		Properties applicationProps = getAppProperties();
		return applicationProps.getProperty(key);
	}

	public static void setProperty(String key, String value) {
		Properties applicationProps = getAppProperties();

		applicationProps.put(key, value);

		try {
			File appDir = new File(new File(System.getenv(APPDATA)),
					MP3TOI_POD_AUDIO_BOOK_CONVERTER);
			if (!appDir.exists()) {
				boolean succ = appDir.mkdir();
				System.out.println(succ);
			}
			FileOutputStream out = new FileOutputStream(new File(appDir,
					APP_PROPERTIES));
			applicationProps.store(out, ""); //$NON-NLS-1$
			out.close();
		} catch (Exception e) {
		}
	}

	public static boolean getBooleanProperty(String key) {
		return new Boolean(getProperty(key)).booleanValue();
	}

	public static void setBooleanProperty(String key, boolean value) {
		setProperty(key, new Boolean(value).toString());
	}

	public static Date getDateProperty(String key) {
		return new Date(Long.parseLong(getProperty(key)));
	}

	public static void setDateProperty(String key, Date date) {
		setProperty(key, String.valueOf(date.getTime()));
	}
}
