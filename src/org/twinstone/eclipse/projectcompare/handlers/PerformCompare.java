package org.twinstone.eclipse.projectcompare.handlers;

import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.twinstone.eclipse.projectcompare.Activator;
import org.twinstone.eclipse.projectcompare.RenameItem;
import org.twinstone.eclipse.projectcompare.ui.CompareInput;

public class PerformCompare {
	
	private IFile file;
	private Shell shell;
	
	
	public PerformCompare(IFile file, Shell shell) {
		super();
		this.file = file;
		this.shell = shell;
	}

	public void run() throws CoreException {
		IProject project = file.getProject();
		IProject cp;
		do {
			String pn = project.getPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, "Project"));
			cp = project.getWorkspace().getRoot().getProject(pn);
			if (cp==null) {
				PreferenceDialog dialog = PreferencesUtil.createPropertyDialogOn(shell, project, "org.twinstone.eclipse.projectcompare.PropertyPage", null, null, 0);
				int ret = dialog.open();
				if (ret==Dialog.CANCEL) return;
			}
		} while (cp==null);
		List<RenameItem> items = RenameItem.read(project);
		IPath path = file.getProjectRelativePath();
		String sp = path.toOSString();
		for (RenameItem it : items) {
			String s = sp.replaceFirst(it.from, it.to);
			if (s!=sp) {
				sp = s;
				break;
			}
		}
		IPath p = Path.fromOSString(sp);
		IFile f = cp.getFile(p);
		if (f==null || !f.exists()) {
			MessageDialog.openConfirm(shell, "No counterpart", "Coupterpart "+sp+" not found in project "+cp.getName());
		} else {
			CompareConfiguration cc = new CompareConfiguration();
			CompareInput ci = new CompareInput(cc, file, f);
			CompareUI.openCompareEditor(ci);
		}
	}
}
