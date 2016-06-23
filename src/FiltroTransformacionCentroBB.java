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
 * Filtro con transformacion relativa al centro de la bounding box (BB) de la seleccion
 */
import java.awt.Point;
import java.awt.event.*;

public class FiltroTransformacionCentroBB extends FiltroTransformacion {

	// Las sublases de esta transformacion usan bb0, bb1 y bbCentro para sus transformaciones
	
	Point bb0, bb1;
	double bbCentroX;
	double bbCentroY;

    public FiltroTransformacionCentroBB( Colorator colorator ) {
        super( colorator );

        bb0 = new Point();
        bb1 = new Point();
    }

    @Override
	public void filtrar() {
    	
    	// Llamar a este metodo desde subclases

    	calcularBBSeleccion();
    	
    	super.filtrar();
	}

	public void calcularBBSeleccion() {

    	// Calcula en bb0, bb1 y bbCentro la BB de la seleccion

    	Pantalla pantallaTrabajo = getPantallaTrabajo();
    	if ( ! pantallaTrabajo.seleccion.haySeleccion ) {
    		bb0.x = 0;
        	bb0.y = 0;
    		bb1.x = Pantalla.BITMAP_X-1;
        	bb1.y = Pantalla.BITMAP_Y-1;
    	}
    	else {

    		bb0.x = Pantalla.BITMAP_X;
	    	bb0.y = Pantalla.BITMAP_Y;
	    	bb1.x = 0;
	    	bb1.y = 0;
	    	
	    	boolean [] sel = pantallaTrabajo.seleccion.bitmap;
	    	int p = 0;
	    	for ( int j = 0; j < Pantalla.BITMAP_Y; j++ ) {
	    		for ( int i = 0; i < Pantalla.BITMAP_X; i++ ) {
	    			
	    			if ( sel[ p ] ) {
	    				if ( i < bb0.x ) {
	    					bb0.x = i;
	    				}
	    				else if ( i > bb1.x ) {
	    					bb1.x = i;
	    				}
	    				if ( j < bb0.y ) {
	    					bb0.y = j;
	    				}
	    				else if ( j > bb1.y ) {
	    					bb1.y = j;
	    				}
	    			}
	    			
	    			p++;
	    		}
	    	}
    	}

    	bbCentroX = ( bb0.x + bb1.x ) * 0.5d;
    	bbCentroY = ( bb0.y + bb1.y ) * 0.5d;
    }
}
