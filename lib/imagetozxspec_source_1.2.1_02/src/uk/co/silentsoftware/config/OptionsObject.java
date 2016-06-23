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

import uk.co.silentsoftware.core.attributestrategy.AttributeStrategy;
import uk.co.silentsoftware.core.attributestrategy.FavourBrightAttributeStrategy;
import uk.co.silentsoftware.core.attributestrategy.FavourHalfBrightAttributeStrategy;
import uk.co.silentsoftware.core.attributestrategy.FavourMostPopularAttributeStrategy;
import uk.co.silentsoftware.core.attributestrategy.ForceBrightAttributeStrategy;
import uk.co.silentsoftware.core.attributestrategy.ForceHalfBrightAttributeStrategy;
import uk.co.silentsoftware.core.colourstrategy.ColourChoiceStrategy;
import uk.co.silentsoftware.core.colourstrategy.FullColourStrategy;
import uk.co.silentsoftware.core.colourstrategy.MonochromeColourStrategy;
import uk.co.silentsoftware.core.converters.image.errordiffusionstrategy.AtkinsonDitherStrategy;
import uk.co.silentsoftware.core.converters.image.errordiffusionstrategy.BurkesDitherStrategy;
import uk.co.silentsoftware.core.converters.image.errordiffusionstrategy.ErrorDiffusionDitherStrategy;
import uk.co.silentsoftware.core.converters.image.errordiffusionstrategy.FloydSteinbergDitherStrategy;
import uk.co.silentsoftware.core.converters.image.errordiffusionstrategy.JarvisJudiceNinkeDitherStrategy;
import uk.co.silentsoftware.core.converters.image.errordiffusionstrategy.LowErrorAtkinsonDitherStrategy;
import uk.co.silentsoftware.core.converters.image.errordiffusionstrategy.NoDitherStrategy;
import uk.co.silentsoftware.core.converters.image.errordiffusionstrategy.StuckiDitherStrategy;
import uk.co.silentsoftware.core.converters.image.orderedditherstrategy.BayerEightByEightDitherStrategy;
import uk.co.silentsoftware.core.converters.image.orderedditherstrategy.BayerFourByFourDitherStrategy;
import uk.co.silentsoftware.core.converters.image.orderedditherstrategy.LightnessOrderedDitherStrategy;
import uk.co.silentsoftware.core.converters.image.orderedditherstrategy.MagicSquareDitherStrategy;
import uk.co.silentsoftware.core.converters.image.orderedditherstrategy.NasikMagicSquareDitherStrategy;
import uk.co.silentsoftware.core.converters.image.orderedditherstrategy.OmegaOrderedDitherStrategy;
import uk.co.silentsoftware.core.converters.image.orderedditherstrategy.OrderedDitherStrategy;
import uk.co.silentsoftware.core.converters.image.orderedditherstrategy.BayerTwoByOneOrderedDitherStrategy;
import uk.co.silentsoftware.core.converters.image.orderedditherstrategy.BayerTwoByTwoOrderedDitherStrategy;

/**
 * The backing object behind the OptionDialog,
 * holding the particular user configuration.
 */
public class OptionsObject {
	
	/**
	 * This class should be used statically
	 */
	private OptionsObject(){}

	/**
	 * The number of frames to sample from the video per second
	 */
	private static double videoFramesPerSecond = 10;

	/**
	 * Prefix identifier for custom basic loaders
	 */
	public static final String CUSTOM_LOADER_PREFIX = "Custom ";
	
	/**
	 * Basic loader for slideshows/video (tap output)
	 */
	private static final BasicLoader[] basicLoaders;

	static {
		basicLoaders = new BasicLoader[]{
				new BasicLoader("Simple (flickers on real hardware)", "simple.tap"),
				new BasicLoader("Black Simple (flickers on real hardware)", "blacksimple.tap"),
				new BasicLoader("Buffered USR 0 (128K)", "buffered.tap"),
				new BasicLoader("Black Buffered USR 0 (128K)", "blackbuffered.tap"),
				new BasicLoader(CUSTOM_LOADER_PREFIX, null)		
		};
	}
	
	/**
	 * The chosen basic loader
	 */
	private static BasicLoader basicLoader = basicLoaders[0]; 

	/**
	 * Dither strategies available
	 */
	private static final ErrorDiffusionDitherStrategy[] errorDithers;
	static {
		errorDithers = new ErrorDiffusionDitherStrategy[]{
			new AtkinsonDitherStrategy(),
			new BurkesDitherStrategy(),
			new FloydSteinbergDitherStrategy(),
			new JarvisJudiceNinkeDitherStrategy(),
			new LowErrorAtkinsonDitherStrategy(),
			new NoDitherStrategy(),
			new StuckiDitherStrategy()
		};
	}
	
	/**
	 * Currently selected error diffusion dither strategy
	 */
	private static ErrorDiffusionDitherStrategy errorDiffusionDitherStrategy = errorDithers[4];

	private static final OrderedDitherStrategy[] orderedDithers;
	static {
		orderedDithers = new OrderedDitherStrategy[]{
			new BayerTwoByOneOrderedDitherStrategy(),	
			new BayerTwoByTwoOrderedDitherStrategy(),
			new OmegaOrderedDitherStrategy(),
			new BayerFourByFourDitherStrategy(),
			new BayerEightByEightDitherStrategy(),
			new LightnessOrderedDitherStrategy(),
			new MagicSquareDitherStrategy(),
			new NasikMagicSquareDitherStrategy()
		};
	}
	
	/**
	 * Currently selected ordered dither strategy
	 */
	private static OrderedDitherStrategy orderedDitherStrategy = orderedDithers[7];
	
	/**
	 * Whether the mode is error diffusion (true) or ordered dither
	 */
	private static volatile boolean errorDiffusion = false;
	
	/**
	 * Scaling modes available
	 */
	private static final ScalingObject[] scalings;
	static {
		scalings = new ScalingObject[]{
			new ScalingObject("None", -1, -1), 
			new ScalingObject("256x192", SpectrumDefaults.SCREEN_WIDTH, SpectrumDefaults.SCREEN_HEIGHT), 
			new ScalingObject("Width Proportional 256x192", SpectrumDefaults.SCREEN_WIDTH, -1),
			new ScalingObject("Height Proportional 256x192", -1, SpectrumDefaults.SCREEN_HEIGHT) 
		};
	}
	
	/**
	 * Currently selected scaling mode
	 */
	public static volatile ScalingObject scaling = scalings[1];
	
	/**
	 * ZX Spectrum scaling mode
	 */
	public static final ScalingObject zxScaling = scalings[1];
	
	/**
	 * Pixel colouring strategy (there used to be a half
	 * colour mode too but I've removed it until I come
	 * up with a better/working replacement)
	 */
	private static final ColourChoiceStrategy[] colourModes;
	static {
		colourModes = new ColourChoiceStrategy[]{
			new FullColourStrategy(),
			new MonochromeColourStrategy()
		};
	}
	
	/**
	 * Currently selected pixel colouring strategy
	 */
	private static volatile ColourChoiceStrategy colourMode = colourModes[0];
	
	/**
	 * Attribute favouritism choice - when colours need to be changed 
	 * to a two colour attribute what is favoured?
	 */
	private static final AttributeStrategy[] attributeModes;
	static {
		attributeModes = new AttributeStrategy[] {
				new FavourHalfBrightAttributeStrategy(),
				new FavourBrightAttributeStrategy(),
				new FavourMostPopularAttributeStrategy(),
				new ForceHalfBrightAttributeStrategy(),
				new ForceBrightAttributeStrategy()
		};
	};
	
	/**
	 * The method of choosing the correct attribute colour
	 */
	private static volatile AttributeStrategy attributeMode = attributeModes[0];
	
	/**
	 * Display frames per second
	 */
	private static volatile boolean fpsCounter = false;	
	
	/**
	 * Display frames per second
	 */
	private static volatile boolean showWipPreview = true;	
	
	/**
	 * Export image formats available
	 */
	private static final String[] imageFormats = new String[]{"png", "jpg"};
	
	/**
	 * Currently selected image export format
	 */
	private static volatile String imageFormat = imageFormats[0];

	/**
	 * Image pre-process contrast setting
	 */
	private static volatile float contrast = 1;
	
	/**
	 * Image pre-process brightness setting
	 */
	private static volatile float brightness = 0;
	
	/**
	 * Image pre-process saturation setting
	 */
	private static volatile float saturation = 0;
	
	/**
	 * Monochrome b/w threshold - determines what
	 * value a colour must be below for it to be
	 * considered black
	 */
	private static volatile int blackThreshold = 384;
	
	/**
	 * Flag for allowing image export
	 */
	private static volatile boolean exportImage = true;
	
	/**
	 * Flag for allowing screen export
	 */
	private static volatile boolean exportScreen = false;
	
	/**
	 * Flag for allowing tape (slideshow) export
	 */
	private static volatile boolean exportTape = false;
	
	/**
	 * The monochrome mode ink colour (spectrum palette index)
	 */
	private static volatile int monochromeInkIndex = 0;
	
	/**
	 * The monochrome mode paper colour (spectrum palette index)
	 */
	private static volatile int monochromePaperIndex = 7;
	
	/**
	 * Intensity for ordered dithering
	 */
	private static volatile int orderedDitherIntensity = 1;

	/**
	 * Threads per CPU (technially this may not be "per" CPU
	 * it's whatever CPU has free resources however it's a good
	 * human metric.
	 */
	private static int threadsPerCPU = 2;
	
	public static int getThreadsPerCPU(){
		return threadsPerCPU;
	}
	public static void setThreadsPerCPU(int threadsPerCPU){
		OptionsObject.threadsPerCPU = threadsPerCPU;
	}
	public static ErrorDiffusionDitherStrategy getErrorDiffusionDitherStrategy() {
		return errorDiffusionDitherStrategy;
	}
	public static void setErrorDitherStrategy(ErrorDiffusionDitherStrategy ditherStrategy) {
		OptionsObject.errorDiffusionDitherStrategy = ditherStrategy;
	}
	public static ErrorDiffusionDitherStrategy[] getErrorDithers() {
		return errorDithers;
	}
	public static ScalingObject[] getScalings() {
		return scalings;
	}
	public static ScalingObject getScaling() {
		return scaling;
	}
	public static ScalingObject getZXDefaultScaling() {
		return zxScaling;
	}
	public static void setScaling(ScalingObject scaling) {
		OptionsObject.scaling = scaling;
	}
	public static float getContrast() {
		return contrast;
	}
	public static void setContrast(float contrast) {
		OptionsObject.contrast = contrast;
	}
	public static float getBrightness() {
		return brightness;
	}
	public static void setBrightness(float brightness) {
		OptionsObject.brightness = brightness;
	}
	public static float getSaturation() {
		return saturation;
	}
	public static void setSaturation(float saturation) {
		OptionsObject.saturation = saturation;
	}
	public static void setFpsCounter(boolean fpsCounter) {
		OptionsObject.fpsCounter = fpsCounter;
	}
	public static void setShowWipPreview(boolean showWipPreview) {
		OptionsObject.showWipPreview = showWipPreview;
	}
	public static String getImageFormat() {
		return imageFormat;
	}
	public static void setImageFormat(String imageFormat) {
		OptionsObject.imageFormat = imageFormat;
	}
	public static String[] getImageFormats() {
		return imageFormats;
	}
	public static boolean getFpsCounter() {
		return fpsCounter;
	}
	public static boolean getShowWipPreview() {
		return showWipPreview;
	}
	public static boolean getExportImage() {
		return exportImage;
	}
	public static void setExportImage(boolean exportImage) {
		OptionsObject.exportImage = exportImage;
	}
	public static boolean getExportScreen() {
		return exportScreen;
	}
	public static void setExportScreen(boolean exportScreen) {
		OptionsObject.exportScreen = exportScreen;
	}
	public static boolean getExportTape() {
		return exportTape;
	}
	public static void setExportTape(boolean exportTape) {
		OptionsObject.exportTape = exportTape;
	}
	public static ColourChoiceStrategy getColourMode() {
		return colourMode;
	}
	public static void setColourMode(ColourChoiceStrategy colourMode) {
		OptionsObject.colourMode = colourMode;
	}
	public static ColourChoiceStrategy[] getColourModes() {
		return colourModes;
	}
	public static int getMonochromeInkIndex() {
		return monochromeInkIndex;
	}
	public static void setMonochromeInkIndex(int monochromeInkIndex) {
		OptionsObject.monochromeInkIndex = monochromeInkIndex;
	}
	public static int getMonochromePaperIndex() {
		return monochromePaperIndex;
	}
	public static void setMonochromePaperIndex(int monochromePaperIndex) {
		OptionsObject.monochromePaperIndex = monochromePaperIndex;
	}
	public static int getBlackThreshold() {
		return blackThreshold;
	}
	public static void setBlackThreshold(int blackThreshold) {
		OptionsObject.blackThreshold = blackThreshold;
	}
	public static OrderedDitherStrategy getOrderedDitherStrategy() {
		return orderedDitherStrategy;
	}
	public static void setOrderedDitherStrategy(
			OrderedDitherStrategy orderedDitherStrategy) {
		OptionsObject.orderedDitherStrategy = orderedDitherStrategy;
	}
	public static OrderedDitherStrategy[] getOrderedDithers() {
		return orderedDithers;
	}
	public static boolean isErrorDiffusion() {
		return errorDiffusion;
	}
	public static void setErrorDiffusion(boolean errorDiffusion) {
		OptionsObject.errorDiffusion = errorDiffusion;
	}
	public static int getOrderedDitherIntensity() {
		return orderedDitherIntensity;
	}
	public static void setOrderedDitherIntensity(int orderedDitherIntensity) {
		OptionsObject.orderedDitherIntensity = orderedDitherIntensity;
	}
	public static AttributeStrategy getAttributeMode() {
		return attributeMode;
	}
	public static void setAttributeMode(AttributeStrategy attributeMode) {
		OptionsObject.attributeMode = attributeMode;
	}
	public static AttributeStrategy[] getAttributeModes() {
		return attributeModes;
	}
	public static BasicLoader[] getBasicLoaders() {
		return basicLoaders;
	}
	public static BasicLoader getBasicLoader() {
		return basicLoader;
	}
	public static void setBasicLoader(BasicLoader basicLoader) {
		OptionsObject.basicLoader = basicLoader;
	}
	public static double getVideoFramesPerSecond() {
		return videoFramesPerSecond;
	}
	public static void setVideoFramesPerSecond(double videoFramesPerSecond) {
		OptionsObject.videoFramesPerSecond = videoFramesPerSecond;
	}
}
