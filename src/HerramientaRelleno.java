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
 * Herramienta de relleno 
 */
import java.awt.event.*;

public class HerramientaRelleno extends Herramienta
{

    int [][] automata;

    public HerramientaRelleno(Colorator colorator)
    {
        super(colorator);
        
        automata = new int[Pantalla.BITMAP_X][Pantalla.BITMAP_Y];
    }
    
    // Un boton se ha pulsado en el panel editor
    public boolean empezar( int x, int y, int botones, int modificadores ) {
        if ( ! super.empezar( x, y, botones, modificadores ) ) {
        	return false;
        }

        if ( ! colorator.pantalla.seleccion.haySeleccionXY( x, y ) ) {
    		return false;
    	}

        rellenar(x, y);
        
    	return true;
    }
    
    public void rellenar( int x, int y ) {

        boolean tinta = true;
        if ( botones == MouseEvent.BUTTON3 ) {
            tinta = false;
        }

        for (int j = 0; j < Pantalla.BITMAP_Y; j++) {
            for (int i = 0; i < Pantalla.BITMAP_X; i++) {
                automata[i][j] = 0;
            }
        }
        
        automata[x][y] = 1;
        
        Pantalla p = colorator.pantalla;
        Seleccion s = p.seleccion;
        boolean acabar = false;
        while ( !acabar ) {
            acabar = true;
            for (int j = 0; j < Pantalla.BITMAP_Y; j++) {
                for (int i = 0; i < Pantalla.BITMAP_X; i++) {
                    if ( automata[i][j] == 1 ) {
                    
                        p.pintarPixel( i, j, tinta, colorator.attrsActual,
                        		colorator.modoTransparentePaper, colorator.modoTransparenteInk, colorator.modoTransparenteBright, colorator.modoTransparenteFlash,
                        		false, false );
                        
                        automata[i][j] = 2;
                        
                        if ( j > 0 && tinta != p.getBitmap(i, j-1) && automata[i][j-1] == 0 && s.haySeleccionXY(i, j-1) ) {
                            automata[i][j-1] = 1;
                            acabar = false;
                        }
                        if ( j < Pantalla.BITMAP_Y-1 &&
                            tinta != p.getBitmap(i, j+1) && automata[i][j+1] == 0 && s.haySeleccionXY(i, j+1) ) {
                            automata[i][j+1] = 1;
                            acabar = false;
                        }
                        if ( i > 0 && tinta != p.getBitmap(i-1, j) && automata[i-1][j] == 0 && s.haySeleccionXY(i-1, j)) {
                            automata[i-1][j] = 1;
                            acabar = false;
                        }
                        if ( i < Pantalla.BITMAP_X-1 &&
                            tinta != p.getBitmap(i+1, j) && automata[i+1][j] == 0 && s.haySeleccionXY(i+1, j)) {
                            automata[i+1][j] = 1;
                            acabar = false;
                        }
                    }
                }
            }
        }
    }
}
