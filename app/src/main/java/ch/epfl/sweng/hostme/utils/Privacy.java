package ch.epfl.sweng.hostme.utils;

import java.util.Locale;

public enum Privacy {
    NONE, PRIVATE, SHARED;

    @Override
    public String toString() {
        String ret = this.name();
        return ret.charAt(0) + ret.substring(1).toLowerCase();
    }
}
