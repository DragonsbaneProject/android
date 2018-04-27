package io.dragonsbane.android.util;

import java.util.Random;

/**
 * Created by Brian on 1/17/18.
 */

public final class Numbers {

    public static int randomNumber(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

}
