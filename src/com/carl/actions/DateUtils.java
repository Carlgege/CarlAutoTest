package com.carl.actions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public String getDate() {
		
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("MM-dd-HH-mm-ss");
		//System.out.println(format.format(date));
		
		return format.format(date);
	}

}
