package com.carl.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
//import org.eclipse.swt.custom.CTabFolder;
//import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;

import com.android.uiautomator.UiAutomatorViewer;

//import com.carl.jfacetest.FileManager;
//import com.carl.jfacetest.JFaceTest;

public class SaveAction extends Action{
	
	//private String previousTab = null;
	//private String currentTab = null;
	
	public String savedScript = "";
	
	public String getSavedScript() {
		return savedScript;
	}

	public SaveAction() {
		super();
		setText("Save(&S)");
		setToolTipText("Save files");
		setAccelerator(SWT.CTRL + 'S');
		//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\save.gif"));
	}
	
	@Override
	public void run() {
		
		if (UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem() == null) {
			return;
		}
		if (UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer().getText().equals("")) {
			return;
		}
		
		//currentTab = UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().getText();
				
		final FileManager fileManager = UiAutomatorViewer.getApp().getManager();
		
		//System.out.println("currentTab:"+currentTab);
		//System.out.println("StyledText content:"+UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedStyledText().getText());
		//System.out.println(fileManager.getFileName());
		//|| (! currentTab.equals(previousTab))
		
		
		String ctabItemText = UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().getText();
		String fileName;
		if (ctabItemText.startsWith("*")) {
			fileName = ctabItemText.substring(1);
			//System.out.println(fileName);
		} else {
			fileName = ctabItemText;
		}
		
		if(ctabItemText.startsWith("*")) {
			
			FileDialog saveDialog = new FileDialog(UiAutomatorViewer.getApp().getShell(), SWT.SAVE);
			saveDialog.setText("Please save file...");
			//saveDialog.setFilterPath("..\\");
			
			saveDialog.setFilterExtensions(new String[]{"*.sh","*.*"});
			saveDialog.setFileName(fileName);
			
			String saveFile = saveDialog.open();
			
			//System.out.println("saveFile:"+saveFile);
			//saveFile != null&& fileManager.isDirty()
			
			if (saveFile == null) {
				return;
			}
			
			savedScript = saveFile;
			//System.out.println("savedScript:"+savedScript);
			
			String savedFileName = saveFile.substring(saveFile.lastIndexOf("\\"));
			if (savedFileName.contains(" ")) {
				MessageDialog.openInformation(null, "Error", "Save failed, file name include space");
				return;
			}
			
			if (!new File(saveFile).exists()) {
				fileManager.setFileName(saveFile);
				
				//System.out.print("text= "+UiAutomatorViewer.getApp().getmUiAutomatorView().getStyledText().getText());
				fileManager.setContent(UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer().getText());
				//System.out.println("saveDialog.getFileName():"+saveDialog.getFileName());
				fileManager.save(saveFile);
				new Upload().open();
				//fileManager.save(saveDialog.getFileName());
				
				//update ctabItem
				UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().setText(saveFile.substring(saveFile.lastIndexOf("\\")+1));
				UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().setToolTipText(saveFile.substring(saveFile.lastIndexOf("\\")+1));
				
				//update statusLine
				UiAutomatorViewer.getApp().getStatusLineManager().setMessage("Saved file name is:" + fileManager.getFileName());
				//fileManager.setDirty(false);
				
			} else {
				
				MessageBox box = new MessageBox(UiAutomatorViewer.getApp().getShell(), SWT.ICON_QUESTION|SWT.YES |SWT.NO);
				box.setText("Save");
				box.setMessage("The file is exist, do you want to rewrite it?");
				int choice = box.open();
				if (choice == SWT.NO) {
					//fileManager.setDirty(true);
					return;
				}
				
				fileManager.setFileName(saveFile);
				fileManager.setContent(UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer().getText());
				fileManager.save(saveFile);
				
				//update ctabItem
				UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().setText(saveFile.substring(saveFile.lastIndexOf("\\")+1));
				UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().setToolTipText(saveFile.substring(saveFile.lastIndexOf("\\")+1));

				//update statusLine
				UiAutomatorViewer.getApp().getStatusLineManager().setMessage("Saved file name is:" + fileManager.getFileName());
				
				new Upload().open();
			}
			return;
		}
		
		/*
		
		//if (fileManager.getFileName() != null || currentTab.equals(previousTab)) {
		System.out.println("file name is: "+fileManager.getFileName());
		if (fileManager.getFileName() != null && currentTab.equals(previousTab)) {
			previousTab = currentTab;
			
			MessageBox box = new MessageBox(UiAutomatorViewer.getApp().getShell(), SWT.ICON_QUESTION|SWT.YES |SWT.NO);
			box.setText("Save");
			box.setMessage("The file is exist, do you want to rewrite it?");
			int choice = box.open();
			if (choice == SWT.NO) {
				fileManager.setDirty(true);
				return;
			}
			fileManager.setContent(UiAutomatorViewer.getApp().getmUiAutomatorView().getStyledText().getText());
			fileManager.save(fileManager.getFileName());
			fileManager.setDirty(false);
			
			//update statusLine
			UiAutomatorViewer.getApp().getStatusLineManager().setMessage("Saved file name is:" + fileManager.getFileName());
		}
		*/
	}

}

