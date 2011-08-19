package org.twinstone.eclipse.projectcompare.ui;

import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SynchronizeResourceTreeDialog extends ProgressMonitorDialog {

	public SynchronizeResourceTreeDialog(Shell parent, IContainer folderA, IContainer folderB) {
		super(parent);
		message = "Resource synchronization";
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		Label hu = new Label(composite, 0);
		hu.setText("Hu");
		return composite;
	}

	
}
