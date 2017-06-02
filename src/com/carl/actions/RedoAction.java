package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

import com.android.uiautomator.UiAutomatorViewer;

public class RedoAction extends Action{
	
	public RedoAction() {
		super();
		setText("Redo");
		setAccelerator(SWT.CTRL + 'Y');
	}
	
	@Override
	public void run() {
		//UiAutomatorViewer.getApp().getmUiAutomatorView().redo();
	}

}
