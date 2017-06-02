package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

public class AboutAction extends Action{
	
	public AboutAction() {
		super();
		setText("About Author");
		//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "images/D.png"));
	}
	
	@Override
	public void run() {
		
		MessageDialog.openInformation(null, "About", "Author: Chen liguo\n"
				+ "Version: 2016-03-10\n"
				+ "EXT: 579-26187\n"
				+ "Mail: carl.lg.chen@mail.foxconn.com\n"
				+ "\n"
				+ "Thanks:"
				+ "Poker,Yana,Rain -- Requirement and feedback");
		//Link link = new Link(UiAutomatorViewer.getApp().getShell(), SWT.NONE);
		//link.setText("Mail: <A href=\"carl.lg.chen@mail.foxconn.com\">");
		//"Mail: <A href=\"carl.lg.chen@mail.foxconn.com\">"
		
		
	}

}
