package com.carl.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CMDUtils {
	
	private static Process p = null;
	
	public static String cmdCommandGetInput(String [] command) {
		
		//p = null;
		String cmdPerformResult = "";
	  	String cmdReadline = "";
	  	
		try {
  			p = Runtime.getRuntime().exec(command);
  			p.waitFor();
  			
  			BufferedReader commandResult = new BufferedReader(new InputStreamReader(p.getInputStream()));
  	  		
  			//System.out.println("inputstream:"+commandResult.readLine().length());
  			
  	  		try	{
  	  			while ((cmdReadline = commandResult.readLine()) != null) {
  	  				
  	  				cmdPerformResult = cmdPerformResult + cmdReadline + "\n";
  	  			}
  	  		} catch (IOException e1) {
  	  			e1.printStackTrace();
  	  		}
  	  		
  		} catch (IOException|InterruptedException ie) {
			ie.printStackTrace();
  		}
  		
		//System.out.println("cmdPerformResult:"+cmdPerformResult);
  		return cmdPerformResult;
		
	}
	
	public static String cmdCommandGetError(String [] command) {
		
		//Process p = null;
		String cmdPerformResult = "";
	  	String cmdReadline = "";
	  	
		try {
  			p = Runtime.getRuntime().exec(command);
  			p.waitFor();
  			
  			BufferedReader commandResult = new BufferedReader(new InputStreamReader(p.getErrorStream()));
  	  		
  			//System.out.println("inputstream:"+commandResult.readLine().length());
  			
  	  		try	{
  	  			while ((cmdReadline = commandResult.readLine()) != null) {
  	  				
  	  				cmdPerformResult = cmdPerformResult + cmdReadline;
  	  				//cmdPerformResult = cmdPerformResult + cmdReadline + "\n";
  	  			}
  	  		} catch (IOException e1) {
  	  			e1.printStackTrace();
  	  		}
  	  		
  	  		
  		} catch (IOException|InterruptedException ie) {
			ie.printStackTrace();
  		}
  		
  		return cmdPerformResult;
		
	}
	
	public static void killProcess() {
		
		p.destroy();
		//System.out.println("kill Process runned");
	}
	
}
