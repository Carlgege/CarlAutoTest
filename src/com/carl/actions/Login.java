package com.carl.actions;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import com.android.uiautomator.UiAutomatorViewer;

public class Login extends Dialog {
	
	public static final int LOGIN_ID = 0;
	public static final int CANCEL_ID = 1;
	public static final String LOGIN_LABEL = "login";
	public static final String CANCEL_LABEL = "cancel";

	public static final String SERVERIP = "ServerIP";
	public static final String USERNAME = "Username";
	public static final String PASSWORD = "Password";
	
	private boolean login = false;
	public Button okButton;
	public Button cancelButton;
	
	
	public static final String LOCALRESULTLPATH = "C:\\Users\\Public\\Documents";
	public PreferenceStore preferenceStore = new PreferenceStore(LOCALRESULTLPATH + "\\preference.pref");
	
	public Text serverIP;
	public Text username;
	public Text password;
	
	public String ip;
	
	
	public boolean isLogin() {
		return login;
	}
	
//	public String getUsername() {
//		
//		return username.getText();
//	}
	

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	//public Login(Shell parentShell) {
	//	super(parentShell);
	//}
	public Login(Shell parentShell) {
		super(parentShell);
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("CAT login");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		group.setText("Please input username and password");
		GridLayout layout = new GridLayout();
		layout.marginHeight = 20;
		layout.marginWidth = 20;
		layout.numColumns = 2;
		group.setLayout(layout);
		
		new Label(group, SWT.LEFT).setText("ServerIP: ");
		serverIP = new Text(group, SWT.BORDER);
		serverIP.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(group, SWT.NONE).setText("Account: ");
		username = new Text(group, SWT.BORDER|SWT.SINGLE);
		username.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(group, SWT.NONE).setText("Password: ");
		password = new Text(group, SWT.BORDER|SWT.SINGLE);
		password.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		password.setEchoChar('*');
		
		loadDefaultValue();
		
		//System.out.println(CMDUtils.cmdCommandGetInput(new String[] { "cmd","/c","arp -a"}));
		
//		ArrayList<String> cmds = new ArrayList<String>();
//		cmds.add("cmd.exe");
//		cmds.add("-c");
//		cmds.add("arp -a");
//		ProcessBuilder pb= new ProcessBuilder(cmds);
//		
//		String cmdPerformResult = "";
//	  	String cmdReadline = "";
//		try {
//			Process p = pb.start();
//			p.waitFor();
//			
//			BufferedReader commandResult = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//  	  		
//  			//System.out.println("inputstream:"+commandResult.readLine().length());
//  			
//  	  		try	{
//  	  			while ((cmdReadline = commandResult.readLine()) != null) {
//  	  				
//  	  				cmdPerformResult = cmdPerformResult + cmdReadline + "\n";
//  	  			}
//  	  		} catch (IOException e1) {
//  	  			e1.printStackTrace();
//  	  		}
//			
//			//p.waitFor();
//			System.out.println("result:\n"+cmdPerformResult);
//		} catch (IOException | InterruptedException  e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		
		
		if (serverIP.getText().equals("")) {
			
			serverIP.setText("10.206.24.197");
		}
		
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, Login.LOGIN_ID, Login.LOGIN_LABEL,
				true);
		
		cancelButton = createButton(parent, Login.CANCEL_ID, Login.CANCEL_LABEL, false);
		
	}

	/**
	 * Return the initial size of the dialog.
	 */
//	@Override
//	protected Point getInitialSize() {
//		return new Point(220, 230);
//	}
	
	
	protected void buttonPressed(int buttonId) {
		if (Login.LOGIN_ID == buttonId) {
			
			ip = serverIP.getText();
	        String un = username.getText();
	        String pwd = password.getText();
	        
//	        System.out.println("ip:"+ip);
//	        System.out.println("un:"+un);
//	        System.out.println("pwd:"+pwd);
			
			Connection conn = null;
	        String sql;
	        // MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值
	        // 避免中文乱码要指定useUnicode和characterEncoding
	        // 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，
	        // 下面语句之前就要先创建javademo数据库

	        
	        String url = "jdbc:mysql://"+ip+":3306/zzdcdqa?"
	                + "user=root&password=&useUnicode=true&characterEncoding=UTF8";

	        try {
	            // 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，
	            // 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以
	            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
	            // or:
	            // com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
	            // or：
	            // new com.mysql.jdbc.Driver();

	            //System.out.println("成功加载MySQL驱动程序");
	            // 一个Connection代表一个数据库连接
	            conn = DriverManager.getConnection(url);
	            // Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等
	            Statement stmt = conn.createStatement();
	            
	            sql = "SELECT nickname FROM `account` WHERE username=\""+un+"\" AND password=\""+pwd+"\"";
	            
	            ResultSet resultSet = stmt.executeQuery(sql);
	            
	            //System.out.println(resultSet.next());

	            if (resultSet.next() == true ) {
	            	//System.out.println("nickname:"+resultSet.getString("nickname"));
	            	UiAutomatorViewer.getApp().getNewAction().setUsername(resultSet.getString("nickname"));
	            	login = true;
	            	//System.out.println("select success.");
	            	saveDefaultValue();
	            	//IPreferenceStore preferenceStore = new PreferenceStore("..\\preference.pref");
	            } else {
	            	
	            	//System.out.println("select failed.");
	            }
	            
	        } catch (SQLException ee) {
	            System.out.println("MySQL操作错误");
	            
	            //ee.printStackTrace();
	        } catch (Exception ee) {
	            //ee.printStackTrace();
	        } finally {
	            try {
					conn.close();
				} catch (SQLException ee) {
					//ee.printStackTrace();
				}
	        }
	        //close();
		} else if (Login.CANCEL_ID == buttonId) {
			
		}
		
		close();
		
	}
	
	public void saveDefaultValue() {
//		System.out.println("ip:"+serverIP.getText());
//		System.out.println("username:"+username.getText());
//		System.out.println("password:"+password.getText());
		
		preferenceStore.setValue(SERVERIP, serverIP.getText());
		preferenceStore.setValue(USERNAME, username.getText());
		preferenceStore.setValue(PASSWORD, password.getText());
		
		try {
			preferenceStore.save();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	public void loadDefaultValue() {
		
		try {
			preferenceStore.load();
		} catch (IOException e) {
			
			//e.printStackTrace();
		}
		
		//System.out.println("default:"+preferenceStore.getDefaultString(SERVERIP));
		//System.out.println(preferenceStore.getString(SERVERIP));
		
		serverIP.setText(preferenceStore.getString(SERVERIP));
		username.setText(preferenceStore.getString(USERNAME));
		password.setText(preferenceStore.getString(PASSWORD));
		
	}

}
