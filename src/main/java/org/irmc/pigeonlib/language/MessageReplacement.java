package org.irmc.pigeonlib.language;

import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;

@Getter
public final class MessageReplacement implements Pair<String, String> {
    private final String placeholder;
    private final String replacement;

    private MessageReplacement(String placeholder, String replacement) {
        this.placeholder = placeholder;
        this.replacement = replacement;
    }

    @Override
    public String left() {
        return placeholder;
    }

    @Override
    public String right() {
        return replacement;
    }

    public String parse(String message) {
        return message.replaceAll(placeholder, replacement);
    }

    public static MessageReplacement replace(String placeholder, String replacement) {
        return new MessageReplacement(placeholder, replacement);
    }
}
