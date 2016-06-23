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
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import uk.co.silentsoftware.config.OptionsObject;
import uk.co.silentsoftware.config.ScalingObject;
import uk.co.silentsoftware.config.SpectrumDefaults;
import uk.co.silentsoftware.ui.ImageToZxSpec;

/**
 * Central class for the work engine that initialises
 * a fixed thread pool and submits work for preprocessing
 * and processing. This class also holds queues for the
 * pre processed images (for work in process preview) and
 * and the final images (used for both output and work in 
 * process preview).
 */
public class WorkDispatcher
{
	/**
	 * The number of preview threads based on CPU count*4. Note that while they may
	 * not run on the individual cores it does give a good performance estimate.
	 */
	public static int THREAD_COUNT = Runtime.getRuntime().availableProcessors()*OptionsObject.getThreadsPerCPU();

	/**
	 * The processing thread
	 */
	private static ExecutorService exec = Executors.newFixedThreadPool(THREAD_COUNT);
	
	/**
	 * The pre processed image queue for WIP preview
	 */
	private static Queue<BufferedImage> graphicsPreProcessedQueue = new LinkedBlockingQueue<BufferedImage>();
	
	/**
	 * The actual processed image queue for WIP preview (NOT for saving, although the references 
	 * held are the same object)
	 */
	private static Queue<BufferedImage> graphicsResultsQueue = new LinkedBlockingQueue<BufferedImage>();

	/**
	 * Shut down this engine and threads
	 */
	public static void shutdownNow() {
		exec.shutdownNow();
	}
	
	/**
	 * Reconfigure the engine
	 * 
	 * WARNING: Calling this will stop any current processing!!
	 * 
	 * @param threads
	 */
	public static void setThreadsPerCPU(int threads) {
		exec.shutdownNow();
		try{exec.awaitTermination(10, TimeUnit.SECONDS);}
		catch (InterruptedException e){}
		THREAD_COUNT = Runtime.getRuntime().availableProcessors()*threads;
		exec = Executors.newFixedThreadPool(THREAD_COUNT);
		ImageToZxSpec.enableInput();
	}
	
	/**
	 * Submit a buffered image to be pre processed and processed by a single thread
	 * 
	 * The resulting Future<WorkContainer> contains the resulting buffered image and
	 * optional SCR bytes. This is the object that should then be saved - do not
	 * use the queues in the WorkDispatcher as the queues are popped by the UI threads 
	 * so a result may be missed.
	 * 
	 * @param original
	 * @param scrRequired
	 * @return
	 */
	public static Future<WorkContainer> submitWork(final BufferedImage original, final boolean scrRequired) {
		Future<WorkContainer> future = exec.submit(new Callable<WorkContainer>()
        {
            public WorkContainer call()
            {
            	/* This will improve the WIP preview display
            	if (OptionsObject.getShowWipPreview()) {
            		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            	}
            	*/
            	final WorkProcessor wp = new WorkProcessor();
            	final WorkContainer result = new WorkContainer();
            	BufferedImage preProcessed = wp.preProcessImage(original, OptionsObject.getScaling());
            	BufferedImage processed = wp.convertImage(preProcessed);
            	graphicsPreProcessedQueue.add(preProcessed);
				graphicsResultsQueue.add(processed);
            	result.setImageResult(processed);
            	if (scrRequired) {
            		// If we have the correct dimensions we do not need to image process again
            		if (processed.getWidth() != SpectrumDefaults.SCREEN_WIDTH || processed.getHeight() != SpectrumDefaults.SCREEN_HEIGHT) {	
            			result.setScrData(wp.convertScreen(original, false));
            		} else {
            			result.setScrData(wp.convertScreen(processed, true));
            		}
            	}
                return result;
            }
        });
		return future;
	}
	
	/**
	 * Similar to submit work (the same processing procedure is followed) however
	 * the output is not added to any queues and no SCR output is provided. This
	 * ensures that dither preview work and actual finished result work is not
	 * mixed.
	 * 
	 * @see #submitWork(BufferedImage, boolean)
	 * 
	 * @param original
	 * @param dither
	 * @return
	 */
	public static Future<WorkContainer> submitPreviewWork(final BufferedImage original, final Object dither) {
		Future<WorkContainer> future = exec.submit(new Callable<WorkContainer>()
        {
            public WorkContainer call()
            {
            	final ScalingObject so = OptionsObject.getZXDefaultScaling();
            	final WorkProcessor wp = new WorkProcessor(dither);
            	final WorkContainer result = new WorkContainer();
            	BufferedImage preProcessed = wp.preProcessImage(original, so);
            	BufferedImage processed = wp.convertImage(preProcessed);
                result.setImageResult(processed);
                return result;
            }
        });
		return future;
	}
	
	/**
	 * Returns the best effort sync'd first (peeked) images from 
	 * the pre processed and graphics results queues, i.e. the
	 * source image and it's equivalent result. Not synchronized
	 * to maintain performance at expense of an inexact/out of
	 * sync preview. This method only returns a non null when
	 * both queues have at least one element each.
	 * 
	 * @return
	 */
	public static BufferedImage[] retrieveGraphicsResults() {
		BufferedImage pre = graphicsPreProcessedQueue.peek();
		BufferedImage res = graphicsResultsQueue.peek();
		if (pre == null || res == null) {
			return null;
		}
		return new BufferedImage[] {pre, res};
	}
	
	/**
	 * Pops (polls) the first element off the processing queues,
	 * typically done after a preview has been shown.
	 */
	public static void pollGraphicsResults() {
		if (graphicsResultsQueue.size() > 1) {
			graphicsResultsQueue.poll();
			graphicsPreProcessedQueue.poll(); 
		}
	}
}
