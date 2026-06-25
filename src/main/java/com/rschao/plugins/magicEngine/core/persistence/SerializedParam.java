package com.rschao.plugins.magicEngine.core.persistence;

import com.google.gson.JsonElement;

public class SerializedParam {
    public String name;
    public String type; // "int","double","boolean","string","uuid","stringList","uuidList"
    public JsonElement value;

    public SerializedParam() {}

    public SerializedParam(String name, String type, JsonElement value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
}

