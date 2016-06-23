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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import uk.co.silentsoftware.config.OptionsObject;
import uk.co.silentsoftware.config.ScalingObject;

/**
 * Frame to display many preview results
 * This frame has a fixed width and height that is
 * 3 Spectrum images across by 5 in height to cater
 * for all 15 dither strategies currently available.
 */
public class PreviewFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 768;
	private static final int HEIGHT = 960;
	private static final int PANEL_HEIGHT = 600;
	private static BufferedImage previewImage;
	private static int imagePositionX = 0;
	private static int imagePositionY = 0;
	private static JPanel imagePanel;
	
	/**
	 * Dither preview frame that locks the resize
	 * to the fixed dimensions required for all 15 dithers
	 */
	public PreviewFrame() {
		setTitle("Preview");
		setIconImage(ImageToZxSpec.IMAGE_ICON.getImage());
		
		previewImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		imagePanel = new JPanel() {
			private static final long serialVersionUID = 1L;
		
			public void paint(Graphics g) {
				g.drawImage(previewImage, 0, 0, null);
			}		
		};
		imagePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ImageToZxSpec.processPreview();
			}
		});
		JScrollPane scrollPane = new JScrollPane(imagePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(refreshButton, BorderLayout.PAGE_START);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		pack();
		Insets insets = this.getInsets();
		final int maxWidth = WIDTH+insets.left+insets.right+scrollPane.getVerticalScrollBar().getSize().width+2;
		setSize(maxWidth, PANEL_HEIGHT+insets.top+insets.bottom);
		addComponentListener(new ComponentListener()
		{   
		    public void componentResized(ComponentEvent e)
		    {
		    	setSize(maxWidth, getHeight());
		    }
		    public void componentHidden(ComponentEvent e){}
		    public void componentMoved(ComponentEvent e){}
		    public void componentShown(ComponentEvent e){}
		});
	}
	
	/**
	 * Repaint method to allow repainting while threads
	 * are still processing the results to draw on this
	 * dialog
	 */
	public static void repaintImage() {
		imagePanel.repaint(100);
	}

	/**
	 * Retrieves the next point on the dither preview image
	 * that should be drawn on by the preview controller. 
	 * Not synchronized since this is only called by the one
	 * controlling thread.
	 * 
	 * @return
	 */
	public static Point getPoint() {
		final ScalingObject scaleObject = OptionsObject.getZXDefaultScaling();
		int singleImageWidth = scaleObject.getWidth();
		int singleImageHeight = scaleObject.getHeight();
		if (imagePositionX+singleImageWidth > WIDTH) {
			imagePositionY+=singleImageHeight;
			imagePositionX = 0;
		}
		Point p = new Point(imagePositionX, imagePositionY);
		imagePositionX+=singleImageWidth;
		if (imagePositionY >= HEIGHT) {
			imagePositionX = 0;
			imagePositionY = 0;
		}
		return p;
	}
	
	/**
	 * Resets the current point to display at on the panel
	 * as provided by getPoint() to 0,0
	 */
	public static void reset() {
		imagePositionX = 0;
		imagePositionY = 0;
	}

	/**
	 * Retrieves the dither preview image to draw on 
	 * @return
	 */
	public static BufferedImage getPreviewImage() {
		return previewImage;
	}
}
