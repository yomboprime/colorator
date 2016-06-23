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

/**
 * Pixel colouring strategy used during the conversion process
 */
public interface ColourChoiceStrategy {
	
	/**
	 * Method to return the RGB int colour (Spectrum colour) that should be
	 * used for a given pixel based on the Spectrum colours
	 * passed in as mostPopularColour and secondMostPopularColour.
	 * ExistingColour is either the original pixel colour or
	 * part converted colour (e.g. a pixel that has already undergone
	 * colour processing). 
	 * 
	 * @param outAlphaRgb
	 * @param mostPopularAlphaRgbColour
	 * @param secondMostAlphaPopularRgb
	 * @return
	 */
	public int colour(int outAlphaRgb, int mostPopularAlphaRgbColour, int secondMostAlphaPopularRgb);
}
