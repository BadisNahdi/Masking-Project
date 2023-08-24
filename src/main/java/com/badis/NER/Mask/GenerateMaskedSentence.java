package com.badis.NER.Mask;

import java.util.Map;
import java.util.Set;

public class GenerateMaskedSentence {
    public static String generateMaskedSentence(String message, Map<String, String> entityMap, Set<String> names) {
        StringBuilder maskedSentence = new StringBuilder();
    
        String[] tokens = message.split("[\\s\\n]+");
        int i = 0;
        while (i < tokens.length) {
            String token = capitalizeFirstLetter(tokens[i]);
            if (entityMap.containsKey(token)  && names.contains(token)) {
                maskedSentence.append(entityMap.get(token)).append(" ");
                i++;
            } else {
                String nextToken = i + 1 < tokens.length ? capitalizeFirstLetter(tokens[i + 1]) : "";
                String multiWordToken = token + " " + nextToken;
                if (entityMap.containsKey(multiWordToken) && names.contains(multiWordToken)) {
                    maskedSentence.append(entityMap.get(multiWordToken)).append(" ");
                    i += 2;
                } else {
                    maskedSentence.append(tokens[i]).append(" ");
                    i++;
                }
            }
        }
        return maskedSentence.toString().trim().substring(8);
    }
    
    private static String capitalizeFirstLetter(String word) {
        if (word.isEmpty()) {
            return word;
        }
        char[] chars = word.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
}
