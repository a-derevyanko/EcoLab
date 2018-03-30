package org.ekolab.server.common;

import java.util.List;
import java.util.SortedMap;

/**
 * Утилиты, испльзуемые из *.jrxml описаний отчёта
 */
@SuppressWarnings("unused")
public class ReportUtils {
    public static <T, V> String getFromMap(SortedMap<T, List<V>> map, T key, int index) {
        return map.containsKey(key) ? String.valueOf(map.get(key).get(index)) : "";
    }
    public static <T, V> String getFromListOfList(List<List<V>> listOfList, int i, int j) {
        return listOfList.size() > i ? String.valueOf(listOfList.get(i).get(j)) : "";
    }
}
