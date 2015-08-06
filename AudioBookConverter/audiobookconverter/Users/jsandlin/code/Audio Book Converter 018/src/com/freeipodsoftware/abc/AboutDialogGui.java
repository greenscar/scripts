package com.freeipodsoftware.abc;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Font;

public class AboutDialogGui extends Composite {

	private Label label = null;

	private Label label1 = null;

	private Link link = null;

	private Link link1 = null;

	private Link link2 = null;

	private Link link3 = null;

	private Link link4 = null;

	private Link link5 = null;

	private Composite composite = null;

	private Button checkBox = null;

	private Button closeButton = null;

	private Link link6;

	private Link link7 = null;

	private Label label2 = null;

	private Label label3 = null;

	private Link jid3Link = null;

	public Button getCloseButton() {
		return closeButton;
	}

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
		GridData gridData3 = new GridData();
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.widthHint = 80;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.numColumns = 2;
		gridLayout.marginWidth = 0;
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(gridData2);
		composite.setLayout(gridLayout);
		checkBox = new Button(composite, SWT.CHECK);
		checkBox.setSelection(AppProperties
				.getBooleanProperty(AppProperties.STAY_UPDATED));
		checkBox.setText(Messages.getString("AboutComposite.checkForUpdates")); //$NON-NLS-1$
		closeButton = new Button(composite, SWT.NONE);
		closeButton.setText(Messages.getString("AboutComposite.close")); //$NON-NLS-1$
		closeButton.setLayoutData(gridData3);
		closeButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						AppProperties.setBooleanProperty(
								AppProperties.STAY_UPDATED, checkBox
										.getSelection());
					}
				});
		closeButton.setFocus();
	}

	private void assignLink(Link link, final String url) {
		link.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Program.launch(url);
			}
		});
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
		new AboutDialogGui(shell, SWT.NONE);
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public AboutDialogGui(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData11 = new GridData();
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.widthHint = 510;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.marginHeight = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.numColumns = 1;
		this.setLayout(gridLayout1);
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;

		gridData.widthHint = 510;
		Label titleLabel = new Label(this, SWT.NONE);
		titleLabel.setText(Messages.getString("MainWindow2.programName")); //$NON-NLS-1$

		titleLabel
				.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD)); //$NON-NLS-1$
		Label versionLabel = new Label(this, SWT.NONE);
		versionLabel
				.setText(Messages.getString("AboutComposite.version") + Version.getVersionString()); //$NON-NLS-1$

		link7 = new Link(this, SWT.NONE);
		link7
				.setText(Messages.getString("AboutComposite.website") + ": <a>www.freeipodsoftware.com</a>"); //$NON-NLS-1$ //$NON-NLS-2$

		assignLink(link7, AppProperties.WEBSITE_URL);

		label = new Label(this, SWT.WRAP);
		label.setText(Messages.getString("AboutComposite.description")); //$NON-NLS-1$
		label.setLayoutData(gridData);
		label1 = new Label(this, SWT.NONE);
		label1.setText(Messages.getString("AboutComposite.thirdPartyText") //$NON-NLS-1$
				+ ":"); //$NON-NLS-1$
		link = new Link(this, SWT.NONE);
		link.setText("<a>JLayer</a>"); //$NON-NLS-1$
		assignLink(link, "http://www.javazoom.net/javalayer/javalayer.html"); //$NON-NLS-1$

		link1 = new Link(this, SWT.NONE);
		link1.setText("<a>Freeware Advanced Audio Coder</a>"); //$NON-NLS-1$
		assignLink(link1, "http://www.audiocoding.com/"); //$NON-NLS-1$

		link2 = new Link(this, SWT.NONE);
		link2.setText("<a>Apache Jakarta Commons IO</a>"); //$NON-NLS-1$
		assignLink(link2, "http://jakarta.apache.org/commons/io/"); //$NON-NLS-1$

		link3 = new Link(this, SWT.NONE);
		link3.setText("<a>Eclipse SWT</a>"); //$NON-NLS-1$
		jid3Link = new Link(this, SWT.NONE);
		jid3Link.setText("<a>JID3</a>"); //$NON-NLS-1$
		assignLink(jid3Link, "http://jid3.blinkenlights.org/"); //$NON-NLS-1$

		assignLink(link3, "http://eclipse.org/swt"); //$NON-NLS-1$

		link5 = new Link(this, SWT.NONE);
		link5
				.setText(Messages.getString("AboutComposite.developedBy") + " <a>Florian Fankhauser</a> " + Messages.getString("AboutComposite.usingJava") + "."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assignLink(link5, "http://ffxml.net"); //$NON-NLS-1$

		Label translatorLabel = new Label(this, SWT.NONE);
		translatorLabel
				.setText(Messages.getString("AboutComposite.translatedBy") + " " + Messages.getString("AboutComposite.translatorName") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ "."); //$NON-NLS-1$

		link6 = new Link(this, SWT.NONE);
		link6
				.setText(Messages
						.getString("AboutComposite.forMoreInformationVisit") + " <a>www.freeipodsoftware.com</a>."); //$NON-NLS-1$ //$NON-NLS-2$
		assignLink(link6, AppProperties.WEBSITE_URL);

		link4 = new Link(this, SWT.NONE);
		link4
				.setText(Messages.getString("AboutComposite.releasedUnderThe") + " <a>GNU General Public License</a>."); //$NON-NLS-1$ //$NON-NLS-2$
		assignLink(link4, "http://www.gnu.org/licenses/gpl.html"); //$NON-NLS-1$

		label2 = new Label(this, SWT.WRAP);
		label2
				.setText("BECAUSE THE PROGRAM IS LICENSED FREE OF CHARGE, THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW. EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER PARTIES PROVIDE THE PROGRAM \"AS IS\" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR CORRECTION. IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY MODIFY AND/OR REDISTRIBUTE THE PROGRAM AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS OF DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY OTHER PROGRAMS), EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. "); //$NON-NLS-1$
		label2.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_DARK_GRAY));
		label2.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		label2.setLayoutData(gridData11);

		label3 = new Label(this, SWT.NONE);
		label3.setText(Messages.getString("AboutComposite.iPodRegisteredTrademark")); //$NON-NLS-1$
		label3.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		createComposite();
	}

} // @jve:decl-index=0:visual-constraint="10,10"
