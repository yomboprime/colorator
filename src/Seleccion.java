import java.awt.Rectangle;

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
 *  Seleccion en una pantalla
 */
public class Seleccion {

	public boolean haySeleccion;
	
	public boolean [] bitmap;
	
	public Seleccion() {
		bitmap = new boolean[ Pantalla.BITMAP_SIZE ];
	}

	public void seleccionar( int x, int y, boolean seleccionar ) {
		
		if ( x < 0 || x >= Pantalla.BITMAP_X || y < 0 || y >= Pantalla.BITMAP_Y ) {
			return;
		}

		bitmap[ x + y * Pantalla.BITMAP_X] = seleccionar;
	}

	public void seleccionarSinCheck( int x, int y, boolean seleccionar ) {
		bitmap[ x + y * Pantalla.BITMAP_X] = seleccionar;
	}

	public void seleccionarConClamp( int x, int y, boolean seleccionar ) {
		
		// Selecciona clampeando las coordenadas al interior de la pantalla
		
		if ( x < 0 ) {
			x = 0;
		}
		else if ( x >= Pantalla.BITMAP_X ) {
			x = Pantalla.BITMAP_X - 1;
		}
		if ( y < 0 ) {
			y = 0;
		}
		else if ( y >= Pantalla.BITMAP_Y )  {
			y = Pantalla.BITMAP_Y - 1;
		}

		bitmap[ x + y * Pantalla.BITMAP_X] = seleccionar;
	}

	public boolean haySeleccionPixel( int p ) {
		return ( ! haySeleccion ) || bitmap[ p ];
	}

	public boolean haySeleccionXY( int x, int y ) {
		if ( x < 0 || x >= Pantalla.BITMAP_X || y < 0 || y >= Pantalla.BITMAP_Y ) {
			return false;
		}
		return ( ! haySeleccion ) || bitmap[ x + y * Pantalla.BITMAP_X ];
	}

	public boolean haySeleccionXYSinCheckNiHaySeleccion( int x, int y ) {
		return bitmap[ x + y * Pantalla.BITMAP_X ];
	}

	public boolean comprobarHaySeleccion() {
		
		haySeleccion = false;
		for ( int p = 0; p < Pantalla.BITMAP_SIZE; p++ ) {
			if ( bitmap[ p ] ) {
				haySeleccion = true;
				break;
			}
		}
		
		return haySeleccion;
	}
	
	public void borrarTodaSeleccion() {
		
		for ( int p = 0; p < Pantalla.BITMAP_SIZE; p++ ) {
			bitmap[ p ] = false;
		}
		haySeleccion = false;
	}

	public void copiarDe( Seleccion s ) {

		haySeleccion = false;
		for ( int p = 0; p < Pantalla.BITMAP_SIZE; p++ ) {
			boolean v = s.bitmap[ p ];
			bitmap[ p ] = v;
			if ( v ) {
				haySeleccion = true;
			}
		}
	}

	public void setMask( Seleccion s, boolean setNoUnset ) {

		// Donde s esta seleccionado, se le pone a esta seleccion el valor setNoUnset
		
		for ( int p = 0; p < Pantalla.BITMAP_SIZE; p++ ) {
			if ( s.bitmap[ p ] ) {
				bitmap[ p ] = setNoUnset;
			}
		}
		haySeleccion = false;
	}

	public int contarNumPixelsSeleccionados() {
		int n = 0;
		for ( int i = 0; i < Pantalla.BITMAP_SIZE; i++ ) {
			if ( bitmap[ i ] ) {
				n++;
			}
		}
		return n;
	}

	public boolean getBBox( Rectangle result ) {

		if ( ! haySeleccion ) {
			return false;
		}

		int x0 = Integer.MAX_VALUE;
		int y0 = Integer.MAX_VALUE;
		int x1 = 0;
		int y1 = 0;
		
		
		int p = 0;
		for ( int j = 0; j < Pantalla.BITMAP_Y; j++ ) {
			for ( int i = 0; i < Pantalla.BITMAP_X; i++ ) {
				if ( bitmap[ p ] ) {
					if ( x0 > i ) {
						x0 = i;
					}
					if ( y0 > j ) {
						y0 = j;
					}
					if ( x1 < i ) {
						x1 = i;
					}
					if ( y1 < j ) {
						y1 = j;
					}
				}
				p++;
			}
		}
		
		result.x = x0;
		result.y = y0;
		result.width = x1 - x0 + 1;
		result.height = y1 - y0 + 1;
		
		return true;
	}
}
