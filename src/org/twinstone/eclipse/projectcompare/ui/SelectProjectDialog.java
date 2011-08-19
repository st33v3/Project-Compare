package org.twinstone.eclipse.projectcompare.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.model.WorkbenchContentProvider;

public class SelectProjectDialog extends Dialog {

	private TableViewer tableViewer;
	private IProject project;
	
	public SelectProjectDialog(IShellProvider parentShell, IProject project) {
		super(parentShell);
		this.project = project;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	public SelectProjectDialog(Shell shell, IProject project) {
		super(shell);
		this.project = project;
	}

	public IProject getSelectedProject() {
		return (IProject) ((IStructuredSelection)tableViewer.getSelection()).getFirstElement();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Select a counterpart project");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        tableViewer = new TableViewer(composite, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(tableViewer.getTable());
        tableViewer.setLabelProvider(WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider());
        tableViewer.setContentProvider(new WorkbenchContentProvider());
        tableViewer.setComparator(new ViewerComparator());
        tableViewer.addFilter(new ViewerFilter() {
			
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				return element!=project;
			}
		});
        tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				setReturnCode(OK);
				close();
				
			}
		});
        tableViewer.setInput(project.getWorkspace());

        return composite;
	}

}
