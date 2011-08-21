package org.twinstone.eclipse.projectcompare.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.twinstone.eclipse.projectcompare.Activator;

public class RenameItem {
	
	public RenameItem(String from, String to) {
		super();
		this.from = from;
		this.to = to;
	}
	
	public String from;
	public String to;
	
	public static List<RenameItem> read(IProject project) throws CoreException {
		ArrayList<RenameItem> ret = new ArrayList<RenameItem>();
		String c =  project.getPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, "RemapSize"));
		int size = 0; 
		try {
			if (c!=null) size = Integer.parseInt(c);
		} catch (NumberFormatException e) {
			//Ignore
		}
		for (int i=0;i<size;i++) {
			String from = project.getPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, "Item"+i+"From"));
			String to = project.getPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, "Item"+i+"To"));
			ret.add(new RenameItem(from, to));
		}
		return ret;
		
	}
	
	public static void store(IProject project, List<RenameItem> items) throws CoreException {
		Map<QualifiedName, String> properties = project.getPersistentProperties();
		for (Map.Entry<QualifiedName, String> p : properties.entrySet()) {
			if (p.getKey().getQualifier().equals(Activator.PLUGIN_ID) && p.getKey().getLocalName().startsWith("Item")) {
				project.setPersistentProperty(p.getKey(), null); 
			}
		}
		project.setPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, "RemapSize"),""+items.size());
		int i = 0;
		for (RenameItem it : items) {
			project.setPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, "Item"+i+"From"),""+it.from);
			project.setPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, "Item"+i+"To"),""+it.to);
			i++;
		}
		
	}
}