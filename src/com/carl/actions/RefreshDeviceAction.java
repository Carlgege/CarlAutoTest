package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import com.android.uiautomator.UiAutomatorViewer;
import com.android.uiautomator.actions.ImageHelper;

public class RefreshDeviceAction extends Action{
	
	//private IDevice myDevice;
	UiAutomatorViewer mViewer;
	
	public RefreshDeviceAction(UiAutomatorViewer viewer) {
		super("&Refresh");
		//setText("a");
		
		mViewer = viewer;
	}
	
	@Override
    public ImageDescriptor getImageDescriptor() {
        return ImageHelper.loadImageDescriptorFromResource("images/refresh.png");
    }
	
	@Override
	public void run() {
		
		mViewer.updateCombo();
	}

}
