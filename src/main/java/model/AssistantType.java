package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum AssistantType {
    ONE, TWO, THREE, FOUR;

    private static final List<AssistantType> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static AssistantType getRandom()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
