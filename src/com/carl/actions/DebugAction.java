package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;



import org.eclipse.swt.widgets.Display;


//import com.android.ddmlib.IDevice;
import com.android.uiautomator.UiAutomatorViewer;

public class DebugAction extends Action{
	
	//private IDevice myDevice;
	UiAutomatorViewer mViewer;
	private String debugCommand;
	
	//public static final String shellDebugPath = "/mnt/sdcard/ShellResult/ScriptFolder/debug.sh";
	public static final String shellDebugPath = "/data/local/tmp/debug.sh";
	
	public DebugAction(UiAutomatorViewer viewer) {
		super();
		setText("Debug");
		setAccelerator(SWT.F6);
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
		
		if (!(new StopAction().getDebugScriptProcessId(
				UiAutomatorViewer.getApp().getChoosedDevice().toString()).equals("-1") )) {
			
			MessageDialog.openInformation(null, "Warning", "There is one script running,"
					+ " please stop it at first.");
			return;
		}
		
		debugCommand = "";
		
		try {
			String [] debugString = UiAutomatorViewer.getApp().getmUiAutomatorView()
					.getSelectedSourceViewer().getSelectionText().split("\n");
			
			//myDevice = mViewer.getChoosedDevice();
			//System.out.println(mViewer.getChoosedDevice());
			
			for (int i=0; i<debugString.length; i++) {
				//System.out.println(i+"="+debugString[i]);
				if (debugString[i].replaceAll("\r|\n", "").startsWith("#")) {
					continue;
				}
				if (debugString[i].replaceAll("\r|\n", "").equals("")) {
					continue;
				}
				if (debugString[i].replaceAll("\r|\n", "").equals("do")) {
					debugCommand = debugCommand + debugString[i].replaceAll("\r|\n", "") + " ";
				} else if (debugString[i].replaceAll("\r|\n", "").equals("then")) {
					debugCommand = debugCommand + debugString[i].replaceAll("\r|\n", "") + " ";
				} else if (debugString[i].replaceAll("\r|\n", "").equals("else")) {
					debugCommand = debugCommand + debugString[i].replaceAll("\r|\n", "") + " ";
				} else {
					debugCommand = debugCommand + debugString[i].replaceAll("\r|\n", "") + ";";
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		debug(debugCommand);
		
	}
	
	public static void debug(final String command) {
		new Thread(
				new Runnable() {
					@Override
					public void run() {
						//CMDUtils.cmdCommand(new String[] {"adb","-s",myDevice.toString(),
						//"shell","\"source Template.sh; "+debugCommand+"\""});
						//mViewer.getmUiAutomatorView().singleCommandRun(debugCommand);
						//System.out.println("debugCommand:\n"+debugCommand);
						final FileManager fileManager = UiAutomatorViewer.getApp().getManager();
						final String sn = UiAutomatorViewer.getApp().getChoosedDevice().toString();
						fileManager.setFileName("..\\Debug\\debug.sh");
						fileManager.setContent("source "+PushTemplateAction.shellTemplatePath+";"+command);
						fileManager.save("..\\Debug\\debug.sh");
						
						CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",sn,"push","..\\Debug\\debug.sh", shellDebugPath});
						//CMDUtils.cmdCommand(new String[] {"adb","-s",myDevice.toString(),
						//"shell","busybox","dos2unix",shellDebugPath});
						//CMDUtils.cmdCommand(new String[] {"adb","-s",myDevice.toString(),
						//"shell","chmod","777",shellDebugPath});
						
						final String debugResult = CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",sn,"shell",
								"\""+"sh "+shellDebugPath+"\""});
						
						//System.out.println("debugResult="+debugResult);
						
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
					}
				}
			).start();
	}
}
