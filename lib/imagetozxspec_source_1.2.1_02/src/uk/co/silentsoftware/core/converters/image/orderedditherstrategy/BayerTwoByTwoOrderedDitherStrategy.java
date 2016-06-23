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
 * Implementation of the classical Bayer 2x2 ordered dither algorithm
 */
public class BayerTwoByTwoOrderedDitherStrategy extends AbstractOrderedDitherStrategy implements OrderedDitherStrategy {

	private static final int[] COEFFS = new int[]{ 
		0,48,
		32,16
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
		return 2;
	}
	
	/*
	 * {@inheritDoc}
	 */
	public int getMatrixHeight() {
		return 2;
	}

	/*
	 * {@inheritDoc}
	 */
	public String toString() {
		return "Ordered 2x2 (Bayer)";
	}
}
