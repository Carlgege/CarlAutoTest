package com.carl.actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.uiautomator.UiAutomatorHelper;
import com.android.uiautomator.UiAutomatorModel;
import com.android.uiautomator.UiAutomatorViewer;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorException;
import com.android.uiautomator.actions.ImageHelper;
import com.android.uiautomator.tree.BasicTreeNode;
import com.android.uiautomator.tree.RootWindowNode;

public class GetXMLAction extends Action{
	
	//private UiAutomatorViewer mViewer;
	//private IDevice myDevice;
	RawImage rawImage;
	public String command = "";
	
	public void setCommand(String command) {
		this.command = command;
	}

	public GetXMLAction() {
		super("&Get attribution and picture");
		//setText("");
		//setToolTipText("Copy");
		//setAccelerator(SWT.F5);
		//mViewer = viewer;
	}
	
	@Override
    public ImageDescriptor getImageDescriptor() {
        return ImageHelper.loadImageDescriptorFromResource("images/D.png");
    }
	
	@Override
	public void run(){
		
		UiAutomatorViewer.getApp().getmUiAutomatorView().setStopShowLog(true);
		waitMS(800);
		
		//System.out.println("getChoosedDevice:"+UiAutomatorViewer.getApp().getChoosedDevice().toString());
		if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
			return;
		}
		
		//System.out.println("run finished");
		
		getXMLFromDevice(UiAutomatorViewer.getApp().getChoosedDevice());
		
		/*
		if (UiAutomatorViewer.getApp().isRoot(true)) {
			getXMLFromDevice(UiAutomatorViewer.getApp().getChoosedDevice());
		} else {
			return;
		}
		*/
		UiAutomatorViewer.getApp().getmUiAutomatorView().setStopShowLog(false);
	}
	
	public void getXMLFromDevice(final IDevice myDevice) {

		ProgressMonitorDialog dialog = new ProgressMonitorDialog(UiAutomatorViewer.getApp().getShell());
        try {
            dialog.run(true, false, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException,
                                                                        InterruptedException {
                	
                    ///***********************comment by carl
					//System.out.println("device:"+device.startScreenRecorder(arg0, arg1, arg2););
					///***********************comment by carl
					
					//push jar file
					//CMDUtils.cmdCommand(new String[]{"adb", "-s", myDevice.toString(), "push" ,
                	//"..\\UiAutomator\\Jar\\CommonAction.jar", "/data/local/tmp"});
					
					//run the jar file to get dynamic xml
					//CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", myDevice.toString(), "shell" ,
						//"\"uiautomator runtest /data/local/tmp/CommonAction.jar -c com.fih.liguo.GetAttributionXML\""});
					
                	if (!command.equals("")) {
                		monitor.subTask("Performing action on test device...");
                		singleCommandRun();
                	}
                	
                	monitor.subTask("Get attribution...");
					CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", myDevice.toString(), "shell" ,
					"\"am instrument -w -e class com.carl.cat.common.GetAttrXML com.carl.cat.test/android.support.test.runner.AndroidJUnitRunner\""});
					
					//pull the xml file to PC
					CMDUtils.cmdCommandGetInput(new String[]{"adb", "-s", myDevice.toString(), "pull",
							"/mnt/sdcard/ShellResult/Temp/dump", "..\\UiAutomator\\XML\\dump"});
					
					
					File xml = new File("..\\UiAutomator\\XML\\dump");
					
					////////*********************************************************************************************
					BufferedReader br = null;
			        BufferedWriter bw = null;
			        
			        try {
						br = new BufferedReader(new FileReader(xml.getAbsoluteFile()));
						
						String line;
			            
			            String updated = "";
			            while ((line = br.readLine()) != null) {
			                //System.out.println(line);

			                if (line.startsWith("</hierarchy>")) {
			                	updated = updated + "</hierarchy>";
			                	break;
			                } else {
			                	updated = updated + line + "\n";
			                }

			                //bw.write(line+"\n");
			            }
			            
			            //System.out.println();
			            //System.out.println("updated:");
			            //System.out.println(updated);
			            br.close();
			            
			            bw = new BufferedWriter(new FileWriter(xml.getAbsoluteFile()));
			            bw.write(updated);
			            bw.flush();
	                    
					} catch (IOException e1) {
						e1.printStackTrace();
					} finally {
						try {
							if (bw != null) {
								bw.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
		            
			        
		            /*
			        try {
			            
			        } catch (Exception e) {
			            return;
			        } finally {
			            try {
			                if(br != null)
			                    br.close();
			            } catch (IOException e) {
			                e.printStackTrace();
			            }
			            try {
			                if(bw != null)
			                    bw.flush();
			                    bw.close();
			            } catch (IOException e) {
			                e.printStackTrace();
			            }
			        }
			        */
			        
					////////*********************************************************************************************
					
					//System.out.println("xml file path:"+xml.getAbsolutePath());
					UiAutomatorModel model;
					try {
					    model = new UiAutomatorModel(xml);
					} catch (Exception e) {
					    // FIXME: show error
						e.printStackTrace();
					    return;
					}
					
					//myDevice.getScreenshot()
					//RawImage rawImage = null;
					monitor.subTask("Get picture...");
					//**************************copy from uiautomatorHelper
					try {
						rawImage = myDevice.getScreenshot();
					} catch (Exception e) {
						String msg = "Error taking device screenshot: " + e.getMessage();
						try {
							throw new UiAutomatorHelper.UiAutomatorException(msg, e);
						} catch (UiAutomatorException e1) {
							
							e1.printStackTrace();
						}
					}
					
					// rotate the screen shot per device rotation
			        BasicTreeNode root = model.getXmlRootNode();
			        //System.out.println("rotation:"+((RootWindowNode)root).getRotation());
			        if (root instanceof RootWindowNode) {
			        	
			            for (int i = 0; i < ((RootWindowNode)root).getRotation(); i++) {
			            	
			                rawImage = rawImage.getRotated();
			            }
			            //System.out.println("prop build version:"+myDevice.getProperty("ro.build.version.sdk"));
			            
			            if (Integer.valueOf(myDevice.getProperty("ro.build.version.sdk")) > 22) {
			            	if (((RootWindowNode)root).getRotation() % 2 == 1) {
				            	rawImage = rawImage.getRotated();
				            }
			            }
			            
			            
			        }
			        
					//**************************************************************
					
					PaletteData palette = new PaletteData(
			                rawImage.getRedMask(),
			                rawImage.getGreenMask(),
			                rawImage.getBlueMask());
			        ImageData imageData = new ImageData(rawImage.width, rawImage.height,
			                rawImage.bpp, palette, 1, rawImage.data);
			        //ImageLoader loader = new ImageLoader();
			        //loader.data = new ImageData[] { imageData };
			        //loader.save(screenshotFile.getAbsolutePath(), SWT.IMAGE_PNG);
			        Image screenshot = new Image(Display.getDefault(), imageData);
			        
					/*
					Image img = null;
					File screenshot = new File("..\\Image\\Temp.png");
					if (screenshot != null) {
					    try {
					        ImageData[] data = new ImageLoader().load(screenshot.getAbsolutePath());
							
					        // "data" is an array, probably used to handle images that has multiple frames
					        // i.e. gifs or icons, we just care if it has at least one here
					        if (data.length < 1) {
					            throw new RuntimeException("Unable to load image: "
					                    + screenshot.getAbsolutePath());
					        }
					        
					        img = new Image(Display.getDefault(), data[0]);
					        
					    } catch (Exception e) {
					        // FIXME: show error
					        return;
					    }
					}
					*/
					
					UiAutomatorViewer.getApp().setModel(model, xml, screenshot);

                    monitor.done();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void waitMS(int ms) {
    	try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
	
	public void singleCommandRun() {
    	
    	DebugAction.debug(command);
    	waitMS(1000);
    	for (int i=0; i<8; i++) {
    		if (!( new StopAction().getDebugScriptProcessId(
    				UiAutomatorViewer.getApp().getChoosedDevice().toString()).equals("-1") )) {
    			waitMS(1000);
    		}
    	}
    	command = "";
    }
    
}
