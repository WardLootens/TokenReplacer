package be.crydust.tokenreplacer;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kristof
 */
public class FilesFinder implements Callable<List<Path>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilesFinder.class);

    private final Path path;
    private final PathMatcher matcher;

    public FilesFinder(Path path, String expression) {
        Objects.requireNonNull(path);
        Strings.requireNonEmpty(expression);
        this.path = path;
        this.matcher = FileSystems.getDefault()
                .getPathMatcher("glob:" + expression);
    }

    @Override
    public List<Path> call() {
        final List<Path> files = new ArrayList<>();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path name = file.getFileName();
                    if (name != null && matcher.matches(name)) {
                        files.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            LOGGER.error(null, ex);
        }
        return files;
    }

}
