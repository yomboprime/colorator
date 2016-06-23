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
 * Placeholder class for video/slideshow
 * basic loader names and paths
 */
public class BasicLoader
{
	/**
	 * The name to display
	 */
	private String name;
	
	/**
	 * The file (or resource name)
	 */
	private String path;

	public BasicLoader(String name, String path) {
		this.name = name;
		this.path = path;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
