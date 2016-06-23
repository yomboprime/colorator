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
 * Exportacion a arduino
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;


public class AkniusTools {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		printParams();
		
		if ( args.length >= 2 ) {
			if ( args[ 0 ].compareTo("-sin") == 0 ) {
				System.out.println( " Writing sin table to " + args[ 1 ] );
				escribeTablaSenos( args[ 1 ] );
			}
		}

	}
	
	public static void printParams() {
		System.out.println( " Aknius Tools v.1.0" );
		System.out.println( " ------------------" );
		System.out.println( "" );
		System.out.println( " Parameters:" );
		System.out.println( " -sin <filename>    Save a file with sin table" );
		System.out.println( "" );
		System.out.println( "" );
		System.out.println( "" );
		
	}
	
	public static void escribeTablaSenos( String nombreFichero ) {

		StringBuffer strBuf = new StringBuffer();

        strBuf.append( "' --- Sin table ---\n" );
        strBuf.append( "dim sinTable(0 to 255) as fixed => _\n{ _\n" );
        int p = 0;
        for ( int l = 0; l < 8; l++ ) {
        	strBuf.append( "\t" );
	        for ( int i = 0; i < 32; i++ ) {
	        	double valor = Math.sin( ( 2.0d * Math.PI / 256.0d ) * p );
	        	strBuf.append( String.format( Locale.ENGLISH, "%.6f", valor ) );
	        	if ( p < 8 * 32 - 1 ) {
	        		strBuf.append( ", ");
	        	}
	        	p++;
	        }
	        strBuf.append( " _\n" );
        }
        strBuf.append( "}\n" );

        escribeFicheroStr( new File( nombreFichero ), strBuf.toString() );
	}
	
	public static void escribeFicheroStr( File fichero, String texto ) {

        // Guarda el fichero
        BufferedWriter output = null;
        try {
            output = new BufferedWriter( new FileWriter( fichero ) );
            output.write( texto );
        }
        catch ( IOException e ) {
        	System.out.println( "Couldn't save file " + fichero );
            return;
        }
        try {
            output.close();
        }
        catch ( IOException e ) {
            // Nada que hacer
        }

	}

}
