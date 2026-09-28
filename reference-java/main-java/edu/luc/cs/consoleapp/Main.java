package edu.luc.cs.consoleapp;

import java.util.Scanner;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

// see https://stackoverflow.com/questions/1963806/#21699069
// why we're using this implementation instead of java.util.ArrayQueue!

public class Main {

  public static final int LAST_N_WORDS = 10;

  public static void main(final String[] args) {

    // Command-line option parsing using Apache Commons CLI
    final Options options = new Options();
    options.addOption("n", "lastNWords", true, "Number of last words to keep (must be a natural number)");
    options.addOption("h", "help", false, "Print this help message");

    final CommandLineParser parser = new DefaultParser();
    final HelpFormatter formatter = new HelpFormatter();
    int lastNWords = LAST_N_WORDS;

    try {
      final CommandLine cmd = parser.parse(options, args);

      if (cmd.hasOption("h")) {
        formatter.printHelp("consoleapp [ last_n_words ]", options);
        System.exit(0);
      }

      final String[] remainingArgs = cmd.getArgs();
      if (cmd.hasOption("n")) {
        if (remainingArgs.length > 0) {
          throw new ParseException("Cannot specify both -n option and positional argument");
        }
        lastNWords = Integer.parseInt(cmd.getOptionValue("n"));
      } else if (remainingArgs.length == 1) {
        lastNWords = Integer.parseInt(remainingArgs[0]);
      } else if (remainingArgs.length > 1) {
        throw new ParseException("Too many arguments provided");
      }

      if (lastNWords < 1) {
        throw new NumberFormatException();
      }
    } catch (final ParseException e) {
      System.err.println("Error parsing command-line arguments: " + e.getMessage());
      formatter.printHelp("consoleapp [ last_n_words ]", options);
      System.exit(2);
    } catch (final NumberFormatException ex) {
      System.err.println("argument should be a natural number");
      System.exit(4);
    }

    final var input = new Scanner(System.in).useDelimiter("(?U)[^\\p{Alpha}0-9']+");
    final var queue = new CircularFifoQueue<String>(lastNWords);

    // this is the core functionality, but it mixes
    // I/O and core processing logic and therefore is hard to test
    input.forEachRemaining(word -> {
      queue.add(word); // the oldest item automatically gets evicted
      System.out.println(queue);
      // terminate on I/O error such as SIGPIPE
      if (System.out.checkError()) {
        System.exit(1);
      }
    });
  }
}
