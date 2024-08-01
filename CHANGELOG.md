craftedcore 5.1
================

- fix crash on Fabric 1.21
- add new graphics tools

craftedcore 5
================

- **rework multi-version structure**
- fix crash on NeoForge 1.20.4 with Server

craftedcore 4.3.1
================

- fix mixins on forge not working properly anymore
- upgrade to Minecraft 1.21

craftedcore 4.3
================

- fix error when sending update message
- improve filter mechanism for modrinth version checking

craftedcore 4.2.4
================

- update to NeoForge 20.6.111 for 1.20.6
- integrate Mixin Trace mod for more detailed crash reports

craftedcore 4.2.3
================

- fix keys not always registered on Forge
- fix breathing speed on 1.18.2
- fix crash on Forge Server when receiving Packet

craftedcore 4.2.2
================

- fix Entity Interactions failed most times
- fix key mappings not always registered on Forge/NeoForge

craftedcore 4.2
================

- remove HTTPClient 5 background library
- improve Render Overlay Events for MinecraftForge & Fabric
- fix SynchronizedReloadRegistry for Fabric
- remove Architectury API dependency
- add Living Breathe Event
- add Data Pack Sync Event
- background improvements

craftedcore 4.1
================

- fix crash on dedicated server
- fix networking issues on 1.20.6
- fix NeoForge not detecting mod
- add Command-Registration-Event
- add Player Sleep Events

craftedcore 4.0
================

- add SynchronizedJsonReloadListener
- implement new custom networking
- create custom Event system
- add VersionChecker for Modrinth API

craftedcore 3.2.4
================

- fix "Invalid Player Data" on first world join with multiple mods
- fix patreons can't be synced with web
- cached patreons are stored locally for offline functionality

craftedcore 3.2.3
================

- fix crash on neoforge & dev versions

craftedcore 3.2.2
================

- merge to apache http client 5

craftedcore 3.2.1
================

- version checker for maven meta data xml files is now updated & improved
- small improvements to the player data saving system
- better logging while config handling
- add local patreons list

craftedcore 3.2
================

- add new version checker option to check for new tags & releases on GitHub
- Patreons are now on a github.io-Page

craftedcore 3.1.2
================

- fix crash on Legacy Forge

craftedcore 3.1
================
This is just a small crash patch to prevent infinite loading times while joining the game.

craftedcore 3.0
================
Patreons are now cached in an external thread to save performance. Furthermore, the metadata automatically updates now,
so the dependency versions are correct (Minecraft, Modloader, Architectury API). Moreover, the version checker doesn't
show you if there's a new version available, if this new version is below your version. Last but not least, there are
massive improvements to the config, including a crash fix. This improvements include options for modder to re-sync the
config to clients, if changes applied and to save it at a specific time.