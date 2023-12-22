package tocraft.craftedcore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VIPs {
	public static final String patreonURL = "https://tocraft.ddns.net/patreons.txt";
	
    public static List<UUID> getPatreons() {
    	return getUUIDOfPeople(patreonURL);
    }
    
    public static List<UUID> getUUIDOfPeople(String URL) {
    	try {
    		return getUUIDOfPeople(new URI(URL).toURL());
    	}
		catch(Exception e) {
			CraftedCore.LOGGER.error("Couldn't get people from " + URL);
			e.printStackTrace();
			return new ArrayList<UUID>();
		}
	}
    
    public static List<UUID> getUUIDOfPeople(URL url) {
		try {
			String line;
			BufferedReader updateReader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
			List<UUID> people = new ArrayList<UUID>();
			while ((line = updateReader.readLine()) != null) {
				people.add(UUID.fromString(line));
			}
			updateReader.close();
			return people;
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<UUID>();
		}
	}
}