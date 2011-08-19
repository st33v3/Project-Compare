package org.twinstone.eclipse.projectcompare.handlers;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class DefaultCounterpartAction implements IObjectActionDelegate {

	private IWorkbenchPart targetPart;
	
	public DefaultCounterpartAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		MessageDialog.openInformation(
				targetPart.getSite().getShell(),
				"Z",
				"Hello, Eclipse world");

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;

	}

}
