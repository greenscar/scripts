package com.freeipodsoftware.abc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class ProgressViewGui extends Composite {

	private Label label = null;

	protected ProgressBar progressBar = null;

	private Composite composite = null;

	protected Label elapsedTimeLabel = null;

	protected Label remainingTimeLabel = null;

	private Composite composite1 = null;

	private Label outputSizeLabel = null;

	protected Label outputFileSizeValueLabel = null;

	protected Button cancelButton = null;

	protected boolean canceled;

	protected Label infoLabel = null;

	protected Button pauseButton = null;

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalSpan = 2;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.numColumns = 2;
		gridLayout.marginWidth = 0;
		composite = new Composite(this, SWT.NONE);
		composite.setLayout(gridLayout);
		composite.setLayoutData(gridData1);
		elapsedTimeLabel = new Label(composite, SWT.NONE);
		elapsedTimeLabel.setText("Time elapsed 0:21:54"); //$NON-NLS-1$
		elapsedTimeLabel.setLayoutData(gridData2);
		remainingTimeLabel = new Label(composite, SWT.RIGHT);
		remainingTimeLabel.setText("Time remaining 0:53:21"); //$NON-NLS-1$
		remainingTimeLabel.setLayoutData(gridData6);
	}

	/**
	 * This method initializes composite1
	 * 
	 */
	private void createComposite1() {
		GridData gridData5 = new GridData();
		gridData5.horizontalSpan = 2;
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessHorizontalSpace = true;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData4.grabExcessHorizontalSpace = false;
		GridData gridData3 = new GridData();
		gridData3.grabExcessHorizontalSpace = false;
		gridData3.horizontalSpan = 2;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginWidth = 0;
		gridLayout1.numColumns = 5;
		gridLayout1.marginHeight = 0;
		composite1 = new Composite(this, SWT.NONE);
		composite1.setLayout(gridLayout1);
		composite1.setLayoutData(gridData3);
		outputSizeLabel = new Label(composite1, SWT.NONE);
		outputSizeLabel.setText(Messages
				.getString("ProgressView.outputFilesize") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		outputFileSizeValueLabel = new Label(composite1, SWT.NONE);
		outputFileSizeValueLabel.setText("---"); //$NON-NLS-1$
		outputFileSizeValueLabel.setLayoutData(gridData5);
		outputFileSizeValueLabel.setFont(new Font(Display.getDefault(),
				"Tahoma", 8, SWT.BOLD)); //$NON-NLS-1$
		pauseButton = new Button(composite1, SWT.TOGGLE);
		pauseButton.setText(Messages.getString("ProgressViewGui.pause")); //$NON-NLS-1$
		cancelButton = new Button(composite1, SWT.NONE);
		cancelButton.setText(Messages.getString("ProgressView.cancel")); //$NON-NLS-1$
		cancelButton.setLayoutData(gridData4);
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
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(new Point(300, 200));
		new ProgressViewGui(shell, SWT.NONE);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public ProgressViewGui(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData11.grabExcessHorizontalSpace = true;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		label = new Label(this, SWT.NONE);
		label.setText(Messages.getString("ProgressView.progress")); //$NON-NLS-1$
		label.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD)); //$NON-NLS-1$
		infoLabel = new Label(this, SWT.RIGHT);
		infoLabel.setText("Info"); //$NON-NLS-1$
		infoLabel.setLayoutData(gridData11);
		progressBar = new ProgressBar(this, SWT.NONE);
		progressBar.setLayoutData(gridData);
		createComposite();
		this.setLayout(gridLayout2);
		createComposite1();
		setSize(new Point(504, 113));
	}

} // @jve:decl-index=0:visual-constraint="10,10"
