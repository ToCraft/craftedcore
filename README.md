[![Build](https://img.shields.io/github/actions/workflow/status/ToCraft/craftedcore/build.yml?style=for-the-badge)](https://github.com/ToCraft/craftedcore/actions/workflows/build.yml)
[![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.tocraft.dev%2Fpublic%2Fdev%2Ftocraft%2Fcraftedcore%2Fmaven-metadata.xml&style=for-the-badge&label=CraftedCore)](https://maven.tocraft.dev/#/public/dev/tocraft/craftedcore)
[![Patreon](https://img.shields.io/badge/Patreon-F96854?style=for-the-badge&logo=patreon&logoColor=white)](https://patreon.com/ToCraft)

# CraftedCore

This mod is my basic library mod, so my mods would work across mod loader, and it won't take me that long to port my
mods.

## Legal Note

This mod is massively inspired by Architectury API and integrates the Mixin Trace mod by comp500, which is licensed
under MIT.
Everything not licensed under LGPL-3.0 is marked as such.

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
modApi "dev.tocraft:craftedcore:${minecraft}-${craftedcore_version}"
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

## Building and installing locally

This fork targets **Minecraft 26.1.2** and publishes to Maven Local (`~/.m2`).

**Requirements:** JDK 21+, internet access for the first Gradle run (downloads MC assets).

```bash
# Compile and publish all three artifacts (common, fabric, neoforge) to ~/.m2
./gradlew publishToMavenLocal
```

To use the locally published artifacts in a dependent mod, add `mavenLocal()` to its repository list before any remote repos:

```kotlin
repositories {
    mavenLocal()
    // ... other repos
}
```

Then depend on the artifacts as normal:

```kotlin
// common
modApi("dev.tocraft:craftedcore:8.0")
// fabric
modApi("dev.tocraft:craftedcore-fabric:8.0")
// neoforge
modApi("dev.tocraft:craftedcore-neoforge:8.0")
```

To launch the Fabric test client:

```bash
./gradlew :fabric:runClient
```

## Fork notes (MC 26.1.2)

This is a fork of [ToCraft/craftedcore](https://github.com/ToCraft/craftedcore) updated to be compatible with **Minecraft Java Edition 26.1.2**, the first unobfuscated MC release (April 2026). The upstream library targets 1.21.x and below.

### What changed

**Minecraft API renames (global)**
- `net.minecraft.resources.ResourceLocation` → `net.minecraft.resources.Identifier`
- `net.minecraft.client.gui.GuiGraphics` → `net.minecraft.client.gui.GuiGraphicsExtractor`
- `GuiGraphics.renderItem()` → `GuiGraphicsExtractor.item()`
- `AbstractScrollArea` constructor requires a `ScrollbarSettings` argument; use `AbstractScrollArea.defaultSettings(width)`
- `MultiLineTextWidget.setColor()` removed; apply via `Component.copy().withStyle(s -> s.withColor(TextColor.fromRgb(...)))`
- `ServerLevel.getDayTime()` → `ServerLevel.getOverworldClockTime()`
- `player.hasPermissions(int)` → `player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)`
- `Player.interactOn(Entity, InteractionHand)` gained a `Vec3` parameter
- `MultiPlayerGameMode.interact(Player, Entity, InteractionHand)` gained an `EntityHitResult` parameter
- `Minecraft.disconnect(Screen, boolean)` now delegates to a new 3-param overload; the actual disconnect logic (including the `GameNarrator.clear()` call site) moved to `disconnect(Screen, boolean, boolean)`

**Fabric API renames (fabric-api 0.147.0+26.1.2)**
- `KeyBindingHelper` / `registerKeyBinding` → `KeyMappingHelper` / `registerKeyMapping` (package `keymapping.v1`)
- `ResourceLoader.registerReloader` → `registerReloadListener`
- `HudRenderCallback` removed; replaced by `HudElementRegistry.addLast(id, HudElement)` with `extractRenderState(GuiGraphicsExtractor, DeltaTracker)` method
- `ServerWorldEvents` → `ServerLevelEvents` (now matches craftedcore's own class name; use FQN to disambiguate)
- `EntitySleepEvents.ALLOW_SLEEP_TIME` removed; replaced by `ALLOW_SLEEPING` with `(Player, BlockPos) → BedSleepingProblem` callback (return `null` to allow, `OTHER_PROBLEM` to deny)
- `PayloadTypeRegistry.playC2S()` / `playS2C()` → `serverboundPlay()` / `clientboundPlay()`
- `FabricRegistryBuilder.createSimple` → `FabricRegistryBuilder.create`

**NeoForge API renames**
- `SleepFinishedTimeEvent.getNewTime()` / `setTimeAddition(long)` removed; replaced by `getAdjustment()` / `setAdjustment(ClockAdjustment)`. Use `new ClockAdjustment.Absolute(time)` — `ClockAdjustment` is not a functional interface so lambdas do not work.

**Build**
- Added `maven-publish` plugin to all subprojects for `publishToMavenLocal` support
- `processResources` `expand()` blocks: capture `project.property(...)` into local vals before the `filesMatching` block (inside the block `this` is `FileCopyDetails`, not `Project`)
- Added `compileOnly("net.fabricmc:fabric-loader:...")` to the neoforge subproject so common `@Environment(EnvType.CLIENT)` annotations compile

