package com.carl.actions;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.StyledText;

import com.android.uiautomator.UiAutomatorViewer;

public class RedoUndoManager {
	
	private List<String> undoStack = new LinkedList<String>();
    
	private List<String> redoStack = new LinkedList<String>();
    
    private List<String> undoIndex = new LinkedList<String>();
    
	private List<String> redoIndex = new LinkedList<String>();
    
    //private static final int MAX_STACK_SIZE = 200;
    private StyledText styledText;
    
    public StyledText getStyledText() {
		return styledText;
	}
    
    public List<String> getUndoIndex() {
		return undoIndex;
	}

	public List<String> getUndoStack() {
		return undoStack;
	}

    
    public RedoUndoManager(StyledText styledText) {
    	this.styledText = styledText;
    }
    
    public void undoCounter(ExtendedModifyEvent event) {
    	if (event.length > 0) {
    		String currText = styledText.getText(0, styledText.getCaretOffset()-1);
        	
    		//System.out.println("currText:\n"+currText);
        	System.out.println("start:"+event.start+" ; "+"end:"+(event.start + event.length));
        	
    		String newText = currText.substring(event.start, event.start + event.length);
    		if (newText != null && newText.length() > 0) {
//    			if (undoStack.size() == MAX_STACK_SIZE) {
//    				undoStack.remove(undoStack.size() - 1);
//    			}
    			undoStack.add(0, newText);
    			undoIndex.add(0, String.valueOf(styledText.getCaretOffset()));
    			
    			System.out.println("newText:"+newText);
    			System.out.println("edit location:"+String.valueOf(styledText.getCaretOffset()));
    		}
    	}
    	
    	
    }
	
	
	public void undo() {
		
    	if (undoStack.size() > 0) {
    		
    		String lastEdit = undoStack.remove(0);
    		int editLength = lastEdit.length();
    		int index = Integer.valueOf(undoIndex.remove(0));
    		
    		//String currText = styledText.getText();
    		String currText = styledText.getText(0, index-1);
    		int startReplaceIndex = currText.length() - editLength;
    		
    		System.out.println("startReplaceIndex:"+startReplaceIndex);
    		System.out.println("lastEdit:"+lastEdit);
    		System.out.println("editLength:"+editLength);
    		
    		styledText.replaceTextRange(startReplaceIndex, editLength, "");
    		styledText.setSelection(index-editLength, index-editLength);
    		
    		redoStack.add(0, lastEdit);
    		redoIndex.add(0, String.valueOf(index));
    	}
    }
	
	public void redo() {
    	if (redoStack.size() > 0) {
    		String lastEdit = redoStack.remove(0);
    		int editLength = lastEdit.length();
    		int index = Integer.valueOf(redoIndex.remove(0));
    		
    		String currText = styledText.getText(0, index-1);
    		int startReplaceIndex = currText.length() - editLength;
    		
    		//System.out.println("currText:\n"+currText);
    		System.out.println("startReplaceIndex:"+startReplaceIndex);
    		System.out.println("redo text:"+lastEdit);
    		System.out.println("redo index:"+index);
    		
    		
    		//styledText.replaceTextRange(startReplaceIndex, editLength, lastEdit);
    		styledText.insert(lastEdit);
    		styledText.setSelection(index, index);
    		
    		//undoStack.add(0, lastEdit);
			//undoIndex.add(0, String.valueOf(styledText.getCaretOffset()));
    		
    		
    		//UiAutomatorViewer.getApp().getmUiAutomatorView().moveCursorToEnd();
//    		styledText.setSelection(index,	index);
//    		//styledText.append(text);
//    		
//    		styledText.setSelection(index+text.length(), index+text.length());
    	}
    }

}
