Colorator v1.15
===============

Copyright (C) 2010 Juan Jose Luna Espinosa juanjoluna@gmail.com

Sobre colorator
---------------

Colorator es un editor gráfico para imágenes del Sinclair ZX Spectrum, las estándar y también las de alta resolución de color (hi-color) para la rutina en ensamblador de [McLeod Ideafix](http://www.zxprojects.com/).

El porqué de este editor
------------------------

El Spectrum tiene una limitación en el número de colores que puede mostrar en cada "caracter" o celda de 8x8 pixels. El fenómeno se conoce como "mezcla de colores" o [attribute clash](https://en.wikipedia.org/wiki/Attribute_clash). Este programa es un editor para imágenes que se encuentren en el formato de la rutina que creó [McLeod Ideafix](http://www.zxprojects.com/), que rompe con este límite alterando la zona de de memoria dedicada a los gráficos justo en el momento en que están siendo enviados a la televisión.

En [este hilo](http://www.speccy.org/foro/viewtopic.php?f=6&t=1194) de los foros de www.speccy.org se puede ver como nació la idea. En resumen, [McLeod Ideafix](http://www.zxprojects.com/) programó una rutina para poder tener una franja central en la que se puede dibujar con una resolución de 2 colores por cada 8x1 pixels, en vez de los originales 8x8. La franja es de 112x192 píxels. Mi hermano me sugirió que empezase a hacer un editor de gráficos para ese modo, aunque el editor permite trabajar también con imágenes estándar de Spectrum.

The idea was born on [this thread](http://www.speccy.org/foro/viewtopic.php?f=6&t=1194) (in Spanish) of the www.speccy.org forums. [McLeod Ideafix](http://www.zxprojects.com/) coded a routine which enables you to have a central screen zone in which you can draw with a resolution of 2 colors in each 8x1 pixels, instead of the original 8x8. The zone is 112x192 pixels wide. My brother told me to start making a graphical editor for that video mode, though the editor lets you also edit standard Spectrum images.

Plataformas soportadas
---------------------

Java (al menos 1.5), o sea, GNU/Linux, Windows, Mac OS X y otros.

Características
---------------

- Manejo de los atributos en baja y alta resolución de color
- Importación una imagen PNG
- Herramientas de edición bastante avanzadas
- Exportar la imagen a formato TAP con su cargador BASIC

Descarga
--------

[Descarga Colorator](https://github.com/yomboprime/colorator)

Editado bajo la licencia de software libre LGPL v3

Ejemplos
--------

Imagen hecha por by Kel:

![Doppel](http://airsynth.es/archivos/colorator/examples/doppel.png)


@airsynth:

![Demo Colorator](http://airsynth.es/archivos/colorator/examples/demo-colorator.png)

Comparación de una imagen editada por mi hermano @airsynth usando alta resolución de color:

![Turbo Esprit Colorator](http://airsynth.es/archivos/colorator/examples/turboesprit-colorator.png)

![Turbo Esprit Original](http://airsynth.es/archivos/colorator/examples/turboesprit-original.png)


Imagen original de MSX para importar (PNG, by Jon Cortázar):

![Escena feliz Original](http://airsynth.es/archivos/colorator/examples/escena-feliz-jon-cortazar-msx.png)

Colorator:

![Escena feliz Colorator](http://airsynth.es/archivos/colorator/examples/escena-feliz-jon-cortazar.png)

Cómo ejecutarlo
---------------

Descomprime el fichero .zip y clica dos veces el script apropiado:

- Windows: Colorator.bat
- GNU/Linux y Mac OS X: Colorator.sh

Si se ejecuta por línea de comandos (la clase de Java Colorator) acepta varios parámetros:

- `-gui`: El interfaz gráfico se divide en la ventana principal y las dos barras de Herramientas
- `-f fichero`: Fichero para cargar (.colorator)

Uso
---

### Modos

El editor puede correr en dos modos diferentes (selecciona el modo a través del menú Edit > Screen mode):


- Hi-color mode (por defecto): La parte central de la pantalla (112x192) tiene atributos de 8x1 pixels, o sea, una fila de ocho pixels en vez de un bloque de 8x8 pixels tendrá los mismos atributos (ink, paper, bright y flash). El resto de la pantalla tiene los atributos de 8x8 como siempre.
- Normal mode: Solo el viejo modo "SCR" del Spectrum, con atributos en bloques de 8x8.

### Panel izquierdo

A la izquierda está el panel de herramientas. Al seleccionar una herramienta aparecen un "tooltip" y texto de ayuda en la parte inferior de la pantalla.

Las herramientas son Pen (Pincel), Rectángulo, Flood fill (Rellenar), Línea, Atributo del pincel y Copiar. Las herramientas de selección son Seleccionar con pincel, Libre selección, Selección rectangular y Bloqueo de selección. Otros iconos son Cargar / Guardar, Deshacer / Rehacer, y Mostrar / Ocultar rejilla de atributos.

### Panel derecho

A la derecha están las herramientas momentáneas e iconos de selección de atributos. Se puede elegir la tinta, papel, brillo y flash que desees, o el modo transparente para cada uno de ellos. Existen las herramientas de inversión (mapa de bits / atributo), las herramientas de vuelta (horizontal / vertical), y herramientas de rotación. También hay un botón de activación para seleccionar si desea que el flash se anime en el editor.

### Otras opciones

Tienes deshacer/rehacer disponible con Ctrl-Z and Ctrl-Y.

Zoom in o out con la rueda del ratón, y puedes hacer panning (desplazamiento de la imagen) pulsando y arrastrando con el botón central del ratón.

Recuerda que todas las herramientas y operaciones afectan sólo al área seleccionada, o la totalidad de la pantalla si no hay nada seleccionado. Puedes borrar la selección con Ctrl-N (Select None).

Other options you can find in the menu are Clear Bitmap, Clear Colours, Select None, or Zoom 1:1

Otras opciones que encontrarás en el menú son borrar el mapa de bits, borrar los colores, seleccionar nada, o Zoom 1:1.

## Entrada y salida de ficheros

- Open/Save: Te permite cargar/guardar en el formato nativo de la rutina en ensamblador que muestra las imágenes con alta resolución de color. Estos archivos tienen la extensión .colorator y almacenan primero el mapa de bits (6144 bytes), después los atributos para el lado izquierdo de la pantalla (216 bytes), después los atributos para el lado derecho de la pantalla (216 bytes) y por último el hi-color (2688 bytes)
- Abrir/Guardar normal SCR image: Estas opciones son para la carga y grabado de una imagen SCR estándar (primero el mapa de bits de 6144 bytes y después el bloque de atributos de 768 bytes)
- Open/Save only hi-resolution color attributes: Sólo el bloque de atributos hi-color (2688 bytes)
- Import image: Hay dos opciones también para importar una imagen de PC (.png, .jpg y similares):
	- Solo atributos hi-color
	- Full screen (pantalla completa): El resultado depende del modo activo de edición: normal SCR o hi-color
- Exportar sprites: Exporting graphics information as code (text). The graphics are a 1x1 or 2x2 blocks of 8x8 pixels each, read from left to right and downwards. The end of graphics file is specified with a block with black ink and black paper
Exportación de gráficos como código (texto). Los gráficos son en bloques de 1x1 o 2x2, cada uno de 8x8 píxeles, y se leen de izquierda a derecha y hacia abajo. El fin del fichero de los gráficos se especifica con un bloque tinta negra y papel negro
- Export image: Exportar la imagen a formato .png
- Export to TAP: Esta opción exporta a un archivo TAP para ser leído en un Spectrum, con un cargador BASIC para cargar los datos de la imagen en la pantalla. Si el programa se encuentra en modo de alta resolución incluye el cargador de la rutina en ensamblador para la visualización de la imagen

Nota: Cuando se exporta a TAP, los atributos de edición en uso se utilizan y almacenan en el cargador. Cuando el cargador se ejecuta, borra la pantalla con ese papel, tinta, brillo y flash. El borde se establece en el color del papel.

After loading the image, the loader waits for a key press, then clears again the screen with specified attributes and then does a LOAD "", so the exported TAP file can be concatenated directly with another TAP with other screen loader, or your own program. For example, in bash:

Después de cargar la imagen, el cargador espera la pulsación de una tecla y luego borra de nuevo la pantalla con los atributos especificados. Después hace un LOAD "", por lo que el archivo TAP exportado se puede concatenar directamente con otro TAP con otro cargador de pantalla o con tu propio programa . Por ejemplo, en bash:

`cat screen1.tap screen2.tap screen3.tap > slideshow.tap`

Y en Windows:

`copy screen1.tap + screen2.tap + screen3.tap slideshow.tap`

There is a "slideshow" TAP demostrating this in the "images" folder.

Desarrollo
-----------

Ficheros de desarrollo del proyecto en Eclipse Java incluidos.

Historial del desaarrollo
-------------------------

1.15

- El cargador del TAP exportado ahora borra la pantalla con los atributos seleccionados en el editor.
- Corregido el readme y el número de versión.

1.1

- Added TAP export with BASIC loader (SCR) or BASIC loader plus assembler routine for displaying hi-color images.
- Now the attribute pen inverts ink and paper by clicking with right mouse button.
- Added application icon.

- Añadida exportación a formato TAP con cargador BASIC (SCR) o el cargador BASIC mas la rutina en ensamblador para mostrar el hi-color
- Ahora el pincel de atributo invierte tinta y papel haciendo clic con el botón derecho del ratón.
- Añadido icono de la aplicación.

1.0

- Primera versión
