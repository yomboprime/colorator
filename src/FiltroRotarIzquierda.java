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
 * Filtro de rotar a la izquierda
 */
import java.awt.Point;
import java.awt.event.*;

public class FiltroRotarIzquierda extends FiltroTransformacionCentroBB {

    public FiltroRotarIzquierda( Colorator colorator ) {
        super( colorator );
    }

    public Point transformarPixel( double x, double y ) {

    	double dx = x - bbCentroX;
    	double dy = y - bbCentroY;

    	puntoResultado.x = (int)( bbCentroX - dy /*+ 0.5d*/);
    	puntoResultado.y = (int)( bbCentroY + dx /*+ 0.5d*/);

    	return puntoResultado;
    }
}
