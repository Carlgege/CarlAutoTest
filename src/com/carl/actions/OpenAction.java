package com.carl.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
//import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.FileDialog;

import com.android.uiautomator.UiAutomatorViewer;

public class OpenAction extends Action{
	
	public OpenAction() {
		super();
		setText("Open(&O)");
		setToolTipText("Open file");
		setAccelerator(SWT.CTRL +'O');
		//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icon\\open.gif"));
	}
	
	@Override
	public void run() {
		
		/*
		FileDialog dialog = new FileDialog(UiAutomatorViewer.getApp().getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[]{"*.java", "*.*"});
		final String name = dialog.open();
		if ((name == null)||(name.length()==0)) {
			return;
		}
		final FileManager fileManager = UiAutomatorViewer.getApp().getManager();
		try {
			ModalContext.run(new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					// TODO Auto-generated method stub
					fileManager.load(name);
					monitor.done();
				}
			}, true, UiAutomatorViewer.getApp().getStatusLineManager().getProgressMonitor(), UiAutomatorViewer.getApp().getShell().getDisplay());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//get the CTabFolder
		final CTabFolder temp = UiAutomatorViewer.getApp().getScriptFolder();
		
		//if this file has opened, do not open it again
		String tabItemName = name.substring(name.lastIndexOf("\\")+1);
		
		for (int i=0; i<temp.getItems().length; i++) {
			if (temp.getItem(i).getText().toString().equals(tabItemName)) {
				return;
			}
		}
		
		//new one tabItem
		CTabItem tabItem = new CTabItem(temp, SWT.NONE);
		//set file's name as the tabItem's name
		
		tabItem.setText(tabItemName);
		
		temp.setSelection(tabItem);
		
		//New one StyledText to store open file
		StyledText text = new StyledText(temp, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL );
		text.setText(fileManager.getContent());
		tabItem.setControl(text);
		
		//update statusLine
		UiAutomatorViewer.getApp().getStatusLineManager().setMessage("Opened file name is:" + name);
		*/
		
		
		FileDialog dialog = new FileDialog(UiAutomatorViewer.getApp().getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[]{"*.sh", "*.*"});
		final String name = dialog.open();
		if ((name == null)||(name.length()==0)) {
			return;
		}
		final FileManager fileManager = UiAutomatorViewer.getApp().getManager();
		try {
			ModalContext.run(new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					// TODO Auto-generated method stub
					fileManager.load(name);
					monitor.done();
				}
			}, true, UiAutomatorViewer.getApp().getStatusLineManager().getProgressMonitor(), UiAutomatorViewer.getApp().getShell().getDisplay());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//get the CTabFolder
		final CTabFolder temp = UiAutomatorViewer.getApp().getScriptFolder();
		
		//if this file has opened, do not open it again
		String tabItemName = name.substring(name.lastIndexOf("\\")+1);
		
		for (int i=0; i<temp.getItems().length; i++) {
			if (temp.getItem(i).getText().toString().equals(tabItemName)) {
				return;
			}
			if (temp.getItem(i).getText().toString().equals("*"+tabItemName)) {
				return;
			}
		}
		
		//new one tabItem
		//CTabItem tabItem = new CTabItem(temp, SWT.NONE);
		//set file's name as the tabItem's name
		
		UiAutomatorViewer.getApp().getmUiAutomatorView().newCTabItem(tabItemName);
		UiAutomatorViewer.getApp().getmUiAutomatorView().getStyledText().setText(fileManager.getContent());
		
		//CTabItem ctabItem = UiAutomatorViewer.getApp().getmUiAutomatorView().newCTabItem();
		//ctabItem.setText(tabItemName);
		
		//temp.setSelection(ctabItem);
		
		//New one StyledText to store open file
		//StyledText text = new StyledText(temp, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL );
		//StyledText text = UiAutomatorViewer.getApp().getmUiAutomatorView().newStyledText();
		
		//text.setText(fileManager.getContent());
		//ctabItem.setControl(text);
		
		//update statusLine
		UiAutomatorViewer.getApp().getStatusLineManager().setMessage("Opened file name is:" + name);
		
		/*
		UiAutomatorViewer.getApp().getmUiAutomatorView().getStyledText().addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent arg0) {
				//UiAutomatorViewer.getApp().getManager().setDirty(true);
				
				if (!UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().getText().contains("*")) {
					UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().setText("*"+UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().getText());
				}
				UiAutomatorViewer.getApp().getmUiAutomatorView().updateCTabItems();
			}
		});
		*/
		
	}

}
