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
 * Herramienta de seleccion de rectangulos
 */

import java.awt.event.MouseEvent;


public class HerramientaSeleccionRectangulo extends Herramienta
{

	int x0, y0;

    public HerramientaSeleccionRectangulo(Colorator colorator)
    {
        super(colorator);
    }

    // Un boton se ha pulsado en el panel editor
    public boolean empezar( int x, int y, int botones, int modificadores ) {
        if ( ! super.empezar( x, y, botones, modificadores ) ) {
        	return false;
        }

        x0 = x;
        y0 = y;

    	seleccionarRectangulo( x, y, botones == MouseEvent.BUTTON1 );

        return true;
    }
    
    // Sin cambiar estado de botones, se ha movido el raton
    public boolean movido( int x, int y ) {
    	if ( ! super.movido( x, y ) ) {
    		return false;
    	}

		//colorator.pantalla.copiarDe( colorator.undo.elementAt( colorator.indexUndo ) );
    	colorator.pantalla.seleccion.copiarDe( colorator.undo.get( colorator.indexUndo ).seleccion  );

    	seleccionarRectangulo( x, y, botones == MouseEvent.BUTTON1 );

        return true;
    }

    // Se ha soltado el boton
    public boolean terminar( int x, int y ) {
        if ( ! super.terminar(x, y) ) {
        	return false;
        }

        seleccionarRectangulo( x, y, botones == MouseEvent.BUTTON1 );
        
        return true;
    }
    
    private void seleccionarRectangulo( int x, int y, boolean seleccionar ) {

    	int bx0 = x0;
    	int by0 = y0;

    	int bx1 = x;
    	int by1 = y;

		if ( bx0 > bx1 ) {
			int temp = bx0;
			bx0 = bx1;
			bx1 = temp;
		}
		
		if ( by0 > by1 ) {
			int temp = by0;
			by0 = by1;
			by1 = temp;
		}
		
    	for ( int j = by0; j <= by1; j++ ) {
    		for ( int i = bx0; i <= bx1; i++ ) {
    			colorator.pantalla.seleccion.seleccionar( i, j, seleccionar );
    		}
    	}
    	
    	colorator.pantalla.seleccion.comprobarHaySeleccion();

    }
}
