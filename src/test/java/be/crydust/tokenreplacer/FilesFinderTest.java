package be.crydust.tokenreplacer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author kristof
 */
public class FilesFinderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testEmpty() {
        FilesFinder cut = new FilesFinder(folder.getRoot().toPath(), "*.template");
        List<Path> files = cut.call();
        assertThat(files, is(empty()));
    }

    @Test
    public void testOneFile() throws IOException {
        folder.newFile("a.template");
        FilesFinder cut = new FilesFinder(folder.getRoot().toPath(), "*.template");
        List<Path> files = cut.call();
        assertThat(files.size(), is(1));
    }

    @Test
    public void testTwoFiles() throws IOException {
        folder.newFile("a.template");
        File subFolder = folder.newFolder();
        new File(subFolder, "b.template").createNewFile();
        FilesFinder cut = new FilesFinder(folder.getRoot().toPath(), "*.template");
        List<Path> files = cut.call();
        assertThat(files.size(), is(2));
    }

}
