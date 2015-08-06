package com.freeipodsoftware.abc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

public class MainWindowGui {

	protected Shell sShell = null; // @jve:decl-index=0:visual-constraint="10,10"

	private Composite composite;

	private Label label;

	private Composite composite2;

	protected Link websiteLink;

	protected Link aboutLink2;

	protected Link helpLink;

	protected Link updateLink;

	protected InputFileSelection inputFileSelection;

	protected ToggleableTagEditorGui toggleableTagEditor;

	private Composite startButtonComposite;

	protected Button startButton = null;

	protected OptionPanel optionPanel = null;

	/**
	 * This method initializes startButtonComposite	
	 *
	 */
	private void createStartButtonComposite() {
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData11 = new GridData();
		gridData11.widthHint = 160;
		startButton = new Button(startButtonComposite, SWT.NONE);
		startButton.setText(Messages.getString("MainWindow2.startConversion")); //$NON-NLS-1$
		startButton.setLayoutData(gridData11);
		startButton.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD)); //$NON-NLS-1$
	}

	/**
	 * This method initializes optionPanel	
	 *
	 */
	private void createOptionPanel() {
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		optionPanel = new OptionPanel(sShell, SWT.NONE);
		optionPanel.setLayoutData(gridData10);
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
		MainWindowGui thisClass = new MainWindowGui();
		thisClass.createSShell();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * This method initializes sShell
	 */
	protected void createSShell() {
		sShell = new Shell();
		sShell.setText(Messages.getString("MainWindow2.programName")); //$NON-NLS-1$
		sShell.setSize(new Point(621, 582));

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginHeight = 3;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginWidth = 0;
		sShell.setLayout(gridLayout1);

		createComposite();
		createInputFileSelection();
		createOptionPanel();
		createToggleableTagEditor();
		createComposite1();
	}

	/**
	 * This method initializes composite1
	 * 
	 */
	private void createComposite1() {
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.marginHeight = 5;
		gridLayout3.horizontalSpacing = 1;
		gridLayout3.numColumns = 1;
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		startButtonComposite = new Composite(sShell, SWT.NONE);
		startButtonComposite.setLayoutData(gridData1);
		startButtonComposite.setLayout(gridLayout3);
		createStartButtonComposite();
	}

	/**
	 * This method initializes toggleableTagEditor
	 * 
	 */
	private void createToggleableTagEditor() {
		GridData gridData9 = new GridData();
		gridData9.grabExcessHorizontalSpace = true;
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		toggleableTagEditor = new ToggleableTagEditorGui(sShell, SWT.NONE);
		toggleableTagEditor.setLayoutData(gridData9);
	}

	/**
	 * This method initializes inputFileSelection
	 * 
	 */
	private void createInputFileSelection() {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		inputFileSelection = new InputFileSelection(sShell, SWT.NONE);
		inputFileSelection.setToolTipText(""); //$NON-NLS-1$
		inputFileSelection.setLayoutData(gridData);
	}

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
		GridData gridData4 = new GridData();
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.numColumns = 2;
		gridLayout.marginWidth = 5;
		gridLayout.numColumns = 2;
		GridData gridData6 = new GridData();
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.horizontalAlignment = SWT.FILL;
		composite = new Composite(sShell, SWT.NONE);
		composite.setLayout(gridLayout);
		composite.setLayoutData(gridData6);
		label = new Label(composite, SWT.NONE);
		label.setText(Messages.getString("MainWindow2.programName")); //$NON-NLS-1$
		label.setLayoutData(gridData4);
		label.setFont(new Font(Display.getDefault(), "Tahoma", 14, SWT.BOLD)); //$NON-NLS-1$
		createComposite2();
	}

	/**
	 * This method initializes composite2
	 * 
	 */
	private void createComposite2() {
		GridData gridData8 = new GridData();
		gridData8.grabExcessHorizontalSpace = true;
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData7 = new GridData();
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData5 = new GridData();
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData3 = new GridData();
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		gridLayout2.marginWidth = 5;
		gridLayout2.horizontalSpacing = 5;
		gridLayout2.verticalSpacing = 3;
		gridLayout2.marginHeight = 3;
		composite2 = new Composite(composite, SWT.NONE);
		composite2.setLayout(gridLayout2);
		websiteLink = new Link(composite2, SWT.NONE);
		websiteLink
				.setText("<a>" + Messages.getString("MainWindow2.website") + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		websiteLink.setLayoutData(gridData8);

		aboutLink2 = new Link(composite2, SWT.NONE);
		aboutLink2
				.setText("<a>" + Messages.getString("MainWindow2.aboutThisSoftware") + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		aboutLink2.setLayoutData(gridData5);
		helpLink = new Link(composite2, SWT.NONE);
		helpLink
				.setText("<a>" + Messages.getString("MainWindow2.help") + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		helpLink.setLayoutData(gridData3);
		updateLink = new Link(composite2, SWT.NONE);
		updateLink
				.setText("<a>" + Messages.getString("MainWindow2.checkForUpdates") + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		updateLink.setLayoutData(gridData7);
	}
}
