package be.crydust.tokenreplacer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author kristof
 */
public class ActionTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public Action createSimpleAction() {
        String begintoken = "@";
        String endtoken = "@";
        Map<String, String> replacetokens = new HashMap<>();
        replacetokens.put("a", "A");
        boolean quiet = true;
        Config config = new Config(begintoken, endtoken, replacetokens, folder.getRoot().toPath(), quiet, new String[0]);
        return new Action(config);
    }

    public Action createActionWithExclude(String exclude) {
        String begintoken = "@";
        String endtoken = "@";
        Map<String, String> replacetokens = new HashMap<>();
        replacetokens.put("a", "A");
        boolean quiet = true;
        Config config = new Config(begintoken, endtoken, replacetokens, folder.getRoot().toPath(), quiet, new String[]{exclude});
        return new Action(config);
    }

    @Test
    public void testNormal() throws Exception {
        File file = folder.newFile("a");
        File template = folder.newFile("a.template");
        new FileWriter("unchanged", file.toPath()).run();
        new FileWriter("@a@", template.toPath()).run();
        createSimpleAction().run();
        assertThat(new FileReader(file.toPath()).call(), is("A"));
    }

    @Test
    public void testReadonly() throws Exception {
        File file = folder.newFile("a");
        File template = folder.newFile("a.template");
        folder.newFile("a.readonly");
        new FileWriter("unchanged", file.toPath()).run();
        new FileWriter("@a@", template.toPath()).run();
        createSimpleAction().run();
        assertThat(new FileReader(file.toPath()).call(), is("unchanged"));
    }

    @Test
    public void testFolderIntheWay() throws Exception {
        Action cut = createSimpleAction();
        folder.newFolder("a");
        folder.newFile("a.template");
        cut.run();
    }

    @Test
    public void testExclude() throws Exception {
        folder.newFolder("a");
        folder.newFolder("a/tmp");
        folder.newFolder("tmp");
        folder.newFolder("tmp/a");
        File file1 = folder.newFile("1");
        File template1 = folder.newFile("1.template");
        File file2 = folder.newFile("tmp/2");
        File template2 = folder.newFile("tmp/2.template");
        File file3 = folder.newFile("a/tmp/3");
        File template3 = folder.newFile("a/tmp/3.template");
        File file4 = folder.newFile("tmp/a/4");
        File template4 = folder.newFile("tmp/a/4.template");
        File file5 = folder.newFile("a/5");
        File template5 = folder.newFile("a/5.template");
        new FileWriter("@a@", template1.toPath()).run();
        new FileWriter("@a@", template2.toPath()).run();
        new FileWriter("@a@", template3.toPath()).run();
        new FileWriter("@a@", template4.toPath()).run();
        new FileWriter("@a@", template5.toPath()).run();
        createActionWithExclude("**/tmp/**").run();
        assertThat(new FileReader(file1.toPath()).call(), is("A"));
        assertThat(new FileReader(file2.toPath()).call(), is(""));
        assertThat(new FileReader(file3.toPath()).call(), is(""));
        assertThat(new FileReader(file4.toPath()).call(), is(""));
        assertThat(new FileReader(file5.toPath()).call(), is("A"));
    }

}
