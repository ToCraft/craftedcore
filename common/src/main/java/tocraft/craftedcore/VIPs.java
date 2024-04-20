package tocraft.craftedcore;

import com.mojang.util.UUIDTypeAdapter;
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
    public static final String patreonURL = "https://raw.githubusercontent.com/ToCraft/craftedcore/1.20.2/common/src/main/resources/patreons.txt";
    private static final List<UUID> CACHED_PATREONS = new ArrayList<>();

    @SuppressWarnings("unused")
    public static List<UUID> getCachedPatreons() {
        return CACHED_PATREONS;
    }

    public static List<UUID> cachePatreons() {
        if (CACHED_PATREONS.isEmpty()) {
            CACHED_PATREONS.addAll(getPatreons());
        }

        if (CraftedCoreConfig.INSTANCE != null && CraftedCoreConfig.INSTANCE.savePatreonsLocal) {
            if (CACHED_PATREONS.isEmpty()) {
                CACHED_PATREONS.addAll(CraftedCoreConfig.INSTANCE.localPatreonsList);
            } else {
                CraftedCoreConfig.INSTANCE.localPatreonsList = CACHED_PATREONS;
                CraftedCoreConfig.INSTANCE.save();
            }
        }
        return CACHED_PATREONS;
    }

    public static List<UUID> getPatreons() {
        List<UUID> patreons = new ArrayList<>();
        try {
            URL localPatreons = CraftedCore.class.getResource("/patreons.txt");
            if (localPatreons != null) {
                for (UUID uuidOfPerson : getUUIDOfPeople(localPatreons)) {
                    if (!patreons.contains(uuidOfPerson)) {
                        patreons.add(uuidOfPerson);
                    }
                }
            }
            for (UUID uuidOfPerson : getUUIDOfPeople(new URL(patreonURL))) {
                if (!patreons.contains(uuidOfPerson)) {
                    patreons.add(uuidOfPerson);
                }
            }
        } catch (MalformedURLException e) {
            CraftedCore.LOGGER.error("Invalid patreon url: " + patreonURL, e);
        }
        return patreons;
    }

    public static List<UUID> getUUIDOfPeople(URL url) {
        String line;
        List<UUID> people = new ArrayList<>();
        try {
            BufferedReader updateReader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            while ((line = updateReader.readLine()) != null && !(line = line.replaceAll("\n", "").replaceAll("\r", "")).isBlank()) {
                people.add(UUID.fromString(line));
            }
            updateReader.close();
        } catch (IOException e) {
            CraftedCore.LOGGER.error("Couldn't get patreons from " + patreonURL, e);
        }
        if (Platform.getEnvironment() == Env.CLIENT && people.contains(UUIDTypeAdapter.fromString(Minecraft.getInstance().getUser().getUuid()))) {
            CraftedCore.LOGGER.info("Thank you for supporting me and my mods! ~To_Craft");
        }

        return people;
    }
}
