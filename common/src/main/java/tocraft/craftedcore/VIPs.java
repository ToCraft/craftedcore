package tocraft.craftedcore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VIPs {
    public static final String patreonURL = "https://tocraft.dev/patreons.txt";
    private static final List<UUID> CACHED_PATREONS = new ArrayList<UUID>();

    public static List<UUID> getCachedPatreons() {
        if (CACHED_PATREONS.isEmpty())
            CACHED_PATREONS.addAll(getPatreons());
        return CACHED_PATREONS;
    }

    public static List<UUID> getPatreons() {
        try {
            return getUUIDOfPeople(new URI(patreonURL).toURL());
        } catch (Exception e) {
            CraftedCore.LOGGER.error("Couldn't get patreons from " + patreonURL);
            CraftedCore.LOGGER.error(e.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public static List<UUID> getUUIDOfPeople(URL url) throws IOException {
        String line;
        BufferedReader updateReader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        List<UUID> people = new ArrayList<>();
        while ((line = updateReader.readLine()) != null) {
            line = line.replaceAll("/n", "").replaceAll(" ", "");
            if (!line.isBlank()) {
                people.add(UUID.fromString(line));
            }
        }
        updateReader.close();
        return people;
    }
}