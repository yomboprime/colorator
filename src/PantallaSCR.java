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
 * Pantalla normal (SCR) de Spectrum  
 */
import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class PantallaSCR extends Pantalla
{
    public static int ATTRS_X = BITMAP_X / 8;
    public static int ATTRS_Y = BITMAP_Y / 8;
    public static int ATTRS_SIZE = ATTRS_X * ATTRS_Y;

    public byte [] attrs;

    public PantallaSCR()
    {
        super();
        attrs = new byte[ATTRS_SIZE];
    }

    public static Pantalla nueva() {
    	return new PantallaSCR();
    }

    public Pantalla clonar() {
        Pantalla p = new PantallaSCR();

        p.copiarDe(this);
        
        return p;
    }

    public void copiarDe(Pantalla p) {

        if ( this.getClass().isAssignableFrom( p.getClass() ) ) {
            
            super.copiarDe(p);

            for (int i = 0; i < ATTRS_SIZE; i++) {
                attrs[i] = ((PantallaSCR)p).attrs[i];
            }
        }
    }

    public void setAttribute(byte attribute, int x, int y) {
        if ( x < 0 || x >= ATTRS_X ) {
            return;
        }
        if ( y < 0 || y >= ATTRS_Y ) {
            return;
        }
        attrs[x + y*ATTRS_X] = attribute;
    }
    
    public byte getAttribute(int x, int y) {
        if ( x < 0 || x >= ATTRS_X ) {
            return 0;
        }
        if ( y < 0 || y >= ATTRS_Y ) {
            return 0;
        }
        return attrs[x + y*ATTRS_X];
    }
    
    public byte getAttributePixel(int i, int j) {
        return getAttribute(i / 8, j / 8);
    }
    
    public void setAttributePixel(int x, int y, byte attrs, boolean ignorarSeleccion) {
    	if ( ignorarSeleccion || seleccion.haySeleccionXY( x, y ) ) {
    		setAttribute(attrs, x / 8, y / 8);
    	}
    }

    public boolean grabarEnFicheroScr(File file) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);

            // bitmap
            grabarBitmap( os );
            
            //atributos
            os.write( attrs );

            return true;
        }
        catch (Exception e) {}
        finally {
            if ( os != null ) {
                try {
                    os.close();
                } catch (IOException e) {}
            }
        }
        return false;
    }
//
//    public String cargarDeFicheroScr(File file) {
//        FileInputStream is = null;
//
//        try {
//            is = new FileInputStream(file);
//            
//            // bitmap
//            byte [] bitmapBytes = new byte[6144];
//            try {
//                is.read(bitmapBytes);
//            }
//            catch (IOException e) {
//                return null;
//            }
//            if ( ! cargarBitmapDeMemoria(bitmapBytes) ) {
//                return "Not enough bytes in file for bitmap";
//            }
//            
//            //atributos
//            int bytesRead = is.read( attrs );
//            if ( bytesRead != ATTRS_SIZE ) {
//                return "Not enough bytes in file for attributes";
//            }
//        }
//        catch (IOException e) {
//        	return "I/O Error while loading image";
//        }
//        finally {
//            if ( is != null ) {
//                try {
//                    is.close();
//                } catch ( Exception e ) {}
//            }
//        }
//        return null;
//    }

    public boolean cargarDeMemoriaScr(byte [] scr) {

    	rellena();
        
        // bitmap
        if ( ! cargarBitmapDeMemoria(scr) ) {
            return false;
        }
        
        //atributos
        int n = attrs.length;
        int l = Pantalla.BITMAP_SIZE / 8;
        for ( int i = 0; i < n; i++ ) {
        	attrs[ i ] = scr[ l++ ];
        }

        return true;
    }
    
    public  byte [] obtenerEnMemoriaScr() {

    	// El array debe tener 256 * 192 / 8 + 256 / 8 * 192 / 8 bytes = 6912 bytes
    	byte datos[] = new byte[ 6912 ];
    	
        // bitmap
    	byte bitmapBytes[] = obtenerBitmapEnMemoria();
    	int bitmapTam = bitmapBytes.length;
    	for ( int i = 0; i < bitmapTam; i++ ) {
    		datos[ i ] = bitmapBytes[ i ];
    	}

    	
        //atributos
        int n = attrs.length;
        int l = Pantalla.BITMAP_SIZE / 8;
        for ( int i = 0; i < n; i++ ) {
        	datos[ l++ ] = attrs[ i ];
        }

        return datos;
    }

    public void normalizar() {
        // Normaliza (paper < ink) en todos los bloques 8x8
        for ( int aj = 0; aj < ATTRS_Y; aj++ ) {
            for ( int ai = 0; ai < ATTRS_X; ai++ ) {
                byte attrs = this.getAttribute( ai, aj );
                int paper = getPaper( attrs );
                int ink = getInk( attrs );
                // If paper > ink...
                if ( paper > ink ) {
                    // Invert paper and ink, and the bitmap
                    attrs = createAttribute( getFlash( attrs ), getBright( attrs ), ink, paper );
                    setAttribute( attrs, ai, aj );
                    for ( int j = 0; j < 8; j++ ) {
                        for ( int i = 0; i < 8; i++ ) {
                            int x = 8 * ai + i;
                            int y = 8 * aj + j;
                            setBitmap( x, y, ! getBitmap( x, y ), false );
                        }
                    }
                }
                else if ( paper == ink ) {
                    boolean flash = paper == 0 ? false : getFlash( attrs );
                    boolean bright = paper == 0 ? false : getBright( attrs );
                    boolean bitmap = paper == 0 ? false : true;
                    attrs = createAttribute( flash, bright, bitmap ? 7 : paper, bitmap ? paper : 7 );
                    setAttribute( attrs, ai, aj );

                    for ( int j = 0; j < 8; j++ ) {
                        for ( int i = 0; i < 8; i++ ) {
                            int x = 8 * ai + i;
                            int y = 8 * aj + j;
                            setBitmap( x, y, bitmap, false );
                        }
                    }
                }
            }
        }
    }
}
