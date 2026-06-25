package com.rschao.plugins.magicEngine.core.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ActionId {
    String value();
    int cooldown();
}

