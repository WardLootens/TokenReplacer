package be.crydust.tokenreplacer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
            List<Path> templates = new FilesFinder(config.getFolder(), "**/*.template", config.getExcludes()).call();
            for (Path template : templates) {
                Path file = FileExtensionUtil.replaceExtension(template, "");
                if (Files.exists(file)) {
                    if (Files.isDirectory(file)) {
                        System.out.printf("Skipped %s (there is a directory with the same name)%n", file);
                        continue;
                    }
                    Path readonlyFile = FileExtensionUtil.replaceExtension(template, ".readonly");
                    if (Files.exists(readonlyFile)) {
                        System.out.printf("Skipped %s (readonly)%n", file);
                        continue;
                    }
                    Path backupFile = FileExtensionUtil.replaceExtension(template, ".bak");
                    Files.move(file, backupFile,
                            StandardCopyOption.ATOMIC_MOVE,
                            StandardCopyOption.REPLACE_EXISTING);
                }
                String templateContents = new FileReader(template).call();
                new FileWriter(replacer.replace(templateContents), file).run();
                System.out.printf("Wrote %s%n", file);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            LOGGER.error(null, ex);
        }
    }

}
