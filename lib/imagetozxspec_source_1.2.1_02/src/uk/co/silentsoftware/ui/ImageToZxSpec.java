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
package uk.co.silentsoftware.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JPopupMenu.Separator;
import javax.swing.filechooser.FileFilter;

import uk.co.silentsoftware.config.OptionsObject;
import uk.co.silentsoftware.core.converters.spectrum.TapeConverter;
import uk.co.silentsoftware.core.helpers.ImageHelper;
import uk.co.silentsoftware.core.helpers.SaveHelper;
import uk.co.silentsoftware.dispatcher.WorkContainer;
import uk.co.silentsoftware.dispatcher.WorkDispatcher;

/**
 * Very simple and noddy image to zx spectrum compatible
 * image converter.
 */
public class ImageToZxSpec {

	/**
	 * Performance enhancement switches :)
	 * 
	 * Disabled by default because not all
	 * systems support them.
	 * 
	 * TODO: Move to Image to ZX Spec runner.
	 */
	static {
		// All systems
		//System.setProperty("sun.java2d.opengl", "true");
		
		// Windows only
		//System.setProperty("sun.java2d.ddscale", "true");
		//System.setProperty("sun.java2d.d3dtexbpp", "16");
	}
	
	/**
	 * Icon for all frames
	 */
	public static final ImageIcon IMAGE_ICON = new ImageIcon(ImageToZxSpec.class.getResource("/logo.png"));
	
	/**
	 * The message to show when idle
	 */
	public static final String DEFAULT_STATUS_MESSAGE = "Waiting...";
	
	/**
	 * The total number of frames/images converted in this session
	 */
	private static int frameCounter = 0;
	
	/**
	 * Actual frames per second for impressing your mates ;)
	 */
	private static volatile float fps;
	
	/**
	 * Input folders to process
	 */
	private static File[] inFiles = null;
	
	/**
	 * Destination folder to output files to
	 */
	private static File outFolder = null;
		
	/**
	 * The spectrum logo when no images are loaded
	 */
	private static volatile BufferedImage specLogo = null;
	
	/**
	 * The ".tap" file converter
	 */
	private static final TapeConverter tapeConverter = new TapeConverter();
	
	/**
	 * The ZX icon for the window
	 */
	private static final String TAP_SLIDESHOW_NAME = "Img2ZXSpec";
	
	/**
	 * The main UI frame
	 */
	private static final JFrame frame = new JFrame("Image to ZX Spec 1.2.1_02 © Silent Software 2010");
	
	/** 
	 * The panel for rendering the images
	 */
	private static JPanel p = null;	
	
	/**
	 * The bottom pane for holding the stauts and convert button
	 */
	private static JPanel bottomPane = null;
	
	/**
	 * The convert image button
	 */
	private static JButton convertButton = null;
	
	/**
	 * The status box
	 */
	private static JTextField statusBox = null;
	
	
	/**
	 * The input folder menu item
	 */
	private static JMenuItem folder = null; 
	
	/**
	 * The output folder menu item
	 */
	private static JMenuItem outputFolder = null;
	
	/**
	 * The menu bar
	 */
	private static JMenuBar menubar = null;
	
	/**
	 * The options dialog
	 */
	private static final PreferencesDialog preferences = new PreferencesDialog();
	
	/**
	 * The preview window
	 */
	private static final PreviewFrame preview = new PreviewFrame();
	
	/**
	 * The debug frames per second counter
	 */
	private static Thread fpsThread;
	
	/**
	 * Create a single thread for the rendering and inits the UI
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		createUserInterface();
	}
	
	/**
	 * Create the natty UI/preview window. Very monolithic
	 * but I don't intend to change this much, and if I do I'll be
	 * sure to rewrite 8P
	 */
	public static void createUserInterface() {
	
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			// Pah just ignore this error, we'll just have a naff looking UI
		}
		
		WindowListener windowListener = new WindowAdapter() {
			public void windowClosing(WindowEvent w) {
				WorkDispatcher.shutdownNow();
				frame.setVisible(false);
				frame.dispose();
			}
		};
		frame.addWindowListener(windowListener);
		
		// Set up the menu bar
		menubar = new JMenuBar();
		frame.setIconImage(IMAGE_ICON.getImage());
		frame.setJMenuBar(menubar);
		frame.getContentPane().setLayout(new BorderLayout());
		
		// Add the panel for rendering the original + result
		p = new JPanel(){
			
			static final long serialVersionUID = 0;
			
			public void paint(Graphics g) {
				super.paint(g);
				BufferedImage[] results = WorkDispatcher.retrieveGraphicsResults();
				if (results != null) {
					g.drawImage(results[0], 0, 0, null);
					g.drawImage(results[1], results[1].getWidth(), 0, null);
					if (OptionsObject.getFpsCounter()) {
						g.setColor(Color.WHITE);
						g.drawString("FPS: "+fps, 20, 20);
					}
				} else if (specLogo != null) {
					g.drawImage(specLogo, 0, 0, null);
				}
			}
			
		};
	    frame.getContentPane().add(p, BorderLayout.CENTER);
	    statusBox = new JTextField(DEFAULT_STATUS_MESSAGE);
	    statusBox.setEditable(false);
		convertButton = new JButton("Convert");
		bottomPane = new JPanel();
		bottomPane.setLayout(new GridLayout(2, 1));
		bottomPane.add(statusBox);
		bottomPane.add(convertButton);
		frame.getContentPane().add(bottomPane, BorderLayout.PAGE_END);
		
		// Add the menu bar options
		JMenu fileMenu = new JMenu("File");
		
		// Input folder
		folder = new JMenuItem("Choose input files");
		folder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser(){
					static final long serialVersionUID = 1L;
					public void approveSelection() {
						for (File f:this.getSelectedFiles()) {
							if (f.isDirectory()) {
								return;
							}
						}
						super.approveSelection();
					}
				};
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				if (inFiles != null && inFiles.length > 0) {
					jfc.setCurrentDirectory(inFiles[0].getParentFile());
				}
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.setFileFilter(new FileFilter() {
					
					public String getDescription() {
						return "AVI, MOV Videos, JPG, PNG and GIF Images";
					}
					public boolean accept(File f) {
						String name = f.getAbsolutePath().toLowerCase();
						return (f.isDirectory()
								|| name.endsWith(".gif") 
								|| name.endsWith(".png")
								|| name.endsWith(".jpg")
								|| name.endsWith(".jpeg") 
								|| name.endsWith(".avi") 
								|| name.endsWith(".mov") 
								&& !name.contains(SaveHelper.FILE_SUFFIX));
					}
				});
				
				jfc.setMultiSelectionEnabled(true);
				if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(null)) {
					inFiles = jfc.getSelectedFiles();
					processPreview();
				}
			}
		});
		
		// Output folder
		outputFolder = new JMenuItem("Choose output folder");
		outputFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser("Choose output directory");
				if (outFolder != null) {
					jfc.setCurrentDirectory(outFolder);
				}
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (JFileChooser.APPROVE_OPTION == jfc.showSaveDialog(null)) {
					outFolder = jfc.getSelectedFile();
				}
			}
		});
		
		// Converter button and listener to start processing thread
		// Includes validation checks
		convertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (inFiles == null || inFiles.length == 0) {
					JOptionPane.showMessageDialog(null, 
							"Please choose the input images first", 
							"Files not selected", 
							JOptionPane.OK_OPTION 
					);
					return;
				} 
				if (outFolder == null) {
					JOptionPane.showMessageDialog(null, 
							"Please choose a folder for outputing images first", 
							"Folder not selected", 
							JOptionPane.OK_OPTION 
					);
					return;
				} 
				
				
				try {
					startFpsCalculator();
					processFiles();
				} catch(Exception e) {
					JOptionPane.showMessageDialog(null, "An error has occurred: "+e.getMessage(), "Guru meditation", JOptionPane.OK_OPTION);  			
				}
			}
		});
		
		// Exit button
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				frame.dispose();
				System.exit(0);
			}
		});
		
		fileMenu.add(folder);
		fileMenu.add(outputFolder);
		fileMenu.add(new Separator());
		fileMenu.add(exit);
		menubar.add(fileMenu);
		
		JMenu optionsMenu = new JMenu("Options");
		JMenuItem preferencesItem = new JMenuItem("Preferences");
		preferencesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (!preferences.isShowing()) {
					preferences.setVisible(true);
				}
			}
		});
		final JMenuItem previewItem = new JMenuItem("View Dither Preview");
		previewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (!preview.isShowing()) {
					preview.setVisible(true);
					processPreview();
				}
			}
		});
		optionsMenu.add(previewItem);
		optionsMenu.addSeparator();
		optionsMenu.add(preferencesItem);
		menubar.add(optionsMenu);
		
		JMenu about = new JMenu("About");
		JMenuItem aboutZx = new JMenuItem("About Image to ZX Spec");
		about.add(aboutZx);
		aboutZx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				long maxHeap = Runtime.getRuntime().maxMemory();
				long currentHeapUse = Runtime.getRuntime().totalMemory();
				int cpus = Runtime.getRuntime().availableProcessors();
				JOptionPane.showMessageDialog(null, 
						"Image to ZX Spec is a simple to use program which applies a Sinclair ZX Spectrum\n" +
						"effect to images, creates actual Spectrum compatible slideshows from images or\n" +
						"Spectrum \"video\" from compatible video files (see JMF 2.1.1 Cross Platform formats).\n\n"+
						"The software is fully cross platform and multi CPU capable and uses aggressive\n"+
						"memory options to enable some of the fastest conversion rates possible.\n\n"+
						"PNG is the default output of choice since it doesn't introduce unwanted artifacts,\n" +
						"and for most common usages of this program results in smaller files than JPEG.\n" +
						"SCR (Screen) output is also available, however any image is resized to 256x192\n" +
						"pixels regardless of chosen options.\n"+
						"TAP (Tape) format will convert images into a slideshow or video (type determined at\n"+
						"runtime) for use on emulators or a real Spectrum (with suitable conversion of .tap\n"+
						"to real tape).\n\n"+
						"The Image to ZX Spec code does not use more than the basic Java 1.6 API and Java\n"+
						"Media Framework (JMF) as I wanted to teach myself about low level colour dithering,\n"+
						"image manipulation and new Java threading capabilities.\n\n" +
						"Image To ZX Spec is licensed under the GNU General Public Licence (GPL) 2.0 - no \n" +
						"warranty is provided. You are free to make deriative works, as long as you release\n" +
						"the amended source code - full details can be found in the license text that\n" +
						"accompanied this software.\n" +
						"The software has been driven forward on features and rapid prototyping to prevent the\n" +
						"project stalling so I'm making no apologies for any sloppy code you may still find :)\n\n"+
						"This software is copyright Silent Software 2010 (Benjamin Brown).\n\n" +
						"Processors: "+cpus+"\n"+
						"Used Java Memory: "+currentHeapUse/1024/1024+"MB (same as total if aggressive settings)\n"+
						"Total Java Memory: "+maxHeap/1024/1024+"MB\n\n"+
						"Visit Silent Software at http://www.silentsoftware.co.uk", 
						"About Image to ZX Spec", JOptionPane.INFORMATION_MESSAGE, IMAGE_ICON);
			}
		});
		menubar.add(about);
		
		// Default settings for window (and sets it relative to screen centre)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menubar);
	    frame.setVisible(true);
	    frame.setResizable(false);
	    
	    // Show a nice logo while nothing loaded
	    try {
			specLogo = ImageIO.read(ImageToZxSpec.class.getResource("/sinclair.png").openStream());
			Dimension dim = new Dimension(480,240+bottomPane.getHeight()+frame.getInsets().top+frame.getInsets().bottom+menubar.getHeight());
			frame.setSize(dim);
		    frame.setPreferredSize(dim);
			frame.repaint();
			frame.setLocationRelativeTo(null);     
		} catch (IOException e1) {
		}
	}
	
	/**
	 * Method to begin the (multi dither, not WIP) preview
	 * which submits work to the work engine and controls the
	 * UI settings and validates the content being loaded.
	 */
	static void processPreview() {
		if (!preview.isShowing())
			return;
		PreviewFrame.reset();
		final List<Future<WorkContainer>> futures = new ArrayList<Future<WorkContainer>>(OptionsObject.getOrderedDithers().length+OptionsObject.getErrorDithers().length);
		try {
			if (inFiles == null) {
				return;
			}
			if (inFiles.length > 0) {
				File f = inFiles[0];
				if (f.getPath().endsWith(".avi") || f.getPath().endsWith(".mov")) {
					setStatusMessage("Preview only works for images - a video was selected.");
					return;
				}
				disableInput();
				
				BufferedImage image = ImageIO.read(f);
				for(Object dither : OptionsObject.getOrderedDithers()) {
					Future<WorkContainer> future = WorkDispatcher.submitPreviewWork(image, dither);
					futures.add(future);
				}			
				for(Object dither : OptionsObject.getErrorDithers()) {
					Future<WorkContainer> future = WorkDispatcher.submitPreviewWork(image, dither);
					futures.add(future);
				}
			}
			startPreviewWaiter(futures);
			
		} catch (Exception e) {
			setStatusMessage(e.getMessage());
		}
	}
	
	/**
	 * A "waiter" which waits for the work engine (the WorkDispatcher) to return
	 * a result so it can be displayed in the dither preview dialog when it is
	 * ready. The list of futures contains the work dispatcher's work in progress
	 * Future tasks.
	 * 
	 * @param futures
	 */
	private static void startPreviewWaiter(final List<Future<WorkContainer>> futures) {
		Thread t = new Thread() {
			public void run() {
				try {
					for (Future<WorkContainer> future : futures) {
						WorkContainer wc = null;
						try{wc = future.get();}catch (Exception e){}
						if (wc != null) {
							BufferedImage img = wc.getImageResult();
							Point p = PreviewFrame.getPoint();
							BufferedImage preview = PreviewFrame.getPreviewImage();
							ImageHelper.copyImage(img, preview, p);
							PreviewFrame.repaintImage();
						}
					}
				}
				finally {
					enableInput();
				}
			}
		};
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * Method to begin the WIP preview which submits work to the work engine 
	 * and controls the UI settings and validates the content being loaded.
	 * In particular if a video is found in the input only this file is processed.
	 */
	public static void processFiles(){
		disableInput();
		convertButton.paintImmediately(convertButton.getBounds());
		Thread t = new Thread(){
		
			@Override
			public void run() {
				try {
					for(File f : inFiles) {
						// We have a video so only deal with this file
						String path = f.getPath().toLowerCase();
						if (path.endsWith(".avi") || path.endsWith(".mov")) {
							return;
						}
					}
					processSingleFiles();
				} catch (Exception e) {
					setStatusMessage(e.getMessage());
					enableInput();
				}
			}
		};
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * Inner core method for the process files method that specifically deals 
	 * with a single files. The files are loaded as images and these are put
	 * into the work engine for processing.
	 * A waiter thread is initialised to pick the completed work up and as the
	 * images are loaded in order the future tasks' results remain correctly 
	 * ordered when they are collected.
	 * 
	 * @throws InterruptedException
	 * @throws NoPlayerException
	 * @throws IOException
	 * @throws ExecutionException
	 */
	private static void processSingleFiles() throws InterruptedException, IOException, ExecutionException {
		if (inFiles.length == 0) {
			return;
		}
		List<Future<WorkContainer>> futures = new ArrayList<Future<WorkContainer>>(inFiles.length);
		
		// Make sure we get through these as fast as possible as they are using memory while 
		// being held in the work engine awaiting collection and outputting before a GC can occur.
		// Due to numerous other processing threads and this being the feeder/producer it must be 
		// high priority
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		for(File f : inFiles) {
			try {
				BufferedImage image = ImageIO.read(f);
				setStatusMessage("Submitting work "+f.getName());
				Future<WorkContainer> future = WorkDispatcher.submitWork(image, OptionsObject.getExportScreen() || OptionsObject.getExportTape());
				futures.add(future);
			// Sad fact of this implementation is that all video is loaded in in one go
			// due to originally being designed for images only which can result in
			// OOMEs.
			// TODO: Refactor some day
			} catch(OutOfMemoryError oome) {
				setStatusMessage(oome.getMessage());
				break;
			}
		}
		startWaiter(futures, null);
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
	}
	
	/**
	 * A "waiter" which waits for the work engine (the WorkDispatcher) to return
	 * a result so it can be displayed in the WIP preview when it is
	 * ready. The list of futures contains the work dispatcher's work in progress
	 * Future tasks. When the results are returned as buffered images or scrs the 
	 * relevant scr, tap, png, jpg etc is created. In the case of video the videoName
	 * is used for the file name base.
	 * 
	 * @param futures
	 */
	private static void startWaiter(final List<Future<WorkContainer>> futures, final String videoName) {
		// Old school threading for the work results handler
		Thread t = new Thread() {
			long time = System.currentTimeMillis();
			public void run() {
				try {
					List<byte[]> converted = new ArrayList<byte[]>();
					specLogo = null;
					final int size = futures.size();
					for (int i=0; i < size; i++) {
						Future<WorkContainer> future = futures.get(i);
						WorkContainer result = future.get();
						setStatusMessage("Processed image "+(i+1)+" of "+size);
						BufferedImage imageResult = result.getImageResult();
						WorkDispatcher.pollGraphicsResults();
						if (OptionsObject.getShowWipPreview()) {
							Dimension dim = new Dimension(imageResult.getWidth()*2,imageResult.getHeight()+bottomPane.getHeight()+frame.getInsets().top+frame.getInsets().bottom+menubar.getHeight());
							if (!frame.getSize().equals(dim)) {
								frame.setSize(dim);
							}
							frame.getContentPane().repaint();
							//p.paintImmediately(0, 0, imageResult.getWidth()*2, imageResult.getHeight());
						}
						
						if (OptionsObject.getExportTape()) {
							converted.add(tapeConverter.createTapPart(result.getScrData()));
						}
						String name = videoName;
						if (videoName != null) {
							name=name+=i;
						} else {
							name = inFiles[i].getName();
						}
						if (OptionsObject.getExportImage()) {
							SaveHelper.saveImage(imageResult, outFolder, name);
						}
						if (OptionsObject.getExportScreen()) {
							SaveHelper.saveBytes(result.getScrData(), new File(outFolder+"/"+name+".scr"));
						}	
						++frameCounter;
					}
					if (OptionsObject.getExportTape() && converted.size() > 0) {
						byte[] result = tapeConverter.createTap(converted);
						if (result != null && result.length > 0) {
							SaveHelper.saveBytes(result, new File(outFolder+"/"+TAP_SLIDESHOW_NAME+".tap"));
						}
					}
					setStatusMessage("Time taken (secs): "+(System.currentTimeMillis()-time)/1000);
					Thread.sleep(3000);
					setStatusMessage(DEFAULT_STATUS_MESSAGE);
					
					// Now really is the best time to GC as we don't want it during a render
					// when we start getting low on free memory (note I usually don't do this
					// stuff manually but this really is a place where it should be used if the
					// GC'er hasn't worked it out)!
					System.gc();
				} catch(Exception e){
					setStatusMessage(e.getMessage());
				} finally {
					enableInput();
				}
			} 
		};
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * A simple implementation of an FPS calculator that uses actual
	 * time difference rather than expected sleep time to determine the
	 * real FPS. finalised variables in the vague hope these will be
	 * optimised and GC cleaned up in the young generation.
	 */
	private static void startFpsCalculator() {
		// Old school threading for the fps calculator
		if (OptionsObject.getFpsCounter() && (fpsThread == null || !fpsThread.isAlive())) {
			fpsThread = new Thread() {
				public void run() {
					frameCounter = 0;
					while (OptionsObject.getFpsCounter()) {
						final float start = frameCounter;
						final long time = System.currentTimeMillis();
						try {Thread.sleep(2000);} catch(Exception e){}
						final long diff = System.currentTimeMillis()- time;
						final float end = frameCounter;
						fps = (end-start)/(diff/1000);
					}
				}
			};
			fpsThread.setDaemon(true);
			fpsThread.setPriority(Thread.MIN_PRIORITY);
			fpsThread.start();
		}
	}
	
	/**
	 * Sets a status or error message above the convert button
	 * @param message
	 */
	public static void setStatusMessage(final String message) {
		statusBox.setText(message);
		statusBox.repaint(1000);
	}
	
	/**
	 * Reset's the UI state to enable input
	 */
	public static void enableInput() {
		convertButton.setEnabled(true);
		folder.setEnabled(true);
		outputFolder.setEnabled(true);
	}
	
	/**
	 * Disable the UI state to prevent input
	 */
	public static void disableInput() {
		convertButton.setEnabled(false);
		folder.setEnabled(false);
		outputFolder.setEnabled(false);
	}
}
