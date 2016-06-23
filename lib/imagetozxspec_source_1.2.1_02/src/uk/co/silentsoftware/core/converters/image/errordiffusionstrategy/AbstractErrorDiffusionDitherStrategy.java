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
package uk.co.silentsoftware.core.converters.image.errordiffusionstrategy;

import java.awt.image.BufferedImage;

import uk.co.silentsoftware.core.helpers.ColourHelper;

public abstract class AbstractErrorDiffusionDitherStrategy {

	/**
	 * Calculates the error diffusion given by the fraction against
	 * the diffused pixel. The error is taken from the currently 
	 * processed pixel whose original colour and new Spectrum colour 
	 * values are given in original and new pixel respectively.
	 * A percentage of the error is then applied to the diffusePixel
	 * as per the error distribution around the currently processed pixel.
	 * 
	 * @param oldPixel
	 * @param newPixel
	 * @param diffusePixel
	 * @param fraction
	 * @return
	 */
	protected int calculateAdjustedRGB(int oldPixel, int newPixel, int diffusePixel, float fraction) {	
		
		int[] oldRgb = ColourHelper.intToRgbComponents(oldPixel);	
		int[] newRgb = ColourHelper.intToRgbComponents(newPixel);
		int[] diffusedRgb = ColourHelper.intToRgbComponents(diffusePixel);
		
		// Calculate the error (difference) in each channel
		// between the old colour and new colour
		int redError = oldRgb[0] - newRgb[0];
		int greenError = oldRgb[1] - newRgb[1];
		int blueError = oldRgb[2] - newRgb[2];
		
		// Apply the given fraction of the error to the chosen
		// surrounding pixel's RGB value
		int red = Math.round((diffusedRgb[0] + (fraction*(float)redError)));
		int green = Math.round((diffusedRgb[1] + (fraction*(float)greenError)));
		int blue = Math.round((diffusedRgb[2] + (fraction*(float)blueError)));
		
		// Return the new RGB value
		return ColourHelper.intToAlphaRgb(red, green, blue);
	}
	
	/**
	 * Verify the x and y coordinates are with the image's width and height
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @return
	 */
	protected boolean isInBounds(BufferedImage image, int x, int y) {
		return ((x >= 0 && x < image.getWidth()) && (y >= 0 && y < image.getHeight())); 
	}
}
