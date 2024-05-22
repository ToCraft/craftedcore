[![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/ToCraft/craftedcore/push_build_and_release.yml?style=for-the-badge)](https://github.com/ToCraft/craftedcore/actions/workflows/push_build_and_release.yml)
[![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.tocraft.dev%2Fpublic%2Fdev%2Ftocraft%2Fcraftedcore%2Fmaven-metadata.xml&versionPrefix=1.20.6&style=for-the-badge&label=CraftedCore)](https://maven.tocraft.dev/#/public/dev/tocraft/craftedcore)
[![Patreon](https://img.shields.io/badge/Patreon-F96854?style=for-the-badge&logo=patreon&logoColor=white)](https://patreon.com/ToCraft)
[![Discord](https://img.shields.io/discord/1183373613508857906?style=for-the-badge&label=Discord)](https://discord.gg/Y3KqxWDUYy)

# CraftedCore

This mod is my basic library mod, so my mods would work across mod loader, and it won't take me that long to port my
mods.

## How to use?

Use the following maven:

```Groovy
maven {
    name "tocraftMavenPublic"
    url "https://maven.tocraft.dev/public"
}
```

And the following mod-dependency:

```Groovy
modApi "dev.tocraft:craftedcore:${minecraft_version}-${craftedcore_version}"
```

#### e.g. for fabric and forge projects, which use a common-project:

common:

```Groovy
modApi "dev.tocraft:craftedcore:1.20.2-2.0"
```

fabric:

```Groovy
modApi "dev.tocraft:craftedcore-fabric:1.20.2-2.0"
```

forge:

```Groovy
modApi "dev.tocraft:craftedcore-forge:1.20.2-2.0"
```

