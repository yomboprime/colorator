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

/**
 * Backing object behind scaling options holding
 * the actual dimensions used by the BufferedImage
 * scaling method.
 */
public class ScalingObject {
	
	/**
	 * The name of this scaling option
	 */
	private final String name;
	
	/**
	 * The new width of the image
	 */
	private final int width;
	
	/**
	 * The new height of the image
	 */
	private final int height;
	
	/**
	 * Default constructor
	 * 
	 * @param name
	 * @param width
	 * @param height
	 */
	public ScalingObject(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public String toString() {
		return name;
	}
}
