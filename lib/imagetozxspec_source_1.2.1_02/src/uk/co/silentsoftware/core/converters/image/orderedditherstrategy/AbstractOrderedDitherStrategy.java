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
package uk.co.silentsoftware.core.converters.image.orderedditherstrategy;

import static uk.co.silentsoftware.config.SpectrumDefaults.SPECTRUM_COLOURS_BRIGHT;
import uk.co.silentsoftware.config.OptionsObject;
import uk.co.silentsoftware.core.colourstrategy.ColourChoiceStrategy;
import uk.co.silentsoftware.core.colourstrategy.FullColourStrategy;
import uk.co.silentsoftware.core.helpers.ColourHelper;

/**
 * Base class for applying an ordered dither strategy
 */
public abstract class AbstractOrderedDitherStrategy {

	/**
	 * Applies the sub class' dither coefficients to
	 * the given rgb matrix from an image
	 * 
	 * @param outRgb
	 * @return
	 */
	public int[] applyDither(int[] outRgb) {
		int[] coeffs = getCoefficients();
		
		// Intensity should really be a fraction of the matrix amount
		// but addition is a faster operation
		int intensity = OptionsObject.getOrderedDitherIntensity();
		for(int i=0; i<outRgb.length; i++) {
			int[] rgb = ColourHelper.intToRgbComponents(outRgb[i]);
			int adjustedCoeff = Math.round((float)coeffs[i]/(float)intensity);
			int oldRed = (rgb[0]+adjustedCoeff);
			int oldGreen = (rgb[1]+adjustedCoeff);
			int oldBlue = (rgb[2]+adjustedCoeff);
			ColourChoiceStrategy colourMode = OptionsObject.getColourMode();
			if (colourMode instanceof FullColourStrategy) {
				outRgb[i] = ColourHelper.getClosestSpectrumColour(oldRed, oldGreen, oldBlue);
			} else {
				outRgb[i] = ColourHelper.getMonochromeColour(oldRed, oldGreen, oldBlue,
					SPECTRUM_COLOURS_BRIGHT[OptionsObject.getMonochromeInkIndex()], 
					SPECTRUM_COLOURS_BRIGHT[OptionsObject.getMonochromePaperIndex()]);
			}
		}
		return outRgb;
	}
	
	/**
	 * Retrieves the coefficients to apply
	 * 
	 * @return
	 */
	public abstract int[] getCoefficients();
	
	/**
	 * Retrieves the width of the matrix (1 dimension only)
	 * @return
	 */
	public abstract int getMatrixWidth();
	
	/**
	 * Retrieves the height of the matrix (1 dimension only)
	 * @return
	 */
	public abstract int getMatrixHeight();
		
}
