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
package uk.co.silentsoftware.dispatcher;

import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import uk.co.silentsoftware.config.OptionsObject;
import uk.co.silentsoftware.config.ScalingObject;
import uk.co.silentsoftware.core.converters.image.ErrorDiffusionConverterImpl;
import uk.co.silentsoftware.core.converters.image.ImageProcessor;
import uk.co.silentsoftware.core.converters.image.OrderedDitherConverterImpl;
import uk.co.silentsoftware.core.converters.image.errordiffusionstrategy.ErrorDiffusionDitherStrategy;
import uk.co.silentsoftware.core.converters.image.orderedditherstrategy.OrderedDitherStrategy;
import uk.co.silentsoftware.core.converters.spectrum.ScrConverter;
import uk.co.silentsoftware.core.helpers.ColourHelper;
import uk.co.silentsoftware.core.helpers.ImageHelper;

/**
 * Wrapper class for a work processing unit that
 * contains conversion actioning methods (i.e. the bits
 * that actually call the bits that do work :) ).
 * This class is not entirely static due to the need for
 * separate instances when used by the many dithers
 * preview requiring a different image processor each time.
 */
public class WorkProcessor
{	
	/**
	 * The image processor to use
	 */
	private ImageProcessor imageProcessor;

	/**
	 * The "SCREEN$" converter
	 */
	private static final ScrConverter screenConverter = new ScrConverter();

	
	/**
	 * Main work processor constructor used for actual results
	 */
	public WorkProcessor() {
		if (OptionsObject.isErrorDiffusion()) {
			imageProcessor = new ErrorDiffusionConverterImpl();
		} else { 
			imageProcessor = new OrderedDitherConverterImpl();
		}
		return;	
	}
	
	/**
	 * Preview constructor for when we need a result from a given
	 * dither strategy as opposed to that selected in options
	 * 
	 * @param dither
	 */
	public WorkProcessor(Object dither) { 
		if (dither instanceof ErrorDiffusionDitherStrategy) {
			imageProcessor = new ErrorDiffusionConverterImpl((ErrorDiffusionDitherStrategy)dither);
		} else { 
			imageProcessor = new OrderedDitherConverterImpl((OrderedDitherStrategy)dither);
		}
	}
	
	/**
	 * Converts the given image to the SCR (SCREEN) format,
	 * optionally saves the file to disk and wraps any errors
	 * during conversion and shows them as a UI dialog message.
	 * 
	 * @param original
	 * @param isAlreadyImageProcessed
	 * @return
	 */
	byte[] convertScreen(BufferedImage original, boolean isAlreadyImageProcessed) {
		try {
			return screenConverter.convert(original,  imageProcessor, isAlreadyImageProcessed);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "An error has occurred: "+e.getMessage(), "Guru meditation", JOptionPane.OK_OPTION);  
		}
		return null;
	}
	
	/**
	 * Converts the given image to the to a "Spectrumified" format
	 * which is returned as a BufferedImage. Any errors during 
	 * conversion are shown as a UI dialog message.
	 * 
	 * @param original
	 * @return
	 */
	BufferedImage convertImage(final BufferedImage original) {
		try {
			return imageProcessor.convert(original);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "An error has occurred: "+e.getMessage(), "Guru meditation", JOptionPane.OK_OPTION);  
		}
		return null;
	}
	
	/**
	 * Pre processes the given bufferedimage applying the given scaling
	 * and the specified option set contrast, saturation and brightness
	 * 
	 * @param original
	 * @param scaling
	 * @return
	 */
	BufferedImage preProcessImage(final BufferedImage original, ScalingObject scaling) {
		BufferedImage origScaled = ImageHelper.quickScaleImage(original, scaling.getWidth(), scaling.getHeight());
		origScaled = ColourHelper.changeContrast(origScaled, OptionsObject.getContrast());
		origScaled = ColourHelper.changeSaturation(origScaled, OptionsObject.getSaturation());
		origScaled = ColourHelper.changeBrightness(origScaled, OptionsObject.getBrightness());
		return origScaled;
	}
}
