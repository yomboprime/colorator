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
 *  Pantalla de Spectrum generica
 */
import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;

public class Pantalla
{

    public static int BITMAP_X = 256;
    public static int BITMAP_Y = 192;
    public static int BITMAP_SIZE = BITMAP_X * BITMAP_Y;

    static int NG = 203;
    public static Color colores[][] = { { Color.black,    // negro
                                          Color.black },
                                    
                                        { new Color(0,0,NG), // azul
                                          new Color(0,0,255) },
                                    
                                        { new Color(NG,0,0), // rojo
                                          new Color(255,0,0) },
                                    
                                        { new Color(NG,0,NG), // magenta
                                          new Color(255,0,255) },
                                    
                                        { new Color(0,NG,0), // verde
                                          new Color(0,255,0) },
                                    
                                        { new Color(0,NG,NG), // azul cielo
                                          new Color(0,255,255) },
                                    
                                        { new Color(NG,NG,0), // amarillo
                                          new Color(255,255,0) },
                                    
                                        { new Color(NG,NG,NG), // blanco
                                          new Color(255,255,255) }
                                       };
                  
    public static int coloresRGB[][] = { { 0x000000,    // negro
                                          0x000000 },
                                    
                                        { 0x0000CB, // azul
                                          0x0000FF },
                                    
                                        { 0xCB0000, // rojo
                                          0xFF0000 },
                                    
                                        { 0xCB00CB, // magenta
                                          0xFF00FF },
                                    
                                        { 0x00CB00, // verde
                                          0x00FF00 },
                                    
                                        { 0x00CBCB, // azul cielo
                                          0x00FFFF },
                                    
                                        { 0xCBCB00, // amarillo
                                          0xFFFF00 },
                                    
                                        { 0xCBCBCB, // blanco
                                          0xFFFFFF }
                                       };
    
    public boolean [] bitmap;

    public Seleccion seleccion;
    
    public Pantalla()
    {
        bitmap = new boolean[BITMAP_SIZE];
        seleccion = new Seleccion();
    }

    public static Pantalla nueva() {
    	return new Pantalla();
    }
    
    public Pantalla clonar() {
        // implement in subclasses
        return null;
    }

    public void copiarDe(Pantalla p) {
        for (int i = 0; i < BITMAP_SIZE; i++) {
            bitmap[i] = p.bitmap[i];
            seleccion.bitmap[i] = p.seleccion.bitmap[i];
            seleccion.haySeleccion = p.seleccion.haySeleccion;
        }
    }

    public static int getPaper(byte attribute) {
        return (attribute & 0x38) >> 3;
    }
    
    public static int getInk(byte attribute) {
        return attribute & 0x07;
    }
    
    public static boolean getFlash(byte attribute) {
        return (attribute & 0x80) != 0;
    }
    
    public static boolean getBright(byte attribute) {
        return (attribute & 0x40) != 0;
    }
    
    public static byte createAttribute(boolean flash, boolean bright, int paper, int ink) {
        byte attr = 0;
        attr = (byte)(ink & 7);
        attr |= (byte)((paper & 7) << 3);
        attr |= bright? 0x40: 0x00;
        attr |= flash? 0x80: 0x00;
        return attr;
    }
    
    public void setBitmap(int x, int y, boolean v, boolean ignorarSeleccion) {
        if ( x < 0 || x >= BITMAP_X ) {
            return;
        }
        if ( y < 0 || y >= BITMAP_Y ) {
            return;
        }
        int p = x + y * BITMAP_X;
        if ( ignorarSeleccion || seleccion.haySeleccionPixel( p ) ) {
        	bitmap[ p ] = v;
        }
    }
    
    public boolean getBitmap(int x, int y) {
        if ( x < 0 || x >= BITMAP_X ) {
            return false;
        }
        if ( y < 0 || y >= BITMAP_Y ) {
            return false;
        }
        return bitmap[x + y*BITMAP_X];
    }
    
	public void pintarPixel(int x, int y, boolean tinta, byte attrs, boolean transparentePaper, boolean transparenteInk, boolean transparenteBright, boolean transparenteFlash, boolean soloAtributo, boolean ignorarSeleccion ) {

		if ( ! soloAtributo ) {
			setBitmap(x, y, tinta, ignorarSeleccion );
		}

        if ( ! transparentePaper || ! transparenteInk || ! transparenteBright || ! transparenteFlash ) {

            int paper = getPaper(attrs);
            int ink = getInk(attrs);
            boolean bright = getBright(attrs);
            boolean flash = getFlash(attrs);
            if ( transparentePaper ) {
                paper = getPaper( getAttributePixel(x, y) );
            }
            if ( transparenteInk ) {
                ink = getInk( getAttributePixel(x, y) );
            }
            if ( transparenteBright ) {
                bright = getBright( getAttributePixel(x, y) );
            }
            attrs = createAttribute( flash, bright, paper, ink );

            setAttributePixel( x, y, attrs, ignorarSeleccion );
        }
    }

    static Color getColor(boolean tinta, byte attr) {

        int indColor0 = 0;
        if ( tinta ) {
            indColor0 = Pantalla.getInk(attr);
        }
        else {
            indColor0 = Pantalla.getPaper(attr);
        }
        int indColor1 = Pantalla.getBright(attr)? 1 : 0;

        return Pantalla.colores[indColor0][indColor1];
    }
    
    static int getColorRGB(boolean tinta, boolean bright, int paper, int ink) {

        int indColor0 = 0;
        if ( tinta ) {
            indColor0 = ink;
        }
        else {
            indColor0 = paper;
        }
        int indColor1 = bright? 1 : 0;

        return Pantalla.coloresRGB[indColor0][indColor1];
    }

    public void pintaEnImagen(BufferedImage imagen, boolean pintarSeleccion, boolean invertirTintaYPapel ) {


    	int p = 0;
        for (int j = 0; j < Pantalla.BITMAP_Y; j++) {
            
            for (int i = 0; i < Pantalla.BITMAP_X; i++) {

                byte attr = getAttributePixel(i,j);
    
                boolean tinta = getBitmap(i, j);
                
                Color color = getColor( invertirTintaYPapel & getFlash( attr )? !tinta : tinta, attr);
                
                int v = color.getRGB();

                // Si el pixel esta seleccionado se le pone alfa translucida
                if ( pintarSeleccion && seleccion.haySeleccion && seleccion.bitmap[ p ] ) {
                	v &= 0x00FFFFFF;
                	v |= 0x50000000;
                }

                imagen.setRGB( i, j, v );

                p++;
            }
        }
    }
    
    public byte getAttributePixel(int i, int j) {
        // implement in subclasses
        return 0;
    }
    
    public void setAttributePixel(int x, int y, byte attrs, boolean ignorarSeleccion) {
        // implement in subclasses
    }

    public void desplazarIzquierda() {
        for (int j = 0; j < BITMAP_Y; j++) {
            for (int i = 0; i < BITMAP_X - 1; i++) {
            	if ( seleccion.haySeleccionXY( i+1, j ) ) {
            		setBitmap(i, j, getBitmap(i+1, j), true);
            	}
            }
        }

        if ( seleccion.haySeleccion ) {
	        for (int j = 0; j < BITMAP_Y; j++) {
	            for (int i = 0; i < BITMAP_X - 1; i++) {
	           		seleccion.seleccionar( i, j, seleccion.haySeleccionXY( i + 1, j ) );
	            }
	        }
        }
        seleccion.comprobarHaySeleccion();
    }

    public void desplazarDerecha() {
        for (int j = 0; j < BITMAP_Y; j++) {
            for (int i = BITMAP_X-1; i > 0; i--) {
            	if ( seleccion.haySeleccionXY( i, j ) ) {
                	if ( seleccion.haySeleccionXY( i-1, j ) ) {
                		setBitmap(i, j, getBitmap(i-1, j), true);
                	}
            	}
            }
        }

        if ( seleccion.haySeleccion ) {
	        for (int j = 0; j < BITMAP_Y; j++) {
	            for (int i = BITMAP_X-1; i > 0; i--) {
	           		seleccion.seleccionar( i, j, seleccion.haySeleccionXY( i - 1, j ) );
	            }
	        }
        }
        seleccion.comprobarHaySeleccion();

    }

    public void desplazarArriba() {
        for (int i = 0; i < BITMAP_X; i++) {
            for (int j = 0; j < BITMAP_Y - 1; j++) {
            	if ( seleccion.haySeleccionXY( i, j+1 ) ) {
            		setBitmap(i, j, getBitmap(i, j+1), true);
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

    public void desplazarAbajo() {
        for (int i = 0; i < BITMAP_X; i++) {
            for (int j = BITMAP_Y-1; j > 0; j--) {
            	if ( seleccion.haySeleccionXY( i, j-1 ) ) {
            		setBitmap(i, j, getBitmap(i, j-1), true);
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

    public void desplazarArribaColor() {
        // Implement in subclasses
    }

    public void desplazarAbajoColor() {
    	// Implement in subclasses
    }

    protected void grabarBitmap(FileOutputStream os) {
    	
    	byte bitmapBytes[] = obtenerBitmapEnMemoria();
    	
        try {
            os.write( bitmapBytes );
        }
        catch (IOException e) {}   	
    }
    

    protected byte[] obtenerBitmapEnMemoria() {

        int tercioEntrelazado = 0;
        int bloqueEntrelazado = 0;
        int filaEntrelazado = 0;
        int jEntrelazado = 0;
        byte [] bitmapBytes = new byte[6144];
        for (int j = 0; j < BITMAP_Y; j++) {
            for (int i = 0; i < BITMAP_X; i+=8) {

                byte b = (byte)( (getBitmap(i, jEntrelazado)? 0x80 : 0) |
                         (getBitmap(i+1, jEntrelazado)? 0x40 : 0) |
                         (getBitmap(i+2, jEntrelazado)? 0x20 : 0) |
                         (getBitmap(i+3, jEntrelazado)? 0x10 : 0) |
                         (getBitmap(i+4, jEntrelazado)? 0x08 : 0) |
                         (getBitmap(i+5, jEntrelazado)? 0x04 : 0) |
                         (getBitmap(i+6, jEntrelazado)? 0x02 : 0) |
                         (getBitmap(i+7, jEntrelazado)? 0x01 : 0) );
                bitmapBytes[ j * 32 + i / 8 ] = b;
            }
            
            bloqueEntrelazado++;
            if (bloqueEntrelazado == BITMAP_Y / 8 / 3) {
                bloqueEntrelazado = 0;
                filaEntrelazado++;
                if ( filaEntrelazado == 8 ) {
                    filaEntrelazado = 0;
                    tercioEntrelazado++;
                }
            }
            jEntrelazado = tercioEntrelazado * (BITMAP_Y / 3) + bloqueEntrelazado * 8 + filaEntrelazado;

        }
        
        return bitmapBytes;
    }

    protected boolean cargarBitmapDeMemoria( byte [] bitmapBytes) {

    	if ( bitmapBytes.length < BITMAP_SIZE / 8 ) {
    		return false;
    	}
    	
        int tercioEntrelazado = 0;
        int bloqueEntrelazado = 0;
        int filaEntrelazado = 0;
        int jEntrelazado = 0;

        for ( int j = 0; j < BITMAP_Y; j++ ) {
            int bit = 0x80;
            for ( int i = 0; i < BITMAP_X; i++ ) {
                byte b = bitmapBytes[j * 32 + i / 8 ];
                setBitmap(i, jEntrelazado, (b & bit) != 0, false);
                
                bit = bit >> 1;
                if ( bit == 0 ) {
                    bit = 0x80;
                }
            }
            
            bloqueEntrelazado++;
            if (bloqueEntrelazado == BITMAP_Y / 8 / 3) {
                bloqueEntrelazado = 0;
                filaEntrelazado++;
                if ( filaEntrelazado == 8 ) {
                    filaEntrelazado = 0;
                    tercioEntrelazado++;
                }
            }
            jEntrelazado = tercioEntrelazado * (BITMAP_Y / 3) + bloqueEntrelazado * 8 + filaEntrelazado;

        }
        
        return true;
    }
    
    // Grabar en formato colorator: bitmap entrelazado + atributos izquierda + atributos derecha + atributos hi
    public boolean grabarEnFichero(File file) {
        // Implement in subclasses
    	return false;
    }

    // Cargar de formato colorator
    public static Pantalla cargarDeFichero(File file) {
        // Implement in subclasses        
        return null;
    }

    // Grabar en formato SCR
    public boolean grabarEnFicheroScr(File file) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            
            // bitmap
            grabarBitmap( os );
            
            //atributos
            int attrsXScr = BITMAP_X / 8;
            int attrsYScr = BITMAP_Y / 8;
            byte [] attrBytes = new byte[ attrsXScr * attrsYScr];
            
            for ( int j = 0; j < attrsYScr; j++ ) {
                for ( int i = 0; i < attrsXScr; i++ ) {

                    attrBytes[j * attrsXScr + i] = getAttributePixel(i * 8, j * 8);
                }
            }
            
            os.write( attrBytes );
            
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

    // Cargar de SCR
    public String cargarDeFicheroScr(File file) {
        FileInputStream is = null;

        try {
            is = new FileInputStream(file);
            
            // bitmap
            byte [] bitmapBytes = new byte[6144];
            try {
                is.read(bitmapBytes);
            }
            catch (IOException e) {
                return "I/O Error while loading image";
            }
            
            if ( ! cargarBitmapDeMemoria(bitmapBytes) ) {
            	return "Not enough bytes in file for bitmap"; 
            }
            
            int attrsXScr = BITMAP_X / 8;
            int attrsYScr = BITMAP_Y / 8;
            
            byte [] attrBytes = new byte[attrsXScr * attrsYScr];
            int bytesRead = is.read( attrBytes );
            if ( bytesRead != attrsXScr * attrsYScr ) {
                return "Not enough bytes in file for attributes";
            }
            
            for (int j = 0; j < attrsYScr; j++) {
                for (int i = 0; i < attrsXScr; i++) {
                    byte b = attrBytes[j * attrsXScr + i];
                    for (int r = 0; r < 8; r++) {
                        setAttributePixel(i * 8, j * 8 + r, b, false);
                    }
                }
            }

        }
        catch (IOException e) {
        	return "I/O Error while loading image";
        }
        finally {
            if ( is != null ) {
                try {
                    is.close();
                } catch ( Exception e ) {}
            }
        }
        return null;
    }

    // Cargar de memoria de SCR
    public boolean cargarDeMemoriaScr(byte [] scr) {
        // Implement in subclasses        
        return false;
    }

    // Obtener en memoria en SCR
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

        int attrsXScr = BITMAP_X / 8;
        int attrsYScr = BITMAP_Y / 8;
        
        for (int j = 0; j < attrsYScr; j++) {
            for (int i = 0; i < attrsXScr; i++) {
                datos[ bitmapTam + j * attrsXScr + i ] = getAttributePixel( i*8, j*8 );
            }
        }
        
        return datos;
    }

    // Grabar solo atributos alta
    public boolean grabarEnFicheroAtributosAlta(File file) {
        // Implement in subclasses
    	return false;
    }

    // Cargar solo atributos alta
    public void cargardeFicheroAtributosAlta(File file) {
        // Implement in subclasses
    }

    // Exportar a PNG
    public String exportarImagenEntera( File file ) {

    	BufferedImage img = new BufferedImage( BITMAP_X, BITMAP_Y, BufferedImage.TYPE_INT_RGB );

    	pintaEnImagen( img, false, false );
    	
        try {
            ImageIO.write( img, "PNG", file );
        }
        catch ( IOException e ) {
        	return "Error while writing to image";
        }
    	
    	return null;
    }
    
    public void rellena() {
        for (int i = 0; i < BITMAP_X;i++) {
            for (int j = 0; j < BITMAP_Y;j++) {
                //boolean v = (i&1)!=0&&(j&1)!=0;//!(((i&0)!=0) || ((j&i)!=0));
                //boolean v = ((i/8+j/8)&1) != 0;
                setBitmap(i, j, false, false);
            }
        }        
    }
    
    public void rellenaAtributos( byte attr ) {
        for (int i = 0; i < BITMAP_X;i++) {
            for (int j = 0; j < BITMAP_Y;j++) {
                setAttributePixel(i, j, attr, false);
            }
        }
    }
    
    public void borra() {
    	rellena();
        rellenaAtributos( createAttribute( false, false, 7, 0 ) );
    }
    
    public void invierteBitmap() {
    	for (int i = 0; i < BITMAP_X;i++) {
            for (int j = 0; j < BITMAP_Y;j++) {
                setBitmap( i, j, ! getBitmap( i, j ), false );
            }
        }    
    }

    public void normalizar() {
        // Implement in subclasses
    }
}
