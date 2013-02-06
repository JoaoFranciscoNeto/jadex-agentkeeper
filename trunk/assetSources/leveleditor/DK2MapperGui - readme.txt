


DK2MapperGui




I. Introduction
DK2MapperGUI  is a graphical front end to assist you in editing/creating DungeonKeeper II maps in concert with DK2mapper, the map compiler. Dk2mapper is  a console program that takes an ASCII representation of the map and turns it into the binary files Dungeon Keeper II requires to run the scenario.

DK2mapperGui and Dk2Mapper were not sponsored by Bullfrog, makers of Dungeon Keeper II, and Bullfrog will not support problems you encounter either with these programs or with the game when you run your homemade maps.

Also, be aware that this program involves HACKING and it could very well get you in a state where your map or the game will not work correctly. I do not accept any responsibility for resulting damage to your system from usage of these programs. USE AT YOUR OWN RISK!

An understanding of  how dk2mapper works is assumed in this document. Please take the time to read the readme file accompanied with DK2Mapper so that you understand what DK2MapperGui is trying to accomplish. 

This GUI is not an attempt to compete with other GUI efforts. I have been asked by many people to produce this GUI, so I felt it necessary to do so. There are other GUIs by  people who have more interface experience, and I am sure they offer different features.

I would like to thank Anthony Affrunti (Wydraz) for his making the graphics, and Simpleton Sam for testing. Also, David Amor at Electronic Arts for his support,  as well as Bullfrog for Dungeon Keeper II.

I. Installation

Place “Dk2MapperGui.exe” in the directory under your path from dungeon keeper:

(dungeon keeper program)\Data\editor\maps

(for example if it is C:\Program Files\ DungeonKeeper2, place it in 
C:\Program Files\ DungeonKeeper2/Data/editor/maps)

In order for the test menu and make thumbnail menus to work correctly, it must be put there.

Also, make sure that dk2mapper.exe (the compiler) is placed in this directory.

If you do not have the compiler, it is available at Richard Wilson’s Dark Sword fan site. Look for it at http://www.dkeeper2.net/ 



II. Functionality

Dk2Mapper functions via a set of dialogs and mouse actions. Through these dialogs you will be able to use all the features of DK2Mapper. 

Be aware that because map making in ASCII for was such a difficult task, that many old maps that ‘appeared’ to be working may not load properly with this editor, and some cleaning of the map may be required before it can be loaded properly.

MOUSE:

The mouse can be used to select a square or to paint I a square. When in SELECT MODE, both the left and right button of the mouse will select a square. When in PAINT MODE, the right button will still select, but the left button will paint the current terrain. Beware, there is no undo function. 

When either the fill Area or Areas dialogs are open, a left click mouse drag will help define an area.  Dragging the mouse out of the window while doing this can force you to have to start dragging over again.


	FILE MENU:


New Map- Will allow you to create a new map. You will have the opportunity to specify the Map Name, dimensions, and fill value.
Load Map – Will open an exiting map file.
Save Map – Will save your map file under the current file name.
Save As – Will allow you to save your map under a name you specify.
Compile – Will use dk2mapper to compile the currently saved map (does not save before compilation).
Test – Will use Dkii (the game) with the –level switch to allow you to examine your map in the game. (does not save or compile)
Make Thumbnail – This will make a thumbnail picture and place it in the thumbnails directory. 
Exit – leave Dk2mapperGui. Does not ask you to save, does not save, and will not ask you if you are sure. So Save often!
	

	EDIT MENU:

Select Mode – Puts you in Select Mode for selecting squares
Deselect – Simply deselects the current selected square (puts you in SELECT MODE)
Place Terrain – Calls up the Terrain Dialog, and puts you in PAINT MODE.
Fill Area – Calls up the Fill Area Dialog.
Place Creatures – Calls up the Creatures Dialog for the selected square
Place Trap/Door – Calls up the Trap/Door Dialog for the selected square
Place Thing – Calls up the Thing Dialog for the selected square
Hero Bands – Calls up the Hero Band Dialog
Areas – Calls up the Areas Dialog
Gates – Calls up the Gates Dialog
Triggers – Calls up the Triggers Dialog

	VIEW MENU:

Toolbar- Displays the toolbar
Status Bar – Displays the status bar at the bottom
Center Sel Sq – Centers the Display around the selected square. The ‘c’ or “C” buttons will perform this function as well.
Scale – choose the display scale for viewing
View Traps/Doors – show squares that contain a Trap or a Door. Will be signified by the letter “T”
View Creatures – shows squares that contain creatures. Square will contain the letter “C”.
View Things – shows squares with things. Square will contain the letter “h”
View Gates – shows square with a specified gate. Square will contain the letter “G”.
View Areas – shows the beginning and ending squares of an area. For Area one, it would show A1s (for supper-left) A1e (for lower-right)

Please note that if you have several view options on and you run at a lower scale, the information as to locations may appear incorrect (to the right of where it is supposed to be).. I advise you only use the view options one/two at a time. Also note that areas that have a common square for the start or end will write the text over each other in the view option.  (ie, if Ae1 and A2s are in the same square, all you will see is A2s).


	TOOLBAR:


The toolbar only has shortcut buttons  to the menu items.  There is no new functionality to be described.



	Terrain Dialog:

The terrain dialog is tabbed with the 7 sets of terrain. One set for other graphics, such as MAP_BORDER and GEMS, and six sets for the room terrain for Neutral, Hero, and Players 1 through Player 4.
Selecting a button will cause that terrain to be used while in PAINT MODE. The dialog can be minimized or closed.
Hearts will now be properly sized and their centers recorded internally.
MAP_BORDER MUST run along the edge of the map, but it can also be used as SOLID_ROCK is anywhere else in the map.


	Fill Area Dialog:
This dialog will allow you to specify an area, then fill it with the Current Terrain. The area is described by two points, the position of the points will not matter. You can also use the mouse to define an area by left clicking and dragging the mouse across the display area. When you release the mouse, the coordinates of the area will appear in the dialog.

	Creatures Dialog:

This dialog will allow you to add creatures to the selected square. To add, just select the type, level, and owner and press add. To remove, select a creature from the list and press remove.

	Traps/Doors Dialog:

This dialog will allow you to place a trap or a door in the select square. You will not be allowed to place 2 in the same square. Simply select the type you wish to place.

	Things  Dialog:

This dialog will allow you to place a thing in the selected square. Simply select the type you wish to place.

	Hero Bands Dialog:

This dialog will allow you to describe the Hero Bands to be used for Hero Releases.
To make a new band, select the new button. To edit an existing band, select the band and press edit. To delete a band, select the band and press remove. If a band is being used in an action, you will not be allowed to delete it. In editing a band, simply specify the creatures and add or remove them from the band.

Areas Dialog:

This dialog will allow you to add/remove areas. If an area is being used in a trigger or action, you will not be allowed to remove it. To add an area, select the upper left square of the area and  click on the Start At: Get Selected Square button, or just enter the coordinates in the edit boxes. Do the same for the lower right square with the ends at button/boxes. Then select Add to add the area. Or select an area in the list and press change to set that area to those coordinates, or click on delete after selecting an area from the list to remove the area. You can also use the mouse to define an area by left clicking and dragging the mouse across the display area. When you release the mouse, the coordinates of the area will appear in the dialog.


Gates Dialog:

This dialog will allow you to add/remove gates. If a gate is being used in an Action, you will not be allowed to remove it. Simply select the square you wish to specify as a gate,  and click on the “Get Selected Square” button, or enter the coordinates in the edit boxes. Then select Add to add the gate. Or select a gate from the list and press change to set that gate to those coordinates, or click on delete after selecting an gate from the list to remove the gate.


Triggers Dialog:

This dialog allows you to add/remove triggers. To create a new trigger, select new. To edit an existing trigger, select it from the list and press edit. To remove a trigger, select it from the list and press remove.

In the trigger editor, select the type of trigger you wish to use. Then specify the seconds/slaps/ or area associated with the trigger.  Then, select the type of action you wish to add, and follow that actions edit dialog to add it. A trigger with no actions will not be stored.

Keeper Info  Dialog:

This dialog allows you to specify the starting conditions for each player. There are two extra tabs listed, one for Heroes and one for the Neutral player, but at this time  the data included in those sections is not used. Possibly in future releases of DK2Mapper it will be.

For each keeper, you can enter the starting Gold amount, the creature counts for all portal creatures, and the starting requirements for rooms, spells, doors and traps. The settings are initialized to the basic defaults seen in most scenarios in the game. See the Dk2mapper readme for more details.

Note that you can place spaces in the scenario name, but they will be replaced with an underscore at saving time. Also, punctuation will cause it to crash.

III. That’s It

Remember to read the readme of dk2mapper if you have not. Since there are several Gui’s coming up, support might become difficult and confusing on the Dark Sword forum. I can be contacted at mok@mars.superlink.net with questions,  problems, compliments, and cash. Do not ask me questions about the other GUIs, I will not know the answer.
Also, this program was done gratis, which means it may not function properly under certain conditions. Care was taken to properly test it, but I cannot guarantee everything will function to spec. If you discover a problem and you can repeat it, email me and I will see what I can do. Also, look for updates to this program at Dark Sword (http://www.dkeeper2.net/ ) and visit their editing forum to see other people’s comments.

I would also advise that if you have put some time and effort into a map, take the five seconds it takes to back it up. This program could chew it up at a moments notice.

This program may be distributed as long as there is no charge associated with that distribution and the readme file is included.

Bugs:
If you switch to a lower scale and the scroll bars disappear, do not panic. Just select any square an center on that square (press “C”). This can happen if you are on the right side of the map and switch to x20 or x10. The code just is not there to recalculate the position.

--Mark
9-10-99
mok@mars.superlink.net




6/1/00
Version 1.2
Fixed Triggered Spell Errors