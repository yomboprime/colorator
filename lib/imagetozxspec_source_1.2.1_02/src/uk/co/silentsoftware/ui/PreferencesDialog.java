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

import static uk.co.silentsoftware.config.SpectrumDefaults.SPECTRUM_COLORS;
import static uk.co.silentsoftware.config.SpectrumDefaults.SPECTRUM_COLOURS_BRIGHT;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import uk.co.silentsoftware.config.BasicLoader;
import uk.co.silentsoftware.config.OptionsObject;
import uk.co.silentsoftware.config.ScalingObject;
import uk.co.silentsoftware.core.attributestrategy.AttributeStrategy;
import uk.co.silentsoftware.core.colourstrategy.ColourChoiceStrategy;
import uk.co.silentsoftware.core.converters.image.errordiffusionstrategy.ErrorDiffusionDitherStrategy;
import uk.co.silentsoftware.core.converters.image.orderedditherstrategy.OrderedDitherStrategy;
import uk.co.silentsoftware.dispatcher.WorkDispatcher;

/**
 * The options selection dialog
 *
 * Note that this class is tied quite tightly to
 * business logic (e.g. slider ranges/magic numbers) 
 * really just because there isn't need to do
 * the separation for a program this small or at this
 * point (although admittedly its bad practice).
 * Feel free to change this however....
 */
public class PreferencesDialog extends JFrame  {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor that initialises all the tabs
	 */
	public PreferencesDialog() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			// Pah just ignore this error, we'll just have a naff looking UI
		}
		
		// Set up the menu bar
		setIconImage(ImageToZxSpec.IMAGE_ICON.getImage());
		setTitle("Preferences");
		setSize(520,480);
	    setLocationRelativeTo(null); 
	    setResizable(false);
		JTabbedPane pane = new JTabbedPane();
		pane.addTab("Pre-Process Options", createPreProcessOptions());
		pane.addTab("Dither Options", createDitherOptions());
		pane.addTab("Misc Options", createMiscOptions());
		getContentPane().add(pane);
	}
	
	/**
	 * Method that adds the pre process options tab and
	 * its action listeners.
	 * 
	 * TODO: Note this is method is too tightly bound to 
	 * the options and sets hard coded ranges. :(
	 * 
	 * @return
	 */
	private JPanel createPreProcessOptions() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5,2));
		JLabel label = new JLabel("Scaling", JLabel.CENTER);
		final JPanel scalingPadding = new JPanel(new GridLayout(3,1));
		
		final JComboBox scaling = new JComboBox(OptionsObject.getScalings());
		scaling.setSelectedItem(OptionsObject.getScaling());
		scaling.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OptionsObject.setScaling((ScalingObject)scaling.getSelectedItem());
			}
		});
		panel.add(label);
		// TODO: this is a bit lame and Java 1.1 style using padding like this
		scalingPadding.add(new JPanel());
		scalingPadding.add(scaling);
		scalingPadding.add(new JPanel());
		panel.add(scalingPadding);
		label = new JLabel("Video Sampling Rate (FPS)", JLabel.CENTER);
		final JTextField sampleRate = new JTextField();
		final JPanel samplePadding = new JPanel(new GridLayout(3,1));
		sampleRate.setHorizontalAlignment(JTextField.RIGHT);
		sampleRate.setText(""+OptionsObject.getVideoFramesPerSecond());
		sampleRate.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e)
			{
				String value = sampleRate.getText();
				if (value != null && value.trim().length() > 0) {
					try {
						double d = Double.parseDouble(value);
						if (d > 0) {
							OptionsObject.setVideoFramesPerSecond(d);
							return;
						}
					} catch (NumberFormatException nfe) {}
				}
				sampleRate.setText(""+OptionsObject.getVideoFramesPerSecond());

			}
		});
		panel.add(label);
		samplePadding.add(new JPanel());
		samplePadding.add(sampleRate);
		samplePadding.add(new JPanel());
		panel.add(samplePadding);
		label = new JLabel("Contrast Change", JLabel.CENTER);
		final JSlider contrastSlider = new JSlider(-15, 15);
		contrastSlider.setMajorTickSpacing(5);
		contrastSlider.setPaintTicks(true);
		contrastSlider.setPaintLabels(true);
		contrastSlider.setLabelTable(contrastSlider.createStandardLabels(5));
		contrastSlider.setValue(Math.round(OptionsObject.getContrast()-1)*10);
		contrastSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				OptionsObject.setContrast((contrastSlider.getValue()/10f)+1);
			}
		});
		panel.add(label);
		panel.add(contrastSlider);
		label = new JLabel("Saturation Change", JLabel.CENTER);
		final JSlider satSlider = new JSlider(-100, 100);
		satSlider.setMajorTickSpacing(25);
		satSlider.setPaintTicks(true);
		satSlider.setPaintLabels(true);
		satSlider.setLabelTable(satSlider.createStandardLabels(25));
		satSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				OptionsObject.setSaturation(satSlider.getValue()/100f);
			}
		});
		panel.add(label);
		panel.add(satSlider);
		label = new JLabel("Brightness Change", JLabel.CENTER);
		final JSlider brightnessSlider = new JSlider(-100, 100);
		brightnessSlider.setMajorTickSpacing(25);
		brightnessSlider.setPaintTicks(true);
		brightnessSlider.setPaintLabels(true);
		brightnessSlider.setLabelTable(brightnessSlider.createStandardLabels(25));
		brightnessSlider.setValue(Math.round(OptionsObject.getBrightness()));
		brightnessSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				OptionsObject.setBrightness(brightnessSlider.getValue()/100f);
			}
		});
		panel.add(label);
		panel.add(brightnessSlider);
		return panel;
	}
	
	/**
	 * Method that adds the dither options tab and
	 * its action listeners.
	 * 
	 * TODO: Note this is method is too tightly bound to 
	 * the options and sets hard coded ranges.
	 * This method uses untyped processor objects in a vector due
	 * to (so far) being unable to use a unified interface for
	 * error and ordered dither stategies.  :(
	 */
	@SuppressWarnings("all")
	private JPanel createDitherOptions() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(7,2));
		JLabel label = new JLabel("Dithering Mode", JLabel.CENTER);
		Vector v = new Vector(Arrays.asList(OptionsObject.getErrorDithers()));
		v.addAll(Arrays.asList(OptionsObject.getOrderedDithers()));
		final JComboBox dithers = new JComboBox(v);
		Object dither = null;
		if (OptionsObject.isErrorDiffusion()) {
			dither = OptionsObject.getErrorDiffusionDitherStrategy();
		} else {
			dither = OptionsObject.getOrderedDitherStrategy();
		}
		dithers.setSelectedItem(dither);
		dithers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Object obj = dithers.getSelectedItem();
				if (obj instanceof ErrorDiffusionDitherStrategy) {
					OptionsObject.setErrorDitherStrategy((ErrorDiffusionDitherStrategy)obj);
					OptionsObject.setErrorDiffusion(true);
				} else {
					OptionsObject.setOrderedDitherStrategy((OrderedDitherStrategy)dithers.getSelectedItem());
					OptionsObject.setErrorDiffusion(false);
				}
			}
		});
		panel.add(label);
		panel.add(dithers);
		label = new JLabel("Colour Mode", JLabel.CENTER);
		final JComboBox colourModes = new JComboBox(OptionsObject.getColourModes());
		colourModes.setSelectedItem(OptionsObject.getColourMode());
		colourModes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OptionsObject.setColourMode((ColourChoiceStrategy)colourModes.getSelectedItem());
			}
		});
		panel.add(label);
		panel.add(colourModes);	
		
		label = new JLabel("Attribute Favouritism", JLabel.CENTER);
		final JComboBox attributeModes = new JComboBox(OptionsObject.getAttributeModes());
		attributeModes.setSelectedItem(OptionsObject.getAttributeMode());
		attributeModes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OptionsObject.setAttributeMode((AttributeStrategy)attributeModes.getSelectedItem());
			}
		});
		panel.add(label);
		panel.add(attributeModes);
		
		label = new JLabel("Monochrome Ink Colour", JLabel.CENTER);
		panel.add(label);
		final JButton ink = new JButton("Click to set ink");
		ink.setOpaque(true);
		ink.setForeground(SPECTRUM_COLORS.get(SPECTRUM_COLOURS_BRIGHT[OptionsObject.getMonochromeInkIndex()]));
		ink.setBackground(SPECTRUM_COLORS.get(SPECTRUM_COLOURS_BRIGHT[OptionsObject.getMonochromeInkIndex()]));
		ink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int newIndex = OptionsObject.getMonochromeInkIndex()+1;
				if (newIndex >= SPECTRUM_COLOURS_BRIGHT.length-1) {
					newIndex = 0;
				}
				ink.setForeground(SPECTRUM_COLORS.get(SPECTRUM_COLOURS_BRIGHT[newIndex]));
				ink.setBackground(SPECTRUM_COLORS.get(SPECTRUM_COLOURS_BRIGHT[newIndex]));
				OptionsObject.setMonochromeInkIndex(newIndex);
			}
		});
		
		panel.add(ink);
		label = new JLabel("Monochrome Paper Colour", JLabel.CENTER);
		panel.add(label);
		final JButton paper = new JButton("Click to set paper");
		paper.setOpaque(true);
		paper.setForeground(SPECTRUM_COLORS.get(SPECTRUM_COLOURS_BRIGHT[OptionsObject.getMonochromePaperIndex()]));
		paper.setBackground(SPECTRUM_COLORS.get(SPECTRUM_COLOURS_BRIGHT[OptionsObject.getMonochromePaperIndex()]));
		paper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int newIndex = OptionsObject.getMonochromePaperIndex()+1;
				if (newIndex >= SPECTRUM_COLOURS_BRIGHT.length) {
					newIndex = 1;
				}
				paper.setBackground(SPECTRUM_COLORS.get(SPECTRUM_COLOURS_BRIGHT[newIndex]));
				paper.setForeground(SPECTRUM_COLORS.get(SPECTRUM_COLOURS_BRIGHT[newIndex]));
				OptionsObject.setMonochromePaperIndex(newIndex);
			}
		});
		panel.add(paper);
		label = new JLabel("Threshold (Monochrome only)", JLabel.CENTER);
		final JSlider thresholdSlider = new JSlider(0, 768);
		thresholdSlider.setPaintTicks(true);
		thresholdSlider.setPaintLabels(true);
		thresholdSlider.setLabelTable(thresholdSlider.createStandardLabels(128));
		thresholdSlider.setMajorTickSpacing(128);
		thresholdSlider.setValue(OptionsObject.getBlackThreshold());
		thresholdSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				OptionsObject.setBlackThreshold(thresholdSlider.getValue());
			}
		});
		panel.add(label);
		panel.add(thresholdSlider);			
		
		
		label = new JLabel("Intensity (Ordered/Magic only)", JLabel.CENTER);
		final JSlider ditherSlider = new JSlider(1, 10);
		ditherSlider.setMajorTickSpacing(1);
		ditherSlider.setPaintTicks(true);
		ditherSlider.setPaintLabels(true);
		ditherSlider.setSnapToTicks(true);
		ditherSlider.setLabelTable(ditherSlider.createStandardLabels(1));
		ditherSlider.setValue(11-OptionsObject.getOrderedDitherIntensity());
		ditherSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				OptionsObject.setOrderedDitherIntensity(11-ditherSlider.getValue());
			}
		});
		panel.add(label);
		panel.add(ditherSlider);	
		
		return panel;
	}
	
	/**
	 * Method that adds the misc/output options tab and
	 * its action listeners. A file dialog also populates
	 * the custom tape loader.
	 * 
	 * TODO: Note this is method is too tightly bound to 
	 * the options and sets hard coded ranges. :(
	 */
	private JPanel createMiscOptions() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(8,2));
		JLabel label = new JLabel("Image Output Format", JLabel.CENTER);
		panel.add(label);
		final JComboBox formatsBox = new JComboBox(OptionsObject.getImageFormats());
		formatsBox.setSelectedItem(OptionsObject.getImageFormat());
		formatsBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OptionsObject.setImageFormat((String)formatsBox.getSelectedItem());
			}
		});
		panel.add(formatsBox);
		label = new JLabel("Tape/Slideshow/Video Loader", JLabel.CENTER);
		panel.add(label);
		final JComboBox loadersBox = new JComboBox(OptionsObject.getBasicLoaders());
		loadersBox.setSelectedItem(OptionsObject.getBasicLoader());
		loadersBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				BasicLoader loader = (BasicLoader)loadersBox.getSelectedItem();
				OptionsObject.setBasicLoader(loader);
				if (OptionsObject.getBasicLoader().toString().startsWith(OptionsObject.CUSTOM_LOADER_PREFIX)) {
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
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.setAcceptAllFileFilterUsed(false);
					jfc.setFileFilter(new FileFilter() {
						
						public String getDescription() {
							return "TAP pre-compiled BASIC loader";
						}
						public boolean accept(File f) {
							String name = f.getAbsolutePath().toLowerCase();
							return (f.isDirectory() || name.endsWith(".tap")); 
						}
					});
					jfc.setMultiSelectionEnabled(false);
					if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(null)) {
						File file = jfc.getSelectedFile();
						try{
							loader.setName(OptionsObject.CUSTOM_LOADER_PREFIX+"("+file.getName()+")");
							loader.setPath(file.getCanonicalPath());
						} catch(IOException io){
							return;
						}					
					}
				}
			}
		});
		panel.add(loadersBox);
		label = new JLabel("Threads/CPU (apply when stopped)", JLabel.CENTER);
		panel.add(label);
		final JComboBox cpuThreads = new JComboBox();
		for (int i=1; i<6; i++) {
			cpuThreads.addItem(i);
		}
		cpuThreads.setSelectedItem(OptionsObject.getThreadsPerCPU());
		cpuThreads.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OptionsObject.setThreadsPerCPU((Integer)cpuThreads.getSelectedItem());
				WorkDispatcher.setThreadsPerCPU(OptionsObject.getThreadsPerCPU());
			}
		});
		panel.add(cpuThreads);
		label = new JLabel("Show FPS (apply when stopped)", JLabel.CENTER);
		panel.add(label);
		final JCheckBox fpsCheckBox = new JCheckBox("", OptionsObject.getFpsCounter());
		fpsCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OptionsObject.setFpsCounter(fpsCheckBox.isSelected());
			}
		});
		panel.add(fpsCheckBox);
		label = new JLabel("Show WIP Preview", JLabel.CENTER);
		panel.add(label);
		final JCheckBox wipPreviewCheckBox = new JCheckBox("", OptionsObject.getShowWipPreview());
		wipPreviewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OptionsObject.setShowWipPreview(wipPreviewCheckBox.isSelected());
			}
		});
		panel.add(wipPreviewCheckBox);
		panel.add(new JLabel("Output Options", JLabel.CENTER));
		final JCheckBox scrCheckBox = new JCheckBox("SCR Export (.scr)", OptionsObject.getExportScreen());
		scrCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OptionsObject.setExportScreen(scrCheckBox.isSelected());
			}
		});
		panel.add(scrCheckBox);
		panel.add(new JPanel());
		final JCheckBox tapeCheckBox = new JCheckBox("Tape Slideshow Export (.tap)", OptionsObject.getExportTape());
		tapeCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OptionsObject.setExportTape(tapeCheckBox.isSelected());
			}
		});
		panel.add(tapeCheckBox);
		panel.add(new JPanel());
		final JCheckBox imageCheckBox = new JCheckBox("Image Export (.png/.jpg)", OptionsObject.getExportImage());
		imageCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				OptionsObject.setExportImage(imageCheckBox.isSelected());
			}
		});
		panel.add(imageCheckBox);
		return panel;
	}
}
