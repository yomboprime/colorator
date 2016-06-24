 
Colorator release 116

 Copyright (C) 2010  Juan Jose Luna Espinosa

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, version 3 of the License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.


 
- ABOUT -

Colorator is a graphical editor for editing Sinclair ZX Spectrum images
(normal and also hi-color resolution for the assembler routine by McLeod)


- SUPPORTED PLATFORMS -

GNU/Linux, Windows and Mac. Java required (1.5 at least)


- DEVELOPMENT -

Included project files for Eclipse Java development.


- HOW TO RUN -

Unzip the .zip file and then:

Windows - Just run the Colorator.bat file by clicking twice on it.

Linux, Mac - Execute the Colorator.sh shell script.

if run manually by command line (the Colorator java class) it accepts some parameters:

	-gui The gui is split in the main window and two toolbars.

	-f file File to load (.colorator)


- USAGE -

The editor can run in two modes (select the mode through Edit > Screen mode menu):

  - Hi color mode (default): The central part of the screen (112x192)
has 8x1 attributes, that is, a row of eight pixels instead of a block
of 8x8 pixels will have the same attributes (ink, paper, bright and flash)
Th rest of the screen has the 8x8 attributes as always.

  - Normal mode: Just the well known Spectrum "SCR" mode, with attributes per 8x8 pixel block.

To the left there is the tool panel, you can select a tool, and a tooltip, and help text
in the bottom of the screen will appear.
The tools are Pen, Rectangle, Flood fill, line, attribute pen, and copy. The selection tools
are select pen, free select, rectangle select and block select. Other icons are load/save,
undo/redo, and show/hide attributes grid.

To the right there is the momentary tools and attribute selection icons. You can choose the
ink, paper, bright and flash you want, or transparent mode for each of those. There are the
shift tools (bitmap/attribute), the flip tools (horizontal/vertical), and rotation tools.
Also there is a toggle button to select if you want flash actually shown in the editor.

You can undo/redo with Ctrl-X and Ctrl-Y

You can zoom in or out with the mouse wheel, and you can pan by pressing and dragging the center
mouse button.

Remember that all tools and operations affect only the selected area, or all the screen if
there is nothing selected. You can clear the selection with Ctrl-N (Select None)

Other options you can find in the menu are Clear Bitmap, Clear Colours, Select None, or
Zoom 1:1


- FILE INPUT / OUTPUT -

These are the options for file input/output:

- Open/Save: You can load/save in the native format for the asm routine that shows the hi-color resolution
images in the Spectrum. These files have .colorator extension and stores first the bitmap
(6144 bytes), then the attributes for the left side of the screen (216 bytes), then the attributes
for the right side of the screen (216 bytes), then the hi color resolution info (2688 bytes)

- Open/Save normal SCR image: These options are for loading/saving a normal SCR image (first bitmap of 6144 bytes,
then attribute block of 768 bytes)

- Open/Save onli hi-resolution color attributes: These options are for loading/saving only the high color
attribute block (2688 bytes)

- Import image: There are two options also for importing a PC image (.png, .jpg and the like): only the
hi-color attributes, or fullscreen (in the last one the results depends on the editor mode: normal SCR or hi-color)

- Export sprites: And finally there are options for exporting graphics information as code (text). The graphics
are a 1x1 or 2x2 blocks of 8x8 pixels read from left to right and downwards. The end of graphics is 
specified with a block with black ink and black paper.

- Export image: There is an option to export the image to .png format.

- Export to TAP: This options exports a Spectrum-readable TAP file with a BASIC loader to load the image data on screen.
If the program is in hi-res mode the loader includes the asm routine for displaying the image.

Note: When exporting to TAP, the current editor attributes are used and stored in the loader. When the loader
executes, it clears the screen with that paper, ink, bright and flash. The border is set to the paper colour.

After loading the image, the loader waits for a key press, then clears again the screen with specified attributes
and then does load"". So the exported tap could be concatenated directly with another TAP with other screen loader,
or your own program. There is a "slideshow" TAP demostrating this in the "images" folder.


- Development Log -
-------------------
1.15 -
- The exported TAP loader now clears the screen with current editor attributes.
- Fixed readme and version number.

1.1 -
- Added TAP export with BASIC loader (SCR) or BASIC loader plus assembler routine for displaying Hi colour images.
- Now the attribute pen inverts ink and paper by clicking with right mouse button.
- Added application icon.

1.0 First release

