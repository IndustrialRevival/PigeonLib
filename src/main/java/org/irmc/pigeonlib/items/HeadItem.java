package org.irmc.pigeonlib.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Base64;
import java.util.logging.Logger;

public class HeadItem {
    private static final String FAKE_OWNER = "IR-Pigeon";

    private HeadItem() {}

    public static ItemStack createByBase64(String base64) {
        String decoded = new String(Base64.getDecoder().decode(base64));
        JsonObject json = JsonParser.parseString(decoded).getAsJsonObject();
        JsonObject skin = json.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject();
        String url = skin.get("url").getAsString();
        return createByUrl(url);
    }

    public static ItemStack createByHash(String hash) {
        return createByUrl("https://textures.minecraft.net/texture/" + hash);
    }

    public static ItemStack createByUrl(String url) {
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        OfflinePlayer player = Bukkit.getOfflinePlayer(FAKE_OWNER);
        PlayerProfile profile = (PlayerProfile) player.getPlayerProfile().clone();
        PlayerTextures textures = profile.getTextures();

        try {
            textures.setSkin(URI.create(url).toURL());
        } catch (MalformedURLException e) {
            Logger.getAnonymousLogger("PigeonLib").warning("Invalid URL for skull item: " + url);
        }

        skullMeta.setPlayerProfile(profile);
        itemStack.setItemMeta(skullMeta);

        return itemStack;
    }
}
