package org.twinstone.eclipse.projectcompare.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class PropertyPage extends org.eclipse.ui.dialogs.PropertyPage implements IWorkbenchPropertyPage {

	private static class Item {
		
		public Item(String from, String to) {
			super();
			this.from = from;
			this.to = to;
		}
		
		public String from;
		public String to;
	}
	
	private TableViewer projectViewer;
	private Button addButton;
	private Button removeButton;
	private Button upButton;
	private Button downButton;
	private List<Item> items = new ArrayList<Item>();
	private TableViewer renameViewer;
	
	public PropertyPage() {
		items.add(new Item("a","b"));
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
		return content;
	}

	private void updateRenameButtonsEnablement() {
		int si = renameViewer.getTable().getSelectionIndex();
		boolean en = !items.isEmpty() && si>=0;
		removeButton.setEnabled(en);
		downButton.setEnabled(en);
		upButton.setEnabled(en);
	}
	
	private void handleRenameButtons(Button b, boolean def) {
		
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
			Item it = (Item) element;
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
		renameViewer = new TableViewer(panel,SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		new TableColumn(renameViewer.getTable(), 0).setText("From");
		new TableColumn(renameViewer.getTable(), 0).setText("To");
		GridDataFactory.fillDefaults().grab(true, true).span(1, 4).applyTo(renameViewer.getTable());
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
		renameViewer.setInput(this);
		addButton = new Button(panel, SWT.PUSH);
		addButton.setText("Add");
		addButton.addSelectionListener(list);
		GridDataFactory.fillDefaults().applyTo(addButton);
		removeButton = new Button(panel, SWT.PUSH);
		removeButton.setText("Remove");
		removeButton.addSelectionListener(list);
		GridDataFactory.fillDefaults().applyTo(removeButton);
		upButton = new Button(panel, SWT.PUSH);
		upButton.setText("Up");
		upButton.addSelectionListener(list);
		GridDataFactory.fillDefaults().applyTo(upButton);
		downButton = new Button(panel, SWT.PUSH);
		downButton.setText("Down");
		downButton.addSelectionListener(list);
		GridDataFactory.fillDefaults().applyTo(downButton);
		return panel;
	}

	private Control createProjectList(Composite parent) {
		projectViewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
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

}
