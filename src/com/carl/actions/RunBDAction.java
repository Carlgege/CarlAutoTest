package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.android.ddmlib.IDevice;
import com.android.uiautomator.UiAutomatorViewer;

public class RunBDAction extends Action{
	
	private IDevice myDevice;
	UiAutomatorViewer mViewer;
	private String runCommand;
	
	public RunBDAction(UiAutomatorViewer viewer) {
		super();
		setText("Run BD");
		setToolTipText("Run at background");
		//setToolTipText("Copy");
		setAccelerator(SWT.F9);
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
		
		runscript(myDevice.toString());
	}
	
	public void runscript(final String SN) {
		
		if (!( new StopAction().getDebugScriptProcessId(SN).equals("-1") )) {
			
			MessageDialog.openInformation(null, "Warning", "There is one script running,"
					+ " please stop it at first.");
			return;
		}

		String [] runStrings = UiAutomatorViewer.getApp().getmUiAutomatorView()
				.getSelectedSourceViewer().getText().split("\n");
		
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
						
						//System.out.println("runCommand:\n"+runCommand);
						//CMDUtils.cmdCommand(new String[] {"adb","-s",myDevice.toString(),
						//"shell","\"source Template.sh;"+runCommand+"\""});
						final FileManager fileManager = UiAutomatorViewer.getApp().getManager();
						fileManager.setFileName("..\\Debug\\debug.sh");
						fileManager.setContent(runCommand);
						fileManager.save("..\\Debug\\debug.sh");
						
						//System.out.println("running");
						
						CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",SN,"push",
								"..\\Debug\\debug.sh",DebugAction.shellDebugPath});
						
						final String debugResult = CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",SN,
								"shell","\""+"sh "+DebugAction.shellDebugPath+"&\""});
						//final String debugResult = CMDUtils.cmdCommandGetInput(new String[] {
						//		"echo sh "+DebugAction.shellDebugPath+"^^^&|adb","-s",SN,"shell"});
						
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
