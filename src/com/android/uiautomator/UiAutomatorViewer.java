/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.uiautomator;

import com.android.ddmlib.IDevice;
import com.android.uiautomator.actions.ImageHelper;
import com.android.uiautomator.actions.OpenFilesAction;
import com.android.uiautomator.actions.SaveScreenShotAction;
import com.android.uiautomator.actions.ScreenshotAction;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;

import java.io.File;
import java.util.List;

import com.carl.actions.*;

public class UiAutomatorViewer extends ApplicationWindow {
	
	//public static final String shellResultPath = "/mnt/sdcard/ShellResult";
	//public static final String shellResultLogPath = "/mnt/sdcard/ShellResult/Log";
	
	private NewAction newAction;
	private OpenAction openAction;
	private SaveAction saveAction;
	
	//private SaveAsAction saveAsAction;
	private ExitAction exitAction;
	private CopyAction copyAction;
	
	private CutAction cutAction;
	private PasteAction pasteAction;
	//private HelpAction helpAction;
	private FileManager manager;
	
	private RedoAction redoAction;
	private UndoAction undoAction;
	private SearchAction searchAction;
	private SelectAllAction selectAllAction;
	
	private static UiAutomatorViewer app;
    private UiAutomatorView mUiAutomatorView;
	//private StyledText textContent;
	
    private RunAction runAction;
	private RunBDAction runBDAction;
	private DebugAction debugAction;
	private StopAction stopAction;
	private RunMultiAction runMultiAction;
	
	//private PullAction pullAction;
	
    private InstallAction installAction;
    private PushTemplateAction pushTemplateAction;
    
    private AboutAction aboutAction;
    private Combo combo;
    private IDevice choosedDevice;
    private MenuManager menuManager;
    
    private List<IDevice> devices;
    
    final Clipboard mClipboard = new Clipboard(Display.getCurrent());
    
    public GetXMLAction getXMLAction = new GetXMLAction();
    
    public static Login login;
    
    public SaveAction getSaveAction() {
		return saveAction;
	}

	public static Login getLogin() {
		return login;
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}
	
	public IDevice getChoosedDevice() {
		
		//UiAutomatorViewer.getApp().getmUiAutomatorView().setStopShowLog(true);
		
		
    	//combo.update();
		if (choosedDevice == null) {
			//UiAutomatorViewer.getApp().getmUiAutomatorView().setStopShowLog(false);
			return null;
		}
		
		String SN = CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", choosedDevice.toString(), "get-serialno"});
		
		//System.out.println("SN: "+SN);
		//System.out.println("choosedDevice: "+choosedDevice.toString());
		if (SN.replaceAll("\r|\n", "").equals(choosedDevice.toString())) {
			//UiAutomatorViewer.getApp().getmUiAutomatorView().setStopShowLog(false);
			return choosedDevice;
		} else {
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					MessageDialog.openInformation(null, "Error",
		    				"The choosed device can not be found now, please refresh device list.");
				}
			});
			//UiAutomatorViewer.getApp().getmUiAutomatorView().setStopShowLog(false);
			return null;
		}
    }
	
	public NewAction getNewAction() {
		return newAction;
	}
    
    public CopyAction getCopyAction() {
		return copyAction;
	}

	public CutAction getCutAction() {
		return cutAction;
	}

	public PasteAction getPasteAction() {
		return pasteAction;
	}

	public RedoAction getRedoAction() {
		return redoAction;
	}

	public UndoAction getUndoAction() {
		return undoAction;
	}
	
    public Clipboard getmClipboard() {
		return mClipboard;
	}
    
    public RunMultiAction getRunMultiAction() {
		return runMultiAction;
	}
    
    public StopAction getStopction() {
		return stopAction;
	}
    
    
    public boolean isRoot(boolean flag) {
    	
    	String result = CMDUtils.cmdCommandGetInput(new String[] { "adb", "-s",
    			getChoosedDevice().toString(), "shell", "ls", "/data/system/dropbox"});
    	//System.out.println("result:"+result);
    	if (result.contains("denied") && flag == true) {
    		MessageDialog.openInformation(null, "Error",
    				"This device has no root permission.");
    		return false;
    	} else if (result.contains("denied") && flag == false) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    
    public Combo getCombo() {
		return combo;
	}
    
	public UiAutomatorViewer() {
		
        super(null);
        
        app = this;
        
        manager = new FileManager();
		
		newAction = new NewAction();
		
		
		openAction = new OpenAction();
		saveAction = new SaveAction();
		//saveAsAction = new SaveAsAction();
		exitAction = new ExitAction();
		copyAction = new CopyAction();
		cutAction = new CutAction();
		pasteAction = new PasteAction();
		
		redoAction = new RedoAction();
		undoAction = new UndoAction();
		searchAction = new SearchAction(this);
		selectAllAction = new SelectAllAction();
		
		//pullAction = new PullAction();
		
		runAction = new RunAction(this);
		runBDAction = new RunBDAction(this);
		debugAction = new DebugAction(this);
		stopAction = new StopAction();
		runMultiAction = new RunMultiAction();
		
		//rootAction = new RootAction(this);
		installAction = new InstallAction(this);
		pushTemplateAction = new PushTemplateAction();
		
		aboutAction = new AboutAction();
		//scsiAction = new ScsiAction();
		
		this.addMenuBar();
		this.addStatusLine();
		
		
    }
    
    @Override
	protected MenuManager createMenuManager() {
		menuManager = new MenuManager("menu");
		
		MenuManager fileMenu = new MenuManager("File(&F)");
		MenuManager editMenu = new MenuManager("Edit(&E)");
		MenuManager formatMenu = new MenuManager("Format(&F)");
		MenuManager preconditionMenu = new MenuManager("Precondition(&P)");
		MenuManager runMenu = new MenuManager("Run(&R)");
		//MenuManager pullMenu = new MenuManager("Pull(&P)");
		//MenuManager toolsMenu = new MenuManager("Tools(&T)");
		MenuManager helpMenu = new MenuManager("Help(&H)");
		
		//MenuManager rootMenu = new MenuManager("Root");
		
		menuManager.add(fileMenu);
		menuManager.add(editMenu);
		menuManager.add(formatMenu);
		menuManager.add(preconditionMenu);
		menuManager.add(runMenu);
		//menuManager.add(pullMenu);
		menuManager.add(new PullMenuAction().PullAction(menuManager));
		menuManager.add(new ToolMenuAction().ToolAction(menuManager));
		editMenu.add(new VisibleAction().VisibleMenuAction(editMenu));
		//menuManager.add(toolsMenu);
		menuManager.add(helpMenu);
		
		fileMenu.add(newAction);
		
		fileMenu.add(openAction);
		fileMenu.add(new Separator());
		fileMenu.add(saveAction);
		//fileMenu.add(saveAsAction);
		fileMenu.add(new Separator());
		fileMenu.add(exitAction);
		
		//editMenu.add(undoAction);
		//editMenu.add(redoAction);
		editMenu.add(new Separator());
		editMenu.add(copyAction);
		editMenu.add(cutAction);
		editMenu.add(pasteAction);
		editMenu.add(new Separator());
		editMenu.add(searchAction);
		editMenu.add(new Separator());
		editMenu.add(selectAllAction);
		
		runMenu.add(runAction);
		runMenu.add(debugAction);
		runMenu.add(stopAction);
		runMenu.add(runMultiAction);
		runMenu.add(runBDAction);
		
		//pullMenu.add(new PullMenuAction(pullMenu));
		
		formatMenu.add(new FormatAction(FormatAction.TYPE_FONT));
		formatMenu.add(new FormatAction(FormatAction.TYPE_BGCOLOR));
		formatMenu.add(new FormatAction(FormatAction.TYPE_FORECOLOR));
		
		//preconditionMenu.add(rootMenu);
		//rootMenu.add(scsiAction);
		//rootMenu.add(rootAction);
		
		//preconditionMenu.add(remountAction);
		preconditionMenu.add(installAction);
		preconditionMenu.add(pushTemplateAction);
		
		helpMenu.add(new HelpAction(HelpAction.PPTX));
		helpMenu.add(new HelpAction(HelpAction.XLSX));
		helpMenu.add(aboutAction);
		
		//app.getStatusLineManager();
		
		return menuManager;
	}
    
    
    @Override
    protected Control createContents(Composite parent) {
    	
        Composite c = new Composite(parent, SWT.BORDER);
        
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        c.setLayout(gridLayout);
        
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        c.setLayoutData(gd);
        
        ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
        
        ///*************************************Create by carl
        
        IContributionItem comboCI = new ControlContribution(null) {
        	
			@Override
			protected Control createControl(Composite arg0) {
				// TODO Auto-generated method stub
				combo = new Combo(arg0, SWT.DROP_DOWN |SWT.READ_ONLY);
				//combo.setSize(1000, 10);
				//combo.setLayoutData(layoutData);
				
				devices = DebugBridge.getDevices();
				
				//If there is no devices connected, return a null list box.
				if (devices.size()==0) {
					return combo;
				}
				
				//Add devices list to the list box: combo
				
				for (int i=0; i<devices.size(); i++) {
					combo.add(devices.get(i).getSerialNumber()+"---"+devices.get(i).getState());
				}
				
				//setup one default value
				combo.setText(devices.get(0).getSerialNumber()+"---"+devices.get(0).getState());
				choosedDevice = devices.get(0);
				
				//add one listener, if user choose another device, then update
				combo.addSelectionListener(new SelectionAdapter() {
	                @Override
	                public void widgetSelected(SelectionEvent arg01) {
	                	
	                	choosedDevice = devices.get(combo.getSelectionIndex());
	                	
	                }
	            });
				
				return combo;
			}
		};
		
        ///*****************************************************
        toolBarManager.add(comboCI);
        toolBarManager.add(new Separator());
		
		//toolBarManager.add(new ListDevice(this));
        toolBarManager.add(new RefreshDeviceAction(this));
        toolBarManager.add(new Separator());
        
        toolBarManager.add(getXMLAction);
        toolBarManager.add(new Separator());
        
        toolBarManager.add(new OpenFilesAction(this));
        toolBarManager.add(new ScreenshotAction(this,false));
        //toolBarManager.add(new ScreenshotAction(this,true));
        
        toolBarManager.add(new SaveScreenShotAction(this));
        
        ToolBar tb = toolBarManager.createControl(c);
        tb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        mUiAutomatorView = new UiAutomatorView(c, SWT.BORDER);
        mUiAutomatorView.setLayoutData(new GridData(GridData.FILL_BOTH));
        //mUiAutomatorView.set
        
        /*
        parent.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				// TODO Auto-generated method stub
				CTabItem[] cTabItem = mUiAutomatorView.getScriptFolder().getItems();
				System.out.println(cTabItem.length);
				for (int i=0; i<cTabItem.length; i++) {
					System.out.println(cTabItem[i].getText());
					System.out.println(cTabItem[i].getText().contains("*"));
					if (cTabItem[i].getText().contains("*")) {
						MessageBox box = new MessageBox(
						UiAutomatorViewer.getApp().getShell(),
						 SWT.ICON_QUESTION|SWT.YES |SWT.NO);
						box.setText("Warning");
						box.setMessage("There is file does not saved, 
						do you want to close program?");
						int choice = box.open();
						if (choice == SWT.NO) {
							//fileManager.setDirty(true);
							//break;
							return;
						}
					}
				}
			}
        	
        });
        
        /*
        parent.addControlListener(new ControlListener() {

			@Override
			public void controlMoved(ControlEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void controlResized(ControlEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("disposed");
				CTabItem[] cTabItem = mUiAutomatorView.getScriptFolder().getItems();
				System.out.println(cTabItem.length);
				for (int i=0; i<cTabItem.length; i++) {
					System.out.println(cTabItem[i].getText());
					System.out.println(cTabItem[i].getText().contains("/*"));
					if (cTabItem[i].getText().contains("/*")) {
						MessageBox box = new MessageBox(UiAutomatorViewer.getApp().getShell(),
						 SWT.ICON_QUESTION|SWT.YES |SWT.NO);
						box.setText("Warning");
						box.setMessage("There is file does not saved,
						 do you want to close program?");
						int choice = box.open();
						if (choice == SWT.NO) {
							//fileManager.setDirty(true);
							return;
						}
					}
				}
			}
        	
        });
        */
        return parent;
    }
    
    public static void main(String args[]) {
        DebugBridge.init();
        
        //mClipboard = new Clipboard(Display.getCurrent());

        try {
        	
            UiAutomatorViewer window = new UiAutomatorViewer();
            
//            login = new Login(window.getShell());
//            login.open();
//            
//            //if user login failed, close window
//            if (!login.isLogin()) {
//            	System.out.println("login failed");
//            	//this.close();
//            	return;
//            }
//            System.out.println("login success");
            
            //window.setShellStyle(SWT.FOCUSED|SWT.FocusIn|SWT.FocusOut);
            window.setBlockOnOpen(true);
            window.open();
            
            Display.getCurrent().dispose();
            
            //System.out.println(window.set);
            //cb.dispose();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DebugBridge.terminate();
        }
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        
        newShell.setMaximized(true);
        newShell.open();
        newShell.setText("CAT--created by liguo");
        
        newShell.setImage(ImageHelper.
        		loadImageDescriptorFromResource("images/myself.jpg").createImage());
    }

    /*
    @Override
    protected Point getInitialSize() {
        return new Point(800, 600);
    }
	*/
    
    public void setModel(final UiAutomatorModel model, final File modelFile,
    		final Image screenshot) {
    	
        if (Display.getDefault().getThread() != Thread.currentThread()) {
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    mUiAutomatorView.setModel(model, modelFile, screenshot);
                }
            });
        } else {
            mUiAutomatorView.setModel(model, modelFile, screenshot);
        }
        
        
    }
    
    public CTabFolder getScriptFolder() {
    	return mUiAutomatorView.getScriptFolder();
    }
    public Image getScreenShot() {
        return mUiAutomatorView.getScreenShot();
    }
    public File getModelFile(){
        return mUiAutomatorView.getModelFile();
    }
    public StyledText getStyledText() {
    	return mUiAutomatorView.getStyledText();
    }
    
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}
	
    @Override
	public StatusLineManager getStatusLineManager() {
		return super.getStatusLineManager();
	}
    
    public FileManager getManager() {
		return manager;
	}
    
    public void setManager(FileManager manager) {
		this.manager = manager;
	}
    
	public UiAutomatorView getmUiAutomatorView() {
		return mUiAutomatorView;
	}

	public static UiAutomatorViewer getApp() {
		return app;
	}

	public StyledText getContent() {
		// TODO Auto-generated method stub
		return mUiAutomatorView.getStyledText();
	}
	
	public void messageBox_OK(String title, String content) {
		MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_INFORMATION |SWT.OK);
		messageBox.setMessage(content);
		messageBox.setText(title);
		messageBox.open();
	}
	
	public void updateCombo() {
    	
    	devices = DebugBridge.getDevices();
    	combo.removeAll();
		//If there is no devices connected, return a null list box.
		if (devices.size()==0) {
			
			combo.setText("");
			return;
		}
		
		//String[] list = ;
		//System.out.println("devices count: "+devices.size());
		//combo.setItems(devices.toArray(new String[0]));
		
		//Add devices list to the list box: combo
		for (int i=0; i<devices.size(); i++) {
			combo.add(devices.get(i).getSerialNumber()+"---"+devices.get(i).getState());
		}
		
		//setup one default value
		combo.setText(devices.get(0).getSerialNumber()+"---"+devices.get(0).getState());
		choosedDevice = devices.get(0);
		
		//combo.setSize(devices.get(0).getName().length(), combo.getTextHeight());
    }

}
