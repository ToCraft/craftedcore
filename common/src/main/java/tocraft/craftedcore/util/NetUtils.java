package tocraft.craftedcore.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class NetUtils {
    @NotNull
    public static String getTextResponse(Map<String, String> header, URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        connection.disconnect();
        in.close();
        return content.toString();
    }

    @NotNull
    public static String getTextResponse(URL url) throws IOException {
        return getTextResponse(new HashMap<>(), url);
    }

    public static JsonElement getJsonResponse(Gson gson, URL url) throws IOException {
        return getJsonResponse(gson, new HashMap<>(), url);
    }

    public static JsonElement getJsonResponse(Gson gson, Map<String, String> header, URL url) throws IOException {
        return GsonHelper.fromJson(gson, getTextResponse(header, url), JsonElement.class);
    }
}
