package org.irmc.pigeonlib.language;

import org.irmc.pigeonlib.objects.Pair;

public final class MessageReplacement extends Pair<String, String> {
    private MessageReplacement(String placeholder, String replacement) {
        super(placeholder, replacement);
    }

    public String parse(String message) {
        return message.replaceAll(left(), right());
    }

    public static MessageReplacement replace(String placeholder, String replacement) {
        return new MessageReplacement(placeholder, replacement);
    }
}
