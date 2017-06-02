package com.carl.myperference;

import java.io.IOException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MyPreferenceSettings extends PreferencePage{

	private Text serverIP;
	private Text username;
	private Text password;
	
	@Override
	protected Control createContents(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		//IPreferenceStore preferenceStore = getPreferenceStore();
		
		new Label(composite, SWT.LEFT).setText(Constants.SERVERIP);
		serverIP = new Text(composite, SWT.BORDER);
		serverIP.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		serverIP.setText("value in file of userName");
		
		new Label(composite, SWT.LEFT).setText(Constants.USERNAME);
		username = new Text(composite, SWT.BORDER);
		username.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		username.setText("value in file of userName");
		
		new Label(composite, SWT.LEFT).setText(Constants.PASSWORD);
		password = new Text(composite, SWT.BORDER);
		password.setEchoChar('*');
		password.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		password.setText("value in file of pwd");
		
		return composite;
	}
	
	@Override
	protected void performDefaults() {
		//IPreferenceStore preferenceStore = getPreferenceStore();
		username.setText("Default userName");
		password.setText("Default pwd");
	}
	
	public boolean performOK() {
		IPreferenceStore preferenceStore = new PreferenceStore("..\\preference.pref");
		if (username != null) {
			preferenceStore.setValue("set user", username.getText());
		}
		if (password != null) {
			preferenceStore.setValue("set pwd", password.getText());
		}
		
		
		try {
			((PreferenceStore) preferenceStore).save();
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
		
		
		
		
		return true;
	}
	
	/*
	protected void contributeButtons(Composite parent) {
		Button bt1 = new Button(parent, SWT.NONE);
		bt1.setText("");
		
		((GridLayout)parent.getLayout()).numColumns++;
		
		Button bt2 = new Button(parent, SWT.NONE);
		bt2.setText("");
		
		((GridLayout)parent.getLayout()).numColumns++;
		
	}
	*/

}












