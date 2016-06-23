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
 * Clase base de filtros aplicados a la pantalla
 */
import java.awt.event.*;

public class Filtro {

	Colorator colorator;
	
    public Filtro(Colorator colorator)
    {
        this.colorator = colorator;
    }

    public void filtrar() {
        // Implementar en subclases
    }
    
    public Pantalla getPantallaTrabajo() {
    	
    	// Devuelve la pantalla de trabajo para generar el filtro
    	
    	return colorator.pantalla;
    }
    public Pantalla getPantallaAnterior() {
    	
    	// Devuelve la pantalla anterior de la que se parte para aplicar el filtro
    	
    	return colorator.undo.get( colorator.indexUndo );
    }
}
