package com.carl.actions;

import org.eclipse.jface.action.Action;
//import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import com.android.uiautomator.UiAutomatorViewer;


public class CopyAction extends Action{
	
	public CopyAction() {
		super();
		setText("Copy(&C)");
		setToolTipText("Copy");
		setAccelerator(SWT.CTRL + 'C');
		//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\cut.gif"));
	}
	
	@Override
	public void run() {
		
		if (UiAutomatorViewer.getApp().getShell().getDisplay().getFocusControl().equals(UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer())) {
			UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer().copy();
		}
		
		if (UiAutomatorViewer.getApp().getShell().getDisplay().getFocusControl().getParent().equals(UiAutomatorViewer.getApp().getmUiAutomatorView().getmTableViewer().getTable())) {
			int tableIndex = UiAutomatorViewer.getApp().getmUiAutomatorView().getmTableViewer().getTable().getSelectionIndex();
			String selectedText = UiAutomatorViewer.getApp().getmUiAutomatorView().getmTableViewer().getTable().getItem(tableIndex).getText(1);
			Object[] data = new Object[]{selectedText};
			Transfer[] types = new Transfer[]{TextTransfer.getInstance()};
			UiAutomatorViewer.getApp().getmClipboard().setContents(data, types);
			//System.out.println(UiAutomatorViewer.getApp().getmUiAutomatorView().getmTableViewer().getTable().getSelection().toString());
		}
		
		if (UiAutomatorViewer.getApp().getShell().getDisplay().getFocusControl().equals(UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedLogItem())) {
			UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedLogItem().copy();
		}
		
		//System.out.println(UiAutomatorViewer.getApp().getShell().getDisplay().getFocusControl());
		
		
	}

}
