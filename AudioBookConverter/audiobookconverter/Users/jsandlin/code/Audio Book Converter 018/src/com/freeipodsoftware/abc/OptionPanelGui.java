package com.freeipodsoftware.abc;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

public class OptionPanelGui extends Composite {

	private Group batchModeGroup = null;
	protected Button oneOutputFileOption = null;
	protected Button oneOutputFilePerInputFileOption = null;
	public OptionPanelGui(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		createBatchModeGroup();
		setSize(new Point(423, 81));
		setLayout(new GridLayout());
	}

	/**
	 * This method initializes batchModeGroup	
	 *
	 */
	private void createBatchModeGroup() {
		GridData gridData = new GridData();
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		batchModeGroup = new Group(this, SWT.NONE);
		batchModeGroup.setLayout(new GridLayout());
		batchModeGroup.setText(Messages.getString("OptionPanelGui.options")); //$NON-NLS-1$
		batchModeGroup.setLayoutData(gridData);
		oneOutputFileOption = new Button(batchModeGroup, SWT.RADIO);
		oneOutputFileOption.setText(Messages.getString("OptionPanelGui.intoOneFile")); //$NON-NLS-1$
		oneOutputFilePerInputFileOption = new Button(batchModeGroup, SWT.RADIO);
		oneOutputFilePerInputFileOption.setText(Messages.getString("OptionPanelGui.intoSeparateFiles")); //$NON-NLS-1$
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
