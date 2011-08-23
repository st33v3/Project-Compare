package org.twinstone.eclipse.projectcompare.handlers;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.ide.ResourceUtil;
import org.twinstone.eclipse.projectcompare.Activator;

public class DefaultCounterpartAction implements IObjectActionDelegate {

	private IWorkbenchPart targetPart;
	private ResourceMapping mapping;
	
	public DefaultCounterpartAction() {
		/**/
	}

	@Override
	public void run(IAction action) {
		Shell shell = targetPart.getSite().getShell();
		IFile file = ResourceUtil.getFile(mapping);
		if (file==null) return;
		try {
			new PerformCompare(file, shell).run();
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(e.getStatus());
		} 
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		mapping = null;
		if (!(selection instanceof IStructuredSelection)) return;
		IStructuredSelection ss = (IStructuredSelection) selection;
		if (ss.size()!=1) return;
		Object o = ss.getFirstElement();
		if (!(o instanceof ResourceMapping)) return;
		mapping = (ResourceMapping) o;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
		Object adapter = targetPart.getAdapter(ResourceMapping.class);
		if (adapter!=null) mapping = (ResourceMapping) adapter;
	}

}
