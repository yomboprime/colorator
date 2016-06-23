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
 *  Hilo de refresco de flash y animacion trama seleccion
 */

import javax.swing.SwingUtilities;


public class HiloRefrescoFlash extends Thread {

	public Colorator colorator;

	public boolean terminar;
	
	@Override
	public void run() {

		while ( ! terminar ) {

			colorator.panelEditor.refrescarDesdeOtroHilo();

			try {
				// Intervalo de flash del Spectrum: aprox. 0.32 segundos ( = 320 ms )
				double ms = 1000.0d * 16 / 50.08d;
				Thread.sleep( (int)ms );
			}
			catch ( InterruptedException e ) {
				// Nada que hacer
			}
		}
		
	}
}