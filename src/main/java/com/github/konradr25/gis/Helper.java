package com.github.konradr25.gis;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Helper {

    public static <T> List<T> symetricAdd(Collection<T> c1, Collection<T> c2) {
        List<T> joined = new ArrayList<>();
        joined.addAll(c1);
        joined.addAll(c2);
        deleteDoubles(joined);

        return joined;
    }

    private static <T> void deleteDoubles(Collection<T> collection) {
        Set<T> set = new HashSet<>(collection);

        for (T item : set) {
            if (Collections.frequency(collection, item) == 2)
                collection.removeAll(Collections.singleton(item));
        }
    }

}
