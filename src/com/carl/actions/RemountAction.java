package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

import com.android.uiautomator.UiAutomatorViewer;

public class RemountAction extends Action{
	
	public RemountAction() {
		super();
		setText("Remount");
		
	}
	
	@Override
	public void run() {
		
		if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
			return;
		}
		
		String sn = UiAutomatorViewer.getApp().getChoosedDevice().getSerialNumber();
		//System.out.println("sn="+sn);
		MessageDialog.openInformation(null, "Remount result", 
				CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", sn,"remount"}));
	}

}
