package org.irmc.pigeonLib.chat;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class ChatInput {

    static ChatInputListener listener;

    private ChatInput() {}

    public static void waitForPlayer(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull Consumer<String> handler) {
        waitForPlayer(plugin, p, s -> true, handler);
    }

    public static void waitForPlayer(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull Predicate<String> predicate, @Nonnull Consumer<String> handler) {
        queue(plugin, p, new ChatInputHandler() {

            @Override
            public boolean test(String msg) {
                return predicate.test(msg);
            }

            @Override
            public void handleInput(Player p, String msg) {
                handler.accept(msg);
            }

        });
    }

    public static void waitForPlayer(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull BiConsumer<Player, String> handler) {
        waitForPlayer(plugin, p, s -> true, handler);
    }

    public static void waitForPlayer(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull Predicate<String> predicate, @Nonnull BiConsumer<Player, String> handler) {
        queue(plugin, p, new ChatInputHandler() {

            @Override
            public boolean test(String msg) {
                return predicate.test(msg);
            }

            @Override
            public void handleInput(Player p, String msg) {
                handler.accept(p, msg);
            }

        });
    }

    public static void queue(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull ChatInputHandler callback) {
        if (listener == null) {
            listener = new ChatInputListener(plugin);
        }

        listener.addCallback(p.getUniqueId(), callback);
    }
}
