/**
 * 
 *  Copyright (C) 2010  Juan Jose Luna Espinosa juanjoluna@gmail.com

 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, version 3 of the License.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  
 *  Importador de imagenes
 */
import java.io.*;
import java.awt.*;
import java.awt.image.*;

import javax.imageio.*;
import javax.swing.*;

import uk.co.silentsoftware.core.converters.image.ErrorDiffusionConverterImpl;
import uk.co.silentsoftware.core.converters.image.ImageProcessor;
import uk.co.silentsoftware.core.converters.image.OrderedDitherConverterImpl;
import uk.co.silentsoftware.core.converters.spectrum.ScrConverter;


public class Importador
{
	
	public static BufferedImage cargarYEscalarImagen( File fichero ) {

		BufferedImage imagen = null;
        try {
            imagen = ImageIO.read( fichero );
        }
        catch (Exception e) {
            return null;
        }
        
        if ( imagen == null ) {
            return null;
        }

        if ( imagen.getWidth() != Pantalla.BITMAP_X || imagen.getHeight() != Pantalla.BITMAP_Y ) {

        	BufferedImage imagenSalida = new BufferedImage( Pantalla.BITMAP_X, Pantalla.BITMAP_Y, BufferedImage.TYPE_INT_RGB );
        	
        	Graphics2D g = (Graphics2D)imagenSalida.getGraphics();
        	
        	g.setBackground( Color.black );
        	g.fillRect( 0, 0, Pantalla.BITMAP_X, Pantalla.BITMAP_Y );
        	
        	double aspectoSalida = ((double)Pantalla.BITMAP_Y) / ((double)Pantalla.BITMAP_X);
        	double aspectoEntrada = ((double)imagen.getHeight()) / ((double)imagen.getWidth());
        	
        	if ( aspectoEntrada == aspectoSalida ) {
        		g.drawImage( imagen, 0, 0, Pantalla.BITMAP_X - 1, Pantalla.BITMAP_Y - 1, null );
        	}
        	else {
        		
        		int x0 = 0;
        		int y0 = 0;
        		int x1 = Pantalla.BITMAP_X - 1;
        		int y1 = Pantalla.BITMAP_Y - 1;
        		
        		if ( aspectoEntrada > aspectoSalida ) {
        			int d = Math.min( Pantalla.BITMAP_X  / 2 - 1, (int) ( Pantalla.BITMAP_X * ( aspectoSalida / aspectoEntrada ) ) / 2 );
        			x0 = Pantalla.BITMAP_X  / 2 - d;
        			x1 = Pantalla.BITMAP_X  / 2 + d;
	        	}
	        	else {
        			int d = Math.min( Pantalla.BITMAP_Y  / 2 - 1, (int) ( Pantalla.BITMAP_Y * ( aspectoEntrada / aspectoSalida ) ) / 2 );
        			y0 = Pantalla.BITMAP_Y  / 2 - d;
        			y1 = Pantalla.BITMAP_Y  / 2 + d;
	        	}

        		g.drawImage( imagen, x0, y0, x1, y1, 0, 0, imagen.getWidth() - 1, imagen.getHeight() - 1, null );

        	}

        	return imagenSalida;
        }
        
        return imagen;
	}
	
    /**
     * @return String with error message or null if ok
     */ 
    public static String importarImagenSoloHI( BufferedImage imagen, PantallaHi pantalla, boolean pantallaCompleta, JProgressBar progressBar, JComponent componentToUpdate) {

        int BITMAP_HI_X = PantallaHi.ATTRS_HI_X * 8;
        int START_BITMAP_HI_X = 9 * 8;

        int offset = 0;
        if ( ! pantallaCompleta ) {
	        if ( imagen.getWidth() != BITMAP_HI_X || imagen.getHeight() != Pantalla.BITMAP_Y ) {
	            return "The image must be " + BITMAP_HI_X + " x " + Pantalla.BITMAP_Y + " pixels wide.";
	        }
        }
        else {
        	offset = PantallaHi.ATTRS_X * 8;
        }

        int [] conjuntoColImagen = new int[8];
        int [] conjuntoColPantalla = new int[8];
        
        int loopNumber = 0;
        int loops = Pantalla.BITMAP_Y * PantallaHi.ATTRS_HI_X;
        
        for ( int j = 0; j < Pantalla.BITMAP_Y; j++ ) {
            for ( int i = 0; i < PantallaHi.ATTRS_HI_X; i++ ) {

                for ( int c = 0; c < 8; c++ ) {
                    conjuntoColImagen[c] = imagen.getRGB( offset + i * 8 + c, j );
                }
  
                int dMin = Integer.MAX_VALUE;
                byte selectedAttr = 0;
                int selectedBitmap = 0;
                for ( int bright = 0; bright < 2; bright++ ) {
                    for ( int paper = 0; paper < 8; paper++ ) {
                        for ( int ink = paper; ink < 8; ink++ ) {
                            for ( int bitmap = 0; bitmap < 256; bitmap++ ) {
                                
                                for ( int c = 0; c < 8; c++ ) {
                                    conjuntoColPantalla[c] = Pantalla.getColorRGB( ( bitmap & ( 1 << c ) ) != 0, bright != 0, paper, ink ); 
                                }

                                int dSqr = 0;
                                for (int c = 0; c < 8; c++ ) {

                                    int color1 = conjuntoColImagen[c];
                                    int color2 = conjuntoColPantalla[c];
                                    
                                    int dRed = ( ( color1 & 0xFF0000 ) >> 16 ) - ( ( color2 & 0xFF0000 ) >> 16 );
                                    int dGreen = ( ( color1 & 0x00FF00 ) >> 8 ) - ( ( color2 & 0x00FF00 ) >> 8 );
                                    int dBlue = ( color1 & 0x0000FF ) - ( color2 & 0x0000FF );
        
                                    dSqr += dRed * dRed + dGreen * dGreen + dBlue * dBlue;
                                    
                                }

                                if ( dSqr < dMin ) {
                                    dMin = dSqr;
                                    selectedAttr = Pantalla.createAttribute(false, bright!=0, paper, ink);
                                    selectedBitmap = bitmap;
                                }
                            }
                        }
                    }
                }
                
                pantalla.setAttributePixel( START_BITMAP_HI_X + i * 8, j, selectedAttr, false );
                
                for ( int c = 0; c < 8; c++ ) {
                    pantalla.setBitmap( START_BITMAP_HI_X + i * 8 + c, j, ( selectedBitmap & ( 1 << c ) ) !=0, false );
                }

                if ( progressBar != null && componentToUpdate!=null ) {
	                int currentLoopNumber = loopNumber + 1;
	                if ( ( (int) ( ( loopNumber * 100.0f ) / loops ) ) != ( (int) ( ( currentLoopNumber * 100.0f ) / loops ) ) ) {
	                    progressBar.setValue( ( int ) ( ( currentLoopNumber * 100.0f ) / loops ) );
	                    componentToUpdate.paintImmediately(0, 0, componentToUpdate.getWidth(), componentToUpdate.getHeight() );
	                }
	                loopNumber++;
                }
            }
        }
        
        return null;
    }
    
    /**
     * @return String with error message or null if ok
     */ 
    public static String importarImagenEnteraHi( BufferedImage imagen, PantallaHi pantalla ) {

    	// Importa una imagen en colores y la adapta al formato High color con los lados SCR normal (baja resolucion)

    	importarImagenEnteraLow( imagen, pantalla );
        
        return Importador.importarImagenSoloHI( imagen, pantalla, true, null, null);

    }

    /**
     * @return String with error message or null if ok
     */ 
    public static void importarImagenEnteraLow( BufferedImage imagen, Pantalla pantalla ) {

    	// Importa una imagen en colores y la adapta al formato SCR normal (baja resolucion)

        ImageProcessor imageProcessor = new ErrorDiffusionConverterImpl();
//        ImageProcessor imageProcessor = new OrderedDitherConverterImpl();

        ScrConverter screenConverter = new ScrConverter();
        byte[] scr = screenConverter.convert( imagen, imageProcessor, false );

        pantalla.cargarDeMemoriaScr( scr );

    }

    private static int incrementarLoopNumber( int loopNumber, int loops, JProgressBar progressBar, JComponent componentToUpdate ) {

    	int currentLoopNumber = loopNumber + 1;

        if ( ( (int) ( ( loopNumber * 100.0f ) / loops ) ) != ( (int) ( ( currentLoopNumber * 100.0f ) / loops ) ) ) {
            progressBar.setValue( ( int ) ( ( currentLoopNumber * 100.0f ) / loops ) );
            componentToUpdate.paintImmediately(0, 0, componentToUpdate.getWidth(), componentToUpdate.getHeight() );
        }
        loopNumber++;
        
        return loopNumber;
    }
}