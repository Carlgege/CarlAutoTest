package com.carl.actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.android.uiautomator.UiAutomatorViewer;

public class Upload extends ApplicationWindow {
	
	public String defaultValue = "";
	public String serverScriptFolder = "\\\\10.206.25.96\\dqa\\06_ZZ Special\\01_Auto test\\03_AutoTest_Carl\\Tools\\CATScripts";

	/**
	 * Create the application window.
	 */
	public Upload() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        container.setLayout(gridLayout);
        
        GridData gridData = new GridData();
        gridData.horizontalSpan= 5;
        gridData.horizontalAlignment = SWT.FILL;
        

		Group product = new Group(container, SWT.SHADOW_ETCHED_OUT);
		product.setLayout(new RowLayout());
		product.setText("Product");
		
		String [] productItems = new String[]{"InFocus", "Xiaomi"};
		Combo productList = new Combo(product, SWT.NONE);
		productList.setItems(productItems);
		
		defaultValue = getProduct();
		for (int i=0; i<productList.getItemCount(); i++) {
			if (defaultValue.equals(productList.getItem(i))) {
				productList.setText(productList.getItem(i));
			}
		}
		
		productList.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
			
		});
		
		
		/*
		Button[] productButton = new Button[productItems.length];
		
		defaultValue = getProduct();
		//System.out.println("Product:"+defaultValue);
		
		for (int i=0; i<productButton.length; i++) {
			productButton[i] = new Button(product, SWT.RADIO);
			productButton[i].setText(productItems[i]);
			
			if (defaultValue.equals(productItems[i])) {
				productButton[i].setSelection(true);
			}
		}
		*/
		
		Group platform = new Group(container, SWT.SHADOW_ETCHED_OUT);
		platform.setLayout(new RowLayout());
		platform.setText("Platform");
		
		String[] platformItems = new String[]{"QC", "MTK", "SC"};
		Combo platformList = new Combo(platform, SWT.NONE);
		platformList.setItems(platformItems);
		
		defaultValue = getPlatform();
		for (int i=0; i<platformList.getItemCount(); i++) {
			if (defaultValue.length() >= 2) {
				if (platformItems[i].toLowerCase().contains(defaultValue.substring(0, 2))) {
					platformList.setText(platformList.getItem(i));
				}
			}
		}
		
		
		Group android = new Group(container, SWT.SHADOW_ETCHED_OUT);
		android.setLayout(new RowLayout());
		android.setText("Android");

		String[] androidItems = new String[]{"4.4", "5.0", "5.1", "6.0", "7.0"};
		Combo androidList = new Combo(android, SWT.NONE);
		androidList.setItems(androidItems);
		
		defaultValue = getAndroidVersion();
		for (int i=0; i<androidList.getItemCount(); i++) {
			if (defaultValue.equals(androidItems[i])) {
				androidList.setText(androidList.getItem(i));
			}
		}
		
		Group sim = new Group(container, SWT.SHADOW_ETCHED_OUT);
		sim.setLayout(new RowLayout());
		sim.setText("SIM");
		
		defaultValue = getSIM();
		String[] simItems;
		if (defaultValue.equals(",")) {
			simItems = new String[]{"Not need"};
		} else {
			simItems = new String[]{defaultValue, "Not need"};
		}
		
		Combo simList = new Combo(sim, SWT.NONE);
		simList.setItems(simItems);
		simList.setText(simItems[0]);
		
		/*
		Button[] simButton = new Button[simItems.length];
		
		for (int i=0; i<simButton.length; i++) {
			simButton[i] = new Button(sim, SWT.RADIO);
			simButton[i].setText(simItems[i]);
		}
		//default not need SIM card
		//simButton[1].setSelection(true);
		*/
		
		Group feature = new Group(container, SWT.SHADOW_ETCHED_OUT);
		feature.setText("Feature");
		feature.setLayout(new RowLayout());
		
		Text featureName = new Text(feature, SWT.WRAP);
		featureName.setText("Input_Feature_Name");
		
		Group preconditon = new Group(container, SWT.SHADOW_ETCHED_OUT);
		preconditon.setText("Precondition");
		preconditon.setLayout(new GridLayout());
		preconditon.setLayoutData(gridData);
		
		Text tcPrecondition = new Text(preconditon, SWT.MULTI|SWT.BORDER|SWT.WRAP|SWT.V_SCROLL);
		//get precondition from script
		String scriptContent = UiAutomatorViewer.getApp().getmUiAutomatorView().getSelectedSourceViewer().getText();
		String searchString = "TestPrecondition=\"";
		int startSearch =  scriptContent.indexOf(searchString);
		//System.out.println("precondition:"+scriptContent.substring(startSearch+searchString.length(),
		//	scriptContent.indexOf("\"", startSearch+searchString.length())));
		
		tcPrecondition.setText(scriptContent.substring(startSearch+searchString.length(),
				scriptContent.indexOf("\"", startSearch+searchString.length())));
		tcPrecondition.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Group procedure = new Group(container, SWT.SHADOW_ETCHED_OUT);
		procedure.setText("Procedure");
		procedure.setLayout(new GridLayout());
		procedure.setLayoutData(gridData);
		
		Text tcProcedure = new Text(procedure, SWT.MULTI|SWT.BORDER|SWT.WRAP|SWT.V_SCROLL);
		//get procedure from script
		searchString = "TestStep=\"";
		startSearch =  scriptContent.indexOf(searchString);
		
		tcProcedure.setText(scriptContent.substring(startSearch+searchString.length(),
				scriptContent.indexOf("\"", startSearch+searchString.length())));
		tcProcedure.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//feature.setLayout(null);
		
		
		Button upload = new Button(container, SWT.PUSH);
		upload.setText("Upload");
		
		GridData uploadGridData = new GridData();
		uploadGridData.horizontalSpan= 5;
		uploadGridData.horizontalAlignment = SWT.CENTER;
		upload.setLayoutData(uploadGridData);
		
		upload.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				//get script name
				//System.out.println("scriptname:"+UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().getText());
				
				//get platform version
				//System.out.println("platform:"+platformList.getText());
				
				//get android version
				//System.out.println("androidversion:"+androidList.getText());
				
				//get feature
				//System.out.println("feature:"+featureName.getText());
				
				//get product
				//System.out.println("product:"+productList.getText());
				
				//get sim
				//System.out.println(simList.getText());
				
				//get test precondition
				//System.out.println(tcPrecondition.getText());
				
				//get test procedure
				//System.out.println(tcProcedure.getText());
				
				
				//connect to database and insert one value
				Connection conn = null;
		        String sql;
		        String url = "jdbc:mysql://"+UiAutomatorViewer.getApp().getLogin().ip+":3306/zzdcdqa?"
		                + "user=root&password=&useUnicode=true&characterEncoding=UTF8";
		        
		        try {
		            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
		            
		            conn = DriverManager.getConnection(url);
		            Statement stmt = conn.createStatement();
		            
		            sql = "INSERT INTO `script` "
		            		+ "(`name`,`product`,`platform`,`android`,`ap`,`sim`,`precondition`,`step`,`uploader`) "
		            		+ "VALUES ('"+UiAutomatorViewer.getApp().getmUiAutomatorView().getSeletedCTabItem().getText()+"', '"
		            		+productList.getText()+"', '"+platformList.getText()+"', '"+androidList.getText()+"', '"
		            		+featureName.getText()+"', '"+simList.getText()+"', '"+tcPrecondition.getText()+"', '"
		            		+tcProcedure.getText()+"', '"+UiAutomatorViewer.getApp().getNewAction().getUsername()+"')";
		            
		            //copy script to server
		            //create folder
		            CMDUtils.cmdCommandGetInput(new String[] {"cmd", "/c", "mkdir",
		            		"\""+serverScriptFolder+"\\" + productList.getText()
		            		+"\\" + androidList.getText()+"\\" + featureName.getText()+"\""});
		            
		            /*
		            String [] createFolder = new String[] {"cmd", "/c", "mkdir",
		            		"\""+serverScriptFolder+"\\" + productList.getText()+"\\"+ androidList.getText()+"\""};
		            for (int i=0; i<createFolder.length; i++) {
		            	System.out.println(createFolder[i]);
		            }
		            */
		            
		            String copyResult = CMDUtils.cmdCommandGetInput(new String[] {"cmd", "/c", "copy",
		            		UiAutomatorViewer.getApp().getSaveAction().savedScript,
		            		"\""+serverScriptFolder+"\\" + productList.getText()+"\\"
		            		+ androidList.getText()+"\\" + featureName.getText()+"\""});
		            
		            UiAutomatorViewer.getApp().messageBox_OK("Upload result", copyResult);
		            
		            //System.out.println("sql:\n"+sql);
		            stmt.executeUpdate(sql);
		            
		            
		        } catch (SQLException ee) {
		            System.out.println("MySQL操作错误");
		            
		        } catch (Exception ee) {
		            //ee.printStackTrace();
		        } finally {
		            try {
						conn.close();
					} catch (SQLException ee) {
						//ee.printStackTrace();
					}
		        }
		        
		        //show one message dialog to prompt upload result
		        
		        
		        close();
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
			
		});
		
		
		/*
		Button cancel = new Button(container, SWT.PUSH);
		cancel.setText("Cancel");
		
		cancel.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getShell().close();
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
			
		});
		*/
		
		return container;
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

	/**
	 * Launch the application.
	 * @param args
	 */
	/*
	public static void main(String args[]) {
		try {
			Upload window = new Upload();
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
		newShell.setText("Upload");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 280);
	}
	
	public String getAndroidVersion() {
		if (UiAutomatorViewer.getApp().getChoosedDevice() != null) {
			return CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",
					UiAutomatorViewer.getApp().getChoosedDevice().toString(),
					"shell","getprop","ro.build.version.release"}).replaceAll("\r|\n", "");
		}
		return "";
		
	}
	
	public String getPlatform() {
		if (UiAutomatorViewer.getApp().getChoosedDevice() != null) {
			return CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",
					UiAutomatorViewer.getApp().getChoosedDevice().toString(),
					"shell","getprop","ro.hardware"}).replaceAll("\r|\n", "");
		}
		return "";
	}
	
	public String getProduct() {
		if (UiAutomatorViewer.getApp().getChoosedDevice() != null) {
			return CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",
					UiAutomatorViewer.getApp().getChoosedDevice().toString(),
					"shell","getprop","ro.product.brand"}).replaceAll("\r|\n", "");
		}
		return "";
	}
	
	public String getSIM() {
		if (UiAutomatorViewer.getApp().getChoosedDevice() != null) {
			return CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",
					UiAutomatorViewer.getApp().getChoosedDevice().toString(),
					"shell","getprop","gsm.sim.operator.alpha"}).replaceAll("\r|\n", "");
		}
		return ",";
	}
	
}
