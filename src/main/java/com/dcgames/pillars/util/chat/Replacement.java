package com.dcgames.pillars.util.chat;

import java.util.HashMap;
import java.util.Map;

public class Replacement {
    private Map<Object, Object> replacements = new HashMap<>();
    private String message;

    public Replacement(String message) {
        this.message = message;
    }

    public Replacement add(Object current, Object replacement) {
        replacements.put(current, replacement);
        return this;
    }

    public String toString() {
        replacements.keySet().forEach(current -> this.message = this.message.replace(String.valueOf(current), String.valueOf(replacements.get(current))));
        return CC.translate(this.message);
    }
}
