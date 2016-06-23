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

/**
 * Wrapper container for holding processed
 * and completed work ready for output and display
 */
public class WorkContainer {

	/**
	 * The resulting image
	 */
	private BufferedImage imageResult;
	
	/**
	 * The equivalent SCR representation of this image
	 */
	private byte[] scrData;
	
	public BufferedImage getImageResult()
	{
		return imageResult;
	}
	public void setImageResult(BufferedImage imageResult)
	{
		this.imageResult = imageResult;
	}
	public byte[] getScrData()
	{
		return scrData;
	}
	public void setScrData(byte[] scrData)
	{
		this.scrData = scrData;
	}
	
}
