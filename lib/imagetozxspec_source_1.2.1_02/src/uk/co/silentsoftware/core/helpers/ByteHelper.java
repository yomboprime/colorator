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
package uk.co.silentsoftware.core.helpers;

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * Byte and BitSet manipulation and conversion class
 * 
 * Disclaimer: I understand bytes and bits, but my
 * manipulation of them is shockingly bad so I wrote
 * this class to help me with the high level Java 
 * objects.
 */
public final class ByteHelper {

	/**
	 * Private constructor since we want static use only
	 */
	private ByteHelper(){}
	
	/**
	 * Noddy method to reverse a BitSet. 
	 * Basically this is just to fix the endianness of
	 * the image bytes.
	 * 
	 * @param bs
	 * @return
	 */
	public static BitSet reverseBitSet(BitSet bs) {
		final BitSet copy = new BitSet(8);
		int counter = 0;
		int size = bs.length();
		for (int i = 0; i < size; ++i) {
			if (i % 8 == 0 && i != 0) {
				for (int j=8; j>=1; j--) {
					bs.clear(i-j);
					if (copy.get(j-1)){
						bs.set(i-j);
					}
				}
				copy.clear();
				counter = 0;
			}
			if (bs.get(i)) {
				copy.set(counter);
			}
			++counter;		
		}
		return bs;
	}

	/**
	 * Copy a BitSet to a byte array
	 * @param bs
	 * @return
	 */
	public static byte[] bitSetToBytes(BitSet bs) {
		byte[] bytes = new byte[bs.size()/8];
		for (int i = 0; i<bytes.length; ++i) {
			for (int j = 0; j<8; ++j) {
				if (bs.get(i*8+j))
					bytes[i] |= 1 << j;
			}
		}
		return bytes;
	}
	
	/**
	 * Copy bits completely from a source BitSet to 
	 * a destination BitSet starting at the destination
	 * index
	 * 
	 * @param source
	 * @param dest
	 * @param fromIndex
	 * @return
	 */
	public static BitSet copyBits(BitSet source, BitSet dest, int fromIndex) {
		for (int i=0; i<source.length(); i++) {
			if (source.get(i)) {
				dest.set(fromIndex+i);
			}
		}
		return dest;
	}
	
	
	/**
	 * Copy bytes completely from a source byte array to 
	 * a destination byte array starting at the destination
	 * index

	 * @param from
	 * @param to
	 * @param fromIndex
	 * @return the copied byte array
	 */
	public static byte[] copyBytes(byte[] from, byte[] to, int fromIndex) {
		int counter = 0;
		for (byte b : from) {
			to[fromIndex+counter] = b;
			++counter;
		}
		return to;
	}
	
	/**
	 * The missing method on a ByteBuffer - allows copying
	 * of multiple bytes into the buffer.
	 * 
	 * @param bb
	 * @param bytes
	 * @param from
	 */
	public static void put(ByteBuffer bb, byte[] bytes, int from) {
		int counter = 0;
		for (byte b : bytes) {
			bb.put(from+counter, b);
			counter++;
		}
	}
	
	/**
	 * Implementation of an XOR checksum
	 *  
	 * @param result
	 * @return
	 */
	public static byte getChecksum(byte[] result) {
		int checksum = 0;
		for (byte  b: result) {
			checksum^=b;
		}
		return (byte)checksum;
	}	
}