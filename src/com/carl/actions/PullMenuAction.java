package com.carl.actions;

import java.io.File;
import java.io.IOException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Display;

import com.android.uiautomator.UiAutomatorViewer;

public class PullMenuAction {
	
	public static final String DB_CALENDAR = "Calendar";
	public static final String DB_CONTACTS = "Contacts";
	public static final String DB_MESSAGING = "Messaging";
	public static final String DB_SETTINGS = "Settings";
	public static final String DB_MUSIC = "Music";
	
	public static final String DB_Path = "/data/data/";
	public static final String CalendarDB_Name = "com.android.providers.calendar";
	public static final String ContactDB_Name = "com.android.providers.contacts";
	public static final String MessagingDB_Name = "com.android.providers.telephony";
	public static final String SettingsDB_Name = "com.android.providers.settings";
	public static final String MusicDB_Name = "com.android.providers.media";
	
	//private boolean isStopPullMTKLog = false;
	
	public String resultFolderPath = "";
	//public String dbName = null;
	
	//private MenuManager pullMenu;
	public MenuManager PullAction(MenuManager menuManager) {
		
		//this.pullMenu = menuManager;
		MenuManager pullMenu = new MenuManager("Pull(&P)");
		menuManager.add(pullMenu);
		
		//MenuManager pullMTKLog = new MenuManager("AndroidLog");
		pullMenu.add(new PullAndroidLogAction());
		
		//pullMTKLog.add(new PullAllMTKLogAction());
		//pullMTKLog.add(new StopPullMTKLogAction());
		
		//pullMenu.add(new PullAllMTKLogAction());
		pullMenu.add(new PullShellResultAction());
		
		MenuManager database = new MenuManager("Database");
		pullMenu.add(database);
		
		database.add(new PullDatabaseAction(DB_CALENDAR));
		database.add(new PullDatabaseAction(DB_CONTACTS));
		database.add(new PullDatabaseAction(DB_MESSAGING));
		database.add(new PullDatabaseAction(DB_SETTINGS));
		database.add(new PullDatabaseAction(DB_MUSIC));
		
		return pullMenu;
	}
	
	public class PullAndroidLogAction extends Action {
		
		public PullAndroidLogAction() {
			setText("AndroidLog");
		}
		
		@Override
		public void run() {
			File file = new File("..\\Tools\\BAT\\AndroidLog.bat");
			
			Runtime rt = Runtime.getRuntime(); 
			Process ps = null; 
			try {
				ps = rt.exec("cmd.exe /c start "+file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//ps.destroy();
		}
	}
	
	
	/*
	public class PullAllMTKLogAction extends Action {
		
		public PullAllMTKLogAction() {
			setText("Start");
		}
		
		@Override
		public void run() {
			
			
			
			if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
				return;
			}
			String SN = UiAutomatorViewer.getApp().getChoosedDevice().toString();
			
			//System.out.println("find device finished.");
			
			if (!UiAutomatorViewer.getApp().isRoot(false)) {
				MessageDialog.openInformation(null, "Error",
					"Please enable root at first, "
					+ "otherwise dropbox can not pull out.");
				return;
			}
			
			//System.out.println("root juage finished.");
			
			final String logFolderName = new DateUtils().getDate();
			
			final String [][] pullLogCommands = {
				
				//pull out dropbox
				new String[] {"adb","-s", SN, "pull","/data/system/dropbox",
					Login.LOCALRESULTLPATH + "/Result/DUTErrorLog/"+logFolderName+"/dropbox"},

				//pull out anr
				new String[] {"adb","-s", SN, "pull","/data/anr",
						Login.LOCALRESULTLPATH + "/Result/DUTErrorLog/"+logFolderName+"/anr"},	
					
				//pull out mtklog
				new String[] {"adb","-s", SN, "pull","/mnt/sdcard/mtklog",
						Login.LOCALRESULTLPATH + "/Result/DUTErrorLog/"+logFolderName+"/mtklog"},

				//pull out aee
				new String[] {"adb","-s", SN, "pull","/data/aee_exp",
						Login.LOCALRESULTLPATH + "/Result/DUTErrorLog/"+logFolderName+"/aee_exp"},
				
			};
			
			isStopPullMTKLog = false;
			new Thread(
				new Runnable() {
					
					@Override
					public void run() {
						
						Display.getDefault().syncExec(new Runnable() {

							@Override
							public void run() {
								UiAutomatorViewer.getApp().getStatusLineManager().setMessage(
								"Pulling logs at background,"
								+" program will show the folder automatically after pull completed!");
								
							}
							
						});
						
						for (int i=0; i<pullLogCommands.length; i++) {
							if (!isStopPullMTKLog) {
								//System.out.println(Arrays.toString(pullLogCommands[i]));
								CMDUtils.cmdCommandGetInput(pullLogCommands[i]);
							} else {
								break;
							}
							//System.out.println(Arrays.toString(pullLogCommands[i]));
							
						}
						
						openFolder(Login.LOCALRESULTLPATH + "/Result/DUTErrorLog/"+logFolderName);
						
					}
						    
				}).start();
			
		}
	}
	
	public class StopPullMTKLogAction extends Action {
		
		public StopPullMTKLogAction() {
			setText("Stop");
		}
		
		@Override
		public void run() {
			
			isStopPullMTKLog = true;
			CMDUtils.killProcess();
			UiAutomatorViewer.getApp().getStatusLineManager().setMessage("Stop pull logs");
			
		}
	}
	*/
	
	public class PullDatabaseAction extends Action {
		
		public PullDatabaseAction(String dbName) {
			
			if (dbName.equals(DB_CALENDAR)) {
				setText(DB_CALENDAR);
			} else if (dbName.equals(DB_CONTACTS)) {
				setText(DB_CONTACTS);
			} else if (dbName.equals(DB_MESSAGING)) {
				setText(DB_MESSAGING);
			} else if (dbName.equals(DB_SETTINGS)) {
				setText(DB_SETTINGS);
			} else if (dbName.equals(DB_MUSIC)) {
				setText(DB_MUSIC);
			}
		}
		
		@Override
		public void run() {
			
			//System.out.println("getText()="+getText());
			
			UiAutomatorViewer.getApp().getStatusLineManager().setMessage(
					"Pulling db at background,"
					+" program will show the folder automatically after pull completed!");
			
			if (getText().equals(DB_CONTACTS)) {
				
				pullDB(ContactDB_Name);
				
			} else if (getText().equals(DB_MESSAGING)) {
				
				pullDB(MessagingDB_Name);
				
			} else if (getText().equals(DB_SETTINGS)) {
				
				pullDB(SettingsDB_Name);
				
			} else if (getText().equals(DB_MUSIC)) {

				pullDB(MusicDB_Name);
				
			} else if (getText().equals(DB_CALENDAR)) {
				
				pullDB(CalendarDB_Name);
				
			}
		}
	}
	
	public class PullShellResultAction extends Action {
		
		public PullShellResultAction() {
			setText("ShellResult");
		}
		
		@Override
		public void run() {
			
			if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
				return;
			}
			
			String currentDate = new DateUtils().getDate();
			File resultFolder = createFolder(Login.LOCALRESULTLPATH + "/Result/ShellResult/" + currentDate);
			
			new Thread(
					new Runnable() {
						
						@Override
						public void run() {
							
							Display.getDefault().syncExec(new Runnable() {

								@Override
								public void run() {
									UiAutomatorViewer.getApp().getStatusLineManager().setMessage(
									"Pulling ShellResult to: " + resultFolder.getAbsolutePath()
									+" , and program will show the folder automatically after pull completed!");
									
								}
								
							});
							
							CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s",
					        		UiAutomatorViewer.getApp().getChoosedDevice().toString(),
					        		"pull","/mnt/sdcard/ShellResult", resultFolder.getAbsolutePath()});
							
							openFolder(Login.LOCALRESULTLPATH + "/Result/ShellResult/");
							
						}
						
					}).start();
			
		}
	}
	
	public File createFolder(String folderName) {
		
		File file = new File(folderName);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}
	
	public static void openFolder(String folderPath) {
		try {
			java.awt.Desktop.getDesktop().open(new File(folderPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void pullDB(final String DBName) {
		
		new Thread(
				new Runnable() {
					@Override
					public void run() {
						
						CMDUtils.cmdCommandGetInput(new String[]{"adb","-s",
					        UiAutomatorViewer.getApp().getChoosedDevice().toString(),
					        "pull", DB_Path+DBName,createFolder(Login.LOCALRESULTLPATH + "\\Result\\DUTDatabase\\DB_"+new DateUtils().getDate()
							        +"/"+DBName).getAbsolutePath() });
						
						openFolder(Login.LOCALRESULTLPATH + "/Result/DUTDatabase");
						
					}
				}
			).start();
	}
	
}
