# Contributing to CraftedCore

CraftedCore is ToCraft's shared library mod. It provides the common infrastructure that allows ToCraft's mods (including [Woodwalkers](https://github.com/ToCraft/woodwalkers-mod)) to run on both Fabric and NeoForge without duplicating platform-specific code. It is inspired by Architectury API and integrates Mixin Trace by comp500 (MIT).

## Table of Contents

- [Role in the Ecosystem](#role-in-the-ecosystem)
- [Ways to Contribute](#ways-to-contribute)
- [Reporting Bugs](#reporting-bugs)
- [Development Setup](#development-setup)
- [Project Structure](#project-structure)
- [Making Changes](#making-changes)
- [Updating to a New Minecraft Version](#updating-to-a-new-minecraft-version)
- [CI / GitHub Actions](#ci--github-actions)
- [Code Style](#code-style)
- [License](#license)

---

## Role in the Ecosystem

CraftedCore occupies the middle tier of the ToCraft dependency chain:

```
ModMaster  →  CraftedCore  → most mods by ToCraft
```

It is compiled using [ModMaster](https://github.com/ToCraft/ModMaster) and published to the ToCraft Maven (`https://maven.tocraft.dev/public`). Downstream mods consume it via Maven artifact — they do not depend on the source directly. This means **a new CraftedCore release must be published before downstream mods can pick up changes**.

---

## Ways to Contribute

- **Bug reports** – Open an [issue](https://github.com/ToCraft/craftedcore/issues).
- **Bug fixes & new API** – Fork the repo, make your changes, and open a pull request against `dev`.
- **Financial support** – [Patreon](https://www.patreon.com/tocraft).

---

## Reporting Bugs

When filing a bug, please include:

- The CraftedCore version (e.g. `8.0`) and Minecraft version.
- Whether you are on Fabric or NeoForge, and the loader version.
- A clear description of what you expected and what happened instead.
- Any relevant log output or crash report.

---

## Development Setup

### Prerequisites

| Tool | Minimum version |
|------|----------------|
| JDK  | 25             |
| Git  | any recent     |

IntelliJ IDEA is recommended. The project uses the [ModMaster](https://github.com/ToCraft/ModMaster) Gradle plugin, so most of the build configuration is handled automatically.

### Cloning and building

```bash
git clone https://github.com/ToCraft/craftedcore.git
cd craftedcore

# Build both Fabric and NeoForge jars, including the test mods
./gradlew build
```

### Running the test mod

CraftedCore ships test mods for both loaders under `testmod-fabric` and `testmod-neoforge`. To run them:

```bash
# Fabric client
./gradlew :testmod-fabric:runClient

# NeoForge client
./gradlew :testmod-neoforge:runClient
```

Sometimes, the run tasks do not run or run but the mod files do not work in a production environment. Therefore, compile the mod with `./gradlew build` and test the `fabric` and `testmod-fabric` artifacts in a production environment. If they work properly, do the same for the `neoforge` and `testmod-neoforge` artifacts. Then, you can open a Pull Request.

---

## Project Structure

```
craftedcore/
├── common/              # Platform-agnostic library code
├── fabric/              # Fabric-specific entrypoints and implementations
├── neoforge/            # NeoForge-specific entrypoints and implementations
├── testmod-common/      # Shared test mod code
├── testmod-fabric/      # Fabric test mod
├── testmod-neoforge/    # NeoForge test mod
├── assets/              # Repository artwork
└── gradle.properties    # Build configuration (MC version, mod version, etc.)
```

Shared logic lives in `common`, platform implementations live in `fabric` and `neoforge`, and all wiring is handled by the ModMaster plugin.

### Key gradle values

```properties
# ModMaster plugin version in build.gradle.kts
id("dev.tocraft.modmaster.root") version ("2.X")

# In gradle.properties
fabric_loader=<new version>
neoform_version=<new version>
mixinextras_version=<new version>

minecraft=<new version>
supported_versions=<new version>
java=<new version, but "oldest" that is still supported by Minecraft>
fabric=<new fabric api version>
neoforge=<new version>
```

---

## Making Changes

1. **Fork** the repository and create a feature branch off `main`.
2. Implement your change in the correct module:
    - Cross-platform API or logic → `common`
    - Fabric-specific code → `fabric`
    - NeoForge-specific code → `neoforge`
3. If you are adding new API surface, add corresponding test coverage in `testmod-common`.
4. Build and run the test mod on Fabric **and** NeoForge to check the change works in-game.
5. Open a **pull request** against `dev`.

Keep pull requests focused — one logical change per PR makes review much easier.

### Adding new API

CraftedCore is a library. Any public API you add becomes a contract that downstream mods depend on. Please:

- Document new public types and methods with Javadoc and update the [wiki](https://github.com/ToCraft/craftedcore/wiki).
- Avoid removing or changing existing public API without a deprecation period — downstream mods will break.
- If your change requires a corresponding update in Woodwalkers or another mod, mention it in the PR description.

---

## Updating to a New Minecraft Version

CraftedCore depends on [ModMaster](https://github.com/ToCraft/ModMaster) and must be updated in the correct order within the broader ecosystem:

```
1. ModMaster  →  2. CraftedCore  →  3. other mods
```

**Do not attempt to update CraftedCore until a compatible ModMaster version has been released.**

Once ModMaster is ready, the steps for CraftedCore are:

1. Update the ModMaster plugin version in `build.gradle.kts`:
   ```kotlin
   id("dev.tocraft.modmaster.root") version ("2.X")
   ```
2. Update `gradle.properties` with the new MC-related values:
   ```properties
    fabric_loader=<new version>
    neoform_version=<new version>
    mixinextras_version=<new version>

    minecraft=<new version>
    supported_versions=<new version>
    java=<new version, but "oldest" that is still supported by Minecraft>
    fabric=<new fabric api version>
    neoforge=<new version>
   ```
3. Fix any compilation errors caused by Mojang API changes.
4. Verify the test mods run on both loaders: `./gradlew :testmod-fabric:runClient` and `./gradlew :testmod-neoforge:runClient`. Sometimes, the run tasks do not run or run but the mod files do not work in a production environment. Therefore, compile the mod with `./gradlew build` and test the `fabric` and `testmod-fabric` artifacts in a production environment. If they work properly, do the same for the `neoforge` and `testmod-neoforge` artifacts. Then, you can open a Pull Request.
5. Open a pull request. Once merged and released, downstream mods can update their `craftedcore_version`.

---

## CI / GitHub Actions

CraftedCore uses two reusable composite actions from the ToCraft organisation.

### `modmaster-build-action` ([repo](https://github.com/ToCraft/modmaster-build-action))

Runs on every push and pull request. Sets up the JDK (Temurin), configures Gradle, executes `./gradlew check build`, and uploads the compiled jars as a named artifact. This is what you will see pass or fail on your PR.

### `modmaster-release-action` ([repo](https://github.com/ToCraft/modmaster-release-action))

Runs when a release is created. Builds the project, then publishes to CurseForge, Modrinth, and the ToCraft Maven (`maven.tocraft.dev/public`) in a single Gradle invocation (`./gradlew check build release`). It also sends a Discord webhook notification and creates a GitHub Release when `artifact_type=release`. Contributors do not need the required secrets (`MAVEN_PASS`, `CURSEFORGE_TOKEN`, `MODRINTH_TOKEN`, `DISCORD_WEBHOOK`) — these are only available in the maintainer's CI environment.

For CI failures on your PR, check the [Actions tab](https://github.com/ToCraft/craftedcore/actions) for the build logs.

---

## Code Style

- The codebase is written in **Java**.
- Follow the conventions present in the files you are editing (indentation, naming, import ordering).
- Avoid large unrelated refactors inside a feature PR — keep the diff readable and reviewable.

---

## License

CraftedCore is licensed under the **GNU Lesser General Public License v3.0 (LGPL-3.0)**. Parts not licensed under LGPL-3.0 are marked individually in the source. By submitting a pull request you agree that your contribution will be made available under LGPL-3.0.