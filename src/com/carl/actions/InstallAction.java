package com.carl.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.android.ddmlib.IDevice;
import com.android.uiautomator.UiAutomatorViewer;

public class InstallAction extends Action{
	
	public static final String busyboxPath = "/data/local/tmp/busybox";
	public static final String procrankPath = "/system/bin/procrank";
	public static final String procrankSOPath = "/system/lib/libpagemap.so";
	//public static final String commonActionJarPath = "/data/local/tmp/CommonAction.jar";
	public static final String testResultTestDevice = "/mnt/sdcard/ShellResult/Log/TestResult.html";
	public static final String testResultPC = "..\\Tools\\html\\TestResult.html";
	public static final String commonActionAPK = "..\\Tools\\APK\\CommonAction.apk";
	public static final String commonActionTestAPK = "..\\Tools\\APK\\CommonActionTest.apk";
	public static final String javascriptTestDevice = "/mnt/sdcard/ShellResult/Log/result.js";
	public static final String javascriptPC = "..\\Tools\\html\\result.js";
	public static final String catAP = "..\\Tools\\APK\\CATAP.apk";
	
	
	private IDevice myDevice;
	UiAutomatorViewer mViewer;
	public String busyboxPushed = "Failed";
	public String procrankPushed = "Failed";
	
	public InstallAction(UiAutomatorViewer viewer) {
		super();
		setText("Install busybox/procrank");
		
		mViewer = viewer;
	}
	
	@Override
	public void run() {
		
		if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
			
			
			return;
		}
		
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(mViewer.getShell());
        try {
            dialog.run(true, false, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException,
                                                                        InterruptedException {
                	
                	myDevice = mViewer.getChoosedDevice();
        			
        			//if (!mViewer.isRoot(true)) {
        				//mViewer.messageBox_OK("Error", "Please root your device at first.");
        				//return;
        			//}
        			//System.out.println(myDevice.getAbis().toString());
        			
        			//remount device
        			//CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", myDevice.toString(),"remount"});
        			
        			String cpuType = CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s",
        					myDevice.toString(), "shell", "getprop" , "ro.product.cpu.abi"});
        			
        			if (cpuType.contains("arm")) {
        				//System.out.println("This device's cpu type is arm");
        				
        				monitor.subTask("Install busybox...");
        				//install busybox
        				CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", myDevice.toString(),"push",
        						"..\\Tools\\Busybox\\Busybox_ARM\\busybox", busyboxPath});
        				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", myDevice.toString(), "shell",
        						"chmod", "777", busyboxPath});
        				
        				monitor.subTask("Install procrank...");
        				//install procrank
        				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", myDevice.toString(), "push",
        					"..\\Tools\\Procrank\\procrank_ARM\\procrank", procrankPath});
        				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", myDevice.toString(), "push",
        					"..\\Tools\\Procrank\\procrank_ARM\\libpagemap.so", procrankSOPath});
        				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", myDevice.toString(), "shell",
        						"chmod", "777", procrankPath});
        				
        			} else if (cpuType.contains("x86")) {
        				//System.out.println("This device's cpu type is x86");
        				
        				monitor.subTask("Install busybox...");
        				//install busybox
        				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", myDevice.toString(), 
        					"push", "..\\Tools\\Busybox\\Busybox_X86\\busybox", busyboxPath});
        				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", myDevice.toString(), 
        						"shell", "chmod", "777", busyboxPath});
        				
        				monitor.subTask("Install procrank...");
        				//install procrank
        				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", myDevice.toString(), "push",
        					"..\\Tools\\Procrank\\procrank_X86\\procrank", procrankPath});
        				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", myDevice.toString(), "push",
        					"..\\Tools\\Procrank\\procrank_X86\\libpagemap.so", procrankSOPath});
        				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", myDevice.toString(), "shell",
        					"chmod", "777", procrankPath});
        				
        			}
        			
        			//push CommonAction.jar
        			//CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", myDevice.toString(), "push" ,
        					//"..\\UiAutomator\\Jar\\CommonAction.jar", commonActionJarPath});
        			
        			//install CommonAction APK
        			monitor.subTask("Install CommonAction APK..., please click \"Accept\" on test device if need.");
        			CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", myDevice.toString(), "install" ,
        					"-r", commonActionAPK});
        			
        			CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", myDevice.toString(), "install" ,
        					"-r", commonActionTestAPK});
        			
        			//install CATAP APK
        			monitor.subTask("Install CAT APK...");
        			CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", myDevice.toString(), "install" ,
        					"-r", catAP});
        			
        			//push TestResult.html
        			monitor.subTask("Install TestResult template...");
        			CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", myDevice.toString(), "push" ,
        					testResultPC, testResultTestDevice});
        			
        			monitor.subTask("Install TestResult template...");
        			CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", myDevice.toString(), "push" ,
        					javascriptPC, javascriptTestDevice});
        			
        			//show install result
        			if (!CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", myDevice.toString(), 
        					"shell", "ls" , busyboxPath }).contains("No such file")) {
        				busyboxPushed = "Pass";
        			}
        			
        			if (!CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", myDevice.toString(), 
        					"shell", "ls" , procrankPath }).contains("No such file")) {
        				procrankPushed = "Pass";
        			}
        			
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        MessageDialog.openInformation(null, "Install result", "busybox: "+busyboxPushed+"\n"
				+ "procrank: "+procrankPushed+"\n"
				+ "Please enable r/w storage from Settings > Apps > CommonAction if Android Version >=6.0 ");
		
	}

}
