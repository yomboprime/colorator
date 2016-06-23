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
 *  Herramienta de lapiz
 */
import java.awt.event.*;

public class Lapiz extends Herramienta
{

    int xAnt, yAnt;

    public Lapiz(Colorator colorator)
    {
        super(colorator);
    }
    
    // Un boton se ha pulsado en el panel editor
    @Override
    public boolean empezar( int x, int y, int botones, int modificadores ) {
        if ( ! super.empezar( x, y, botones, modificadores ) ) {
        	return false;
        }

        pintarPixel(x, y);

        xAnt = x;
        yAnt = y;
        
        return true;
    }
    
    // Sin cambiar estado de botones, se ha movido el raton
    @Override
    public boolean movido( int x, int y ) {
    	if ( ! super.movido( x, y ) ) {
    		return false;
    	}

    	colorator.linea.botones = this.botones;
        colorator.linea.pintarLinea( colorator.pantalla, xAnt, yAnt, x, y );
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

        pintarPixel(x, y);
        
        return true;
    }
    
    public void pintarPixel(int x, int y) {

        boolean tinta = true;
        if ( botones == MouseEvent.BUTTON3 ) {
            tinta = false;
        }
        
        colorator.pantalla.pintarPixel( x, y,
        		tinta, colorator.attrsActual,
        		colorator.modoTransparentePaper, colorator.modoTransparenteInk, colorator.modoTransparenteBright, colorator.modoTransparenteFlash,
        		false, false );
    }
}
