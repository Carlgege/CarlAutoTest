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

//import com.android.SdkConstants;
import com.android.SdkConstants;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
//import com.carl.actions.CMDUtils;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DebugBridge {
    private static AndroidDebugBridge sDebugBridge;

    private static String getAdbLocation() {
    	
    	// check if adb in system path
        //System.out.println("path:"+System.getenv("path"));
        String [] pathStrs = System.getenv("path").split(";");
        for (int i=0; i<pathStrs.length; i++) {
        	//System.out.println(pathStrs[i]);
        	File adb = new File(pathStrs[i], SdkConstants.FN_ADB);
        	if (adb.exists()) {
        		//System.out.println(adb.getAbsolutePath());
        		return adb.getAbsolutePath();
        		//break;
        	}
        }
        
        File myAdb = new File("..\\Tools\\adb");
		return myAdb.getAbsolutePath();
        
        //if there is no adb command environment, Update system path
        //File adbFolder = new File("E:\\adt-bundle-windows-x86-20130219\\sdk\\tools\\");
        
        
        //String pathOld = System.getenv("path");
        //String pathNew = pathOld+";"+adbFolder.getAbsolutePath();
        //System.out.println("/");
        //System.out.println("\\");
        //System.out.println("pathNew="+pathNew.replaceAll("/", "\\"));
        //System.out.println("pathNew="+pathNew);
        //System.out.println(CMDUtils.cmdCommand(new String[]{"setx path \"%path%;E:\\adt-bundle-windows-x86-20130219\\sdk\\tools\" /M"}));
        
        
        
        
        //update path via the command: wmic ENVIRONMENT where "name='path'" set VariableValue="%path_new%"
        //String result = CMDUtils.cmdCommand(new String[]{"setx path \"%path%;"+adbFolder.getAbsolutePath()+"\" /M"});
        //String []updatePath = new String[]{"wmic","ENVIRONMENT","where","name='path'","set VariableValue=\""+pathNew+"\""};
        /*
        for (int i=0; i<result.length; i++) {
        	System.out.print(result[i]+" ");
        }
        */
        //System.out.println();
        //System.out.println(result);
        //System.out.println(System.getenv("path"));
        /*
        String toolsDir = System.getProperty("com.android.uiautomator.bindir"); //$NON-NLS-1$
        if (toolsDir == null) {
            return null;
        }

        File sdk = new File(toolsDir).getParentFile();

        // check if adb is present in platform-tools
        File platformTools = new File(sdk, "platform-tools");
        File adb = new File(platformTools, SdkConstants.FN_ADB);
        if (adb.exists()) {
            return adb.getAbsolutePath();
        }

        // check if adb is present in the tools directory
        adb = new File(toolsDir, SdkConstants.FN_ADB);
        if (adb.exists()) {
            return adb.getAbsolutePath();
        }

        // check if we're in the Android source tree where adb is in $ANDROID_HOST_OUT/bin/adb
        String androidOut = System.getenv("ANDROID_HOST_OUT");
        if (androidOut != null) {
            String adbLocation = androidOut + File.separator + "bin" + File.separator +
                    SdkConstants.FN_ADB;
            if (new File(adbLocation).exists()) {
                return adbLocation;
            }
        }
        */

        
    }

    public static void init() {
        String adbLocation = getAdbLocation();
        
        //System.out.println("adbLocation:"+adbLocation);
        
        if (adbLocation != null) {
            AndroidDebugBridge.init(false /* debugger support */);
            sDebugBridge = AndroidDebugBridge.createBridge(adbLocation, false);
        }
    }

    public static void terminate() {
        if (sDebugBridge != null) {
            sDebugBridge = null;
            AndroidDebugBridge.terminate();
        }
    }

    public static boolean isInitialized() {
    	//if () {
    		
    	//}
        return sDebugBridge != null;
    }

    public static List<IDevice> getDevices() {
        return Arrays.asList(sDebugBridge.getDevices());
    }
}
