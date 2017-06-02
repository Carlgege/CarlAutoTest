package com.carl.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

//import java.io.Writer;

import com.android.uiautomator.UiAutomatorViewer;

public class FileManager {
	
	private String fileName = null;
	private boolean dirty = false;
	private String content;
	
	public FileManager() {
		
	}
	
	public void load(String name) {
		final String textString;
		try {
			File file = new File(name);
			FileInputStream stream = new FileInputStream(file.getPath());
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			char [] readBuffer = new char[1024];
			StringBuffer buffer = new StringBuffer((int)file.length());
			int n;
			while ((n = in.read(readBuffer))>0) {
				buffer.append(readBuffer, 0, n);
			}
			textString = buffer.toString();
			stream.close();
			in.close();
		} catch (FileNotFoundException e) {
			UiAutomatorViewer.getApp().getStatusLineManager().setMessage(fileName + " can not be found.");
			return;
		} catch (IOException e) {
			UiAutomatorViewer.getApp().getStatusLineManager().setMessage(fileName + " read error");
			return;
		}
		content = textString;
		fileName = name;
	}
	
	public void save(String name) {
		//final String textString = content;
		try {
			//System.out.println("name="+name);
			File file = new File(name);
			if (file.exists()) {
				file.delete();
				//System.out.println("deleted");
			}
			try	{
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			FileOutputStream stream = new FileOutputStream(file.getPath());
			OutputStreamWriter out = new OutputStreamWriter(stream, "UTF-8");
			//Writer out = new OutputStreamWriter(stream);
			//FileOutputStream fos = new FileOutputStream(file);
			
			//System.out.println("Content:"+getContent());
			out.write(getContent());
			out.flush();
			out.close();
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			UiAutomatorViewer.getApp().getStatusLineManager().setMessage(fileName + " can not be found.");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			UiAutomatorViewer.getApp().getStatusLineManager().setMessage(fileName + " save error");
			return;
		}
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}




















