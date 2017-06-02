package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

import com.android.uiautomator.UiAutomatorViewer;

public class UndoAction extends Action{
	
	public UndoAction() {
		super();
		setText("Undo");
		setAccelerator(SWT.CTRL + 'Z');
	}
	
	@Override
	public void run() {
		//UiAutomatorViewer.getApp().getmUiAutomatorView().undo();
	}

}
