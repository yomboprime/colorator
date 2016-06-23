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

import static uk.co.silentsoftware.core.helpers.ByteHelper.copyBytes;
import static uk.co.silentsoftware.core.helpers.ByteHelper.getChecksum;
import static uk.co.silentsoftware.core.helpers.ByteHelper.put;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import uk.co.silentsoftware.config.OptionsObject;
import uk.co.silentsoftware.core.helpers.ByteHelper;

/**
 * Converter to output a Tape image format file (.tap)
 */
public class TapeConverter {
	
	/**
	 * Outputs an SCR format image into a tap file part
	 * (i.e. a standard data block). 
	 * 
	 * See the following URL for the specification:
	 * http://www.zxmodules.de/fileformats/tapformat.html
	 * 
	 * TODO: Should probably clean up and remove magic numbers
	 * 
	 * @param image
	 * @return
	 */
	public byte[] createTapPart(byte[] image) {
		
		ByteBuffer imageData = ByteBuffer.allocate(2+image.length);
		imageData.order(ByteOrder.LITTLE_ENDIAN);
		imageData.put(0, (byte)255);
		put(imageData, image, 1);
		imageData.put(6913, getChecksum(imageData.array()));
		
		ByteBuffer imageHeader = ByteBuffer.allocate(19);
		imageHeader.order(ByteOrder.LITTLE_ENDIAN);
		imageHeader.put(0, (byte)0);
		imageHeader.put(1, (byte)3);
		put(imageHeader, "Loading...".getBytes(), 2);
		imageHeader.putShort(12, (short)6912);
		imageHeader.putShort(14, (short)16384);
		imageHeader.putShort(16, (short)32768);
		imageHeader.put(18, getChecksum(imageHeader.array()));
		
		ByteBuffer imageHeaderBlock = ByteBuffer.allocate(2+imageHeader.array().length);
		imageHeaderBlock.order(ByteOrder.LITTLE_ENDIAN);
		imageHeaderBlock.putShort((short)(imageHeader.array().length));
		put(imageHeaderBlock, imageHeader.array(), 2);
		
		ByteBuffer imageDataBlock = ByteBuffer.allocate(2+imageData.array().length);
		imageDataBlock.order(ByteOrder.LITTLE_ENDIAN);
		imageDataBlock.putShort((short)(imageData.array().length));
		put(imageDataBlock, imageData.array(), 2);
		byte[] b = new byte[imageHeaderBlock.array().length+imageDataBlock.array().length];
		b = copyBytes(imageHeaderBlock.array(), b, 0);
		b = copyBytes(imageDataBlock.array(), b, imageHeaderBlock.array().length);
		return b;
	}
	
	/**
	 * Returns a the new tap file bytes containing
	 * a basic SCR loader followed by the SCR images
	 * that have been already converted to TAP parts
	 * 
	 * @param parts the TAP parts (data blocks) contain SCR images
	 * @return
	 */
	public byte[] createTap(List<byte[]> parts) {
		byte[] loader = createLoader();
		int size = loader.length;
		for (byte[] b: parts) {
			size+=b.length;
		}
		byte[] result = new byte[size];
		ByteHelper.copyBytes(loader, result, 0);
		int index = loader.length;
		for (byte[] b: parts) {
			ByteHelper.copyBytes(b, result, index);
			index += b.length;
		}
		return result;
	}
	
	private byte[] createLoader() {
		ByteBuffer b = null;
		BufferedInputStream bis = null;
		try {
			InputStream fis = (this.getClass().getClassLoader().getResourceAsStream(OptionsObject.getBasicLoader().getPath()));
			if (fis == null) {
				fis = new FileInputStream(OptionsObject.getBasicLoader().getPath());
			}
			bis = new BufferedInputStream(fis);
			b = ByteBuffer.allocate(4096);
			int data;
			while((data = bis.read()) != -1) {
				b.put((byte)data);
			}	
		} catch(Exception e) {
		} finally {
			if (bis != null) {
				try {bis.close();}catch(IOException io){}
			}
		}
		byte[] sized = new byte[b.position()];
		b.rewind();
		b.get(sized);
		return sized;
	}
}
