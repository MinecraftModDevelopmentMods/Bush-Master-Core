# Bush Master Core
[![](http://cf.way2muchnoise.eu/bush-master-core.svg)](https://minecraft.curseforge.com/projects/bush-master-core)
[![](http://cf.way2muchnoise.eu/versions/bush-master-core.svg)](https://minecraft.curseforge.com/projects/bush-master-core)
[![](https://img.shields.io/badge/Discord-MMD%20Cat%20Mods-blue.svg)](https://discord.gg/xDw3Vkj)

Master of all minecraft bushes!

#### Currently integrates with:
- Biomes o' Plenty
- Natura
- Plants 2
- Rustic
- Oreberries
- Ore Shrubs
- Extra Utilities 2

#### If one wants to use this lib in one's mod
One must first add the maven repository to the list:
```gradle
repositories {
    maven { name='MMD'; url='https://maven.mcmoddev.com' }
}
``` 
And then one must add the dependency:
```gradle
dependencies {
    compile("net.ndrei:bush-master-core:${project.bmc_version}+:deobf")
}
```
And I would also recommend having this in your `gradle.properties`:
```gradle
bmc_version = 1.0.0
```
for easier update later on.