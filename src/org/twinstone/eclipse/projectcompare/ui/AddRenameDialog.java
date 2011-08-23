package org.twinstone.eclipse.projectcompare.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.twinstone.eclipse.projectcompare.RenameItem;

public class AddRenameDialog extends Dialog {

	private RenameItem item;
	
	private Text fromField;
	private Text toField;

	public AddRenameDialog(Shell parentShell, RenameItem item) {
		super(parentShell);
		this.item = item;
	}

	
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Add name mapping");
		Composite composite = (Composite) super.createDialogArea(parent);
		FieldDecoration dec= FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
		int w = dec.getImage().getBounds().width;
		((GridLayout)composite.getLayout()).numColumns = 2;
		new Label(composite, SWT.NONE).setText("From");
		fromField = new Text(composite, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true,false).indent(w, 0).applyTo(fromField);
		new Label(composite, SWT.NONE).setText("To");
		toField = new Text(composite, SWT.BORDER);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).indent(w, 0).applyTo(toField);
		return composite;
	}


	@Override
	protected void okPressed() {
		item.from = fromField.getText();
		item.to = toField.getText();
		super.okPressed();
	}
	
	
}


