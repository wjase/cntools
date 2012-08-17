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

package com.cybernostics.lib.animator.sprite;

import com.cybernostics.lib.collections.IterableArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a visual layer of sprites at the same z_order
 *
 * @author jasonw
 */
public class SpriteLayer implements SpriteCollection
{

	public SpriteLayer( int zOrder )
	{
		this.z_order = zOrder;
	}

	private int z_order;

	ArrayList< ISprite > sprites = new ArrayList< ISprite >();

	//private ISprite[] sprites = null;

	public List< ISprite > getSprites()
	{
		return sprites;
	}

	public void setSprites( ISprite[] sprites )
	{
		this.sprites = new ArrayList< ISprite >( Arrays.asList( sprites ) );
	}

	public int getZ_order()
	{
		return z_order;
	}

	public void setZ_order( int z_order )
	{
		this.z_order = z_order;
	}

	// If items are removed the indicies are added here
	ArrayList< Integer > freeList = new ArrayList< Integer >();

	public void removeSprites( ISprite... toRemove )
	{

		for (ISprite eachSprite : IterableArray.get( toRemove ))
		{
			sprites.remove( eachSprite );
		}
		//        for ( ISprite eachSprite : IterableArray.get( toRemove ) )
		//        {
		//            for ( int index = 0; index < sprites.length; ++index )
		//            {
		//                if ( sprites[index].getId().equals( eachSprite.getId() ) )
		//                {
		//                    sprites[index] = null;
		//                    eachSprite.setContainer( null ); // uncouple it
		//                    freeList.add( index );
		//                }
		//            }
		//
		//        }

	}

	public void addSprites( ISprite... toAdd )
	{
		for (ISprite eachSprite : IterableArray.get( toAdd ))
		{
			sprites.add( eachSprite );
			eachSprite.setContainer( this );
		}

		//        int toAddIndex = toAdd.length - 1;
		//
		//        // fill up free slots from the free list index
		//        while ( !freeList.isEmpty() )
		//        {
		//            // if it isn't already in this collection
		//            if ( toAdd[toAddIndex].getGetContainer() != this )
		//            {
		//                sprites[freeList.remove( 0 )] = toAdd[toAddIndex];
		//                toAdd[toAddIndex].setContainer( this );
		//            }
		//            if ( toAddIndex == 0 )
		//            {
		//                return;
		//            }
		//            --toAddIndex;
		//        }
		//
		//        // Add the remaining sprites to the end
		//        if ( toAddIndex >= 0 )
		//        {
		//            int combinedSize = 1 + toAddIndex + ( ( sprites != null ) ? sprites.length : 0 );
		//            while ( toAddIndex >= 0 )
		//            {
		//                toAdd[toAddIndex].setContainer( this );
		//                --toAddIndex;
		//            }
		//            sprites = ArrayUtils.merge( ISprite.class, combinedSize, sprites, toAdd );
		//        }

	}

	@Override
	public ISprite getById( String id )
	{
		for (ISprite eachOne : sprites)
		{
			if (eachOne.getId()
				.equals(
					id ))
			{
				return eachOne;
			}
		}
		return null;
	}

}
