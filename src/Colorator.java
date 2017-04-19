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
 * Clase de aplicacion
 */
import java.util.*;
import java.io.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Colorator
{

    public static int SPRITES_ZXB = 0;
    public static int SPRITES_ARDUINO = 1;
	
    public JFrame frameEditor;

    // Scroll de la pantalla visora
    public JScrollPane scrollPane;

    public PanelEditor panelEditor;
    public PanelToolbar panelToolbar;
    public PanelToolbar2 panelToolbar2;
    public MenuColorator menuColorator;
    public JLabel labelBarraTexto;

    boolean hiResScreenMode;
    
    public Pantalla pantalla;
    boolean pantallaEditada;
    
    public Herramienta herramientaActual;
    public Lapiz lapiz;
    public HerramientaRectangulo herramientaRectangulo;
    public HerramientaRelleno herramientaRelleno;
    public LapizAtributo lapizAtributo;
    public Linea linea;
    public HerramientaSeleccionLapiz herramientaSeleccionLapiz;
    public HerramientaSeleccionLapizFree herramientaSeleccionLapizFree;
    public HerramientaSeleccionRectangulo herramientaSeleccionRectangulo;
    public HerramientaSeleccionBloques herramientaSeleccionBloques;
    public HerramientaCopiar herramientaCopiar;
    
    public FiltroFlipHorizontal filtroFlipHorizontal;
    public FiltroFlipVertical filtroFlipVertical;
    public FiltroRotarDerecha filtroRotarDerecha;
    public FiltroRotarIzquierda filtroRotarIzquierda;

    public HiloRefrescoFlash hiloRefrescoFlash;
    
    public byte attrsActual;
    public boolean modoTransparentePaper;
    public boolean modoTransparenteInk;
    public boolean modoTransparenteBright;
    public boolean modoTransparenteFlash;
    public boolean gridVisible;
    
    public ArrayList<Pantalla> undo;
    public int indexUndo;
    
    public static void main(String args[]) {
        
        System.out.println("Colorator graphical editor");
        System.out.println("Copyright (C) 2009  Juan Jose Luna Espinosa juanjoluna@gmail.com");
        System.out.println("");
        System.out.println("This program is free software: you can redistribute it and/or modify");
        System.out.println("it under the terms of the GNU General Public License as published by");
        System.out.println("the Free Software Foundation, version 3 of the License.");
        System.out.println("");
        System.out.println("This program is distributed in the hope that it will be useful,");
        System.out.println("but WITHOUT ANY WARRANTY; without even the implied warranty of");
        System.out.println("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the");
        System.out.println("GNU General Public License for more details.");
        System.out.println("");
        System.out.println("You should have received a copy of the GNU General Public License");
        System.out.println("along with this program.  If not, see <http://www.gnu.org/licenses/>.");
        System.out.println("");
        
        if ( args.length == 0 ) {
            System.out.println("Parameters:");
            System.out.println("-gui Show editor and toolbar in separated windows.");
            System.out.println("-f file Load file. The extension indicates the file type:");
            System.out.println("    *.colorator: Colorator internal file type");
            System.out.println("    *.scr: SCR file format");
        }
        
        Colorator colorator = new Colorator(args);
        
    }

    public Colorator(String args[]) {

        /*
         * Captura de parametros
         */
        boolean guiOneFrame = true;
        String fileToLoad = "../examples/demo-colorator.colorator";
        int i = 0;
        while ( i < args.length ) {
            if ( args[i].compareTo("-gui")==0 ) {
                guiOneFrame = false;
            }
            else if ( args[i].compareTo("-f")==0 ) {
            	if ( args.length > i ) {
            		fileToLoad = args[ i + 1 ];
            		i++;
            	}
            }
            i++;
        }

        hiResScreenMode = true;
        
        pantalla = new PantallaHi();
        pantalla.borra();
        pantallaEditada = false;
        
        attrsActual = Pantalla.createAttribute(false, false, 7, 0);
        modoTransparentePaper = false;
        modoTransparenteInk = false;
        
        lapiz = new Lapiz(this);
        herramientaRectangulo = new HerramientaRectangulo(this);
        herramientaRelleno = new HerramientaRelleno(this);
        lapizAtributo = new LapizAtributo(this);
        linea = new Linea(this);
        herramientaCopiar = new HerramientaCopiar(this);
        herramientaSeleccionBloques = new HerramientaSeleccionBloques(this);
        herramientaSeleccionRectangulo= new HerramientaSeleccionRectangulo( this );
        herramientaSeleccionLapiz = new HerramientaSeleccionLapiz( this );
        herramientaSeleccionLapizFree = new HerramientaSeleccionLapizFree( this );

        // La herramienta inicial es una dummy que hara padding y cogera atributos.
        // Para empezar a hacer algo el usuario debe seleccionar una herramienta con el GUI
        herramientaActual = new Herramienta( this );
        
        filtroFlipHorizontal = new FiltroFlipHorizontal( this );
        filtroFlipVertical = new FiltroFlipVertical( this );
        filtroRotarIzquierda = new FiltroRotarIzquierda( this );
        filtroRotarDerecha = new FiltroRotarDerecha( this );

        undo = new ArrayList<Pantalla>();
        newActionUndo();
        indexUndo = 0;
       
        /*
         * Inicializacion del GUI                
        */

        JFrame frameEditor = null;

        this.panelEditor = new PanelEditor(this);
        scrollPane = new JScrollPane( this.panelEditor );

        this.panelToolbar = new PanelToolbar(this);
        
        this.panelToolbar2 = new PanelToolbar2(this);
        
        this.menuColorator = new MenuColorator(this);
        JMenuBar menuBar = menuColorator.crearMenuBar();

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        contentPane.add( scrollPane, BorderLayout.CENTER );

        labelBarraTexto = new JLabel( "Colorator is loading..." );
        contentPane.add( labelBarraTexto, BorderLayout.SOUTH );

        if ( ! guiOneFrame ) {
            frameEditor = new JFrame("Colorator");
            frameEditor.setSize(new Dimension(800,600));
            
            frameEditor.setJMenuBar(menuBar);
            frameEditor.setContentPane(contentPane);
            
            frameEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameEditor.pack();
            frameEditor.setVisible(true);

            JFrame frameToolbar = new JFrame("");
            frameToolbar.getContentPane().add(this.panelToolbar);
            frameToolbar.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frameToolbar.pack();
            frameToolbar.setVisible(true);
            
            JFrame frameToolbar2 = new JFrame("");
            frameToolbar2.getContentPane().add(this.panelToolbar2);
            frameToolbar2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frameToolbar2.pack();
            frameToolbar2.setVisible(true);
        }
        else {
            frameEditor = new JFrame("Colorator");
            frameEditor.setSize(new Dimension(800,600));
  
            contentPane.add(panelToolbar, BorderLayout.WEST);
            contentPane.add(panelToolbar2, BorderLayout.EAST);

            frameEditor.setJMenuBar(menuBar);
            frameEditor.setContentPane(contentPane);

            frameEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameEditor.pack();
            frameEditor.setVisible(true);
        }
        
        this.frameEditor = frameEditor;

        frameEditor.setTitle( "Colorator" );

        Toolkit tk = Toolkit.getDefaultToolkit();
        Image icono = tk.createImage( "../iconos/colorator.png" );
        frameEditor.setIconImage( icono );

        if ( fileToLoad != null ) {

        	newActionUndo();

        	if ( fileToLoad.endsWith( ".colorator" ) ) {
        		cargar( new File( fileToLoad ) );
        	}
        	else if ( fileToLoad.endsWith( ".scr" ) ) {
        		cargarScr( new File( fileToLoad ) );
        	}
        	
        	finishActionUndo();
        }

        hiloRefrescoFlash = new HiloRefrescoFlash();
        hiloRefrescoFlash.colorator = this;
        hiloRefrescoFlash.start();

        setBarraTexto( "Note: Hi-color screen selected by default. To change it, select Edit > Screen mode > Normal SCR color mode." );
    }
    
    public void setBarraTexto( String texto ) {
    	labelBarraTexto.setText( texto );
    }

    public void changeScreenMode( boolean hiResScreenMode ) {
        if (this.hiResScreenMode == hiResScreenMode) {
            return;
        }
        
        if ( ! confirmUnsavedChangesDiscard() ) {
            return;
        }
        
        this.hiResScreenMode = hiResScreenMode;
        
        if ( hiResScreenMode ) {
            menuColorator.opcionAbrir.setEnabled(true);
            menuColorator.opcionGuardar.setEnabled(true);
            menuColorator.opcionAbrirAtributosAlta.setEnabled(true);
            menuColorator.opcionGuardarAtributosAlta.setEnabled(true);
            menuColorator.opcionImportarSoloHI.setEnabled(true);
            panelToolbar.btnOpen.setEnabled(true);
            panelToolbar.btnSave.setEnabled(true);
            
            pantalla = new PantallaHi();
            
            setBarraTexto( "Mode set to hi color resolution image." );
        }
        else {
            menuColorator.opcionAbrir.setEnabled(false);
            menuColorator.opcionGuardar.setEnabled(false);
            menuColorator.opcionAbrirAtributosAlta.setEnabled(false);
            menuColorator.opcionGuardarAtributosAlta.setEnabled(false);
            menuColorator.opcionImportarSoloHI.setEnabled(false);
            panelToolbar.btnOpen.setEnabled(false);
            panelToolbar.btnSave.setEnabled(false);
            
            pantalla = new PantallaSCR();
            
            setBarraTexto( "Mode set to normal Spectrum SCR image." );
        }
        
        pantalla.borra();
        undo.clear();
        newActionUndo();
        indexUndo = 0;
        
        panelEditor.repaint();
        pantallaEditada = false;
    }
    
    // After this function executes, member pantalla is supposed to be changed. Unless eraseNewActionUndo() is called.
    public void newActionUndo() {
    	newActionUndo( null );
    }
    public void newActionUndo( Pantalla p ) {
        while ( indexUndo < ( undo.size() - 1 ) ) {
            undo.remove( undo.size() - 1 );
        }
        if ( p == null ) {
        	p = pantalla.clonar();
        }
        undo.add(p);
        indexUndo++;

    }
    
    // Call this function only just after newActionUndo()
    public void eraseNewActionUndo() {
    	
    }

    public void finishActionUndo() {
        ( ( Pantalla ) undo.get( indexUndo ) ).copiarDe( pantalla );
    }
    
    public void makeUndo() {
        
        if ( herramientaActual != null && herramientaActual.enUso ) {
            return;
        }
        
        if ( indexUndo > 0 ) {

            indexUndo--;
            
            pantalla.copiarDe( undo.get( indexUndo ) );
            
            panelEditor.repaint();
        }
    }
    
    public void makeRedo() {
        
        if ( herramientaActual != null && herramientaActual.enUso ) {
            return;
        }
        
        if ( indexUndo < ( undo.size() - 1 ) ) {

            indexUndo++;
           
            pantalla.copiarDe( undo.get( indexUndo ) );
            
            panelEditor.repaint();
        }
    }

    public void setAttrActual( byte attr ) {

    	attrsActual = attr;
	    
	    panelToolbar2.seleccionarBtnInk( 
	    panelToolbar2.btnInkArray[ Pantalla.getInk(attr) ] );
	
	    panelToolbar2.seleccionarBtnPaper( 
	    panelToolbar2.btnPaperArray[ Pantalla.getPaper(attr) ] );
	        
	    panelToolbar2.btnBright.setSelected( Pantalla.getBright(attr) );
	    panelToolbar2.seleccionarBtnBright();
    }
    
    public void seleccionarHerramienta( Herramienta h ) {
    	
    	if ( h == lapiz ) {
    		panelToolbar.seleccionarBtnHerramienta( panelToolbar.btnLapiz );
    		setBarraTexto( "Pen tool. Left click to paint pixels, right click to erase pixels. Ctrl to get attributes." );
    	}
    	else if ( h == herramientaRectangulo ) {
    		panelToolbar.seleccionarBtnHerramienta( panelToolbar.btnRectangulo );
    		setBarraTexto( "Rectangle tool. Left click and drag to paint a rectangle, right click and drag to erase a rectangle. Ctrl to get attributes." );
    	}
    	else if ( h == herramientaRelleno ) {
    		panelToolbar.seleccionarBtnHerramienta( panelToolbar.btnRellenar );
    		setBarraTexto( "Floodfill tool. Left click to flood ink, right click to flood paper. Ctrl to get attributes." );
    	}
    	else if ( h == lapizAtributo ) {
    		panelToolbar.seleccionarBtnHerramienta( panelToolbar.btnPintarAtributo );
    		setBarraTexto( "Attribute pen tool. Left click to paint attributes, right click to interchange ink and paper. Ctrl to get attributes." );
    	}
    	else if ( h == linea ) {
    		panelToolbar.seleccionarBtnHerramienta( panelToolbar.btnLinea );
    		setBarraTexto( "Line tool. Left click and drag to draw a line, right click and drag to erase a line. Ctrl to get attributes." );
    	}
    	else if ( h == herramientaCopiar ) {
    		panelToolbar.seleccionarBtnHerramienta( panelToolbar.btnCopy );
    		setBarraTexto( "Copy tool. Left click and drag to copy pixel-wise. Right click and drag to copy block-wise." );
    	}
    	else if ( h == herramientaSeleccionBloques ) {
    		panelToolbar.seleccionarBtnHerramienta( panelToolbar.btnSelectBlock );
    		setBarraTexto( "Block select tool. Left click and drag to select a rectangle of blocks. Right click and drag to deselect a rectangle of blocks." );
    	}
    	else if ( h == herramientaSeleccionRectangulo ) {
    		panelToolbar.seleccionarBtnHerramienta( panelToolbar.btnSelectRect );
    		setBarraTexto( "Rectangle select tool. Left click and drag to select a rectangle. Right click and drag to deselect a rectangle." );
    	}
    	else if ( h == herramientaSeleccionLapiz ) {
    		panelToolbar.seleccionarBtnHerramienta( panelToolbar.btnSelectPen );
    		setBarraTexto( "Pen select tool. Left click and drag to draw selection. Right click and drag to draw deselection." );
    	}
    	else if ( h == herramientaSeleccionLapizFree ) {
    		panelToolbar.seleccionarBtnHerramienta( panelToolbar.btnSelectFree );
    		setBarraTexto( "Free select tool. Left click and drag to draw closed selection. Right click and drag to draw closed deselection." );
    	}

    	herramientaActual = h;
    }

    public void eraseBitmap() {
    	
    	newActionUndo();
    	
    	pantalla.rellena();

    	finishActionUndo();
    	
    	panelEditor.repaint();

    }

    public void invertBitmap() {
    	
    	newActionUndo();
    	
    	pantalla.invierteBitmap();

    	finishActionUndo();
    	
    	panelEditor.repaint();

    }

    public void eraseColours() {

    	newActionUndo();

    	for (int i = 0; i < Pantalla.BITMAP_X;i++) {
            for (int j = 0; j < Pantalla.BITMAP_Y;j++) {
                pantalla.pintarPixel( i, j,
                		false, attrsActual,
                		modoTransparentePaper, modoTransparenteInk, modoTransparenteBright, modoTransparenteFlash,
                		true, false );
            }
    	}

    	finishActionUndo();

    	panelEditor.repaint();
    }

    public void desplazarPantallaIzquierda() {
        newActionUndo();
        pantalla.desplazarIzquierda();
        panelEditor.repaint();
    }
    
    public void desplazarPantallaDerecha() {
        newActionUndo();
        pantalla.desplazarDerecha();
        panelEditor.repaint();
    }
    
    public void desplazarPantallaArriba() {
        newActionUndo();
        pantalla.desplazarArriba();
        panelEditor.repaint();
    }
    
    public void desplazarPantallaAbajo() {
        newActionUndo();
        pantalla.desplazarAbajo();
        panelEditor.repaint();
    }

    public void desplazarPantallaAbajoColor() {
        newActionUndo();
        pantalla.desplazarAbajoColor();
        panelEditor.repaint();
    }

    public void desplazarPantallaArribaColor() {
        newActionUndo();
        pantalla.desplazarArribaColor();
        panelEditor.repaint();
    }

    public void aplicarFiltroFlipHorizontal() {
        newActionUndo();
        filtroFlipHorizontal.filtrar();
        panelEditor.repaint();
    }

    public void aplicarFiltroFlipVertical() {
        newActionUndo();
        filtroFlipVertical.filtrar();
        panelEditor.repaint();
    }

    public void aplicarFiltroRotarDerecha() {
        newActionUndo();
        filtroRotarDerecha.filtrar();
        panelEditor.repaint();
    }

    public void aplicarFiltroRotarIzquierda() {
        newActionUndo();
        filtroRotarIzquierda.filtrar();
        panelEditor.repaint();
    }

    public void salvar(File file) {
        if ( pantalla.grabarEnFichero( file ) ) {
        	setBarraTexto( "High resolution color image saved." );
        	pantallaEditada = false;
        }
        else {
        	panelEditor.mostrarMensaje( "There was an error saving high resolution color image." );
        }
    }

    public void salvarSCR(File file) {
        if ( pantalla.grabarEnFicheroScr( file ) ) {
        
	        if ( ! hiResScreenMode ) {
	            pantallaEditada = false;
	        }
	        
	        setBarraTexto( "Spectrum SCR image saved." );
        }
        else {
        	panelEditor.mostrarMensaje( "There was an error saving Spectrum SCR image." );
        }
    }
    
    public void salvarAtributosAlta(File file) {
        if ( pantalla.grabarEnFicheroAtributosAlta( file ) ) {
        	setBarraTexto( "Hi color attributes saved to file " + file.getName() );
        }
        else {
        	panelEditor.mostrarMensaje( "There was an error saving hi color attributes to file " + file.getName() );
        }
    }
    
    public void cargar(File file) {

        if ( hiResScreenMode ) {
            pantalla = PantallaHi.cargarDeFichero(file);
        }
        else {
            pantalla = PantallaSCR.cargarDeFichero(file);
        }
        
        if ( pantalla == null ) {
            
            panelEditor.mostrarMensaje("There was an error while loading the image.");
            
            if ( hiResScreenMode ) {
                pantalla = new PantallaHi();
            }
            else {
                pantalla = new PantallaSCR();
            }
            pantalla.rellena();
        }

        panelEditor.repaint();
        
        setBarraTexto( "High resolution color image loaded." );
    }

    public void cargarScr(File file) {

    	newActionUndo();

    	String error = pantalla.cargarDeFicheroScr(file);

        if ( error != null ) {
            panelEditor.mostrarMensaje( error );
        }
        
        finishActionUndo();

        panelEditor.repaint();
        
        setBarraTexto( "Spectrum SCR image loaded." );
    }

    public void cargarAtributosAlta(File file) {
        
        if ( ! hiResScreenMode ) {
            return;
        }

    	newActionUndo();

        if ( ! ((PantallaHi)pantalla).cargarDeFicheroAtributosAlta( file ) ) {
            panelEditor.mostrarMensaje("There was an error loading hi-res attributes");
        }
        
        finishActionUndo();
        
        panelEditor.repaint();
    }

    public void importarImagenSoloHI(File file) {

    	newActionUndo();
    	
        JFrame frameProgressBar = new JFrame("Importing image...");

        JProgressBar progressBar;
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        JPanel panelProgressBar = new JPanel();
        panelProgressBar.setLayout( new BorderLayout() );
        panelProgressBar.add( new JLabel("Importing image..."), BorderLayout.NORTH );
        panelProgressBar.add( progressBar );

        frameProgressBar.setContentPane( panelProgressBar );
        frameProgressBar.pack();
        frameProgressBar.setVisible(true);

        
        String errorMessage = Importador.importarImagenSoloHI( Importador.cargarYEscalarImagen( file ), (PantallaHi)pantalla, false, progressBar, panelProgressBar );

        frameProgressBar.dispose();
        
        if ( errorMessage != null ) {
            panelEditor.mostrarMensaje( errorMessage );
        }

        finishActionUndo();
        
        panelEditor.repaint();
        
        setBarraTexto( "Image imported." );
    }

    public void importarImagenEntera(File file) {

    	newActionUndo();

    	if ( hiResScreenMode ) {
	        String errorMessage = Importador.importarImagenEnteraHi( Importador.cargarYEscalarImagen( file), (PantallaHi)pantalla );
	        
	        if ( errorMessage != null ) {
	            panelEditor.mostrarMensaje( errorMessage );
	        }
    	}
    	else {
	        Importador.importarImagenEnteraLow( Importador.cargarYEscalarImagen( file), (PantallaSCR)pantalla );
    	}
        finishActionUndo();

        panelEditor.repaint();
        
        setBarraTexto( "Image imported." );
    }

    public void exportarImagenEntera(File file) {

        String errorMessage = pantalla.exportarImagenEntera( file );
        
        if ( errorMessage != null ) {
            panelEditor.mostrarMensaje( errorMessage );
            return;
        }

        pantallaEditada = false;
        
        setBarraTexto( "Image exported." );
    }
    
    public void exportarFicheroTAP( File file ) {

    	if ( pantalla.getClass().isAssignableFrom( PantallaHi.class ) ) {

    		String nombreFichSpectrum = file.getName();

    		TAP tap = new TAP();

	    	// Bloque cargador basic
	    	String pathCargador = "../TAP Colorator/hiloader.bin";
	    	byte [] cargadorBasic = UtilsFicherosBinario.leerFicheroBinario( pathCargador );
	    	if ( cargadorBasic == null ) {
	    		panelEditor.mostrarMensaje( "Unable to find file " + pathCargador );
	    		return;
	    	}
	    	cambiarColoresBloqueBasic( cargadorBasic );
	    	
	    	tap.nuevoBloque( TAP.BLOQUE_BASIC, nombreFichSpectrum, cargadorBasic, 0, cargadorBasic.length, 10, 0 );
	    	
	    	// Bloque rutina CM
	    	String pathRutinaCM = "../TAP Colorator/COLR8R.CM datos.bin";
	    	byte [] rutinaCM = UtilsFicherosBinario.leerFicheroBinario( pathRutinaCM );
	    	if ( rutinaCM == null ) {
	    		panelEditor.mostrarMensaje( "Unable to find file " + pathRutinaCM );
	    		return;
	    	}
	    	
	    	tap.nuevoBloque( TAP.BLOQUE_CODE, nombreFichSpectrum, rutinaCM, 0, rutinaCM.length, 49407, 0 );
	    	
	    	// Bloque de colores HI
	    	    	
	    	byte [] bloqueHi = ((PantallaHi)pantalla).attrsHi;
	    	
	    	tap.nuevoBloque( TAP.BLOQUE_CODE, nombreFichSpectrum, bloqueHi, 0, bloqueHi.length, 32768, 0 );
	    	
	    	// Bloque pantalla SCR

	    	byte datosSCR[] = pantalla.obtenerEnMemoriaScr();
	    	tap.nuevoBloque( TAP.BLOQUE_SCR, nombreFichSpectrum, datosSCR, 0, datosSCR.length, 0, 0 );
	    	
	    	tap.grabarFicheroTAP( file );
    	}
    	
    	else {
    		
    		TAP tap = new TAP();

	    	// Bloque cargador basic
	    	String pathCargador = "../TAP Colorator/scrloader.bin";
	    	byte [] cargadorBasic = UtilsFicherosBinario.leerFicheroBinario( pathCargador );
	    	if ( cargadorBasic == null ) {
	    		panelEditor.mostrarMensaje( "Unable to find file " + pathCargador );
	    		return;
	    	}
	    	cambiarColoresBloqueBasic( cargadorBasic );

//	    	tap.nuevoBloque( TAP.BLOQUE_BASIC, file.getName(), cargadorBasic, 0, cargadorBasic.length, 10/*Line 0*/, 0 /*dont care*/);

	    	byte datos[] = pantalla.obtenerEnMemoriaScr();
	    	
	    	tap.nuevoBloque( TAP.BLOQUE_SCR, file.getName(), datos, 0, datos.length, 0, 0 );
	    	
	    	tap.grabarFicheroTAP( file );
    	}
    }

    public void cambiarColoresBloqueBasic( byte programaBasic[] ) {
    	
    	// Esta funcion analiza un bloque de bytes que representa un programa en Basic de Sinclair y
    	// sustituye todos los comandos INK, PAPER, BORDER, BRIGHT y FLASH por los atributos actuales
    	// de edicion de Colorator. El border se pone igual que el paper.
    	
    	// Bucle por todos los bytes, pero asegurando que hay 5 bytes por delante (los ultimos no pueden ser comandos)
    	int n = programaBasic.length - 5;
    	for ( int i = 0; i < n; i++ ) {

    		byte b = programaBasic[ i ];
    		
    		// Todos estos comandos de Basic son iguales, si el byte 0 es el de comando, el byte 1 es el
    		// codigo textual del numero (0x30 + numero), y el byte 5 es el numero tal cual (dentro de un
    		// numero de coma flotante)
    		byte byteDato = 0;
    		boolean comandoEncontrado = false;
    		
    		if ( b == ((byte)0xD9) ) {
    			// Comando INK
    			byteDato = (byte)Pantalla.getInk( attrsActual );
    			comandoEncontrado = true;
    		}
    		else if ( b == ((byte)0xDA) ) {
    			// Comando PAPER
    			byteDato = (byte)Pantalla.getPaper( attrsActual );
    			comandoEncontrado = true;
    		}
    		else if ( b == ((byte)0xDB) ) {
    			// Comando FLASH
    			byteDato = Pantalla.getFlash( attrsActual ) ? (byte)0x01 : (byte)0x00;
    			comandoEncontrado = true;
    		}
    		else if ( b == ((byte)0xDC) ) {
    			// Comando BRIGHT
    			byteDato = Pantalla.getBright( attrsActual ) ? (byte)0x01 : (byte)0x00;
    			comandoEncontrado = true;
    		}
    		else if ( b == ((byte)0xE7) ) {
    			// Comando BORDER
    			byteDato = (byte)Pantalla.getPaper( attrsActual );
    			comandoEncontrado = true;
    		}

    		if ( comandoEncontrado ) {
    			
    			// Hemos asegurado que habia 5 bytes por delante por lo que podemos hacer
    			// estas modificaciones:
    			
    			// El byte 1 es el codigo textual del valor:
    			programaBasic[ i + 1 ] = (byte) ( byteDato + 0x030 );
    			
    			// El byte 5 es el valor tal cual:
    			programaBasic[ i + 5 ] = byteDato;
    			
    		}
    	}

    }
    
    public void exportarSprites2x2( File file, int tipo ) {
    	
    	// Tipo es de los definidos en Colorator

    	ArrayList<Integer> bitmapBytes = new ArrayList<Integer>();
    	ArrayList<Integer> attributeBytes = new ArrayList<Integer>();

    	int indGrafico = 0;
    	boolean fin = false;

    	int attrsX = Pantalla.BITMAP_X / 8;
    	int attrsY = Pantalla.BITMAP_Y / 8;
    	int maxGraficos = ( attrsX / 2 ) * ( attrsY / 2 ); 
    	
    	while ( ! fin ) {
    		
    		// Coordenadas de grafico
    		int j = indGrafico / ( attrsX / 2 );
    		int i = indGrafico - j * ( attrsX / 2 );
    		
    		// Coordenadas del primer bloque
    		int bi = i * 2;
    		int bj = j * 2;
    		
    		// Condicion de fin: pantalla entera completada
    		if ( indGrafico >= maxGraficos ) {
    			fin = true;
    			break;
    		}
    		// Condicion de fin: si el paper y el ink del primer bloque del grafico son 0, termina de obtener graficos
    		byte attr = pantalla.getAttributePixel( bi * 8, bj * 8 );
    		if ( Pantalla.getInk( attr ) == 0 && Pantalla.getPaper( attr ) == 0 ) {
    			fin = true;
    			break;
    		}
    		
    		// Bucle por los 4 bloques del grafico
    		for ( int rbj = 0; rbj < 2; rbj++ ) {
    			int abj = bj + rbj;
    			for ( int rbi = 0; rbi < 2; rbi++ ) {
    				int abi = bi + rbi;
    				
    				// Bucle para las 8 filas del bloque
    				for ( int fila = 0; fila < 8; fila++ ) {
    					int pj = abj * 8 + fila;
    					
    					int valorFila = 0;
    					int valorPixel = 128;

    					// Bucle para los 8 pixels de la fila
    					for ( int col = 0; col < 8; col++ ) {
    						int pi = abi * 8 + col;

    						if ( pantalla.getBitmap( pi, pj ) ) {
    							valorFila += valorPixel;
    						}
    						valorPixel >>= 1;
    					}
    					
    					// Guarda el byte generado
    					bitmapBytes.add( new Integer( valorFila ) );
    				}
    				
    				// Guarda los atributos del bloque
    				attributeBytes.add( new Integer( pantalla.getAttributePixel( abi * 8, abj * 8) ) );
    			}
    		}
    		
    		indGrafico++;
    		
    	}

    	if ( tipo == SPRITES_ZXB ) {
    		generarTextoZXB2x2( file, indGrafico, bitmapBytes, attributeBytes );
    		
    		setBarraTexto( "Done exporting 2x2 ZXB graphics." );
    	}
    	else if ( tipo == SPRITES_ARDUINO ) {
    		generarTextoArduino2x2( file, indGrafico, bitmapBytes, attributeBytes );
    		
    		setBarraTexto( "Done exporting 2x2 Arduino graphics." );
    	}
    }

    public void generarTextoZXB2x2( File file, int numGraficos, ArrayList<Integer> bitmapBytes, ArrayList<Integer> attributeBytes ) {

        StringBuffer strBuf = new StringBuffer();

        strBuf.append("' Sprites exported from " + file + "\n\n");

        strBuf.append( "#define NUM_GRAPHICS " + numGraficos + "\n" );

        strBuf.append( "dim graphics( 0 to NUM_GRAPHICS * 32 -1 ) as ubyte => _\n" );
        strBuf.append( "{ _\n" );
        
        for ( int i = 0; i < numGraficos; i++ ) {
        	strBuf.append( "\t" );
        	for ( int j = 0; j < 32; j++ ) {
        		strBuf.append( bitmapBytes.get( i * 32 + j ) );
        		if ( j < 31 || i < numGraficos - 1 ) {
        			strBuf.append( ", ");
        		}
        	}
       		strBuf.append( " _\n" );
        }

        strBuf.append( "}\n\ndim attributes( 0 to NUM_GRAPHICS * 4 -1 ) as ubyte => _\n{ _\n\t" );
        int numBloques = numGraficos * 4;
        for ( int i = 0; i < numBloques; i++ ) {
        	strBuf.append( attributeBytes.get( i ) );
        	if ( i < numBloques - 1 ) {
    			strBuf.append( ", ");
            	if ( ( i + 1 ) % 32 == 0 ) {
            		strBuf.append( "_\n\t");
            	}
        	}
        }
        strBuf.append( " _\n}\n" );

/*
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("' Sprites exported from " + file + '\n');

        strBuf.append( "#define NUM_UFO_GRAPHICS 2" );
        
        for ( int indSprite = 0; indSprite < 5; indSprite++ ) {
        	strBuf.append( "\n\n\n' Sprite #" + indSprite + '\n' );
        	for ( int bj = 0; bj < 2; bj++) {
        		for ( int bi = 0; bi < 2; bi++) {
        			// Bloque bi,bj del sprite indSprite
        			strBuf.append( "' Block " + bi + ", " + bj + '\n' );
        			for ( int fila = 0; fila < 8; fila++) {
        				strBuf.append("poke 65368 + " + ( fila + bi * 8 + bj * 2 * 8 + indSprite * 4 * 8 ) + ", " );
            			for ( int columna = 0; columna < 8; columna++) {
            				int x = indSprite * 2 * 8 + bi * 8 + columna;
            				if ( pantalla.getBitmap(x, fila + bj * 8)) {
            					strBuf.append("1");
            				}
            				else {
            					strBuf.append("0");
            				}
            			}
            			strBuf.append("b\n");
        			}
        		}
        	}
        }
*/

        // Guarda el fichero
        BufferedWriter output = null;
        try {
            output = new BufferedWriter( new FileWriter( file ) );
            output.write( strBuf.toString() );
        }
        catch ( IOException e ) {
            panelEditor.mostrarMensaje( "Couldn't save file " + file );
            return;
        }
        try {
            output.close();
        }
        catch ( IOException e ) {
            // Nada que hacer
        }

    }

    public void generarTextoArduino2x2( File file, int numGraficos, ArrayList<Integer> bitmapBytes, ArrayList<Integer> attributeBytes ) {

        StringBuffer strBuf = new StringBuffer();

        strBuf.append("// Sprites exported from " + file + "\n\n");

        strBuf.append( "const int numGraphicsBitmap1 = " + numGraficos + ";\n" );

        strBuf.append( "PROGMEM unsigned char bitmap1[] = {" );

        for ( int i = 0; i < numGraficos; i++ ) {
        	strBuf.append( "\n\t" );
        	for ( int j = 0; j < 32; j++ ) {
        		strBuf.append( bitmapBytes.get( i * 32 + j ) );
        		if ( j < 31 || i < numGraficos - 1 ) {
        			strBuf.append( ", ");
        		}
        	}
        }

        strBuf.append( "\n};" );
        
        // Guarda el fichero
        BufferedWriter output = null;
        try {
            output = new BufferedWriter( new FileWriter( file ) );
            output.write( strBuf.toString() );
        }
        catch ( IOException e ) {
            panelEditor.mostrarMensaje( "Couldn't save file " + file );
            return;
        }
        try {
            output.close();
        }
        catch ( IOException e ) {
            // Nada que hacer
        }

    }

    public void exportarSprites1x1( File file, int tipo ) {
    	
    	// Tipo es de los definidos en Colorator

    	ArrayList<Integer> bitmapBytes = new ArrayList<Integer>();
    	ArrayList<Integer> attributeBytes = new ArrayList<Integer>();

    	int indGrafico = 0;
    	boolean fin = false;

    	int attrsX = Pantalla.BITMAP_X / 8;
    	int attrsY = Pantalla.BITMAP_Y / 8;
    	int maxGraficos = ( attrsX ) * ( attrsY ); 
    	
    	while ( ! fin ) {
    		
    		// Coordenadas de grafico
    		int j = indGrafico / attrsX;
    		int i = indGrafico - j * attrsX;
    		
    		// Condicion de fin: pantalla entera completada
    		if ( indGrafico >= maxGraficos ) {
    			fin = true;
    			break;
    		}
    		// Condicion de fin: si el paper y el ink del bloque son 0, termina de obtener graficos
    		byte attr = pantalla.getAttributePixel( i * 8, j * 8 );
    		if ( Pantalla.getInk( attr ) == 0 && Pantalla.getPaper( attr ) == 0 ) {
    			fin = true;
    			break;
    		}
    		
			// Bucle para las 8 filas del bloque
			for ( int fila = 0; fila < 8; fila++ ) {
				int pj = j * 8 + fila;
				
				int valorFila = 0;
				int valorPixel = 128;

				// Bucle para los 8 pixels de la fila
				for ( int col = 0; col < 8; col++ ) {
					int pi = i * 8 + col;

					if ( pantalla.getBitmap( pi, pj ) ) {
						valorFila += valorPixel;
					}
					valorPixel >>= 1;
				}
				
				// Guarda el byte generado
				bitmapBytes.add( new Integer( valorFila ) );
			}
    				
			// Guarda los atributos del bloque
			attributeBytes.add( new Integer( pantalla.getAttributePixel( i * 8, j * 8) ) );

    		indGrafico++;
    		
    	}

    	if ( tipo == SPRITES_ZXB ) {
    		generarTextoZXB1x1( file, indGrafico, bitmapBytes, attributeBytes );
    	}
    	else if ( tipo == SPRITES_ARDUINO ) {
    		// Nada que hacer
    	}
    	
    	setBarraTexto( "Done exporting 1x1 graphics." );
    }

    public void generarTextoZXB1x1_antigua( File file, int numGraficos, ArrayList<Integer> bitmapBytes, ArrayList<Integer> attributeBytes ) {

    	// Por cada grafico hay 8 bytes de bitmap y 1 de atributos
    	
        StringBuffer strBuf = new StringBuffer();

        strBuf.append( "\n\n#define NUM_GRAPHICS " + numGraficos + "\n" );

        /*
        strBuf.append( "dim graphics( 0 to NUM_GRAPHICS * 8 -1 ) as ubyte => _\n" );
        strBuf.append( "{ _\n" );
        */

        strBuf.append( "static unsigned char graphics[] = {\n" );
        
        for ( int i = 0; i < numGraficos; i++ ) {
        	strBuf.append( "\t" );
        	for ( int j = 0; j < 8; j++ ) {
        		strBuf.append( bitmapBytes.get( i * 8 + j ) );
        		if ( j < 7 || i < numGraficos - 1 ) {
        			strBuf.append( ", ");
        		}
        	}
       		strBuf.append( " \n" );
        }

        strBuf.append( "};\n" );
        
        if ( attributeBytes != null ) {
	        strBuf.append( "\ndim attributes( 0 to NUM_GRAPHICS -1 ) as ubyte => _\n{ _\n\t" );
	        int numBloques = numGraficos * 1;
	        for ( int i = 0; i < numBloques; i++ ) {
	        	strBuf.append( attributeBytes.get( i ) );
	        	if ( i < numBloques - 1 ) {
	    			strBuf.append( ", ");
	            	if ( ( i + 1 ) % 32 == 0 ) {
	            		strBuf.append( "_\n\t");
	            	}
	        	}
	        }
	        strBuf.append( " _\n}\n" );
        }

        // Guarda el fichero
        BufferedWriter output = null;
        try {
            output = new BufferedWriter( new FileWriter( file ) );
            output.write( strBuf.toString() );
        }
        catch ( IOException e ) {
            panelEditor.mostrarMensaje( "Couldn't save file " + file );
            return;
        }
        try {
            output.close();
        }
        catch ( IOException e ) {
            // Nada que hacer
        }

    }

    public void generarTextoZXB1x1( File file, int numGraficos, ArrayList<Integer> bitmapBytes, ArrayList<Integer> attributeBytes ) {

    	// Por cada grafico hay 8 bytes de bitmap y 1 de atributos

        StringBuffer strBuf = new StringBuffer();

        for ( int i = 0; i < numGraficos; i++ ) {
        	for ( int j = 0; j < 8; j++ ) {
        		strBuf.append( Integer.toHexString( bitmapBytes.get( i * 8 + j ) ) );
                        strBuf.append( "\n" );
        	}
        }

        // Guarda el fichero
        BufferedWriter output = null;
        try {
            output = new BufferedWriter( new FileWriter( file ) );
            output.write( strBuf.toString() );
        }
        catch ( IOException e ) {
            panelEditor.mostrarMensaje( "Couldn't save file " + file );
            return;
        }
        try {
            output.close();
        }
        catch ( IOException e ) {
            // Nada que hacer
        }

    }


    public void exportarSpritesSeleccion( File file, int tipo ) {

    	// Obtiene bounding box
    	Rectangle rect = new Rectangle();
    	if ( ! pantalla.seleccion.getBBox( rect ) ) {
    		panelEditor.mostrarMensaje( "Can't export (nothing is selected)" );
    		return;
    	}

    	ArrayList<Integer> listaBytes = new ArrayList<Integer>();
    	
    	int x0 = rect.x;
    	int x1 = x0 + rect.width - 1;
    	int y0 = rect.y;
    	int y1 = y0 + rect.height - 1;

    	// Convierte a coordenadas de bloques
    	x0 /= 8;
    	y0 /= 8;
    	x1 /= 8;
    	y1 /= 8;

    	int numBloquesX = x1 - x0 + 1;
    	int numBloquesY = y1 - y0 + 1;
    	int numBloques = numBloquesX * numBloquesY;

    	// Bucle por los bloques seleccionados
		for ( int bi = x0; bi <= x1; bi++ ) {
			for ( int bj = y0; bj <= y1; bj++ ) {

    			// Bucle por las filas del bloque, generando un byte por fila
    			for ( int fila = 0; fila < 8; fila++ ) {

	    			int pj = bi * 8 + fila;

	    	    	int valorFila = 0;
	    	    	int valorPixel = 128;

	    			// Bucle para los 8 pixels de la fila
	    			for ( int col = 0; col < 8; col++ ) {
	    				int pi = bj * 8 + col;
	
	    				if ( pantalla.getBitmap( pj, pi ) ) {
	    					valorFila += valorPixel;
	    				}
	    				valorPixel >>= 1;
	    			}
	    			
	    			// Guarda el byte generado
	    			listaBytes.add( new Integer( valorFila ) );
    			}
    		}
    	}

    	generarTextoZXB1x1( file, numBloques, listaBytes, null );
    }

    public boolean confirmUnsavedChangesDiscard() {
        if ( pantallaEditada == false ) {
            return true;
        }
        
        int result = JOptionPane.showConfirmDialog(panelToolbar,
                                                   "There are unsaved changes. Continue?",
                                                   "Confirm",
                                                   JOptionPane.YES_NO_OPTION,
                                                   JOptionPane.WARNING_MESSAGE);
    
        if (result == JOptionPane.YES_OPTION) {
            return true;
        }
        return false;
    }

	public void seleccionarNada() {
		
		newActionUndo();

		pantalla.seleccion.borrarTodaSeleccion();

    	finishActionUndo();

    	panelEditor.repaint();
	}
}
