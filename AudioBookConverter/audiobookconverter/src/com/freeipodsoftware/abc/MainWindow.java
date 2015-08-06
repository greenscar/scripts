package com.freeipodsoftware.abc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.freeipodsoftware.abc.conversionstrategy.BatchConversionStrategy;
import com.freeipodsoftware.abc.conversionstrategy.ConversionStrategy;
import com.freeipodsoftware.abc.conversionstrategy.JoiningConversionStrategy;

public class MainWindow extends MainWindowGui implements FinishListener {

	public static class UpdateThread extends Thread {

		public UpdateThread(Shell shell) {
			setDaemon(true);

			try {
				URL updateUrl = new URL(AppProperties.WEBSITE_URL
						+ "/checkversion_auto.php?major=" + Version.MAJOR //$NON-NLS-1$
						+ "&minor=" + Version.MINOR); //$NON-NLS-1$
				URLConnection yc = updateUrl.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(yc
						.getInputStream()));

				String inputLine = in.readLine();
				in.close();

				if ("true".equals(inputLine)) { //$NON-NLS-1$
					MessageBox msg = new MessageBox(shell, SWT.ICON_INFORMATION
							| SWT.YES | SWT.NO);
					msg.setText(Messages.getString("MainWindow2.newVersion")); //$NON-NLS-1$
					msg.setMessage(Messages
							.getString("MainWindow2.aNewVersionIsAvailable")); //$NON-NLS-1$
					int result = msg.open();
					if (result == SWT.YES) {
						Program.launch(AppProperties.WEBSITE_URL);
					} else {
						Calendar calendar = Calendar.getInstance();
						calendar.add(Calendar.DAY_OF_MONTH, 7);
						AppProperties.setDateProperty(
								AppProperties.NO_UPDATECHECK_UNTIL, calendar
										.getTime());
					}
				}

			} catch (Exception e) {
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * Before this is run, be sure to set up the launch configuration
		 * (Arguments->VM Arguments) for the correct SWT library path in order
		 * to run with the SWT dlls. The dlls are located in the SWT plugin jar.
		 * For example, on Windows the Eclipse SWT 3.1 plugin jar is:
		 * installation_directory\plugins\org.eclipse.swt.win32_3.1.0.jar
		 */
		Display display = Display.getDefault();
		MainWindow thisClass = new MainWindow();
		thisClass.create();
		thisClass.sShell.open();

		if (AppProperties.getBooleanProperty(AppProperties.STAY_UPDATED)) {
			if (!isUpdateCheckSuspended()) {
				checkForUpdates(thisClass.sShell);
			}
		}

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private EventDispatcher eventDispatcher;

	private TagSuggestionStrategy tagSuggestionStrategy;

	private ProgressView progressView;

	private ConversionStrategy conversionStrategy;

	private boolean batchMode;

	public MainWindow() {
		eventDispatcher = new EventDispatcher();
	}

	protected void create() {
		createSShell();
		startButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						startConversion();
					}
				});

		inputFileSelection.setEventDispatcher(eventDispatcher);

		tagSuggestionStrategy = new TagSuggestionStrategy();
		tagSuggestionStrategy.setEventDispatcher(eventDispatcher);
		tagSuggestionStrategy.setTagEditor(toggleableTagEditor.getTagEditor());
		tagSuggestionStrategy.setInputFileSelection(inputFileSelection);

		updateToggleableTagEditorEnablement();

		aboutLink2
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						showAboutDialog();
					}
				});

		updateLink
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						Program.launch(AppProperties.WEBSITE_URL
								+ "/checkversion.php?major=" //$NON-NLS-1$
								+ Version.MAJOR + "&minor=" + Version.MINOR); //$NON-NLS-1$
					}
				});

		websiteLink
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						Program.launch(AppProperties.WEBSITE_URL);
					}
				});

		helpLink
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						Program.launch(AppProperties.HELP_URL);
					}
				});

		optionPanel.addOptionChangedListener(new OptionChangedListener() {
			public void optionChanged() {
				updateToggleableTagEditorEnablement();
			}
		});

	}

	private void updateToggleableTagEditorEnablement() {
		toggleableTagEditor.setEnabled(optionPanel.isSingleOutputFileMode());
	}

	private void showAboutDialog() {
		new AboutDialog(sShell).open();
	}

	private static boolean isUpdateCheckSuspended() {
		try {
			Date noUpdateCheckUntil = AppProperties
					.getDateProperty(AppProperties.NO_UPDATECHECK_UNTIL);
			return new Date().before(noUpdateCheckUntil);
		} catch (Exception e) {
		}
		return false;
	}

	private static void checkForUpdates(Shell shell) {
		UpdateThread updateThread = new UpdateThread(shell);
		updateThread.start();
	}

	private ConversionStrategy getConversionStrategy() {
		if (conversionStrategy == null
				|| !(conversionStrategy.getClass()
						.equals(getConversionStrategyClass()))) {
			try {
				conversionStrategy = getConversionStrategyClass().newInstance();
			} catch (Exception e) {
				Display.getCurrent().syncExec(new Runnable() {
					public void run() {
						MessageBox messageBox = new MessageBox(sShell,
								SWT.ICON_ERROR);
						messageBox.setText(sShell.getText());
						messageBox
								.setMessage(Messages
										.getString("MainWindow.cannotInstantiateConversionStrategy") //$NON-NLS-1$
										+ " " + getConversionStrategyClass()); //$NON-NLS-1$
						messageBox.open();
					}
				});
			}
		}
		return conversionStrategy;
	}

	/**
	 * Returns the current conversion strategy class.
	 * 
	 * @return
	 */
	private Class<? extends ConversionStrategy> getConversionStrategyClass() {
		if (isBatchMode()) {
			return BatchConversionStrategy.class;
		} else {
			return JoiningConversionStrategy.class;
		}
	}

	/**
	 * Returns wheater we are in batch mode.
	 * 
	 * @return
	 */
	private boolean isBatchMode() {
		Display.getCurrent().syncExec(new Runnable() {
			public void run() {
				batchMode = !optionPanel.isSingleOutputFileMode();
			}
		});
		return batchMode;
	}

	private void startConversion() {
		if (inputFileSelection.getFileList().length == 0)
			return;

		getConversionStrategy().setInputFileList(
				inputFileSelection.getFileList());

		if (getConversionStrategy().makeUserInterview(sShell)) {
			createProgressView();

			setUIEnabled(false);
			getConversionStrategy().setFinishListener(this);
			getConversionStrategy().setMp4Tags(
					toggleableTagEditor.getTagEditor().getMp4Tags());

			getConversionStrategy().start(sShell);

			ProgressUpdateThread progressUpdateThread = new ProgressUpdateThread(
					getConversionStrategy(), progressView);
			progressUpdateThread.start();
		}
	}

	private void createProgressView() {
		if (progressView == null) {
			GridData gridData = new GridData();
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
			gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
			progressView = new ProgressView(sShell, SWT.NONE);
			progressView.setLayoutData(gridData);
			progressView.setButtonWidthHint(inputFileSelection
					.getButtonWidthHint());
			Point preferedSize = progressView.computeSize(SWT.DEFAULT,
					SWT.DEFAULT);
			sShell.setSize(sShell.getSize().x, sShell.getSize().y
					+ preferedSize.y);
		} else {
			progressView.reset();
		}
	}

	private void setUIEnabled(boolean enabled) {
		startButton.setEnabled(enabled);
		inputFileSelection.setEnabled(enabled);
		if (enabled) {
			toggleableTagEditor.setEnabled(getConversionStrategy()
					.supportsTagEditor());
		} else {
			toggleableTagEditor.setEnabled(false);
		}
		optionPanel.setEnabled(enabled);
	}

	public void finishedWithError(final String errorMessage) {
		sShell.getDisplay().syncExec(new Runnable() {
			public void run() {
				MessageBox messageBox = new MessageBox(sShell, SWT.ICON_ERROR);
				messageBox.setText(sShell.getText());
				messageBox.setMessage(errorMessage);
				messageBox.open();
				setUIEnabled(true);
				progressView.finished();
			}
		});
	}

	public void finished() {
		sShell.getDisplay().syncExec(new Runnable() {
			public void run() {
				MessageBox messageBox = new MessageBox(sShell,
						SWT.ICON_INFORMATION);
				messageBox.setText(sShell.getText());
				messageBox.setMessage(Messages
						.getString("MainWindow2.finished") //$NON-NLS-1$
						+ ".\n\n" //$NON-NLS-1$
						+ getConversionStrategy()
								.getAdditionalFinishedMessage());
				messageBox.open();
				setUIEnabled(true);
				progressView.finished();
			}
		});
	}

	public void canceled() {
		sShell.getDisplay().syncExec(new Runnable() {
			public void run() {
				setUIEnabled(true);
				progressView.finished();
			}
		});
	}
}
