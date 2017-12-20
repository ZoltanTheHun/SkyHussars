# SkyHussars

SkyHussars is an open source flight simulator project built on top of the jMonkeyEngine. The game is licensed under the Modified BSD license.
The purpose of this project to learn more about planes and game programming, development is done for fun. 

The game is written in Java, the regular releases will provide executable for Linux and Windows, later executables will be available for BSDs and MacOS. 
The project can be easily built with gradle. There are 2 commands available to achive this:
```
gradle assemble - this will create the common distribution, with runscripts for Linux and Windows
gradle launch4j -Ptargetexe=SkyHussars -  this will build the mane game and create a Windows executable
gradle launch4j -Ptargetexe=PlaneEd -  this will build the plane editor and create a Windows executable
gradle launch4j -Ptargetexe=TerrainEd -  this will build the terrain editor and create a Windows executable
```
Game assets are stored in the assets folder. All of them are licensed under some open source license or created by _ZoltanTheHun_, with the license indicated in the appropriate folder.

Download the latest release [here](https://github.com/ZoltanTheHun/SkyHussars/releases/tag/R11)

