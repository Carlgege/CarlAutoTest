package com.carl.actions;

//import org.eclipse.jface.action.MenuManager;
//import org.eclipse.jface.action.StatusLineManager;
//import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.android.uiautomator.UiAutomatorViewer;

public class FindAndReplace extends ApplicationWindow {
	
	private Button btFind;
	private Button btReplace;
	private Button btReplaceAll;
	private Button btClose;
	private StyledText selectedStyledText;
	
	private Label findResult;
	private int index = 0;
	
	public boolean opened = false;
	
	/**
	 * Create the application window.
	 */
	public FindAndReplace() {
		super(null);
		//createActions();
		//addToolBar(SWT.FLAT | SWT.WRAP);
		//addMenuBar();
		//addStatusLine();
	}
	
	@Override
	public boolean close() {
		super.close();
		opened = false;
		return true;
	}
	

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite container) {
		Composite parent = new Composite(container, SWT.NONE);
		
		selectedStyledText = UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer();
		
		parent.setLayout(new GridLayout(2,false));
		new Label(parent, SWT.LEFT).setText("Find: ");
		final Text findText = new Text(parent, SWT.BORDER);
		findText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(parent, SWT.LEFT).setText("Relace: ");
		final Text replaceText = new Text(parent, SWT.BORDER);
		replaceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Group group = new Group(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		group.setLayoutData(data);
		group.setText("Direction");
		group.setLayout(new GridLayout(2, true));
		
		final Button forwardButton = new Button(group, SWT.RADIO);
		forwardButton.setText("Forward");
		
		final Button backButton = new Button(group, SWT.RADIO);
		backButton.setText("Backward");
		
		group = new Group(parent, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		group.setLayoutData(data);
		group.setText("Item");
		group.setLayout(new GridLayout(2, true));
		
		final Button match = new Button(group, SWT.CHECK);
		match.setText("Case sensitive");
		
		final Button wholeWord = new Button(group, SWT.CHECK);
		wholeWord.setText("Whole word");
		
		final Button regexp = new Button(group, SWT.CHECK);
		regexp.setText("regexp");
		//new Label(group, SWT.NONE);
		
		Composite composite = new Composite(parent, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		composite.setLayoutData(data);
		composite.setLayout(new GridLayout(2, true));
		
		btFind = new Button(composite, SWT.PUSH);
		btFind.setText("Find");
		btFind.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		btReplaceAll = new Button(composite, SWT.PUSH);
		btReplaceAll.setText("Replace All");
		btReplaceAll.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		btReplace = new Button(composite, SWT.PUSH);
		btReplace.setText("Replace");
		btReplace.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		btClose = new Button(composite, SWT.PUSH);
		btClose.setText("Close");
		btClose.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		findResult = new Label(parent, SWT.LEFT);
		findResult.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		findResult.setText("Find result: ");
		
		wholeWord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (wholeWord.getSelection()) {
					regexp.setSelection(false);
					regexp.setEnabled(false);
				} else {
					regexp.setEnabled(true);
				}
			}
		});
		
		regexp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (regexp.getSelection()) {
					wholeWord.setSelection(false);
					wholeWord.setEnabled(false);
				} else {
					wholeWord.setEnabled(true);
				}
			}
		});
		
		findText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				if (findText.getText().equals("")) {
					btFind.setEnabled(false);
				} else {
					btFind.setEnabled(true);
				}
			}
		});
		
		
		btFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				
				if (selectedStyledText == null) {
					return;
				}
				
				String findStr = findText.getText();				
				String textContent = selectedStyledText.getText();
				
				//System.out.println("find text: "+findStr);
				//System.out.println("text content: "+textContent);
				
				if (!textContent.contains(findStr)) {
					//System.out.println("runed");
					
					updateFindResult("String not found");
					
				} else {
					
					updateFindResult("String found");
					
					final Color RED = UiAutomatorViewer.getApp().getShell().getDisplay().getSystemColor(SWT.COLOR_RED);
					
					GC gc = new GC(selectedStyledText);
					
					int stringWidth = gc.stringExtent(findStr).x;
			        int lineHeight = selectedStyledText.getLineHeight();
			        
			        gc.setForeground(RED);
			        index = selectedStyledText.getText().indexOf(findStr);
			        while (index != -1) {
			          Point topLeft = selectedStyledText.getLocationAtOffset(index);
			          gc.drawRectangle(topLeft.x - 1, topLeft.y, stringWidth + 1, lineHeight - 1);
			          index = selectedStyledText.getText().indexOf(findStr, index + 1);
			        }
				}
				
				/*
				offsetText++;
				
				for (int i=0; i<selectedStyledText.getLineCount(); i++) {
					System.out.println("Line"+i+": "+selectedStyledText.getLine(i));
					if (selectedStyledText.getLine(i).contains(findStr)) {
						selectedStyledText.setCursor(new Cursor());
						selectedStyledText.setSelection(start, end);
					}
				}
				
				
				String [] textContentLine = textContent.split("\r\n");
				
				for (int i=0; i<textContentLine.length; i++) {
					//System.out.println(textContentLine[i]);
					
					if (textContentLine[i].contains(findStr)) {
						//selectedStyledText.setLineWrapIndent(startLine, lineCount, wrapIndent);
					}
				}
				*/
				
				enableReplaceButtons(true);
			}
		});
		
		btReplace.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent event) {
				
				if (selectedStyledText == null) {
					return;
				}
				
				String findStr = findText.getText();				
				String textContent = selectedStyledText.getText();
				
				index = selectedStyledText.getText().indexOf(findStr);
				
				if (!textContent.contains(findStr)) {
					
					updateFindResult("String not found");
					
				} else {
					
					updateFindResult("String found");
					
					final Color RED = UiAutomatorViewer.getApp().getShell().getDisplay().getSystemColor(SWT.COLOR_RED);
					
					GC gc = new GC(selectedStyledText);
					
					int stringWidth = gc.stringExtent(findStr).x;
			        int lineHeight = selectedStyledText.getLineHeight();
			        
			        gc.setForeground(RED);
			        
			        //System.out.println("index="+index);

		        	String tempStr0 = selectedStyledText.getText().substring(0,index);
		        	String tempStr1 = selectedStyledText.getText().substring(index).replaceFirst(findText.getText(), replaceText.getText());
		        	
		        	selectedStyledText.setText(tempStr0+tempStr1);
		        	
		        	Point topLeft = selectedStyledText.getLocationAtOffset(index);
		        	
		        	gc.drawRectangle(topLeft.x - 1, topLeft.y, stringWidth + 1, lineHeight - 1);
		        	index = selectedStyledText.getText().indexOf(findStr, index + 1);
			        
				}
			}
		});
		
		btReplaceAll.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent event) {
				
				if (selectedStyledText == null) {
					return;
				}
				
				if (!selectedStyledText.getText().contains(findText.getText())) {
					updateFindResult("String not found");
				} else {
					updateFindResult("String found");
					selectedStyledText.setText(selectedStyledText.getText().replaceAll(findText.getText(), replaceText.getText()));
				}
			}
		});
		
		btClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				getShell().dispose();
			}
		});
		
		forwardButton.setSelection(true);
		enableReplaceButtons(false);
		findText.setFocus();
		
		//return container;
		return parent;
	}

	/**
	 * Create the actions.
	 */
	
	/*
	private void createActions() {
		// Create the actions
	}
	*/

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	/*
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}
	*/

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	/*
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}
	*/

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	/*
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}
	*/
	/**
	 * Launch the application.
	 * @param args
	 */
	/*
	public static void main(String args[]) {
		try {
			FindAndReplace window = new FindAndReplace();
			window.setBlockOnOpen(true);
			window.open();
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
		newShell.setText("Find/Replace");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 400);
	}
	

	private void enableReplaceButtons(boolean enable) {
		btReplace.setEnabled(enable);
		btReplaceAll.setEnabled(enable);
	}
	
	private void updateFindResult(String text) {
		findResult.setText(text);
	}
	

}
