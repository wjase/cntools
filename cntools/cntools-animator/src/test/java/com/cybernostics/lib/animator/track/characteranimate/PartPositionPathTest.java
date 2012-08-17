/*
 * #%L cntools-animator %% Copyright (C) 2012 Cybernostics Pty Ltd %% Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. #L%
 */

package com.cybernostics.lib.animator.track.characteranimate;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author jasonw
 * 
 */
public class PartPositionPathTest
{
	@Test
	public void pathTest()
	{
		PartPositionPath path = new PartPositionPath();
		path.setPath( "body-front.head-front.mouth-open" );
		PartPositionPath path2 = new PartPositionPath();
		path2.setPath( "body-front.head-front.mouth-closed" );

		PartPositionPath parent = new PartPositionPath( "body-front" );

		Assert.assertEquals(
			"comparison",
			true,
			path2.compareTo( path ) < 0 );
		Assert.assertEquals(
			"comparison",
			true,
			path.compareTo( path2 ) > 0 );
		Assert.assertEquals(
			"comparison",
			true,
			path.compareTo( path ) == 0 );
		Assert.assertEquals(
			"comparison",
			true,
			parent.compareTo( path ) != 0 );
	}

	@Test
	public void matchesPartTest()
	{
		PartPositionPath pathToMatch = new PartPositionPath( "body-front.head-side" );

		PartPositionPath path1 = new PartPositionPath( "body-front.head-front" );
		PartPositionPath path2 = new PartPositionPath( "body-front.head-front.mouth-open" );
		PartPositionPath path3 = new PartPositionPath( "body-front.head-front.eyes-open" );
		PartPositionPath path4 = new PartPositionPath( "body-front.arm-raised" );

		Assert.assertEquals(
			"comparison",
			true,
			pathToMatch.matchesPart( path1 ) );
		Assert.assertEquals(
			"comparison",
			true,
			pathToMatch.matchesPart( path2 ) );
		Assert.assertEquals(
			"comparison",
			true,
			pathToMatch.matchesPart( path3 ) );
		Assert.assertEquals(
			"comparison",
			false,
			pathToMatch.matchesPart( path4 ) );

	}

	@Test
	public void createChildFromTest()
	{
		PartPositionPath oldPath = new PartPositionPath( "body-front.head-front.mouth-open" );
		PartPositionPath newPath = new PartPositionPath( "body-front.head-side" );

		PartPositionPath childPath = newPath.getChildPathFrom( oldPath );

		Assert.assertEquals(
			"body-front.head-side.mouth-open",
			childPath.getPath() );

	}

	@Test
	public void hierarchyElementDisplayerTest()
	{
		SVGHierachyElementDisplayer displayer = new SVGHierachyElementDisplayer();

		displayer.addControlledElement( new PartPositionPath( "body-front" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-front" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-front.eyes-closed" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-front.eyes-front" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-front.mouth-ahh" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-front.mouth-fff" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-front.mouth-mmm" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-front.mouth-ooh" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-front.nose-front" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-right" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-right.eyes-closed" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-right.eyes-front" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-right.mouth-ahh" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-right.mouth-fff" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-right.mouth-mmm" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-right.mouth-ooh" ) );
		displayer.addControlledElement( new PartPositionPath( "body-front.head-right.nose-front" ) );

		displayer.changePosition( "body-front.head-right.eyes-front" );
		displayer.changePosition( "body-front.head-right.nose-front" );
		displayer.changePosition( "body-front.head-right.mouth-mmm" );
		System.out.printf( "===========================\n" );
		displayer.changePosition( "body-front.head-front.mouth-ahh" );
		// System.out.printf("===========================\n");
		// displayer.changePosition( "head-front");
		// System.out.printf("===========================\n");
		// displayer.changePosition( "mouth-ahh");

		// Assert.assertEquals( "body-front.head-side.mouth-open",
		// childPath.getPath() );

	}

}
