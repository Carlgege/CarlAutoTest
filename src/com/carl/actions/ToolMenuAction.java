package com.carl.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
//import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import com.android.uiautomator.UiAutomatorViewer;

public class ToolMenuAction {
	
	public static final String CURRENTVERSION = "Get current version";
	public static final String PUSH = "Push old version and compare";
	public static final String PULLRESULT = "Pull result";
	
	public static final String VERSIONCOMPARERESULTFOLDER = "/mnt/sdcard/ShellResult/VersionCompareResult";
	public static final String VERSIONCOMPARESHPATH = "/data/local/tmp";
	public static final String VERSIONCOMPARELOCALRESULT = Login.LOCALRESULTLPATH + "\\Result\\VersionCompareResult";
	public static final String MONKEYANALYSELOCALRESULT = Login.LOCALRESULTLPATH + "\\Result\\MonkeyAnalyseResult";
	
	private RootAction rootAction;
    private ScsiAction scsiAction;
    
	//private MenuManager pullMenu;
	public MenuManager ToolAction(MenuManager menuManager) {
		
		//this.pullMenu = menuManager;
		MenuManager toolMenu = new MenuManager("Tools(&T)");
		menuManager.add(toolMenu);
		
		MenuManager versionCompareMenu = new MenuManager("Version Compare");
		
		MenuManager rootMenu = new MenuManager("Root");
		
		toolMenu.add(rootMenu);
		
		rootAction = new RootAction();
		scsiAction = new ScsiAction();
		
		rootMenu.add(scsiAction);
		rootMenu.add(rootAction);
		
		toolMenu.add(versionCompareMenu);
		toolMenu.add(new MonkeyAnalyseAction());
		
		versionCompareMenu.add(new VersionCompareAction(CURRENTVERSION));
		versionCompareMenu.add(new VersionCompareAction(PUSH));
		versionCompareMenu.add(new VersionCompareAction(PULLRESULT));
		
		return toolMenu;
	}
	
	public class VersionCompareAction extends Action {
		
		String myOption;
		
		public VersionCompareAction(String option) {
			
			setText("Version Compare");
			this.myOption = option;
			initAction();
		}
		
		void initAction() {
			if (myOption.equals(CURRENTVERSION)) {
				this.setText(CURRENTVERSION);
				//this.setToolTipText("set Font");
				//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\font.gif"));
				
			} else if (myOption.equals(PUSH)) {
				this.setText(PUSH);
				//this.setToolTipText("Set forecolor");
				//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\forecolor.gif"));
			} else {
				this.setText(PULLRESULT);
				//this.setToolTipText("Pull result");
				//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\bgColor.gif"));
			}
		}
		
		@Override
		public void run() {
			
			if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
				return;
			}
			
			/*
			if (!UiAutomatorViewer.getApp().isRoot(false)) {
				MessageDialog.openInformation(null, "Error", "Please enable root at first.");
				return;
			}
			*/
			
			final String SN = UiAutomatorViewer.getApp().getChoosedDevice().toString();
			
			if (myOption.equals(CURRENTVERSION)) {
				this.setText(CURRENTVERSION);
				//this.setToolTipText("set Font");
				//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\font.gif"));
				
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
		        try {
		            dialog.run(true, false, new IRunnableWithProgress() {
		                @Override
		                public void run(IProgressMonitor monitor) throws InvocationTargetException,
		                                                                        InterruptedException {
		                	
		                	monitor.setTaskName("Analysing apk info from test device");
		                	monitor.subTask("Analysing..., it need about 2 minutes");
		                	
		                	//CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "push",
		                	//		"..\\Tools\\VersionCompare\\APPI.txt",
		                	//		VERSIONCOMPARERESULTFOLDER+"/APPI.txt" });
		                	//CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "shell",
		                	//		"busybox", "dos2unix", 
		                	//		VERSIONCOMPARERESULTFOLDER+"/APPI.txt" });
		                	//CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "shell",
		                	//		"rm", VERSIONCOMPARESHPATH+"/*.sh" });
		                	CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "push",
		                			"..\\Tools\\VersionCompare\\PackageInfo.sh", 
		                			VERSIONCOMPARESHPATH+"/PackageInfo.sh" });
		                	CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "shell",
		                			InstallAction.busyboxPath, "dos2unix", 
		                			VERSIONCOMPARESHPATH+"/PackageInfo.sh" });
		                	CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "shell",
		                			"chmod", "777", 
		                			VERSIONCOMPARESHPATH+"/PackageInfo.sh" });
		                	CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "shell",
		                			"sh", VERSIONCOMPARESHPATH+"/PackageInfo.sh" });
		                	//CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "pull",
		                	//		"/data/ShellResult", "..\\Result\\VersionCompareResult" });
		                	
		                	monitor.done();
		                }
		            });
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
				
			} else if (myOption.equals(PUSH)) {
				this.setText(PUSH);
				//this.setToolTipText("Set forecolor");
				//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\forecolor.gif"));
				
				FileDialog dialog = new FileDialog(UiAutomatorViewer.getApp().getShell(), SWT.OPEN);
				dialog.setFilterPath(Login.LOCALRESULTLPATH + "\\Result\\VersionCompareResult");
				dialog.setFilterExtensions(new String[]{"*.xls", "*.*"});
				final String name = dialog.open();
				//System.out.println("push file name:"+name);
				if ((name == null)||(name.length()==0)) {
					return;
				}
				
				ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(null);
		        try {
		        	progressDialog.run(true, false, new IRunnableWithProgress() {
		                @Override
		                public void run(IProgressMonitor monitor) throws InvocationTargetException,
		                                                                        InterruptedException {
		                	
		                	monitor.setTaskName("Comparing apk info from test device");
		                	monitor.subTask("Compareing..., it need about 3 minutes");
		                	
		                	//System.out.println("name sub:"+name.substring(name.lastIndexOf("\\")+1));
		                	
		                	CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "push",
		                			name, VERSIONCOMPARERESULTFOLDER+"/"+name.substring(name.lastIndexOf("\\")+1)});
		                	
		    				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "shell",
		    						"sh", VERSIONCOMPARESHPATH+"/PackageInfo.sh" });
		    				
		                	monitor.done();
		                }
		            });
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
				
			} else {
				this.setText(PULLRESULT);
				//this.setToolTipText("Pull result");
				//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\bgColor.gif"));
				
				createFolder(VERSIONCOMPARELOCALRESULT);
				
				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "shell", "rm",
						VERSIONCOMPARESHPATH+"/*.sh" });
				
				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "shell", "rm",
						VERSIONCOMPARESHPATH+"/*.txt" });
				
				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "pull",
						VERSIONCOMPARERESULTFOLDER, VERSIONCOMPARELOCALRESULT });
				
				try {
					java.awt.Desktop.getDesktop().open(new File(VERSIONCOMPARELOCALRESULT));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public class MonkeyAnalyseAction extends Action {
		
		public MonkeyAnalyseAction() {
			setText("Monkey Analyse");
		}
		
		@Override
		public void run() {
			
			if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
				return;
			}
			
			/*
			if (!UiAutomatorViewer.getApp().isRoot(false)) {
				MessageDialog.openInformation(null, "Error", "Please enable root at first.");
				return;
			}
			*/
			
			createFolder(MONKEYANALYSELOCALRESULT);
			
			final String SN = UiAutomatorViewer.getApp().getChoosedDevice().toString();
			
			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(null);
	        try {
	        	progressDialog.run(true, false, new IRunnableWithProgress() {
	                @Override
	                public void run(IProgressMonitor monitor) throws InvocationTargetException,
	                                                                        InterruptedException {
	                	
	                	monitor.setTaskName("Monkey Analyse");
	                	monitor.subTask("Analysing..., it need about 2 minutes");
	                	
	    				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "shell",
	    						"chmod", "777", "/data/MKY_LOG/*" });
	    				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "push",
	    						"..\\Tools\\MonkeyAnalyse\\MonkeyAnalyse.sh", "/data/MonkeyAnalyse.sh" });
	    				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "shell",
	    						InstallAction.busyboxPath, "dos2unix", "/data/MonkeyAnalyse.sh" });
	    				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "shell",
	    						InstallAction.busyboxPath, "chmod", "777", "/data/MonkeyAnalyse.sh" });
	    				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "shell",
	    						"sh", "/data/MonkeyAnalyse.sh" });
	    				CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s", SN, "pull",
	    						"/data/ShellResult", MONKEYANALYSELOCALRESULT });
	    				
	                	monitor.done();
	                }
	            });
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        
	        try {
				java.awt.Desktop.getDesktop().open(new File(MONKEYANALYSELOCALRESULT));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createFolder(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
}
