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

import com.cybernostics.lib.maths.IntegerReverseComparator;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Organises sprites into layers sorted by z_order
 *
 * @author jasonw
 */
public class SpriteLayerCollection
	implements
	Iterable< ISprite >,
	SpriteCollection
{

	ConcurrentLinkedQueue< ISprite > toAddQueue = new ConcurrentLinkedQueue< ISprite >();

	ConcurrentLinkedQueue< ISprite > toRemoveQueue = new ConcurrentLinkedQueue< ISprite >();

	/**
	 * A sorted map of layers ordered by z_order
	 */
	// map is sorted with the highest first
	Map< Integer, SpriteLayer > layers = new TreeMap< Integer, SpriteLayer >();

	SpriteLayer getLayer( int z_order )
	{
		SpriteLayer layer = layers.get( z_order );

		if (layer == null)
		{
			layer = new SpriteLayer( z_order );
			layers.put(
				z_order,
				layer );
		}

		return layer;
	}

	@Override
	public void removeSprites( ISprite... toRemove )
	{
		toRemoveQueue.addAll( Arrays.asList( toRemove ) );
		//        SpriteLayer currentLayer = null;
		//
		//        for ( ISprite eachSprite : toRemove )
		//        {
		//            if ( currentLayer == null || currentLayer.getZ_order() != eachSprite.getZ_order() )
		//            {
		//                currentLayer = getLayer( eachSprite.getZ_order() );
		//            }
		//            currentLayer.removeSprites( eachSprite );
		//        }
	}

	public void removeSprite( ISprite toRemove )
	{
		SpriteLayer currentLayer = getLayer( toRemove.getZ_order() );
		currentLayer.removeSprites( toRemove );
	}

	@Override
	public void addSprites( ISprite... toAdd )
	{
		toAddQueue.addAll( Arrays.asList( toAdd ) );
		//        SpriteLayer currentLayer = null;
		//
		//        for ( ISprite eachSprite : toRemove )
		//        {
		//            if ( currentLayer == null || currentLayer.getZ_order() != eachSprite.getZ_order() )
		//            {
		//                currentLayer = getLayer( eachSprite.getZ_order() );
		//            }
		//            currentLayer.addSprites( eachSprite );
		//        }
	}

	public void addSprite( ISprite toAdd )
	{
		SpriteLayer currentLayer = getLayer( toAdd.getZ_order() );
		currentLayer.addSprites( toAdd );
	}

	@Override
	public ISprite getById( String id )
	{
		for (ISprite eachOne : this)
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

	private class LayerIterator implements Iterator< ISprite >
	{

		private Iterator< ISprite > currentLayerIt = null;

		private Iterator< Entry< Integer, SpriteLayer >> layerIt = null;

		private int currentLayerZOrder = -1;

		LayerIterator()
		{
			layerIt = SpriteLayerCollection.this.layers.entrySet()
				.iterator();
		}

		@Override
		public boolean hasNext()
		{
			if (currentLayerIt == null || !currentLayerIt.hasNext())
			{
				if (layerIt.hasNext())
				{
					Entry< Integer, SpriteLayer > entry = layerIt.next();
					currentLayerIt = entry.getValue()
						.getSprites()
						.iterator();
					currentLayerZOrder = entry.getKey()
						.intValue();
				}
				else
				{
					resolveAddedRemoved();
					return false;
				}
			}

			boolean result = currentLayerIt != null && currentLayerIt.hasNext();
			if (!result)
			{
				resolveAddedRemoved(); // clean up any removed
			}
			return result;
		}

		private ISprite current = null;

		@Override
		public ISprite next()
		{
			current = currentLayerIt.next();
			if (current.getZ_order() != currentLayerZOrder)
			{
				updateSpriteLayer( current );
			}
			return current;
		}

		@Override
		public void remove() // will be removed on next iteration
		{
			toRemoveQueue.add( current );
		}

	}

	private void resolveAddedRemoved()
	{
		while (!toRemoveQueue.isEmpty())
		{
			ISprite eachOne = toRemoveQueue.remove();
			removeSprite( eachOne );

		}
		while (!toAddQueue.isEmpty())
		{
			ISprite eachOne = toAddQueue.remove();
			addSprite( eachOne );

		}

	}

	@Override
	public Iterator< ISprite > iterator()
	{
		resolveAddedRemoved();
		return new LayerIterator();
	}

	public void updateSpriteLayer( ISprite toMove )
	{
		// by deleting and re-adding the sprite it will be assigned 
		// to the right layer
		toRemoveQueue.add( toMove );
		toAddQueue.add( toMove );
	}

}
