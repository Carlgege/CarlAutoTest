package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.android.ddmlib.IDevice;
import com.android.uiautomator.UiAutomatorViewer;

public class RunAction extends Action{
	
	private IDevice myDevice;
	UiAutomatorViewer mViewer;
	private String runCommand;
	
	public RunAction(UiAutomatorViewer viewer) {
		super();
		setText("Run");
		//setToolTipText("Copy");
		setAccelerator(SWT.F5);
		//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\cut.gif"));
		mViewer = viewer;
	}
	
	@Override
	public void run() {
		
		if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
			return;
		}
		
		//if (!UiAutomatorViewer.getApp().isRoot(true)) {
		//	return;
		//}
		
		//if there is no script editing, return
		if (UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer() == null) {
			return;
		}
		
		runCommand = "";
		myDevice = mViewer.getChoosedDevice();
		
		//mViewer.getmUiAutomatorView().getSelectedStyledText().getText();
		runscript(myDevice.toString());
		//UiAutomatorViewer.getApp().getmUiAutomatorView().refreshResultFolder();
	}
	
	public void runscript(final String SN) {
		
		String isScriptRunning = new StopAction().getDebugScriptProcessId(
				UiAutomatorViewer.getApp().getChoosedDevice().toString());
		
		if (isScriptRunning.contains("busybox: not found")) {
			//System.out.println("isScriptRunning:"+"busybox not found.");
			MessageDialog.openInformation(null, "Warning", 
					"busybox not found, please install busybox and push template at first!");
			return;
		}
		
		if (!( isScriptRunning.equals("-1") )) {
			
			MessageDialog.openInformation(null, "Warning", 
					"There is one script running, please stop it at first.");
			return;
		}
		
		String [] runStrings = UiAutomatorViewer.getApp().getmUiAutomatorView().
				getSelectedSourceViewer().getText().split("\n");
		
		for (int i=0; i<runStrings.length; i++) {
			//System.out.println(i+"="+runStrings[i]);
			if (runStrings[i].replaceAll("\r|\n", "").startsWith("#")) {
				continue;
			}
			if (runStrings[i].replaceAll("\r|\n", "").equals("")) {
				continue;
			}
			if (runStrings[i].replaceAll("\r|\n", "").equals("do")) {
				runCommand = runCommand + runStrings[i].replaceAll("\r|\n", "") + " ";
			} else if (runStrings[i].replaceAll("\r|\n", "").equals("then")) {
				runCommand = runCommand + runStrings[i].replaceAll("\r|\n", "") + " ";
			} else if (runStrings[i].replaceAll("\r|\n", "").equals("else")) {
				runCommand = runCommand + runStrings[i].replaceAll("\r|\n", "") + " ";
			} else {
				runCommand = runCommand + runStrings[i].replaceAll("\r|\n", "") + ";";
			}
		}
		
		new Thread(
				
				new Runnable() {
					@Override
					public void run() {
						
						final FileManager fileManager = UiAutomatorViewer.getApp().getManager();
						fileManager.setFileName("..\\Debug\\debug.sh");
						fileManager.setContent(runCommand);
						fileManager.save("..\\Debug\\debug.sh");
						
						CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",SN,"push",
								"..\\Debug\\debug.sh",DebugAction.shellDebugPath});
						
						final String debugResult = CMDUtils.cmdCommandGetInput(new String[] {"adb",
								"-s",SN,"shell","\""+"sh "+DebugAction.shellDebugPath+""+"\""});
						
						Display.getDefault().syncExec(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (debugResult.contains("syntax error")) {
									//MessageDialog.openInformation(null, "Syntax Error", debugResult+"\n");
									UiAutomatorViewer.getApp().messageBox_OK("Syntax Error", debugResult);
								}
							}
							
						});
						
						//mViewer.getmUiAutomatorView().singleCommandRun(runCommand);
					}
				}
			).start();
	}
}
