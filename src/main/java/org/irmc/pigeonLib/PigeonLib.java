package org.irmc.pigeonLib;

import org.bukkit.plugin.java.JavaPlugin;
import org.irmc.pigeonLib.language.LanguageManager;

public final class PigeonLib extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getComponentLogger().info(LanguageManager.parseToComponent("<green>PigeonLib has been enabled!"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
