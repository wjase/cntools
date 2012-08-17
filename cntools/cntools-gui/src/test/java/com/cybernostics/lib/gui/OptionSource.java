package com.cybernostics.lib.gui;

import com.cybernostics.lib.gui.autocomplete.AutoCompleteOptionSource;

/**
 *
 * @author jasonw
 */
public class OptionSource implements AutoCompleteOptionSource
{

	@Override
	public String[] getOptions( String currentInput )
	{
		{
			final String[] options =
			{ "Option1", "Option2", "Option3", "Option4", "Option5", "Option6",
				"Option7", "Option8", "Option9" };
			return options;
		}
	}
}
