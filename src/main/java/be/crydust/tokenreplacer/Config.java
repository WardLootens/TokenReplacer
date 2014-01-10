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
        StringBuilder replacetokensSB = new StringBuilder();
        replacetokensSB.append("{");
        for (Map.Entry<String, String> replacetoken : replacetokens.entrySet()) {
            replacetokensSB
                    .append("\n    ")
                    .append(replacetoken.getKey())
                    .append('=')
                    .append(replacetoken.getValue())
                    .append(",");
        }
        if (replacetokensSB.length() > 0) {
            replacetokensSB.setLength(replacetokensSB.length() - 1);
        }
        replacetokensSB.append("\n  }");
        return String.format(""
                + "Config{\n"
                + "  begintoken=%s,\n"
                + "  endtoken=%s,\n"
                + "  replacetokens=%s,\n"
                + "  folder=%s,\n"
                + "  quiet=%s\n"
                + "}", begintoken, endtoken, replacetokensSB, folder, quiet);
    }

}
