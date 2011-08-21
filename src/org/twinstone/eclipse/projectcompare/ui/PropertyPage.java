package org.twinstone.eclipse.projectcompare.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.twinstone.eclipse.projectcompare.Activator;

public class PropertyPage extends org.eclipse.ui.dialogs.PropertyPage implements IWorkbenchPropertyPage {

	private TableViewer projectViewer;
	private Button addButton;
	private Button removeButton;
	private Button upButton;
	private Button downButton;
	private Button clearButton;
	private List<RenameItem> items = new ArrayList<RenameItem>();
	private TableViewer renameViewer;
	
	public PropertyPage() {
		/**/
	}

	@Override
	protected Control createContents(Composite parent) {
		SashForm content = new SashForm(parent, SWT.VERTICAL);
		Composite lp = new Composite(content, SWT.NONE);
		GridLayoutFactory.fillDefaults().applyTo(lp);
		new Label(lp, SWT.NONE).setText("Select Counterpart Project");
		Control lpc = createProjectList(lp);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(lpc);
		Composite rp = new Composite(content, SWT.NONE);
		GridLayoutFactory.fillDefaults().applyTo(rp);
		new Label(rp, SWT.NONE).setText("Regular Expressions for Filaname Remaping");
		Control rpc = createRenamePanel(rp);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(rpc);
		fillPageData();
		return content;
	}

	
	private void fillPageData() {
		IProject proj = (IProject) getElement();
		try {
			String pn = proj.getPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, "Project"));
			IProject cp = null;
			if (pn!=null) cp = ResourcesPlugin.getWorkspace().getRoot().getProject(pn);
			if (cp==null) projectViewer.getTable().select(-1);
			else projectViewer.setSelection(new StructuredSelection(cp));
			items.clear();
			items = RenameItem.read(proj);
			renameViewer.refresh();
		} catch (CoreException e) {
			//Ignore the exception
		}
		
	}

	private void updateRenameButtonsEnablement() {
		int si = renameViewer.getTable().getSelectionIndex();
		boolean en = !items.isEmpty() && si>=0;
		removeButton.setEnabled(en);
		downButton.setEnabled(en);
		upButton.setEnabled(en);
		clearButton.setEnabled(en);
	}
	
	private void handleRenameButtons(Button b, boolean def) {
		if (b==addButton) {
			RenameItem it = new RenameItem("","");
			AddRenameDialog d = new AddRenameDialog(getShell(), it);
			if (d.open()==Dialog.OK) {
				items.add(it);
				renameViewer.add(it);
			}
		}
		if (b==removeButton) {
			int idx = renameViewer.getTable().getSelectionIndex();
			if (idx>=0) {
				renameViewer.remove(items.remove(idx));
			}
		}
		if (b==clearButton) { 
			items.clear();
			renameViewer.refresh();
		}
		updateRenameButtonsEnablement();
	}

	private static class RenameLabelProvider extends LabelProvider implements ITableLabelProvider {

		
		@Override
		public String getText(Object element) {
			return super.getText(element);
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			RenameItem it = (RenameItem) element;
			switch (columnIndex) {
				case 0: return it.from;
				case 1: return it.to;
				default: return "Nothing";
			}
		}
	}
	
	private Control createRenamePanel(Composite parent) {
		Composite panel = new Composite(parent, 0);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(panel);
		SelectionListener list = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleRenameButtons((Button) e.widget, false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				handleRenameButtons((Button) e.widget, true);
			}
		};
		renameViewer = new TableViewer(panel,SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		final Table table = renameViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		new TableColumn(table, 0).setText("From");
		new TableColumn(table, 0).setText("To");
		GridDataFactory.fillDefaults().grab(true, true).span(1, 4).applyTo(table);
		renameViewer.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				/**/
			}
			
			@Override
			public void dispose() {
				/**/
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return items.toArray();
			}
		});
		renameViewer.setLabelProvider(new RenameLabelProvider());
		renameViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateRenameButtonsEnablement();
			}
		});
		table.addControlListener(new ControlAdapter() {
			
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle area = table.getClientArea();
				TableColumn[] cols = table.getColumns();
				for (int i=0;i<cols.length;i++) {
					cols[i].setWidth(area.width/cols.length);
				}
			}
		});
		renameViewer.setInput(this);
		addButton = new Button(panel, SWT.PUSH);
		addButton.setText("Add");
		addButton.addSelectionListener(list);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(addButton);
		removeButton = new Button(panel, SWT.PUSH);
		removeButton.setText("Remove");
		removeButton.addSelectionListener(list);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(removeButton);
		upButton = new Button(panel, SWT.PUSH);
		upButton.setText("Up");
		upButton.addSelectionListener(list);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(upButton);
		downButton = new Button(panel, SWT.PUSH);
		downButton.setText("Down");
		downButton.addSelectionListener(list);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(downButton);
		return panel;
	}

	private Control createProjectList(Composite parent) {
		projectViewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		projectViewer.setContentProvider(new WorkbenchContentProvider());
		projectViewer.setLabelProvider(WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider());
		projectViewer.setInput(ResourcesPlugin.getWorkspace());
		projectViewer.setComparator(new ViewerComparator());
		projectViewer.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				return element!=getElement();
			}
		});
		return projectViewer.getTable();
	}

	protected void performDefaults() {
		super.performDefaults();
		projectViewer.getTable().select(-1);
		items.clear();
		renameViewer.refresh();
	}
	
	public boolean performOk() {
		try {
			String pname = null;
			IStructuredSelection selection = (IStructuredSelection) projectViewer.getSelection();
			if (selection!=null && !selection.isEmpty()) pname = ((IProject)selection.getFirstElement()).getName();
			IProject proj = (IProject) getElement();
			proj.setPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, "Project"),pname);
			RenameItem.store(proj, items);
		} catch (CoreException e) {
			return false;
		}
		return true;
	}


}
