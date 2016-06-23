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
 *  Pantalla de Spectrum de alta resolucion de atributos
 */
import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class PantallaHi extends Pantalla
{
    public static int ATTRS_X = 9;
    public static int ATTRS_Y = 24;
    public static int ATTRS_SIZE = ATTRS_X * ATTRS_Y;

    public static int ATTRS_HI_X = 14;
    public static int ATTRS_HI_Y = 192;
    public static int ATTRS_HI_SIZE = ATTRS_HI_X * ATTRS_HI_Y;

    public byte [] attrsIzquierda;
    public byte [] attrsDerecha;
    public byte [] attrsHi;

    public PantallaHi()
    {
        super();
        attrsIzquierda = new byte[ATTRS_SIZE];
        attrsDerecha = new byte[ATTRS_SIZE];
        attrsHi = new byte[ATTRS_HI_SIZE];
    }

    public static Pantalla nueva() {
    	return new PantallaHi();
    }

    public Pantalla clonar() {
        Pantalla p = new PantallaHi();

        p.copiarDe(this);
        
        return p;
    }

    public void copiarDe(Pantalla p) {

        if ( this.getClass().isAssignableFrom( p.getClass() ) ) {
            
            super.copiarDe(p);
            
            
            for (int i = 0; i < ATTRS_SIZE; i++) {
                attrsIzquierda[i] = ((PantallaHi)p).attrsIzquierda[i];
                attrsDerecha[i] = ((PantallaHi)p).attrsDerecha[i];
            }
            for (int i = 0; i < ATTRS_HI_SIZE; i++) {
                attrsHi[i] = ((PantallaHi)p).attrsHi[i];
            }
        }
    }

    public void setAttributeIzquierda(byte attribute, int x, int y) {
        if ( x < 0 || x >= ATTRS_X ) {
            return;
        }
        if ( y < 0 || y >= ATTRS_Y ) {
            return;
        }
        attrsIzquierda[x + y*ATTRS_X] = attribute;
    }
    
    public void setAttributeDerecha(byte attribute, int x, int y) {
        if ( x < 0 || x >= ATTRS_X ) {
            return;
        }
        if ( y < 0 || y >= ATTRS_Y ) {
            return;
        }
        attrsDerecha[x + y*ATTRS_X] = attribute;
    }
    
    public void setAttributeHi(byte attribute, int x, int y) {
        if ( x < 0 || x >= ATTRS_HI_X ) {
            return;
        }
        if ( y < 0 || y >= ATTRS_HI_Y ) {
            return;
        }
        attrsHi[x + y*ATTRS_HI_X] = attribute;
    }

    public byte getAttributeIzquierda(int x, int y) {
        if ( x < 0 || x >= ATTRS_X ) {
            return 0;
        }
        if ( y < 0 || y >= ATTRS_Y ) {
            return 0;
        }
        return attrsIzquierda[x + y*ATTRS_X];
    }
    
    public byte getAttributeDerecha(int x, int y) {
        if ( x < 0 || x >= ATTRS_X ) {
            return 0;
        }
        if ( y < 0 || y >= ATTRS_Y ) {
            return 0;
        }
        return attrsDerecha[x + y*ATTRS_X];
    }

    public byte getAttributeHi(int x, int y) {
        if ( x < 0 || x >= ATTRS_HI_X ) {
            return 0;
        }
        if ( y < 0 || y >= ATTRS_HI_Y ) {
            return 0;
        }
        return attrsHi[x + y*ATTRS_HI_X];
    }

    public byte getAttributePixel(int i, int j) {
        int attrX = i / 8;
        int attrY = j / 8;
        byte attr = 0;
        if ( attrX < ATTRS_X ) {
            attr = getAttributeIzquierda(attrX, attrY);
        }
        else if ( attrX >= ATTRS_X + ATTRS_HI_X ) {
            attr = getAttributeDerecha(attrX - ATTRS_X - ATTRS_HI_X, attrY);
        }
        else {
            attr = getAttributeHi(attrX - ATTRS_X, j);
        }
        return attr;
    }
    
    public void setAttributePixel(int x, int y, byte attrs, boolean ignorarSeleccion) {
    	
    	if ( ignorarSeleccion || seleccion.haySeleccionXY( x, y ) ) {
	        int attrX = x / 8;
	        int attrY = y / 8;
	        if ( attrX < ATTRS_X ) {
	            setAttributeIzquierda(attrs, attrX, attrY);
	        }
	        else if ( attrX >= ATTRS_X + ATTRS_HI_X ) {
	            setAttributeDerecha(attrs, attrX - ATTRS_X - ATTRS_HI_X, attrY);
	        }
	        else {
	            setAttributeHi(attrs, attrX - ATTRS_X, y);
	        }
    	}
    }

    public void desplazarArribaColor() {
        for (int i = PantallaHi.ATTRS_X * 8; i < (PantallaHi.ATTRS_X + PantallaHi.ATTRS_HI_X) * 8; i+=8) {
            for (int j = 0; j < BITMAP_Y - 1; j++) {
            	if ( seleccion.haySeleccionXY( i, j+1) ) {
            		setAttributePixel( i, j, getAttributePixel( i, j+1), true );
            	}
            }
        }
        
        if ( seleccion.haySeleccion ) {
	        for (int i = 0; i < BITMAP_X; i++) {
	            for (int j = 0; j < BITMAP_Y - 1; j++) {
	           		seleccion.seleccionar( i, j, seleccion.haySeleccionXY( i, j + 1 ) );
	            }
	        }
        }
        seleccion.comprobarHaySeleccion();
    }

    public void desplazarAbajoColor() {
        for (int i = PantallaHi.ATTRS_X * 8; i < (PantallaHi.ATTRS_X + PantallaHi.ATTRS_HI_X) * 8; i+=8) {
            for (int j = BITMAP_Y - 1; j > 0 ; j--) {
            	if ( seleccion.haySeleccionXY( i, j-1) ) {
            		setAttributePixel( i, j, getAttributePixel( i, j-1), true);
            	}
            }
        }
        
        if ( seleccion.haySeleccion ) {
	        for (int i = 0; i < BITMAP_X; i++) {
	            for (int j = BITMAP_Y - 1; j > 0; j--) {
	           		seleccion.seleccionar( i, j, seleccion.haySeleccionXY( i, j - 1 ) );
	            }
	        }
        }
        seleccion.comprobarHaySeleccion();
    }

    public boolean grabarEnFichero(File file) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);

            // bitmap
            grabarBitmap( os );
            
            //atributos izquierda
            os.write( attrsIzquierda );
            
            //atributos derecha
            os.write( attrsDerecha );
            
            // atributos alta definicion
            os.write( attrsHi );

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

    public static Pantalla cargarDeFichero(File file) {
        FileInputStream is = null;
        PantallaHi p = new PantallaHi();
        
        p.rellena();
        
        try {
            is = new FileInputStream(file);
            
            // bitmap
            byte [] bitmapBytes = new byte[6144];
            try {
                int bytesRead = is.read(bitmapBytes);
                if ( bytesRead != 6144 ) {
                    return null;
                }
            }
            catch (IOException e) {
                return null;
            }
            p.cargarBitmapDeMemoria(bitmapBytes);

            //atributos izquierda
            int bytesRead = is.read( p.attrsIzquierda );
            if ( bytesRead != ATTRS_SIZE ) {
                return null;
            }
            
            //atributos derecha
            bytesRead = is.read( p.attrsDerecha );
            if ( bytesRead != ATTRS_SIZE ) {
                return null;
            }            
            // atributos alta definicion
            bytesRead = is.read( p.attrsHi );
            if ( bytesRead != ATTRS_HI_SIZE ) {
                return null;
            }

        }
        catch (Exception e) {
            p = null;
        }
        finally {
            if ( is != null ) {
                try {
                    is.close();
                } catch ( Exception e ) {}
            }
        }
        return p;
    }

    public boolean cargarDeMemoriaScr(byte [] scr) {

        rellena();
        
        // bitmap
        cargarBitmapDeMemoria(scr);

        int attrsXScr = BITMAP_X / 8;
        int attrsYScr = BITMAP_Y / 8;
        
        int l = Pantalla.BITMAP_SIZE / 8;
        for (int j = 0; j < attrsYScr; j++) {
            for (int i = 0; i < attrsXScr; i++) {
                byte b = scr[ l + j * attrsXScr + i];
                for (int r = 0; r < 8; r++) {
                    setAttributePixel(i * 8, j * 8 + r, b, false);
                }
            }
        }

        return true;
    }

    public boolean grabarEnFicheroAtributosAlta(File file) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            
            //atributos alta
            os.write( attrsHi );
            
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

    public boolean cargarDeFicheroAtributosAlta(File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            
            //atributos alta
            int bytesRead = is.read( attrsHi );
            if ( bytesRead != ATTRS_HI_SIZE ) {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
        finally {
            if ( is != null ) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return true;
    }
}
