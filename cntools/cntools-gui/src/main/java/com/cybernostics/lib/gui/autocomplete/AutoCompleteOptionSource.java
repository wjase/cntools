package com.cybernostics.lib.gui.autocomplete;

/**
 * This interface is used for objects to provide popup autocomplete options
 * based on the current text content.
 * 
 * @author jasonw
 * 
 */
public interface AutoCompleteOptionSource
{

	/**
	 * Takes the current text input and returns a list of possible strings to
	 * append. For example with file paths, this could be the list of children
	 * of a given path.
	 * 
	 * @param currentInput
	 *            the text to set the context eg /usr/bin/ would return a list
	 *            of strings for files and folders under that folder.
	 * @return
	 */
	public String[] getOptions( String currentInput );
}
