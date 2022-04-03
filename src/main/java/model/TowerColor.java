package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum TowerColor {
    WHITE, BLACK, GREY;

    private static final List<TowerColor> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static TowerColor getRandom()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}