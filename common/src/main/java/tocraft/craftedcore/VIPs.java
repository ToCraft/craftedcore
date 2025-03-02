package tocraft.craftedcore;

import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import tocraft.craftedcore.platform.PlatformData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VIPs {
    public static final String patreonURL = "https://raw.githubusercontent.com/ToCraft/craftedcore/main/common/src/main/resources/patreons.txt";
    private static final List<UUID> CACHED_PATREONS = new ArrayList<>();
    private static final Path CACHE_FILE = CraftedCore.CACHE_DIR.resolve("patreons.txt");

    @SuppressWarnings("unused")
    public static List<UUID> getCachedPatreons() {
        return CACHED_PATREONS;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static List<UUID> cachePatreons() {
        if (CACHED_PATREONS.isEmpty()) {
            CACHED_PATREONS.addAll(getPatreons());
        }
        StringBuilder s = new StringBuilder();
        for (UUID cachedPatreon : CACHED_PATREONS) {
            s.append(cachedPatreon).append("\n");
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            CACHE_FILE.getParent().toFile().mkdirs();
            Files.writeString(CACHE_FILE, s);
        } catch (IOException e) {
            CraftedCore.LOGGER.error("Caught an exception.", e);
        }
        return CACHED_PATREONS;
    }

    public static @NotNull List<UUID> getPatreons() {
        List<UUID> patreons = new ArrayList<>();
        try {
            // web patreons
            for (UUID uuidOfPerson : getUUIDOfPeople(new URI(patreonURL).toURL())) {
                if (!patreons.contains(uuidOfPerson)) {
                    patreons.add(uuidOfPerson);
                }
            }
            // cached patreons, only if the web patreons couldn't be loaded
            if (CACHE_FILE.toFile().exists() && patreons.isEmpty()) {
                URL cachedPatreons = CACHE_FILE.toFile().toURI().toURL();
                for (UUID uuidOfPerson : getUUIDOfPeople(cachedPatreons)) {
                    if (!patreons.contains(uuidOfPerson)) {
                        patreons.add(uuidOfPerson);
                    }
                }
            }
            // integrated patreons
            URL localPatreons = CraftedCore.class.getResource("/patreons.txt");
            if (localPatreons != null) {
                for (UUID uuidOfPerson : getUUIDOfPeople(localPatreons)) {
                    if (!patreons.contains(uuidOfPerson)) {
                        patreons.add(uuidOfPerson);
                    }
                }
            }
        } catch (MalformedURLException | URISyntaxException e) {
            CraftedCore.LOGGER.error("Invalid patreon url: " + patreonURL, e);
        }
        return patreons;
    }

    public static @NotNull List<UUID> getUUIDOfPeople(@NotNull URL url) {
        String line;
        List<UUID> people = new ArrayList<>();
        try {
            BufferedReader updateReader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            while ((line = updateReader.readLine()) != null && !(line = line.replaceAll("\n", "").replaceAll("\r", "")).isBlank()) {
                people.add(UUID.fromString(line));
            }
            updateReader.close();
        } catch (IOException e) {
            if ((e instanceof SocketException || e instanceof UnknownHostException)) {
                CraftedCore.reportMissingInternet(e);
            } else {
                CraftedCore.LOGGER.error("Couldn't get patreons from " + patreonURL, e);
            }
        }
        if (PlatformData.getEnv() == EnvType.CLIENT && people.contains(Minecraft.getInstance().getUser().getProfileId())) {
            CraftedCore.LOGGER.info("Thank you for supporting me and my mods! ~To_Craft");
        }

        return people;
    }

    public static void clearCache() {
        CraftedCore.forceDeleteFile(CACHE_FILE.toFile());
        CACHED_PATREONS.clear();
    }
}
