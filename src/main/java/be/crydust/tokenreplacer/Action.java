package be.crydust.tokenreplacer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kristof
 */
public class Action implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Action.class);
    private final Config config;

    public Action(Config config) {
        this.config = config;
    }

    @Override
    public void run() {
        try {
            TokenReplacer replacer = new TokenReplacer(config.getBegintoken(), config.getEndtoken(), config.getReplacetokens());
            List<Path> templates = new FilesFinder(config.getFolder(), "*.template").call();
            for (Path template : templates) {
                Path file = FileExtensionUtil.replaceExtension(template, "");
                if (Files.exists(file)) {
                    Path readonlyFile = FileExtensionUtil.replaceExtension(template, ".readonly");
                    if (Files.exists(readonlyFile)) {
                        System.out.printf("Skipping readonly file %s (file exists).%n", file);
                        continue;
                    }
                    String fileContents = new FileReader(file).call();
                    Path backupFile = FileExtensionUtil.replaceExtension(template, ".bak");
                    new FileWriter(fileContents, backupFile).run();
                }
                String templateContents = new FileReader(template).call();
                new FileWriter(replacer.replace(templateContents), file).run();
            }
        } catch (Exception ex) {
            LOGGER.error(null, ex);
        }
    }

}
