package com.carl.table;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import com.carl.table.ScriptListTable;

public class MyLabelProvider implements ITableLabelProvider{

	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		
		ScriptEO script = (ScriptEO)element;
		if (columnIndex == ScriptListTable.ID) {
			return script.getID()+"";
		} else if (columnIndex == ScriptListTable.NAME) {
			return script.getName()+"";
		} else if (columnIndex == ScriptListTable.FILEPATH) {
			return script.getFilepath()+"";
		} else if (columnIndex == ScriptListTable.REPEAT) {
			return script.getRepeat()+"";
		}
		return null;
		
	}

}
