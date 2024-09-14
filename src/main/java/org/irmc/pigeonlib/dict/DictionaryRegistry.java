package org.irmc.pigeonlib.dict;

import java.util.ArrayList;
import java.util.List;

public class DictionaryRegistry {
    private static final List<? super Dictionary> dictionaries = new ArrayList<>();
    public static void registerDictionary(Dictionary dictionary) {
        dictionary.setEnabled(true);
        dictionaries.add(dictionary);
    }
    public static void unregisterDictionary(Dictionary dictionary) {
        dictionary.setEnabled(true);
        dictionary.setLocked(false);
        dictionaries.remove(dictionary);
    }
    public static int getDictionaryCount() {
        return dictionaries.size();
    }
}
