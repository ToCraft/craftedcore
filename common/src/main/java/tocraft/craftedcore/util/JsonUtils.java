package tocraft.craftedcore.util;

import java.util.*;

public class JsonUtils {
    public static String addComments(String json, Map<String, String> keyToComment) {
        List<String> lines = new ArrayList<>(json.lines().toList());
        Map<Integer, String> insertions = new TreeMap<>();
        // write comments into new file
        for (int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);
            String indentation = " ".repeat(s.length() - s.trim().length());

            for (Map.Entry<String, String> entry : keyToComment.entrySet()) {
                String comment = entry.getValue();
                if (s.trim().startsWith(String.format("\"%s\"", entry.getKey()))) {
                    if (comment.contains("\n")) {
                        comment = indentation + "// " + String.join(String.format("\n%s// ", indentation), comment.split("\n"));
                    } else {
                        comment = String.format("%s// %s", indentation, comment);
                    }
                    insertions.put(i + insertions.size(), comment);
                    break;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        insertions.forEach(lines::add);
        lines.forEach(s -> sb.append(String.format("%s%n", s)));
        return sb.toString();
    }
}
