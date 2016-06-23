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

/**
 * Special low error dither strategy especially for Spectrum image conversion
 * and only (currently) in use in this program, based on the Atkinson distribution
 * but using only 25% error distribution. This strategy tends to get the best
 * results in most cases, however images with high detail but similar colours
 * (e.g. blues underwater/red light on a face etc etc) may lose some detail depending
 * on pre process contrast. The trade off is that the error distribution is far less 
 * likely to propagate to surrounding *ZX Spectrum attribute blocks* resulting in
 * less colour clash than the classical diffusion algorithms.
 * 
 * This strategy was devised by me, Benjamin Brown, based on the original Atkinson
 * distribution of error, if anybody feels like publishing it :)
 */
public class LowErrorAtkinsonDitherStrategy extends AbstractErrorDiffusionDitherStrategy implements ErrorDiffusionDitherStrategy {

	public final static float TWENTY_FOURTH = 1f/24f;//1f/18f;
	
	/*
	 * {@inheritDoc}
	 */
	public void distributeError(BufferedImage output, int oldPixel,
			int newPixel, int x, int y) {
		if (isInBounds(output, x+1, y)) {output.setRGB(x+1, y, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x+1, y), TWENTY_FOURTH));}
		if (isInBounds(output, x+2, y)) {output.setRGB(x+2, y, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x+2, y), TWENTY_FOURTH));}
		if (isInBounds(output, x-1, y+1)) {output.setRGB(x-1, y+1, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x-1, y+1), TWENTY_FOURTH));}
		if (isInBounds(output, x, y+1)) {output.setRGB(x, y+1, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x, y+1), TWENTY_FOURTH));}
		if (isInBounds(output, x+1, y+1)) {output.setRGB(x+1, y+1, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x+1, y+1), TWENTY_FOURTH));}
		if (isInBounds(output, x, y+2)) {output.setRGB(x, y+2, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x, y+2), TWENTY_FOURTH));}
	}
	
	@Override
	public String toString() {
		return "Low Error Atkinson";
	}
}
