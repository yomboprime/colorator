/* Image to ZX Spec
 * Copyright (C) 2010 Silent Software (Benjamin Brown)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package uk.co.silentsoftware.core.colourstrategy;

import uk.co.silentsoftware.core.helpers.ColourHelper;


/**
 * Colouring strategy to colour all pixels using
 * whichever of the two popular colours is closest
 * to the original.
 */
public class FullColourStrategy implements ColourChoiceStrategy {
	
	public String toString() {
		return "Full Colour";
	}

	@Override
	public int colour(int originalAlphaRgb, int mostPopularRgbColour, int secondMostPopularRgb)
	{
		// Break the colours into their RGB components
		int[] originalRgbComps = ColourHelper.intToRgbComponents(originalAlphaRgb);
		int[] mostPopRgbComps = ColourHelper.intToRgbComponents(mostPopularRgbColour);
		int[] secondPopRgbComps = ColourHelper.intToRgbComponents(secondMostPopularRgb);
		
		// Work out whether the original colour is closer to the most popular
		// or second most popular colour by looking at the difference of total
		// RGB components
		int diff1 = Math.abs(originalRgbComps[0] - mostPopRgbComps[0]) 
		+ Math.abs(originalRgbComps[1] - mostPopRgbComps[1]) 
		+ Math.abs(originalRgbComps[2] - mostPopRgbComps[2]);
		int diff2 = Math.abs(originalRgbComps[0] - secondPopRgbComps[0]) 
		+ Math.abs(originalRgbComps[1] - secondPopRgbComps[1]) 
		+ Math.abs(originalRgbComps[2] - secondPopRgbComps[2]);
		
		// Return the closest popular colour
		if (diff1 <= diff2) {
			return ColourHelper.intToAlphaRgb(mostPopRgbComps);
		}
		return ColourHelper.intToAlphaRgb(secondPopRgbComps);
	}
}
