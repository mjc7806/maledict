package net.mjcarpenter.csci788.ui.text;

import java.awt.Font;
import java.io.File;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public final class XMLTextArea extends RSyntaxTextArea
{
	private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
	private final RTextScrollPane pane;
	
	private File file;
	
	public XMLTextArea(File f)
	{
		super(25,100);
		this.file = f;
		this.pane = new RTextScrollPane(this, true); // ScrollPane with line numbers on.
		pane.setMinimumSize(getSize());
		pane.setPreferredSize(getSize());
		
		// Set up preferences
		setAntiAliasingEnabled(true);
		setAutoIndentEnabled(true);
		setFont(FONT);
		setBracketMatchingEnabled(true);
		setCodeFoldingEnabled(true);
		setCloseCurlyBraces(true);
		setHighlightCurrentLine(true);
		setMarginLineEnabled(true);
		setMarginLinePosition(80);
		setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		setTabSize(4);
		
		
		//setText(f.getContents());
		
		
		// Detect changes and update the file.
		getDocument().addDocumentListener(new DocumentListener()
			{
				public void insertUpdate(DocumentEvent e){}
				public void removeUpdate(DocumentEvent e){}
	
				public void changedUpdate(DocumentEvent e)
				{
					//file.update(getText());
				}
				
			});
	}
}
