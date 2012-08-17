package com.cybernostics.lib.gui.control;

import com.cybernostics.lib.gui.TooltipListener;
import com.cybernostics.lib.animator.track.characteranimate.AnimatedCharacter;
import java.net.URL;
import javax.swing.JComponent;

/**
 *
 * @author jasonw
 */
public class CharacterTooltip implements TooltipListener
{

	public CharacterTooltip( AnimatedCharacter character, URL speech )
	{
		this.tipSpeaker = character;
		this.toSay = speech;
	}

	AnimatedCharacter tipSpeaker;
	URL toSay;

	@Override
	public void doTooltipFor( JComponent jc )
	{
		tipSpeaker.say( toSay );
	}

}
