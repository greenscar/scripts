package com.freeipodsoftware.abc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TagEditorGui extends Composite {

	private static final String[] genres = new String[] { "Alternative", //$NON-NLS-1$
			"Blues", "Books & Spoken", "Children's Music", "Classical", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"Comedy", "Country", "Dance", "Easy Listening", "Electronic", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"Folk", "Funk", "House", "Hip Hop", "Indie", "Industrial", "Jazz", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"Metal", "Misc", "New Age", "Poscast", "Pop", "Punk", "Religious", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
			"Rock", "Reggae", "Soft Rock", "Soundtrack", "Techno", "Trance", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"Unclassifiable", "World" }; //$NON-NLS-1$ //$NON-NLS-2$

	private Composite tagsComposite = null;

	private Label label = null;

	protected Text artistText = null;

	private Label label1 = null;

	protected Text writerText = null;

	private Label label2 = null;

	protected Text titleText = null;

	private Label label3 = null;

	protected Text albumText = null;

	private Label label4 = null;

	protected Combo genreCombo = null;

	private Label label5 = null;

	protected Text yearText = null;

	private Label label6 = null;

	protected Text trackText = null;

	private Label label7 = null;

	protected Text discText = null;

	private Label label8 = null;

	protected Text commentText = null;

	private Label label9 = null;

	private Label label10 = null;

	public TagEditorGui(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 1;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 0;
		createTagsComposite();
		this.setLayout(gridLayout1);
		this.setSize(new Point(490, 145));
	}

	/**
	 * This method initializes tagsComposite
	 * 
	 */
	private void createTagsComposite() {
		GridData gridData22 = new GridData();
		gridData22.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData15 = new GridData();
		gridData15.grabExcessHorizontalSpace = true;
		gridData15.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData14 = new GridData();
		gridData14.grabExcessHorizontalSpace = false;
		GridData gridData13 = new GridData();
		gridData13.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData13.grabExcessHorizontalSpace = true;
		GridData gridData12 = new GridData();
		gridData12.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData111 = new GridData();
		gridData111.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData41 = new GridData();
		gridData41.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData31 = new GridData();
		gridData31.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData31.grabExcessHorizontalSpace = false;
		GridData gridData21 = new GridData();
		gridData21.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData21.grabExcessHorizontalSpace = false;
		GridData gridData11 = new GridData();
		gridData11.horizontalSpan = 5;
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.horizontalSpan = 2;
		gridData3.grabExcessHorizontalSpace = true;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.horizontalSpan = 2;
		gridData2.grabExcessHorizontalSpace = true;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.horizontalSpan = 2;
		gridData1.grabExcessHorizontalSpace = true;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		tagsComposite = new Composite(this, SWT.NONE);
		tagsComposite.setLayout(gridLayout);
		tagsComposite.setLayoutData(gridData13);
		label = new Label(tagsComposite, SWT.NONE);
		label.setText(Messages.getString("TagEditorGui.artist")); //$NON-NLS-1$
		label.setLayoutData(gridData8);
		artistText = new Text(tagsComposite, SWT.BORDER);
		artistText.setText(""); //$NON-NLS-1$
		artistText.setLayoutData(gridData1);
		label1 = new Label(tagsComposite, SWT.NONE);
		label1.setText(Messages.getString("TagEditorGui.writer")); //$NON-NLS-1$
		label1.setLayoutData(gridData12);
		writerText = new Text(tagsComposite, SWT.BORDER);
		writerText.setLayoutData(gridData3);
		label2 = new Label(tagsComposite, SWT.NONE);
		label2.setText(Messages.getString("TagEditorGui.title")); //$NON-NLS-1$
		label2.setLayoutData(gridData7);
		titleText = new Text(tagsComposite, SWT.BORDER);
		titleText.setLayoutData(gridData);
		label3 = new Label(tagsComposite, SWT.NONE);
		label3.setText(Messages.getString("TagEditorGui.album")); //$NON-NLS-1$
		label3.setLayoutData(gridData111);
		albumText = new Text(tagsComposite, SWT.BORDER);
		albumText.setLayoutData(gridData2);
		label4 = new Label(tagsComposite, SWT.NONE);
		label4.setText(Messages.getString("TagEditorGui.genre")); //$NON-NLS-1$
		label4.setLayoutData(gridData6);
		createGenreCombo();
		label5 = new Label(tagsComposite, SWT.NONE);
		label5.setText(Messages.getString("TagEditorGui.year")); //$NON-NLS-1$
		label5.setLayoutData(gridData10);
		yearText = new Text(tagsComposite, SWT.BORDER);
		yearText.setLayoutData(gridData14);
		@SuppressWarnings("unused") //$NON-NLS-1$
		Label filler = new Label(tagsComposite, SWT.NONE);
		label6 = new Label(tagsComposite, SWT.NONE);
		label6.setText(Messages.getString("TagEditorGui.track")); //$NON-NLS-1$
		label6.setLayoutData(gridData5);
		trackText = new Text(tagsComposite, SWT.BORDER);
		trackText.setLayoutData(gridData21);
		label9 = new Label(tagsComposite, SWT.NONE);
		label9.setText(Messages.getString("TagEditorGui.numberTotal")); //$NON-NLS-1$
		label9.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.ITALIC)); //$NON-NLS-1$
		label9.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		label9.setLayoutData(gridData15);
		label9.setEnabled(true);
		label7 = new Label(tagsComposite, SWT.NONE);
		label7.setText(Messages.getString("TagEditorGui.disc")); //$NON-NLS-1$
		label7.setLayoutData(gridData9);
		discText = new Text(tagsComposite, SWT.BORDER);
		discText.setLayoutData(gridData31);
		label10 = new Label(tagsComposite, SWT.NONE);
		label10.setText(Messages.getString("TagEditorGui.numberTotal")); //$NON-NLS-1$
		label10.setEnabled(true);
		label10
				.setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.ITALIC)); //$NON-NLS-1$
		label10.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		label10.setLayoutData(gridData22);
		label8 = new Label(tagsComposite, SWT.NONE);
		label8.setText(Messages.getString("TagEditorGui.comment")); //$NON-NLS-1$
		label8.setLayoutData(gridData41);
		commentText = new Text(tagsComposite, SWT.BORDER);
		commentText.setLayoutData(gridData11);
	}

	/**
	 * This method initializes genreCombo
	 * 
	 */
	private void createGenreCombo() {
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.horizontalSpan = 2;
		gridData4.grabExcessHorizontalSpace = true;
		genreCombo = new Combo(tagsComposite, SWT.NONE);
		genreCombo.setLayoutData(gridData4);
		fillGenres();
	}

	private void fillGenres() {
		for (int i = 0; i < genres.length; i++) {
			genreCombo.add(genres[i]);
		}
	}

} // @jve:decl-index=0:visual-constraint="10,10"
