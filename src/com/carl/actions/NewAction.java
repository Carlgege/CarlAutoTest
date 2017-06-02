package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
//import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

import com.android.uiautomator.*;

//import com.carl.jfacetest.FileManager;
//import com.carl.jfacetest.JFaceTest;

public class NewAction extends Action{
	
	public String username = "";
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public NewAction() {
		super();
		setText("New(&N)");
		setAccelerator(SWT.CTRL + 'N');
		//this.setAccelerator(SWT.ALT + SWT.SHIFT + 'N');
		setToolTipText("New");
		//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\new.gif"));
	}
	
	@Override
	public void run() {
		
		//UiAutomatorViewer temp = new UiAutomatorViewer();
		//temp.setmUiAutomatorView(new UiAutomatorView(null, SWT.NONE));
		//final CTabFolder temp = UiAutomatorViewer.getApp().getScriptFolder();
		//final CTabFolder ctabFolder = temp.getScriptFolder();
		try {
			
			InputDialog inputDialog = new InputDialog(UiAutomatorViewer.getApp().getShell(),
					"Please input file name", //This is dialog's title
					"Please input file name:", //This is dialog's hint
					"", //Default value
					new fileNameValidator());
			int result = inputDialog.open();
			if (result == Window.OK) {
				
				UiAutomatorViewer.getApp().getmUiAutomatorView().newCTabItem(inputDialog.getValue());
				
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("source /data/local/tmp/Template.sh\r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("Creater="+username+"\r\n");
				
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("TestPrecondition=\"1. \r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("2. \"\r\n");
				
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("TestStep=\"1. \r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("2. \"\r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("\r\n");
				
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("Repeat=$1\r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("for i"+" in $(busybox seq ${Repeat:-1})\r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("do\r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("echo `DATE` ${0##*/} Current Time is:$i"+">>$DetailLog\r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("#Please add operation on below\r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("BackToHome\r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("\r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("\r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("\r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("done\r\n");
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("LogPass\r\n");
				
				//set the cursor location
				UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer().setSelection(250+username.length(), 250+username.length());
				
				StyledText styledText = UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer();
				RedoUndoManager ru = new RedoUndoManager(styledText);
				UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer().addExtendedModifyListener(new ExtendedModifyListener() {

					@Override
					public void modifyText(ExtendedModifyEvent event) {
						//ru.undoCounter(event);
						
						String currText = ru.getStyledText().getText(0, ru.getStyledText().getCaretOffset()-1);
						
						if (currText.length() >= event.start + event.length) {
							String newText = currText.substring(event.start, event.start + event.length);
				    		if (newText != null && newText.length() > 0) {
//				    			if (undoStack.size() == MAX_STACK_SIZE) {
//				    				undoStack.remove(undoStack.size() - 1);
//				    			}
				    			ru.getUndoStack().add(0, newText);
				    			ru.getUndoIndex().add(0, String.valueOf(ru.getStyledText().getCaretOffset()));
				    			
				    			System.out.println("newText:"+newText);
				    			System.out.println("edit location:"+String.valueOf(ru.getStyledText().getCaretOffset()));
				    		}
						}
						
						
						
					}
		    	});
		    	
				UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer().addKeyListener(new KeyListener() {

					@Override
					public void keyPressed(KeyEvent arg0) {
						//System.out.println("摁下键盘："+arg0.character+" = "+arg0.keyCode);  
		                //boolean flag1 = (arg0.stateMask & SWT.CTRL)!=0; //同时按下Ctrl+一个其他键就不为0  
		                //boolean flag2 = (arg0.stateMask & SWT.ALT)!=0; //同时按下Alt+一个其他键就不为0  
		                //System.err.println(flag1+";"+flag2);
						
						//System.out.println("stateMask:"+arg0.stateMask);
						//System.out.println("character:"+arg0.character);
						//System.out.println("keyCode:"+arg0.keyCode);
		                
		                
					}

					@Override
					public void keyReleased(KeyEvent arg0) {
						if ((arg0.stateMask & SWT.CTRL) != 0){
		                	if (arg0.keyCode == 122) {
		                		//System.out.println("您同时按下了Ctrl+z");
		                		ru.undo();
		                	} else if (arg0.keyCode == 121) {
		                		//System.out.println("您同时按下了Ctrl+y");
		                		ru.redo();
		                	}
		                }  
					}
		    	});
				
			}
			
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		//StyledText textArea = new StyledText(temp.getmUiAutomatorView().getScriptFolder(), SWT.BORDER|SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
        //textArea.setLayout(new FillLayout(SWT.HORIZONTAL));
        //textArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		//JFaceTest.getApp().getContent().setText(" ");
		//JFaceTest.getApp().setManager(new FileManager());
	}
	
	class fileNameValidator implements IInputValidator {

		@Override
		public String isValid(String arg0) {
			// TODO Auto-generated method stub
			if (arg0.equals("")) {
				return "Error, file name can not be none";
			}
			if (arg0.contains(" ")) {
				return "Error, file name should not include space";
			}
			
			//if this file has opened, do not open it again
			final CTabFolder temp = UiAutomatorViewer.getApp().getScriptFolder();
			for (int i=0; i<temp.getItems().length; i++) {
				if (temp.getItem(i).getText().toString().equals(arg0)) {
					return "Error, same name is opened";
				}
				if (temp.getItem(i).getText().toString().equals("*"+arg0)) {
					return "Error, same name is opened";
				}
			}
			
			return null;
		}
	}	

}
