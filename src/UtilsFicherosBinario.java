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
 * Utilidades para cargar y salvar ficheros binarios  
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UtilsFicherosBinario {

	public static byte [] leerFicheroBinario( String path ) {

		File f = new File( path );
		if ( ! f.isFile() ) {
			return null;
		}
		try {
			long tam  = f.length();
			if ( tam > Integer.MAX_VALUE ) {
				return null;
			}
			int tami = (int)tam;
			byte [] datos = new byte[ tami ];
			FileInputStream fis = new FileInputStream( f );
			fis.read( datos );
			fis.close();
			return datos;
		}
		catch ( FileNotFoundException e ) {
		}
		catch ( IOException e ) {
		}
		
		return null;
	}
	
	public static void grabarFicheroBinario( String path, byte[] datos ) {

		File f = new File( path );

		try {
			FileOutputStream fos = new FileOutputStream( f );
			fos.write( datos );
			fos.flush();
			fos.close();
		}
		catch ( FileNotFoundException e ) {
		}
		catch ( IOException e ) {
		}
	}
	
}
