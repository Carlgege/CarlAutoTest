package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import com.carl.table.*;

public class RunMultiAction extends Action{
	
	private ScriptListTable scriptListTable;
	public RunMultiAction() {
		super();
		setText("Multi script push");
		setAccelerator(SWT.F8);
		scriptListTable = new ScriptListTable();
	}
	
	@Override
	public void run() {
		
		if (scriptListTable.opened) {
			scriptListTable.getShell().setFocus();
		} else {
			//scriptListTable.create();
			
			scriptListTable.open();
			//scriptListTable.opened = true;
		}
		
		//new ScriptListTable().getShell().setFocus();
		//new ScriptListTable().open();
		
	}

}
