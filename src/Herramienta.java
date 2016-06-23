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
 * Clase base de las herramientas de colorator que afectan a la pantalla
 */
import java.awt.Rectangle;
import java.awt.event.*;

public class Herramienta
{
    Colorator colorator;
    int botones;
    int modificadores;
    
    int x0Padding, y0Padding;
    
    public boolean enUso;

    public Herramienta(Colorator colorator)
    {
        this.colorator = colorator;
        enUso = false;
    }

    // Un boton se ha pulsado en el panel editor
    public boolean empezar( int x, int y, int botones, int modificadores ) {
    	boolean editado = true;

    	if ( ( modificadores & MouseEvent.CTRL_DOWN_MASK ) != 0 ) {
    		// Con control pulsado se coge color en vez de usar la herramienta
    		editado = false;
    		cogerPixel( x, y );
    	}
    	else if ( botones == MouseEvent.BUTTON2 ) {
    		// Con btn2 (el boton de en medio del raton) se mueve la vista
    		editado = false;
    		x0Padding = x;
    		y0Padding = y;
    	}
    	else {
    		colorator.pantallaEditada = true;
    	}
        enUso = true;
    	this.botones = botones;
        this.modificadores = modificadores;

        return editado;
    }
    
    // Sin cambiar estado de botones, se ha movido el raton
    public boolean movido( int x, int y ) {
    	
    	boolean editado = true;
        if ( ( modificadores & MouseEvent.CTRL_DOWN_MASK ) != 0 ) {

    		editado = false;
    		cogerPixel( x, y );
    	}
        else if ( botones == MouseEvent.BUTTON2 ) {
    		editado = false;
    		
    		// Mueve la vista
    		int z = colorator.panelEditor.zoom;
    		int dx = z * ( x0Padding - x );
    		int dy = z * ( y0Padding - y );

    		colorator.panelEditor.mueveVista( dx, dy );

    	}
    	return editado;
    }
    
    // Se ha soltado el boton
    public boolean terminar( int x, int y ) {

    	enUso = false;

        return ! ( botones == MouseEvent.BUTTON2 || ( ( modificadores & MouseEvent.CTRL_DOWN_MASK ) != 0 ) );
    }
    
    public void cogerPixel(int x, int y) {
        
        byte attr = colorator.pantalla.getAttributePixel(x, y);
        
        colorator.setAttrActual( attr );
    }
}
