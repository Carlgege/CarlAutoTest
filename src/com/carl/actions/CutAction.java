package com.carl.actions;

import org.eclipse.jface.action.Action;
//import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

import com.android.uiautomator.UiAutomatorViewer;

//import com.carl.jfacetest.JFaceTest;

public class CutAction extends Action{
	
	public CutAction() {
		super();
		setText("Cut(&C)");
		setToolTipText("Cut");
		setAccelerator(SWT.CTRL + 'X');
		//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\cut.gif"));
	}
	
	@Override
	public void run() {
		//JFaceTest.getApp().getContent().cut();.
		//UiAutomatorViewer.getApp().getStyledText().cut();
		if (UiAutomatorViewer.getApp().getShell().getDisplay().getFocusControl().equals(UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer())) {
			UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer().cut();
		}
		
		/*
		if (UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedStyledText() == null) {
			return;
		}
		*/
	}

}
