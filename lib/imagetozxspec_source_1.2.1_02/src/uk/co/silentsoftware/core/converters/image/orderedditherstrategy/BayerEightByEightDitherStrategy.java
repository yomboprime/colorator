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
 * Implementation of the classical Bayer 8x8 ordered dither algorithm
 */
public class BayerEightByEightDitherStrategy extends AbstractOrderedDitherStrategy implements OrderedDitherStrategy {

	private static final int[] COEFFS = new int[]{
		0,48,12,60,3,51,15,63,
		32,16,44,28,35,19,47,31,
		8,56,4,52,11,59,7,55,
		40,24,36,20,43,27,39,23,
		2,50,14,62,1,49,13,61,
		34,18,46,30,33,17,45,29,
		10,58,6,54,9,57,5,53,
		42,26,38,22,41,25,37,21
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
		return 8;
	}
	
	/*
	 * {@inheritDoc}
	 */
	public int getMatrixHeight() {
		return 8;
	}

	/*
	 * {@inheritDoc}
	 */
	public String toString() {
		return "Ordered 8x8 (Bayer)";
	}
}
