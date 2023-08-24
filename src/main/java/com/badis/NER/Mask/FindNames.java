package com.badis.NER.Mask;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.client.RestTemplate;

public class FindNames {
    public static Set<String> find_names(MaskedLog MaskedLog) {
        MaskedLog MaskedLog_tosend = new MaskedLog(MaskedLog.getMaskedMessage());
        RestTemplate restTemplate = new RestTemplate();
        Set<?> set = restTemplate.postForEntity("http://localhost:8000/detect_names", MaskedLog_tosend, Set.class).getBody();
        Set<String> strings = set.stream().map(Object::toString).collect(Collectors.toSet());
        return strings;
    }
}
