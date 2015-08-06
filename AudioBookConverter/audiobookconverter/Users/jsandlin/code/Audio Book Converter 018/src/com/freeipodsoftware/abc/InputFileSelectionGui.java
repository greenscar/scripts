package com.freeipodsoftware.abc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class InputFileSelectionGui extends Composite {

	private Label label = null;

	protected List list = null;

	private Composite composite = null;

	protected Button addButton = null;

	protected Button removeButton = null;

	private Composite composite1 = null;

	protected Button moveUpButton = null;

	protected Button moveDownButton = null;

	protected Button clearButton = null;

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginHeight = 0;
		gridLayout1.marginWidth = 0;
		GridData gridData1 = new GridData();
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(gridData1);
		composite.setLayout(gridLayout1);
		addButton = new Button(composite, SWT.NONE);
		addButton.setText(Messages.getString("InputFileSelectionGui.add")); //$NON-NLS-1$
		addButton.setToolTipText(Messages
				.getString("InputFileSelectionGui.addTooltip")); //$NON-NLS-1$
		addButton.setLayoutData(gridData2);
		removeButton = new Button(composite, SWT.NONE);
		removeButton.setText(Messages.getString("InputFileSelectionGui.remove")); //$NON-NLS-1$
		removeButton.setToolTipText(Messages
				.getString("InputFileSelectionGui.removeTooltip")); //$NON-NLS-1$
		removeButton.setLayoutData(gridData5);
		clearButton = new Button(composite, SWT.NONE);
		clearButton.setText(Messages.getString("InputFileSelectionGui.clear")); //$NON-NLS-1$
		clearButton.setToolTipText(Messages.getString("InputFileSelectionGui.clearTooltip")); //$NON-NLS-1$
		clearButton.setLayoutData(gridData6);
		createComposite1();
	}

	/**
	 * This method initializes composite1
	 * 
	 */
	private void createComposite1() {
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.marginHeight = 0;
		gridLayout2.marginWidth = 0;
		GridData gridData3 = new GridData();
		gridData3.grabExcessHorizontalSpace = false;
		gridData3.grabExcessVerticalSpace = true;
		composite1 = new Composite(composite, SWT.NONE);
		composite1.setLayoutData(gridData3);
		composite1.setLayout(gridLayout2);
		moveUpButton = new Button(composite1, SWT.NONE);
		moveUpButton.setText(Messages.getString("InputFileSelectionGui.moveUp")); //$NON-NLS-1$
		moveUpButton.setToolTipText(Messages
				.getString("InputFileSelectionGui.moveUpTooltip")); //$NON-NLS-1$
		moveUpButton.setLayoutData(gridData4);
		moveDownButton = new Button(composite1, SWT.NONE);
		moveDownButton.setText(Messages
				.getString("InputFileSelectionGui.moveDown")); //$NON-NLS-1$
		moveDownButton.setToolTipText(Messages
				.getString("InputFileSelectionGui.moveDownTooltip")); //$NON-NLS-1$
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
		shell.setSize(new Point(600, 400));
		new InputFileSelectionGui(shell, SWT.NONE);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public InputFileSelectionGui(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData11 = new GridData();
		gridData11.horizontalSpan = 2;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = false;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		label = new Label(this, SWT.WRAP);
		label.setText(Messages.getString("InputFileSelectionGui.headline")); //$NON-NLS-1$
		label.setLayoutData(gridData11);
		list = new List(this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
				| SWT.H_SCROLL);
		list.setLayoutData(gridData);
		this.setLayout(gridLayout);
		createComposite();
		setSize(new Point(545, 367));
	}

} // @jve:decl-index=0:visual-constraint="10,10"
