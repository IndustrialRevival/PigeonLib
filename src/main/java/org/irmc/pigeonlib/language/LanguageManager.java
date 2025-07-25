package org.irmc.pigeonlib.language;

import java.io.File;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.irmc.pigeonlib.enums.Language;
import org.irmc.pigeonlib.file.ConfigFileUtil;
import org.jetbrains.annotations.Nullable;

/**
 * A class to manage language files and messages.
 * You can create a new instance of this class by passing a reference to your plugin.
 */
public final class LanguageManager {
    private final Plugin plugin;
    private final Language defaultLanguage;

    private final Map<String, YamlConfiguration> configurations = new HashMap<>();

    private boolean detectPlayerLocale;
    private YamlConfiguration defaultConfiguration;

    public LanguageManager(Plugin plugin) {
        this(plugin, Language.EN_US);
    }

    public LanguageManager(Plugin plugin, Language defaultLanguage) {
        this.plugin = plugin;
        this.defaultLanguage = defaultLanguage;

        loadLanguages();
    }

    private void loadLanguages() {
        detectPlayerLocale = plugin.getConfig().getBoolean("detect-player-locale", true);

        defaultConfiguration =
                YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "language/zh-CN.yml"));

        File pluginFolder = plugin.getDataFolder();

        URL fileURL = Objects.requireNonNull(plugin.getClass().getClassLoader().getResource("language/"));
        String jarPath = fileURL.toString().substring(0, fileURL.toString().indexOf("!/") + 2);

        try {
            URL jar = URI.create(jarPath).toURL();
            JarURLConnection jarCon = (JarURLConnection) jar.openConnection();
            JarFile jarFile = jarCon.getJarFile();
            Enumeration<JarEntry> jarEntries = jarFile.entries();

            while (jarEntries.hasMoreElements()) {
                JarEntry entry = jarEntries.nextElement();
                String name = entry.getName();
                if (name.startsWith("language/") && !entry.isDirectory()) {
                    String realName = name.replaceAll("language/", "");
                    try (InputStream stream = plugin.getClass().getClassLoader().getResourceAsStream(name)) {
                        File destinationFile = new File(pluginFolder, "language/" + realName);

                        if (!destinationFile.exists() && stream != null) {
                            plugin.saveResource("language/" + realName, false);
                        }

                        ConfigFileUtil.completeLangFile(plugin, "language/" + realName);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        File[] languageFiles = new File(pluginFolder, "language").listFiles();
        if (languageFiles != null) {
            for (File languageFile : languageFiles) {
                String language = convertToRightLangCode(languageFile.getName().replaceAll(".yml", ""));
                configurations.put(language, YamlConfiguration.loadConfiguration(languageFile));
            }
        }
    }

    private String convertToRightLangCode(String lang) {
        if (lang == null || lang.isBlank()) return "en-US";
        String[] split = lang.split("-");
        if (split.length == 1) {
            String[] split2 = lang.split("_");
            if (split2.length == 1) return lang;
            return lang.replace(split2[1], split2[1].toUpperCase());
        }
        return lang.replace(split[1], split[1].toUpperCase());
    }

    //fixed format methods

    public Component getItemName(NamespacedKey id) {
        return parseToComponent(getMsg(null, "item." + id.toString() + ".name"));
    }

    public List<Component> getItemLore(NamespacedKey id) {
        return parseToComponentList(getMsgList(null, "item." + id.toString() + ".lore"));
    }

    public Component getRecipeTypeName(NamespacedKey key) {
        return parseToComponent(getMsg(null, "recipe_type." + key + ".name"));
    }

    public List<Component> getRecipeTypeLore(NamespacedKey key) {
        return parseToComponentList(getMsgList(null, "recipe_type." + key + ".lore"));
    }

    public Component getGroupName(Component id) {
        return parseToComponent(getMsg(null, "group." + id + ".name"));
    }

    public List<Component> getGroupLore(Component id) {
        return parseToComponentList(getMsgList(null, "group." + id + ".lore"));
    }

    public Component getItemNameByLanguage(Language lang, String id) {
        return parseToComponent(getMsgByLanguage(lang, "item." + id + ".name"));
    }

    public List<Component> getItemLoreByLanguage(Language lang, String id) {
        return parseToComponentList(getMsgListByLanguage(lang, "item." + id + ".lore"));
    }

    public Component getRecipeTypeNameByLanguage(Language lang, NamespacedKey key) {
        return parseToComponent(getMsgByLanguage(lang, "recipe_type." + key + ".name"));
    }

    public List<Component> getRecipeTypeLoreByLanguage(Language lang, NamespacedKey key) {
        return parseToComponentList(getMsgListByLanguage(lang, "recipe_type." + key + ".lore"));
    }

    public Component getGroupNameByLanguage(Language lang, NamespacedKey id) {
        return parseToComponent(getMsgByLanguage(lang, "group." + id + ".name"));
    }

    public List<Component> getGroupLoreByLanguage(Language lang, NamespacedKey id) {
        return parseToComponentList(getMsgListByLanguage(lang, "group." + id + ".lore"));
    }

    //end of fixed format methods

    public void sendMessage(CommandSender CommandSender, String key, MessageReplacement... args) {
        CommandSender.sendMessage(parseToComponent(getMsg(CommandSender, key, args)));
    }

    public void consoleMessage(String key, MessageReplacement... args) {
        Bukkit.getConsoleSender().sendMessage(parseToComponent(getMsg(null, key, args)));
    }

    public Component getMsgComponent(@Nullable CommandSender commandSender, String key, MessageReplacement... args) {
        return parseToComponent(getMsg(commandSender, key, args));
    }

    public Component getMsgComponentByLanguage(@Nullable Language lang, String key, MessageReplacement... args) {
        return parseToComponent(getMsgByLanguage(lang, key, args));
    }

    public List<Component> getMsgComponentList(
            @Nullable CommandSender CommandSender, String key, MessageReplacement... args) {
        return parseToComponentList(getMsgList(CommandSender, key, args));
    }

    public List<Component> getMsgComponentListByLanguage(
            @Nullable Language lang, String key, MessageReplacement... args) {
        return parseToComponentList(getMsgListByLanguage(lang, key, args));
    }

    public String getMsg(@Nullable CommandSender commandSender, String key, MessageReplacement... args) {
        String msg = getConfiguration(commandSender).getString(key);
        if (msg == null) {
            return key;
        }

        for (MessageReplacement arg : args) {
            msg = arg.parse(msg);
        }

        return msg;
    }

    public List<String> getMsgList(@Nullable CommandSender commandSender, String key, MessageReplacement... args) {
        List<String> msgList = getConfiguration(commandSender).getStringList(key);
        for (MessageReplacement arg : args) {
            msgList.replaceAll(arg::parse);
        }

        return msgList;
    }

    public String getMsgByLanguage(@Nullable Language lang, String key, MessageReplacement... args) {
        String msg = getConfiguration(lang).getString(key);
        if (msg == null) {
            return key;
        }

        for (MessageReplacement arg : args) {
            msg = arg.parse(msg);
        }

        return msg;
    }

    public List<String> getMsgListByLanguage(@Nullable Language lang, String key, MessageReplacement... args) {
        List<String> msgList = getConfiguration(lang).getStringList(key);
        for (MessageReplacement arg : args) {
            msgList.replaceAll(arg::parse);
        }

        return msgList;
    }

    public void reload() {
        loadLanguages();
    }

    public static Component parseToComponent(String msg) {
        return MiniMessage.miniMessage().deserialize(msg).decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> parseToComponentList(List<String> msgList) {
        return msgList.stream().map(LanguageManager::parseToComponent).toList();
    }

    private Configuration getConfiguration(CommandSender p) {
        if (!detectPlayerLocale || !(p instanceof Player pl)) {
            String lang = plugin.getConfig().getString("language", defaultLanguage.toTagRegionUpper());
            return configurations.getOrDefault(lang, defaultConfiguration);
        }

        return configurations.getOrDefault(pl.locale().toLanguageTag(), defaultConfiguration);
    }

    private Configuration getConfiguration(Language lang) {
        return configurations.getOrDefault(Objects.requireNonNullElse(lang, defaultLanguage).toTagRegionUpper(), defaultConfiguration);
    }
}