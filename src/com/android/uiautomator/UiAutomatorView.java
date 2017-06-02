/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.uiautomator;

import com.android.uiautomator.actions.ExpandAllAction;
import com.android.uiautomator.actions.ImageHelper;
//import com.android.uiautomator.actions.ScreenshotAction;
import com.android.uiautomator.actions.ToggleNafAction;
import com.android.uiautomator.tree.AttributePair;
import com.android.uiautomator.tree.BasicTreeNode;
import com.android.uiautomator.tree.BasicTreeNodeContentProvider;
//import com.android.uiautomator.tree.UiHierarchyXmlLoader;
import com.android.uiautomator.tree.UiNode;
import com.carl.actions.CMDUtils;
//import com.carl.actions.DebugAction;
import com.carl.actions.InstallAction;
//import com.carl.actions.RedoUndoManager;
import com.carl.actions.RunAction;
import com.carl.actions.RunBDAction;
//import com.carl.actions.RunMultiAction;
import com.carl.actions.StopAction;
import com.carl.table.ScriptListTable;

import org.eclipse.core.runtime.IProgressMonitor;
//import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
//import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
//import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;


import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

public class UiAutomatorView extends Composite {
	
    private static final int IMG_BORDER = 2;
    
	public static final String logFolderPath = "/mnt/sdcard/ShellResult/Log/";
	public static final String scriptFolderPath = "/mnt/sdcard/ShellResult/ScriptFolder/";
	public static final String runDetailLogPath = "/mnt/sdcard/ShellResult/Log/RunDetail.txt";
	public static final String resultLogPath = "/mnt/sdcard/ShellResult/Log/Result.txt";
	public String sendOption = "";
	
    // The screenshot area is made of a stack layout of two components: screenshot canvas and
    // a "specify screenshot" button. If a screenshot is already available, then that is displayed
    // on the canvas. If it is not availble, then the "specify screenshot" button is displayed.
    private Composite mScreenshotComposite;
    
    private StackLayout mStackLayout;
    
    public Group grpNodeDetail;
    public Composite tableContainer;
    
    private Composite mSetScreenshotComposite;
    private Canvas mScreenshotCanvas;
    
    private TreeViewer mTreeViewer;
    private Composite upperRightBase;
    private Composite middleRightBase;
    private TableViewer mTableViewer;

	private float mScale = 1.0f;
    private int mDx, mDy;

    private UiAutomatorModel mModel;
    private File mModelFile;
    private Image mScreenshot;

    private List<BasicTreeNode> mSearchResult;
    private int mSearchResultIndex;
    private ToolItem itemDeleteAndInfo;
    private Text searchTextarea;
    private Cursor mOrginialCursor;
    private ToolItem itemPrev, itemNext;
    private ToolItem coordinateLabel;

    private String mLastSearchedTerm;
    private Cursor mCrossCursor;
    
    private CTabFolder scriptFolder;
    private CTabItem cTabItem;
    
    
    private SourceViewer sourceViewer;
    
    StringBuffer stringBuffer = new StringBuffer("");
    //declare one list to store multi sourceViewer
    private List<SourceViewer> sourceViewerList = new ArrayList<SourceViewer>();
    
    private int mSourceViewerIndex = 0;
    
    private List<String> cTabItems = new ArrayList<String>();
    
    //private List<String> undoStack;
    //private List<String> redoStack;
    //private static final int MAX_STACK_SIZE = 200;
    
    private String x_coor;
    private String y_coor;
    
    private File temp;
	private ArrayList<String> scriptFileName = new ArrayList<String>();
	private ArrayList<String> scriptFilePath = new ArrayList<String>();
    
	private boolean stopShowLog = false;
	//private boolean showLogWorking = false;
	private int refreshStartLine;
	private int refreshStopLine;
	private String tempLine = "";
	private CTabFolder logTabFolder;
	private StyledText resultStyledText;
	private StyledText detailStyledText;
	
	private String dragFrom;
	private ArrayList<String> wirelessDeviceList = new ArrayList<String>();
	
	public static boolean wirelessRun = false;
	
	//public GetXMLAction getXMLAction = new GetXMLAction();
    //private IUndoManager undoManager;
	
    ///***********************************create by carl********************
    public void newCTabItem(String text) {
    	cTabItem = new CTabItem(scriptFolder, SWT.NONE);
    	cTabItem.setText(text);
    	cTabItem.setToolTipText(cTabItem.getText());
    	
    	updateCTabItems();
    	
    	scriptFolder.setSelection(cTabItem);
    	    	
    	sourceViewer = new SourceViewer(scriptFolder, new VerticalRuler(10), SWT.V_SCROLL|SWT.H_SCROLL);
    	
    	cTabItem.setControl(sourceViewer.getControl());
    	sourceViewer.getControl().setFocus();
    	
    	//styledText = new StyledText(scriptFolder, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL|SWT.CANCEL);
    	
    	//styledText.setFont(new Font(getDisplay(), "微軟正黑體", 12, SWT.NORMAL));
    	//font.dispose();
    	
    	//cTabItem.setControl(styledText);    	
    	//styledText.setFocus();
    	    	
    	sourceViewerList.add(sourceViewer);
    	
    	
    	//mStyledTextIndex++;
    	
    	sourceViewer.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent arg0) {
				//UiAutomatorViewer.getApp().getManager().setDirty(true);
				
				if (!getSeletedCTabItem().getText().contains("*")) {
					getSeletedCTabItem().setText("*"+getSeletedCTabItem().getText());
				}
				updateCTabItems();
			}
		});
    	
    	
    	
    	
    	cTabItem.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				
				//System.out.println("before dispose item count:"+cTabItems.size());
				
				for (int i=0; i<cTabItems.size(); i++) {
					
					//System.out.println("i="+i);
					String selectItem = cTabItems.get(i).replaceFirst("\\*", "").replaceFirst(".sh", "");
					String arg = arg0.getSource().toString().replaceFirst("\\*", "").replaceFirst(".sh", "");
					
					//System.out.println("cTabItems:"+cTabItems.get(i));
					//System.out.println("source to string:"+arg0.getSource().toString());
					
					if (selectItem.equals(arg)) {
						mSourceViewerIndex = i;
						//System.out.println("mStyledTextIndex="+mStyledTextIndex);
						
						break;
					}
				}
				//System.out.println("mStyledTextIndex:"+mStyledTextIndex);
				//styledTextList.get(mStyledTextIndex).getFont().dispose();
				sourceViewerList.get(mSourceViewerIndex).getControl().dispose();
				sourceViewerList.remove(mSourceViewerIndex);
				
				//cTabItems.get(mStyledTextIndex)
				cTabItems.remove(mSourceViewerIndex);
				//System.out.println("after dispose item count:"+cTabItems.size());
				//System.out.println("arg0:"+arg0.toString());
				
				//CTabItem [] items = scriptFolder.getItems();
				//for (int i=0; i<items.length; i++) {
				//	System.out.println("items:"+items[i]);
				//}
				//(styledTextList.get(0)).getText();
				//styledText.dispose();
			}
    	});
    	
    	
    	//undoManager = new DefaultUndoManager(100);
    	//undoManager.connect( (ITextViewer) styledText);
    	//font.dispose();
    }
    
    ///***********************************create by carl********************
    
    ///this function copied from http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/CatalogSWT-JFace-Eclipse.htm
    /*
    public void undo() {
    	if (undoStack.size() > 0) {
    		
    		String lastEdit = undoStack.remove(0);
    		int editLength = lastEdit.length();
    		String currText = getSelectedStyledText().getText();
    		int startReplaceIndex = currText.length() - editLength;
    		getSelectedStyledText().replaceTextRange(startReplaceIndex, editLength, "");
    		redoStack.add(0, lastEdit);
    	}
    }
    */
    
    ///this function copied from http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/CatalogSWT-JFace-Eclipse.htm
    /*
    public void redo() {
    	if (redoStack.size() > 0) {
    		String text = redoStack.remove(0);
    		moveCursorToEnd();
    		getSelectedStyledText().append(text);
    		moveCursorToEnd();
    	}
    }
    */
    
    
    ///this function copied from http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/CatalogSWT-JFace-Eclipse.htm
    public void moveCursorToEnd() {
    	getSelectedSourceViewer().setCaretOffset(getSelectedSourceViewer().getText().length());
    }
    
    public void updateCTabItems() {
    	
    	CTabItem [] items = scriptFolder.getItems();
    	cTabItems.clear();
    	//System.out.println("item length:"+items.length);
    	for (int i=0; i<items.length; i++) {
    		//System.out.println("scriptFolder subitem:"+items[i].getText());
    		//System.out.println("item to string:"+items[i].toString());
    		//if (items[i].getData().toString().equals(cTabItem.getData().toString())) {
    		cTabItems.add(items[i].toString());
    		//}
    	}
    }
    
    public TableViewer getmTableViewer() {
		return mTableViewer;
	}
    
    public Composite getTableContainer() {
    	return tableContainer;
    }
    
    public CTabItem getcTabItem() {
		return cTabItem;
	}
    
    public CTabItem getSeletedCTabItem() {
    	return scriptFolder.getSelection();
    }

	public void setcTabItem(CTabItem cTabItem) {
		this.cTabItem = cTabItem;
	}
	    
    public StyledText getStyledText() {
		return styledText;
	}
    
    public SourceViewer getSelectedSourceViewer() {
    	//System.out.println("scriptFolder.getSelectionIndex():"+scriptFolder.getSelectionIndex());
    	if (scriptFolder.getSelectionIndex() > -1) {
    		
    		return sourceViewerList.get(scriptFolder.getSelectionIndex());
    	} else {
    		return null;
    	}
    }
    
    public SourceViewer getSelectedLogItem() {
    	
    	if (logTabFolder.getSelectionIndex() == 0) {
    		return resultStyledText;
    	} else if (logTabFolder.getSelectionIndex() == 1) {
    		return detailStyledText;
    	}
    	
    	return null;
    }
    
	public void setStyledText(StyledText styledText) {
		
		this.styledText = styledText;
	}

	public CTabFolder getScriptFolder() {
		return scriptFolder;
	}
    
	public UiAutomatorView(Composite parent, int style) {
        super(parent, SWT.NONE);
        setLayout(new FillLayout());
        
        ///*************************added by carl
        //undoStack = new LinkedList<String>();
        //redoStack = new LinkedList<String>();
        ///********************************************
        
        SashForm baseSash = new SashForm(this, SWT.HORIZONTAL);
        mOrginialCursor = getShell().getCursor();
        mCrossCursor = new Cursor(getDisplay(), SWT.CURSOR_CROSS);
        mScreenshotComposite = new Composite(baseSash, SWT.BORDER);
        mStackLayout = new StackLayout();
        mScreenshotComposite.setLayout(mStackLayout);
        // draw the canvas with border, so the divider area for sash form can be highlighted
        mScreenshotCanvas = new Canvas(mScreenshotComposite, SWT.BORDER);
        mStackLayout.topControl = mScreenshotCanvas;
        mScreenshotComposite.layout();
        
        // set cursor when enter canvas
        mScreenshotCanvas.addListener(SWT.MouseEnter, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                getShell().setCursor(mCrossCursor);
            }
        });
        mScreenshotCanvas.addListener(SWT.MouseExit, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                getShell().setCursor(mOrginialCursor);
            }
        });

        mScreenshotCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                if (mModel != null) {
                    mModel.toggleExploreMode();
                    redrawScreenshot();
                }
                
                ///**********************************Create by carl
                
                if (e.button == 3) {
                	
                	x_coor = String.valueOf(getInverseScaledSize(e.x - mDx));
                	y_coor = String.valueOf(getInverseScaledSize(e.y - mDy));
                	
                	//setup right option from mScreenshotCanvas
                	Menu menu = new Menu(mScreenshotCanvas);
                	mScreenshotCanvas.setMenu(menu);
                	
                	MenuItem screenOperation = new MenuItem(menu, SWT.CASCADE);
                	screenOperation.setText("Screen operation");
                	
                	Menu screenOperationMenu = new Menu(screenOperation);
                	screenOperation.setMenu(screenOperationMenu);
                	
                	MenuItem click = new MenuItem(screenOperationMenu, SWT.CASCADE);
                	click.setText("Click");
                	
                	click.addSelectionListener(new SelectionListener() {

            			@Override
            			public void widgetDefaultSelected(SelectionEvent arg0) {}
            			
            			@Override
            			public void widgetSelected(SelectionEvent arg0) {
            				
            				insertTextToStyledText("Click "+x_coor+" "+y_coor+"\r\n");
            				//singleCommandRun("Click "+x_coor+" "+y_coor);
            				UiAutomatorViewer.getApp().getXMLAction.setCommand("Click "+x_coor+" "+y_coor);
        					//update screen and attribution table
        					UiAutomatorViewer.getApp().getXMLAction.run();
            			}
                	});
                	
                	MenuItem relativeClick = new MenuItem(screenOperationMenu, SWT.CASCADE);
                	relativeClick.setText("RelativeClick");
                	
                	relativeClick.addSelectionListener(new SelectionListener() {

            			@Override
            			public void widgetDefaultSelected(SelectionEvent arg0) {}

            			@Override
            			public void widgetSelected(SelectionEvent arg0) {
            				
            				//System.out.println(mScreenshot.getBounds().width);
            				//System.out.println(mScreenshot.getBounds().height);
            				
            				insertTextToStyledText("RelativeClick "+x_coor+"/"+mScreenshot.getBounds().width
            						+" "+y_coor+"/"+mScreenshot.getBounds().height+"\r\n");
            				//singleCommandRun("RelativeClick "+x_coor+" "+y_coor);
            				UiAutomatorViewer.getApp().getXMLAction.setCommand("RelativeClick "+x_coor+" "+y_coor);
        					//update screen and attribution table
        					UiAutomatorViewer.getApp().getXMLAction.run();
            			}
                	});
                	
                	MenuItem doubleClick = new MenuItem(screenOperationMenu, SWT.CASCADE);
                	doubleClick.setText("DoubleClick");
                	
                	doubleClick.addSelectionListener(new SelectionListener() {
                		
            			@Override
            			public void widgetDefaultSelected(SelectionEvent arg0) {}

            			@Override
            			public void widgetSelected(SelectionEvent arg0) {
            				insertTextToStyledText("DoubleClick "+x_coor+" "+y_coor+"\r\n");
            				//singleCommandRun("DoubleClick "+x_coor+" "+y_coor);
            				UiAutomatorViewer.getApp().getXMLAction.setCommand("DoubleClick "+x_coor+" "+y_coor);
        					//update screen and attribution table
        					UiAutomatorViewer.getApp().getXMLAction.run();
            			}
                	});
                	
                	MenuItem relativeDoubleClick = new MenuItem(screenOperationMenu, SWT.CASCADE);
                	relativeDoubleClick.setText("RelativeDoubleClick");
                	relativeDoubleClick.addSelectionListener(new SelectionListener() {
                		
                		@Override
            			public void widgetDefaultSelected(SelectionEvent arg0) {}
                		
            			@Override
            			public void widgetSelected(SelectionEvent arg0) {
            				
            				insertTextToStyledText("RelativeDoubleClick "+x_coor+"/"
            				+mScreenshot.getBounds().width+" "+y_coor+"/"+mScreenshot.getBounds().height+"\r\n");
            				//singleCommandRun("RelativeDoubleClick "+x_coor+" "+y_coor);
            				UiAutomatorViewer.getApp().getXMLAction.setCommand("RelativeDoubleClick "+x_coor+" "+y_coor);
        					//update screen and attribution table
        					UiAutomatorViewer.getApp().getXMLAction.run();
            			}
                	});

                	MenuItem longPress = new MenuItem(screenOperationMenu, SWT.CASCADE);
                	longPress.setText("LongPress");
                	longPress.addSelectionListener(new SelectionListener() {
                		
            			@Override
            			public void widgetDefaultSelected(SelectionEvent arg0) {}

            			@Override
            			public void widgetSelected(SelectionEvent arg0) {
            				insertTextToStyledText("LongPress "+x_coor+" "+y_coor+"\r\n");
            				//singleCommandRun("LongPress "+x_coor+" "+y_coor);
            				UiAutomatorViewer.getApp().getXMLAction.setCommand("LongPress "+x_coor+" "+y_coor);
        					//update screen and attribution table
        					UiAutomatorViewer.getApp().getXMLAction.run();
            			}
                	});
                	
                	MenuItem relativeLongPress = new MenuItem(screenOperationMenu, SWT.CASCADE);
                	relativeLongPress.setText("RelativeLongPress");
                	relativeLongPress.addSelectionListener(new SelectionListener() {
                		
                		@Override
            			public void widgetDefaultSelected(SelectionEvent arg0) {}
                		
            			@Override
            			public void widgetSelected(SelectionEvent arg0) {
            				
            				insertTextToStyledText("RelativeLongPress "+x_coor+"/"
            				+mScreenshot.getBounds().width+" "+y_coor+"/"+mScreenshot.getBounds().height+"\r\n");
            				//singleCommandRun("RelativeLongPress "+x_coor+" "+y_coor);
            				UiAutomatorViewer.getApp().getXMLAction.setCommand("RelativeLongPress "+x_coor+" "+y_coor);
        					//update screen and attribution table
        					UiAutomatorViewer.getApp().getXMLAction.run();
            			}
                	});
                	
                	addRightMenu(menu);
                	
                }
            }
        });
        ///********************************************************
        
        mScreenshotCanvas.setBackground(
                getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        mScreenshotCanvas.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                if (mScreenshot != null) {
                    updateScreenshotTransformation();
                    // shifting the image here, so that there's a border around screen shot
                    // this makes highlighting red rectangles on the screen shot edges more visible
                    Transform t = new Transform(e.gc.getDevice());
                    t.translate(mDx, mDy);
                    t.scale(mScale, mScale);
                    e.gc.setTransform(t);
                    e.gc.drawImage(mScreenshot, 0, 0);
                    // this resets the transformation to identity transform, i.e. no change
                    // we don't use transformation here because it will cause the line pattern
                    // and line width of highlight rect to be scaled, causing to appear to be blurry
                    e.gc.setTransform(null);
                    if (mModel.shouldShowNafNodes()) {
                        // highlight the "Not Accessibility Friendly" nodes
                        e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_YELLOW));
                        e.gc.setBackground(e.gc.getDevice().getSystemColor(SWT.COLOR_YELLOW));
                        for (Rectangle r : mModel.getNafNodes()) {
                            e.gc.setAlpha(50);
                            e.gc.fillRectangle(mDx + getScaledSize(r.x), mDy + getScaledSize(r.y),
                                    getScaledSize(r.width), getScaledSize(r.height));
                            e.gc.setAlpha(255);
                            e.gc.setLineStyle(SWT.LINE_SOLID);
                            e.gc.setLineWidth(2);
                            e.gc.drawRectangle(mDx + getScaledSize(r.x), mDy + getScaledSize(r.y),
                                    getScaledSize(r.width), getScaledSize(r.height));
                        }
                    }

                    // draw the search result rects
                    if (mSearchResult != null) {
                        for (BasicTreeNode result : mSearchResult) {
                            if (result instanceof UiNode) {
                                UiNode uiNode = (UiNode) result;
                                Rectangle rect = new Rectangle(
                                        uiNode.x, uiNode.y, uiNode.width, uiNode.height);
                                e.gc.setForeground(
                                        e.gc.getDevice().getSystemColor(SWT.COLOR_YELLOW));
                                e.gc.setLineStyle(SWT.LINE_DASH);
                                e.gc.setLineWidth(1);
                                e.gc.drawRectangle(mDx + getScaledSize(rect.x),
                                        mDy + getScaledSize(rect.y),
                                        getScaledSize(rect.width), getScaledSize(rect.height));
                            }
                        }
                    }

                    // draw the mouseover rects
                    Rectangle rect = mModel.getCurrentDrawingRect();
                    if (rect != null) {
                        e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_RED));
                        if (mModel.isExploreMode()) {
                            // when we highlight nodes dynamically on mouse move,
                            // use dashed borders
                            e.gc.setLineStyle(SWT.LINE_DASH);
                            e.gc.setLineWidth(1);
                        } else {
                            // when highlighting nodes on tree node selection,
                            // use solid borders
                            e.gc.setLineStyle(SWT.LINE_SOLID);
                            e.gc.setLineWidth(2);
                        }
                        e.gc.drawRectangle(mDx + getScaledSize(rect.x), mDy + getScaledSize(rect.y),
                                getScaledSize(rect.width), getScaledSize(rect.height));
                    }
                }
            }
        });
        mScreenshotCanvas.addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent e) {
            	
            	//*****************************add by carl
                //mScreenshotComposite.setToolTipText(String.format("(%d,%d)", e.x - mDx, e.y - mDy));
                //****************************************
                
                
                if (mModel != null) {
                	
                    int x = getInverseScaledSize(e.x - mDx);
                    int y = getInverseScaledSize(e.y - mDy);
                    
                    // show coordinate
                    coordinateLabel.setText(String.format("(%d,%d)", x,y));
                    if (mModel.isExploreMode()) {
                        BasicTreeNode node = mModel.updateSelectionForCoordinates(x, y);
                        if (node != null) {
                            updateTreeSelection(node);
                        }
                    }
                    
                }
                
            }
        });
        
        mScreenshotCanvas.addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {
				int x = getInverseScaledSize(e.x - mDx);
                int y = getInverseScaledSize(e.y - mDy);
                
                try {
                	if (mModel.getSelectedNode() != null) {
      					//mScreenshotCanvas.setToolTipText("Coordinate:"+x + "," + y);
                    	
                    	BasicTreeNode selectedNode = mModel.getSelectedNode();
          				Object [] selectedElement = selectedNode.getAttributesArray();
          				
          				String selectedElementDetail = "";
          				
          				for (int j=0; j<selectedElement.length; j++) {
          					//if (((AttributePair)selectedElement[j]).key.equals("text")) {
        						 
          					selectedElementDetail = selectedElementDetail + 
          							((AttributePair)selectedElement[j]).key
          							+ " : "+((AttributePair)selectedElement[j]).value+"\n";

        					 //}
          				}
          				
          				mScreenshotCanvas.setToolTipText("Coordinate:"+x + "," + y + "\n" +
          						selectedElementDetail);
      					
      				}
                } catch (NullPointerException e1) {
                	
                }
                
  				
  				
			}
        	
        });
        

        mSetScreenshotComposite = new Composite(mScreenshotComposite, SWT.NONE);
        mSetScreenshotComposite.setLayout(new GridLayout());

        final Button setScreenshotButton = new Button(mSetScreenshotComposite, SWT.PUSH);
        setScreenshotButton.setText("Specify Screenshot...");
        setScreenshotButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                FileDialog fd = new FileDialog(setScreenshotButton.getShell());
                fd.setFilterExtensions(new String[] {"*.png" });
                if (mModelFile != null) {
                    fd.setFilterPath(mModelFile.getParent());
                }
                String screenshotPath = fd.open();
                if (screenshotPath == null) {
                    return;
                }

                ImageData[] data;
                try {
                    data = new ImageLoader().load(screenshotPath);
                } catch (Exception e) {
                    return;
                }

                // "data" is an array, probably used to handle images that has multiple frames
                // i.e. gifs or icons, we just care if it has at least one here
                if (data.length < 1) {
                    return;
                }

                mScreenshot = new Image(Display.getDefault(), data[0]);
                redrawScreenshot();
            }
        });

        // right sash is split into 2 parts: upper-right and lower-right
        // both are composites with borders, so that the horizontal divider can be highlighted by
        // the borders
        SashForm rightSash = new SashForm(baseSash, SWT.VERTICAL);
        
        
        ///*****************************************create by carl******************************************
        
        SashForm thirdSash = new SashForm(baseSash, SWT.VERTICAL);
        
        //mTextComposite = new Composite(baseSash, SWT.BORDER);
        Composite thirdComposite = new Composite(thirdSash, SWT.BORDER);
        thirdComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
        //thirdComposite.setLayout(new GridLayout(1, false));
        
        scriptFolder = new CTabFolder(thirdComposite, SWT.BORDER|SWT.CLOSE);
        scriptFolder.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        DropTarget dropTarget = new DropTarget(scriptFolder, DND.DROP_MOVE);
        dropTarget.setTransfer(new Transfer[] {
                LocalSelectionTransfer.getTransfer(),
                FileTransfer.getInstance() });
        
        dropTarget.addDropListener(new DropTargetAdapter() {
			
			@Override
            public void drop(DropTargetEvent event) {
            	
            	String[] files = (String[]) event.data;
            	scriptFileName.removeAll(scriptFileName);
        		scriptFilePath.removeAll(scriptFilePath);
        		
            	for (int i=0; i<files.length; i++) {
            		
            		temp = new File(files[i]);
            		//System.out.println("start"+i+":"+files[i]);
            		//System.out.println("is faile:"+temp.isFile());
            		if (temp.isFile()) {
            			if (temp.getAbsolutePath().endsWith(".sh")) {
            				//System.out.println("file"+i+":"+files[i]);
            				
            				scriptFileName.add(temp.getName());
            				scriptFilePath.add(temp.getAbsolutePath());
            				
            				insertTextToStyledText("sh "+"\"$ScriptPath/"+temp.getName()+"\"\r\n");
            			}
            		} else {
            			//System.out.println("Other"+i+":"+files[i]);
            			for (int j=0; j<temp.listFiles().length; j++) {
            				if (temp.listFiles()[j].getAbsolutePath().endsWith(".sh")) {
            					System.out.println("file name:"+temp.listFiles()[j].getAbsolutePath());
                				
            					scriptFileName.add(temp.listFiles()[j].getName());
                				scriptFilePath.add(temp.listFiles()[j].getAbsolutePath());
                				
                				insertTextToStyledText("sh "+"\"$ScriptPath/"+temp.listFiles()[j].getName()+"\"\r\n");
            				}
            			}
            		}
            		
            	}
            	
            	/*
            	//System.out.println("total:"+tableScriptList.getTable().getItemCount());
            	for (int j=0; j<tableScriptList.getTable().getItemCount(); j++) {
            		//System.out.println("name:"+tableScriptList.getTable().getItem(j).getText(1));
            		//System.out.println("path:"+tableScriptList.getTable().getItem(j).getText(2));
            	}
            	*/
            	
            	ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
		        try {
		            dialog.run(true, false, new IRunnableWithProgress() {
		                @Override
		                public void run(IProgressMonitor monitor) throws InvocationTargetException,
		                                                                        InterruptedException {
		                	
		                	for (int j=0; j<scriptFileName.size(); j++) {
		                		//System.out.println(temp.listFiles()[j].getAbsolutePath());
            					monitor.subTask("Push script: "
										+ scriptFileName.get(j));
            					
            					//System.out.println("name:"+scriptFileName.get(j));
            					//System.out.println("path"+scriptFilePath.get(j));
            					
            					//push script to /mnt/sdcard/ShellResult/ScriptFolder
            					CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",
                						UiAutomatorViewer.getApp().getChoosedDevice().toString(),
                						"push",scriptFilePath.get(j),
                						scriptFolderPath+scriptFileName.get(j)});
                				
            					//change the script to unix format
            					CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",
                						UiAutomatorViewer.getApp().getChoosedDevice().toString(),
                						"shell", InstallAction.busyboxPath ,"dos2unix",
                						scriptFolderPath+scriptFileName.get(j)});
                				
	            			}
		                	
		                	monitor.done();
		                }
		            });
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
            	
            }
        });
        //dropTarget.setComponent(getSelectedStyledText());
        
        //scriptFolder.setMaximized(true);
        
        //UiAutomatorViewer.getApp().getCopyAction().setEnabled(true);
		//UiAutomatorViewer.getApp().getCutAction().setEnabled(true);
		//UiAutomatorViewer.getApp().getPasteAction().setEnabled(true);
		//UiAutomatorViewer.getApp().getRedoAction().setEnabled(true);
		//UiAutomatorViewer.getApp().getUndoAction().setEnabled(true);
        
        scriptFolder.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				mSourceViewerIndex = scriptFolder.getSelectionIndex();
				//System.out.println("Choosed id:"+scriptFolder.getSelectionIndex());
			}
        	
        });
        
        ///**********************************create by carl*******************************
        
        
        // upper-right base contains the toolbar and the tree
        upperRightBase = new Composite(rightSash, SWT.BORDER);
        upperRightBase.setLayout(new GridLayout(1, false));

        ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
        toolBarManager.add(new ExpandAllAction(this));
        toolBarManager.add(new ToggleNafAction(this));
        ToolBar searchtoolbar = toolBarManager.createControl(upperRightBase);

        // add search box and navigation buttons for search results
        ToolItem itemSeparator = new ToolItem(searchtoolbar, SWT.SEPARATOR | SWT.RIGHT);
        searchTextarea = new Text(searchtoolbar, SWT.BORDER | SWT.SINGLE | SWT.SEARCH);
        searchTextarea.setToolTipText("Can search text and cont-desc");
        searchTextarea.pack();
        itemSeparator.setWidth(searchTextarea.getBounds().width);
        itemSeparator.setControl(searchTextarea);
        itemPrev = new ToolItem(searchtoolbar, SWT.SIMPLE);
        itemPrev.setImage(ImageHelper.loadImageDescriptorFromResource("images/prev.png")
                .createImage());
        itemNext = new ToolItem(searchtoolbar, SWT.SIMPLE);
        itemNext.setImage(ImageHelper.loadImageDescriptorFromResource("images/next.png")
                .createImage());
        itemDeleteAndInfo = new ToolItem(searchtoolbar, SWT.SIMPLE);
        itemDeleteAndInfo.setImage(ImageHelper.loadImageDescriptorFromResource("images/delete.png")
                .createImage());
        itemDeleteAndInfo.setToolTipText("Clear search results");
        coordinateLabel = new ToolItem(searchtoolbar, SWT.SIMPLE);
        coordinateLabel.setText("");
        coordinateLabel.setEnabled(false);
        
        // add search function
        searchTextarea.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent event) {
                if (event.keyCode == SWT.CR) {
                    String term = searchTextarea.getText();
                    if (!term.isEmpty()) {
                        if (term.equals(mLastSearchedTerm)) {
                            nextSearchResult();
                            return;
                        }
                        clearSearchResult();
                        mSearchResult = mModel.searchNode(term);
                        if (!mSearchResult.isEmpty()) {
                            mSearchResultIndex = 0;
                            updateSearchResultSelection();
                            mLastSearchedTerm = term;
                        }
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent event) {
            }
        });
        SelectionListener l = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent se) {
                if (se.getSource() == itemPrev) {
                    prevSearchResult();
                 } else if (se.getSource() == itemNext) {
                    nextSearchResult();
                 } else if (se.getSource() == itemDeleteAndInfo) {
                    searchTextarea.setText("");
                    clearSearchResult();
                 }
            }
        };
        itemPrev.addSelectionListener(l);
        itemNext.addSelectionListener(l);
        itemDeleteAndInfo.addSelectionListener(l);

        searchtoolbar.pack();
        
        ///****************************updated by carl*********************
        //searchtoolbar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        searchtoolbar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL, 40));

        mTreeViewer = new TreeViewer(upperRightBase, SWT.NONE);
        mTreeViewer.setContentProvider(new BasicTreeNodeContentProvider());
        // default LabelProvider uses toString() to generate text to display
        mTreeViewer.setLabelProvider(new LabelProvider());
        mTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                BasicTreeNode selectedNode = null;
                if (event.getSelection() instanceof IStructuredSelection) {
                    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                    Object o = selection.getFirstElement();
                    if (o instanceof BasicTreeNode) {
                        selectedNode = (BasicTreeNode) o;
                    }
                }

                mModel.setSelectedNode(selectedNode);
                redrawScreenshot();
                if (selectedNode != null) {
                    loadAttributeTable();
                }
            }
        });
        Tree tree = mTreeViewer.getTree();
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        // move focus so that it's not on tool bar (looks weird)
        tree.setFocus();

        // lower-right base contains the detail group
        middleRightBase = new Composite(rightSash, SWT.BORDER);
        //middleRightBase.setLayout(new GridLayout(1, true));
        middleRightBase.setLayout(new FillLayout());
        grpNodeDetail = new Group(middleRightBase, SWT.NONE);
        grpNodeDetail.setLayout(new FillLayout(SWT.HORIZONTAL));
        grpNodeDetail.setText("Node Detail");
        
        //******************************
        //******************************create by carl
        Composite lowwerRightBase = new Composite(rightSash, SWT.BORDER);
        //lowwerRightBase.setLayout(new GridLayout(5, false));
        //lowwerRightBase.setLayout(new FillLayout());
        lowwerRightBase.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        CTabFolder attrAndWireless = new CTabFolder(lowwerRightBase, SWT.BORDER);
        attrAndWireless.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        CTabItem attrTabItem = new CTabItem(attrAndWireless, SWT.NONE);
		CTabItem wirelessTabItem = new CTabItem(attrAndWireless, SWT.NONE);
		
		attrTabItem.setText("Attr");
		wirelessTabItem.setText("Wireless");
        
		attrAndWireless.setSelection(attrTabItem);
		
		ScrolledComposite sc = new ScrolledComposite(attrAndWireless, SWT.H_SCROLL|SWT.V_SCROLL|SWT.BORDER);
		//sc.setLayout(new FillLayout());
		
        Composite attr = new Composite(sc, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        attr.setLayout(gridLayout);
        
        attrTabItem.setControl(sc);
        
        Composite wireless = new Composite(attrAndWireless, SWT.BORDER);
        wirelessTabItem.setControl(wireless);
        
        
        //wireless.setLayout(gridLayout);
        wireless.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        
        GridData gridData = new GridData();
        gridData.horizontalSpan= 4;
        gridData.horizontalAlignment = SWT.FILL;
        
        //GridData twoGridData = new GridData();
        //twoGridData.horizontalSpan=2;
        //twoGridData.horizontalAlignment=SWT.FILL;
        
        
        Group multiDevice = new Group(wireless, SWT.NONE);
        multiDevice.setText("Multi Device");
        //multiDevice.setLayout(new RowLayout());
        multiDevice.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        Group wirelessOperation = new Group(wireless, SWT.NONE);
        wirelessOperation.setText("Wireless Operation");
        
        RowLayout rowLayout = new RowLayout();
        rowLayout.marginLeft = 5;
        rowLayout.marginRight = 5;
        rowLayout.spacing = 20;
        wirelessOperation.setLayout(rowLayout);

        final Text ipAddr = new Text(wirelessOperation, SWT.BORDER | SWT.CENTER);
        ipAddr.setText("192.168.1.100");
        ipAddr.setToolTipText("Input ip address of test device");
        
        final Table wireTable = new Table(multiDevice, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
        
        wireTable.setHeaderVisible(true);
        wireTable.setLinesVisible(true);
        //.setControl(wireTable);
        
        TableColumn ipAddrColumn = new TableColumn(wireTable, SWT.NONE);
        ipAddrColumn.setWidth(120);
        ipAddrColumn.setText("IP Addr");
        
        TableColumn connectColumn = new TableColumn(wireTable, SWT.NONE);
        connectColumn.setWidth(105);
        connectColumn.setText("Connected");
        
        Menu menuWireless = new Menu(wireTable);
        wireTable.setMenu(menuWireless);
        
        MenuItem checkConnection = new MenuItem(menuWireless, SWT.NONE);
        checkConnection.addSelectionListener(new SelectionAdapter() {
  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				
  				for (int i=0; i<wireTable.getItemCount(); i++) {
  					if (wireTable.getItem(i).getChecked()) {
  						String singleResult = CMDUtils.cmdCommandGetInput(new String[] {"adb",
  								"connect",wireTable.getItem(i).getText(0)+":5558"});
  						
  						if (singleResult.contains("unable")) {
  							wireTable.getItems()[i].setText(1, "Failed");
  						} else if (singleResult.contains("already")) {
  							wireTable.getItems()[i].setText(1, "success");
  						} else if (singleResult.startsWith("connect")) {
  							wireTable.getItems()[i].setText(1, "success");
  						}
  						
  					}
  				}
  			}
  		});
        checkConnection.setText("Check connection");
        
        MenuItem delete = new MenuItem(menuWireless, SWT.NONE);
        delete.addSelectionListener(new SelectionAdapter() {
  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				int index = wireTable.getSelectionIndex();
  				if (index>=0 && index<wireTable.getItemCount()) {
  					wireTable.remove(wireTable.getSelectionIndex());
  				}
  			}
  		});
        delete.setText("Delete");
        
        MenuItem clear = new MenuItem(menuWireless, SWT.NONE);
        clear.addSelectionListener(new SelectionAdapter() {
  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				wireTable.removeAll();
  			}
  		});
        clear.setText("Clear all");
        
        MenuItem selectAll = new MenuItem(menuWireless, SWT.NONE);
        selectAll.addSelectionListener(new SelectionAdapter() {
  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				for (int i=0; i<wireTable.getItemCount(); i++) {
  					wireTable.getItem(i).setChecked(true);
  				}
  			}
  		});
        selectAll.setText("Select all");
        
        MenuItem unselectAll = new MenuItem(menuWireless, SWT.NONE);
        unselectAll.addSelectionListener(new SelectionAdapter() {
  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				for (int i=0; i<wireTable.getItemCount(); i++) {
  					wireTable.getItem(i).setChecked(false);
  				}
  			}
  		});
        unselectAll.setText("Unselect all");

        MenuItem runMultiScript = new MenuItem(menuWireless, SWT.NONE);
        runMultiScript.addSelectionListener(new SelectionAdapter() {
  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				wirelessRun = true;
  				wirelessDeviceList.removeAll(wirelessDeviceList);
  				for(int i=0; i<wireTable.getItemCount(); i++) {
  					
  					if (wireTable.getItem(i).getChecked()) {
  						//if the device wireless connect success, add to wirelessDeviceList
  						if (wireTable.getItem(i).getText(1).equals("success")) {
  							wirelessDeviceList.add(wireTable.getItem(i).getText(0));
  						}
  					}
  				}
  				
  				ScriptListTable.setWirelessDeviceList(wirelessDeviceList);
  				
  				UiAutomatorViewer.getApp().getRunMultiAction().run();
  			}
  		});
        runMultiScript.setText("Multi script push");
        
        MenuItem run = new MenuItem(menuWireless, SWT.NONE);
        run.addSelectionListener(new SelectionAdapter() {
  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				//wirelessRun = true;
  				//wirelessDeviceList.removeAll(wirelessDeviceList);
  				for(int i=0; i<wireTable.getItemCount(); i++) {
  					
  					if (wireTable.getItem(i).getChecked()) {
  						//if the device wireless connect successed, add to wirelessDeviceList
  						if (wireTable.getItem(i).getText(1).equals("success")) {
  							//wirelessDeviceList.add(wireTable.getItem(i).getText(0));
  							
  							//run script
  							new RunAction(UiAutomatorViewer.getApp()).runscript(
  									wireTable.getItem(i).getText(0)+":5558");
  							
  						}
  					}
  				}
  				
  				//ScriptListTable.setWirelessDeviceList(wirelessDeviceList);
  				//UiAutomatorViewer.getApp().getRunMultiAction().run();
  			}
  		});
        run.setText("Run");
        
        MenuItem runBD = new MenuItem(menuWireless, SWT.NONE);
        runBD.addSelectionListener(new SelectionAdapter() {
  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				//wirelessRun = true;
  				//wirelessDeviceList.removeAll(wirelessDeviceList);
  				for(int i=0; i<wireTable.getItemCount(); i++) {
  					
  					if (wireTable.getItem(i).getChecked()) {
  						//if the device wireless connect successed, add to wirelessDeviceList
  						if (wireTable.getItem(i).getText(1).equals("success")) {
  							//wirelessDeviceList.add(wireTable.getItem(i).getText(0));
  							
  							//run script
  							new RunBDAction(UiAutomatorViewer.getApp()).runscript(
  									wireTable.getItem(i).getText(0)+":5558");
  							
  						}
  					}
  				}
  				
  				//ScriptListTable.setWirelessDeviceList(wirelessDeviceList);
  				//UiAutomatorViewer.getApp().getRunMultiAction().run();
  			}
  		});
        runBD.setText("RunBD");
        
        MenuItem stop = new MenuItem(menuWireless, SWT.NONE);
        stop.addSelectionListener(new SelectionAdapter() {
  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				//wirelessRun = true;
  				//wirelessDeviceList.removeAll(wirelessDeviceList);
  				for(int i=0; i<wireTable.getItemCount(); i++) {
  					
  					if (wireTable.getItem(i).getChecked()) {
  						
  						if (wireTable.getItem(i).getText(1).equals("success")) {
  							
  							//stop script
  							new StopAction().stopRunningScript(wireTable.getItem(i).getText(0)+":5558");
  							
  						}
  					}
  				}
  			}
  		});
        stop.setText("Stop");
        
        
        
		
        Button connect = new Button(wirelessOperation, SWT.PUSH);
        connect.setText("Connect");
        connect.setToolTipText("Connect to the ip address via: adb connect ip:port");
        
        connect.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String result = CMDUtils.cmdCommandGetInput(new String[]{"adb","connect",ipAddr.getText()+":5558"});
				UiAutomatorViewer.getApp().messageBox_OK("Connect Result", result);
			}
        	
        });

        Button add = new Button(wirelessOperation, SWT.PUSH);
        add.setText("Add");
        add.setToolTipText("Add to multi device table");
        
        add.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
  				String ipAddrStr = ipAddr.getText();
  				int i=0;
  				for (i=0; i<wireTable.getItemCount(); i++) {
  					if (wireTable.getItem(i).getText().equals(ipAddrStr)) {
  						break;
  					}
  				}
  				if (i >= wireTable.getItemCount()) {
  					TableItem item = new TableItem(wireTable, SWT.NONE);
  					item.setText(ipAddr.getText());
  				} else {
  					UiAutomatorViewer.getApp().messageBox_OK("Add Result", "Existed");
  				}
			}
        	
        });
        
        //Button add = new Button(wirelessOperation, SWT.PUSH);
        //add.setText("Add");
        //add.setToolTipText("Add to multi device table");
        //Composite wirelessTable = new Composite(multiDevice, SWT.NONE);
        
        
        Group attrGroup = new Group(attr, SWT.NONE);
        
        attrGroup.setText("Attr operation");
        attrGroup.setLayout(new RowLayout());
        attrGroup.setLayoutData(gridData);
        
        final Combo attrOperation = new Combo(attrGroup, SWT.NONE);
        attrOperation.setToolTipText("AttrOperation");
        String[] attrOperationList = new String[]{"Click", "DoubleClick",
        		"LongPress", "Exist", "Find"};
        attrOperation.setItems(attrOperationList);
        attrOperation.setText("Attr");
        
        attrOperation.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				sendOption = attrOperation.getItem(attrOperation.getSelectionIndex());
				
			}
        });
        
        final Combo isOption = new Combo(attrGroup, SWT.NONE);
        isOption.setToolTipText("Is");
        String[] isOptionList = new String[]{"checkable", "checked", "clickable", "enabled",
        		"focused", "scrollable", "long-clickable", "password", "selected"};
        isOption.setItems(isOptionList);
        isOption.setText("Is");
        
        isOption.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				sendOption = isOption.getItem(isOption.getSelectionIndex());
			}
        	
        });
        
        final Combo swipeOption = new Combo(attrGroup, SWT.NONE);
        swipeOption.setToolTipText("Swipe");
        String[] swipeOptionList = new String[]{"Up", "Down", "Left", "Right"};
        swipeOption.setItems(swipeOptionList);
        swipeOption.setText("Swipe");
        
        swipeOption.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				sendOption = swipeOption.getItem(swipeOption.getSelectionIndex());
			}
        	
        });
        
        final Combo rotateOption = new Combo(attrGroup, SWT.NONE);
        rotateOption.setToolTipText("Rotate");
        String[] rotateOptionList = new String[]{"Left", "Right", "Nature"};
        rotateOption.setItems(rotateOptionList);
        rotateOption.setText("Rotate");
        
        rotateOption.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				sendOption = "Rotate "+rotateOption.getItem(rotateOption.getSelectionIndex());
			}
        	
        });
        
        final Combo dragOption = new Combo(attrGroup, SWT.NONE);
        dragOption.setToolTipText("Drag");
        String[] dragOptionList = new String[]{"From", "To"};
        dragOption.setItems(dragOptionList);
        dragOption.setText("Drag");
        
        dragOption.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				sendOption = "Drag"+dragOption.getItem(dragOption.getSelectionIndex());
			}
        	
        });
        
        final Combo pinchOption = new Combo(attrGroup, SWT.NONE);
        pinchOption.setToolTipText("Pinch");
        String[] pinchOptionList = new String[]{"In", "Out"};
        pinchOption.setItems(pinchOptionList);
        pinchOption.setText("Pinch");
        
        pinchOption.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				sendOption = pinchOption.getItem(pinchOption.getSelectionIndex());
			}
        	
        });
        
        Group hardkey = new Group(attr, SWT.NONE);
        //hardkey.setLayoutData(gridData);
        
        hardkey.setText("Hardkey");
        hardkey.setLayout(new RowLayout());
        
        final Combo hardKeyOption = new Combo(hardkey, SWT.NONE);
        hardKeyOption.setToolTipText("HardKey");
        String[] hardKeyList = new String[]{"Back","Menu","LongPressMenu","Home","Power",
        		"LongPressPower","Recent", "Hook", "VolumeUp", "VolumeDown","Power+VolumeDown"};
        hardKeyOption.setItems(hardKeyList);
        hardKeyOption.setText("HardKey");
        
        hardKeyOption.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				sendOption = hardKeyOption.getItem(hardKeyOption.getSelectionIndex());
			}
        	
        });
        
        Group errorHandle = new Group(attr, SWT.NONE);
        errorHandle.setText("Error handle");
        errorHandle.setLayout(new RowLayout());
        
        final Combo errorHandleOption = new Combo(errorHandle, SWT.NONE);
        errorHandleOption.setToolTipText("ErrorHandle");
        String[] errorHandleOptionList = new String[]{"BackToHome", "CrashHandle",
        		"Screenshot", "GetDeviceInfo"};
        errorHandleOption.setItems(errorHandleOptionList);
        errorHandleOption.setText("ErrorHandle");
        
        errorHandleOption.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				sendOption = errorHandleOption.getItem(errorHandleOption.getSelectionIndex());
			}
        	
        });
        
        Group log = new Group(attr, SWT.NONE);
        log.setText("Log");
        log.setLayout(new RowLayout());
        
        final Combo logOption = new Combo(log, SWT.NONE);
        logOption.setToolTipText("Log");
        String[] logOptionList = new String[]{"LogPass", "LogFail"};
        logOption.setItems(logOptionList);
        logOption.setText("Log");
        
        logOption.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				sendOption = logOption.getItem(logOption.getSelectionIndex());
			}
        	
        });
        
        /*
        Group getStatus = new Group(attr, SWT.NONE);
        //getStatus.setLayoutData(gridData);
        
        getStatus.setText("Get status");
        getStatus.setLayout(new RowLayout());
        
        final Combo getStatusOption = new Combo(getStatus, SWT.NONE);
        getStatusOption.setToolTipText("Get status of test device");
        String[] getStatusOptionList = new String[]{"GetAPUss", "GetBattery",
         "GetCPUFreq", "GetCPUUsage", "GetSignalStrength"};
        getStatusOption.setItems(getStatusOptionList);
        getStatusOption.setText("GetStatus");
        
        getStatusOption.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText(getStatusOption.getItem(
				getStatusOption.getSelectionIndex())+"\r\n");
			}
        	
        });
        */
        
        Group scriptOption = new Group(attr, SWT.NONE);
        //scriptOption.setLayoutData(gridData);
        scriptOption.setLayout(new RowLayout());
        scriptOption.setText("Script option");
        
        final Combo shellScriptOption = new Combo(scriptOption, SWT.NONE);
        shellScriptOption.setToolTipText("Shell script option");
        String[] shellScriptOptionList = new String[]{"for", "break", "input text",
        		"comment", "uncomment", "Wait"};
        shellScriptOption.setItems(shellScriptOptionList);
        shellScriptOption.setText("ScriptOption");
        
        shellScriptOption.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				sendOption = shellScriptOption.getItem(shellScriptOption.getSelectionIndex());
				//shellScriptOption.setBackground(new Color(getDisplay(), 99, 99, 99));
			}
        	
        });
        
        Button sendButton = new Button(attr, SWT.PUSH);
        sendButton.setText("Send");
        
        sendButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				sendOption();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
        	
        });
        
        
        sc.setContent(attr);
        sc.setExpandHorizontal(true);
	    sc.setExpandVertical(true);
	    //sc.setAlwaysShowScrollBars(true);
	    sc.setMinSize(attr.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	    
        rightSash.setWeights(new int[] {2, 3, 2});
        //******************************************************************

        tableContainer = new Composite(grpNodeDetail, SWT.NONE);

        TableColumnLayout columnLayout = new TableColumnLayout();
        tableContainer.setLayout(columnLayout);

        mTableViewer = new TableViewer(tableContainer, SWT.NONE | SWT.FULL_SELECTION);
        Table table = mTableViewer.getTable();
        table.setLinesVisible(true);
        // use ArrayContentProvider here, it assumes the input to the TableViewer
        // is an array, where each element represents a row in the table
        mTableViewer.setContentProvider(new ArrayContentProvider());
        ///********************************************************create by carl
        
        Menu menuTableViewer = new Menu(mTableViewer.getControl());
    	mTableViewer.getTable().setMenu(menuTableViewer);
    	addRightMenu(menuTableViewer);
    	//mTableViewer.getTable().getL
    	
        ///***************************************************************************

        TableViewerColumn tableViewerColumnKey = new TableViewerColumn(mTableViewer, SWT.NONE);
        TableColumn tblclmnKey = tableViewerColumnKey.getColumn();
        
        tableViewerColumnKey.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof AttributePair) {
                    // first column, shows the attribute name
                    return ((AttributePair) element).key;
                }
                return super.getText(element);
            }
        });
        columnLayout.setColumnData(tblclmnKey,
                new ColumnWeightData(2, ColumnWeightData.MINIMUM_WIDTH, true));
        
        TableViewerColumn tableViewerColumnValue = new TableViewerColumn(mTableViewer, SWT.NONE);
        tableViewerColumnValue.setEditingSupport(new AttributeTableEditingSupport(mTableViewer));
        
        TableColumn tblclmnValue = tableViewerColumnValue.getColumn();
        columnLayout.setColumnData(tblclmnValue,
                new ColumnWeightData(4, ColumnWeightData.MINIMUM_WIDTH, true));
        
        tableViewerColumnValue.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof AttributePair) {
                    // second column, shows the attribute value
                	
                	//***************************create by carl
                	/*
                	if (((AttributePair) element).key.equals("text")) {
                		System.out.println("text:"+((AttributePair) element).value);
                	}
                	if (((AttributePair) element).key.equals("resource-id")) {
                		System.out.println("resource-id:"+((AttributePair) element).value);
                	}
                	*/
                	//************************************************************
                	
                    return ((AttributePair) element).value;
                }
                return super.getText(element);
            }
        });
        
        //*********************************create by carl
        TableViewerColumn tableViewerColumnUnique = new TableViewerColumn(mTableViewer, SWT.NONE);
        TableColumn tblclmnUnique = tableViewerColumnUnique.getColumn();
        columnLayout.setColumnData(tblclmnUnique,
                new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
        
        tableViewerColumnUnique.setLabelProvider(new ColumnLabelProvider() {
        	 @Override
             public String getText(Object element) {
        		 
        		 int instance = 1;
                 if (element instanceof AttributePair) {
                     // third column, shows the attribute instance
                	 
                	 if (((AttributePair) element).key.equals("index")) {
                		 
                		 List<BasicTreeNode> totalTreeNode = mModel.getmNodelist();
                		 BasicTreeNode selectedNode = mModel.getSelectedNode();
                		 Object [] selectedElement = selectedNode.getAttributesArray();
                		 
                		instance = 1;
                		 
                		 for (int i=0; i<totalTreeNode.size(); i++) {
                			 if (selectedNode.equals(totalTreeNode.get(i))) {
                				 
                				 //System.out.println("i================="+i);
                				 break;
                				 
                			 } else {
                				 
                				 boolean textCompare = false;
                				 boolean resourceCompare = false;
                				 boolean classCompare = false;
                				 boolean descriptionCompare = false;
                				 
                				 Object [] compareElement = totalTreeNode.get(i).getAttributesArray();
                				 for (int j=0; j<selectedElement.length; j++) {
                					 
                					 //if compareElement has NAF item, update compare value

            						 //if (j==0 && ((AttributePair)compareElement[0]).key.equals("NAF")) {
                					 //	 compareNum = j + 1;
                					 //}
                					 
                					 //System.out.println("key:"+((AttributePair)selectedElement[selectNum]).key);
                					 
                					 if (((AttributePair)selectedElement[j]).key.equals("text")) {
                						 
                						 if (((AttributePair)selectedElement[j]).value.equals(((AttributePair)compareElement[j]).value)) {
                							 textCompare = true;
                						 }
                					 }
                					 
                					 if (((AttributePair)selectedElement[j]).key.equals("resource-id")) {
                						 if (((AttributePair)selectedElement[j]).value.equals(((AttributePair)compareElement[j]).value)) {
                							 resourceCompare = true;
                						 }
                					 }
                					 
                					 if (((AttributePair)selectedElement[j]).key.equals("class")) {
                						 if (((AttributePair)selectedElement[j]).value.equals(((AttributePair)compareElement[j]).value)) {
                							 classCompare = true;
                						 }
                					 }
                					 
                					 if (((AttributePair)selectedElement[j]).key.equals("content-desc")) {
                						 if (((AttributePair)selectedElement[j]).value.equals(((AttributePair)compareElement[j]).value)) {
                							 descriptionCompare = true;
                						 }
                					 }
                					 
                				 }
                				 if (textCompare && resourceCompare && classCompare && descriptionCompare) {
            						 instance++;
            					 }
                			 }
                		 }
                		 
                		 return String.valueOf(instance);
                	 }
                	 
                	 if (((AttributePair) element).key.equals("text")) {
                		 
                		 //List<BasicTreeNode> totalTreeNode = UiAutomatorModel.getmNodelist();
                		 List<BasicTreeNode> totalTreeNode = mModel.getmNodelist();
                		 
                		 //System.out.println("total tree node:"+totalTreeNode.size());
                		 
                		 for (int i=0; i<totalTreeNode.size(); i++) {
                			 
                			 BasicTreeNode selectedNode = mModel.getSelectedNode();
                			 
                			 if (selectedNode.equals(totalTreeNode.get(i))) {
                				 
                				 break;
                				 //System.out.println("Find it without getAttr, equal:"+i);
                			 } else {
                				 
                				 Object [] selectedElement = totalTreeNode.get(i).getAttributesArray();
                				 
                				 for (int j=0; j<selectedElement.length; j++) {

                					 if (((AttributePair) selectedElement[j]).key.equals("text")) {
                						 if (((AttributePair) selectedElement[j]).value.equals(((AttributePair) element).value)) {
                							 instance++;
                						 }
                					 }
                					 //System.out.println("selectedElement["+j+"]"+((AttributePair) selectedElement[j]).value);
                				 }
                			 }
                			 
                			 /*
                			 if (selectedNode == totalTreeNode.get(i)) {
                				 System.out.println("Find it without getAttr=="+i);
                			 }
                			 */
                		 }
                		 
                		 //System.out.println("text:"+((AttributePair) element).value);
                		 return String.valueOf(instance);
                	 }
                	 
                	 if (((AttributePair) element).key.equals("resource-id")) {
                		 
                		 List<BasicTreeNode> totalTreeNode = mModel.getmNodelist();
                		 
                		 for (int i=0; i<totalTreeNode.size(); i++) {
                			 
                			 BasicTreeNode selectedNode = mModel.getSelectedNode();
                			 
                			 if (selectedNode.equals(totalTreeNode.get(i))) {
                				 
                				 break;
                				 
                			 } else {
                				 
                				 Object [] selectedElement = totalTreeNode.get(i).getAttributesArray();
                				 
                				 for (int j=0; j<selectedElement.length; j++) {

                					 if (((AttributePair) selectedElement[j]).key.equals("resource-id")) {
                						 if (((AttributePair) selectedElement[j]).value.equals(((AttributePair) element).value)) {
                							 instance++;
                						 }
                					 }
                				 }
                			 }
                		 }
                		 return String.valueOf(instance);
                	 }
                	 
                	 if (((AttributePair) element).key.equals("content-desc")) {
                		 
                		 List<BasicTreeNode> totalTreeNode = mModel.getmNodelist();
                		 
                		 for (int i=0; i<totalTreeNode.size(); i++) {
                			 
                			 BasicTreeNode selectedNode = mModel.getSelectedNode();
                			 
                			 if (selectedNode.equals(totalTreeNode.get(i))) {
                				 
                				 break;
                				 
                			 } else {
                				 
                				 Object [] selectedElement = totalTreeNode.get(i).getAttributesArray();
                				 
                				 for (int j=0; j<selectedElement.length; j++) {

                					 if (((AttributePair) selectedElement[j]).key.equals("content-desc")) {
                						 if (((AttributePair) selectedElement[j]).value.equals(((AttributePair) element).value)) {
                							 instance++;
                						 }
                					 }
                				 }
                			 }
                		 }
                		 return String.valueOf(instance);
                	 }
                	 
                	 if (((AttributePair) element).key.equals("class")) {
                		 
                		 List<BasicTreeNode> totalTreeNode = mModel.getmNodelist();
                		 
                		 for (int i=0; i<totalTreeNode.size(); i++) {
                			 
                			 BasicTreeNode selectedNode = mModel.getSelectedNode();
                			 
                			 if (selectedNode.equals(totalTreeNode.get(i))) {
                				 
                				 break;
                				 
                			 } else {
                				 
                				 Object [] selectedElement = totalTreeNode.get(i).getAttributesArray();
                				 
                				 for (int j=0; j<selectedElement.length; j++) {

                					 if (((AttributePair) selectedElement[j]).key.equals("class")) {
                						 if (((AttributePair) selectedElement[j]).value.equals(((AttributePair) element).value)) {
                							 instance++;
                						 }
                					 }
                				 }
                			 }
                		 }
                		 return String.valueOf(instance);
                	 }
                	 
                 }
                 return "NA";
             }
        });
        
        
        //mTableViewer.getTable().setEnabled(false);
        //******************************************************************
        
        
        // sets the ratio of the vertical split: left 5 vs right 3
        baseSash.setWeights(new int[] {2, 3, 3 });
        
        ///***********************create by carl
        
        //add one field to show logs
        SashForm logSash = new SashForm(thirdSash, SWT.HORIZONTAL);
        thirdSash.setWeights(new int[]{5, 2});
        
        Composite logComposite = new Composite(logSash, SWT.BORDER);
        logComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        //add one CTabFolder to this field
        logTabFolder = new CTabFolder(logComposite, SWT.BORDER);
        logTabFolder.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        //add right toolbar to this CTabFolder
        ToolBar tabFolderToolBar = new ToolBar(logTabFolder, SWT.NONE);
        //add one button on the toolbar
        ToolItem showItem = new ToolItem(tabFolderToolBar, SWT.PUSH);
        showItem.setText("Show");
        
        ToolItem stopItem = new ToolItem(tabFolderToolBar, SWT.PUSH);
        stopItem.setText("Stop");
        stopItem.setToolTipText("Stop refresh logs ");
        
		ToolItem clearItem = new ToolItem(tabFolderToolBar, SWT.PUSH);
		clearItem.setText("Clear");
		clearItem.setToolTipText("Clear log from local");
		
		ToolItem clearDUTItem = new ToolItem(tabFolderToolBar, SWT.PUSH);
		clearDUTItem.setText("ClearDUT");
		clearDUTItem.setToolTipText("Clear log from test device");
		
		//set the CTabFolder's toolbar
		logTabFolder.setTopRight(tabFolderToolBar);
		//add two item on the CTabFolder
		CTabItem resultTabItem = new CTabItem(logTabFolder, SWT.NONE);
		CTabItem detailTabItem = new CTabItem(logTabFolder, SWT.NONE);
		resultTabItem.setText("Result");
		detailTabItem.setText("Detail");
		
		resultStyledText = new StyledText(logTabFolder, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL|SWT.CANCEL|SWT.READ_ONLY);
		resultStyledText.setFont(new Font(getDisplay(), "微軟正黑體", 12, SWT.NORMAL));
		resultStyledText.setText("");
		
		resultTabItem.setControl(resultStyledText);
		
		detailStyledText = new StyledText(logTabFolder, SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL|SWT.CANCEL|SWT.READ_ONLY);
		detailStyledText.setFont(new Font(getDisplay(), "微軟正黑體", 12, SWT.NORMAL));
		detailStyledText.setText("");
		//logTabFolder.setSelection(resultTabItem);
		detailTabItem.setControl(detailStyledText);
		
		logTabFolder.setSelection(detailTabItem);
		
		showItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				refreshResultFolder();
				
				/*
				if (Integer.valueOf(lineCount) > 50) {
					MessageDialog.openInformation(null, "More than 50 line", "Result has: "+
						lineCount.replaceAll("\r|\n", "")+" lines, show the last 50 line");
				}
				
				String runResult = CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",
						UiAutomatorViewer.getApp().getChoosedDevice().toString(),
						"shell","cat","/mnt/sdcard/ShellResult/Log/RunDetail.txt"});
				
				String [] runResultDetail = runResult.split("\n");
				for (int i=0; i<runResultDetail.length; i++) {
					if (runResultDetail[i].replaceAll("\r|\n", "").equals("")) {
						continue;
					} else {
						resultStyledText.append(runResultDetail[i].replaceAll("\r|\n", "")+"\n");
					}
				}
				*/
				
			}
				
		});
		
		stopItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setStopShowLog(true);
			}
		});
		
		
		clearItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				if (logTabFolder.getSelectionIndex() == 0) {
					//the first cTabItem is result item
					resultStyledText.setText("");
					
				} else if (logTabFolder.getSelectionIndex() == 1) {
					detailStyledText.setText("");
				}
			}
		});
		
		clearDUTItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//resultStyledText.setText("");
				if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
					return;
				}
				
				CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",
						UiAutomatorViewer.getApp().getChoosedDevice().toString(),
						"shell","rm",runDetailLogPath});
				
				CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",
						UiAutomatorViewer.getApp().getChoosedDevice().toString(),
						"shell","rm",resultLogPath});
				
				CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",
						UiAutomatorViewer.getApp().getChoosedDevice().toString(),
						"shell","rm","/mnt/sdcard/ShellResult/Log/Result.json"});
			}
		});
    }
	
	public void refreshResultFolder() {
		
		if (logTabFolder.getSelectionIndex() == 0) {
			//the first cTabItem is result item
			refreshSelectedItemFromLogFolder("Result.txt", resultStyledText);
			
		} else if (logTabFolder.getSelectionIndex() == 1) {
			//the second cTabItem is detail item
			refreshSelectedItemFromLogFolder("RunDetail.txt", detailStyledText);
			
		}
		
	}
	
	public void refreshSelectedItemFromLogFolder(final String fileName, final StyledText selectedStyledText) {
		
		if (UiAutomatorViewer.getApp().getChoosedDevice() == null) {
			return;
		}
		
		String lineCount = CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",
			UiAutomatorViewer.getApp().getChoosedDevice().toString(),"shell",
			"\" cat "+logFolderPath+fileName+"|"+InstallAction.busyboxPath+" wc -l\""}).replaceAll("\r|\n", "");
		//System.out.println("lineCount:"+lineCount);
		
		if (lineCount.contains("No such file")) {
			return;
		}
		
		if (lineCount.equals("")) {
			return;
		}
		
		int lineCountNum = Integer.valueOf(lineCount);
		
		if (lineCountNum == 0) {
			return;
		}
		
		//System.out.println("styledText line count:"+resultStyledText.getLineCount());
		
		if (lineCountNum == selectedStyledText.getLineCount()-1) {
			return;
		}
		
		stopShowLog = false;
		
		if (lineCountNum > selectedStyledText.getLineCount()-1) {
			refreshStartLine =  selectedStyledText.getLineCount();
			refreshStopLine = lineCountNum;
			
			//System.out.println("refreshStartLine"+refreshStartLine);
			//System.out.println("refreshStopLine"+refreshStopLine);
			//showLogWorking = true;
			
			new Thread(
				
				new Runnable() {
					@Override
					public void run() {
						
						for (int i=refreshStartLine; i<=refreshStopLine; i++) {
							if (stopShowLog == true) {
								//showLogWorking = false;
								break;
							}
							tempLine = CMDUtils.cmdCommandGetInput(new String[] {"adb","-s",
								UiAutomatorViewer.getApp().getChoosedDevice().toString(),"shell",
								"\"cat /mnt/sdcard/ShellResult/Log/"+
								fileName+"|"+InstallAction.busyboxPath+" sed -n "+i+"p\""}).replaceAll("\r|\n", "");
							//System.out.println("tempLine="+tempLine);
							
							//update the resultStyledText from this thread
							Display.getDefault().syncExec(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									selectedStyledText.append(tempLine+"\n");
								}
								
							});
						}
					}
				}
			).start();
			stopShowLog = false;
		}
	}

    public void setStopShowLog(boolean stopShowLog) {
		this.stopShowLog = stopShowLog;
	}

	protected void prevSearchResult() {
        if (mSearchResult == null)
            return;
        if(mSearchResult.isEmpty()){
            mSearchResult = null;
            return;
        }
        mSearchResultIndex = mSearchResultIndex - 1;
        if (mSearchResultIndex < 0){
            mSearchResultIndex += mSearchResult.size();
        }
        updateSearchResultSelection();
    }
    protected void clearSearchResult() {
        itemDeleteAndInfo.setText("");
        mSearchResult = null;
        mSearchResultIndex = 0;
        mLastSearchedTerm = "";
        mScreenshotCanvas.redraw();
    }
    protected void nextSearchResult() {
        if (mSearchResult == null)
            return;
        if(mSearchResult.isEmpty()){
            mSearchResult = null;
            return;
        }
        mSearchResultIndex = (mSearchResultIndex + 1) % mSearchResult.size();
        updateSearchResultSelection();
    }

    private void updateSearchResultSelection() {
        updateTreeSelection(mSearchResult.get(mSearchResultIndex));
        itemDeleteAndInfo.setText("" + (mSearchResultIndex + 1) + "/"
                + mSearchResult.size());
    }

    private int getScaledSize(int size) {
        if (mScale == 1.0f) {
            return size;
        } else {
            return new Double(Math.floor((size * mScale))).intValue();
        }
    }

    private int getInverseScaledSize(int size) {
        if (mScale == 1.0f) {
            return size;
        } else {
            return new Double(Math.floor((size / mScale))).intValue();
        }
    }

    private void updateScreenshotTransformation() {
        Rectangle canvas = mScreenshotCanvas.getBounds();
        Rectangle image = mScreenshot.getBounds();
        float scaleX = (canvas.width - 2 * IMG_BORDER - 1) / (float) image.width;
        float scaleY = (canvas.height - 2 * IMG_BORDER - 1) / (float) image.height;

        // use the smaller scale here so that we can fit the entire screenshot
        mScale = Math.min(scaleX, scaleY);
        // calculate translation values to center the image on the canvas
        mDx = (canvas.width - getScaledSize(image.width) - IMG_BORDER * 2) / 2 + IMG_BORDER;
        mDy = (canvas.height - getScaledSize(image.height) - IMG_BORDER * 2) / 2 + IMG_BORDER;
    }

    private class AttributeTableEditingSupport extends EditingSupport {

        private TableViewer mViewer;

        public AttributeTableEditingSupport(TableViewer viewer) {
            super(viewer);
            mViewer = viewer;
        }

        @Override
        protected boolean canEdit(Object arg0) {
            return true;
        }

        @Override
        protected CellEditor getCellEditor(Object arg0) {
            return new TextCellEditor(mViewer.getTable());
        }

        @Override
        protected Object getValue(Object o) {
            return ((AttributePair) o).value;
        }

        @Override
        protected void setValue(Object arg0, Object arg1) {
        }
    }

    /**
     * Causes a redraw of the canvas.
     *
     * The drawing code of canvas will handle highlighted nodes and etc based on data
     * retrieved from Model
     */
    public void redrawScreenshot() {
        if (mScreenshot == null) {
            mStackLayout.topControl = mSetScreenshotComposite;
        } else {
            mStackLayout.topControl = mScreenshotCanvas;
        }
        mScreenshotComposite.layout();
        mScreenshotCanvas.redraw();
    }

    public void setInputHierarchy(Object input) {
        mTreeViewer.setInput(input);
    }

    public void loadAttributeTable() {
        // update the lower right corner table to show the attributes of the node
        mTableViewer.setInput(mModel.getSelectedNode().getAttributesArray());
    }
    
    public void expandAll() {
        mTreeViewer.expandAll();
        //System.out.println("item count:"+mTreeViewer.getTree().getItemCount());
    }
    
    public void updateTreeSelection(BasicTreeNode node) {
        mTreeViewer.setSelection(new StructuredSelection(node), true);
    }

    public void setModel(UiAutomatorModel model, File modelBackingFile, Image screenshot) {
        mModel = model;
        mModelFile = modelBackingFile;

        if (mScreenshot != null) {
            mScreenshot.dispose();
        }
        mScreenshot = screenshot;
        clearSearchResult();
        redrawScreenshot();
        // load xml into tree
        BasicTreeNode wrapper = new BasicTreeNode();
        // putting another root node on top of existing root node
        // because Tree seems to like to hide the root node
        wrapper.addChild(mModel.getXmlRootNode());
        setInputHierarchy(wrapper);
        mTreeViewer.getTree().setFocus();
        
        expandAll();

    }

    public boolean shouldShowNafNodes() {
        return mModel != null ? mModel.shouldShowNafNodes() : false;
    }

    public void toggleShowNaf() {
        if (mModel != null) {
            mModel.toggleShowNaf();
        }
    }

    public Image getScreenShot() {
        return mScreenshot;
    }

    public File getModelFile() {
        return mModelFile;
    }
    
    private void addRightMenu(Menu menu) {
    	
    	MenuItem attributionOperation = new MenuItem(menu, SWT.CASCADE);
    	attributionOperation.setText("Attribution operation");
    	addMenuForAttribution(attributionOperation);
    	
    	MenuItem keyOperation = new MenuItem(menu, SWT.CASCADE);
    	keyOperation.setText("Key operation");
    	addMenuForKeyOperation(keyOperation);
    	
    	MenuItem errorOperation = new MenuItem(menu, SWT.CASCADE);
    	errorOperation.setText("Error handle");
    	addMenuForErrorOperation(errorOperation);
    	
    	MenuItem logOperation = new MenuItem(menu, SWT.CASCADE);
    	logOperation.setText("Log");
    	addMenuForLogOperation(logOperation);
    	
    	MenuItem functionOperation = new MenuItem(menu, SWT.CASCADE);
    	functionOperation.setText("Function");
    	addMenuForFunctionOperation(functionOperation);
    	
    	MenuItem getStatusOperation = new MenuItem(menu, SWT.CASCADE);
    	getStatusOperation.setText("Get status");
    	addMenuForGetStateOperation(getStatusOperation);
    	
    	MenuItem shellOperation = new MenuItem(menu, SWT.CASCADE);
    	shellOperation.setText("Shell operation");
    	addMenuForShellOperation(shellOperation);
    	
    	MenuItem inputText = new MenuItem(menu, SWT.CASCADE);
    	inputText.setText("input text");
    	
    	inputText.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				inputTextToStyledText();
			}
    	});
    }
    
    public void inputTextToStyledText() {
    	InputDialog inputDialog = new InputDialog(UiAutomatorViewer.getApp().getShell(),
				"Please input string", //This is dialog's title
				"", //This is dialog's hint
				"", //Default value
				null);
		inputDialog.open();
		
		String text = inputDialog.getValue();
		
		if (text == null) {
			return;
		}
		
		for (int i=0; i<text.length(); i++) {
			
			char c=text.charAt(i);
			
			if (c == '*') {
				insertTextToStyledText("\r\ninput keyevent KEYCODE_NUMPAD_MULTIPLY\r\n");
			} else if (c == ' ') {
				insertTextToStyledText("\r\ninput keyevent KEYCODE_SPACE\r\n");
			} else if (c == '#') {
				insertTextToStyledText("\r\ninput keyevent KEYCODE_POUND\r\n");
			} else if (c == '`') {
				insertTextToStyledText("\r\ninput keyevent KEYCODE_GRAVE\r\n");
			} else if (c == '"') {
				insertTextToStyledText("\r\ninput text \"\r\n");
			} else {
				
				if (i>0) {
					
					char preC=text.charAt(i-1);
					if (preC == '*' || preC == ' '|| preC == '#' || preC == '`' || preC == '"') {
						insertTextToStyledText("input text "+c);
					} else {
						insertTextToStyledText(""+c);
					}
				} else {
					insertTextToStyledText("input text "+c);
				}
			}
		}
		insertTextToStyledText("\r\n");
    }
    
    public String insertAttrValue() {
    	
    	String text = getValueFromAttributionTable("text", 1);
		String resourceId = getValueFromAttributionTable("resource-id",1);
		String className = getValueFromAttributionTable("class", 1);
		String description = getValueFromAttributionTable("content-desc",1);
		String instance = getValueFromAttributionTable("index",2);
		
		if (text.equals("") && resourceId.equals("") && className.equals("") && description.equals("")) {
			
			MessageBox box = new MessageBox(getShell(), SWT.ICON_QUESTION|SWT.YES|SWT.NO);
			box.setText("Continue?");
			box.setMessage("text, resource-id, class, description are empty, you'd better do "
					+ "not use this attribution.\nClick Y to continue, otherwise will cancel.");
			int choice = box.open();
			if (choice == SWT.NO) {
				return null;
			}
		}
		
		//System.out.println("text contain result:"+text.contains("`"));
		
		String command = "TEXT=\""+text.replaceAll("`", "\\\\`")+"\""+";"+
				"RESOURCEID=\""+resourceId+"\""+";"+
				"CLASS=\""+className+"\""+";"+
				"CONTENTDESC=\""+description.replaceAll("`", "\\`")+"\""+";"+
				"INSTANCE=\""+instance+"\"";
		
		//System.out.println("command is:"+command);
		
		//insertTextToStyledText(command+"\r\n");
		
		return command;
    }
    
    public String insertAttrValue(String isOption) {
    	
    	String text = getValueFromAttributionTable("text", 1);
		String resourceId = getValueFromAttributionTable("resource-id", 1);
		String className = getValueFromAttributionTable("class", 1);
		String description = getValueFromAttributionTable("content-desc", 1);
		String instance = getValueFromAttributionTable("index", 2);
		String is = getValueFromAttributionTable(isOption, 1);
		
		if (text.equals("") && resourceId.equals("") && className.equals("") && description.equals("")) {
			
			MessageBox box = new MessageBox(getShell(), SWT.ICON_QUESTION|SWT.YES|SWT.NO);
			box.setText("Continue?");
			box.setMessage("text, resource-id, class, description are empty, you'd better do "
					+ "not use this attribution.\nClick Y to continue, otherwise will cancel.");
			int choice = box.open();
			if (choice == SWT.NO) {
				return null;
			}
			
		}
		
		//System.out.println("text contain result:"+text.contains("`"));
		
		String command = "TEXT=\""+text.replaceAll("`", "\\\\`")+"\""+";"+
				"RESOURCEID=\""+resourceId+"\""+";"+
				"CLASS=\""+className+"\""+";"+
				"CONTENTDESC=\""+description.replaceAll("`", "\\`")+"\""+";"+
				isOption.toUpperCase()+"=\""+is+"\""+";"+
				"INSTANCE=\""+instance+"\"";
		
		//insertTextToStyledText(command+"\r\n");
		
		return command;
    }
    
    public void waitMS(int ms) {
    	try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    private void attrIntelligent(String optionType) {
    	String str;
    	if (("ClickDoubleClickLongPressExist").contains(optionType)) {
    		str = insertAttrValue();
    	} else {
    		str = insertAttrValue(optionType);
    	}
    	
    	if (str == null) {
    		return;
    	}
    	
    	insertTextToStyledText(str+"\r\n");
    	
    	if (optionType.equals("Click")) {
    		
			insertTextToStyledText("ClickIntelligent"+"\r\n");
			//singleCommandRun(str+";"+"ClickIntelligent");
			
			UiAutomatorViewer.getApp().getXMLAction.setCommand(str+";"+"ClickIntelligent");
			UiAutomatorViewer.getApp().getXMLAction.run();
			
		} else if (optionType.equals("DoubleClick")) {
			
			insertTextToStyledText("DoubleClickIntelligent"+"\r\n");
			//singleCommandRun(str+";"+"DoubleClickIntelligent");
			UiAutomatorViewer.getApp().getXMLAction.setCommand(str+";"+"DoubleClickIntelligent");
			UiAutomatorViewer.getApp().getXMLAction.run();
			//refreshResultFolder();
		} else if (optionType.equals("LongPress")) {
			
			insertTextToStyledText("LongPressIntelligent"+"\r\n");
			//singleCommandRun(str+";"+"LongPressIntelligent");
			UiAutomatorViewer.getApp().getXMLAction.setCommand(str+";"+"LongPressIntelligent");
			UiAutomatorViewer.getApp().getXMLAction.run();
			//refreshResultFolder();
		} else if (optionType.equals("Exist")) {
			
			insertTextToStyledText("ExistIntelligent\r\n");
			insertTextToStyledText("if test $centerX -ne -1\r\n");
			insertTextToStyledText("then\r\n");
			insertTextToStyledText("echo exist, do something\r\n");
			insertTextToStyledText("\r\n");
			insertTextToStyledText("else\r\n");
			insertTextToStyledText("echo not exist, do something\r\n");
			insertTextToStyledText("\r\n");
			insertTextToStyledText("fi\r\n");
		} else {

			insertTextToStyledText("ExistIntelligent\r\n");
			insertTextToStyledText("if test $centerX -ne -1\r\n");
			insertTextToStyledText("then\r\n");
			insertTextToStyledText("echo exist, do something\r\n");
			insertTextToStyledText("\r\n");
			insertTextToStyledText("else\r\n");
			insertTextToStyledText("echo not exist, do something\r\n");
			insertTextToStyledText("\r\n");
			insertTextToStyledText("fi\r\n");
		}
    	
    }
    
    private void addMenuForAttribution(MenuItem attributionOperation) {
    	
    	Menu attributionOperationMenu = new Menu(attributionOperation);
    	attributionOperation.setMenu(attributionOperationMenu);
    	
    	//MenuItem attributionClick = new MenuItem(attributionOperationMenu, SWT.CASCADE);
    	//attributionClick.setText("Click");
    	//addMenuForAttributionOperation(attributionClick);
    	
    	MenuItem attributionClickIntelligent = new MenuItem(attributionOperationMenu, SWT.CASCADE);
    	attributionClickIntelligent.setText("ClickIntelligent");
    	attributionClickIntelligent.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				attrIntelligent("Click");
			}    		
    	});
    	
    	MenuItem attributionDoubleClickIntelligent = new MenuItem(attributionOperationMenu, SWT.CASCADE);
    	attributionDoubleClickIntelligent.setText("DoubleClickIntelligent");
    	attributionDoubleClickIntelligent.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				attrIntelligent("DoubleClick");
				
			}
    	});
    	
    	//MenuItem attributionLongPress = new MenuItem(attributionOperationMenu, SWT.CASCADE);
    	//attributionLongPress.setText("LongPress");
    	//addMenuForAttributionOperation(attributionLongPress);
    	
    	MenuItem attributionLongPressIntelligent = new MenuItem(attributionOperationMenu, SWT.CASCADE);
    	attributionLongPressIntelligent.setText("LongPressIntelligent");
    	attributionLongPressIntelligent.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				attrIntelligent("LongPress");
			}    		
    	});

    	MenuItem attributionSwipe = new MenuItem(attributionOperationMenu, SWT.CASCADE);
    	attributionSwipe.setText("Swipe");
    	addMenuForAttributionSwipe(attributionSwipe);
    	
    	MenuItem attributionDrag = new MenuItem(attributionOperationMenu, SWT.CASCADE);
    	attributionDrag.setText("Drag");
    	addMenuForAttributionDrag(attributionDrag);
    	
    	MenuItem attributionPinch = new MenuItem(attributionOperationMenu, SWT.CASCADE);
    	attributionPinch.setText("Pinch");
    	addMenuForAttributionPinch(attributionPinch);
    	
    	MenuItem attributionExistIntelligent = new MenuItem(attributionOperationMenu, SWT.CASCADE);
    	attributionExistIntelligent.setText("ExistIntelligent");
    	attributionExistIntelligent.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				attrIntelligent("Exist");
			}    		
    	});
    	
    	MenuItem attributionFind = new MenuItem(attributionOperationMenu, SWT.CASCADE);
    	attributionFind.setText("FindObjFromListByResourceId");
    	addMenuForAttributionFind(attributionFind);
    	
    	MenuItem getTextByAttr = new MenuItem(attributionOperationMenu, SWT.CASCADE);
    	getTextByAttr.setText("GetTextByAttribution");
    	addMenuForAttributionOperation(getTextByAttr);
    	
    	MenuItem rotateOperation = new MenuItem(attributionOperationMenu, SWT.CASCADE);
    	rotateOperation.setText("Rotate");
    	addMenuForRotateOperation(rotateOperation);
    }
    
    private void addMenuForAttributionOperation(MenuItem sub) {
    	
    	Menu option = new Menu(sub);
    	sub.setMenu(option);
    	
    	final String operation = sub.getText();
    	
    	MenuItem text = new MenuItem(option, SWT.CASCADE);
    	text.setText("text");
    	
    	text.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				String textAttr = getValueFromAttributionTable("text", 1);
				String instance = getValueFromAttributionTable("text", 2);
				
				if (textAttr.equals("")) {
					MessageDialog.openInformation(null, "Invalid operation", "Text is empty.");
					return;
				}
	    		
		    	if (operation.equals("Click")) {
		    		if (textAttr.contains("'")) {
		    			insertTextToStyledText("ClickByText "+"\""+textAttr+"\" "+instance+"\r\n");
		    		} else {
		    			insertTextToStyledText("ClickByText "+"'"+textAttr+"' "+instance+"\r\n");
		    		}
		    		
		    		//singleCommandRun("ClickByText"+" '"+textAttr+"' "+instance+"");
		    		UiAutomatorViewer.getApp().getXMLAction.setCommand("ClickByText"+" '"+textAttr+"' "+instance+"");
					//update screen and attribution table
					UiAutomatorViewer.getApp().getXMLAction.run();
					//refreshResultFolder();
		    	} else if (operation.equals("LongPress")) {
		    		if (textAttr.contains("'")) {
		    			insertTextToStyledText("LongPressByText "+"\""+textAttr+"\" "+instance+"\r\n");
		    		} else {
		    			insertTextToStyledText("LongPressByText "+"'"+textAttr+"' "+instance+"\r\n");
		    		}
		    		
		    		//insertTextToStyledText("LongPressByText "+"'"+getValueFromAttributionTable("text", 1)+"' 1\n");
		    		//singleCommandRun("LongPressByText"+" '"+textAttr+"' "+instance+"");
		    		UiAutomatorViewer.getApp().getXMLAction.setCommand("LongPressByText"+" '"+textAttr+"' "+instance+"");
		    		UiAutomatorViewer.getApp().getXMLAction.run();
		    		//refreshResultFolder();
		    	} else if (operation.equals("Hold")) {
		    		if (textAttr.contains("'")) {
		    			insertTextToStyledText("HoldByText "+"\""+textAttr+"\" "+instance+"\r\n");
		    		} else {
		    			insertTextToStyledText("HoldByText "+"'"+textAttr+"' "+instance+"\r\n");
		    		}
		    		
		    		//insertTextToStyledText("HoldByText "+"'"+getValueFromAttributionTable("text", 1)+"' 1\n");
		    		//singleCommandRun("HoldByText"+" '"+textAttr+"' "+instance+"");
		    		UiAutomatorViewer.getApp().getXMLAction.setCommand("HoldByText"+" '"+textAttr+"' "+instance+"");
		    		UiAutomatorViewer.getApp().getXMLAction.run();
		    		//refreshResultFolder();
		    	} else if (operation.equals("Drag")) {
		    		insertTextToStyledText("Drag "+"\""+textAttr+"\"\r\n");		    		
		    	} else if (operation.equals("Swipe")) {
		    		insertTextToStyledText("SwipeByAttribution "+"\""+textAttr+"\"\r\n");
		    	} else if (operation.equals("GetTextByAttribution")) {
		    		insertTextToStyledText("GetTextByAttribution "+"'"+textAttr+"\" "+instance+"\r\n");		    		
		    	}
			}
    	});

    	MenuItem resourceId = new MenuItem(option, SWT.CASCADE);
    	resourceId.setText("resource-id");
    	
    	resourceId.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				String resourceIdAttr = getValueFromAttributionTable("resource-id",1);
				String instance = getValueFromAttributionTable("resource-id", 2);
				
				if (resourceIdAttr.equals("")) {
					MessageDialog.openInformation(null, "Invalid operation", "ResourceId is empty.");
					return;
				}
	    		
		    	if (operation.equals("Click")) {
		    		insertTextToStyledText("ClickByResourceId "+"\""+resourceIdAttr+"\" "+instance+"\r\n");
		    		//singleCommandRun("ClickByResourceId"+" \""+resourceIdAttr+"\" "+instance+"");
		    		UiAutomatorViewer.getApp().getXMLAction.setCommand("ClickByResourceId"+" \""+resourceIdAttr+"\" "+instance+"");
					UiAutomatorViewer.getApp().getXMLAction.run();
					//refreshResultFolder();
		    	} else if (operation.equals("LongPress")) {
		    		insertTextToStyledText("LongPressByResourceId "+"\""+resourceIdAttr+"\" "+instance+"\r\n");
		    		//singleCommandRun("LongPressByResourceId"+" \""+resourceIdAttr+"\" "+instance+"");
		    		UiAutomatorViewer.getApp().getXMLAction.setCommand("LongPressByResourceId"+" \""+resourceIdAttr+"\" "+instance+"");
					UiAutomatorViewer.getApp().getXMLAction.run();
					//refreshResultFolder();
		    	} else if (operation.equals("Hold")) {
		    		insertTextToStyledText("HoldByResourceId "+"\""+resourceIdAttr+"\" "+instance+"\r\n");
		    		//singleCommandRun("HoldByResourceId"+" \""+resourceIdAttr+"\" "+instance+"");
		    		UiAutomatorViewer.getApp().getXMLAction.setCommand("HoldByResourceId"+" \""+resourceIdAttr+"\" "+instance+"");
		    		UiAutomatorViewer.getApp().getXMLAction.run();
		    		//refreshResultFolder();
		    	} else if (operation.equals("Drag")) {
		    		insertTextToStyledText("Drag "+"\""+resourceIdAttr+"\"\r\n");		    		
		    	} else if (operation.equals("Swipe")) {
		    		insertTextToStyledText("SwipeByResourceId "+"\""+resourceIdAttr+"\"\r\n");
		    	} else if (operation.equals("GetTextByAttribution")) {
		    		insertTextToStyledText("GetTextByAttribution "+"'"+resourceIdAttr+"\" "+instance+"\r\n");		    		
		    	}
			}
    	});
    	
    	MenuItem content = new MenuItem(option, SWT.CASCADE);
    	content.setText("content-desc");
    	
    	content.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				String description = getValueFromAttributionTable("content-desc",1);
				String instance = getValueFromAttributionTable("content-desc", 2);
				
				if (description.equals("")) {
					MessageDialog.openInformation(null, "Invalid operation", "Description is empty.");
					return;
				}
	    		
		    	if (operation.equals("Click")) {
		    		insertTextToStyledText("ClickByDescription "+"\""+description+"\" "+instance+"\r\n");
		    		//singleCommandRun("ClickByDescription"+" \""+description+"\" "+instance+"");
		    		UiAutomatorViewer.getApp().getXMLAction.setCommand("ClickByDescription"+" \""+description+"\" "+instance+"");
					UiAutomatorViewer.getApp().getXMLAction.run();
					//refreshResultFolder();
		    	} else if (operation.equals("LongPress")) {
		    		insertTextToStyledText("LongPressByDescription "+"\""+description+"\" "+instance+"\r\n");
		    		//singleCommandRun("LongPressByDescription"+" \""+description+"\" "+instance+"");
		    		UiAutomatorViewer.getApp().getXMLAction.setCommand("LongPressByDescription"+" \""+description+"\" "+instance+"");
					UiAutomatorViewer.getApp().getXMLAction.run();
					//refreshResultFolder();
		    	} else if (operation.equals("Hold")) {
		    		insertTextToStyledText("HoldByDescription "+"\""+description+"\" "+instance+"\r\n");
		    		//singleCommandRun("HoldByDescription"+" \""+description+"\" "+instance+"");
		    		UiAutomatorViewer.getApp().getXMLAction.setCommand("HoldByDescription"+" \""+description+"\" "+instance+"");
		    		UiAutomatorViewer.getApp().getXMLAction.run();
		    		//refreshResultFolder();
		    	} else if (operation.equals("Drag")) {
		    		insertTextToStyledText("Drag "+"\""+description+"\"\r\n");
		    	} else if (operation.equals("Swipe")) {
		    		insertTextToStyledText("Swipe "+"\""+description+"\"\r\n");
		    	} else if (operation.equals("GetTextByAttribution")) {
		    		insertTextToStyledText("GetTextByAttribution "+"'"+description+"\" "+instance+"\r\n");
		    	}
			}
    	});
    }
    
    public void attrSwipeOption(String direction) {
    	String resourceId = getValueFromAttributionTable("resource-id",1);
    	String instance = getValueFromAttributionTable("resource-id", 2);
    	if (resourceId.equals("")) {
			MessageDialog.openInformation(null, "Invalid operation", "resource-id is empty.");
			return;
		}
    	
    	if (direction.equals("Up")|direction.equals("Down")|direction.equals("Left")|direction.equals("Right")) {
    		
			insertTextToStyledText("SwipeByAttribution "+"\""+resourceId+"\" "+instance+" "+direction+"\r\n");
			//singleCommandRun("SwipeByAttribution "+"\""+resourceId+"\" "+instance+" "+direction);
			UiAutomatorViewer.getApp().getXMLAction.setCommand("SwipeByAttribution "+"\""+resourceId+"\" "+instance+" "+direction);
			UiAutomatorViewer.getApp().getXMLAction.run();
			//refreshResultFolder();
    	}
    }
    
    private void addMenuForAttributionSwipe(MenuItem attributionSwipe) {
    	
    	Menu option = new Menu(attributionSwipe);
    	attributionSwipe.setMenu(option);
    	
    	MenuItem direUp = new MenuItem(option, SWT.CASCADE);
    	direUp.setText("Up");
    	
    	direUp.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				attrSwipeOption("Up");
			}
    	});
    	
    	MenuItem direDown = new MenuItem(option, SWT.CASCADE);
    	direDown.setText("Down");
    	
    	direDown.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				attrSwipeOption("Down");
			}
    	});
    	
    	MenuItem direLeft = new MenuItem(option, SWT.CASCADE);
    	direLeft.setText("Left");
    	
    	direLeft.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				attrSwipeOption("Left");
			}
    	});
    	
    	MenuItem direRight = new MenuItem(option, SWT.CASCADE);
    	direRight.setText("Right");
    	
    	direRight.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				attrSwipeOption("Right");
			}
    	});
    }
    
    private void attrDragTo() {
    	String text = getValueFromAttributionTable("text", 1);
		String resourceId = getValueFromAttributionTable("resource-id",1);
		String clazz = getValueFromAttributionTable("class",1);
		String description = getValueFromAttributionTable("content-desc",1);
		String instance = getValueFromAttributionTable("index",2);
		
		String command = "TEXTTWO=\""+text.replaceAll("`", "\\\\`")+"\""+";"+
				"RESOURCEIDTWO=\""+resourceId+"\""+";"+
				"CLASSTWO=\""+clazz+"\""+";"+
				"CONTENTDESCTWO=\""+description.replaceAll("`", "\\`")+"\""+";"+
				"INSTANCETWO=\""+instance+"\"";
		
		insertTextToStyledText(command+"\r\n");
		insertTextToStyledText("DragIntelligent"+"\r\n");
		
		//singleCommandRun(dragFrom+";"+command+";"+"DragIntelligent");
		//singleCommandRun(command+";"+"DragTo");
		UiAutomatorViewer.getApp().getXMLAction.setCommand(command+";"+"DragTo");
		
		UiAutomatorViewer.getApp().getXMLAction.run();
		//refreshResultFolder();
    }
    
    private void addMenuForAttributionDrag(MenuItem attributionDrag) {
    	
    	Menu option = new Menu(attributionDrag);
    	attributionDrag.setMenu(option);
    	
    	MenuItem from = new MenuItem(option, SWT.CASCADE);
    	from.setText("From");
    	
    	MenuItem to = new MenuItem(option, SWT.CASCADE);
    	to.setText("To");
    	
    	from.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				sendOption = "DragFrom";
				sendOption();
			}
    	});
    	
    	to.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				attrDragTo();
			}
    	});
    }
    
    private void attrPinch(String action) {
    	
    	String resourceId = getValueFromAttributionTable("resource-id",1);
		//String instance = getValueFromAttributionTable("resource-id",2);
    	
    	if (resourceId.equals("")) {
			MessageDialog.openInformation(null, "Invalid operation", "resource-id is empty.");
			return;
		}
    	
    	if (action.equals("In")) {
    		insertTextToStyledText("PinchIn "+"\""+resourceId+"\" "+"\r\n");
    		//singleCommandRun("PinchIn "+"\""+resourceId+"\" ");
    		UiAutomatorViewer.getApp().getXMLAction.setCommand("PinchIn "+"\""+resourceId+"\" ");
    	} else if (action.equals("Out")) {
    		insertTextToStyledText("PinchOut "+"\""+resourceId+"\" "+"\r\n");
    		//singleCommandRun("PinchOut "+"\""+resourceId+"\" ");
    		UiAutomatorViewer.getApp().getXMLAction.setCommand("PinchOut "+"\""+resourceId+"\" ");
    	}
		
		UiAutomatorViewer.getApp().getXMLAction.run();
		//refreshResultFolder();
    }
    
    private void addMenuForAttributionPinch(MenuItem attributionPinch) {
    	
    	Menu option = new Menu(attributionPinch);
    	attributionPinch.setMenu(option);
    	
    	MenuItem pinchIn = new MenuItem(option, SWT.CASCADE);
    	pinchIn.setText("PinchIn");
    	
    	MenuItem pinchOut = new MenuItem(option, SWT.CASCADE);
    	pinchOut.setText("PinchOut");
    	
    	pinchIn.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				attrPinch("In");
			}
    	});
    	
    	pinchOut.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				attrPinch("Out");
			}
    	});
    }
    
    private void findChildViaParentAttr() {
    	String text = getValueFromAttributionTable("text", 1).replaceAll("`", "\\\\`");
		String instance = getValueFromAttributionTable("text", 2);
		if (text.equals("")) {
			MessageDialog.openInformation(null, "Invalid operation", "Text is empty.");
			return;
		}
		
		insertTextToStyledText("FindObjFromListByResourceId "+"\"ResourceId\" \""+text+"\" "+instance+"\r\n");
		
    }
    
    private void addMenuForAttributionFind (MenuItem attributionFind) {
    	
    	attributionFind.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				findChildViaParentAttr();
			}
    	});
    }
    
    private void addMenuForRotateOperation(MenuItem rotateOperation) {
    	
    	Menu option = new Menu(rotateOperation);
    	rotateOperation.setMenu(option);
    	
    	MenuItem leftLandscapeMode = new MenuItem(option, SWT.CASCADE);
    	leftLandscapeMode.setText("Left");
    	
    	MenuItem rightLandscapeMode = new MenuItem(option, SWT.CASCADE);
    	rightLandscapeMode.setText("Right");
    	
    	MenuItem portraitMode = new MenuItem(option, SWT.CASCADE);
    	portraitMode.setText("Nature");
    	
    	leftLandscapeMode.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("Rotate Left\r\n");
				//UiAutomatorViewer.getApp().getXMLAction.run();
			}
    	});
    	
    	rightLandscapeMode.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("Rotate Right\r\n");
				//UiAutomatorViewer.getApp().getXMLAction.run();
			}
    	});
    	
    	portraitMode.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("Rotate Nature\r\n");
				//UiAutomatorViewer.getApp().getXMLAction.run();
			}
    	});
    }
    
    private void hardkeyOperation(String key) {
    	switch (key) {
    	case "Back":
    		insertTextToStyledText("Back\r\n");
			UiAutomatorViewer.getApp().getXMLAction.setCommand("Back");
			break;
    	case "Home":
    		insertTextToStyledText("Home\r\n");
			UiAutomatorViewer.getApp().getXMLAction.setCommand("Home");
			break;
    	case "Menu":
    		insertTextToStyledText("Menu\r\n");
			UiAutomatorViewer.getApp().getXMLAction.setCommand("Menu");
			break;
    	case "LongPressMenu":
    		insertTextToStyledText("LongPressMenu\r\n");
			UiAutomatorViewer.getApp().getXMLAction.setCommand("LongPressMenu");
			break;
    	case "Power":
    		insertTextToStyledText("Power\r\n");
			UiAutomatorViewer.getApp().getXMLAction.setCommand("Power");
			break;
    	case "LongPressPower":
    		insertTextToStyledText("LongPressPower\r\n");
			UiAutomatorViewer.getApp().getXMLAction.setCommand("LongPressPower");
			break;
    	case "Recent":
    		insertTextToStyledText("Recent\r\n");
			UiAutomatorViewer.getApp().getXMLAction.setCommand("Recent");
			break;
    	case "Hook":
    		insertTextToStyledText("Hook\r\n");
    		UiAutomatorViewer.getApp().getXMLAction.setCommand("Hook");
			break;
    	case "VolumeUp":
    		insertTextToStyledText("VolumeUp\r\n");
			UiAutomatorViewer.getApp().getXMLAction.setCommand("VolumeUp");
			break;
    	case "VolumeDown":
    		insertTextToStyledText("VolumeDown\r\n");
			UiAutomatorViewer.getApp().getXMLAction.setCommand("VolumeDown");
    	case "Power+VolumeDown":
    		insertTextToStyledText("Power+VolumeDown\r\n");
			UiAutomatorViewer.getApp().getXMLAction.setCommand("Power+VolumeDown");
    	}
    	if (!key.startsWith("Volume")) {
    		UiAutomatorViewer.getApp().getXMLAction.run();
    		//refreshResultFolder();
    	}
    }
    
    private void addMenuForKeyOperation(MenuItem keyOperaton) {
		
    	Menu option = new Menu(keyOperaton);
    	keyOperaton.setMenu(option);
    	
    	MenuItem backKey = new MenuItem(option, SWT.CASCADE);
    	backKey.setText("Back");
    	
    	backKey.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				hardkeyOperation("Back");
			}
    	});
    	
    	MenuItem homeKey = new MenuItem(option, SWT.CASCADE);
    	homeKey.setText("Home");
    	
    	homeKey.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				hardkeyOperation("Home");
			}
		});
    	
    	MenuItem menuKey = new MenuItem(option, SWT.CASCADE);
    	menuKey.setText("Menu");
    	
    	menuKey.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				hardkeyOperation("Menu");
			}
		});
    	
    	MenuItem recentKey = new MenuItem(option, SWT.CASCADE);
    	recentKey.setText("Recent");
    	
    	recentKey.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				hardkeyOperation("Recent");
			}
		});
    	
    	MenuItem powerKey = new MenuItem(option, SWT.CASCADE);
    	powerKey.setText("Power");
    	
    	powerKey.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				hardkeyOperation("Power");
			}
		});
    	
    	MenuItem LongPressPower = new MenuItem(option, SWT.CASCADE);
    	LongPressPower.setText("LongPressPower");
    	
    	LongPressPower.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				hardkeyOperation("LongPressPower");
			}
		});
    	
    	MenuItem hookKey = new MenuItem(option, SWT.CASCADE);
    	hookKey.setText("Hook");
    	
    	hookKey.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				hardkeyOperation("Hook");
			}
		});
    	
    	MenuItem volUpKey = new MenuItem(option, SWT.CASCADE);
    	volUpKey.setText("VolumeUp");
    	
    	volUpKey.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				hardkeyOperation("VolumeUp");
			}
		});
    	
    	MenuItem voldownKey = new MenuItem(option, SWT.CASCADE);
    	voldownKey.setText("VolumeDown");
    	
    	voldownKey.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				hardkeyOperation("VolumeDown");
			}
		});
	}
    
    private void addMenuForErrorOperation(MenuItem errorOperation) {
		
    	Menu option = new Menu(errorOperation);
    	errorOperation.setMenu(option);
    	
    	MenuItem backToHome = new MenuItem(option, SWT.CASCADE);
    	backToHome.setText("BackToHome");
    	
    	backToHome.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("BackToHome\r\n");
			}
    	});
    	
    	MenuItem crashHandle = new MenuItem(option, SWT.CASCADE);
    	crashHandle.setText("CrashHandle");
    	
    	crashHandle.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("CrashHandle\r\n");
			}
    	});
    	
    	MenuItem screenShot = new MenuItem(option, SWT.CASCADE);
    	screenShot.setText("Screenshot");
    	
    	screenShot.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("Screenshot\r\n");
			}
    	});
    	
	}
    
    private void addMenuForLogOperation(MenuItem logOperation) {
    	
    	Menu option = new Menu(logOperation);
    	logOperation.setMenu(option);
    	
    	MenuItem logPass = new MenuItem(option, SWT.CASCADE);
    	logPass.setText("LogPass");
    	
    	logPass.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("LogPass\r\n");
			}
    	});
    	
    	MenuItem logFail = new MenuItem(option, SWT.CASCADE);
    	logFail.setText("LogFail");
    	
    	logFail.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("LogFail\r\n");
			}
    	});
    }
    
    private void addMenuForFunctionOperation(MenuItem functionOperation) {
    	
    	Menu option = new Menu(functionOperation);
    	functionOperation.setMenu(option);
    	
    	MenuItem Launcher = new MenuItem(option, SWT.CASCADE);
    	Launcher.setText("Launcher");
    	
    	MenuItem Phone = new MenuItem(option, SWT.CASCADE);
    	Phone.setText("Phone");
    	
    	MenuItem systemUi = new MenuItem(option, SWT.CASCADE);
    	systemUi.setText("SystemUI");
    	
    	Menu submenu;
    	submenu = new Menu(Launcher);
    	Launcher.setMenu(submenu);

    	MenuItem addApp = new MenuItem(submenu, SWT.CASCADE);
    	addApp.setText("AddApp");

    	MenuItem addWidget = new MenuItem(submenu, SWT.CASCADE);
    	addWidget.setText("AddWidget");

    	MenuItem deleteWidget = new MenuItem(submenu, SWT.CASCADE);
    	deleteWidget.setText("DeleteWidget");
    	
    	MenuItem deleteShortcut = new MenuItem(submenu, SWT.CASCADE);
    	deleteShortcut.setText("DeleteShortcut");
    	
    	MenuItem launchAP = new MenuItem(submenu, SWT.CASCADE);
    	launchAP.setText("LaunchAP");
    	
    	launchAP.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("LaunchAP \"APName\"\r\n");
			}
    	});
    	
    	addWidget.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("AddWidget \"widgetname\" 1 \r\n");
			}
    	});
    	
    	addApp.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("AddApp \"apptname\"\r\n");
			}
    	});
    	
    	deleteWidget.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("DeleteWidget\r\n");
			}
    	});
    	
    	deleteShortcut.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("DeleteShortcut\r\n");
			}
    	});
    	
    	submenu = new Menu(Phone);
    	Phone.setMenu(submenu);
    	
    	MenuItem inputPhoneNumber = new MenuItem(submenu, SWT.CASCADE);
    	inputPhoneNumber.setText("InputPhoneNumber");

    	inputPhoneNumber.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("InputPhoneNumber \"PhoneNumber\"\r\n");
			}
    	});
    	
    	submenu = new Menu(systemUi);
    	systemUi.setMenu(submenu);

    	MenuItem notification = new MenuItem(submenu, SWT.CASCADE);
    	notification.setText("Notification");
    	
    	MenuItem unlockDefault = new MenuItem(submenu, SWT.CASCADE);
    	unlockDefault.setText("Unlock");

    	unlockDefault.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("Unlock\r\n");
			}
    	});
    	
    	notification.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("Notification\r\n");
			}
    	});
    }
    
    private void addMenuForGetStateOperation(MenuItem getStatusOperation) {
    	
    	Menu option = new Menu(getStatusOperation);
    	getStatusOperation.setMenu(option);
    	
    	MenuItem getUSS = new MenuItem(option, SWT.CASCADE);
    	getUSS.setText("GetMemory(procrank)");
    	
    	getUSS.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("GetAPUss\r\n");
			}
    	});
    	
    	MenuItem getMemoryUsage = new MenuItem(option, SWT.CASCADE);
    	getMemoryUsage.setText("GetMemoryUsage(df)");
    	
    	getMemoryUsage.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("GetMemoryUsage\r\n");
			}
    	});
    	
    	MenuItem getBattery = new MenuItem(option, SWT.CASCADE);
    	getBattery.setText("GetBattery");
    	
    	getBattery.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("GetBattery\r\n");
			}
    	});
    	
    	MenuItem getCPUFreq = new MenuItem(option, SWT.CASCADE);
    	getCPUFreq.setText("GetCPUFreq");
    	
    	getCPUFreq.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("GetCPUFreq\r\n");
			}
    	});
    	
    	MenuItem getCPUUsage = new MenuItem(option, SWT.CASCADE);
    	getCPUUsage.setText("GetCPUUsage");
    	
    	getCPUUsage.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("GetCPUUsage\r\n");
			}
    	});
    	
    	MenuItem getSignalStrength = new MenuItem(option, SWT.CASCADE);
    	getSignalStrength.setText("GetSignalStrength");
    	
    	getSignalStrength.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				insertTextToStyledText("GetSignalStrength\r\n");
			}
    	});
    }
    
    private void addMenuForShellOperation(MenuItem shellOperation) {
    	Menu option = new Menu(shellOperation);
    	shellOperation.setMenu(option);
    	
    	//MenuItem ifOperation = new MenuItem(option, SWT.CASCADE);
    	//ifOperation.setText("if");
    	
    	MenuItem forOperation = new MenuItem(option, SWT.CASCADE);
    	forOperation.setText("for");
    	
    	forOperation.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				sendOption = forOperation.getText();
				sendOption();
			}
    	});
    	
    	MenuItem commentOperation = new MenuItem(option, SWT.CASCADE);
		commentOperation.setText("comment");
		
		commentOperation.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				sendOption = commentOperation.getText();
				sendOption();
			}
    	});
    	
    	MenuItem uncommentOperation = new MenuItem(option, SWT.CASCADE);
    	uncommentOperation.setText("uncomment");
    	
    	uncommentOperation.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				sendOption = uncommentOperation.getText();
				sendOption();
				
			}
    	});
    	
    	MenuItem sleepOperation = new MenuItem(option, SWT.CASCADE);
    	sleepOperation.setText("Wait");
    	
    	sleepOperation.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				sendOption = sleepOperation.getText();
				sendOption();
			}
    	});
    	
    }
    
    public void insertTextToStyledText(String text) {
    	
    	if (getSelectedSourceViewer()==null) {
    		return;
    	}
    	
    	stringBuffer.delete(0, stringBuffer.length());
		stringBuffer.append(text);
		getSelectedSourceViewer().insert(stringBuffer.toString());
		getSelectedSourceViewer().setSelection(
				getSelectedSourceViewer().getCaretOffset()+stringBuffer.length(),
				getSelectedSourceViewer().getCaretOffset()+stringBuffer.length());
    }
    
    /*
    public void singleCommandRun(String command) {
    	
    	
//    	CountDownLatch result=null;
//    	try {
//    		System.out.println("command: "+
//    				"source "+PushTemplateAction.shellTemplatePath+";"+command);
//			UiAutomatorViewer.getApp().getChoosedDevice().executeShellCommand("source "+
//    		PushTemplateAction.shellTemplatePath+";"+command,
//					new CollectingOutputReceiver(result));
//		} catch (TimeoutException | AdbCommandRejectedException
//				| ShellCommandUnresponsiveException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

    	
    	DebugAction.debug(command);
    	waitMS(1000);
    	for (int i=0; i<8; i++) {
    		if (!( new StopAction().getDebugScriptProcessId(
    				UiAutomatorViewer.getApp().getChoosedDevice().toString()).equals("-1") )) {
    			waitMS(1000);
    		}
    	}
    	
    }
    */
    
    public String getValueFromAttributionTable(String type, int ver) {
    	
    	if (mTableViewer.getTable().getItemCount() < 1) {
    		return "";
    	}
    	
    	//for (int i=0; i<mTableViewer.getTable().getItemCount(); i++) {
    	//	System.out.println(mTableViewer.getTable().getItem(i).getText(0));
    	//}
    	
    	for (int i=0; i<mTableViewer.getTable().getItemCount(); i++) {
    		if (type.equals(mTableViewer.getTable().getItem(i).getText(0))) {
    			return mTableViewer.getTable().getItem(i).getText(ver);
    		}
    	}
    	
    	return "";
    }
    
    public void switchTableVisible() {
	    boolean visible = middleRightBase.getVisible();
	    visible = !visible;
	    
	    middleRightBase.setVisible(visible);
	    middleRightBase.getParent().layout();
    }
    
    public void switchTreeVisible() {
    	boolean visible = upperRightBase.getVisible();
	    visible = !visible;
	    
	    upperRightBase.setVisible(visible);
	    upperRightBase.getParent().layout();
    }
    
    public void sendOption() {
    	
    	//System.out.println(sendOption);
    	
    	if (getSelectedSourceViewer() == null) {
			return;
		}
    	
    	if (sendOption.equals("Click")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("DoubleClick")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("LongPress")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("Exist")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("Find")) {
    		findChildViaParentAttr();
    	} else if (sendOption.equals("Up")) {
    		attrSwipeOption(sendOption);
    	} else if (sendOption.equals("Down")) {
    		attrSwipeOption(sendOption);
    	} else if (sendOption.equals("Left")) {
    		attrSwipeOption(sendOption);
    	} else if (sendOption.equals("Right")) {
    		attrSwipeOption(sendOption);
    	} else if (sendOption.equals("Rotate Left")) {
    		insertTextToStyledText(sendOption+"\r\n");
    	} else if (sendOption.equals("Rotate Right")) {
    		insertTextToStyledText(sendOption+"\r\n");
    	} else if (sendOption.equals("Rotate Nature")) {
    		insertTextToStyledText(sendOption+"\r\n");
    	} else if (sendOption.equals("DragFrom")) {
    		dragFrom = insertAttrValue();
    		if (dragFrom == null) {
        		return;
        	}
    		insertTextToStyledText(dragFrom+"\r\n");
			UiAutomatorViewer.getApp().getXMLAction.setCommand(dragFrom+";"+"DragFrom");
			UiAutomatorViewer.getApp().getXMLAction.run();
    	} else if (sendOption.equals("DragTo")) {
    		attrDragTo();
    	} else if (sendOption.equals("In")) {
    		attrPinch(sendOption);
    	} else if (sendOption.equals("Out")) {
    		attrPinch(sendOption);
    	} else if (sendOption.equals("Back")) {
    		hardkeyOperation(sendOption);
    	} else if (sendOption.equals("Menu")) {
    		hardkeyOperation(sendOption);
    	} else if (sendOption.equals("LongPressMenu")) {
    		hardkeyOperation(sendOption);
    	} else if (sendOption.equals("Home")) {
    		hardkeyOperation(sendOption);
    	} else if (sendOption.equals("Power")) {
    		hardkeyOperation(sendOption);
    	} else if (sendOption.equals("LongPressPower")) {
    		hardkeyOperation(sendOption);
    	} else if (sendOption.equals("Hook")) {
    		hardkeyOperation(sendOption);
    	} else if (sendOption.equals("VolumeDown")) {
    		hardkeyOperation(sendOption);
    	} else if (sendOption.equals("VolumeUp")) {
    		hardkeyOperation(sendOption);
    	} else if (sendOption.equals("Power+VolumeDown")) {
    		hardkeyOperation(sendOption);
    	} else if (sendOption.equals("BackToHome")) {
    		insertTextToStyledText(sendOption+"\r\n");
    	} else if (sendOption.equals("CrashHandle")) {
    		insertTextToStyledText(sendOption+"\r\n");
    	} else if (sendOption.equals("DoubleClick")) {
    		insertTextToStyledText(sendOption+"\r\n");
    	} else if (sendOption.equals("Screenshot")) {
    		insertTextToStyledText(sendOption+"\r\n");
    	} else if (sendOption.equals("GetDeviceInfo")) {
    		insertTextToStyledText(sendOption+"\r\n");
    	} else if (sendOption.equals("LogPass")) {
    		insertTextToStyledText(sendOption+"\r\n");
    	} else if (sendOption.equals("for")) {
    		for (int i=1; i<200; i++) {
				
				if (getSelectedSourceViewer().getText().contains("for i"+i)) {
					continue;
				} else {
					
					insertTextToStyledText("for i"+i+" in $(busybox seq 300)\r\n");
					insertTextToStyledText("do\r\n");
					insertTextToStyledText("echo `DATE` ${0##*/} Current Time i"+i+" is:$i"+i+">>$DetailLog\r\n");
					insertTextToStyledText("#Please add operation on below\r\n");
					insertTextToStyledText("\r\n");
					insertTextToStyledText("\r\n");
					insertTextToStyledText("done\r\n");
					break;
				}
			}
    	} else if (sendOption.equals("break")) {
    		insertTextToStyledText("break\r\n");
    	} else if (sendOption.equals("input text")) {
    		inputTextToStyledText();
    	} else if (sendOption.equals("Wait")) {
    		insertTextToStyledText("Wait 1000\r\n");
    	} else if (sendOption.equals("checkable")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("checked")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("clickable")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("enabled")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("focusable")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("focused")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("scrollable")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("long-clickable")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("password")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("selected")) {
    		attrIntelligent(sendOption);
    	} else if (sendOption.equals("comment")) {
    		String [] selectedText = getSelectedSourceViewer().getSelectionText().split("\r\n");
			
			String updateText = "";
			
			for (int i=0; i<selectedText.length; i++) {
				if (!selectedText[i].startsWith("#")) {
					updateText = updateText + "#" +selectedText[i] + "\r\n";
				} else {
					updateText = updateText + selectedText[i] + "\r\n";
				}
				
			}
			
			getSelectedSourceViewer().replaceTextRange(getSelectedSourceViewer().getCaretOffset(),
					getSelectedSourceViewer().getSelectionText().length(), updateText);
			
    	} else if (sendOption.equals("uncomment")) {
    		String [] selectedText = getSelectedSourceViewer().getSelectionText().split("\r\n");
			String updateText = "";
			
			for (int i=0; i<selectedText.length; i++) {
				if (selectedText[i].startsWith("#")) {
					updateText = updateText + selectedText[i].replaceFirst("#", "") + "\r\n";							
				} else {
					updateText = updateText + selectedText[i]+"\r\n";							
				}
				
			}
			
			getSelectedSourceViewer().replaceTextRange(getSelectedSourceViewer().getCaretOffset(),
					getSelectedSourceViewer().getSelectionText().length(), updateText);
			
    	} else if (sendOption.equals("LogFail")) {
    		insertTextToStyledText(sendOption+"\r\n");
    	} 
    	
    	
    }
    
}















