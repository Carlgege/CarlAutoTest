package com.carl.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import com.android.uiautomator.UiAutomatorViewer;

public class PushTemplateAction extends Action{
	
	public static final String shellTemplatePath = "/data/local/tmp/Template.sh";
	
	public PushTemplateAction() {
		super();
		setText("Push template");
		
	}
	
	@Override
	public void run() {
		
		if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
			return;
		}
		
		String SN = UiAutomatorViewer.getApp().getChoosedDevice().toString();

		//if (!UiAutomatorViewer.getApp().isRoot(true)) {
		//	return;
		//}
		
		FileDialog dialog = new FileDialog(UiAutomatorViewer.getApp().getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[]{"*.sh", "*.*"});
		
		//File currentFolder = new File("..");
		//System.out.println("pre folder:"+currentFolder.getAbsolutePath());
		
		dialog.setFilterPath(new File("..\\Template").getAbsolutePath());
		final String name = dialog.open();
		if ((name == null)||(name.length()==0)) {
			return;
		}
		
		File templateFile = new File(name);
		//System.out.println("file path:"+templateFile.getAbsolutePath());
		
		//push template.sh to /system/bin/Template.sh
		//String pushResult = CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", 
				//UiAutomatorViewer.getApp().getChoosedDevice().toString(),
				//"push", templateFile.getAbsolutePath(), shellTemplatePath});
		
		CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN,
				"push", templateFile.getAbsolutePath(), shellTemplatePath});
		//System.out.println("pushResult:"+pushResult);
		
		//if (pushResult.contains("fail")) {
		//	MessageDialog.openInformation(null, "Push Template result", 
		//"fail, make sure your device has root and remount successfully "+"\n");
		//} else {
		//change it to unix format
		CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN,
				"shell", InstallAction.busyboxPath, "dos2unix",shellTemplatePath});
		
		//let it has root permission
		CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN,
				"shell", "chmod", "777", shellTemplatePath});
		
		
		MessageDialog.openInformation(null, "Push Template result", "success. "+"\n");
		//}
		
	}

}
