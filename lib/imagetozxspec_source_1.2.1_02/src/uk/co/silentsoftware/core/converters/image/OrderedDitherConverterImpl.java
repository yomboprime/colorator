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

import uk.co.silentsoftware.config.OptionsObject;
import uk.co.silentsoftware.core.colourstrategy.MonochromeColourStrategy;
import uk.co.silentsoftware.core.converters.image.orderedditherstrategy.OrderedDitherStrategy;
import uk.co.silentsoftware.core.helpers.ColourHelper;
import uk.co.silentsoftware.core.helpers.ImageHelper;

/**
* An ordered dithering converter
 */
public class OrderedDitherConverterImpl implements ImageProcessor {
	
	private OrderedDitherStrategy previewModeStrategy = null;
	
	public OrderedDitherConverterImpl(OrderedDitherStrategy previewModeStrategy) {
		this.previewModeStrategy = previewModeStrategy;
	}
	
	public OrderedDitherConverterImpl() {}
	
	/*
	 * {@inheritDoc}
	 */
	@Override
	public BufferedImage convert(final BufferedImage original) {
		BufferedImage output = ImageHelper.copyImage(original);
		final OrderedDitherStrategy ods = previewModeStrategy !=null?previewModeStrategy:OptionsObject.getOrderedDitherStrategy();
		int xMax = ods.getMatrixWidth();
		int yMax = ods.getMatrixHeight();
		for (int y=0; y+yMax<=original.getHeight(); y+=yMax) {
			for (int x=0; x+xMax<=original.getWidth() && y+yMax<=original.getHeight(); x+=xMax) {
				int outRgb[] = original.getRGB(x, y, xMax, yMax, null, 0, xMax);
				outRgb = ods.applyDither(outRgb);
				output.setRGB(x, y, xMax, yMax, outRgb, 0, xMax);	
			}
		}
		
		// Attribute blocks not needed since already
		// 2 colour across entire image
		if (!(OptionsObject.getColourMode() instanceof MonochromeColourStrategy)) {
			// Just colour all pixels but use the original image
			// as a basis for the colour selection
			ColourHelper.colourAttributes(output, original, OptionsObject.getColourMode());
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
