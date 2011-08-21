package org.twinstone.eclipse.projectcompare.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.ISaveablesSource;
import org.eclipse.ui.Saveable;

//implements ISaveablesSource
public class CompareInput extends CompareEditorInput  {

	private IResource left;
	private IResource right;
	
	public CompareInput(CompareConfiguration configuration, IResource left, IResource right) {
		super(configuration);
		this.left = left;
		this.right = right;
	}

	@Override
	protected Object prepareInput(IProgressMonitor monitor)	throws InvocationTargetException, InterruptedException {
		ResourceNode rl = new ResourceNode(left);
		ResourceNode rr = new ResourceNode(right);
		Differencer d = new Differencer(); 
		 Object diff = d.findDifferences(
				 false,
				 monitor,
				 null,
				 null,
				 rl,
				 rr); 
		 return diff;
	}

	@Override
	public void saveChanges(IProgressMonitor monitor) throws CoreException {
		super.saveChanges(monitor);
		MessageDialog.openConfirm(null, "asd", "asdf");
	}

//	@Override
//	public Saveable[] getSaveables() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Saveable[] getActiveSaveables() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	
}
