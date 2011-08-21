package org.twinstone.eclipse.projectcompare.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

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
		IFile file;
		if (!(selection instanceof IStructuredSelection)) {
			IEditorPart editor = HandlerUtil.getActiveEditor(event);
			if (editor==null) return null;
			IResource res = (IResource) editor.getEditorInput().getAdapter(IResource.class);
			file = res instanceof IFile ? (IFile) res : null;
			if (file==null) return null;
		} else {
			IStructuredSelection ss = (IStructuredSelection) selection;
			if (ss.size()!=1) return null;
			Object element = ss.getFirstElement();
			file = null;
			if (element instanceof IFile) file = (IFile) element;
			else {
				IAdapterManager manager = Platform.getAdapterManager();
				IResource res = (IResource) manager.getAdapter(element, IResource.class); 
				if (res instanceof IFile) file = (IFile) res; 
			}
			if (file==null) return null;
		}
		Shell shell = HandlerUtil.getActiveShell(event);
		try {
			new PerformCompare(file, shell).run();
		} catch (CoreException e) {
			throw new ExecutionException("Can not compare", e);
		}
		return null;
	}
}
