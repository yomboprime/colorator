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
 *  Herramienta de dibujar lineas
 */
import java.awt.event.*;

public class Linea extends Herramienta
{

    int x0, y0;

    public Linea(Colorator colorator)
    {
        super(colorator);
    }
    
    // Un boton se ha pulsado en el panel editor
    public boolean empezar( int x, int y, int botones, int modificadores ) {
        if ( ! super.empezar( x, y, botones, modificadores ) ) {
        	return false;
        }

        //pintarLinea(x, y, x, y);
        x0 = x;
        y0 = y;
        
        return true;
    }
    
    // Sin cambiar estado de botones, se ha movido el raton
    public boolean movido( int x, int y ) {
    	if ( ! super.movido( x, y ) ) {
    		return false;
    	}

        colorator.pantalla.copiarDe( colorator.undo.get( colorator.indexUndo ) );

        pintarLinea( colorator.pantalla, x0, y0, x, y );
        
        return true;
    }
    
    // Se ha soltado el boton
    public boolean terminar( int x, int y ) {
        if ( ! super.terminar(x, y) ) {
        	return false;
        }

        pintarLinea( colorator.pantalla, x0, y0, x, y );
        
        return true;

    }
    
    public void pintarLinea( Pantalla pant, int x0, int y0, int x1, int y1 ) {
        
        boolean tinta = true;
        if ( botones == MouseEvent.BUTTON3 ) {
            tinta = false;
        }

        pant.pintarPixel(x0, y0, tinta, colorator.attrsActual,
                                       colorator.modoTransparentePaper,
                                       colorator.modoTransparenteInk,
                                       colorator.modoTransparenteBright,
                                       colorator.modoTransparenteFlash,
                                       false, false);

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
            	pant.pintarPixel(x, y0, tinta, colorator.attrsActual,
                                       colorator.modoTransparentePaper,
                                       colorator.modoTransparenteInk,
                                       colorator.modoTransparenteBright,
                                       colorator.modoTransparenteFlash,
                                       false, false);
                x+= incX;
            } while ( x != x1 );
            
            pant.pintarPixel(x, y0, tinta, colorator.attrsActual,
                                       colorator.modoTransparentePaper,
                                       colorator.modoTransparenteInk,
                                       colorator.modoTransparenteBright,
                                       colorator.modoTransparenteFlash,
                                       false, false);
            
            return;
        }

        
        if ( dx == 0 ) {
            int y = y0;
            do {
            	pant.pintarPixel(x0, y, tinta, colorator.attrsActual,
                                       colorator.modoTransparentePaper,
                                       colorator.modoTransparenteInk,
                                       colorator.modoTransparenteBright,
                                       colorator.modoTransparenteFlash,
                                       false, false);
                y+= incY;
            } while ( y!=y1 );
            
            pant.pintarPixel(x0, y, tinta, colorator.attrsActual,
                                       colorator.modoTransparentePaper,
                                       colorator.modoTransparenteInk,
                                       colorator.modoTransparenteBright,
                                       colorator.modoTransparenteFlash,
                                       false, false);
            
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
                pant.pintarPixel(x, y, tinta, colorator.attrsActual,
                                            colorator.modoTransparentePaper,
                                            colorator.modoTransparenteInk,
                                            colorator.modoTransparenteBright,
                                            colorator.modoTransparenteFlash,
                                            false, false);
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
                pant.pintarPixel(x, y, tinta, colorator.attrsActual, 
                                        colorator.modoTransparentePaper,
                                        colorator.modoTransparenteInk,
                                        colorator.modoTransparenteBright,
                                        colorator.modoTransparenteFlash,
                                        false, false);
            } while (y != y1);
            
        }

    }
}
