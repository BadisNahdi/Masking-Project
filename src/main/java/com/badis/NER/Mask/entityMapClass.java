package com.badis.NER.Mask;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class entityMapClass {
    @Id
    private String Id;
    @ElementCollection
    private Map<String, String> entityMap = new HashMap<>();
    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }
    public Map<String, String> getEntityMap() {
        return entityMap;
    }
    public void setEntityMap(Map<String, String> entityMap) {
        this.entityMap = entityMap;
    }
    public entityMapClass(String id, Map<String, String> entityMap) {
        Id = id;
        this.entityMap = entityMap;
    }
    public entityMapClass() {
    }
    
}
