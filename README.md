# SkyHussars

##### 2016.02.27
Spring integration and logging put in place.

_- LtPete_

##### 2016.02.26
Project structure updates. So far lined out the gradle build, a couple of dependencies
and done the dist assembly part.

To build the project, run `gradle assemble`, this will
put the distribution package into the _build/distributions_ folder. To run the application, you have two choices:
  - execute `gradle run`
  - unzip the distribution and run `Launch SkyHussars.bat`

_- LtPete_

##### 2016.01.19
First readme...
So, the gradle build is almost up and running. However the assets are separately distributed as of now. 

To build the project please execute:
`gradle build copyRuntimeLibs`

This will put all resources into the /build/libs/lib folder,
except assets.jar, which is separately distributed. Please add
that manually from a release until to the /build/libs/lib folder.
The assets most probably will load separately in the future.

_- ZoltanTheHun_