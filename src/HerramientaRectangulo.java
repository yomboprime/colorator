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
 * Herramienta de rectangulos 
 */
import java.awt.event.*;

public class HerramientaRectangulo extends Herramienta
{

    int x0, y0, x1, y1;
    boolean tinta;

    public HerramientaRectangulo(Colorator colorator)
    {
        super(colorator);
    }
    
    // Un boton se ha pulsado en el panel editor
    public boolean empezar( int x, int y, int botones, int modificadores ) {
        if ( ! super.empezar( x, y, botones, modificadores) ) {
        	return false;
        }
        
        tinta = true;
        if ( botones == MouseEvent.BUTTON3 ) {
            tinta = false;
        }

        x0 = x;
        y0 = y;
        x1 = x;
        y1 = y;
        
        pintarCuadrado();
        
        return true;
    }
    
    // Sin cambiar estado de botones, se ha movido el raton
    public boolean movido( int x, int y ) {
    	if ( ! super.movido( x, y ) ) {
    		return false;
    	}

        x1 = x;
        y1 = y;

        colorator.pantalla.copiarDe( colorator.undo.get( colorator.indexUndo ) );
        
        pintarCuadrado();
        
        return true;
    }
    
    // Se ha soltado el boton
    public boolean terminar( int x, int y ) {
        if ( ! super.terminar( x, y ) ) {
        	return false;
        }
        
        x1 = x;
        y1 = y;
        
        pintarCuadrado();
        
        return true;
    }
    
    public void pintarCuadrado() {
        
        int i0 = x0;
        int j0 = y0;
        int i1 = x1;
        int j1 = y1;
        if ( i0 > i1 ) {
            int i = i0;
            i0 = i1;
            i1 = i;
        }
        if ( j0 > j1 ) {
            int j = j0;
            j0 = j1;
            j1 = j;
        }
        
        for (int j = j0; j <= j1; j++) {
            for (int i = i0; i <= i1; i++) {
                colorator.pantalla.pintarPixel( i, j,
                		tinta, colorator.attrsActual,
                		colorator.modoTransparentePaper, colorator.modoTransparenteInk, colorator.modoTransparenteBright, colorator.modoTransparenteFlash,
                		false, false );
            }
        }
    }
}
