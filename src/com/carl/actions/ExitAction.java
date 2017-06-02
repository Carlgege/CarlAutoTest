package com.carl.actions;

import org.eclipse.jface.action.Action;
//import org.eclipse.jface.resource.ImageDescriptor;

import com.android.uiautomator.UiAutomatorViewer;

//import com.carl.jfacetest.JFaceTest;

public class ExitAction extends Action{
	
	public ExitAction() {
		super();
		setText("Exit(&E)");
		setToolTipText("Exit this tool");
		//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\exit.gif"));
	}
	
	@Override
	public void run() {
		UiAutomatorViewer.getApp().close();
	}
}
