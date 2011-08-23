package org.twinstone.eclipse.projectcompare.ui;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

//implements ISaveablesSource
public class CompareInput extends CompareEditorInput  {

	private IResource left;
	private IResource right;
	private ResourceNode rl;
	private ResourceNode rr;
	
	public CompareInput(CompareConfiguration configuration, IResource left, IResource right) {
		super(configuration);
		this.left = left;
		this.right = right;
	}

	@Override
	protected Object prepareInput(IProgressMonitor monitor)	throws InvocationTargetException, InterruptedException {
		rl = new ResourceNode(left);
		rr = new ResourceNode(right);
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
		if (rl!=null) {
			byte[] content = rl.getContent();
			((IFile)left).setContents(new ByteArrayInputStream(content), IFile.KEEP_HISTORY, monitor);
		}
		if (rr!=null) {
			byte[] content = rr.getContent();
			((IFile)right).setContents(new ByteArrayInputStream(content), IFile.KEEP_HISTORY, monitor);
		}
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
