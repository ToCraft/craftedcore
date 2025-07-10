package dev.tocraft.craftedcore.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class NetUtils {
    public static byte @NotNull [] getByteResponse(URL url) throws IOException {
        return getByteResponse(new HashMap<>(), url);
    }

    public static byte @NotNull [] getByteResponse(@NotNull Map<String, String> header, @NotNull URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
        byte[] response = connection.getInputStream().readAllBytes();
        connection.disconnect();
        return response;
    }

    @NotNull
    public static String getTextResponse(Map<String, String> header, URL url) throws IOException {
        byte[] response = getByteResponse(header, url);
        return new String(response, StandardCharsets.UTF_8);
    }

    @NotNull
    public static String getTextResponse(URL url) throws IOException {
        return getTextResponse(new HashMap<>(), url);
    }

    public static JsonElement getJsonResponse(Gson gson, URL url) throws IOException {
        return getJsonResponse(gson, new HashMap<>(), url);
    }

    public static JsonElement getJsonResponse(@NotNull Gson gson, Map<String, String> header, URL url) throws IOException {
        String response = getTextResponse(header, url);
        return gson.fromJson(response, JsonElement.class);
    }
}
