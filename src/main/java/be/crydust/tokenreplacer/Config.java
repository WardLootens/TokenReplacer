package be.crydust.tokenreplacer;

import java.nio.file.Path;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kristof
 */
public class Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    private final String begintoken;
    private final String endtoken;
    private final Map<String, String> replacetokens;
    private final Path folder;
    private final boolean quiet;

    public Config(String begintoken, String endtoken, Map<String, String> replacetokens, Path folder, boolean quiet) {
        this.begintoken = begintoken;
        this.endtoken = endtoken;
        this.replacetokens = replacetokens;
        this.folder = folder;
        this.quiet = quiet;
    }

    public String getBegintoken() {
        return begintoken;
    }

    public String getEndtoken() {
        return endtoken;
    }

    public Map<String, String> getReplacetokens() {
        return replacetokens;
    }

    public Path getFolder() {
        return folder;
    }

    public boolean isQuiet() {
        return quiet;
    }

    @Override
    public String toString() {
        return "Config{\n" + "  begintoken=" + begintoken + ",\n" + "  endtoken=" + endtoken + ",\n" + "  replacetokens=" + replacetokens + ",\n" + "  folder=" + folder + ",\n" + "  quiet=" + quiet + "\n" + "}";
    }

}
