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
 * Herramienta de seleccion libre
 */
import java.awt.event.*;

public class HerramientaSeleccionLapizFree extends HerramientaSeleccionLapiz
{

	int x0, y0;
	
    int xAnt, yAnt;
    
    boolean accionSeleccionarNoDeseleccionar;
    
    int [][] automata;

    // Seleccion de pantalla usada para el algoritmo de rellenado de figura concava dibujada a lapiz
    Seleccion seleccionTemp;

    public HerramientaSeleccionLapizFree(Colorator colorator)
    {
        super(colorator);
        
        seleccionTemp = new Seleccion();
        
        automata = new int[Pantalla.BITMAP_X][Pantalla.BITMAP_Y];
    }
    
    // Un boton se ha pulsado en el panel editor
    @Override
    public boolean empezar(int x, int y, int botones, int modificadores) {
        if ( ! super.empezar( x, y, botones, modificadores ) ) {
        	return false;
        }

        x0 = x;
        y0 = y;

        xAnt = x;
        yAnt = y;

        seleccionar = true;
        
        accionSeleccionarNoDeseleccionar = true;
        if ( botones == MouseEvent.BUTTON3 ) {
        	accionSeleccionarNoDeseleccionar = false;
        }

        seleccionTemp.borrarTodaSeleccion();

        seleccionarLinea( seleccionTemp, x, y, x, y );
        colorator.pantalla.seleccion.setMask( seleccionTemp, accionSeleccionarNoDeseleccionar );

        colorator.pantalla.seleccion.comprobarHaySeleccion();

        return true;
    }
    
    // Sin cambiar estado de botones, se ha movido el raton
    @Override
    public boolean movido( int x, int y ) {
    	if ( ! super.movido( x, y ) ) {
    		return false;
    	}

        //colorator.pantalla.copiarDe( colorator.undo.elementAt( colorator.indexUndo ) );
    	colorator.pantalla.seleccion.copiarDe( colorator.undo.get( colorator.indexUndo ).seleccion  );

    	seleccionarLinea( seleccionTemp, xAnt, yAnt, x, y );
        colorator.pantalla.seleccion.setMask( seleccionTemp, accionSeleccionarNoDeseleccionar );

        colorator.pantalla.seleccion.comprobarHaySeleccion();

        xAnt = x;
        yAnt = y;
        
        return true;
    }
    
    // Se ha soltado el boton
    @Override
    public boolean terminar( int x, int y ) {
        if ( ! super.terminar(x, y) ) {
        	return false;
        }

        // Selecciona una linea desde el ultimo punto al inicio
        seleccionarLinea( seleccionTemp, x, y, x0, y0 );

        // Rellena "el interior" de la forma dibujada
        rellenar();
        
        colorator.pantalla.seleccion.setMask( seleccionTemp, accionSeleccionarNoDeseleccionar );

        colorator.pantalla.seleccion.comprobarHaySeleccion();

        return true;
    }
    
    public void rellenar() {

    	// Vacia el automata
        for (int j = 0; j < Pantalla.BITMAP_Y; j++) {
            for (int i = 0; i < Pantalla.BITMAP_X; i++) {
                automata[i][j] = 0;
            }
        }

        // Pone semillas en el borde de la pantalla menos donde hay seleccion
        for ( int i = 0; i < Pantalla.BITMAP_X; i++ ) {
        	if ( ! seleccionTemp.bitmap[ i ] ) {
        		automata[ i ][ 0 ] = 1;
        	}
        	if ( ! seleccionTemp.bitmap[ i + (Pantalla.BITMAP_Y-1) * Pantalla.BITMAP_X ] ) {
        		automata[ i ][ Pantalla.BITMAP_Y-1 ] = 1;
        	}
        }
        for ( int j = 0; j < Pantalla.BITMAP_Y; j++ ) {
        	if ( ! seleccionTemp.bitmap[ j * Pantalla.BITMAP_X ] ) {
        		automata[ 0 ][ j ] = 1;
        	}
        	if ( ! seleccionTemp.bitmap[ (Pantalla.BITMAP_X-1) + j * Pantalla.BITMAP_X ] ) {
        		automata[ Pantalla.BITMAP_X-1 ][ j ] = 1;
        	}
        }

        // Propaga las semillas por toda la pantalla por donde no hay seleccion. El resultado es
        // que el automata vale 2 donde no hay que seleccionar (exterior), el interior quedaran
        // como celdas no visitadas.
        boolean acabar = false;
        while ( !acabar ) {
            acabar = true;
            int p = 0;
            for (int j = 0; j < Pantalla.BITMAP_Y; j++) {
                for (int i = 0; i < Pantalla.BITMAP_X; i++) {
                    if ( automata[i][j] == 1 ) {

                        automata[i][j] = 2;
                        
                        if ( j > 0 && automata[i][j-1] == 0 && !seleccionTemp.haySeleccionXYSinCheckNiHaySeleccion(i, j-1) ) {
                            automata[i][j-1] = 1;
                            acabar = false;
                        }
                        if ( j < Pantalla.BITMAP_Y-1 && automata[i][j+1] == 0 && !seleccionTemp.haySeleccionXYSinCheckNiHaySeleccion(i, j+1) ) {
                            automata[i][j+1] = 1;
                            acabar = false;
                        }
                        if ( i > 0 && automata[i-1][j] == 0 && !seleccionTemp.haySeleccionXYSinCheckNiHaySeleccion(i-1, j)) {
                            automata[i-1][j] = 1;
                            acabar = false;
                        }
                        if ( i < Pantalla.BITMAP_X-1 && automata[i+1][j] == 0 && !seleccionTemp.haySeleccionXYSinCheckNiHaySeleccion(i+1, j)) {
                            automata[i+1][j] = 1;
                            acabar = false;
                        }
                    }
                    p++;
                }
            }
        }
        
        // Selecciona las celdas no visitadas, que son las interiores o que ya estaban seleccionadas 
        int p = 0;
        for (int j = 0; j < Pantalla.BITMAP_Y; j++) {
            for (int i = 0; i < Pantalla.BITMAP_X; i++) {
            	if ( automata[ i ][ j ] == 0 ) {
            		seleccionTemp.bitmap[ p ] = true;
            	}
            	p++;
            }
        }
    }
    
    public void rellenarAntiguo() {
    	
    	
    	// TODO Este algoritmo no sirve, usar otro:
    	// Probar a buscar segmentos en la line mientras no se llegue al final
    	// Un segmento es una fila de vacios entre dos pixeles seleccionados. El del extremo izquierdo ha de tener
    	// un vacio a su izquierda (o el extremo de la pantalla)

    	boolean algunPixelNoRellenadoEnX = false;

    	// Pasada en el eje X
    	int p = 0;
    	for ( int j = 0; j < Pantalla.BITMAP_Y; j++ ) {

        	boolean rellenando = seleccionTemp.bitmap[ j * Pantalla.BITMAP_X ];
        	boolean previoRellenando = false; 

    		for ( int i = 0; i < Pantalla.BITMAP_X; i++ ) {

            	boolean v = seleccionTemp.bitmap[ p ];
            	if ( rellenando ) {
            		seleccionTemp.bitmap[ p ] = true;
            	}
            	if ( v ) {
            		if ( ! previoRellenando ) {
            			rellenando = ! rellenando;
            			previoRellenando = rellenando;
            		}
            		else {
            			// No podemos seguir rellenando esta linea ya que hay al menos dos pixeles seguidos
            			// Pasamos a la siguiente linea

            			// Actualiza p por el break a la siguiente linea
            			p = ( j + 1 ) * Pantalla.BITMAP_X;
            			
            			// Marca flag para siguiente pasada en Y
            			algunPixelNoRellenadoEnX = true;
            			
            			// Salta el bucle i hasta la siguiente linea
            			break;
            		}
            	}
            	else {
            		previoRellenando = false;
            	}

            	p++;
            }
    	}
    }

}
