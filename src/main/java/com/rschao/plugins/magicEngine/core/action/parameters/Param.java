package com.rschao.plugins.magicEngine.core.action.parameters;

import java.util.Objects;

/** Simple pair nombre->valor para parámetros de acción. */
public final class Param {
    private final String name;
    private final Object value;

    public Param(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }
    public Object getValue() { return value; }

    @Override
    public String toString() {
        return name + " = " + Objects.toString(value, "null");
    }
}

