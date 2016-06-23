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
 * Herramienta de copiar 
 */
import java.awt.event.*;

public class HerramientaCopiar extends Herramienta
{

    int x0, y0;
    boolean porBloques;

    public HerramientaCopiar(Colorator colorator)
    {
        super(colorator);
    }

    // Un boton se ha pulsado en el panel editor
    @Override
    public boolean empezar( int x, int y, int botones, int modificadores ) {
        if ( ! super.empezar( x, y, botones, modificadores ) ) {
        	return false;
        }
        
        if ( ! colorator.pantalla.seleccion.haySeleccion ) {
        	return false;
        }

        porBloques = botones == MouseEvent.BUTTON3;

    	if ( porBloques ) {
    		x = x - x % 8;
    		y = y - y % 8;
    	}

        x0 = x;
        y0 = y;
        
        return true;
    }

    public boolean movido( int x, int y ) {
    	if ( ! super.movido( x, y ) ) {
    		return false;
    	}

    	colorator.pantalla.copiarDe( colorator.undo.get( colorator.indexUndo ) );

    	copiar( x, y );
        
        return true;
    }
    
    // Se ha soltado el boton
    public boolean terminar( int x, int y ) {
        if ( ! super.terminar(x, y) ) {
        	return false;
        }

        copiar(x, y);
        
        return true;
    }
    
    private void copiar( int x, int y ) {

    	if ( porBloques ) {
    		x = x - x % 8;
    		y = y - y % 8;
    	}

    	int desplX = x - x0;
    	int desplY = y - y0;
    	
    	Pantalla pantalla = colorator.pantalla;
    	Seleccion seleccion = colorator.pantalla.seleccion;    	
    	for ( int j = 0; j < Pantalla.BITMAP_Y; j ++ ) {
    		for ( int i = 0; i < Pantalla.BITMAP_X; i ++ ) {
    			if ( seleccion.haySeleccionXY( i, j ) ) {
                	pantalla.pintarPixel( i + desplX, j + desplY,
                							pantalla.getBitmap( i, j ),
                							pantalla.getAttributePixel( i, j ),
                							colorator.modoTransparentePaper,
                							colorator.modoTransparenteInk,
                							colorator.modoTransparenteBright,
                							colorator.modoTransparenteFlash,
                							false,
                							true );

    			}
    		}
    	}
    }
}
