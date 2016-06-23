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
package uk.co.silentsoftware.core.converters.spectrum;

import java.awt.image.BufferedImage;
import java.util.BitSet;

import uk.co.silentsoftware.config.OptionsObject;
import uk.co.silentsoftware.config.SpectrumDefaults;
import uk.co.silentsoftware.core.attributestrategy.AttributeStrategy;
import uk.co.silentsoftware.core.converters.image.ImageProcessor;
import uk.co.silentsoftware.core.helpers.ByteHelper;
import uk.co.silentsoftware.core.helpers.ImageHelper;

/**
 * Converter to save images to the Spectrum scr 
 * memory model/dump format.
 */
public class ScrConverter {

	/**
	 * SCR images are fixed size (representing Spectrum memory)
	 * - this is the size in bits
	 */
	private static final int SCR_BITS_SIZE = 6912*8;
	
	/**
	 * Retrieves the Spectrum two ink/paper colour data 
	 * for blocks in the provided image. I.e. a 256x192
	 * image is passed in, this is divided by the Spectrum
	 * colour block size (8x8) and returns a two colour 
	 * ColourData array of 32x24 holding ink and paper.
	 * Note this method EXPECTS PIXELS TO BE IN SPECTRUM
	 * COLOURS ONLY - NO VALIDATION IS DONE!
	 * 
	 * A popularity check is also performed in choosing 
	 * which colour is ink and which is paper - the most
	 * popular is paper (i.e. usually a background)
	 * 
	 * @param img
	 * @return
	 */
	private ColourData[][] getBlockedColourData(BufferedImage img) {
		int width = img.getWidth()/SpectrumDefaults.ATTRIBUTE_BLOCK_SIZE;
		int height = img.getHeight()/SpectrumDefaults.ATTRIBUTE_BLOCK_SIZE;
		
		AttributeStrategy attributeStrategy = OptionsObject.getAttributeMode();
		ColourData[][] data = new ColourData[width][height];
		for (int y=0; y<height; ++y) {
			for (int x=0; x<width; ++x) {
				data[x][y] = new ColourData();
				int[] block = img.getRGB(x*SpectrumDefaults.ATTRIBUTE_BLOCK_SIZE, y*SpectrumDefaults.ATTRIBUTE_BLOCK_SIZE, SpectrumDefaults.ATTRIBUTE_BLOCK_SIZE, SpectrumDefaults.ATTRIBUTE_BLOCK_SIZE, null, 0, SpectrumDefaults.ATTRIBUTE_BLOCK_SIZE);
				data[x][y].setInkRGB(block[0]);
				data[x][y].setPaperRGB(block[0]);
				int inkCount = 0;
				int paperCount = 0;
				for (int rgb : block) {
					if (rgb != data[x][y].getInkRGB()) {
						data[x][y].setPaperRGB(rgb);
						++paperCount;
					} else {
						++inkCount;
					}
				}
				
				// Swap the ink and paper around if ink is more popular
				if (inkCount > paperCount) {
					int temp = data[x][y].getPaperRGB();
					data[x][y].setPaperRGB(data[x][y].getInkRGB());
					data[x][y].setInkRGB(temp);
				}
				
				data[x][y].setBrightSet(attributeStrategy.isBrightSet(data[x][y].getPaperRGB(), data[x][y].getInkRGB()));
			}
		}
		return data;
	}
	
	/**
	 * Converts and returns an image converted using the given image processor
	 * if the image has not already been processed
	 * 
	 * @param original
	 * @param imagePro
	 * @param isAlreadyProcessed
	 * @return
	 */
	public byte[] convert(final BufferedImage original, ImageProcessor imagePro, boolean isAlreadyProcessed) {
		
		// The image must be scaled first to 256x192. If it's not already scaled we
		// need to process it again.
		BufferedImage output = original;
		if (!isAlreadyProcessed) {
			output = imagePro.convert(ImageHelper.quickScaleImage(original, SpectrumDefaults.SCREEN_WIDTH, SpectrumDefaults.SCREEN_HEIGHT));
		}
		BitSet bitset = new BitSet(SCR_BITS_SIZE);
		int counter = 0;
		
		// Get our palette data
		ColourData[][] colourData = getBlockedColourData(output);
		
		// Set the bit data for our image in the weird Spectrum format
		counter = toScr(output, bitset, counter, colourData);
		
		// Fix the endianness (Java is big endian by default, Spectrum (Z80) 
		// - AND x86 - are little endian)
		bitset = ByteHelper.reverseBitSet(bitset);
		
		// Write the palette info into the bitset
		for (int y=0; y<SpectrumDefaults.ROWS; y++) {
			for (int x=0; x<SpectrumDefaults.COLUMNS; x++) {
				
				// Convert the Spectrum RGBs to Spectrum palette indexes
				int specInk = SpectrumDefaults.SPECTRUM_INTEGER.get(colourData[x][y].getInkRGB());
				int specPaper = SpectrumDefaults.SPECTRUM_INTEGER.get(colourData[x][y].getPaperRGB());
			
				// Set the correct bits (noddy method but does the trick)
				counter = colourToBitSet(specInk, bitset, counter);
				counter = colourToBitSet(specPaper, bitset, counter);
				
				//Bright
				if (colourData[x][y].isBrightSet()) {
					bitset.set(counter);
				}
				counter++;
				
				// Flash
				//bitset.set(counter);
				counter++;
			}
		}
		return ByteHelper.bitSetToBytes(bitset);
	}
	
	/**
	 * Copies the buffered image output to the given bitset, but only
	 * copies the "ink" values, i.e. those that match the colour data
	 * for the relevant pixel 8x8 range. The data in the bitset is in
	 * correctly formatted scr order.
	 * 
	 * @param output
	 * @param bs
	 * @param counter
	 * @param colourData
	 * @return
	 */
	private int toScr(BufferedImage output, BitSet bs, int counter, ColourData colourData[][]) {
		for (int thirdChunk=0; thirdChunk < SpectrumDefaults.SCREEN_HEIGHT; thirdChunk+=SpectrumDefaults.SCREEN_HEIGHT_THIRD)
			for (int z=0; z<SpectrumDefaults.ATTRIBUTE_BLOCK_SIZE; z++) { // num horiz lines in 8x8 block
				for (int y=thirdChunk+z; y<thirdChunk+SpectrumDefaults.SCREEN_HEIGHT_THIRD; y+=SpectrumDefaults.ATTRIBUTE_BLOCK_SIZE) { // num lines in third of screen 
					for (int x=0;x<SpectrumDefaults.SCREEN_WIDTH; x++) { //line of horiz pixels
						int xBlock = x/SpectrumDefaults.ATTRIBUTE_BLOCK_SIZE;
						int yBlock = y/SpectrumDefaults.ATTRIBUTE_BLOCK_SIZE;
						if (colourData[xBlock][yBlock].getInkRGB() == output.getRGB(x,y)) {
							bs.set(counter);
						}
						++counter;
					}
				}
		}
		return counter; //Should be 49152;
	}
	
	/**
	 * Shockingly bad method to convert Spectrum colour indexes to
	 * the relevant little endian bits. There is probably a better
	 * way of doing this but I'm tired.
	 * 
	 * @param colour
	 * @param bs
	 * @param counter
	 * @return
	 */
	public int colourToBitSet(int colour, BitSet bs, int counter) {
		if (colour == 0) {
			counter +=3;
		} else if (colour == 1 || colour == 8) {
			bs.set(counter);
			counter+=3;//100
		} else if (colour == 2 || colour == 9) {
			counter++;//010
			bs.set(counter);
			counter+=2;
		} else if (colour == 3 || colour == 10) {
			bs.set(counter);//110
			counter++;
			bs.set(counter);
			counter+=2;
		} else if (colour == 4 || colour == 11) {
			counter+=2;//001
			bs.set(counter);
			counter++;
		} else if (colour == 5 || colour == 12) {
			bs.set(counter);//101
			counter+=2;
			bs.set(counter);
			counter++;
		} else if (colour == 6 || colour == 13) {
			counter++;//011
			bs.set(counter);
			counter++;
			bs.set(counter);
			counter++;
		} else if (colour == 7 || colour == 14) {
			bs.set(counter);//111
			counter++;
			bs.set(counter);
			counter++;
			bs.set(counter);
			counter++;
		}
		return counter;
	}
}
