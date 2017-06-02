package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

import com.android.uiautomator.UiAutomatorViewer;

public class StopAction extends Action{
	
	public StopAction() {
		super();
		setText("Stop");
		setAccelerator(SWT.F7);
	}
	
	@Override
	public void run() {
		
		if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
			return;
		}
		
		String SN = UiAutomatorViewer.getApp().getChoosedDevice().toString();
		stopRunningScript(SN);
	}
	
	public void stopRunningScript(String SN) {
		
		//System.out.println(getRunningScriptProcessId());
		String debugProcessId = getDebugScriptProcessId(SN);
		//stop the debug.sh
		if (!debugProcessId.equals("-1")) {
			CMDUtils.cmdCommandGetInput(new String[]{"adb","-s",SN,
					"shell","kill","-9",debugProcessId});
			UiAutomatorViewer.getApp().getStatusLineManager().setMessage(
					"Stop the running script and process id is: " + debugProcessId);
		} else {
			UiAutomatorViewer.getApp().getStatusLineManager().setMessage(
					"No script running.");
		}
		
		String runningProcessId = CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",SN,"shell",
				"\"cat /mnt/sdcard/ShellResult/Temp/RunningScript.txt|"
						+InstallAction.busyboxPath+" tail -1|"
						+ InstallAction.busyboxPath+" fgrep -v debug.sh|"+
						InstallAction.busyboxPath+" awk '{print $NF}'\""});
		
		//stop the running sh during run multi scripts
		if (!runningProcessId.equals("")) {
			CMDUtils.cmdCommandGetInput(new String[]{"adb","-s",SN,
					"shell","kill","-9",runningProcessId});
		}				
	}
	
	public String getDebugScriptProcessId(String SN) {
		
		//String SN = UiAutomatorViewer.getApp().getChoosedDevice().toString();
		//System.out.println("SN="+SN);
		String processId = CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",SN,"shell",
				"\""+InstallAction.busyboxPath+" ps|"+InstallAction.busyboxPath+
				" fgrep debug.sh|"+InstallAction.busyboxPath+" grep -v grep|"+
						InstallAction.busyboxPath+" awk '{print $1}'\""});
		
		if (processId.equals("")) {
			return "-1";
		} else {
			return processId;
		}
	}
}
