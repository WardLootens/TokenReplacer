package be.crydust.tokenreplacer;

import java.nio.charset.StandardCharsets;
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
        options.addOption(OptionBuilder.create("D"));
        return options;
    }

    private static Config readConfig(String[] args) {
        Config config = null;
        Options options = getOptions();
        try {
            CommandLine commandLine = new BasicParser().parse(options, args);
            if (commandLine.hasOption('h') || !commandLine.hasOption("D")) {
                printHelp(options);
            } else {
                Map<String, String> replacetokens = new HashMap<>();
                Properties properties = commandLine.getOptionProperties("D");
                for (String key : properties.stringPropertyNames()) {
                    replacetokens.put(key, properties.getProperty(key));
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
            System.out.println(ex.getMessage());
            printHelp(options);
        }
        return config;
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar tokenreplacer.jar", options);
    }

    private static boolean readContinue() {
        System.out.println("Continue [y/N]?");
        try (Scanner scanner = new Scanner(
                System.in,
                StandardCharsets.US_ASCII.name())) {
            return scanner.next().equalsIgnoreCase("y");
        }
    }

    public static void main(String[] args) {
        Config config = readConfig(args);
        if (config != null) {
            System.out.println(config.toString());
            if (config.isQuiet() || readContinue()) {
                new Action(config).run();
                System.out.println("Done.");
            } else {
                System.out.println("Canceled.");
            }
        }
    }

}
