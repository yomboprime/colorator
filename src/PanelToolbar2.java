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
 *  Panel de herramientas derecho
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelToolbar2 extends JPanel implements ActionListener {

    Colorator colorator;

    JButton btnDesplazarIzquierda;
    JButton btnDesplazarDerecha;
    JButton btnDesplazarArriba;
    JButton btnDesplazarAbajo;
    JButton btnDesplazarArribaColor;
    JButton btnDesplazarAbajoColor;
    JButton btnFlipHorizontal;
    JButton btnFlipVertical;
    JButton btnRotarIzquierda;
    JButton btnRotarDerecha;
    
    JToggleButton btnPaperColor0;
    JToggleButton btnPaperColor1;
    JToggleButton btnPaperColor2;
    JToggleButton btnPaperColor3;
    JToggleButton btnPaperColor4;
    JToggleButton btnPaperColor5;
    JToggleButton btnPaperColor6;
    JToggleButton btnPaperColor7;
    JToggleButton btnPaperColorT;
        
    JToggleButton btnInkColor0;
    JToggleButton btnInkColor1;
    JToggleButton btnInkColor2;
    JToggleButton btnInkColor3;
    JToggleButton btnInkColor4;
    JToggleButton btnInkColor5;
    JToggleButton btnInkColor6;
    JToggleButton btnInkColor7;
    JToggleButton btnInkColorT;
    
    JToggleButton [] btnInkArray;
    JToggleButton [] btnPaperArray;
    
    JToggleButton btnBright;
    JToggleButton btnBrightT;
    
    JToggleButton btnFlash;
    JToggleButton btnFlashT;
    
    JCheckBox chkFlash;
    
    public PanelToolbar2(Colorator col) {
        super();
        colorator = col;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel panel0 = new JPanel();
        panel0.setLayout(new BoxLayout(panel0, BoxLayout.X_AXIS));
        
        btnDesplazarIzquierda = new JButton(new ImageIcon("../iconos/izquierda.png"));
        btnDesplazarIzquierda.setToolTipText( "Move selection left");
        btnDesplazarIzquierda.addActionListener(this);
        panel0.add(btnDesplazarIzquierda);

        btnDesplazarDerecha = new JButton(new ImageIcon("../iconos/derecha.png"));
        btnDesplazarDerecha.setToolTipText( "Move selection right");
        btnDesplazarDerecha.addActionListener(this);
        panel0.add(btnDesplazarDerecha);

        add(panel0);
        
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        
        btnDesplazarArriba = new JButton(new ImageIcon("../iconos/arriba.png"));
        btnDesplazarArriba.setToolTipText( "Move selection up");
        btnDesplazarArriba.addActionListener(this);
        panel1.add(btnDesplazarArriba);
        
        btnDesplazarAbajo = new JButton(new ImageIcon("../iconos/abajo.png"));
        btnDesplazarAbajo.setToolTipText( "Move selection down");
        btnDesplazarAbajo.addActionListener(this);
        panel1.add(btnDesplazarAbajo);
        
        add(panel1);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        
        btnDesplazarArribaColor = new JButton(new ImageIcon("../iconos/arribacolor.png"));
        btnDesplazarArribaColor.setToolTipText( "Move selection attributes up");
        btnDesplazarArribaColor.addActionListener(this);
        panel2.add(btnDesplazarArribaColor);

        btnDesplazarAbajoColor = new JButton(new ImageIcon("../iconos/abajocolor.png"));
        btnDesplazarAbajoColor.setToolTipText( "Move selection attributes down");
        btnDesplazarAbajoColor.addActionListener(this);
        panel2.add(btnDesplazarAbajoColor);

        add(panel2);

        JPanel panel2b = new JPanel();
        panel2b.setLayout(new BoxLayout(panel2b, BoxLayout.X_AXIS));
        
        btnFlipHorizontal = new JButton(new ImageIcon("../iconos/fliphoriz.png"));
        btnFlipHorizontal.setToolTipText( "Flip selection horizontally");
        btnFlipHorizontal.addActionListener(this);
        panel2b.add(btnFlipHorizontal);

        btnFlipVertical = new JButton(new ImageIcon("../iconos/flipvertical.png"));
        btnFlipVertical.setToolTipText( "Flip selection vertically");
        btnFlipVertical.addActionListener(this);
        panel2b.add(btnFlipVertical);

        add(panel2b);

        JPanel panel2c = new JPanel();
        panel2c.setLayout(new BoxLayout(panel2c, BoxLayout.X_AXIS));
        
        btnRotarIzquierda = new JButton(new ImageIcon("../iconos/rotarizda.png"));
        btnRotarIzquierda.setToolTipText( "Rotate selection CCW");
        btnRotarIzquierda.addActionListener(this);
        panel2c.add(btnRotarIzquierda);

        btnRotarDerecha = new JButton(new ImageIcon("../iconos/rotardcha.png"));
        btnRotarDerecha.setToolTipText( "Rotate selection CW");
        btnRotarDerecha.addActionListener(this);
        panel2c.add(btnRotarDerecha);

        add(panel2c);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        
        JPanel panelPaper = new JPanel();
        panelPaper.setLayout(new BoxLayout(panelPaper, BoxLayout.Y_AXIS));
        
        panelPaper.add(new JLabel("Paper"));
        
        btnPaperColor0 = new JToggleButton(new ImageIcon("../iconos/color0.png"));
        btnPaperColor0.addActionListener(this);
        panelPaper.add(btnPaperColor0);
        
        btnPaperColor1 = new JToggleButton(new ImageIcon("../iconos/color1.png"));
        btnPaperColor1.addActionListener(this);
        panelPaper.add(btnPaperColor1);
        
        btnPaperColor2 = new JToggleButton(new ImageIcon("../iconos/color2.png"));
        btnPaperColor2.addActionListener(this);
        panelPaper.add(btnPaperColor2);
        
        btnPaperColor3 = new JToggleButton(new ImageIcon("../iconos/color3.png"));
        btnPaperColor3.addActionListener(this);
        panelPaper.add(btnPaperColor3);
        
        btnPaperColor4 = new JToggleButton(new ImageIcon("../iconos/color4.png"));
        btnPaperColor4.addActionListener(this);
        panelPaper.add(btnPaperColor4);
        
        btnPaperColor5 = new JToggleButton(new ImageIcon("../iconos/color5.png"));
        btnPaperColor5.addActionListener(this);
        panelPaper.add(btnPaperColor5);
        
        btnPaperColor6 = new JToggleButton(new ImageIcon("../iconos/color6.png"));
        btnPaperColor6.addActionListener(this);
        panelPaper.add(btnPaperColor6);
        
        btnPaperColor7 = new JToggleButton(new ImageIcon("../iconos/color7.png"));
        btnPaperColor7.addActionListener(this);
        btnPaperColor7.setSelected(true);
        panelPaper.add(btnPaperColor7);

        btnPaperColorT = new JToggleButton(new ImageIcon("../iconos/colorTransp.png"));
        btnPaperColorT.addActionListener(this);
        btnPaperColorT.setToolTipText( "Set Transparent Paper On/Off" );
        panelPaper.add(btnPaperColorT);

        panel3.add(panelPaper);
        
        JPanel panelInk = new JPanel();
        panelInk.setLayout(new BoxLayout(panelInk, BoxLayout.Y_AXIS));

        panelInk.add(new JLabel("Ink"));
                
        btnInkColor0 = new JToggleButton(new ImageIcon("../iconos/color0.png"));
        btnInkColor0.addActionListener(this);
        btnInkColor0.setSelected(true);
        panelInk.add(btnInkColor0);
        
        btnInkColor1 = new JToggleButton(new ImageIcon("../iconos/color1.png"));
        btnInkColor1.addActionListener(this);
        panelInk.add(btnInkColor1);
        
        btnInkColor2 = new JToggleButton(new ImageIcon("../iconos/color2.png"));
        btnInkColor2.addActionListener(this);
        panelInk.add(btnInkColor2);
        
        btnInkColor3 = new JToggleButton(new ImageIcon("../iconos/color3.png"));
        btnInkColor3.addActionListener(this);
        panelInk.add(btnInkColor3);
        
        btnInkColor4 = new JToggleButton(new ImageIcon("../iconos/color4.png"));
        btnInkColor4.addActionListener(this);
        panelInk.add(btnInkColor4);
        
        btnInkColor5 = new JToggleButton(new ImageIcon("../iconos/color5.png"));
        btnInkColor5.addActionListener(this);
        panelInk.add(btnInkColor5);
        
        btnInkColor6 = new JToggleButton(new ImageIcon("../iconos/color6.png"));
        btnInkColor6.addActionListener(this);
        panelInk.add(btnInkColor6);
        
        btnInkColor7 = new JToggleButton(new ImageIcon("../iconos/color7.png"));
        btnInkColor7.addActionListener(this);
        panelInk.add(btnInkColor7);

        btnInkColorT = new JToggleButton(new ImageIcon("../iconos/colorTransp.png"));
        btnInkColorT.addActionListener(this);
        btnInkColorT.setToolTipText( "Set Transparent Ink On/Off" );
        panelInk.add(btnInkColorT);
        
        panel3.add(panelInk);
 
        add(panel3);

        btnBright = new JToggleButton(new ImageIcon("../iconos/brillo.png"));
        btnBright.addActionListener(this);
        btnBright.setToolTipText( "Set Bright On/Off");
        panelInk.add(btnBright);
        
        btnBrightT = new JToggleButton(new ImageIcon("../iconos/brillotransp.png"));
        btnBrightT.addActionListener(this);
        btnBrightT.setToolTipText( "Set Transparent Bright On/Off");
        panelPaper.add(btnBrightT);
        
        btnFlash = new JToggleButton(new ImageIcon("../iconos/flash.png"));
        btnFlash.addActionListener(this);
        btnFlash.setToolTipText( "Set Flash On/Off");
        panelInk.add(btnFlash);
        
        btnFlashT = new JToggleButton(new ImageIcon("../iconos/flashtransp.png"));
        btnFlashT.addActionListener(this);
        btnFlashT.setToolTipText( "Set Transparent Flash On/Off");
        panelPaper.add(btnFlashT);

        JPanel panel4 = new JPanel();
        chkFlash = new JCheckBox( "Show flash", true );
        panel4.add( chkFlash );
        add( panel4 );
        
        
        btnInkArray = new JToggleButton[8];
        btnPaperArray = new JToggleButton[8];
        
        btnInkArray[0] = btnInkColor0;
        btnInkArray[1] = btnInkColor1;
        btnInkArray[2] = btnInkColor2;
        btnInkArray[3] = btnInkColor3;
        btnInkArray[4] = btnInkColor4;
        btnInkArray[5] = btnInkColor5;
        btnInkArray[6] = btnInkColor6;
        btnInkArray[7] = btnInkColor7;
        
        btnPaperArray[0] = btnPaperColor0;
        btnPaperArray[1] = btnPaperColor1;
        btnPaperArray[2] = btnPaperColor2;
        btnPaperArray[3] = btnPaperColor3;
        btnPaperArray[4] = btnPaperColor4;
        btnPaperArray[5] = btnPaperColor5;
        btnPaperArray[6] = btnPaperColor6;
        btnPaperArray[7] = btnPaperColor7;
    }
    
    public void actionPerformed(ActionEvent e) {

    	if (e.getSource() == btnDesplazarIzquierda) {
            colorator.desplazarPantallaIzquierda();
        }
        else if (e.getSource() == btnDesplazarDerecha) {
            colorator.desplazarPantallaDerecha();
        }
        else if (e.getSource() == btnDesplazarArriba) {
            colorator.desplazarPantallaArriba();
        }
        else if (e.getSource() == btnDesplazarAbajo) {
            colorator.desplazarPantallaAbajo();
        }
        else if (e.getSource() == btnDesplazarAbajoColor) {
            colorator.desplazarPantallaAbajoColor();
        }
        else if (e.getSource() == btnDesplazarArribaColor) {
            colorator.desplazarPantallaArribaColor();
        }
        else if (e.getSource() == btnFlipHorizontal) {
            colorator.aplicarFiltroFlipHorizontal();
        }
        else if (e.getSource() == btnFlipVertical) {
            colorator.aplicarFiltroFlipVertical();
        }
        else if (e.getSource() == btnRotarIzquierda) {
            colorator.aplicarFiltroRotarIzquierda();
        }
        else if (e.getSource() == btnRotarDerecha) {
            colorator.aplicarFiltroRotarDerecha();
        }
        else if (e.getSource() == btnPaperColor0) {
            seleccionarBtnPaper(btnPaperColor0);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            0, Pantalla.getInk(colorator.attrsActual));
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnPaperColor1) {
            seleccionarBtnPaper(btnPaperColor1);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            1, Pantalla.getInk(colorator.attrsActual));
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnPaperColor2) {
            seleccionarBtnPaper(btnPaperColor2);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            2, Pantalla.getInk(colorator.attrsActual));
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnPaperColor3) {
            seleccionarBtnPaper(btnPaperColor3);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            3, Pantalla.getInk(colorator.attrsActual));
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnPaperColor4) {
            seleccionarBtnPaper(btnPaperColor4);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            4, Pantalla.getInk(colorator.attrsActual));
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnPaperColor5) {
            seleccionarBtnPaper(btnPaperColor5);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            5, Pantalla.getInk(colorator.attrsActual));
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnPaperColor6) {
            seleccionarBtnPaper(btnPaperColor6);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            6, Pantalla.getInk(colorator.attrsActual));
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnPaperColor7) {
            seleccionarBtnPaper(btnPaperColor7);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            7, Pantalla.getInk(colorator.attrsActual));
            colorator.attrsActual = attrs;
        }        
        else if (e.getSource() == btnPaperColorT) {
            seleccionarBtnPaper(btnPaperColorT);
        }
        
        else if (e.getSource() == btnInkColor0) {
            seleccionarBtnInk(btnInkColor0);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            Pantalla.getPaper(colorator.attrsActual), 0);
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnInkColor1) {
            seleccionarBtnInk(btnInkColor1);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            Pantalla.getPaper(colorator.attrsActual), 1);
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnInkColor2) {
            seleccionarBtnInk(btnInkColor2);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            Pantalla.getPaper(colorator.attrsActual), 2);
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnInkColor3) {
            seleccionarBtnInk(btnInkColor3);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            Pantalla.getPaper(colorator.attrsActual), 3);
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnInkColor4) {
            seleccionarBtnInk(btnInkColor4);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            Pantalla.getPaper(colorator.attrsActual), 4);
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnInkColor5) {
            seleccionarBtnInk(btnInkColor5);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            Pantalla.getPaper(colorator.attrsActual), 5);
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnInkColor6) {
            seleccionarBtnInk(btnInkColor6);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            Pantalla.getPaper(colorator.attrsActual), 6);
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnInkColor7) {
            seleccionarBtnInk(btnInkColor7);
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            Pantalla.getPaper(colorator.attrsActual), 7);
            colorator.attrsActual = attrs;
        }
        else if (e.getSource() == btnInkColorT) {
            seleccionarBtnInk(btnInkColorT);
        }

        else if (e.getSource() == btnBright) {
            
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            Pantalla.getPaper(colorator.attrsActual),
                            Pantalla.getInk(colorator.attrsActual));
            colorator.attrsActual = attrs;
                
            seleccionarBtnBright();
        }
        
        else if (e.getSource() == btnBrightT) {
            
            colorator.modoTransparenteBright = btnBrightT.isSelected();
        }
    	
        else if (e.getSource() == btnFlash) {
            
            byte attrs = Pantalla.createAttribute( btnFlash.isSelected(), btnBright.isSelected(),
                            Pantalla.getPaper(colorator.attrsActual),
                            Pantalla.getInk(colorator.attrsActual));
            colorator.attrsActual = attrs;

        }
        
        else if (e.getSource() == btnBrightT) {
            
            colorator.modoTransparenteBright = btnBrightT.isSelected();
        }

    }

    public void seleccionarBtnBright() {
        if (btnBright.isSelected()) {
            btnInkColor1.setIcon(new ImageIcon("../iconos/color1b.png"));
            btnInkColor2.setIcon(new ImageIcon("../iconos/color2b.png"));
            btnInkColor3.setIcon(new ImageIcon("../iconos/color3b.png"));
            btnInkColor4.setIcon(new ImageIcon("../iconos/color4b.png"));
            btnInkColor5.setIcon(new ImageIcon("../iconos/color5b.png"));
            btnInkColor6.setIcon(new ImageIcon("../iconos/color6b.png"));
            btnInkColor7.setIcon(new ImageIcon("../iconos/color7b.png"));

            btnPaperColor1.setIcon(new ImageIcon("../iconos/color1b.png"));
            btnPaperColor2.setIcon(new ImageIcon("../iconos/color2b.png"));
            btnPaperColor3.setIcon(new ImageIcon("../iconos/color3b.png"));
            btnPaperColor4.setIcon(new ImageIcon("../iconos/color4b.png"));
            btnPaperColor5.setIcon(new ImageIcon("../iconos/color5b.png"));
            btnPaperColor6.setIcon(new ImageIcon("../iconos/color6b.png"));
            btnPaperColor7.setIcon(new ImageIcon("../iconos/color7b.png"));
        }
        else {
            btnInkColor1.setIcon(new ImageIcon("../iconos/color1.png"));
            btnInkColor2.setIcon(new ImageIcon("../iconos/color2.png"));
            btnInkColor3.setIcon(new ImageIcon("../iconos/color3.png"));
            btnInkColor4.setIcon(new ImageIcon("../iconos/color4.png"));
            btnInkColor5.setIcon(new ImageIcon("../iconos/color5.png"));
            btnInkColor6.setIcon(new ImageIcon("../iconos/color6.png"));
            btnInkColor7.setIcon(new ImageIcon("../iconos/color7.png"));

            btnPaperColor1.setIcon(new ImageIcon("../iconos/color1.png"));
            btnPaperColor2.setIcon(new ImageIcon("../iconos/color2.png"));
            btnPaperColor3.setIcon(new ImageIcon("../iconos/color3.png"));
            btnPaperColor4.setIcon(new ImageIcon("../iconos/color4.png"));
            btnPaperColor5.setIcon(new ImageIcon("../iconos/color5.png"));
            btnPaperColor6.setIcon(new ImageIcon("../iconos/color6.png"));
            btnPaperColor7.setIcon(new ImageIcon("../iconos/color7.png"));                
        }
    }
    
    public void seleccionarBtnPaper(JToggleButton btn) {
        
        btn.setSelected(true);
        
        if (btn != btnPaperColor0) {
            btnPaperColor0.setSelected(false);
        }
        if (btn != btnPaperColor1) {
            btnPaperColor1.setSelected(false);
        }
        if (btn != btnPaperColor2) {
            btnPaperColor2.setSelected(false);
        }
        if (btn != btnPaperColor3) {
            btnPaperColor3.setSelected(false);
        }
        if (btn != btnPaperColor4) {
            btnPaperColor4.setSelected(false);
        }
        if (btn != btnPaperColor5) {
            btnPaperColor5.setSelected(false);
        }
        if (btn != btnPaperColor6) {
            btnPaperColor6.setSelected(false);
        }
        if (btn != btnPaperColor7) {
            btnPaperColor7.setSelected(false);
        }
        colorator.modoTransparentePaper = true;
        if (btn != btnPaperColorT) {
            btnPaperColorT.setSelected(false);
            colorator.modoTransparentePaper = false;
        }
    }
    
    public void seleccionarBtnInk(JToggleButton btn) {
        
        btn.setSelected(true);
        
        if (btn != btnInkColor0) {
            btnInkColor0.setSelected(false);
        }
        if (btn != btnInkColor1) {
            btnInkColor1.setSelected(false);
        }
        if (btn != btnInkColor2) {
            btnInkColor2.setSelected(false);
        }
        if (btn != btnInkColor3) {
            btnInkColor3.setSelected(false);
        }
        if (btn != btnInkColor4) {
            btnInkColor4.setSelected(false);
        }
        if (btn != btnInkColor5) {
            btnInkColor5.setSelected(false);
        }
        if (btn != btnInkColor6) {
            btnInkColor6.setSelected(false);
        }
        if (btn != btnInkColor7) {
            btnInkColor7.setSelected(false);
        }
        colorator.modoTransparenteInk = true;

        if (btn != btnInkColorT) {
            btnInkColorT.setSelected(false);
            colorator.modoTransparenteInk = false;
        }
    }
}