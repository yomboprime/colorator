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
package uk.co.silentsoftware.config;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * ZX Spectrum constants
 */
public class SpectrumDefaults {

	/**
	 * The size of Spectrum the colour "blocks" (8x8 pixels default)
	 */
	public static final int ATTRIBUTE_BLOCK_SIZE = 8;
	
	/**
	 * The spectrum screen width in pixels
	 */
	public static final int SCREEN_WIDTH = 256;
	
	/**
	 * The spectrum screen height in pixels
	 */
	public static final int SCREEN_HEIGHT = 192;
	
	/**
	 * 1/3 of the spectrum screen height in pixels
	 */
	public static final int SCREEN_HEIGHT_THIRD = SCREEN_HEIGHT/3;
	
	/**
	 * The number of character colour rows
	 */
	public static final int ROWS = SCREEN_HEIGHT/ATTRIBUTE_BLOCK_SIZE;
	
	/**
	 * The number of character colour columns
	 */
	public static final int COLUMNS = SCREEN_WIDTH/ATTRIBUTE_BLOCK_SIZE;
	
	/**
	 * Spectrum's primary colours
	 */
	public static final int[] SPECTRUM_COLOURS_HALF_BRIGHT;
	
	/**
	 * Spectrum's half bright set
	 */
	public static final int[] SPECTRUM_COLOURS_BRIGHT;
	
	/**
	 * All Spectrum colours
	 */
	public static final int[] SPECTRUM_COLOURS_ALL;
	
	
	/**
	 * Initialise the trusty Spectrum colour set
	 */
	static {
		SPECTRUM_COLOURS_BRIGHT = new int[] {
				0xFF000000,
				0xFF0000FF, 	
				0xFFFF0000, 	
				0xFFFF00FF,
				0xFF00FF00, 	
				0xFF00FFFF,
				0xFFFFFF00, 
				0xFFFFFFFF
		};
		
		SPECTRUM_COLOURS_HALF_BRIGHT = new int[] {
				0xFF000000,
				0xFF0000CD,	
				0xFFCD0000,
				0xFFCD00CD,
				0xFF00CD00, 	
				0xFF00CDCD,
				0xFFCDCD00,
				0xFFCDCDCD
		};
		
		SPECTRUM_COLOURS_ALL= new int[] {
				0xFF000000,
				0xFF0000FF, 	
				0xFFFF0000, 	
				0xFFFF00FF,
				0xFF00FF00, 	
				0xFF00FFFF,
				0xFFFFFF00, 
				0xFFFFFFFF,
				0xFF000000,
				0xFF0000CD,	
				0xFFCD0000,
				0xFFCD00CD,
				0xFF00CD00, 	
				0xFF00CDCD,
				0xFFCDCD00,
				0xFFCDCDCD
		};
	}
	
	/**
	 * Mappings from RGB -> ZX Spectrum palette (technically this is a slightly intermediate step
	 * as the mappings are not all 1 to 1 with the Spectrum palette, but mappings to a bit set used
	 * by the SCR converter).
	 */
	public static final Map<Integer, Integer> SPECTRUM_INTEGER = new HashMap<Integer, Integer>(15);
	static {
		SPECTRUM_INTEGER.put(0xFF000000, 0);
		SPECTRUM_INTEGER.put(0xFF0000CD, 1); SPECTRUM_INTEGER.put(0xFF0000FF, 8);
		SPECTRUM_INTEGER.put(0xFFCD0000, 2); SPECTRUM_INTEGER.put(0xFFFF0000, 9);
		SPECTRUM_INTEGER.put(0xFFCD00CD, 3); SPECTRUM_INTEGER.put(0xFFFF00FF, 10);
		SPECTRUM_INTEGER.put(0xFF00CD00, 4); SPECTRUM_INTEGER.put(0xFF00FF00, 11);
		SPECTRUM_INTEGER.put(0xFF00CDCD, 5); SPECTRUM_INTEGER.put(0xFF00FFFF, 12);
		SPECTRUM_INTEGER.put(0xFFCDCD00, 6); SPECTRUM_INTEGER.put(0xFFFFFF00, 13);
		SPECTRUM_INTEGER.put(0xFFCDCDCD, 7); SPECTRUM_INTEGER.put(0xFFFFFFFF, 14);
	}
	
	/**
	 * Mappings from RGB -> Color (for caching common colours)
	 */
	public static final Map<Integer, Color> SPECTRUM_COLORS = new HashMap<Integer, Color>(15);
	static {
		SPECTRUM_COLORS.put(0xFF000000, new Color(0xFF000000));
		SPECTRUM_COLORS.put(0xFF0000CD, new Color(0xFF0000CD)); SPECTRUM_COLORS.put(0xFF0000FF, new Color(0xFF0000FF));
		SPECTRUM_COLORS.put(0xFFCD0000, new Color(0xFFCD0000)); SPECTRUM_COLORS.put(0xFFFF0000, new Color(0xFFFF0000));
		SPECTRUM_COLORS.put(0xFFCD00CD, new Color(0xFFCD00CD)); SPECTRUM_COLORS.put(0xFFFF00FF, new Color(0xFFFF00FF));
		SPECTRUM_COLORS.put(0xFF00CD00, new Color(0xFF00CD00)); SPECTRUM_COLORS.put(0xFF00FF00, new Color(0xFF00FF00));
		SPECTRUM_COLORS.put(0xFF00CDCD, new Color(0xFF00CDCD)); SPECTRUM_COLORS.put(0xFF00FFFF, new Color(0xFF00FFFF));
		SPECTRUM_COLORS.put(0xFFCDCD00, new Color(0xFFCDCD00)); SPECTRUM_COLORS.put(0xFFFFFF00, new Color(0xFFFFFF00));
		SPECTRUM_COLORS.put(0xFFCDCDCD, new Color(0xFFCDCDCD)); SPECTRUM_COLORS.put(0xFFFFFFFF, new Color(0xFFFFFFFF));
	}
}