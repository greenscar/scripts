package com.freeipodsoftware.abc;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;

public class OptionPanel extends OptionPanelGui {

	public static final String OPTION_PANEL_SINGLE_OUTPUT_FILE_MODE = "optionPanel.singleOutputFileMode"; //$NON-NLS-1$

	private Set<OptionChangedListener> optionChangedListenerSet;

	private SelectionListener outputFileSelectionListener;

	public OptionPanel(Composite parent, int style) {
		super(parent, style);
		optionChangedListenerSet = new HashSet<OptionChangedListener>();
		outputFileSelectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fireOptionChanged();
				AppProperties.setBooleanProperty(
						OPTION_PANEL_SINGLE_OUTPUT_FILE_MODE,
						isSingleOutputFileMode());
			}
		};
		oneOutputFileOption.addSelectionListener(outputFileSelectionListener);
		oneOutputFilePerInputFileOption
				.addSelectionListener(outputFileSelectionListener);

		setSingleOutputFileMode(AppProperties
				.getBooleanProperty(OPTION_PANEL_SINGLE_OUTPUT_FILE_MODE));
	}

	void addOptionChangedListener(OptionChangedListener listener) {
		optionChangedListenerSet.add(listener);
	}

	protected void fireOptionChanged() {
		for (OptionChangedListener optionChangedListener : optionChangedListenerSet) {
			optionChangedListener.optionChanged();
		}
	}

	public boolean isSingleOutputFileMode() {
		return oneOutputFileOption.getSelection();
	}

	private void setSingleOutputFileMode(boolean singleOuputFileMode) {
		if (singleOuputFileMode) {
			oneOutputFileOption.setSelection(true);
		} else {
			oneOutputFilePerInputFileOption.setSelection(true);
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		SwtUtils.setEnabledRecursive(this, enabled);
	}
}
