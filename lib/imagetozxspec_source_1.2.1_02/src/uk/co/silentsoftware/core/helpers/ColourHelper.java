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
package uk.co.silentsoftware.core.helpers;

import static uk.co.silentsoftware.config.SpectrumDefaults.ATTRIBUTE_BLOCK_SIZE;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.co.silentsoftware.config.OptionsObject;
import uk.co.silentsoftware.config.SpectrumDefaults;
import uk.co.silentsoftware.core.colourstrategy.ColourChoiceStrategy;

/**
 * Utility class to provide common colour functionality
 */
public final class ColourHelper {
	
	/**
	 * Retrieves the spectrum colour most like the provided rgb colour
	 * 
	 * @param rgb
	 * @return
	 */
	public static int getClosestSpectrumColour(int rgb) {
		return getClosestSpectrumColour(rgb, SpectrumDefaults.SPECTRUM_COLOURS_ALL);
	}
	
	/**
	 * Retrieves the spectrum colour most like the provided rgb colour
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public static int getClosestSpectrumColour(int red, int green, int blue) {
		return getClosestSpectrumColour(red, green, blue, SpectrumDefaults.SPECTRUM_COLOURS_ALL);
	}
	
	/**
	 * Retrieves the spectrum bright colour most like the provided rgb colour
	 * which is NOT the excluded rgb
	 * @param rgb
	 * @return
	 */
	public static int getClosestBrightSpectrumColour(int rgb, int excludedRgb) {
		return getClosestSpectrumColourWithExclusion(rgb, excludedRgb, SpectrumDefaults.SPECTRUM_COLOURS_BRIGHT);
	}
	
	/**
	 * Retrieves the spectrum bright colour most like the provided rgb colour
	 * 
	 * @param rgb
	 * @return
	 */
	public static int getClosestBrightSpectrumColour(int rgb) {
		return getClosestSpectrumColour(rgb, SpectrumDefaults.SPECTRUM_COLOURS_BRIGHT);
	}
	
	/**
	 * Retrieves the spectrum half bright colour most like the provided rgb colour
	 * which is NOT the excluded rgb
	 * 
	 * @param rgb
	 * @return
	 */
	public static int getClosestHalfBrightSpectrumColour(int rgb, int excludedRgb) {
		return getClosestSpectrumColourWithExclusion(rgb, excludedRgb, SpectrumDefaults.SPECTRUM_COLOURS_HALF_BRIGHT);
	}
	
	/**
	 * Retrieves the spectrum half bright colour most like the provided rgb colour
	 * 
	 * @param rgb
	 * @return
	 */
	public static int getClosestHalfBrightSpectrumColour(int rgb) {
		return getClosestSpectrumColour(rgb, SpectrumDefaults.SPECTRUM_COLOURS_HALF_BRIGHT);
	}
	
		
	/**
	 * Retrieves the from the colourSet most like the provided rgb colour
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @param colourSet
	 * @return
	 */
	private static int getClosestSpectrumColour(int red, int green, int blue, int[] colourSet) {

		int bestMatch = Integer.MAX_VALUE;
		int closest = colourSet[0];
		for (int colour : colourSet) {
			final int[] colourSetComps = intToRgbComponents(colour);
			int diff = Math.abs(red - colourSetComps[0]) 
				+ Math.abs(green - colourSetComps[1]) 
				+ Math.abs(blue - colourSetComps[2]);
			if (diff < bestMatch) {
				closest = colour;
				bestMatch = diff;
			}
		}
		return closest;
	}
	
	private static int getClosestSpectrumColour(int rgb, int[] colourSet) {		
		final int[] comps = intToRgbComponents(rgb);
		return getClosestSpectrumColour(comps[0], comps[1], comps[2], colourSet);
	}
	
	/**
	 * Retrieves the colour from the colourSet most like the provided rgb colour
	 * 
	 * @param rgb
	 * @param colourSet
	 * @return
	 */
	private static int getClosestSpectrumColourWithExclusion(int rgb, int excludedRgb, int[] colourSet) {
		
		int original[] = intToRgbComponents(rgb);	
	
		int bestMatch = Integer.MAX_VALUE;
		int closest = colourSet[0];
		for (int colour : colourSet) {
			if (colour == excludedRgb) {
				continue;
			}
			int[] colourSetRgb = intToRgbComponents(colour);	
			
			int diff = Math.abs(original[0] - colourSetRgb[0]) 
				+ Math.abs(original[1] - colourSetRgb[1]) 
				+ Math.abs(original[2] - colourSetRgb[2]);
			if (diff < bestMatch) {
				closest = colour;
				bestMatch = diff;
			}
		}
		return closest;
	}
	
	/**
	 * Colours an entire image using the given colourstrategy based
	 * on the original and output images
	 * 
	 * @param output
	 * @param original
	 * @param colourChoiceStrategy
	 * @return
	 */
	public static BufferedImage colourAttributes(BufferedImage output, final BufferedImage original, ColourChoiceStrategy colourChoiceStrategy) {
		return colourAttributes(ATTRIBUTE_BLOCK_SIZE, ATTRIBUTE_BLOCK_SIZE, output, original, colourChoiceStrategy);
	}
	
	/**
	 * Colours the Spectrum attribute blocks by selecting xMax by yMax parts
	 * of the output image (i.e. usually 8x8 pixels), chooses the most popular 
	 * two colours. The colour choice strategy then decides how to colour 
	 * individual pixels based on these two colours.
	 * 
	 * Note it is expected that this method will be called AFTER the pixels have
	 * been changed to Spectrum colours.
	 * 
	 * @param xMax
	 * @param yMax
	 * @param output
	 * @param colourChoiceStrategy
	 * @return
	 */
	private static BufferedImage colourAttributes(int xMax, int yMax, 
			BufferedImage output, final BufferedImage original, ColourChoiceStrategy colourChoiceStrategy) {
		//BufferedImage copy = ImageHelper.copyImage(output);
		
		// Make sure there is no rehashing
		final int maxElements = 90;
		Map<Integer, Integer> map = new HashMap<Integer, Integer>(maxElements);
		
		// Analyse block and choose the two most popular colours in attribute block
		for (int y=0; y+yMax<=output.getHeight(); y+=yMax) {
			for (int x=0; x+xMax<=output.getWidth() && y+yMax<=output.getHeight(); x+=xMax) {
				int outRgb[] = output.getRGB(x, y, xMax, yMax, null, 0, xMax);
				//int rgb[] = copy.getRGB(x, y, xMax, yMax, null, 0, xMax);
				map.clear();
				int mostFrequent = -1;
				int mostPopularColour = 0;
				
				for (int i=0; i<outRgb.length; ++i) {
					int value = ColourHelper.getClosestSpectrumColour(outRgb[i]); 
					int count = 1;
					if (map.containsKey(value)) {
						count = map.get(value)+1;				
					}
					map.put(value, count);
					if (count > mostFrequent) {
						mostPopularColour = value;
						mostFrequent = count;
					} 
				}	
				
				// We must perform this is a new loop since we need
				// to know the most popular before we can determine
				// a different colour for second most popular. This
				// cannot be determined via one rgb loop iteration
				// as the most popular changes therefore we cannot 
				// exclude it.
				int secondMostPopularColour = mostPopularColour;
				Set<Integer> keySet = map.keySet();
				map.remove(mostPopularColour);
				mostFrequent = -1;
				
				for (int colour : keySet) {
					int count = map.get(colour);
					if (count > mostFrequent) {
						secondMostPopularColour = colour;
						mostFrequent = count;
					}
				}
				
				final int[] mostPopular = ColourHelper.intToRgbComponents(mostPopularColour);
				final int[] secondMostPopular = ColourHelper.intToRgbComponents(secondMostPopularColour);
				
				// Enforce attribute favouritism rules on the two spectrum attribute colours
				// (fixes the problem that colours could be from both the bright and half bright set).
				int[] correctedAlphaColours = OptionsObject.getAttributeMode().enforceAttributeRule(mostPopular, secondMostPopular);
				
				// Replace all colours in attribute block with the popular two
				for (int i=0; i<outRgb.length; ++i) {
					outRgb[i] = colourChoiceStrategy.colour(outRgb[i], correctedAlphaColours[0], correctedAlphaColours[1]);
				}			
				output.setRGB(x, y, xMax, yMax, outRgb, 0, xMax);	
			}
		}
		return output;
	}
	
	/**
	 * Determines whether the colour is from the Spectrum's
	 * bright or half bright colour set.
	 * 
	 * @param argb
	 * @return
	 */
	public static boolean isBrightSet(int argb) {
		for (int i = 0; i<SpectrumDefaults.SPECTRUM_COLOURS_BRIGHT.length; ++i) {
			int def = SpectrumDefaults.SPECTRUM_COLOURS_BRIGHT[i];
			if (def == argb) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Changes the contrast of an image
	 * 
	 * @param img
	 * @param amount
	 * @return
	 */
	public static BufferedImage changeContrast(BufferedImage img, float amount) {
		if (amount == 1) {
			return img;
		}
		for (int y=0; y<img.getHeight(); ++y) {
			for (int x=0; x<img.getWidth(); ++x) {
				img.setRGB(x, y, changePixelContrast(img.getRGB(x, y), amount));
			}
		}
		return img;
	}
	
	/**
	 * Changes the contrast of an individual pixel by a given
	 * amount.
	 * 
	 * @param pixel
	 * @param c
	 * @return
	 */
	private static int changePixelContrast(int pixel, float c) {
		int[] rgbc = intToRgbComponents(pixel);

		int a = Math.round((rgbc[0] + rgbc[1] + rgbc[2]) / 3f);

		int red = Math.round(a + ((rgbc[0] - a) * c));
		int green = Math.round(a + ((rgbc[1] - a) * c));
		int blue = Math.round(a + ((rgbc[2] - a) * c));
		
		return intToAlphaRgb(red, green, blue);
	}

	/**
	 * This is the "official" way of increasing brightness but
	 * frankly its poor - try it and see the whites change
	 * colours at high brightness!
	 * 
	 * @param pixel
	 * @param amount
	 * @return
	 */
//	private static int changePixelBrightness(int pixel, float amount) {
//		int[] rgbc = intToRgbComponents(pixel);
//		float[] hsb = Color.RGBtoHSB(rgbc[0], rgbc[1], rgbc[2], null);
//		float brightness = correctRange((hsb[2]+=amount), 0,1);
//		return Color.HSBtoRGB(hsb[0], hsb[1], brightness);
//	}	
	
	/**
	 * Changes brightness by increasing all pixel values by a given amount
	 * 
	 * @param img
	 * @param amount
	 * @return
	 */
	public static BufferedImage changeBrightness(BufferedImage img, float amount) {
		if (amount == 0) {
			return img;
		}
		for (int y=0; y<img.getHeight(); ++y) {
			for (int x=0; x<img.getWidth(); ++x) {
				img.setRGB(x, y, changePixelBrightness(img.getRGB(x, y), amount));
			}
		}
		return img;
	}
	
	
	/**
	 * Changes brightness by increasing the pixel value by a given amount
	 * 
	 * @param pixel
	 * @param amount
	 * @return
	 */
	private static int changePixelBrightness(int pixel, float amount) {
		final int MULTIPLIER = 100; 
		final int[] rgbc = intToRgbComponents(pixel);
	
		rgbc[0] += Math.round(amount*MULTIPLIER);
		rgbc[1] += Math.round(amount*MULTIPLIER);
		rgbc[2] += Math.round(amount*MULTIPLIER);
		
		return intToAlphaRgb(rgbc[0], rgbc[1], rgbc[2]);
	}
	
	/**
	 * Changes image saturation by a given amount (0-1 range)
	 * @param img
	 * @param amount
	 * @return
	 */
	public static BufferedImage changeSaturation(BufferedImage img, float amount) {
		if (amount == 0) {
			return img;
		}
		for (int y=0; y<img.getHeight(); ++y) {
			for (int x=0; x<img.getWidth(); ++x) {
				img.setRGB(x, y, changePixelSaturation(img.getRGB(x, y), amount));
			}
		}
		return img;
	}
	
	/**
	 * Changes the saturation of an individual pixel by the given amount 
	 * (0-1 range)
	 * 
	 * @param pixel
	 * @param amount
	 * @return
	 */
	private static int changePixelSaturation(int pixel, float amount) {
		int[] rgb = intToRgbComponents(pixel);
		float[] hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], null);
		hsb[1]+=amount;
		float saturation = correctRange(hsb[1], 0,1);
		return Color.HSBtoRGB(hsb[0], saturation, hsb[2]);
	}	
	
	/**
	 * Ensures a value is within a given range. If it exceeds or
	 * is below it is set to the high value or low value respectively
	 * 
	 * @param value
	 * @param low
	 * @param high
	 * @return
	 */
	 static float correctRange(float value, int low, int high) {
		if (value < low) {
			return low;
		}
		if (value > high) {
			return high;
		}
		return value;
	}
	
	public static int[] intToRgbComponents(int rgb)
	{
		return new int[] { rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF };
	}

	/**
	 * Convert individual RGB components into a 32 bit ARGB value
	 * 
	 * @param rgb
	 * @return
	 */
	public static int intToAlphaRgb(int[] rgb)
	{
		return intToAlphaRgb(rgb[0], rgb[1], rgb[2]);
	}
	
	/**
	 * Convert individual RGB components into a 32 bit ARGB value
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public static int intToAlphaRgb(int red, int green, int blue)
	{
		return new Color(correctRange(red), correctRange(green), correctRange(blue)).getRGB();
	}
	
	/**
	 * Corrects and individual colour channel value's
	 * range to 0>channel<255
	 * 
	 * @param channel
	 * @return
	 */
	private static int correctRange(int channel) {
		return (int)ColourHelper.correctRange(channel, 0, 255);
	}
	
	/**
	 * Determines whether a pixel is closer to black (than white)
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public static boolean isBlack(int red, int green, int blue) {
		int colourTotal = red+green+blue;
		return colourTotal < OptionsObject.getBlackThreshold();
	}
	
	/**
	 * Based on the darkness of the pixel colour determines
	 * whether a pixel is ink or paper and returns that colour.
	 * Used for converting colour to monochrome based on whether
	 * a pixel can be considered black using the isBlack threshold.
	 * 
	 * @param rgb
	 * @param ink
	 * @param paper
	 * @return
	 */
	public static int getMonochromeColour(int rgb, int ink, int paper) {
		int[] comps = intToRgbComponents(rgb);
		return getMonochromeColour(comps[0], comps[1], comps[2], ink, paper);
	}
	
	/**
	 * Based on the darkness of the pixel colour determines
	 * whether a pixel is ink or paper and returns that colour.
	 * Used for converting colour to monochrome based on whether
	 * a pixel can be considered black using the isBlack threshold.
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @param ink
	 * @param paper
	 * @return
	 */

	public static int getMonochromeColour(int red, int green, int blue, int ink, int paper) {
		if (isBlack(red, green, blue))
			return ink;
		return paper;
	}
}
