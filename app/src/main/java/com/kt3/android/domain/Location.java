package com.kt3.android.domain;

import java.util.AbstractMap;

/**
 * Created by 97lynk on 24/02/2018.
 */

public class Location extends AbstractMap.SimpleEntry<Integer, String> {
    public Location(Integer key, String value) {
        super(key, value);
    }

    @Override
    public String toString() {
        return getValue();
    }
}
