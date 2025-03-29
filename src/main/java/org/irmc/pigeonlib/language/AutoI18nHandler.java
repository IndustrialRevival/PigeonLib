package org.irmc.pigeonlib.language;

import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;
import org.irmc.pigeonlib.enums.Language;

import java.util.List;

public abstract class AutoI18nHandler {
    private final LanguageManager languageManager;

    public static AutoI18nHandler createDefault(Plugin plugin) {
        return createDefault(plugin, Language.EN_US);
    }

    public static AutoI18nHandler createDefault(Plugin plugin, Language defaultLanguage) {
        return new AutoI18nHandler(plugin, defaultLanguage) {
            @Override
            public Component getI18nItemName(Language language, String id) {
                return getLanguageManager().getItemNameByLanguage(language, id);
            }

            @Override
            public List<Component> getI18nItemLore(Language language, String id) {
                return getLanguageManager().getItemLoreByLanguage(language, id);
            }
        };
    }

    public AutoI18nHandler(Plugin plugin) {
        this(plugin, Language.EN_US);
    }

    public AutoI18nHandler(Plugin plugin, Language defaultLanguage) {
        this.languageManager = new LanguageManager(plugin, defaultLanguage);
    }

    public abstract Component getI18nItemName(Language language, String id);

    public abstract List<Component> getI18nItemLore(Language language, String id);

    public LanguageManager getLanguageManager() {
        return languageManager;
    }
}
