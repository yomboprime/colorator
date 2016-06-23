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
 *  Herramienta de lapiz que pinta solo atributos
 */
import java.awt.event.*;

public class LapizAtributo extends Herramienta
{

    int xAnt, yAnt;

    public LapizAtributo(Colorator colorator)
    {
        super(colorator);
    }
    
    // Un boton se ha pulsado en el panel editor
    public boolean empezar( int x, int y, int botones, int modificadores ) {
        super.empezar( x, y, botones, modificadores );
        
        if ( botones == MouseEvent.BUTTON3 ) {
        	cambiarTintaPorPapel( x, y );
        }
        else {
        	pintarAtributo(x, y);
        	xAnt = x;
            yAnt = y;
        }
        
        return true;
    }
    
    // Sin cambiar estado de botones, se ha movido el raton
    public boolean movido( int x, int y ) {
    	if ( ! super.movido( x, y ) ) {
    		return false;
    	}

    	if ( botones == MouseEvent.BUTTON3 ) {
        	// Nada que hacer
        }
        else {
        	pintarAtributo(x, y);
        	xAnt = x;
            yAnt = y;
        }

        
        return true;
    }
    
    // Se ha soltado el boton
    public boolean terminar( int x, int y ) {
        if ( ! super.terminar(x, y) ) {
        	return false;
        }

    	if ( botones == MouseEvent.BUTTON3 ) {
    		// Nada que hacer
        }
        else {
        	pintarAtributo(x, y);
        	xAnt = x;
            yAnt = y;
        }
        
        return true;
    }
    
    public void pintarAtributo(int x, int y) {
        
        colorator.pantalla.pintarPixel( x, y, true, colorator.attrsActual,
        		colorator.modoTransparentePaper, colorator.modoTransparenteInk, colorator.modoTransparenteBright, colorator.modoTransparenteFlash,
        		true, false );
    }

    public void cambiarTintaPorPapel( int x, int y ) {

    	byte attr1 = colorator.pantalla.getAttributePixel( x, y );

    	byte attrInvertido = Pantalla.createAttribute( Pantalla.getFlash( attr1 ),
    												   Pantalla.getBright( attr1 ),
    												   Pantalla.getInk( attr1 ),
    												   Pantalla.getPaper( attr1 ) );

    	colorator.pantalla.pintarPixel( x, y, true, attrInvertido,
        		false, false, false, false,
        		true, false );

    }
}
