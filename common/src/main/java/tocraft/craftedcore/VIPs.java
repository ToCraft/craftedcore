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
    private static final List<UUID> LOCAL_PATREONS = new ArrayList<>() {
        {
            add(UUID.fromString("74b6d9b3-c8c1-40db-ab82-ccc290d1aa03"));
            add(UUID.fromString("d4a50582-c44e-4a0d-ab0c-9711e2cf4b29"));
            add(UUID.fromString("ccddb138-0b29-493f-9d27-0f51ed3a0578"));
            add(UUID.fromString("1f63e38e-4059-4a4f-b7c4-0fac4a48e744"));
        }
    };

    public static List<UUID> getCachedPatreons() {
        return CACHED_PATREONS;
    }

    public static List<UUID> cachePatreons() {
        if (CACHED_PATREONS.isEmpty()) {
            CACHED_PATREONS.addAll(getPatreons());
            CACHED_PATREONS.addAll(LOCAL_PATREONS);
        }
        return CACHED_PATREONS;
    }

    public static List<UUID> getPatreons() {
        try {
            return getUUIDOfPeople(new URL(patreonURL));
        } catch (MalformedURLException e) {
            CraftedCore.LOGGER.error("Wrong patreon url: " + patreonURL, e);
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
            CraftedCore.LOGGER.error("Couldn't get patreons from " + patreonURL, e);
        }
        if (Platform.getEnvironment() == Env.CLIENT && people.contains(Minecraft.getInstance().getUser().getProfileId())) {
            CraftedCore.LOGGER.info("Thank you for supporting me and my mods! ~To_Craft");
        }

        return people;
    }
}
