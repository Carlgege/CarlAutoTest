package com.carl.actions;

import org.eclipse.jface.action.Action;
//import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

import com.android.uiautomator.UiAutomatorViewer;

//import com.carl.jfacetest.JFaceTest;

public class PasteAction extends Action{
	
	public PasteAction() {
		super();
		setText("Paste(&P)");
		setToolTipText("Paste");
		setAccelerator(SWT.CTRL + 'V');
		//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\cut.gif"));
	}
	
	@Override
	public void run() {
		//JFaceTest.getApp().getContent().paste();
		//UiAutomatorViewer.getApp().getStyledText().paste();
		if (UiAutomatorViewer.getApp().getShell().getDisplay().getFocusControl().equals(UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer())) {
			UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer().paste();
		}
		
		/*
		if (UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedStyledText() == null) {
			return;
		}
		*/
	}

}
