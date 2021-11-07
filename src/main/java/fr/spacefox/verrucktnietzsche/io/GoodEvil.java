package fr.spacefox.verrucktnietzsche.io;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GoodEvil {
    GREATER_GOOD("Greater Good", "#145A32"),
    GOOD("Good", "#1E8449"),
    LESSER_GOOD("Lesser Good", "#229954"),
    BEYOND_GOOD("Beyond Good", "#117A65"),

    GREATER_EVIL("Greater Evil", "#641E16"),
    EVIL("Evil", "#922B21"),
    LESSER_EVIL("Lesser Evil", "#C0392B"),
    BEYOND_EVIL("Beyond Evil", "#B03A2E"),

    BEYOND_GOOD_EVIL("Beyond Good & Evil", "#1A5276"),
    NOT_GOOD_NOR_EVIL("Not Good nor Evil", "#B7950B"),
    NEUTRAL("Neutral", "#283747"),
    PARADOXAL("Paradoxal", "#5B2C6F"),

    EMPTY("Empty", "#4D5656"),
    DONT_KNOW("Gibberish", "#424949");

    private final String message;
    private final String color;

    GoodEvil(String message, String color) {
        this.message = message;
        this.color = color;
    }

    public String getMessage() {
        return message;
    }

    public String getColor() {
        return color;
    }
}
