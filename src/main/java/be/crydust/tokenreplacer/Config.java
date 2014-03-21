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
    private final String[] excludes;

    public Config(String begintoken, String endtoken, Map<String, String> replacetokens, Path folder, boolean quiet, String[] excludes) {
        this.begintoken = begintoken;
        this.endtoken = endtoken;
        this.replacetokens = replacetokens;
        this.folder = folder;
        this.quiet = quiet;
        this.excludes = excludes;
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

    public String[] getExcludes() {
        return excludes;
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
        if (replacetokensSB.length() > 1) {
            replacetokensSB.setLength(replacetokensSB.length() - 1);
        }
        replacetokensSB.append("\n  }");

        StringBuilder excludesSB = new StringBuilder();
        excludesSB.append("[");
        for (String exclude : excludes) {
            excludesSB.append(exclude.replace(",", "\\,")).append(",");
        }
        if (excludesSB.length() > 1) {
            excludesSB.setLength(excludesSB.length() - 1);
        }
        excludesSB.append("]");

        return String.format(""
                + "Config{\n"
                + "  begintoken=%s,\n"
                + "  endtoken=%s,\n"
                + "  replacetokens=%s,\n"
                + "  folder=%s,\n"
                + "  quiet=%s\n"
                + "  excludes=%s\n"
                + "}", begintoken, endtoken, replacetokensSB, folder, quiet, excludesSB);
    }

}
