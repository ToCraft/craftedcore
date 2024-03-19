package tocraft.craftedcore;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VIPs {
    public static final String patreonURL = "https://tocraft.github.io/patreons.txt";
    private static final List<UUID> CACHED_PATREONS = new ArrayList<>();

    public static List<UUID> getCachedPatreons() {
        return CACHED_PATREONS;
    }

    public static List<UUID> cachePatreons() {
        if (CACHED_PATREONS.isEmpty()) {
            CACHED_PATREONS.addAll(getPatreons());
        }
        return CACHED_PATREONS;
    }

    public static List<UUID> getPatreons() {
        try {
            return getUUIDOfPeople(new URL(patreonURL));
        } catch (MalformedURLException e) {
            throwError(e);
            return new ArrayList<>();
        }
    }

    public static List<UUID> getUUIDOfPeople(URL url) {
        String line;
        List<UUID> people = new ArrayList<>();
        try {
            BufferedReader updateReader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            while ((line = updateReader.readLine()) != null && (line = line.replaceAll("\n", "")).isBlank()) {
                people.add(UUID.fromString(line));
            }
            updateReader.close();
        } catch (IOException e) {
            throwError(e);
        }
        if (Platform.getEnvironment() == Env.CLIENT && people.contains(Minecraft.getInstance().getUser().getProfileId())) {
            CraftedCore.LOGGER.info("Thank you for supporting me and my mods! ~To_Craft");
        }

        return people;
    }

    private static void throwError(IOException e) {
        CraftedCore.LOGGER.error("Couldn't get all patreons from " + patreonURL);
        CraftedCore.LOGGER.error(e.getLocalizedMessage());
    }
}
