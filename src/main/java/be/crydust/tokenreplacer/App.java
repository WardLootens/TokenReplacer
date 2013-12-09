package be.crydust.tokenreplacer;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private static Options getOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "print this message");
        options.addOption("b", "begintoken", true, "begintoken (default @)");
        options.addOption("e", "endtoken", true, "endtoken (default @)");
        options.addOption("f", "folder", true, "folder (default current directory)");
        options.addOption("q", "quiet", false, "quiet mode, do not ask if ok to replace");
        OptionBuilder.withArgName("key=value");
        OptionBuilder.hasArgs(2);
        OptionBuilder.withValueSeparator('=');
        OptionBuilder.withDescription("key value pairs to replace (required)");
        OptionBuilder.isRequired(true);
        options.addOption(OptionBuilder.create("D"));
        return options;
    }

    private static Config readConfig(String[] args) {
        Config config = null;
        try {
            Options options = getOptions();
            CommandLine commandLine = new BasicParser().parse(options, args);
            if (commandLine.hasOption('h')) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java -jar TokenReplacer.jar", options);
            } else {
                Map<String, String> replacetokens = new HashMap<>();
                if (commandLine.hasOption("D")) {
                    Properties properties = commandLine.getOptionProperties("D");
                    for (String key : properties.stringPropertyNames()) {
                        replacetokens.put(key, properties.getProperty(key));
                    }
                }
                config = new Config(
                        commandLine.getOptionValue('b', "@"),
                        commandLine.getOptionValue('e', "@"),
                        replacetokens,
                        Paths.get(commandLine.getOptionValue('f', System.getProperty("user.dir"))),
                        commandLine.hasOption("q")
                );
            }
        } catch (ParseException ex) {
            LOGGER.error("Parsing failed.", ex);
        }
        return config;
    }

    private static boolean readContinue() {
        System.out.println("Continue [y/N]?");
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.next().trim().equalsIgnoreCase("y");
        }
    }

    public static void main(String[] args) {
        Config config = readConfig(args);
        if (config != null) {
            System.out.print(config.toString());
            if (config.isQuiet() || readContinue()) {
                new Action(config).run();
                System.out.println("Done.");
            } else {
                System.out.println("Canceled.");
            }
        }
    }

}
