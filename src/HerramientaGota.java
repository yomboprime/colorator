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
 * Herramienta de coger atributos (obsoleta) 
 */
import java.awt.event.*;

public class HerramientaGota extends Herramienta
{

    public HerramientaGota(Colorator colorator)
    {
        super(colorator);
    }
    
    // Un boton se ha pulsado en el panel editor
    public boolean empezar( int x, int y, int botones, int modificadores ) {
        super.empezar( x, y, botones, modificadores );
        cogerPixel( x, y );
        return true;
    }
    
    // Sin cambiar estado de botones, se ha movido el raton
    public boolean movido(int x, int y)
    {
    	if ( super.movido( x, y ) ) {
    		cogerPixel(x, y);
    		return true;
    	}
        
        return false;
    }
    
    // Se ha soltado el boton
    public boolean terminar(int x, int y)
    {
        if ( super.terminar(x, y) ) {
        	cogerPixel(x, y);
        	return true;
        }
        
        return false;
    }
    
    public void cogerPixel(int x, int y) {
        
        if ( botones != MouseEvent.BUTTON1 ) {
            return;
        }
        
        byte attr = colorator.pantalla.getAttributePixel(x, y);
        colorator.setAttrActual( attr );
    }
}
