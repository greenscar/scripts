package com.freeipodsoftware.abc;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class BatchModeOptionsDialog extends Dialog {

	private boolean canceled;

	private BatchModeOptionsGui gui;

	private Shell shell;

	private String folder;

	private boolean intoSameFolder;

	public BatchModeOptionsDialog(Shell parent) {
		super(parent, SWT.APPLICATION_MODAL);
		setText(Messages.getString("BatchModeOptionsDialog.outputOptions")); //$NON-NLS-1$
	}

	public boolean open() {
		canceled = true;
		Shell parent = getParent();
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL
				| SWT.RESIZE);
		shell.setText(getText());

		initializeComponents(shell);

		Util.centerDialog(getParent(), shell);
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return !canceled;
	}

	private void initializeComponents(final Shell shell) {
		gui = new BatchModeOptionsGui(shell, SWT.NONE);

		shell.setLayout(new FillLayout());
		shell.setDefaultButton(gui.okButton);

		gui.folderText.setText(folder);

		gui.okButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						canceled = false;
						folder = gui.folderText.getText();
						intoSameFolder = gui.sameAsInputFileRadioButton
								.getSelection();
						shell.close();
					}
				});

		gui.cancelButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						canceled = true;
						shell.close();
					}
				});

		gui.chooseButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						DirectoryDialog dialog = new DirectoryDialog(shell);
						dialog.setMessage(Messages.getString("BatchModeOptionsDialog.chooseOuputFolder")); //$NON-NLS-1$
						try {
							dialog.setFilterPath(gui.folderText.getText());
						} catch (Exception ex) {
						}
						String result = dialog.open();
						if (result != null) {
							gui.folderText.setText(result);
							validateControls();
						}
					}
				});

		gui.differentFolderRadioButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						validateControls();
					}
				});

		gui.sameAsInputFileRadioButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						validateControls();
					}
				});

		gui.folderText.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				validateControls();
			}
		});
		
		shell.pack();
		shell.setSize(400, shell.getSize().y);
	}

	private void validateControls() {
		gui.folderText
				.setEnabled(gui.differentFolderRadioButton.getSelection());
		gui.chooseButton.setEnabled(gui.differentFolderRadioButton
				.getSelection());
		if (gui.differentFolderRadioButton.getSelection()) {
			try {
				File dir = new File(gui.folderText.getText());
				gui.okButton.setEnabled(dir.exists() && dir.isDirectory());
			} catch (Exception e) {
				gui.okButton.setEnabled(false);
			}
		} else {
			gui.okButton.setEnabled(true);
		}
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public boolean isIntoSameFolder() {
		return intoSameFolder;
	}

}