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

/**
 * Implementation of the "Lightness" ordered dither algorithm found 
 * on news groups (author unknown)
 */
public class LightnessOrderedDitherStrategy extends AbstractOrderedDitherStrategy implements OrderedDitherStrategy {

	private static final int[] COEFFS = new int[]{ 
		0,58,14,54,3,57,13,53,0,58,14,54,3,57,13,53,
		32,16,46,30,35,19,45,29,32,16,46,30,35,19,45,
		29,8,48,4,62,11,51,7,61,8,48,4,62,11,51,
		7,61,40,24,36,20,43,27,39,23,40,24,36,20,43,
		27,39,23,2,56,12,52,1,59,15,55,2,56,12,52,
		1,59,15,55,34,18,44,28,33,17,47,31,34,18,44,
		28,33,17,47,31,10,50,6,60,9,49,5,63,10,50,
		6,60,9,49,5,63,42,26,38,22,41,25,37,21,42,
		26,38,22,41,25,37,21,0,58,14,54,3,57,13,53,
		0,58,14,54,3,57,13,53,32,16,46,30,35,19,45,
		29,32,16,46,30,35,19,45,29,8,48,4,62,11,51,
		7,61,8,48,4,62,11,51,7,61,40,24,36,20,43,
		27,39,23,40,24,36,20,43,27,39,23,2,56,12,52,
		1,59,15,55,2,56,12,52,1,59,15,55,34,18,44,
		28,33,17,47,31,34,18,44,28,33,17,47,31,10,50,
		6,60,9,49,5,63,10,50,6,60,9,49,5,63,42,
		26,38,22,41,25,37,21,42,26,38,22,41,25,37,21
	};
	
	/*
	 * {@inheritDoc}
	 */
	public int[] getCoefficients() {
		return COEFFS;
	}
	
	/*
	 * {@inheritDoc}
	 */
	public int getMatrixWidth() {
		return 16;
	}
	
	/*
	 * {@inheritDoc}
	 */
	public int getMatrixHeight() {
		return 16;
	}

	/*
	 * {@inheritDoc}
	 */
	public String toString() {
		return "Ordered 16x16 (Lightness)";
	}
}
