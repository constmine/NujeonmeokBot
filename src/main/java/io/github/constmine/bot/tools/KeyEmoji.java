package io.github.constmine.bot.tools;

public enum KeyEmoji {
    ONE(":one:"),
    TWO(":two:"),
    THREE(":three:"),
    FOUR(":four:"),
    FIVE(":five:"),
    SIX(":six:"),
    SEVEN(":seven:"),
    EIGHT(":eight:"),
    NINE(":nine:"),
    TEN(":keycap_ten:");

    private final String emoji;

    KeyEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }

    public static KeyEmoji getByIndex(int index) {
        if (index >= 0 && index < values().length) {
            return values()[index];
        } else {
            throw new IllegalArgumentException("잘못된 인덱스");
        }
    }
}
