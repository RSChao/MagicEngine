package com.rschao.plugins.magicEngine.core.persistence;

import java.util.ArrayList;
import java.util.List;

public class SerializedAction {
    public int version = 1;
    public String type;
    public List<SerializedParam> params = new ArrayList<>();

    public SerializedAction() {}

    public SerializedAction(String type) { this.type = type; }
}

