package model;

import javax.swing.text.*;

/**
 * Provide auto indent for text document.
 * Found here: http://stackoverflow.com/questions/15867900/java-auto-indentation-in-jtextpane
 * @author camickr
 */
public class NewLineFilter extends DocumentFilter
{
	public void insertString(FilterBypass fb, int offs, String str, AttributeSet a)
			throws BadLocationException
	{
		if ("\n".equals(str))
			str = addWhiteSpace(fb.getDocument(), offs);

		super.insertString(fb, offs, str, a);
	}

	public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a)
			throws BadLocationException
	{
		if ("\n".equals(str))
			str = addWhiteSpace(fb.getDocument(), offs);

		super.replace(fb, offs, length, str, a);
	}

	private String addWhiteSpace(Document doc, int offset)
			throws BadLocationException
	{
		StringBuilder whiteSpace = new StringBuilder("\n");
		Element rootElement = doc.getDefaultRootElement();
		int line = rootElement.getElementIndex( offset );
		int i = rootElement.getElement(line).getStartOffset();

		while (true)
		{
			String temp = doc.getText(i, 1);

			if (" ".equals(temp) || "\t".equals(temp))
			{
				whiteSpace.append(temp);
				i++;
			}
			else
				break;
		}

		return whiteSpace.toString();
	}
}