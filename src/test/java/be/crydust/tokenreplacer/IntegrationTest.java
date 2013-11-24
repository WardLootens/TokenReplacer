package be.crydust.tokenreplacer;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kristof
 */
public class IntegrationTest {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IntegrationTest.class);

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testWriteAndRead() throws Exception {
        String input = "Lorem ipsum";
        Path a = folder.newFile("a").toPath();
        new FileWriter(input, a).run();
        String output = new FileReader(a).call();
        assertThat(output, is(input));
    }
    
    @Test
    public void testReplaceWriteAndRead() throws Exception {
        String expected = "Lorem ipsum";
        String begintoken = "@";
        String endtoken = "@";
        String input = "@a@ @b@";
        Map<String, String> replacetokens = new HashMap<>();
        replacetokens.put("a", "Lorem");
        replacetokens.put("b", "ipsum");
        TokenReplacer replacer = new TokenReplacer(begintoken, endtoken, replacetokens);
        String replaced = replacer.replace(input);
        Path a = folder.newFile("a").toPath();
        new FileWriter(replaced, a).run();
        String output = new FileReader(a).call();
        assertThat(output, is(expected));
    }

    @Test
    public void testFindReadReplaceWriteAndRead() throws Exception {
        String expected = "Lorem ipsum";
        String begintoken = "@";
        String endtoken = "@";
        String input = "@a@ @b@";
        Map<String, String> replacetokens = new HashMap<>();
        replacetokens.put("a", "Lorem");
        replacetokens.put("b", "ipsum");
        File aTemplate = folder.newFile("a.template");
        
        new FileWriter(input, aTemplate.toPath()).run();
        TokenReplacer replacer = new TokenReplacer(begintoken, endtoken, replacetokens);
        List<Path> templates = new FilesFinder(folder.getRoot().toPath(), "*.template").call();
        for(Path template : templates){
            String templateContents = new FileReader(template).call();
            Path file = template.getParent().resolve(template.getFileName().toString().replaceFirst("\\.template$", ""));
            new FileWriter(replacer.replace(templateContents), file).run();
        }
        String output = new FileReader(folder.getRoot().toPath().resolve("a")).call();
        
        assertThat(output, is(expected));
    }

}
