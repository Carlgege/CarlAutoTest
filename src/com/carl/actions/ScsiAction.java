package com.carl.actions;

import org.eclipse.jface.action.Action;

public class ScsiAction extends Action{
	
	public ScsiAction() {
		super();
		setText("Scsi");
	}
	
	@Override
	public void run() {
		new Thread(
			new Runnable() {
				@Override
				public void run() {
					CMDUtils.cmdCommandGetInput(new String[] {"..\\Tools\\SCSI\\ScsiCmdAgent.exe"});
				}
			}
		).start();
	}
}
