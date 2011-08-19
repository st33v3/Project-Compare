package org.twinstone.eclipse.projectcompare.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.twinstone.eclipse.projectcompare.ui.SelectProjectDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public SampleHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (!(selection instanceof IStructuredSelection)) return null;
		IStructuredSelection ss = (IStructuredSelection) selection;
		if (ss.size()!=1) return null;
		Object element = ss.getFirstElement();
		if (element instanceof IResource) {
			IProject exclude = ((IResource)element).getProject();
			Shell shell = HandlerUtil.getActiveShell(event);
			SelectProjectDialog spd = new SelectProjectDialog(shell, exclude);
			if (Dialog.OK==spd.open()) {
				MessageDialog.openInformation(shell, "Zdarec", "Project selected:" + spd.getSelectedProject());
			}
		}
		return null;
	}
}
