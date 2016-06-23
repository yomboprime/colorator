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
package uk.co.silentsoftware.core.converters.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import static uk.co.silentsoftware.config.SpectrumDefaults.SPECTRUM_COLOURS_BRIGHT;
import uk.co.silentsoftware.config.OptionsObject;
import uk.co.silentsoftware.core.colourstrategy.ColourChoiceStrategy;
import uk.co.silentsoftware.core.colourstrategy.FullColourStrategy;
import uk.co.silentsoftware.core.colourstrategy.MonochromeColourStrategy;
import uk.co.silentsoftware.core.converters.image.errordiffusionstrategy.ErrorDiffusionDitherStrategy;
import uk.co.silentsoftware.core.helpers.ColourHelper;
import uk.co.silentsoftware.core.helpers.ImageHelper;

/**
 * An error diffusion dithering converter
 */
public class ErrorDiffusionConverterImpl implements ImageProcessor {

	private ErrorDiffusionDitherStrategy previewModeStrategy = null;
	
	public ErrorDiffusionConverterImpl(ErrorDiffusionDitherStrategy previewModeStrategy) {
		this.previewModeStrategy = previewModeStrategy;
	}

	public ErrorDiffusionConverterImpl() {}
	
	/*
	 * {@inheritDoc}
	 */
	public BufferedImage convert(final BufferedImage original) {
		BufferedImage output = ImageHelper.copyImage(original);
		final ErrorDiffusionDitherStrategy edds = previewModeStrategy != null?previewModeStrategy:OptionsObject.getErrorDiffusionDitherStrategy();
		
		final ColourChoiceStrategy colourMode = OptionsObject.getColourMode();
		for (int y=0; y<output.getHeight(); ++y) {
			for (int x=0; x<output.getWidth(); ++x) {
				int oldPixel = output.getRGB(x, y);
				int newPixel = 0;
				
				// Use all spectrum colours
				if (colourMode instanceof FullColourStrategy) {
					newPixel = ColourHelper.getClosestSpectrumColour(oldPixel);
					
			    // Use just two colours (monochrome)
				} else {
					newPixel = ColourHelper.getMonochromeColour(oldPixel, 
						SPECTRUM_COLOURS_BRIGHT[OptionsObject.getMonochromeInkIndex()], 
						SPECTRUM_COLOURS_BRIGHT[OptionsObject.getMonochromePaperIndex()] 
					);
				}
				output.setRGB(x, y, newPixel);
				edds.distributeError(output, oldPixel, newPixel, x, y);
			}
		}
		
		// Attribute blocks not needed since already
		// 2 colour across entire image
		if (!(colourMode instanceof MonochromeColourStrategy)) {
			
			// Just colour all pixels but use the error diffused image
			// as a basis for the colour selection
			ColourHelper.colourAttributes(output, original, colourMode);		
		}
		
		// Print the name of the preview strategy
		if (previewModeStrategy != null) {
			Graphics g = output.getGraphics();
			g.setColor(Color.DARK_GRAY);
			g.setFont(g.getFont().deriveFont(Font.BOLD));
			g.drawString(previewModeStrategy.toString(),0,20);
		}
		return output;
	}
}
