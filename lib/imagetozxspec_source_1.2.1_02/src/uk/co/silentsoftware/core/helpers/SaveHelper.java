package uk.co.silentsoftware.core.helpers;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import uk.co.silentsoftware.config.OptionsObject;

public class SaveHelper {
	
	
	/**
	 * The default file format and suffix
	 */
	public static final String FILE_SUFFIX = ".zx.";
	
	/**
	 * Saves the image to a file
	 * 
	 * @param output
	 * @param destFolder
	 * @param fileName
	 */
	public static void saveImage(final BufferedImage output, File destFolder, String fileName) {
		OutputStream out = null;
		try {
			File f = new File(destFolder.getAbsolutePath()+"/"+fileName.substring(0, fileName.lastIndexOf("."))+FILE_SUFFIX+OptionsObject.getImageFormat());
			if (f.exists())
				f.delete();
			out = new FileOutputStream(f);
			ImageIO.write(output, OptionsObject.getImageFormat(), out);
			out.close();
		} catch(IOException io) {
			if (out != null) {
				try{out.close();}catch(Throwable t){}
			}
		}
	}
	
	/**
	 * Saves raw byte data to the given file
	 * 
	 * @param bytes
	 * @param file
	 */
	public static void saveBytes(byte[] bytes, File file) {
		BufferedOutputStream out = null;
		try {
			if (file.exists())
				file.delete();
			out = new BufferedOutputStream(new FileOutputStream(file));
			out.write(bytes);
			out.close();
		} catch(IOException io) {
			if (out != null) {
				try{out.close();}catch(Throwable t){}
			}
		}
	}
	
	/**
	 * Saves raw byte data to the chosen output folder, but
	 * uses the given file filename + the given suffix
	 * 
	 * @param b
	 * @param destFolder
	 * @param fileName
	 * @param suffix
	 */
	public static void saveBytes(byte[] b, File destFolder, String fileName, String suffix) {
		OutputStream out = null;
		try {
			File f = new File(destFolder.getAbsolutePath()+"/"+fileName.substring(0, fileName.lastIndexOf("."))+suffix);
			if (f.exists())
				f.delete();
			out = new FileOutputStream(f);
			out.write(b);
			out.close();
		} catch(IOException io) {
			if (out != null) {
				try{out.close();}catch(Throwable t){}
			}
		}
	}
}
