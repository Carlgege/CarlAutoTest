package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import com.android.uiautomator.UiAutomatorViewer;

public class SearchAction extends Action{
	
	//private UiAutomatorViewer mViewer;
	private FindAndReplace findAndReplace;
	public SearchAction(UiAutomatorViewer Viewer) {
		super();
		setText("Find");
		setAccelerator(SWT.CTRL + 'F');
		findAndReplace = new FindAndReplace();
		//mViewer=Viewer;
	}
	
	@Override
	public void run() {
		
		//Display display = new Display();
		//Shell parent =new Shell(mViewer.getShell(), SWT.SHELL_TRIM);
		
		if (findAndReplace.opened) {
			findAndReplace.getShell().setFocus();
		} else {
			findAndReplace.open();
			//scriptListTable.opened = true;
		}
		
		//new FindAndReplace().open();
		
	}

}
