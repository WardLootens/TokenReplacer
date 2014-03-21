package be.crydust.tokenreplacer;

import java.util.Objects;

/**
 *
 * @author kristof
 */
public final class Strings {

    private Strings() {
    }

    public static String requireNonEmpty(String string) {
        Objects.requireNonNull(string);
        if (string.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return string;
    }

}
