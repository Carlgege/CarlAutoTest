package com.carl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.FontDialog;
import com.android.uiautomator.UiAutomatorViewer;

public class FormatAction extends Action{
	
	public static final String TYPE_FORECOLOR = "FORECOLOR";
	public static final String TYPE_BGCOLOR = "BGCOLOR";
	public static final String TYPE_FONT = "FONT";
	
	private String formatType;
	
	private Font font;
	private Color foregroundColor;
	private Color bgColor;
	private ColorDialog colorDialog;
	
	public FormatAction(String type) {
		super();
		this.formatType = type;
		initAction();
	}
	
	private void initAction() {
		if (formatType.equals(TYPE_FONT)) {
			this.setText("Set font");
			
			//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\font.gif"));
		} else if (formatType.equals(TYPE_FORECOLOR)) {
			this.setText("Set forecolor");
			
			//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\forecolor.gif"));
		} else {
			this.setText("Set backgroudcolor");
			
			//setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class, "icons\\bgColor.gif"));
		}
	}
	
	@Override
	public void run() {
		
		StyledText content = UiAutomatorViewer.getApp().getmUiAutomatorView().getStyledText();
		if (content == null) {
			return;
		}
		
		if (formatType.equals(TYPE_FONT)) {
			FontDialog fontDialog = new FontDialog(UiAutomatorViewer.getApp().getShell());
			fontDialog.setFontList(content.getFont().getFontData());
			FontData fontData = fontDialog.open();
			if (fontData != null) {
				font = new Font(UiAutomatorViewer.getApp().getShell().getDisplay(), fontData);
				content.setFont(font);
				//font.dispose();
			}
		} else if (formatType.equals(TYPE_FORECOLOR)) {
			
			colorDialog = new ColorDialog(UiAutomatorViewer.getApp().getShell());
			colorDialog.setRGB(content.getForeground().getRGB());
			RGB rgb = colorDialog.open();
			if (rgb != null) {
				foregroundColor = new Color(UiAutomatorViewer.getApp().getShell().getDisplay(), rgb);
				content.setForeground(foregroundColor);
				//foregroundColor.dispose();
			}
		} else {
			colorDialog = new ColorDialog(UiAutomatorViewer.getApp().getShell());
			colorDialog.setRGB(content.getBackground().getRGB());
			RGB rgb = colorDialog.open();
			if (rgb != null) {
				bgColor = new Color(UiAutomatorViewer.getApp().getShell().getDisplay(), rgb);
				content.setBackground(bgColor);
				//bgColor.dispose();
			}
			
		}
	}

}

















