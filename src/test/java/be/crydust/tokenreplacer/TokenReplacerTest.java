package be.crydust.tokenreplacer;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TokenReplacerTest {

    @Test
    public void testReplacetokensWithNoMatches() {
        String begintoken = "<";
        String endtoken = ">";
        Map<String, String> replacetokens = new HashMap<>();
        replacetokens.put("DATE", "today");
        String input = "Lorem ipsum";
        String expected = "Lorem ipsum";
        TokenReplacer cut = new TokenReplacer(begintoken, endtoken, replacetokens);
        String actual = cut.replace(input);
        assertThat(actual, is(expected));
    }

    @Test
    public void testReplacetokensWithOneMatch() {
        String begintoken = "<";
        String endtoken = ">";
        Map<String, String> replacetokens = new HashMap<>();
        replacetokens.put("DATE", "today");
        String input = "Lorem <DATE> ipsum";
        String expected = "Lorem today ipsum";
        TokenReplacer cut = new TokenReplacer(begintoken, endtoken, replacetokens);
        String actual = cut.replace(input);
        assertThat(actual, is(expected));
    }

    @Test
    public void testReplacetokensWithMatchAtStartMiddleEnd() {
        String begintoken = "<";
        String endtoken = ">";
        Map<String, String> replacetokens = new HashMap<>();
        replacetokens.put("DATE", "today");
        String input = "<DATE> Lorem <DATE> ipsum <DATE>";
        String expected = "today Lorem today ipsum today";
        TokenReplacer cut = new TokenReplacer(begintoken, endtoken, replacetokens);
        String actual = cut.replace(input);
        assertThat(actual, is(expected));
    }
}
