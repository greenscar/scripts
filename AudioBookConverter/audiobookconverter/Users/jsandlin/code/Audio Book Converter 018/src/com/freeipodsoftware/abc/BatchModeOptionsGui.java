package com.freeipodsoftware.abc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class BatchModeOptionsGui extends Composite {

	private Group destinationGroup = null;
	Button sameAsInputFileRadioButton = null;
	Button differentFolderRadioButton = null;
	Text folderText = null;
	Button chooseButton = null;
	private Composite buttonBarComposite = null;
	Button okButton = null;
	Button cancelButton = null;

	public BatchModeOptionsGui(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		createDestinationGroup();
		createButtonBarComposite();
		setSize(new Point(453, 140));
		setLayout(new GridLayout());
	}

	/**
	 * This method initializes destinationGroup	
	 *
	 */
	private void createDestinationGroup() {
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		destinationGroup = new Group(this, SWT.NONE);
		destinationGroup.setText(Messages.getString("BatchModeOptionsGui.destinationForConvertedFiles")); //$NON-NLS-1$
		destinationGroup.setLayoutData(gridData1);
		destinationGroup.setLayout(gridLayout);
		sameAsInputFileRadioButton = new Button(destinationGroup, SWT.RADIO);
		sameAsInputFileRadioButton.setText(Messages.getString("BatchModeOptionsGui.sameFolderAsInputFile")); //$NON-NLS-1$
		sameAsInputFileRadioButton.setLayoutData(gridData);
		differentFolderRadioButton = new Button(destinationGroup, SWT.RADIO);
		differentFolderRadioButton.setText(Messages.getString("BatchModeOptionsGui.thisFolder")); //$NON-NLS-1$
		folderText = new Text(destinationGroup, SWT.BORDER);
		folderText.setLayoutData(gridData2);
		chooseButton = new Button(destinationGroup, SWT.NONE);
		chooseButton.setText(Messages.getString("BatchModeOptionsGui.choose")); //$NON-NLS-1$
	}

	/**
	 * This method initializes buttonBarComposite	
	 *
	 */
	private void createButtonBarComposite() {
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.widthHint = 90;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.END;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		gridLayout1.marginWidth = 0;
		gridLayout1.horizontalSpacing = 5;
		gridLayout1.marginHeight = 5;
		gridLayout1.makeColumnsEqualWidth = true;
		buttonBarComposite = new Composite(this, SWT.NONE);
		buttonBarComposite.setLayout(gridLayout1);
		buttonBarComposite.setLayoutData(gridData3);
		okButton = new Button(buttonBarComposite, SWT.NONE);
		okButton.setText(Messages.getString("BatchModeOptionsGui.ok")); //$NON-NLS-1$
		okButton.setLayoutData(gridData4);
		cancelButton = new Button(buttonBarComposite, SWT.NONE);
		cancelButton.setText(Messages.getString("BatchModeOptionsGui.cancel")); //$NON-NLS-1$
		cancelButton.setLayoutData(gridData5);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
