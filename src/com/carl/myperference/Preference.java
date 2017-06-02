package com.carl.myperference;

import java.io.IOException;

import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Display;

public class Preference {
	
	PreferenceStore preferenceStore;
	
	public static void main(String[] args) {
		Display display = new Display();
		
		PreferenceManager manager = new PreferenceManager();
		PreferenceNode nodeOne = new PreferenceNode("System",
				"System settings", null, MyPreferenceSettings.class.getName());
		manager.addToRoot(nodeOne);
		
		PreferenceNode one = new PreferenceNode("one",
				"first page", null, PageOne.class.getName());
		
		nodeOne.add(one);
		
		PreferenceDialog dlg = new PreferenceDialog(null, manager);
		
		dlg.addPageChangedListener( new IPageChangedListener(){
			
			public void pageChanged(PageChangedEvent event) {
				
				IPreferencePage page = (IPreferencePage)event.getSelectedPage();
				
				System.out.println(page.getTitle());
			}
			
		});
		
		final PreferenceStore preferenceStore = new PreferenceStore("..\\preference.pref");
		
		try {
			preferenceStore.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		preferenceStore.setValue(Constants.SERVERIP, "mysql");
		preferenceStore.setValue(Constants.USERNAME, "Janet");
		preferenceStore.setValue(Constants.PASSWORD, "123");
		
		preferenceStore.addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				
				System.out.println(event.getProperty());
				
				if (event.getProperty().equals(Constants.SERVERIP)) {
					System.out.println("old value:" + event.getOldValue());
					System.out.println("new value:" + event.getNewValue());
					

//					preferenceStore.setValue(Constants.SERVERIP, "mysql");
//					preferenceStore.setValue(Constants.USERNAME, "Janet");
//					preferenceStore.setValue(Constants.PASSWORD, "123");
				}
			}

		});
		
		
		try {
			
			preferenceStore.load();
			
			dlg.setPreferenceStore(preferenceStore);
			dlg.open();
			preferenceStore.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		display.dispose();
	}
	
	
	
	
}















