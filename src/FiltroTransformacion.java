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
 * Filtro de transformacion
 */
import java.awt.Point;
import java.awt.event.*;

public class FiltroTransformacion extends Filtro {

	protected Point puntoResultado;

    public FiltroTransformacion( Colorator colorator ) {
        super( colorator );
        
        puntoResultado = new Point();
        
    }

    @Override
    public void filtrar() {

    	Pantalla pantallaTrabajo = getPantallaTrabajo();
    	Pantalla pantallaAnterior = getPantallaAnterior();

    	boolean borrarSeleccionAlFinal = false;
    	if ( ! pantallaTrabajo.seleccion.haySeleccion ) {
    		borrarSeleccionAlFinal = true;
    	}
    	
    	pantallaTrabajo.seleccion.borrarTodaSeleccion();

    	
    	// Se escanean todos los puntos. Cada punto se transforma y si el transformado esta seleccionado se copia ese,
    	// si no se copia el original. La seleccion queda transformada.
    	int p = 0;
    	for ( int j = 0; j < Pantalla.BITMAP_Y; j++ ) {
    		for ( int i = 0; i < Pantalla.BITMAP_X; i++ ) {
    			
    			Point puntoTransformado = transformarPixel( i, j );

    			if ( pantallaAnterior.seleccion.haySeleccionXY( puntoTransformado.x, puntoTransformado.y ) ) {
    		        pantallaTrabajo.pintarPixel( i, j,
    		        				pantallaAnterior.getBitmap( puntoTransformado.x, puntoTransformado.y ),
    		        				pantallaAnterior.getAttributePixel( puntoTransformado.x, puntoTransformado.y ),
    		        				colorator.modoTransparentePaper, colorator.modoTransparenteInk, colorator.modoTransparenteBright, colorator.modoTransparenteFlash,
    		        				false, true );
    		        pantallaTrabajo.seleccion.seleccionarSinCheck( i, j, true );
    			}
    			else {
    				pantallaTrabajo.pintarPixel( i, j,
	        				pantallaAnterior.bitmap[ p ],
	        				pantallaAnterior.getAttributePixel( i, j ),
	        				colorator.modoTransparentePaper, colorator.modoTransparenteInk, colorator.modoTransparenteBright, colorator.modoTransparenteFlash,
	        				false, true );
    			}

    			p++;
    		}
    	}

    	if ( borrarSeleccionAlFinal ) {
    		pantallaTrabajo.seleccion.borrarTodaSeleccion();
    	}
    	else {
    		pantallaTrabajo.seleccion.comprobarHaySeleccion();
    	}
    	
    }
    
    public Point transformarPixel( double x, double y ) {
    	// Implementar en subclases. La entrada es double pero la salida es entera.
    	// Nada que hacer
    	return null;
    }
}
