package com.carl.actions;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.action.Action;

public class HelpAction extends Action{
	
	public static final String XLSX = "Function Details";
	public static final String PPTX = "CAT Introduction";
	
	private String helpOption;
	
	public HelpAction (String option) {
		super();
		setText("Help(&H)");
		//setToolTipText("Help");
		
		helpOption = option;
		initAction();
	}
	
	private void initAction() {
		if (helpOption.equals(PPTX)) {
			this.setText(PPTX);
			
			//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\font.gif"));
		} else if (helpOption.equals(XLSX)) {
			this.setText(XLSX);
			
			//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\forecolor.gif"));
		}
	}
	
	@Override
	public void run() {
		
		if (helpOption.equals(PPTX)) {
			
			try {
				java.awt.Desktop.getDesktop().open(new File("..\\Doc\\CATIntroduction.pptx"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (helpOption.equals(XLSX)) {
			try {
				java.awt.Desktop.getDesktop().open(new File("..\\Doc\\Help.xlsx"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
