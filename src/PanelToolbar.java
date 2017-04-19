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
 * Panel de herramientas izquierdo  
 */
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;

public class PanelToolbar extends JPanel implements ActionListener {

    Colorator colorator;

    JButton btnOpen;
    JButton btnSave;
    JButton btnUndo;
    JButton btnRedo;
    JButton btnZoomIn;
    JButton btnZoomOut;
    JToggleButton btnLapiz;
    JToggleButton btnRectangulo;
    JToggleButton btnRellenar;
    JToggleButton btnPintarAtributo;
    JToggleButton btnLinea;
    JToggleButton btnGrid;
    JToggleButton btnCopy;
    JToggleButton btnSelectPen;
    JToggleButton btnSelectFree;
    JToggleButton btnSelectRect;
    JToggleButton btnSelectBlock;
    
    JFileChooser fileChooser;
    
    FileNameExtensionFilter fcfilterCOLORATOR;
    FileNameExtensionFilter fcfilterCOLORATOR_ATTRS;
    FileNameExtensionFilter fcfilterSCR;
    FileNameExtensionFilter fcfilterPNG;
    FileNameExtensionFilter fcfilterPNG_JPG_GIF;
    FileNameExtensionFilter fcfilterTAP;
    FileNameExtensionFilter fcfilterTXT;

    
    public PanelToolbar(Colorator col) {
        super();
        colorator = col;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel panel0 = new JPanel();
        panel0.setLayout(new BoxLayout(panel0, BoxLayout.X_AXIS));
        
        btnOpen = new JButton(new ImageIcon("../iconos/open.png"));
        btnOpen.setToolTipText( "Open hi-res image" );
        btnOpen.addActionListener(this);        
        panel0.add(btnOpen);
        
        btnSave = new JButton(new ImageIcon("../iconos/save.png"));
        btnSave.setToolTipText( "Save hi-res image" );
        btnSave.addActionListener(this);
        panel0.add(btnSave);
        
        add(panel0);
        
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        
        btnUndo = new JButton(new ImageIcon("../iconos/undo.png"));
        btnUndo.setToolTipText( "Undo last action" );
        btnUndo.addActionListener(this);
        panel1.add(btnUndo);

        btnRedo = new JButton(new ImageIcon("../iconos/redo.png"));
        btnRedo.setToolTipText( "Redo last undone action" );
        btnRedo.addActionListener(this);
        panel1.add(btnRedo);
        
        add(panel1);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        
        btnZoomIn = new JButton(new ImageIcon("../iconos/zoomin.png"));
        btnZoomIn.setToolTipText( "Zoom in" );
        btnZoomIn.addActionListener(this);
        panel2.add(btnZoomIn);

        btnZoomOut = new JButton(new ImageIcon("../iconos/zoomout.png"));
        btnZoomOut.setToolTipText( "Zoom out" );
        btnZoomOut.addActionListener(this);
        panel2.add(btnZoomOut);

        add(panel2);
        
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        
        btnLapiz = new JToggleButton(new ImageIcon("../iconos/lapiz.png"));
        btnLapiz.setToolTipText( "Pen tool" );
        btnLapiz.addActionListener(this);
//        btnLapiz.setSelected(true);
        panel3.add(btnLapiz);

        btnRectangulo = new JToggleButton(new ImageIcon("../iconos/rectangulo.png"));
        btnRectangulo.setToolTipText( "Rectangle tool" );
        btnRectangulo.addActionListener(this);        
        panel3.add(btnRectangulo);
        
        add(panel3);

        JPanel panel4 = new JPanel();
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
        
        btnRellenar = new JToggleButton(new ImageIcon("../iconos/fill.png"));
        btnRellenar.setToolTipText( "Floodfill tool" );
        btnRellenar.addActionListener(this);
        panel4.add(btnRellenar);

        btnPintarAtributo = new JToggleButton(new ImageIcon("../iconos/lapizatributo.png"));
        btnPintarAtributo.setToolTipText( "Pen tool (only attributes)" );
        btnPintarAtributo.addActionListener(this);        
        panel4.add(btnPintarAtributo);
        
        add(panel4);

        JPanel panel5 = new JPanel();
        panel5.setLayout(new BoxLayout(panel5, BoxLayout.X_AXIS));
        
        btnLinea = new JToggleButton(new ImageIcon("../iconos/linea.png"));
        btnLinea.setToolTipText( "Line tool" );
        btnLinea.addActionListener(this);
        panel5.add(btnLinea);

        btnGrid = new JToggleButton(new ImageIcon("../iconos/grid.png"));
        btnGrid.setToolTipText( "Show/hide attributes grid" );
        btnGrid.addActionListener(this);
        panel5.add(btnGrid);

        add(panel5);

        JPanel panel6 = new JPanel();
        panel6.setLayout(new BoxLayout(panel6, BoxLayout.X_AXIS));

        btnSelectPen = new JToggleButton(new ImageIcon("../iconos/selectpen.png"));
        btnSelectPen.setToolTipText( "Select pen tool" );
        btnSelectPen.addActionListener(this);
        panel6.add( btnSelectPen );

        btnSelectFree = new JToggleButton(new ImageIcon("../iconos/selectfree.png"));
        btnSelectFree.setToolTipText( "Free select tool" );
        btnSelectFree.addActionListener(this);
        panel6.add( btnSelectFree );

        add(panel6);

        JPanel panel7 = new JPanel();
        panel7.setLayout(new BoxLayout(panel7, BoxLayout.X_AXIS));

        btnSelectRect = new JToggleButton(new ImageIcon("../iconos/selectrectangle.png"));
        btnSelectRect.setToolTipText( "Select rectangles tool" );
        btnSelectRect.addActionListener( this );
        panel7.add( btnSelectRect );

        btnSelectBlock = new JToggleButton(new ImageIcon("../iconos/selectblock.png"));
        btnSelectBlock.setToolTipText( "Select blocks tool" );
        btnSelectBlock.addActionListener(this);
        panel7.add(btnSelectBlock);

        add(panel7);
        
        JPanel panel8 = new JPanel();
        panel8.setLayout(new BoxLayout(panel8, BoxLayout.X_AXIS));

        btnCopy = new JToggleButton(new ImageIcon("../iconos/copy.png"));
        btnCopy.setToolTipText( "Copy tool" );
        btnCopy.addActionListener(this);
        panel8.add(btnCopy);

        add(panel8);

        
        
        fileChooser = new JFileChooser("../examples");
        fileChooser.setAcceptAllFileFilterUsed( true );
        
        fcfilterCOLORATOR = new FileNameExtensionFilter( "Colorator files (.colorator)", "colorator" );
        fcfilterCOLORATOR_ATTRS = new FileNameExtensionFilter( "Colorator hi-res attribute files (.colattr)", "colattr" );
        fcfilterSCR = new FileNameExtensionFilter( "SCR images", "scr" );
        fcfilterPNG = new FileNameExtensionFilter( "PNG images", "png" );
        fcfilterPNG_JPG_GIF = new FileNameExtensionFilter( "PNG, JPG, GIF images", "png", "jpg", "jpeg", "gif" );
        fcfilterTAP = new FileNameExtensionFilter( ".TAP files", "tap" );
        fcfilterTXT = new FileNameExtensionFilter( ".txt files", "txt" );
        
        fileChooser.setFileFilter( fcfilterCOLORATOR );
        fileChooser.setFileFilter( fcfilterCOLORATOR_ATTRS );
        fileChooser.setFileFilter( fcfilterSCR );
        fileChooser.setFileFilter( fcfilterPNG );
        fileChooser.setFileFilter( fcfilterPNG_JPG_GIF );
        fileChooser.setFileFilter( fcfilterTAP );
        fileChooser.setFileFilter( fcfilterTXT );

    }
    
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnOpen) {
            boolean confirm = colorator.confirmUnsavedChangesDiscard();
            if ( ! confirm ) {
                return;
            }
            fileChooser.setFileFilter( fcfilterCOLORATOR );
            int result = fileChooser.showOpenDialog(colorator.panelEditor);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.cargar( fileChooser.getSelectedFile() );
            }
        }
        else if (e.getSource() == btnSave) {
            fileChooser.setFileFilter( fcfilterCOLORATOR );
            int result = fileChooser.showSaveDialog(colorator.panelEditor);
            if (result == JFileChooser.APPROVE_OPTION) {
                colorator.salvar( fileChooser.getSelectedFile() );
            }
        }
        else if (e.getSource() == btnZoomOut) {
            colorator.panelEditor.zoomOut();
        }
        else if (e.getSource() == btnZoomIn) {            
            colorator.panelEditor.zoomIn();
        }
        else if (e.getSource() == btnUndo) {
            colorator.makeUndo();
        }
        else if (e.getSource() == btnRedo) {
            colorator.makeRedo();
        }
        else if (e.getSource() == btnLapiz) {
        	colorator.seleccionarHerramienta( colorator.lapiz );
        }
        else if (e.getSource() == btnRectangulo) {
        	colorator.seleccionarHerramienta( colorator.herramientaRectangulo );
        }
        else if (e.getSource() == btnRellenar) {
        	colorator.seleccionarHerramienta( colorator.herramientaRelleno );
        }
        else if (e.getSource() == btnPintarAtributo) {
        	colorator.seleccionarHerramienta( colorator.lapizAtributo );
        }
        else if (e.getSource() == btnLinea) {
        	colorator.seleccionarHerramienta( colorator.linea );
        }
        else if (e.getSource() == btnCopy) {
        	colorator.seleccionarHerramienta( colorator.herramientaCopiar );
        }
        else if (e.getSource() == btnSelectPen) {
        	colorator.seleccionarHerramienta( colorator.herramientaSeleccionLapiz );
        }
        else if (e.getSource() == btnSelectFree) {
        	colorator.seleccionarHerramienta( colorator.herramientaSeleccionLapizFree );
        }
        else if (e.getSource() == btnSelectRect) {
        	colorator.seleccionarHerramienta( colorator.herramientaSeleccionRectangulo );
        }
        else if (e.getSource() == btnSelectBlock) {
        	colorator.seleccionarHerramienta( colorator.herramientaSeleccionBloques );
        }
        else if (e.getSource() == btnGrid) {
            colorator.gridVisible = btnGrid.isSelected();
            colorator.panelEditor.repaint();
        }
        else {
            seleccionarBtnHerramienta( null );
        }
    }

    public void seleccionarBtnHerramienta(JToggleButton btn) {
        
        if ( btn != null ) {
            btn.setSelected(true);
        }

        if (btn != btnLapiz) {
            btnLapiz.setSelected(false);
        }
        if (btn != btnRectangulo) {
            btnRectangulo.setSelected(false);
        }
        if (btn != btnRellenar) {
            btnRellenar.setSelected(false);
        }
        if (btn != btnPintarAtributo) {
        	btnPintarAtributo.setSelected(false);
        }
        if (btn != btnLinea) {
            btnLinea.setSelected(false);
        }
        if (btn != btnCopy) {
            btnCopy.setSelected(false);
        }
        if (btn != btnSelectPen) {
        	btnSelectPen.setSelected(false);
        }
        if (btn != btnSelectFree) {
        	btnSelectFree.setSelected(false);
        }
        if (btn != btnSelectRect) {
        	btnSelectRect.setSelected(false);
        }
        if (btn != btnSelectBlock) {
        	btnSelectBlock.setSelected(false);
        }
    }
}