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
 *  Panel editor de la pantalla de Spectrum
 */
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class PanelEditor extends JComponent implements MouseMotionListener, MouseListener, MouseWheelListener {

	private static final long serialVersionUID = 1L;

	Colorator colorator;

    static int MAX_ZOOM = 32;
    
    // Current zoom factor
    int zoom;
    
    BufferedImage imagen;

    // Indica que estamos en fase de flash de cambiar papel por tinta
    boolean flashTocaCambiar;
    
    Color gridColor = new Color(128,128,128);

    Rectangle rect;

    // Cosas de pintar la seleccion
    BufferedImage imagenTileSeleccion1;
    BufferedImage imagenTileSeleccion2;
    TexturePaint texPaint1;
    TexturePaint texPaint2;
    TexturePaint texPaintActual;
    

    public PanelEditor(Colorator col) {
        super();
        colorator = col;
        
        rect = new Rectangle();
        
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addMouseWheelListener( this );
        
        setZoom( 3 );
        
        imagen = new BufferedImage(Pantalla.BITMAP_X, Pantalla.BITMAP_Y, BufferedImage.TYPE_INT_ARGB);

        // Imagen de 2x2 con trama de seleccion
        imagenTileSeleccion1 = new BufferedImage( 2, 2, BufferedImage.TYPE_INT_ARGB);
   		imagenTileSeleccion1.setRGB( 0, 0, Color.gray.getRGB() );
   		imagenTileSeleccion1.setRGB( 0, 1, Color.gray.getRGB() );
   		imagenTileSeleccion1.setRGB( 1, 0, Color.orange.getRGB() );
   		imagenTileSeleccion1.setRGB( 1, 1, Color.orange.getRGB() );
   		
   		// Otra, para alternar con el flash
        imagenTileSeleccion2 = new BufferedImage( 2, 2, BufferedImage.TYPE_INT_ARGB);
   		imagenTileSeleccion2.setRGB( 0, 0, Color.gray.getRGB() );
   		imagenTileSeleccion2.setRGB( 0, 1, Color.orange.getRGB() );
   		imagenTileSeleccion2.setRGB( 1, 0, Color.gray.getRGB() );
   		imagenTileSeleccion2.setRGB( 1, 1, Color.orange.getRGB() );

   		texPaint1 = new TexturePaint( imagenTileSeleccion1, new Rectangle( 2, 2 ) );
   		texPaint2 = new TexturePaint( imagenTileSeleccion2, new Rectangle( 2, 2 ) );
   		
   		texPaintActual = texPaint1;
    }
    
    public void cambiarTileSeleccion() {
    	if ( texPaintActual == texPaint1 ) {
    		texPaintActual = texPaint2;
    	}
    	else {
    		texPaintActual = texPaint1;
    	}
    }
    
    public boolean setZoom(int zoom) {
        if ( zoom < 1 ) {
            zoom = 1;
        }

        if ( zoom > MAX_ZOOM ) {
            zoom = MAX_ZOOM;
        }

        if ( zoom == this.zoom ) {
            return false;
        }
        
        this.zoom = zoom;
        
        Dimension d = new Dimension(Pantalla.BITMAP_X * zoom,
                                    Pantalla.BITMAP_Y * zoom);
        setPreferredSize(d);
        setMaximumSize(d);
        revalidate();
        
        repaint();
        
        return true;
    }
    
    public void zoomIn() {
        setZoom( zoom + 1 );
    }

    public void zoomOut() {
        setZoom( zoom - 1 );
    }

    public void zoomIn( int x, int y ) {

        if ( setZoom( zoom + 1 ) ) {

            centrarVista( x, y, 1 );
            
        }

    }

    public void zoomOut( int x, int y ) {

        if ( setZoom( zoom - 1 ) ) {

            centrarVista( x, y, - 1 );
            
        }

    }

    public void mostrarMensaje(String mensaje) {
    	colorator.setBarraTexto( "" );
        try {
            JOptionPane.showMessageDialog(this, mensaje);
        }
        catch (Exception e) {}
    }

    @Override
    public void mousePressed(MouseEvent e) {

    	if ( colorator.herramientaActual == null ) {
    		return;
    	}

    	Pantalla p = colorator.pantalla.clonar();

        if ( colorator.herramientaActual.empezar( e.getX() / zoom,
                                           		  e.getY() / zoom,
                                           		  e.getButton(),
                                           		  e.getModifiersEx() ) ) {
        	colorator.newActionUndo( p );
        }
                                             
        repaint();
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {

    	if ( colorator.herramientaActual == null ) {
    		return;
    	}

        colorator.herramientaActual.terminar( e.getX() / zoom,
                                              e.getY() / zoom );

        colorator.finishActionUndo();

        repaint();        
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    	if ( colorator.herramientaActual == null ) {
    		return;
    	}

        colorator.herramientaActual.movido( e.getX() / zoom,
                                            e.getY() / zoom );
        repaint();
    }

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		// La rueda del raton sube o baja el zoom
		
		if ( e.getWheelRotation() < 0 ) {
			zoomIn( e.getX(), e.getY() );
		}
		else {
			zoomOut( e.getX(), e.getY() );
		}
		
	}

	@Override
    public void mouseMoved(MouseEvent e) {
        // nothing to do
    }
    
	@Override
    public void mouseClicked(MouseEvent e) {
        // nothing to do
    }

	@Override
    public void mouseEntered(MouseEvent e) {
        // nothing to do
    }
    
	@Override
    public void mouseExited(MouseEvent e) {
        // nothing to do
    }
    
	@Override
    public void update(Graphics g) {

    	Graphics2D g2 = (Graphics2D)g;
    	
        synchronized(this) {

        	int tamXConZoom = Pantalla.BITMAP_X * zoom;
        	int tamYConZoom = Pantalla.BITMAP_Y * zoom;

        	// Pinta la pantalla de edicion en la imagen con la seleccion transparente
            colorator.pantalla.pintaEnImagen( imagen, true, flashTocaCambiar && colorator.panelToolbar2.chkFlash.isSelected() );

        	// Este rectangulo tiene la trama de la seleccion. Luego, al pintar la imagen en el Graphics,, la seleccion es
        	// transparente, por lo que se vera esta trama.
        	g2.setPaint( texPaintActual );
        	g2.fillRect( 0, 0, tamXConZoom, tamYConZoom );

            // Pinta la imagen en el Graphics
            g.drawImage( imagen, 0, 0, tamXConZoom, tamYConZoom, null );


            // Grid
            if ( colorator.gridVisible ) {
        
                g.setColor( gridColor );
                
                if ( colorator.hiResScreenMode ) {
                
                    int y = PantallaHi.ATTRS_Y * 8 * zoom;
                    for ( int i = 1; i < PantallaHi.ATTRS_X + PantallaHi.ATTRS_HI_X + PantallaHi.ATTRS_X; i++ ) {
                        int x = i * 8 * zoom;
                        g.drawLine( x, 0, x, y);
                    }  
                    
                    int x = PantallaHi.ATTRS_X * 8 * zoom;
                    for ( int j = 1; j < PantallaHi.ATTRS_Y; j++ ) {
                        y = j * 8 * zoom;
                        g.drawLine( 0, y, x, y);
                    }
                    
                    x = PantallaHi.ATTRS_X * 8 * zoom;
                    int x0 = ( PantallaHi.ATTRS_X + PantallaHi.ATTRS_HI_X ) * 8 * zoom;
                    int x1 = ( PantallaHi.ATTRS_X + PantallaHi.ATTRS_HI_X + PantallaHi.ATTRS_X) * 8 * zoom;
                    for ( int j = 1; j < PantallaHi.ATTRS_Y; j++ ) {
                        y = j * 8 * zoom;
                        g.drawLine( 0, y, x, y);
                        g.drawLine( x0, y, x1, y);
                    }
                }
                else {
                    int y = PantallaSCR.ATTRS_Y * 8 * zoom;
                    for ( int i = 1; i < PantallaSCR.ATTRS_X; i++ ) {
                        int x = i * 8 * zoom;
                        g.drawLine( x, 0, x, y);
                    }  
                    
                    int x = PantallaSCR.ATTRS_X * 8 * zoom;
                    for ( int j = 1; j < PantallaSCR.ATTRS_Y; j++ ) {
                        y = j * 8 * zoom;
                        g.drawLine( 0, y, x, y);
                    }
                }
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        update(g);
    }

    public void refrescarDesdeOtroHilo() {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
    			cambiarTileSeleccion();
    			flashTocaCambiar = ! flashTocaCambiar;
                repaint();
            }
        });
    }

    public void mueveVista( int dx, int dy ) {
		
	// Mueve la vista en coordenadas de componente
		
	Rectangle r = colorator.scrollPane.getViewport().getViewRect();
	rect.setBounds( (int)r.getX() + dx, (int)r.getY() + dy, (int)r.getWidth(), (int)r.getHeight() );
		
	colorator.panelEditor.scrollRectToVisible( rect );

    }

    private void centrarVista( int x, int y, int zoomIncrement ) {

        // Esta funcion centra la vista en x, y (del raton)
        Rectangle r = colorator.scrollPane.getViewport().getViewRect();
        
        int prevZoom = Math.min( MAX_ZOOM, Math.max( 1, ( zoom - zoomIncrement ) ) );

        double multiplier = ((double)zoom) / prevZoom;

        double dx = 0;
        double dy = 0;

        dx = ( x - r.getX() );
        dy = ( y - r.getY() );
 
        int x0 = (int)( ( r.getX() + dx ) * multiplier - dx );
        int y0 = (int)( ( r.getY() + dy ) * multiplier - dy );
 
        r.setBounds( x0, y0, (int) ( r.getWidth() ), (int) ( r.getHeight() ) );
        colorator.panelEditor.scrollRectToVisible( r );
        
        
    }

}