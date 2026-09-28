package edu.luc.cs.consoleapp;

import java.util.Scanner;

import org.apache.commons.collections4.queue.CircularFifoQueue;

// see https://stackoverflow.com/questions/1963806/#21699069
// why we're using this implementation instead of java.util.ArrayQueue!

public class MainStream {

  public static final int LAST_N_WORDS = 10;

  public static void main(final String[] args) {
    // perform argument validity checking
    if (args.length > 1) {
      System.err.println("usage: ./target/universal/stage/bin/consoleapp [ last_n_words ]");
      System.exit(2);
    }

    var lastNWords = LAST_N_WORDS;
    try {
      if (args.length == 1) {
        lastNWords = Integer.parseInt(args[0]);
        if (lastNWords < 1) {
          throw new NumberFormatException();
        }
      }
    } catch (final NumberFormatException ex) {
      System.err.println("argument should be a natural number");
      System.exit(4);
    }

    final var input = new Scanner(System.in).useDelimiter("(?U)[^\\p{Alpha}0-9']+");
    final var queue = new CircularFifoQueue<String>(lastNWords);

    input.tokens().takeWhile(word -> !System.out.checkError()).forEach(word -> {
      queue.add(word); // the oldest item automatically gets evicted
      System.out.println(queue);
    });
  }
}
