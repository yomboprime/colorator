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
 * Herramienta de seleccion de bloques
 */
import java.awt.event.MouseEvent;


public class HerramientaSeleccionBloques extends Herramienta
{

	int x0, y0;

    public HerramientaSeleccionBloques(Colorator colorator)
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

    	seleccionarBloque( x, y, botones == MouseEvent.BUTTON1 );

        return true;
    }
    
    // Sin cambiar estado de botones, se ha movido el raton
    public boolean movido( int x, int y ) {
    	if ( ! super.movido( x, y ) ) {
    		return false;
    	}

		//colorator.pantalla.copiarDe( colorator.undo.elementAt( colorator.indexUndo ) );
    	colorator.pantalla.seleccion.copiarDe( colorator.undo.get( colorator.indexUndo ).seleccion  );

    	seleccionarBloque( x, y, botones == MouseEvent.BUTTON1 );

        return true;
    }
    
    // Se ha soltado el boton
    public boolean terminar( int x, int y ) {
        if ( ! super.terminar(x, y) ) {
        	return false;
        }

        seleccionarBloque( x, y, botones == MouseEvent.BUTTON1 );
        
        return true;
    }
    
    private void seleccionarBloque( int x, int y, boolean seleccionar ) {

    	int bx0 = x0 / 8;
    	bx0 *= 8;
    	int by0 = y0 / 8;
    	by0 *= 8;

    	int bx1 = x / 8;
    	bx1 *= 8;
    	int by1 = y / 8;
    	by1 *= 8;

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
		
		bx1 += 7;
		by1 += 7;

    	for ( int j = by0; j <= by1; j++ ) {
    		for ( int i = bx0; i <= bx1; i++ ) {
    			colorator.pantalla.seleccion.seleccionar( i, j, seleccionar );
    		}
    	}
    	
    	colorator.pantalla.seleccion.comprobarHaySeleccion();

    }
}
