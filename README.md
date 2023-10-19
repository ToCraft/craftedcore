[![Build](https://github.com/ToCraft/craftedcore/actions/workflows/gradle-1.20.1.yml/badge.svg)](https://github.com/ToCraft/craftedcore/actions/workflows/gradle-1.20.1.yml)
![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Ftocraft.ddns.net%2Fmaven%2Freleases%2Fdev%2Ftocraft%2Fcraftedcore%2Fmaven-metadata.xml&versionPrefix=1.20.1-common&label=CraftedCore)
![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Ftocraft.ddns.net%2Fmaven%2Freleases%2Fdev%2Ftocraft%2Fcraftedcore%2Fmaven-metadata.xml&versionPrefix=1.20.1-forge&label=CraftedCore)
![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Ftocraft.ddns.net%2Fmaven%2Freleases%2Fdev%2Ftocraft%2Fcraftedcore%2Fmaven-metadata.xml&versionPrefix=1.20.1-fabric&label=CraftedCore)

# CraftedCore

This mod is my basic libary mod, so my mods would work across modloader and it won't take me that long to port my mods.

## How to use?

Use the following maven:
```Groovy
maven {
    name "tocraftMavenReleases"
    url "https://tocraft.ddns.net/maven/releases"
}
```

And the following mod-dependency:

```Groovy
modApi "dev.tocraft:craftedcore:${minecraft_version}-project.name-${craftedcore_version}"
```

#### e.g. for fabric and forge projects, which use a common-project:
common: 
```Groovy
modApi "dev.tocraft:craftedcore:1.20.1-common-1.0"
```

fabric:
```Groovy
modApi "dev.tocraft:craftedcore:1.20.1-fabric-1.0"
```

forge:
```Groovy
modApi "dev.tocraft:craftedcore:1.20.1-forge-1.0"
```


## Legal note

This mod is inspired by [Architectury API](https://github.com/architectury/architectury-api/tree/1.19.2) and therefore uses some similar scripts.
