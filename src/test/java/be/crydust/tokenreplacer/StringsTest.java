package be.crydust.tokenreplacer;

import org.junit.Test;

public class StringsTest {

    @Test(expected = NullPointerException.class)
    public void testRequireNonEmptyWithNull() {
        Strings.requireNonEmpty(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequireNonEmptyWithEmpty() {
        Strings.requireNonEmpty("");
    }

    @Test
    public void testRequireNonEmptyWithNonEmpty() {
        Strings.requireNonEmpty("a");
    }

}
