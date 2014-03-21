package be.crydust.tokenreplacer;

import java.nio.file.Path;
import java.util.Objects;

/**
 *
 * @author kristof
 */
public final class FileExtensionUtil {

    private FileExtensionUtil() {
    }

    public static Path replaceExtension(Path path, String newExtension) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(newExtension);
        String originalFileName = path.getFileName().toString();
        if (!originalFileName.contains(".")) {
            throw new IllegalArgumentException("path has no extension");
        }
        final String dot;
        if (newExtension.isEmpty() || newExtension.startsWith(".")) {
            dot = "";
        } else {
            dot = ".";
        }
        String newFileName = originalFileName.replaceFirst("\\.\\w+$", dot + newExtension);
        return path.resolveSibling(newFileName);
    }
}
