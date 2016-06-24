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
 *  Menu de la barra superior
 */
import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MenuColorator implements ActionListener
{
    public Colorator colorator;
    
    public JMenuItem opcionAbrir;
    public JMenuItem opcionGuardar;
    public JMenuItem opcionAbrirScr;
    public JMenuItem opcionGuardarScr;
    public JMenuItem opcionAbrirAtributosAlta;
    public JMenuItem opcionGuardarAtributosAlta;
    public JMenuItem opcionImportarSoloHI;
    public JMenuItem opcionImportarEntera;
    public JMenuItem opcionExportarImagen;
    public JMenuItem opcionExportarTAP;
    public JMenuItem opcionExportarSpritesZXB1x1;
    public JMenuItem opcionExportarSpritesZXB2x2;
    public JMenuItem opcionExportarSpritesZXBSeleccion;
    public JMenuItem opcionExportarSpritesArduino2x2;
    public JMenuItem opcionSalir;
    
    public JMenuItem opcionUndo;
    public JMenuItem opcionRedo;
    public JMenuItem opcionBorrarBitmap;
    public JMenuItem opcionBorrarColores;
    public JMenuItem opcionInvertirBitmap;
    
    public JRadioButtonMenuItem radioModoHiRes;
    public JRadioButtonMenuItem radioModoNormal;

    public JMenuItem opcionSeleccionarNada;
    public JMenuItem opcionSeleccionarLapiz;
    public JMenuItem opcionSeleccionarFree;
    public JMenuItem opcionSeleccionarRectangulo;
    public JMenuItem opcionSeleccionarBloques;

    public JMenuItem opcionLapiz;
    public JMenuItem opcionRectangulo;
    public JMenuItem opcionRelleno;
    public JMenuItem opcionPintarAtributo;
    public JMenuItem opcionLinea;
    public JMenuItem opcionCopiar;
    
    public JMenuItem opcionZoomIn;
    public JMenuItem opcionZoomOut;
    public JMenuItem opcionZoom1;
    public JMenuItem opcionGrid;

    public JMenuItem opcionAbout;

    public MenuColorator(Colorator colorator) {
        this.colorator = colorator;
    }

    public JMenuBar crearMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu menuFichero = new JMenu("File");
        menuFichero.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menuFichero);
        
        opcionAbrir = new JMenuItem("Open", KeyEvent.VK_O);
        opcionAbrir.addActionListener(this);
        menuFichero.add(opcionAbrir);

        opcionGuardar = new JMenuItem("Save", KeyEvent.VK_S);
        opcionGuardar.addActionListener(this);
        menuFichero.add(opcionGuardar);

        menuFichero.addSeparator();

        opcionAbrirScr = new JMenuItem("Open normal SCR image", KeyEvent.VK_N);
        opcionAbrirScr.addActionListener(this);
        menuFichero.add(opcionAbrirScr);

        opcionGuardarScr = new JMenuItem("Save normal SCR image", KeyEvent.VK_O);
        opcionGuardarScr.addActionListener(this);
        menuFichero.add(opcionGuardarScr);

        menuFichero.addSeparator();

        opcionAbrirAtributosAlta = new JMenuItem("Open only hi-resolution color attributes", KeyEvent.VK_I);
        opcionAbrirAtributosAlta.addActionListener(this);
        menuFichero.add(opcionAbrirAtributosAlta);

        opcionGuardarAtributosAlta = new JMenuItem("Save only hi-resolution color attributes", KeyEvent.VK_H);
        opcionGuardarAtributosAlta.addActionListener(this);
        menuFichero.add(opcionGuardarAtributosAlta);

        menuFichero.addSeparator();

        opcionImportarSoloHI = new JMenuItem("Import image to HI res, 112x192", KeyEvent.VK_M);
        opcionImportarSoloHI.addActionListener(this);
        menuFichero.add(opcionImportarSoloHI);

        opcionImportarEntera = new JMenuItem("Import image to HI Res / SCR, any size", KeyEvent.VK_F);
        opcionImportarEntera.addActionListener(this);
        menuFichero.add(opcionImportarEntera);

        menuFichero.addSeparator();

        opcionExportarImagen = new JMenuItem("Export image (Full screen, 256x192, PNG format)", KeyEvent.VK_E);
        opcionExportarImagen.addActionListener(this);
        menuFichero.add(opcionExportarImagen);

        menuFichero.addSeparator();

        opcionExportarSpritesZXB1x1 = new JMenuItem("Export sprites (ZXB format 1x1)", KeyEvent.VK_P);
        opcionExportarSpritesZXB1x1.addActionListener(this);
        menuFichero.add(opcionExportarSpritesZXB1x1);
        
        opcionExportarSpritesZXB2x2 = new JMenuItem("Export sprites (ZXB format 2x2)", KeyEvent.VK_P);
        opcionExportarSpritesZXB2x2.addActionListener(this);
        menuFichero.add(opcionExportarSpritesZXB2x2);
        
        opcionExportarSpritesZXBSeleccion = new JMenuItem("Export sprites (ZXB format, selected blocks)", KeyEvent.VK_C);
        opcionExportarSpritesZXBSeleccion.addActionListener(this);
        menuFichero.add(opcionExportarSpritesZXBSeleccion);
        
        opcionExportarSpritesArduino2x2 = new JMenuItem("Export sprites (Arduino format 2x2)", KeyEvent.VK_Y);
        opcionExportarSpritesArduino2x2.addActionListener(this);
        menuFichero.add(opcionExportarSpritesArduino2x2);

        menuFichero.addSeparator();

        opcionExportarTAP = new JMenuItem("Export to TAP", KeyEvent.VK_T);
        opcionExportarTAP.addActionListener(this);
        menuFichero.add(opcionExportarTAP);

        menuFichero.addSeparator();
        
        opcionSalir = new JMenuItem("Exit", KeyEvent.VK_E);
        opcionSalir.addActionListener(this);
        menuFichero.add(opcionSalir);

        JMenu menuEdicion = new JMenu("Edit");
        menuEdicion.setMnemonic(KeyEvent.VK_E);
        menuBar.add(menuEdicion);
        
        opcionUndo = new JMenuItem("Undo", KeyEvent.VK_U);
        opcionUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        opcionUndo.addActionListener(this);
        menuEdicion.add(opcionUndo);

        opcionRedo = new JMenuItem("Redo", KeyEvent.VK_R);
        opcionRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        opcionRedo.addActionListener(this);
        menuEdicion.add(opcionRedo);

        menuEdicion.addSeparator();

        opcionBorrarBitmap = new JMenuItem("Clear bitmap", KeyEvent.VK_B);
        opcionBorrarBitmap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        opcionBorrarBitmap.addActionListener(this);
        menuEdicion.add(opcionBorrarBitmap);

        opcionBorrarColores = new JMenuItem("Clear colours", KeyEvent.VK_S);
        opcionBorrarColores.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        opcionBorrarColores.addActionListener(this);
        menuEdicion.add(opcionBorrarColores);
        
        opcionInvertirBitmap = new JMenuItem("Invert bitmap", KeyEvent.VK_I);
        opcionInvertirBitmap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        opcionInvertirBitmap.addActionListener(this);
        menuEdicion.add(opcionInvertirBitmap);

        menuEdicion.addSeparator();
        
        JMenu subMenuModo = new JMenu("Screen mode");
        subMenuModo.setMnemonic(KeyEvent.VK_M);
        
        radioModoHiRes = new JRadioButtonMenuItem("Hi-res color mode");
        radioModoHiRes.setMnemonic(KeyEvent.VK_H);
        radioModoHiRes.setSelected(true);
        radioModoHiRes.addActionListener(this);

        radioModoNormal = new JRadioButtonMenuItem("Normal SCR color mode");
        radioModoNormal.setMnemonic(KeyEvent.VK_H);
        radioModoNormal.addActionListener(this);

        ButtonGroup grupoModo = new ButtonGroup();
        grupoModo.add(radioModoHiRes);
        grupoModo.add(radioModoNormal);

        subMenuModo.add(radioModoHiRes);
        subMenuModo.add(radioModoNormal);
        
        menuEdicion.add(subMenuModo);

        JMenu menuSeleccion = new JMenu("Select");
        menuSeleccion.setMnemonic(KeyEvent.VK_S);
        menuBar.add(menuSeleccion);

        opcionSeleccionarNada = new JMenuItem("None", KeyEvent.VK_N);
        opcionSeleccionarNada.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        opcionSeleccionarNada.addActionListener(this);
        menuSeleccion.add(opcionSeleccionarNada);
        
        opcionSeleccionarLapiz = new JMenuItem("Pen select", KeyEvent.VK_P);
        opcionSeleccionarLapiz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        opcionSeleccionarLapiz.addActionListener(this);
        menuSeleccion.add(opcionSeleccionarLapiz);
        
        opcionSeleccionarFree = new JMenuItem("Free select", KeyEvent.VK_F);
        opcionSeleccionarFree.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        opcionSeleccionarFree.addActionListener(this);
        menuSeleccion.add(opcionSeleccionarFree);
        
        opcionSeleccionarRectangulo = new JMenuItem("Rectangle select", KeyEvent.VK_R);
        opcionSeleccionarRectangulo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        opcionSeleccionarRectangulo.addActionListener(this);
        menuSeleccion.add(opcionSeleccionarRectangulo);

        opcionSeleccionarBloques = new JMenuItem("Block select", KeyEvent.VK_K);
        opcionSeleccionarBloques.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
        opcionSeleccionarBloques.addActionListener(this);
        menuSeleccion.add(opcionSeleccionarBloques);
        
        JMenu menuTools = new JMenu("Tools");
        menuTools.setMnemonic(KeyEvent.VK_T);
        menuBar.add(menuTools);

        opcionLapiz = new JMenuItem("Pen", KeyEvent.VK_P);
        opcionLapiz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        opcionLapiz.addActionListener(this);
        menuTools.add(opcionLapiz);

        opcionRectangulo = new JMenuItem("Rectangle", KeyEvent.VK_R);
        opcionRectangulo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        opcionRectangulo.addActionListener(this);
        menuTools.add(opcionRectangulo);

        opcionRelleno = new JMenuItem("Flood fill", KeyEvent.VK_F);
        opcionRelleno.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        opcionRelleno.addActionListener(this);
        menuTools.add(opcionRelleno);

        opcionPintarAtributo = new JMenuItem("Paint only attributes", KeyEvent.VK_A);
        opcionPintarAtributo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        opcionPintarAtributo.addActionListener(this);
        menuTools.add(opcionPintarAtributo);

        opcionLinea = new JMenuItem("Line", KeyEvent.VK_L);
        opcionLinea.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        opcionLinea.addActionListener(this);
        menuTools.add(opcionLinea);

        opcionCopiar = new JMenuItem("Copy", KeyEvent.VK_C);
        opcionCopiar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        opcionCopiar.addActionListener(this);
        menuTools.add(opcionCopiar);

        JMenu menuView = new JMenu("View");
        menuView.setMnemonic(KeyEvent.VK_V);
        menuBar.add(menuView);

        opcionZoomIn = new JMenuItem("Zoom in", KeyEvent.VK_I);
        opcionZoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        opcionZoomIn.addActionListener(this);
        menuView.add(opcionZoomIn);

        opcionZoomOut = new JMenuItem("Zoom out", KeyEvent.VK_O);
        opcionZoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        opcionZoomOut.addActionListener(this);
        menuView.add(opcionZoomOut);

        opcionZoom1 = new JMenuItem("Zoom 1:1", KeyEvent.VK_1);
        opcionZoom1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
        opcionZoom1.addActionListener(this);
        menuView.add(opcionZoom1);

        opcionGrid = new JMenuItem("Show/hide grid", KeyEvent.VK_G);
        opcionGrid.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        opcionGrid.addActionListener(this);
        menuView.add(opcionGrid);

        JMenu menuHelp = new JMenu("Help");
        menuHelp.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menuHelp);

        opcionAbout = new JMenuItem("About Colorator...", KeyEvent.VK_A);
        opcionAbout.addActionListener(this);
        menuHelp.add(opcionAbout);

        return menuBar;
    }

    public void mostrarFrameAbout() {

    	JFrame frameAbout = new JFrame();
    	frameAbout.setTitle( "About Colorator" );

    	String ruta = "../README.md";
    	ArrayList<String> texto = leerFicheroTexto( ruta );
    	StringBuffer sb = new StringBuffer();
    	for ( String s: texto ) {
    		sb.append( s );
    		sb.append( '\n' );
    	}

    	JTextArea textArea = new JTextArea( sb.toString(), 100, 70 );
    	textArea.setEditable( false );
    	JScrollPane contentPane = new JScrollPane( textArea );
    	contentPane.setPreferredSize( new Dimension( 800, 600  ) );
    	frameAbout.setContentPane( contentPane );
    	frameAbout.pack();
    	frameAbout.setVisible( true );
    }
    
    public static ArrayList<String> leerFicheroTexto( String path ) {

        // Si el fichero no existe o hay otro error, se devuelve null

    	ArrayList<String> lineas = new ArrayList<String>();

        BufferedReader input = null;
        try {
            input = new BufferedReader( new FileReader( path ) );
        }
        catch ( FileNotFoundException e ) {
            return null;
        }

        String linea = null;
        try {
            while ( ( linea = input.readLine() ) != null ) {
            	lineas.add( linea );
            }
        }
        catch ( IOException e ) {
        	return null;
        }

        try {
            input.close();
        }
        catch ( IOException e ) {
            // Nada que hacer
        }
        
        return lineas;
    }


    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == opcionAbrir) {
            boolean confirm = colorator.confirmUnsavedChangesDiscard();
            if ( ! confirm ) {
                return;
            }
            int result = colorator.panelToolbar.fileChooser.showOpenDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.cargar( colorator.panelToolbar.fileChooser.getSelectedFile() );
            }
        }
        else if (e.getSource() == opcionGuardar) {
            int result = colorator.panelToolbar.fileChooser.showSaveDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.salvar( colorator.panelToolbar.fileChooser.getSelectedFile() );
            }
        }
        if (e.getSource() == opcionAbrirScr) {
            boolean confirm = colorator.confirmUnsavedChangesDiscard();
            if ( ! confirm ) {
                return;
            }
            int result = colorator.panelToolbar.fileChooser.showOpenDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.cargarScr( colorator.panelToolbar.fileChooser.getSelectedFile() );
            }
        }
        else if (e.getSource() == opcionGuardarScr) {
            int result = colorator.panelToolbar.fileChooser.showSaveDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.salvarSCR( colorator.panelToolbar.fileChooser.getSelectedFile() );
            }
        }
        else if (e.getSource() == opcionGuardarAtributosAlta) {
            int result = colorator.panelToolbar.fileChooser.showSaveDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.salvarAtributosAlta( colorator.panelToolbar.fileChooser.getSelectedFile() );
            }
        }
        else if (e.getSource() == opcionAbrirAtributosAlta) {
            int result = colorator.panelToolbar.fileChooser.showOpenDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.cargarAtributosAlta( colorator.panelToolbar.fileChooser.getSelectedFile() );
            }
        }
        else if (e.getSource() == opcionImportarSoloHI) {
            int result = colorator.panelToolbar.fileChooser.showOpenDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.importarImagenSoloHI( colorator.panelToolbar.fileChooser.getSelectedFile() );
            }
        }
        else if (e.getSource() == opcionImportarEntera) {
            int result = colorator.panelToolbar.fileChooser.showOpenDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.importarImagenEntera( colorator.panelToolbar.fileChooser.getSelectedFile() );
            }
        }
        else if (e.getSource() == opcionExportarImagen) {
        	int result = colorator.panelToolbar.fileChooser.showSaveDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.exportarImagenEntera( colorator.panelToolbar.fileChooser.getSelectedFile() );
            }
        }
        else if (e.getSource() == opcionExportarTAP) {
        	int result = colorator.panelToolbar.fileChooser.showSaveDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.exportarFicheroTAP( colorator.panelToolbar.fileChooser.getSelectedFile() );
            }
        }
        else if (e.getSource() == opcionExportarSpritesZXB1x1) {
        	int result = colorator.panelToolbar.fileChooser.showSaveDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.exportarSprites1x1( colorator.panelToolbar.fileChooser.getSelectedFile(), Colorator.SPRITES_ZXB );
            }
        }
        else if (e.getSource() == opcionExportarSpritesZXB2x2) {
        	int result = colorator.panelToolbar.fileChooser.showSaveDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.exportarSprites2x2( colorator.panelToolbar.fileChooser.getSelectedFile(), Colorator.SPRITES_ZXB );
            }
        }
        else if (e.getSource() == opcionExportarSpritesZXBSeleccion) {
        	int result = colorator.panelToolbar.fileChooser.showSaveDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.exportarSpritesSeleccion( colorator.panelToolbar.fileChooser.getSelectedFile(), Colorator.SPRITES_ZXB );
            }
        }
        else if (e.getSource() == opcionExportarSpritesArduino2x2) {
        	int result = colorator.panelToolbar.fileChooser.showSaveDialog(colorator.panelToolbar);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.exportarSprites2x2( colorator.panelToolbar.fileChooser.getSelectedFile(), Colorator.SPRITES_ARDUINO );
            }
        }

        else if (e.getSource() == opcionSalir) {
            if ( colorator.pantallaEditada ) {
                boolean exit = colorator.confirmUnsavedChangesDiscard();
                if ( exit ) {
                    System.exit(0);
                }
            }
            else {
                System.exit(0);
            }
        }
        
        else if (e.getSource() == opcionUndo) {
            colorator.makeUndo();
        }
        else if (e.getSource() == opcionRedo) {
            colorator.makeRedo();
        }
        else if (e.getSource() == opcionBorrarBitmap ) {
        	colorator.eraseBitmap();
        }
        else if (e.getSource() == opcionBorrarColores ) {
        	colorator.eraseColours();
        }
        else if (e.getSource() == opcionInvertirBitmap ) {
        	colorator.invertBitmap();
        }
        
        else if (e.getSource() == radioModoHiRes) {
            colorator.changeScreenMode(true);
        }
        else if (e.getSource() == radioModoNormal) {
            colorator.changeScreenMode(false);
        }

        else if (e.getSource() == opcionSeleccionarNada) {
            colorator.seleccionarNada();
        }
        else if (e.getSource() == opcionSeleccionarLapiz) {
            colorator.seleccionarHerramienta( colorator.herramientaSeleccionLapiz );
        }
        else if (e.getSource() == opcionSeleccionarFree) {
            colorator.seleccionarHerramienta( colorator.herramientaSeleccionLapizFree );
        }
        else if (e.getSource() == opcionSeleccionarRectangulo) {
            colorator.seleccionarHerramienta( colorator.herramientaSeleccionRectangulo );
        }
        else if (e.getSource() == opcionSeleccionarBloques) {
            colorator.seleccionarHerramienta( colorator.herramientaSeleccionBloques );
        }

        else if (e.getSource() == opcionLapiz) {
        	colorator.seleccionarHerramienta( colorator.lapiz );
        }
        else if (e.getSource() == opcionRectangulo) {
            colorator.seleccionarHerramienta( colorator.herramientaRectangulo );
        }
        else if (e.getSource() == opcionRelleno) {
            colorator.seleccionarHerramienta( colorator.herramientaRelleno );
        }
        else if (e.getSource() == opcionPintarAtributo) {
            colorator.seleccionarHerramienta( colorator.lapizAtributo );
        }
        else if (e.getSource() == opcionLinea) {
            colorator.seleccionarHerramienta( colorator.linea );
        }
        else if (e.getSource() == opcionCopiar) {
        	colorator.seleccionarHerramienta( colorator.herramientaCopiar );
        }
        
        else if (e.getSource() == opcionZoomIn) {
            colorator.panelEditor.zoomIn();
        }
        else if (e.getSource() == opcionZoomOut) {
            colorator.panelEditor.zoomOut();
        }
        else if (e.getSource() == opcionZoom1) {
            colorator.panelEditor.setZoom(1);
        }
        else if (e.getSource() == opcionGrid) {
            colorator.gridVisible = ! colorator.gridVisible;
            colorator.panelToolbar.btnGrid.setSelected( colorator.gridVisible );
            colorator.panelEditor.repaint();
        }
        else if (e.getSource() == opcionAbout) {
        	mostrarFrameAbout();
        }
    }
}
