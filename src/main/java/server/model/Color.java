package server.model;

import java.util.*;

public enum Color {

    /*
    GREEN("green"),
    RED("red"),
    YELLOW("yellow"),
    PINK("pink"),
    BLUE("blue");
    */

    GREEN("\u001b[32m", "green"),
    RED("\u001b[31m", "red"),
    YELLOW("\u001b[33m", "yellow"),
    PINK("\u001b[35m", "pink"),
    BLUE("\u001b[34m", "blue");

    public final String ansi;
    public final String label;

    Color(String ansi , String label) {
        this.ansi = ansi;
        this.label = label;

    }

    public static Optional<Color> getColorByLabel(String value) {
        return Arrays.stream(Color.values())
                .filter(color -> color.label.equals(value))
                .findFirst();
    }

    private static final List<Color> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Color getRandom()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}