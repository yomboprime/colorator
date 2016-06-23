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
 * Herramienta de lapiz de seleccion
 */
import java.awt.event.*;

public class HerramientaSeleccionLapiz extends Herramienta
{

    int xAnt, yAnt;
    
    boolean seleccionar;

    public HerramientaSeleccionLapiz(Colorator colorator)
    {
        super(colorator);
    }
    
    // Un boton se ha pulsado en el panel editor
    public boolean empezar( int x, int y, int botones, int modificadores ) {
        if ( ! super.empezar( x, y, botones, modificadores ) ) {
        	return false;
        }

        seleccionar = true;
        if ( botones == MouseEvent.BUTTON3 ) {
        	seleccionar = false;
        }

        seleccionarLinea( colorator.pantalla.seleccion, x, y, x, y );

        colorator.pantalla.seleccion.comprobarHaySeleccion();

        xAnt = x;
        yAnt = y;

        return true;
    }
    
    // Sin cambiar estado de botones, se ha movido el raton
    public boolean movido( int x, int y ) {
    	if ( ! super.movido( x, y ) ) {
    		return false;
    	}

        seleccionarLinea( colorator.pantalla.seleccion, xAnt, yAnt, x, y );

        xAnt = x;
        yAnt = y;
        
        return true;
    }
    
    // Se ha soltado el boton
    public boolean terminar( int x, int y ) {
        if ( ! super.terminar(x, y) ) {
        	return false;
        }

        seleccionarLinea( colorator.pantalla.seleccion, x, y, x, y );

        colorator.pantalla.seleccion.comprobarHaySeleccion();

        return true;
    }
    
    public void seleccionarPixel( Seleccion s, int x, int y ) {
    	s.seleccionarConClamp( x, y, seleccionar );
    }
    
    public void seleccionarLinea( Seleccion s, int x0, int y0, int x1, int y1 ) {
        
        seleccionarPixel( s, x0, y0 );

        if ( x0 == x1 && y0 == y1 ){
            return;
        }
        
        int dx = x1 - x0;
        int dy = y1 - y0;
        
        int incX = 1;
        if ( x1 < x0 ) {
            incX = -1;
        }
        int incY = 1;
        if ( y1 < y0 ) {
            incY = -1;
        }        

        if ( dy == 0 ) {
            int x = x0;
            do { 
            	seleccionarPixel( s, x, y0 );
                x+= incX;
            } while ( x != x1 );
            
            seleccionarPixel( s, x, y0 );
            
            return;
        }

        
        if ( dx == 0 ) {
            int y = y0;
            do {
            	seleccionarPixel( s, x0, y );
                y+= incY;
            } while ( y!=y1 );
            
            seleccionarPixel( s, x0, y );
            
            return;
        }

        if ( dx < 0 ) {
            dx = -dx;
        }
        if ( dy < 0 ) {
            dy = -dy;
        }        

        int x = x0;
        int y = y0;
        if ( dx > dy ) {
            int acum = dx / 2;
            do {
                x += incX;
                acum += dy;
                if ( acum >= dx ) {
                    y += incY;
                    acum -= dx;
                }
                seleccionarPixel( s, x, y );
            } while (x != x1);
            
        }
        else {
            int acum = dy / 2;
            do {
                y += incY;
                acum += dx;
                if ( acum >= dy ) {
                    x += incX;
                    acum -= dy;
                }
                seleccionarPixel( s, x, y );
            } while (y != y1);
            
        }

    }
}
