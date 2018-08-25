package org.ecolab.server.common;

public class I18NUtils {
    public static String getEnumName(Enum<?> key) {
        return key.getDeclaringClass().getSimpleName() + '.' + key.name();
    }
}
