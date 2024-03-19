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
Patreons are now cached in an external thread to save performance. Furthermore, the metadata automatically updates now, so the dependency versions are correct (Minecraft, Modloader, Architectury API). Moreover, the version checker doesn't show you if there's a new version available, if this new version is below your version. Last but not least, there are massive improvements to the config, including a crash fix. This improvements include options for modder to re-sync the config to clients, if changes applied and to save it at a specific time.