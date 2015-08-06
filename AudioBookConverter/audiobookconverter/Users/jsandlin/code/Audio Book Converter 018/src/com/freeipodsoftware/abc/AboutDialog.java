package com.freeipodsoftware.abc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AboutDialog extends Dialog {

	protected Shell sShell = null; // @jve:decl-index=0:visual-constraint="9,11"

	protected AboutDialogGui aboutComposite;

	public AboutDialog(Shell parent) {
		super(parent);
	}

	public void open() {
		Shell parent = getParent();
		createSShell(parent);
		sShell.open();
		Display display = parent.getDisplay();
		while (!sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * This method initializes sShell
	 * 
	 * @param parent
	 */
	private void createSShell(Shell parent) {
		sShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		sShell.setText(Messages.getString("AboutDialog.caption")); //$NON-NLS-1$
		sShell.setLayout(new GridLayout());
		aboutComposite = new AboutDialogGui(sShell, SWT.NONE);
		aboutComposite.getCloseButton().addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						sShell.close();
					}
				});
		sShell.setDefaultButton(aboutComposite.getCloseButton());
		sShell.pack();
	}
}
