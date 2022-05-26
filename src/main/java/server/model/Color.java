package server.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Color {

    RED("\u001b[31m"),
    YELLOW("\u001b[33m"),
    BLUE("\u001b[34m"),
    GREEN("\u001b[32m"),
    PINK("\u001b[35m");

    public final String label;

    Color(String label) {
        this.label = label;
    }

    private static final List<Color> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Color getRandom()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}