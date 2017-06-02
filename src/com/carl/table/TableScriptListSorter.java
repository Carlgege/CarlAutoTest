package com.carl.table;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class TableScriptListSorter extends ViewerSorter {
	
	private static final int ASCENDING = 0;
	private static final int DESCENDING = 1;
	
	private int order;
	private int column;
	
	public void doSort(int columnID) {
		if (columnID == this.column) {
			order = 1 - order;
		} else {
			this.column = columnID;
			order = ASCENDING;
		}
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		
		int result = 0;
		
		ScriptEO s1 = (ScriptEO) e1;
		ScriptEO s2 = (ScriptEO) e2;
		
		//System.out.println("column="+column);
		switch(column) {
		case ScriptListTable.ID:
			//System.out.println("ID1="+s1.getID());
			//System.out.println("ID2="+s2.getID());
			
			result = s1.getID() > s2.getID() ? 1 : -1;
			break;
		case ScriptListTable.NAME:
			result = collator.compare(s1.getName(), s2.getName());
			break;
		case ScriptListTable.FILEPATH:
			result = collator.compare(s1.getFilepath(), s2.getFilepath());
			break;
		case ScriptListTable.REPEAT:
			result = collator.compare(s1.getRepeat(), s2.getRepeat());
			
		} 
		
		if (order == DESCENDING) {
			result = -result;
		}
		return result;
		
	}

}
