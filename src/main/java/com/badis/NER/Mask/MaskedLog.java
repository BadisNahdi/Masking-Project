package com.badis.NER.Mask;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;

public class MaskedLog {

    private entityMapRepository EntityMapRepository;

    private String generateRandomIdentifier() {
        return UUID.randomUUID().toString();
    }

    private String maskedMessage;

    public String getMaskedMessage() {
        return maskedMessage;
    }

    public void setMaskedMessage(String maskedMessage) {
        this.maskedMessage = maskedMessage;
    }

    public entityMapRepository getEntityMapRepository() {
        return EntityMapRepository;
    }

    public MaskedLog(String maskedMessage, entityMapRepository entityMapRepository) {
        EntityMapRepository = entityMapRepository;
        this.maskedMessage = maskedMessage;
    }

    public void setEntityMapRepository(entityMapRepository entityMapRepository) {
        EntityMapRepository = entityMapRepository;
    }

    public MaskedLog(String maskedMessage) {
        this.maskedMessage = maskedMessage;
    }

    public MaskedLog() {
    }

    public String retirieveThreadId() {
        return "ABC";
    }

    public void mask_PhysicalAddress() {
        String sentence = this.getMaskedMessage();
        String ThreadId = this.retirieveThreadId();
        entityMapRepository entityMapRepository = this.getEntityMapRepository();
        Optional<entityMapClass> entityMapOptional = entityMapRepository.findById(ThreadId);
        entityMapClass entityMapInstance;

        if (!entityMapOptional.isPresent()) {
            entityMapInstance = EntityMapRepository.save(new entityMapClass(ThreadId, new HashMap<>()));
        } else {
            entityMapInstance = entityMapOptional.get();
        }

        Map<String, String> entityMap = entityMapInstance.getEntityMap();

        // Updated regex pattern with double backslashes for Java string
        String pattern = "[0-9a-zA-Z]{4}[+][0-9a-zA-Z]{3},+[0-9a-zA-z\\s\\-_]+,[a-zA-z\\s\\-_]+[0-9]{3,5}";
        Pattern addressPattern = Pattern.compile(pattern);
        Matcher matcher = addressPattern.matcher(sentence);
        StringBuilder maskedSentence = new StringBuilder();
        int lastEnd = 0;

        // Process the pattern matches
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String address = sentence.substring(start, end);
            if (!entityMap.containsKey(address)) {
                String entityId = generateRandomIdentifier();
                entityMap.put(address, entityId);
            }
            maskedSentence.append(sentence, lastEnd, start);
            maskedSentence.append(entityMap.get(address));
            lastEnd = end;
        }
        if (lastEnd < sentence.length()) {
            maskedSentence.append(sentence.substring(lastEnd));
        }
        entityMapInstance.setEntityMap(entityMap);
        EntityMapRepository.save(entityMapInstance);
        this.setMaskedMessage(maskedSentence.toString());
    }



    public void Mask_Names() {

        entityMapRepository entityMapRepository = this.getEntityMapRepository();
        String message = this.getMaskedMessage();
        message = "Masked: " + message;
        this.setMaskedMessage(message);

        Set<String> names = FindNames.find_names(this);
        String ThreadId = this.retirieveThreadId();
        Optional<entityMapClass> entityMapOptional = entityMapRepository.findById(ThreadId);
        entityMapClass entityMapInstance;

        if (!entityMapOptional.isPresent()) {
            entityMapInstance = entityMapRepository.save(new entityMapClass(ThreadId, new HashMap<>()));
        } else {
            entityMapInstance = entityMapOptional.get();
        }
        
        Map<String, String> entityMap = entityMapInstance.getEntityMap();
        names.forEach(name -> {
            if (!entityMap.containsKey(name)) {
                String entityId = generateRandomIdentifier();
                entityMap.put(name, entityId);
            }
        });
        entityMapInstance.setEntityMap(entityMap);
        entityMapRepository.save(entityMapInstance);

        this.setMaskedMessage(GenerateMaskedSentence.generateMaskedSentence(message, entityMap, names));
    }

    public void Mask_Email() {
        entityMapRepository entityMapRepository = this.getEntityMapRepository();
        String message = this.getMaskedMessage();
        String ThreadId = this.retirieveThreadId();
        Optional<entityMapClass> entityMapOptional = entityMapRepository.findById(ThreadId);
        entityMapClass entityMapInstance;
        if (!entityMapOptional.isPresent()) {
            entityMapInstance = entityMapRepository.save(new entityMapClass(ThreadId, new HashMap<>()));
        } else {
            entityMapInstance = entityMapOptional.get();
        }
        Map<String, String> entityMap = entityMapInstance.getEntityMap();
        String[] words = message.split("\\s+");

        StringBuilder maskedSentence = new StringBuilder();
        for (String word : words) {
            if (EmailValidator.getInstance().isValid(word)) {
                if (!entityMap.containsKey(word)) {
                    String entityId = generateRandomIdentifier();
                    entityMap.put(word, entityId);
                }
                maskedSentence.append(entityMap.get(word));
            } else {
                maskedSentence.append(word).append(" ");
            }
        }
        entityMapInstance.setEntityMap(entityMap);
        entityMapRepository.save(entityMapInstance);
        this.setMaskedMessage(maskedSentence.toString().trim());
    }

}
