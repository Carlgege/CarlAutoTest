package com.carl.table;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
//import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
//import org.eclipse.swt.custom.CTabFolder;
//import org.eclipse.swt.dnd.DND;
//import org.eclipse.swt.dnd.DropTarget;
//import org.eclipse.swt.dnd.DropTargetAdapter;
//import org.eclipse.swt.dnd.DropTargetEvent;
//import org.eclipse.swt.dnd.FileTransfer;
//import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.android.uiautomator.UiAutomatorView;
import com.android.uiautomator.UiAutomatorViewer;
import com.carl.actions.CMDUtils;
import com.carl.actions.DateUtils;
import com.carl.actions.InstallAction;

public class ScriptListTable extends ApplicationWindow {
	
	private TableViewer tableScriptList;
	
	private ArrayList<String> scriptFileName = new ArrayList<String>();
	private ArrayList<String> scriptFilePath = new ArrayList<String>();
	
	
	//private ArrayList<String> scriptRepeat = new ArrayList<String>();
	
	//private Clipboard clipboard;
	
	public static final int ID = 0;
	public static final int NAME = 1;
	public static final int FILEPATH = 2;
	public static final int REPEAT = 3;
	
	//private File temp;
	private ArrayList<ScriptEO> scriptList = new ArrayList<ScriptEO>();
	
	public static final String[] COLUMN_NAME = {"Id","Name","FilePath","Repeat"};
	
	private TableEditor tableEditorModifyRepeat;
	private Text textEditRepeat;
	
	private ArrayList<TableEditor> tableEditorModifyRepeatList = new ArrayList<TableEditor>();
	private ArrayList<Text> textEditRepeatList = new ArrayList<Text>();
	
	private static ArrayList<String> wirelessDeviceList = new ArrayList<String>();
	
	public boolean opened = false;
	
	public TableViewer getTableScriptList() {
		return tableScriptList;
	}

	/**
	 * Create the application window.
	 */
	public ScriptListTable() {
		super(null);
		//initScriptList();
		
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		//initScriptList();
		
		//scriptFileName.removeAll(scriptFileName);
		//scriptFilePath.removeAll(scriptFilePath);
	}
	
	/*
	private void initScriptList() {
		scripts = new ArrayList<ScriptEO>();
		scripts.add(new ScriptEO(1,"1111111","11111111"));
	}
	*/
	
	public void subFolderList(File rootFile) {
		
		File[] files = rootFile.listFiles();
		if ((files != null) && (files.length > 0)) {
			for (int i = 0; i < files.length; i++) {
				
				if (files[i].getAbsolutePath().endsWith(".sh")) {
					//System.out.println(files[i].toString());
					
					ScriptEO script = new ScriptEO();
    				
    				script.setID(tableScriptList.getTable().getItemCount()+1L);
    				script.setName(files[i].getName());
    				script.setFilepath(files[i].getAbsolutePath());
    				script.setRepeat(String.valueOf(ScriptEO.defaultRepeat));
    				//script.repeat=ScriptEO.repeat;
    				
    				tableScriptList.add(script);
    				
    				scriptFileName.add(files[i].getName());
    				scriptFilePath.add(files[i].getAbsolutePath());
    				
    				//scriptRepeat.add("1");
    				
    				scriptList.add(script);
				}
				
				subFolderList(files[i]);
			}
		}
	}
	
	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		//Composite container = new Composite(parent, SWT.NONE);
		
		tableScriptList = new TableViewer(parent, SWT.FULL_SELECTION);
		for (int i=0; i<COLUMN_NAME.length; i++) {
			new TableColumn(tableScriptList.getTable(), SWT.LEFT).setText(COLUMN_NAME[i]);
			tableScriptList.getTable().getColumn(i).pack();
		}
		
		tableScriptList.getTable().setHeaderVisible(true);
		tableScriptList.getTable().setLinesVisible(true);
		
		tableScriptList.setContentProvider(new MyContentProvider());
		tableScriptList.setLabelProvider(new MyLabelProvider());
		tableScriptList.setSorter(new TableScriptListSorter());
		tableScriptList.setInput(scriptList);
		
		TableColumn tableColumn = tableScriptList.getTable().getColumn(0);
		tableColumn.setWidth(30);
		tableColumn.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Sort the table
				((TableScriptListSorter)tableScriptList.getSorter()).doSort(ScriptListTable.ID);
				tableScriptList.refresh();
				//refreshRepeat();
				updateSingleRepeatValue();
			}
		});
		
		tableColumn = tableScriptList.getTable().getColumn(1);
		tableColumn.setWidth(250);
		tableColumn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				((TableScriptListSorter)tableScriptList.getSorter()).doSort(ScriptListTable.NAME);
				tableScriptList.refresh();
				//refreshRepeat();
				updateSingleRepeatValue();
			}
		});
		
		tableColumn = tableScriptList.getTable().getColumn(2);
		tableColumn.setWidth(300);
		tableColumn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				((TableScriptListSorter)tableScriptList.getSorter()).doSort(ScriptListTable.FILEPATH);
				tableScriptList.refresh();
				//refreshRepeat();
				updateSingleRepeatValue();
			}
		});
		
		createContextMenu();
		
		/*
		TableViewerColumn  tableViewerId = new TableViewerColumn(tableScriptList, SWT.NONE);
		TableColumn tableColumnId = tableViewerId.getColumn();
		TableColumnLayout columnLayout = new TableColumnLayout();
		tableScriptList.getTable().setLayout(columnLayout);
		
		columnLayout.setColumnData(tableColumnId,
                new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		*/
		
		
		/*
		DropTarget dropTarget = new DropTarget(tableScriptList.getTable(), DND.DROP_MOVE
                | DND.DROP_COPY | DND.DROP_LINK |DND.DROP_TARGET_MOVE);
		
		dropTarget.setTransfer(new Transfer[] {
                LocalSelectionTransfer.getTransfer(),
                FileTransfer.getInstance() });
		
		dropTarget.addDropListener(new DropTargetAdapter() {
			
			@Override
            public void drop(DropTargetEvent event) {
				
				scriptFileName.removeAll(scriptFileName);
				scriptFilePath.removeAll(scriptFilePath);
            	
            	String[] files = (String[]) event.data;
            	for (int i=0; i<files.length; i++) {
            		
            		temp = new File(files[i]);
            		//System.out.println("start"+i+":"+files[i]);
            		//System.out.println("is faile:"+temp.isFile());
            		if (temp.isFile()) {
            			if (temp.getAbsolutePath().endsWith(".sh")) {
            				//System.out.println("file"+i+":"+files[i]);
            				ScriptEO script = new ScriptEO();
            				
            				script.setID(tableScriptList.getTable().getItemCount()+1L);
            				script.setName(temp.getName());
            				script.setFilepath(temp.getAbsolutePath());
            				
            				tableScriptList.add(script);
            				
            				scriptFileName.add(temp.getName());
            				scriptFilePath.add(temp.getAbsolutePath());
            				
            				scriptList.add(script);
            			}
            		} else {
            			//System.out.println("Other"+i+":"+files[i]);
            			
            			subFolderList(temp);
            			
            			/*
            			for (int j=0; j<temp.listFiles().length; j++) {
            				if (temp.listFiles()[j].getAbsolutePath().endsWith(".sh")) {
            					//System.out.println(temp.listFiles()[j].getAbsolutePath());
                				
                				ScriptEO script = new ScriptEO();
                				
                				script.setID(tableScriptList.getTable().getItemCount()+1L);
                				script.setName(temp.listFiles()[j].getName());
                				script.setFilepath(temp.listFiles()[j].getAbsolutePath());
                				
                				tableScriptList.add(script);
                				
                				scriptFileName.add(temp.listFiles()[j].getName());
                				scriptFilePath.add(temp.listFiles()[j].getAbsolutePath());
                				
                				scriptList.add(script);
            				}
            			}
            		}
            	}
            	getStatusLineManager().setMessage("Total script: "+scriptList.size());
            	
            	//Add Repeat edit listener
    			updateSingleRepeatValue();
            }
        });
		*/
		
		return parent;
	}
	
	
	public void updateSingleRepeatValue() {
		//create a TableEdit object
		
		TableItem [] items = tableScriptList.getTable().getItems();
		
		if (textEditRepeatList.size() > 0) {
			disposeTableEditor();
			textEditRepeatList.removeAll(textEditRepeatList);
		}
		if (tableEditorModifyRepeatList.size() > 0) {
			disposeTableEditor();
			tableEditorModifyRepeatList.removeAll(tableEditorModifyRepeatList);
		}
		//System.out.println("items.length= "+items.length);
		
		for (int i=0; i<items.length; i++) {
			tableEditorModifyRepeat = new TableEditor(tableScriptList.getTable());
			textEditRepeat = new Text(tableScriptList.getTable(), SWT.NONE);
			textEditRepeat.setText(items[i].getText(REPEAT));
			
			tableEditorModifyRepeat.grabHorizontal = true;
			tableEditorModifyRepeat.setEditor(textEditRepeat, items[i], REPEAT);
			textEditRepeatList.add(textEditRepeat);
			tableEditorModifyRepeatList.add(tableEditorModifyRepeat);
			
			final int f = i;
			
			textEditRepeatList.get(f).addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent arg0) {
					
					//Update the value
					//System.out.println("before modify item"+f+":"+tableScriptList.getTable().getItem(f).getText(REPEAT));
					
					//System.out.println(textEditRepeat.getText());
					
					tableScriptList.getTable().getItem(f).setText(REPEAT, textEditRepeatList.get(f).getText());
					scriptList.get(f).setRepeat(textEditRepeatList.get(f).getText());
					
					//System.out.println("after modify item"+f+":"+tableScriptList.getTable().getItem(f).getText(REPEAT));
					//tableEditorModifyRepeat.getItem().setText(REPEAT,textEditRepeat.getText());
				}
			});
		}
	}
	
	public void disposeTableEditor() {
		
		for (int i=0; i<textEditRepeatList.size(); i++) {
			textEditRepeatList.get(i).dispose();
			//tableEditorModifyRepeatList.get(i).dispose();
		}
		//System.out.println("tableEditorModifyRepeatList.size()"+tableEditorModifyRepeatList.size());
		for (int i=0; i<tableEditorModifyRepeatList.size(); i++) {
			//textEditRepeatList.get(i).dispose();
			tableEditorModifyRepeatList.get(i).dispose();
		}
	}
	
	private void createContextMenu() {
		MenuManager menu = new MenuManager();
		
		//AddAction addAction = new AddAction();
		//addAction.setText("New");
		menu.add(new AddAction());
		menu.add(new AddFolderAction());
		menu.add(new DelAction());
		menu.add(new RunAction());
		menu.add(new ClearAllAction());
		
		Menu m = menu.createContextMenu(getShell());
		tableScriptList.getTable().setMenu(m);
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		
		MenuManager editMemu = new MenuManager("Edit");
		MenuManager runMemu = new MenuManager("Run");
		
		editMemu.add(new AddAction());
		editMemu.add(new AddFolderAction());
		editMemu.add(new DelAction());
		editMemu.add(new ClearAllAction());
		editMemu.add(new RepeatAction());
		
		runMemu.add(new RunAction());
		
		menuManager.add(editMemu);
		menuManager.add(runMemu);
		
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}
	
	@Override
	public StatusLineManager getStatusLineManager() {
		return super.getStatusLineManager();
		
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	
	/*
	public static void main(String args[]) {
		try {
			ScriptListTable window = new ScriptListTable();
			window.setBlockOnOpen(true);
			window.open();
			//System.out.println("start???");
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Script list");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(750, 400);
	}
	
	
	@Override
	public int open() {
		
		if (!opened) {
			
			createActions();
			addToolBar(SWT.FLAT | SWT.WRAP);
			addMenuBar();
			addStatusLine();
			
			//initScriptList();
			
			//scriptFileName.removeAll(scriptFileName);
			//scriptFilePath.removeAll(scriptFilePath);
			
			super.open();
			
			updateSingleRepeatValue();
			opened = true;
			
		}
		return 0;
		
	}
	
	@Override
	public boolean close() {
		
		super.close();
		//System.out.println("closed");
		UiAutomatorView.wirelessRun = false;
		opened = false;
		
		return true;
		
	}
	
	public class AddAction extends Action {
		
		public AddAction() {
			setText("Add");
		}
		
		@Override
		public void run() {
			
			FileDialog dialog = new FileDialog(getShell(), SWT.OPEN|SWT.MULTI);
			dialog.setFilterPath("..\\Script");
			dialog.setFilterExtensions(new String[]{"*.sh", "*.*"});
			
			final String name = dialog.open();
			if ((name == null)||(name.length()==0)) {
				return;
			}
			
			File file;
			for (int i=0; i<dialog.getFileNames().length; i++) {
				file = new File(dialog.getFileNames()[i]);
				
				ScriptEO script = new ScriptEO();
				
				script.setID(tableScriptList.getTable().getItemCount()+1L);
				script.setName(file.getName());
				script.setFilepath(dialog.getFilterPath()+"\\"+file.getName());
				script.setRepeat(String.valueOf(ScriptEO.defaultRepeat));
				
				scriptList.add(script);
				tableScriptList.add(script);
				
				//This is a wrong command
				//System.out.println(new File(dialog.getFileNames()[i]).getAbsolutePath());
				//This is a right command
				//System.out.println(dialog.getFilterPath());
			}
			getShell().setFocus();
			getStatusLineManager().setMessage("Total script: "+scriptList.size());
			
			//Add Repeat edit listener
			updateSingleRepeatValue();
			/*
			File file = new File(name);
			
			ScriptEO script = new ScriptEO();
			
			script.setID(tableScriptList.getTable().getItemCount()+1L);
			script.setName(file.getName());
			script.setFilepath(file.getAbsolutePath());
			
			scriptList.add(script);
			
			tableScriptList.add(script);
			//getShell().setActive();
			getShell().setFocus();
			
			*/
		}
	}
	
	public class AddFolderAction extends Action {
		
		public AddFolderAction() {
			setText("AddFolder");
		}
		
		@Override
		public void run() {
			
			DirectoryDialog direDialog = new DirectoryDialog(getShell());
			direDialog.setMessage("Please choose one folder");
			direDialog.setText("Choose");
			
			direDialog.setFilterPath("..\\Script");
			String folder = direDialog.open();
			if (folder != null) {
				
				//System.out.println(folder);
				subFolderList(new File(folder));
			}
			
			/*
			FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
			dialog.setFilterExtensions(new String[]{"*.sh", "*.*"});
			final String name = dialog.open();
			if ((name == null)||(name.length()==0)) {
				return;
			}
			
			File file = new File(name);
			
			ScriptEO script = new ScriptEO();
			
			script.setID(tableScriptList.getTable().getItemCount()+1L);
			script.setName(file.getName());
			script.setFilepath(file.getAbsolutePath());
			
			scriptList.add(script);
			
			tableScriptList.add(script);
			//getShell().setActive();
			*/
			getShell().setFocus();
			getStatusLineManager().setMessage("Total script: "+scriptList.size());
			
			//Add Repeat edit listener
			updateSingleRepeatValue();
		}
	}
	
	public class DelAction extends Action {
		
		public DelAction() {
			setText("Del");
		}
		
		@Override
		public void run() {
			StructuredSelection selection = (StructuredSelection) tableScriptList.getSelection();
			
			ScriptEO s = (ScriptEO) selection.getFirstElement();
			if (s != null) {
				tableScriptList.remove(s);
				scriptList.remove(s);
				getStatusLineManager().setMessage("Total script: "+scriptList.size());
			}
		}
	}
	
	public class ClearAllAction extends Action {
		
		public ClearAllAction() {
			setText("Clear All");
		}
		
		@Override
		public void run() {
			
			tableScriptList.getTable().removeAll();
			scriptList.removeAll(scriptList);
			getStatusLineManager().setMessage("Total script: "+scriptList.size());
			
			disposeTableEditor();
			//tableEditorModifyRepeat.dispose();
			
			//System.out.println("textEditRepeatList.size()"+textEditRepeatList.size());
			
			//textEditRepeat.dispose();
			/*
			int listCount = tableScriptList.getTable().getItemCount();
			//System.out.println(listCount);
			for (int i=0; i<listCount; i++) {
				tableScriptList.getTable().remove(0);
			}
			*/
		}
	}
	
	public class RunAction extends Action {
		
		public RunAction() {
			setText("Push to test device(s)");
		}
		
		@Override
		public void run() {
			
			//ScriptEO script = new ScriptEO();
			//script.setID(tableScriptList);
			//System.out.println(new DateUtils().getDate());
			
			//new one script to save run info
			UiAutomatorViewer.getApp().getmUiAutomatorView().newCTabItem(new DateUtils().getDate());
			
			//insert text
			UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("source /data/local/tmp/Template.sh\n");
			//UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("cp $ScriptPath/*.sh /data/local/tmp\n");
			//UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("cd /data/local/tmp\n");
			//UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("busybox dos2unix *.sh\n");
			UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("\n");
			
			for (int i=0; i<tableScriptList.getTable().getItemCount(); i++) {
				//System.out.println(tableScriptList.getTable().getItem(i).getText(2));
				UiAutomatorViewer.getApp().getmUiAutomatorView().insertTextToStyledText("sh "+
				"\""+tableScriptList.getTable().getItem(i).getText(NAME)+
				"\" "+tableScriptList.getTable().getItem(i).getText(REPEAT)+"\n");
			}
			
			if (UiAutomatorView.wirelessRun) {
				//System.out.println("Multi device run");
				
				for (int i=0; i<wirelessDeviceList.size(); i++) {
					
					System.out.println("wirelessDeviceList "+i+"="+wirelessDeviceList.get(i));
					String SN = wirelessDeviceList.get(i)+":5558";
					pushScript(SN);
				}
				
				//return;
			} else {
				//System.out.println("Single device run");
				if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
            		return;
            	}
				
				pushScript(UiAutomatorViewer.getApp().getChoosedDevice().toString());
		        
			}
	        
			//getShell().setFocus();
			getShell().close();
			
		}
	}
	
	public void pushScript(final String SN) {
		
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
        try {
            dialog.run(true, false, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException,
                                                                        InterruptedException {
                	
                	for (int j=0; j<scriptList.size(); j++) {
                		//System.out.println(temp.listFiles()[j].getAbsolutePath());
    					monitor.subTask("Push script: "
								+ scriptList.get(j).getName());
    					
    					//System.out.println("name:"+scriptFileName.get(j));
    					//System.out.println("path"+scriptFilePath.get(j));
    					//push script to /mnt/sdcard/ShellResult/ScriptFolder
    					
    					//System.out.println(scriptList.get(j).getName());
    					//System.out.println(scriptList.get(j).getFilepath());
    					
    					CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",SN,
        						"push",scriptList.get(j).getFilepath(),
        						"/data/local/tmp/"+scriptList.get(j).getName()});
        				
    					//change the script to unix format
    					CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",SN,
        						"shell",InstallAction.busyboxPath+" dos2unix",
        						"/data/local/tmp/*.sh"});
        				
        			}
                	
                	monitor.done();
                }
            });
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
	}
	
	public static void setWirelessDeviceList(ArrayList<String> list) {
		wirelessDeviceList = list;
	}
	
	public class RepeatAction extends Action {
		
		public RepeatAction() {
			setText("Set default Repeat");
		}
		
		@Override
		public void run() {
			
			InputDialog inputDialog = new InputDialog(UiAutomatorViewer.getApp().getShell(),
					"Please input repeat times", //This is dialog's title
					"input a number here", //This is dialog's hint
					"", //Default value
					new numberValidator());
		
			inputDialog.open();
			
			String modifiedRepeat = inputDialog.getValue();
			
			ScriptEO.defaultRepeat = Integer.valueOf(modifiedRepeat);
			getStatusLineManager().setMessage("Update default repeat time to: "+ScriptEO.defaultRepeat);
			
			getShell().setFocus();
		}
		
	}
	
	class numberValidator implements IInputValidator {

		@Override
		public String isValid(String arg0) {
			if (!arg0.matches("[0-9]+")) {
				return "Error, please input a number";
			}
			
			return null;
		}
	}	

}
