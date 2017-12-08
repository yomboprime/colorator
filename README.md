Colorator v1.16
===============

[Versión en español](https://github.com/yomboprime/colorator/blob/master/README-es.md)

Copyright (C) 2010 [Juan José Luna Espinosa](https://yombo.org/) (juanjoluna@gmail.com)

Índice
------

1. [About Colorator](#about-colorator)
2. [Why this editor](#why-this-editor)
3. [Supported platforms](#supported-platforms)
4. [Features](#features)
5. [Download](#download)
6. [Examples](#examples)
7. [How to run](#how-to-run)
8. [Usage](#usage)
9. [Development](#development)
10. [Development Log](#development-log)

About Colorator
---------------

Colorator is a graphical editor for Sinclair ZX Spectrum images, regular ones and also hi-color resolution for the assembler routine by [McLeod Ideafix](https://twitter.com/zxprojects).

Why this editor
---------------

The Spectrum has a limitation in the number of colors available in a given "character" (a 8x8 pixel cell). The phenomenon is known as [attribute clash](https://en.wikipedia.org/wiki/Attribute_clash). This program is an editor for the images stored in the format for the assembler routine created by [McLeod Ideafix](https://twitter.com/zxprojects), which breaks this limit by altering the graphics memory zone right when the image is being sent to TV.

The idea was born on [this thread](http://www.speccy.org/foro/viewtopic.php?f=6&t=1194) (in Spanish) of the www.speccy.org forums. [McLeod Ideafix](https://twitter.com/zxprojects) coded a routine which enables you to have a central screen zone in which you can draw with a resolution of 2 colors in each 8x1 pixels, instead of the original 8x8. The zone is 112x192 pixels wide. The computer must increase a lot the consumed resources and the amount of memory reserved for graphics gets multiplied by four, but the results are impressive. It's also inpired on some articles of the famous Spanish magazine MicroHobby, (issues [143](http://www.microhobby.org/numero142.htm) (page 18) and [183](http://www.microhobby.org/numero183.htm) (page 42). My brother told me to start making a graphical editor for that video mode, though the editor lets you also edit standard Spectrum images.

On [Va de Retro](http://www.va-de-retro.com/foros/viewtopic.php?f=62&t=1998) forum there was also a conversation about how to create an image.

Supported platforms
-------------------

Java (at least 1.5), that is, GNU/Linux, Windows, macOS and others.

Features
--------

- Handle low and hi-color attribute formats
- Import a PNG format image
- Quite advanced editing tools
- Export the image to TAP file format with its BASIC loader

Download
--------

[Download Colorator](https://github.com/yomboprime/colorator)

Released under LGPL v3 free software license

Examples
--------

![La Mulana MSX](https://github.com/yomboprime/colorator/raw/master/examples/colorator-pantallazo-la-mulana.png)

Kel:

![Doppel](https://github.com/yomboprime/colorator/raw/master/examples/doppel2.png)

[Jon Cortázar](https://twitter.com/jon_cortazar):

![Escena feliz Colorator](https://github.com/yomboprime/colorator/raw/master/examples/escena-feliz-jon-cortazar.png)

![Escena feliz Original](https://github.com/yomboprime/colorator/raw/master/examples/escena-feliz-jon-cortazar-msx.png)

[@airsynth](http://airsynth.es/):

![Demo Colorator](https://github.com/yomboprime/colorator/raw/master/examples/demo-colorator.png)

![Turbo Esprit Colorator](https://github.com/yomboprime/colorator/raw/master/examples/turboesprit-colorator.png)

![Turbo Esprit Original](https://github.com/yomboprime/colorator/raw/master/examples/turboesprit-original.png)

Conversion from MSX:

![La Mulana Colorator](https://github.com/yomboprime/colorator/raw/master/examples/la-mulana.png)

![La Mulana MSX](https://github.com/yomboprime/colorator/raw/master/examples/la-mulana-msx.png)


How to run
----------

Unzip the .zip file and double click the appropiate script:

- Windows: Colorator.bat
- GNU/Linux and Mac OS X: Colorator.sh

If run manually by command line (the Colorator java class) it accepts some parameters:

- `-gui`: The GUI is split in the main window and two toolbars.
- `-f file`: File to load (.colorator)

Usage
-----

### Modes

The editor can run in two different modes (select the mode through Edit > Screen mode menu):

- Hi color mode (default): The central part of the screen (112x192) has 8x1 pixel attributes, that is, a row of eight pixels instead of a block of 8x8 pixels will have the same attributes (ink, paper, bright and flash). The rest of the screen has the 8x8 attributes as usual.
- Normal mode: Just the well known Spectrum "SCR" mode, with attributes of 8x8 pixel block.

### Left panel

To the left there is the tool panel. When  you select a tool a tooltip and help text at the bottom of the screen will appear.

The tools are Pen, Rectangle, Flood fill, Line, Attribute pen, and Copy. The selection tools are select Pen, Free select, Rectangle select and Block select. Other icons are Load/Save, Undo/Redo, and Show/Hide attributes grid.

### Right panel

To the right there's the momentary tools and attribute selection icons. You can choose the ink, paper, bright and flash you want, or transparent mode for each of those. There are the shift tools (bitmap/attribute), the flip tools (horizontal/vertical), and rotation tools. There is also a toggle button to select if you want flash to be animated on the editor.

### Other options

You can undo/redo with Ctrl-Z and Ctrl-Y.

Zoom in or out with the mouse wheel, and you can pan by pressing and dragging the center mouse button.

Remember that all tools and operations affect only the selected area, or all the screen if there is nothing selected. You can clear the selection with Ctrl-N (Select None)

Other options you can find in the menu are Clear Bitmap, Clear Colours, Select None, or Zoom 1:1

### File input/output

- Open/Save: You can load/save in the native format for the assembler routine that shows the hi-color resolution images in the Spectrum. These files have .colorator extension and store first the bitmap (6144 bytes), then the attributes for the left side of the screen (216 bytes), then the attributes for the right side of the screen (216 bytes), and finally hi-color resolution info (2688 bytes)
- Open/Save normal SCR image: These options are for loading/saving a standard SCR image (first bitmap of 6144 bytes, then attribute block of 768 bytes)
- Open/Save only hi-resolution color attributes: Only the high color attribute block (2688 bytes)
- Import image: There are also two options to import a PC image (.png, .jpg and the like):
	- Only hi-color attributes
	- Full screen: Results depend on the active editor mode: normal SCR or hi-color
- Export sprites: Exporting graphics information as code (text). The graphics are a 1x1 or 2x2 blocks of 8x8 pixels read from left to right and downwards. The end of graphics is specified with a block with black ink and black paper.
- Export image: There is an option to export the image to .png format.
- Export to TAP: This option exports to a TAP file to be read by a Spectrum, with a BASIC loader to load the image data on screen. If the program is in hi-res mode the loader includes the assembler routine for displaying the image.

Note: When exporting to TAP, the current editor attributes are used and stored in the loader. When the loader executes, it clears the screen with that paper, ink, bright and flash. The border is set to the paper colour.

After loading the image, the loader waits for a key press, then clears again the screen with specified attributes and then does a LOAD "", so the exported TAP file can be concatenated directly with another TAP with other screen loader, or your own program. For example, in bash:

`cat screen1.tap screen2.tap screen3.tap > slideshow.tap`

And in Windows:

`copy screen1.tap + screen2.tap + screen3.tap slideshow.tap`

There is a "slideshow" TAP demostrating this in the "images" folder.

Development
-----------

Eclipse Java development project files included.

Development Log
---------------

1.16 (2017-04-19)

- Better zoom handling.
- Added La Mulana example.
- Some documentation improvements.

1.15

- The exported TAP loader now clears the screen with current editor attributes.
- Fixed readme and version number.

1.1

- Added TAP export with BASIC loader (SCR) or BASIC loader plus assembler routine for displaying Hi colour images.
- Now the attribute pen inverts ink and paper by clicking with right mouse button.
- Added application icon.

1.0

- First release


License
-------

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, version 3 of the License.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
