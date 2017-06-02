package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

import com.android.uiautomator.UiAutomatorViewer;

public class SelectAllAction extends Action{
	
	public SelectAllAction() {
		super();
		setText("Select All");
		setAccelerator(SWT.CTRL + 'A');
	}
	
	@Override
	public void run() {
		UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer().selectAll();
	}

}
