package org.twinstone.eclipse.projectcompare.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class SelectProjectDialog extends ListDialog {

	private IProject project;

	public SelectProjectDialog(Shell shell, IProject project) {
		super(shell);
		this.project = project;
        setLabelProvider(WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider());
        setContentProvider(new WorkbenchContentProvider());
        setTitle("Select project");
        setMessage("Select a counterpart project");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        TableViewer tableViewer = getTableViewer();
        tableViewer.setComparator(new ViewerComparator());
        tableViewer.addFilter(new ViewerFilter() {
			
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				return element!=project;
			}
		});
        tableViewer.setInput(project.getWorkspace());

        return composite;
	}

}
