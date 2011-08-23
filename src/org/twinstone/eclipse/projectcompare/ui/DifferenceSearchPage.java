package org.twinstone.eclipse.projectcompare.ui;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class DifferenceSearchPage extends DialogPage implements ISearchPage {

	private ISearchPageContainer container;
	
	public DifferenceSearchPage() {
		/**/
	}

	public DifferenceSearchPage(String title) {
		super(title);
		/**/
	}

	public DifferenceSearchPage(String title, ImageDescriptor image) {
		super(title, image);
		/**/
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(3).applyTo(composite);
		new Label(composite, SWT.NONE).setText("Source Project");
		new Text(composite, SWT.BORDER);
		Button button = new Button(composite, SWT.PUSH);
		button.setText("Browse ...");
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ListDialog ld = new ListDialog(getShell());
				ld.setContentProvider(new WorkbenchContentProvider());
				ld.setLabelProvider(new WorkbenchLabelProvider());
				ld.setInput(ResourcesPlugin.getWorkspace().getRoot());
				ld.open();
			}
			
		});
		
		
		setControl(composite);
	}


	@Override
	public boolean performAction() {
		MessageDialog.openConfirm(getShell(), getTitle(), "Zdarec");
		return false;
	}

	@Override
	public void setContainer(ISearchPageContainer container) {
		this.container = container;
		
	}

}
