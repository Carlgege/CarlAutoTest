package com.carl.actions;

import org.eclipse.jface.action.Action;
//import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import com.android.uiautomator.UiAutomatorViewer;

//import com.carl.jfacetest.FileManager;
//import com.carl.jfacetest.JFaceTest;

public class SaveAsAction extends Action{
	
	public SaveAsAction() {
		super();
		setText("Save As(&S)");
		setToolTipText("Save As");
		//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\saveas.gif"));
	}
	
	@Override
	public void run() {
		
		if (UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem() == null) {
			return;
		}
		
		
		final FileManager fileManager = UiAutomatorViewer.getApp().getManager();
		FileDialog saveDialog = new FileDialog(UiAutomatorViewer.getApp().getShell(), SWT.SAVE);
		saveDialog.setText("Please choose the file");
		saveDialog.setFilterPath("..\\");
		saveDialog.setFilterExtensions(new String[]{"*.sh", "*.*"});
		
		saveDialog.setFileName(UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().getText());
		
		String saveFile = saveDialog.open();
		if (saveFile != null) {
			fileManager.setFileName(saveFile);
			fileManager.setContent(UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer().getText());
			fileManager.save(fileManager.getFileName());
			
			//update ctabItem
			UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().setText(saveFile.substring(saveFile.lastIndexOf("\\")+1));
			UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().setToolTipText(saveFile.substring(saveFile.lastIndexOf("\\")+1));
			
			//update statusLine
			UiAutomatorViewer.getApp().getStatusLineManager().setMessage("Opened file name is:" + fileManager.getFileName());
			
			//fileManager.setDirty(false);
		}
		
		return;
		
	}

}
