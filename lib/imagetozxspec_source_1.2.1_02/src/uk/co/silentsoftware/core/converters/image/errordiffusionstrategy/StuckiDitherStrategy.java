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
 * Implementation of the Stucki error diffusion algorithm
 */
public class StuckiDitherStrategy extends AbstractErrorDiffusionDitherStrategy implements ErrorDiffusionDitherStrategy {

	private static final float TWENTY_ONETH = 1f/21f;
	private static final float TWO_TWENTY_ONETHS = 1f/21f;
	private static final float FOUR_TWENTY_ONETHS = 4f/21f;
	private static final float FOURTY_TWOTH = 4f/21f;
	
	/*
	 * {@inheritDoc}
	 */
	public void distributeError(BufferedImage output, int oldPixel,
			int newPixel, int x, int y) {

		if (isInBounds(output, x+1, y)) {output.setRGB(x+1, y, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x+1, y), FOUR_TWENTY_ONETHS));}
		if (isInBounds(output, x+2, y)) {output.setRGB(x+2, y, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x+2, y), TWO_TWENTY_ONETHS));}
		if (isInBounds(output, x-2, y+1)) {output.setRGB(x-2, y+1, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x-2, y+1), TWENTY_ONETH));}
		if (isInBounds(output, x-1, y+1)) {output.setRGB(x-1, y+1, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x-1, y+1), TWO_TWENTY_ONETHS));}
		if (isInBounds(output, x, y+1)) {output.setRGB(x, y+1, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x, y+1), FOUR_TWENTY_ONETHS));}
		if (isInBounds(output, x+1, y+1)) {output.setRGB(x+1, y+1, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x+1, y+1), TWO_TWENTY_ONETHS));}
		if (isInBounds(output, x+2, y+1)) {output.setRGB(x+2, y+1, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x+2, y+1), TWENTY_ONETH));}
		if (isInBounds(output, x-2, y+2)) {output.setRGB(x-2, y+2, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x-2, y+2), FOURTY_TWOTH));}
		if (isInBounds(output, x-1, y+2)) {output.setRGB(x-1, y+2, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x-1, y+2), TWENTY_ONETH));}
		if (isInBounds(output, x, y+2)) {output.setRGB(x, y+2, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x, y+2), TWO_TWENTY_ONETHS));}
		if (isInBounds(output, x+1, y+2)) {output.setRGB(x+1, y+2, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x+1, y+2), TWENTY_ONETH));}
		if (isInBounds(output, x+2, y+2)) {output.setRGB(x+2, y+2, calculateAdjustedRGB(oldPixel, newPixel, output.getRGB(x+2, y+2), FOURTY_TWOTH));}		
	}
	
	@Override
	public String toString() {
		return "Stucki";
	}
}
