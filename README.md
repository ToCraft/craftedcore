[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/ToCraft/craftedcore/push_build_and_release.yml?style=for-the-badge)](https://github.com/ToCraft/craftedcore/actions/workflows/push_build_and_release.yml)
[![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Ftocraft.ddns.net%2Fmaven%2Freleases%2Fdev%2Ftocraft%2Fcraftedcore%2Fmaven-metadata.xml&versionPrefix=1.20.2&style=for-the-badge&label=CraftedCore)](https://tocraft.ddns.net/maven/releases/dev/tocraft/craftedcore)
[![Patreon](https://img.shields.io/badge/Patreon-F96854?style=for-the-badge&logo=patreon&logoColor=white)](https://patreon.com/ToCraft)
[![Discord](https://img.shields.io/discord/1183373613508857906?style=for-the-badge&label=Discord)](https://discord.gg/Y3KqxWDUYy)

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
modApi "dev.tocraft:craftedcore:1.20.2-common-1.0"
```

fabric:

```Groovy
modApi "dev.tocraft:craftedcore:1.20.2-fabric-1.0"
```

forge:

```Groovy
modApi "dev.tocraft:craftedcore:1.20.2-forge-1.0"
```

## Legal note

This mod is inspired by [Architectury API](https://github.com/architectury/architectury-api/tree/1.19.2) and therefore
uses some similar scripts.
