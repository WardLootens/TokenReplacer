package be.crydust.tokenreplacer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 *
 * @author kristof
 */
public class FileReader implements Callable<String> {

    private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
    
    private final Path path;
    private final Charset encoding;

    public FileReader(Path path) {
        this(path, DEFAULT_ENCODING);
    }
    
    public FileReader(Path path, Charset encoding) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(encoding);
        this.path = path;
        this.encoding = encoding;
    }

    @Override
    public String call() throws Exception {
        byte[] encoded = Files.readAllBytes(path);
        return encoding.decode(ByteBuffer.wrap(encoded)).toString();
    }

}
