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
package uk.co.silentsoftware;

import uk.co.silentsoftware.ui.ImageToZxSpec;

/**
 * Solution/workaround to allow executable jars
 * to change their memory settings as Java cannot 
 * have its command line properties defined in a 
 * Jar file. Why Sun never implemented anything like 
 * this is beyond me.
 */
public class ImageToZxSpecRunner {

	/**
	 * Minimum heap memory for starting the app and allowing
	 * for reasonable sized images (note deliberately 1 MB
	 * smaller than 512MB due to rounding/non accurate free 
	 * heap calculation) 
	 */
	private final static int MIN_HEAP = 511;	
	
	public static void main(String[] args) throws Exception {
		// Do we have enough memory already (some VMs 
		// and later Java 6 revisions have bigger default 
		// heaps based on total machine memory....)?
		float heapSizeMegs = (Runtime.getRuntime().maxMemory()/1024)/1024;
		
		// Yes so start
		if (heapSizeMegs > MIN_HEAP) {
			ImageToZxSpec.main(args);
			
		// No so set a large heap. Tut - I did use -server mode here originally
		// which does something similar for heap (i.e. can choose a machine specific
		// maximum) but this has some problems with Java 6 R19 for some reason 
		// on my single core machine but worked on Java 6 R13 :( so for now I'll 
		// use a naughty option. I can always re-release with a fix here for Java in 
		// future :) Incidentally -server provides slightly better 2-3 FPS performance
		// during colour Nasik conversion.
		} else {
			String pathToJar = ImageToZxSpecRunner.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
	        ProcessBuilder pb = new ProcessBuilder("java","-XX:+AggressiveHeap", "-classpath", pathToJar, "uk.co.silentsoftware.ui.ImageToZxSpec");
	        pb.start();
		}
	}
}
