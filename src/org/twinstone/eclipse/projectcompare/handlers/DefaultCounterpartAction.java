package org.twinstone.eclipse.projectcompare.handlers;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.twinstone.eclipse.projectcompare.ui.SynchronizeResourceTreeDialog;

public class DefaultCounterpartAction implements IObjectActionDelegate {

	private IWorkbenchPart targetPart;
	
	public DefaultCounterpartAction() {
		/**/
	}

	@Override
	public void run(IAction action) {
		Shell shell = targetPart.getSite().getShell();
		SynchronizeResourceTreeDialog sd = new SynchronizeResourceTreeDialog(shell, null, null);
		sd.open();
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
