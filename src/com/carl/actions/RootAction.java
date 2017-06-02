package com.carl.actions;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.android.ddmlib.IDevice;
import com.android.uiautomator.UiAutomatorViewer;

public class RootAction extends Action{
	
	private IDevice myDevice;
	//UiAutomatorViewer mViewer;
	
	public RootAction() {
		super();
		setText("adb root");
		
		//mViewer = viewer;
	}
	
	@Override
	public void run() {
		
		if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
			return;
		}
		
		try {
			myDevice = UiAutomatorViewer.getApp().getChoosedDevice();
//			if (myDevice == null) {
//				mViewer.messageBox_OK("Error", "No devices connected");
//				return;
//			}
			
			rootDevice();
			//System.out.println("root result:"+mViewer.isRoot(false));
			
			//System.out.println("root device finished.");
			if (UiAutomatorViewer.getApp().isRoot(false)) {
				UiAutomatorViewer.getApp().messageBox_OK("Root result", "Success");
			} else {
				UiAutomatorViewer.getApp().messageBox_OK("Root result", "Fail");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void rootDevice() {
		
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(UiAutomatorViewer.getApp().getShell());
		
        try {
        	
            dialog.run(true, false, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException,InterruptedException {
                	
//                	Runtime rt = Runtime.getRuntime(); 
//        			Process ps = null; 
//        			try {
//        				ps = rt.exec(new String[]{"adb", "-s", myDevice.toString(),"root"});
//        			} catch (IOException e) {
//        				e.printStackTrace();
//        			}
//        			
                	CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", myDevice.toString(),"root"});
                	Thread.sleep(7000);
                	
    				monitor.done();
    				
    				//System.out.println("monitor done.");
	            }
	        });
        } catch (Exception e1) {
        	e1.printStackTrace();
            
        } finally {
        	dialog.close();
        }
	}	

}
