package com.freeipodsoftware.abc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

public class InputFileSelection extends InputFileSelectionGui {

	public static final String FILE_LIST_CHANGED_EVENT = "fileListChangedEvent"; //$NON-NLS-1$

	class MyKeyListener extends KeyAdapter implements KeyListener {
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			if (e.keyCode == SWT.DEL) {
				removeInputFiles();
			} else if (e.keyCode == 'a' && e.stateMask == SWT.CONTROL) {
				list.selectAll();
			}
		}
	}

	private String lastFolder;

	private EventDispatcher eventDispatcher;

	public InputFileSelection(Composite parent, int style) {
		super(parent, style);

		addButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						addInputFile();
					}
				});

		removeButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						removeInputFiles();
					}
				});

		clearButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						list.removeAll();
					}
				});

		moveUpButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						moveUp();
					}
				});

		moveDownButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						moveDown();
					}
				});

		createDropTarget();
		list.addKeyListener(new MyKeyListener());
		addButton.setFocus();
	}

	private void createDropTarget() {
		DropTarget target = new DropTarget(list, DND.DROP_COPY | DND.DROP_MOVE
				| DND.DROP_DEFAULT);
		target.setTransfer(new Transfer[] { FileTransfer.getInstance(),
				TextTransfer.getInstance() });
		target.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {
				if (FileTransfer.getInstance().isSupportedType(
						event.currentDataType)) {
					String[] files = (String[]) event.data;
					for (int i = 0; i < files.length; i++) {
						list.add(files[i]);
					}

					eventDispatcher.raiseEvent(FILE_LIST_CHANGED_EVENT);
				}
			}
		});
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		SwtUtils.setEnabledRecursive(this, enabled);
	}

	public int getButtonWidthHint() {
		return addButton.getSize().x;
	}

	private void addInputFile() {
		FileDialog fileDialog = new FileDialog(this.getShell(), SWT.MULTI
				| SWT.OPEN);
		if (lastFolder != null) {
			fileDialog.setFileName(lastFolder);
		}
		fileDialog
				.setFilterNames(new String[] {
						Messages.getString("InputFileSelection.mp3Files"), Messages.getString("InputFileSelection.allFiles") }); //$NON-NLS-1$ //$NON-NLS-2$
		fileDialog.setFilterExtensions(new String[] { "*.mp3", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
		String firstFile = fileDialog.open();
		if (firstFile != null) {
			lastFolder = firstFile;
			String[] fileNames = fileDialog.getFileNames();

			for (int i = 0; i < fileNames.length; i++) {
				String filterPath = fileDialog.getFilterPath();
				list.add(filterPath + System.getProperty("file.separator") //$NON-NLS-1$
						+ fileNames[i]);
			}

			eventDispatcher.raiseEvent(FILE_LIST_CHANGED_EVENT);
		}
	}

	private void removeInputFiles() {
		list.remove(list.getSelectionIndices());
		eventDispatcher.raiseEvent(FILE_LIST_CHANGED_EVENT);
	}

	private void moveDown() {
		if (list.getSelectionCount() == 1) {
			int selectionIndex = list.getSelectionIndex();
			if (selectionIndex < list.getItemCount() - 1) {
				list.add(list.getItem(selectionIndex), selectionIndex + 2);
				list.remove(selectionIndex);
				list.setSelection(selectionIndex + 1);

				eventDispatcher.raiseEvent(FILE_LIST_CHANGED_EVENT);
			}
		}
	}

	private void moveUp() {
		if (list.getSelectionCount() == 1) {
			int selectionIndex = list.getSelectionIndex();
			if (selectionIndex > 0) {
				list.add(list.getItem(selectionIndex), selectionIndex - 1);
				list.remove(selectionIndex + 1);
				list.setSelection(selectionIndex - 1);

				eventDispatcher.raiseEvent(FILE_LIST_CHANGED_EVENT);
			}
		}
	}

	public String[] getFileList() {
		return list.getItems();
	}

	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}

}
