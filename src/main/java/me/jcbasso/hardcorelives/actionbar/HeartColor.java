package me.jcbasso.hardcorelives.actionbar;

public enum HeartColor {
    EMPTY("empty", "\uE000"),
    RED("red", "\uE001"),
    GOLD("gold", "\uE002");

    private final String value;
    private final String unicode;

    HeartColor(String value, String unicode) {
        this.value = value;
        this.unicode = unicode;
    }

    public static HeartColor fromString(String value) throws EnumConstantNotPresentException {
        for (HeartColor heartColor : values()) {
            if (heartColor.value.equals(value)) {
                return heartColor;
            }
        }
        throw new EnumConstantNotPresentException(HeartColor.class, value);
    }

    public String toString() {
        return this.value;
    }

    public String getUnicode() {
        return this.unicode;
    }
}

