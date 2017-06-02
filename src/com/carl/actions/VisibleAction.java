package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;

import com.android.uiautomator.UiAutomatorViewer;

public class VisibleAction {
	
	public static final String TREE = "AttrTree";
	public static final String TABLE = "AttrTable";
	
	public MenuManager VisibleMenuAction(MenuManager menuManager) {
		
		//this.pullMenu = menuManager;
		MenuManager visible = new MenuManager("Visible");
		menuManager.add(visible);
		
		//MenuManager pullMTKLog = new MenuManager("AndroidLog");
		visible.add(new TreeAction());
		visible.add(new TableAction());
		
		return visible;
	}
	
	
	public class TreeAction extends Action {
		public TreeAction() {
			setText(TREE);
		}
		
		@Override
		public void run() {
			UiAutomatorViewer.getApp().getmUiAutomatorView().switchTreeVisible();
		}
	}
	
	public class TableAction extends Action {
		public TableAction() {
			setText(TABLE);
		}
		
		@Override
		public void run() {
			UiAutomatorViewer.getApp().getmUiAutomatorView().switchTableVisible();
		}
	}
	
	
}
