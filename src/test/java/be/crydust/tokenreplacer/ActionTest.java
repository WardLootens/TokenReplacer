package be.crydust.tokenreplacer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Before;

/**
 *
 * @author kristof
 */
public class ActionTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    Action cut;
    
    @Before
    public void before() {
        String begintoken = "@";
        String endtoken = "@";
        Map<String, String> replacetokens = new HashMap<>();
        replacetokens.put("a", "A");
        boolean quiet = true;
        Config config = new Config(begintoken, endtoken, replacetokens, folder.getRoot().toPath(), quiet);
        cut = new Action(config);
    }
    
    @Test
    public void testNormal() throws Exception {
        File file = folder.newFile("a");
        File template = folder.newFile("a.template");
        new FileWriter("unchanged", file.toPath()).run();
        new FileWriter("@a@", template.toPath()).run();
        cut.run();
        assertThat(new FileReader(file.toPath()).call(), is("A"));
    } 
    
    @Test
    public void testReadonly() throws Exception {
        File file = folder.newFile("a");
        File template = folder.newFile("a.template");
        folder.newFile("a.readonly");
        new FileWriter("unchanged", file.toPath()).run();
        new FileWriter("@a@", template.toPath()).run();
        cut.run();
        assertThat(new FileReader(file.toPath()).call(), is("unchanged"));
    }

}
