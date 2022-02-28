/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utils.specification;

import org.springframework.data.domain.Pageable;

/**
 *
 * @author Aristide MASSAGA
 */
public class SearchCriteria {
     private String key;
    private String operation;
    private Object value;
    private String type;
    private Pageable pgble;
    private String mode;

    public SearchCriteria() {
        this.type = "text";
    }

    public SearchCriteria(Pageable pgble, String type) {
        this.pgble = pgble;
        this.type = type;
        this.key = "";
        this.operation = "";
        this.value = "";
        this.mode = "semi";
    }
    
    public SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.type = "text";
    }

    public SearchCriteria(String key, String operation, Object value, String type, String mode) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.type = type;
        this.mode = mode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        
        String phrase = key;
        String[] tokens = phrase.split("\\.");
        this.key = tokens[tokens.length - 1];

    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Pageable getPgble() {
        return pgble;
    }

    public void setPgble(Pageable pgble) {
        this.pgble = pgble;
    }
    
    public String field() {
        return pgble.getSort().toString().split(":")[0];
    }
    
    public String direction() {
        return pgble.getSort().getOrderFor(field()).getDirection().name();
    }

    public String getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" + "key=" + key + ", operation=" + operation + ", value=" + value + ", type=" + type + ", pgble=" + pgble + ", mode=" + mode + '}';
    }
    
}
