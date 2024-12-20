package org.irmc.pigeonlib.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused"})
public class ClipboardUtil {
    public static void send(@NotNull Player player, String display, String text) {
        send(player, display, "点击复制到剪贴板", text);
    }

    public static void send(@NotNull Player player, String display, String hover, String text) {
        Component kyoriText = LegacyComponentSerializer.legacySection().deserialize(display + text);
        kyoriText = kyoriText.hoverEvent(HoverEvent.showText(LegacyComponentSerializer.legacySection().deserialize(hover)));
        kyoriText = kyoriText.clickEvent(ClickEvent.copyToClipboard(text));
        player.sendMessage(kyoriText);
    }
}
