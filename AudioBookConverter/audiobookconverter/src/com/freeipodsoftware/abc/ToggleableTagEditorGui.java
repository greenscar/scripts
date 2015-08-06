package com.freeipodsoftware.abc;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;

public class ToggleableTagEditorGui extends Composite {

	private static final String A_TAG_CLOSING = "</a>"; //$NON-NLS-1$

	private static final String A_TAG = "<a>"; //$NON-NLS-1$

	private static final String SHOW_TAG_EDITOR = Messages
			.getString("ToggleableTagEditorGui.showTagEditor"); //$NON-NLS-1$

	private static final String HIDE_TAG_EDITOR = Messages
			.getString("ToggleableTagEditorGui.hideTagEditor"); //$NON-NLS-1$

	public static final String TOGGLEABLE_TAG_EDITOR_VISIBLE = "toggleableTagEditor.visible"; //$NON-NLS-1$

	private Link toggleLink = null;

	private TagEditor tagEditor = null;

	private Label infoLabel = null;

	public ToggleableTagEditorGui(Composite parent, int style) {
		super(parent, style);
		initialize();

		setTagEditorVisible(AppProperties
				.getBooleanProperty(TOGGLEABLE_TAG_EDITOR_VISIBLE));

		layout(true);
	}

	public TagEditor getTagEditor() {
		return tagEditor;
	}

	private void initialize() {
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.numColumns = 2;
		toggleLink = new Link(this, SWT.NONE);
		// toggleLink.setText(A_TAG + HIDE_TAG_EDITOR + A_TAG_CLOSING);
		infoLabel = new Label(this, SWT.NONE);
		infoLabel.setText("Label"); //$NON-NLS-1$
		infoLabel.setLayoutData(gridData1);
		toggleLink
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						setTagEditorVisible(!tagEditor.isVisible());
						getParent().layout(true);
						tagEditor.setFocus();
					}
				});
		createTagEditor();
		updateToggleLinkText();
		this.setLayout(gridLayout);
		setSize(new Point(351, 152));
	}

	private void setTagEditorVisible(boolean visible) {
		if (visible) {
			tagEditor.setVisible(true);
		} else {
			tagEditor.setVisible(false);
		}
		updateToggleLinkText();
		AppProperties
				.setBooleanProperty(TOGGLEABLE_TAG_EDITOR_VISIBLE, visible);
	}

	/**
	 * This method initializes tagEditor
	 * 
	 */
	private void createTagEditor() {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		tagEditor = new TagEditor(this, SWT.NONE);
		tagEditor.setLayoutData(gridData);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		SwtUtils.setEnabledRecursive(this, enabled);
		updateToggleLinkText();
	}

	private void updateToggleLinkText() {
		if (isEnabled()) {
			infoLabel.setText(""); //$NON-NLS-1$
		} else {
			infoLabel.setText("(" //$NON-NLS-1$
					+ Messages.getString("ToggleableTagEditorGui.onlyAvailableWhenConvertingToOneFile") //$NON-NLS-1$
					+ ")"); //$NON-NLS-1$
		}

		if (tagEditor.isVisible()) {
			toggleLink.setText(A_TAG + HIDE_TAG_EDITOR + A_TAG_CLOSING);
		} else {
			toggleLink.setText(A_TAG + SHOW_TAG_EDITOR + A_TAG_CLOSING);
		}
		layout(true);
	}
} // @jve:decl-index=0:visual-constraint="10,10"
