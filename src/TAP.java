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
 *  Cargador de ficheros TAP (no funciona)
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TAP {

	public static final int BLOQUE_BASIC = 0;
	public static final int BLOQUE_CODE = 1;
	public static final int BLOQUE_SCR = 2;
	public static final int MAX_TIPOS_BLOQUE = 2;

	private static final int INICIO_CABECERA = 0;
	private static final int INICIO_NOMBRE = 1;
	private static final int INICIO_LONGITUD = 11;
	private static final int INICIO_PARAM1 = 13;
	private static final int INICIO_PARAM2 = 15;
	
	private class Bloque {
		String nombre;
		int tipo;
		byte datos[];
		int param1;
		int param2;

		public Bloque( int tipo, String nombre, byte datos[], int offset, int longitud, int param1, int param2 ) {

			setNombre( nombre );
			this.tipo = tipo;
			this.datos = new byte[ longitud ];
			if ( tipo == BLOQUE_SCR ) {
				param1 = 16384;
			}
			if ( tipo == BLOQUE_CODE || tipo == BLOQUE_SCR ) {
				// En este caso param2 siempre es 32768.
				// Si es BLOQUE_CODE el param1 es la direccion de memoria donde se carga el bloque.
				param2 = 32768;
			}
			
			if ( tipo == BLOQUE_BASIC ) {
				// En este caso param1  es el la linea de inicio, de 0 a 32767 o >= 32768 si no estaba especificada
				// Param2 es el inicio de las variables, que pongo al final del bloque del programa:
				param2 = longitud;
			}
			
			this.param1 = param1;
			this.param2 = param2;

			for ( int i = 0; i < longitud; i++ ) {
				this.datos[ i ] = datos[ i + offset ];
			}
			
		}

		public void setNombre( String nombre ) {
			if ( nombre.compareTo("") == 0 ) {
				nombre = "noname";
			}
			if ( nombre.length() <= 10 ) {
				this.nombre = nombre;
			}
			else {
				this.nombre = nombre.substring( 0, 10 );
			}
		}
	}

	private ArrayList<Bloque> bloques;
	
	private static byte cabecera[] = new byte[ 17 ];
	
	public TAP() {
		bloques = new ArrayList<Bloque>();
	}

	public int getNumBloques() {
		return bloques.size();
	}
	public String nuevoBloque( int tipo, String nombre, byte datos[], int offset, int longitud, int param1, int param2 ) {
		return nuevoBloque( bloques.size(), tipo, nombre, datos, offset, longitud, param1, param2  );
	}

	public String nuevoBloque( int posicion, int tipo, String nombre, byte datos[], int offset, int longitud, int param1, int param2  ) {

		// Tipo es de las constantes BLOQUE_xxx
		// Offset y longitud son en el array de bytes pasado por parametros

		if ( tipo < 0 || tipo > MAX_TIPOS_BLOQUE ) {
			return "Wrong block type: " + tipo;
		}
		
		if ( datos.length > 65535 ) {
			return "Block is too long ( > 65535 bytes )";
		}
		
		if ( posicion < 0 || posicion > bloques.size() ) {
			return "Wrong block position: " + posicion;
		}
		
		if ( offset < 0 || longitud <= 0 || offset + longitud > datos.length ) {
			return "Wrong offset/length";
		}
		
		Bloque b = new Bloque( tipo, nombre, datos, offset, longitud, param1, param2 );
		
		bloques.add( posicion, b );		

		return null;
	}

	public String grabarFicheroTAP( File fichero ) {
	
		if ( bloques.size() == 0 ) {
			return "No blocks to save.";
		}
		
        FileOutputStream os = null;
        try {
            os = new FileOutputStream( fichero );

            int numBloques = bloques.size();
            for ( int i = 0; i < numBloques; i++ ) {
            	Bloque b = bloques.get( i );

            	// Header block:

            	// Byte de tipo
            	switch ( b.tipo ) {
            	case BLOQUE_BASIC:
            		cabecera[ INICIO_CABECERA ] = 0;	// Tipo: Programa basic
            		break;

            	case BLOQUE_CODE:
            	case BLOQUE_SCR:
            		cabecera[ INICIO_CABECERA ] = 3;	// Tipo: code
            		break;
            	default:
            		return "Internal error: Wrong block type.";
            	}

            	// Nombre del bloque
        		byte nombre[] = b.nombre.getBytes( "UTF8" );
        		int nNombre = Math.min( 10, nombre.length );
        		int j = 0;
        		while ( j < nNombre ) {
        			cabecera[ INICIO_NOMBRE + j ] = nombre[ j ];
        			j++;
        		}
        		while ( j < 10 ) {
        			cabecera[ INICIO_NOMBRE + j ] = 32; // Rellena con espacios hasta 10 caracteres
        			j++;
        		}
        		
        		// Longitud del bloque
        		int longitud = b.datos.length;
        		byte longitudLow = (byte)( longitud & 0xFF );
        		byte longitudHigh = (byte)( ( longitud & 0xFF00 ) >> 8 );
        		cabecera[ INICIO_LONGITUD ] = longitudLow;
        		cabecera[ INICIO_LONGITUD + 1 ] = longitudHigh;
        		
        		// Parametros
        		byte param1Low = (byte)( b.param1 & 0xFF );
        		byte param1High = (byte)( ( b.param1 & 0xFF00 ) >> 8 );
        		cabecera[ INICIO_PARAM1 ] = param1Low;
        		cabecera[ INICIO_PARAM1 + 1 ] = param1High;
        		
        		byte param2Low = (byte)( b.param2 & 0xFF );
        		byte param2High = (byte)( ( b.param2 & 0xFF00 ) >> 8 );
        		cabecera[ INICIO_PARAM2 ] = param2Low;
        		cabecera[ INICIO_PARAM2 + 1 ] = param2High;
        		
        		// Graba el bloque de la cabecera
        		String result = grabarBloque( os, cabecera, (byte)0x00 );
        		if ( result != null ) {
        			return result;
        		}
        		
        		// Graba el bloque de datos
        		result = grabarBloque( os, b.datos, (byte)0xFF );
        		if ( result != null ) {
        			return result;
        		}
            }
        }
        catch (Exception e) {}
        finally {
            if ( os != null ) {
                try {
                    os.close();
                } catch (IOException e) {}
            }
        }
		
		return null;
	}
	
	private String grabarBloque( FileOutputStream os, byte datos[], byte flag ) throws IOException {

		if ( datos.length > 65535 ) {
			return "Block is too long ( > 65535 bytes )";
		}
		
    	// Start of block: 2 bytes for length. Length is data length plus 2 bytes for flag and checksum bytes
    	int byteCount = datos.length + 2;
    	byte byteCountLow = (byte)( byteCount & 0xFF );
    	byte byteCountHigh = (byte)( ( byteCount & 0xFF00 ) >> 8 );
    	os.write( byteCountLow );
    	os.write( byteCountHigh );

    	// Write flag byte
    	os.write( flag );

    	// Writes data
    	os.write( datos );

    	// Compute checksum
    	//int checksum = flag;
    	int checksum = datos[0];
    	int n = datos.length;
    	for ( int i = 1; i < n; i++ ) {
    		checksum ^= datos[ i ];
    	}
    	checksum ^= flag;
    	
    	// Write checksum
    	os.write( checksum & 0xFF );


		return null;
	}
}
